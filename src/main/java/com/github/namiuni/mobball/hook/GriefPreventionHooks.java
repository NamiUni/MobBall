package com.github.namiuni.mobball.hook;

import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.UUID;

@DefaultQualifier(NonNull.class)
public final class GriefPreventionHooks {

    private GriefPreventionHooks() {
        throw new IllegalStateException("Utility class");
    }

    private static @MonotonicNonNull GriefPrevention griefPrevention = null;

    public static boolean hasClaimPermissionOrIsNotClaim(final UUID uuid, final Location targetLocation, final ClaimPermission claimPermission) {
        try {
            final var dataStore = griefPrevention().dataStore;
            final var claim = dataStore.getClaimAt(targetLocation, true, null);

            if (claim == null) {
                return true;
            }

            return claim.getOwnerID().equals(uuid)
                    || claim.hasExplicitPermission(uuid, claimPermission)
                    || dataStore.getPlayerData(uuid).ignoreClaims;

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
