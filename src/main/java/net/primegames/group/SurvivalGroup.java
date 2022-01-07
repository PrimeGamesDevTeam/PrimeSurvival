package net.primegames.group;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.primegames.JavaCore;
import net.primegames.JavaSurvival;
import net.primegames.groups.GroupTier;
import net.primegames.utils.CoreLogger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public enum SurvivalGroup {

    OWNER("owner", GroupTier.OWNER),
    CHIEF("chief", GroupTier.CHIEF),
    LOS("los", GroupTier.LOS),
    ADMIN("admin", GroupTier.ADMIN),
    MODERATOR("moderator", GroupTier.MOD),
    HELPER("helper", GroupTier.TRAINEE),
    DEFAULT("default", GroupTier.TIER_0),
    VOTER("voter", GroupTier.TIER_1),
    VIP("vip", GroupTier.TIER_2),
    VIP_P("vip+", GroupTier.TIER_3),
    VIP_PP("vip++", GroupTier.TIER_4),
    BUILDER("builder", GroupTier.TIER_5),
    MVP("mvp", GroupTier.TIER_6),
    LEGEND("legend", GroupTier.TIER_8),
    IMMORTAL("immortal", GroupTier.TIER_9);

    @Getter
    private final String name;
    @Getter
    private final GroupTier tier;
    @Getter
    @NonNull
    private final Group group;

    SurvivalGroup(String name, GroupTier groupTier) {
        this.tier = groupTier;
        this.name = name;
        Group group = JavaCore.getInstance().getLuckPerms().getGroupManager().getGroup(name);
        if (group != null) {
            this.group = group;
        } else {
            throw new IllegalArgumentException("Group named: " + name + " does not exist in LuckPerms!");
        }
    }

    public static List<@NonNull SurvivalGroup> fromTiers(List<GroupTier> tiers) {
        List<@NonNull SurvivalGroup> groups = new ArrayList<>();
        SurvivalGroup[] survivalGroups = SurvivalGroup.values();
        for (SurvivalGroup survivalGroup : survivalGroups) {
            for (GroupTier tier : tiers) {
                if (survivalGroup.getTier().equals(tier)) {
                    groups.add(survivalGroup);
                }
            }
        }
        return groups;
    }

    public static SurvivalGroup fromTier(GroupTier groupTier) {
        SurvivalGroup[] groups = SurvivalGroup.values();
        for (SurvivalGroup group : groups) {
            if (group.getTier().equals(groupTier)) {
                return group;
            }
        }
        return null;
    }

    public static SurvivalGroup fromTierId(int id) {
        SurvivalGroup[] groups = SurvivalGroup.values();
        for (SurvivalGroup group : groups) {
            if (group.getTier().getId() == id) {
                return group;
            }
        }
        return null;
    }

    public static SurvivalGroup getHighestPriority(List<SurvivalGroup> groups) {
        SurvivalGroup highestPriority = SurvivalGroup.DEFAULT;
        for (SurvivalGroup survivalGroup : groups) {
            if (highestPriority.getTier().getPriority() < survivalGroup.getTier().getPriority()) {
                highestPriority = survivalGroup;
            }
        }
        return highestPriority;
    }

    public static void addGroupsFromTiers(@NonNull Player player, List<@NonNull GroupTier> tiers) {
        List<SurvivalGroup> survivalGroups = fromTiers(tiers);
        CoreLogger.info(ChatColor.YELLOW + "Loading survival groups for " + player.getName());
        LuckPerms luckPerms = JavaSurvival.getInstance().getLuckPerms();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            user.getNodes().clear();
            for (SurvivalGroup survivalGroup : survivalGroups) {
                InheritanceNode node = InheritanceNode.builder(survivalGroup.getGroup().getName()).value(true).build();
                if (user.data().add(node) == DataMutateResult.SUCCESS) {
                    CoreLogger.debug("Added survival group " + survivalGroup.getGroup().getName() + " to " + player.getName());
                } else {
                    CoreLogger.error("Failed to add survival group " + survivalGroup.getGroup().getName() + " to " + player.getName());
                }
            }
            SurvivalGroup finalGroup = SurvivalGroup.getHighestPriority(survivalGroups);
            if (user.setPrimaryGroup(finalGroup.getGroup().getName()) == DataMutateResult.SUCCESS) {
                CoreLogger.success("Set " + player.getName() + "'s primary group to " + finalGroup.getGroup().getName());
            } else {
                CoreLogger.error("Failed to set " + player.getName() + "'s primary group to " + finalGroup.getGroup().getName());
            }
            luckPerms.getUserManager().saveUser(user);
        } else {
            CoreLogger.error("Failed to get LuckPerms User for " + player.getName());
        }
    }

}
