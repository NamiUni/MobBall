package com.github.namiuni.mobball.listeners;

import com.github.namiuni.mobball.config.ConfigManager;
import com.github.namiuni.mobball.wrapper.WrappedEntity;
import com.google.inject.Inject;
import me.ryanhamshire.GriefPrevention.events.ClaimPermissionCheckEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class GriefPreventionListener implements Listener {

    private final ConfigManager configManager;

    @Inject
    public GriefPreventionListener(final ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onRejectedClaim(final ClaimPermissionCheckEvent event) {
        if (event.getDenialReason() == null) {
            return;
        }

        if (!this.configManager.primaryConfig().integration().griefPrevention().enable()) {
            return;
        }

        final WrappedEntity wrappedEntity;
        if (event.getTriggeringEvent() instanceof final PlayerInteractEntityEvent interactEntityEvent) {
            wrappedEntity = WrappedEntity.create(interactEntityEvent.getRightClicked());
        } else if (event.getTriggeringEvent() instanceof final EntityEvent entityEvent) {
            wrappedEntity = WrappedEntity.create(entityEvent.getEntity());
        } else {
            return;
        }

        if (event.getCheckedUUID().equals(wrappedEntity.ownerUUID())) {
            event.setDenialReason(null);
        }
    }
}
