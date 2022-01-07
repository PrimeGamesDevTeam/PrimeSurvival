package net.primegames.listener;

import de.flo56958.minetinker.MineTinker;
import de.flo56958.minetinker.data.Lists;
import de.flo56958.minetinker.events.ToolUpgradeEvent;
import de.flo56958.minetinker.modifiers.ModManager;
import de.flo56958.minetinker.modifiers.Modifier;
import de.flo56958.minetinker.utils.datatypes.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class MineTickerListener implements Listener {

    private static final ModManager modManager = ModManager.instance();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        final HumanEntity he = event.getWhoClicked();

        if (!(he instanceof Player player && event.getClickedInventory() instanceof PlayerInventory inv)) {
            return;
        }

        final ItemStack item1 = event.getCurrentItem();
        final ItemStack item2 = event.getCursor();

        if (item1 == null || item2 == null || item2.getType().equals(Material.AIR) || item1.getType().equals(Material.AIR)) {
            return;
        }

        if (Lists.WORLDS.contains(player.getWorld().getName())) {
            return;
        }

        if (!(modManager.isToolViable(item1) || modManager.isArmorViable(item1))) {
            return;
        }

        final Modifier mod = modManager.getModifierFromItem(item2);

        ItemStack newTool = null;

        if (mod != null) {
            newTool = item1.clone();
            if (!modManager.addMod(player, newTool, mod, false, false, true, true)) {
                return;
            }
        } else if (item1.getType().equals(item2.getType())) { //Whether we're combining the tools
            if ((modManager.isToolViable(item2) || modManager.isArmorViable(item2))
                    && MineTinker.getPlugin().getConfig().getBoolean("Combinable")
                    && player.hasPermission("minetinker.tool.combine")) {
                newTool = item1.clone();

                for (Modifier tool2Mod : modManager.getToolMods(item2)) {
                    int modLevel = modManager.getModLevel(item2, tool2Mod);
                    for (int i = 0; i < modLevel; i++) {
                        modManager.addMod(player, newTool, tool2Mod, false, false, true, false);
                    }
                }

                modManager.addExp(player, newTool, modManager.getExp(item2), false);
            }
        } else {
            if (MineTinker.getPlugin().getConfig().getBoolean("Upgradeable")
                    && player.hasPermission("minetinker.tool.upgrade")) {

                    final Pair<Material, Integer> materialIntegerPair = ModManager.itemUpgrader(item1.getType(), item2.getType());
                    if (materialIntegerPair != null && materialIntegerPair.x() != null) {
                        if (materialIntegerPair.y() != null && item2.getAmount() == materialIntegerPair.y()) {
                            newTool = item1.clone();
                            newTool.setType(materialIntegerPair.x());
                            modManager.addArmorAttributes(newTool); //The Attributes need to be reapplied
                            final ItemMeta meta = newTool.getItemMeta();
                            if (meta instanceof Damageable) {
                                ((Damageable) meta).setDamage(0);
                            }
                            newTool.setItemMeta(meta);
                        }
                    } else {
                        Bukkit.getPluginManager().callEvent(new ToolUpgradeEvent(player, item1, false));
                    }
            }
        }

        if (newTool != null) {
            inv.clear(event.getSlot());
            player.setItemOnCursor(newTool);
        }

    }

}
