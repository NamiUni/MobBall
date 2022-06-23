package com.github.namiuni.mobball.util;

import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class GriefPreventionChecker {

    private static @Nullable GriefPrevention griefPrevention;

    public static boolean hasClaimPermission(Player player, Location targetLocation) {
        try {
            var dataStore = Objects.requireNonNull(griefPrevention()).dataStore;
            var claim = dataStore.getClaimAt(targetLocation, true, null);

            if (claim == null) return false;

            return claim.getOwnerID().equals(player.getUniqueId()) ||
                    claim.hasExplicitPermission(player, ClaimPermission.Build) ||
                    dataStore.getPlayerData(player.getUniqueId()).ignoreClaims;

        } catch (Exception ex) {
            ComponentLogger.logger().error(ex.getMessage());
            return false;
        }
    }

    private static @Nullable GriefPrevention griefPrevention() {
        if (GriefPreventionChecker.griefPrevention == null) {
            griefPrevention = GriefPrevention.instance;
        }

        return griefPrevention;
    }
}
