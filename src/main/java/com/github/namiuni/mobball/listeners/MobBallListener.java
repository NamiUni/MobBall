package com.github.namiuni.mobball.listeners;

import com.github.namiuni.mobball.capture.CaptureHandler;
import com.github.namiuni.mobball.config.ConfigManager;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractEntity(final PlayerInteractEntityEvent event) {
        this.captureHandler.handleInteractEvent(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHit(final ProjectileHitEvent event) {
        this.captureHandler.handleHitEvent(event);
    }

    @EventHandler
    public void onDamageByEntity(final EntityDamageByEntityEvent event) {
        this.captureHandler.handleDamageEvent(event);
    }
}
