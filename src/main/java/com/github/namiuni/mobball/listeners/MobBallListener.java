package com.github.namiuni.mobball.listeners;

import com.github.namiuni.mobball.capture.CaptureHandler;
import com.github.namiuni.mobball.wrapper.WrappedSnowBall;
import com.google.inject.Inject;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class MobBallListener implements Listener {

    private final CaptureHandler captureHandler;

    @Inject
    public MobBallListener(
            final CaptureHandler captureHandler
    ) {
        this.captureHandler = captureHandler;
    }

    @EventHandler
    public void onHit(final ProjectileHitEvent event) {
        this.captureHandler.handleProjectile(event.getEntity(), event.getHitEntity());
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Snowball snowball)) {
            return;
        }

        final var wrappedSnowBall = WrappedSnowBall.create(snowball);
        if (wrappedSnowBall.wrappedItem().ballType() != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntity(final PlayerInteractEntityEvent event) {
        this.captureHandler.handleInteractEntity(event.getRightClicked(), event.getPlayer());
    }
}
