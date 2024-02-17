package com.github.namiuni.mobball.listeners;

import com.github.namiuni.mobball.capture.CaptureHandler;
import com.github.namiuni.mobball.config.ConfigManager;
import com.github.namiuni.mobball.wrapper.WrappedEntity;
import com.google.inject.Inject;
import me.ryanhamshire.GriefPrevention.events.ClaimPermissionCheckEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class MobBallListener implements Listener {

    private final CaptureHandler captureHandler;
    private final ConfigManager configManager;

    @Inject
    public MobBallListener(
            final CaptureHandler captureHandler,
            final ConfigManager configManager
    ) {
        this.captureHandler = captureHandler;
        this.configManager = configManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onInteractEntity(final PlayerInteractEntityEvent event) {
        this.captureHandler.handleInteractEvent(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onHit(final ProjectileHitEvent event) {
        this.captureHandler.handleHitEvent(event);
    }

    @EventHandler
    public void onDamageByEntity(final EntityDamageByEntityEvent event) {
        this.captureHandler.handleDamageEvent(event);
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
