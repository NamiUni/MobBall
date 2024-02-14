package com.github.namiuni.mobball.hook;

import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class GriefPreventionHooks {

    private GriefPreventionHooks() {
        throw new IllegalStateException("Utility class");
    }

    private static @MonotonicNonNull GriefPrevention griefPrevention = null;

    public static boolean isClaim(final Location targetLocation) {
        final var dataStore = griefPrevention().dataStore;
        final var claim = dataStore.getClaimAt(targetLocation, true, null);

        return claim != null;
    }

    public static boolean hasClaimPermission(final Player player, final Location targetLocation) {
        try {
            final var dataStore = griefPrevention().dataStore;
            final var claim = dataStore.getClaimAt(targetLocation, true, null);

            return claim.getOwnerID().equals(player.getUniqueId()) ||
                    claim.hasExplicitPermission(player, ClaimPermission.Build) ||
                    dataStore.getPlayerData(player.getUniqueId()).ignoreClaims;

        } catch (Exception ex) {
            ComponentLogger.logger().error(ex.getMessage());
            return false;
        }
    }

    private static GriefPrevention griefPrevention() {

        if (GriefPreventionHooks.griefPrevention == null) {
            griefPrevention = GriefPrevention.instance;
        }

        return griefPrevention;
    }
}
