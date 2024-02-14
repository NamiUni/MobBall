package com.github.namiuni.mobball.capture;

import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.config.ConfigManager;
import com.github.namiuni.mobball.hook.GriefPreventionHooks;
import com.github.namiuni.mobball.wrapper.WrappedEntity;
import com.github.namiuni.mobball.wrapper.WrappedSnowBall;
import com.google.inject.Inject;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.projectiles.ProjectileSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class CaptureHandler {

    private final ConfigManager configManager;

    @Inject
    public CaptureHandler(final ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void handleInteractEntity(final Entity clickedEntity, final Player player) {
        if (!player.isSneaking()) {
            return;
        }

        final var wrappedEntity = WrappedEntity.create(clickedEntity);

        if (wrappedEntity.wrappedItem() == null) {
            return;
        }

        if (!player.getUniqueId().equals(wrappedEntity.ownerUUID())) {
            return;
        }

        final var ballItem = wrappedEntity.wrappedItem();
        ballItem.entity(wrappedEntity);
        clickedEntity.getWorld().dropItem(clickedEntity.getLocation(), ballItem.itemStack());
        clickedEntity.remove();
    }

    public void handleProjectile(final Projectile projectile, final @Nullable Entity hitEntity) {

        if (!(projectile instanceof Snowball snowball)) {
            return;
        }

        final var wrappedSnowBall = WrappedSnowBall.create(snowball);
        final @Nullable WrappedEntity wrappedHitEntity = hitEntity != null
                ? WrappedEntity.create(hitEntity)
                : null;

        if (wrappedSnowBall.wrappedItem().ballType() == null) {
            return;
        }

        if (wrappedSnowBall.wrappedItem().hasEntity()) {
            this.handleExistBall(wrappedSnowBall);
        } else if (wrappedHitEntity != null){
            this.handleEmptyBall(wrappedSnowBall, wrappedHitEntity);
        }
    }

    private void handleExistBall(final WrappedSnowBall wrappedSnowBall) {
        final var capturedEntity = Objects.requireNonNull(wrappedSnowBall.wrappedItem().entity());
        final @Nullable ProjectileSource shooter = wrappedSnowBall.snowball().getShooter();
        if (shooter instanceof Player player) {
            final var hitLocation = wrappedSnowBall.snowball().getLocation();
            if (MobBall.gpLoaded() && GriefPreventionHooks.isClaim(hitLocation) && !GriefPreventionHooks.hasClaimPermission(player, hitLocation)) {
                capturedEntity.remove();
                this.dropBall(wrappedSnowBall.snowball());
                return;
            }
            capturedEntity.spawnAt(wrappedSnowBall.snowball().getLocation());

        } else {
            capturedEntity.remove();
            this.dropBall(wrappedSnowBall.snowball());
        }
    }

    private void handleEmptyBall(final WrappedSnowBall wrappedSnowBall, final WrappedEntity wrappedEntity) {
        if (!configManager.primaryConfig().capturableMOBs().contains(wrappedEntity.entity().getType())) {
            return;
        }

        if (!(wrappedSnowBall.snowball().getShooter() instanceof Player player)) {
            return;
        }

        if (!wrappedEntity.checkTamable(player.getUniqueId())) {
            return;
        }

        if (MobBall.gpLoaded() && GriefPreventionHooks.isClaim(wrappedEntity.entity().getLocation()) && !GriefPreventionHooks.hasClaimPermission(player, wrappedEntity.entity().getLocation())) {
            return;
        }

        if (wrappedEntity.ownerUUID() != null && wrappedEntity.wrappedItem() != null) {
            return;
        }

        wrappedEntity.wrappedItem(Objects.requireNonNull(wrappedSnowBall.wrappedItem().ballType()));
        wrappedEntity.ownerUUID(player.getUniqueId());

        final var wrappedItem = wrappedSnowBall.wrappedItem();
        wrappedItem.entity(wrappedEntity);
        final var location = wrappedSnowBall.snowball().getLocation();
        wrappedSnowBall.snowball().getWorld().dropItem(location, wrappedItem.itemStack());
        wrappedEntity.entity().remove();
    }

    private void dropBall(final Snowball snowball) {
        snowball.getWorld().dropItem(snowball.getLocation(), snowball.getItem());
    }
}
