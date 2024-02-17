package com.github.namiuni.mobball.capture;

import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.config.ConfigManager;
import com.github.namiuni.mobball.hook.GriefPreventionHooks;
import com.github.namiuni.mobball.wrapper.WrappedEntity;
import com.github.namiuni.mobball.wrapper.WrappedSnowBall;
import com.google.inject.Inject;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class CaptureHandler {

    private final ConfigManager configManager;

    @Inject
    public CaptureHandler(final ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void handleInteractEvent(final PlayerInteractEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!event.getPlayer().isSneaking()) {
            return;
        }

        final var playerInventory = event.getPlayer().getInventory();
        if (playerInventory.getItemInMainHand().isEmpty() || playerInventory.getItemInOffHand().isEmpty()) {
            return;
        }

        final var wrappedRightClicked = WrappedEntity.create(event.getRightClicked());
        if (event.getPlayer().getUniqueId().equals(wrappedRightClicked.ownerUUID()) && wrappedRightClicked.ballType() != null) {
            final var ballItem = wrappedRightClicked.ballType();
            final var clickedEntity = wrappedRightClicked.entity();

            ballItem.entity(wrappedRightClicked);
            clickedEntity.getWorld().dropItem(clickedEntity.getLocation(), ballItem.itemStack());
            clickedEntity.remove();
        }
    }

    public void handleDamageEvent(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Snowball snowball)) {
            return;
        }

        final var wrappedSnowBall = WrappedSnowBall.create(snowball);
        if (wrappedSnowBall.wrappedItem().ballType() != null) {
            if (!wrappedSnowBall.wrappedItem().hasEntity()) {
                final var wrappedHitEntity = WrappedEntity.create(event.getEntity());
                this.tryCapture(wrappedSnowBall, wrappedHitEntity, event.isCancelled());
            }

            event.setCancelled(true);
        }
    }

    public void handleHitEvent(final ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball snowball)) {
            return;
        }

        final var wrappedSnowBall = WrappedSnowBall.create(snowball);
        if (wrappedSnowBall.wrappedItem().ballType() != null && wrappedSnowBall.wrappedItem().hasEntity()) {
            if (event.getHitEntity() != null) {
                event.setCancelled(true);
            } else if (event.getHitBlock() != null) {
                this.trySummon(wrappedSnowBall, event.isCancelled());
            }
        }
    }

    private void tryCapture(final WrappedSnowBall wrappedSnowBall, final WrappedEntity targetEntity, final boolean eventCanceled) {
        if (eventCanceled) {
            return;
        }

        if (!configManager.primaryConfig().capturableMOBs().contains(targetEntity.entity().getType())) {
            return;
        }

        if (!(wrappedSnowBall.snowball().getShooter() instanceof Player player)) {
            return;
        }

        if (!targetEntity.checkTamable(player.getUniqueId())) {
            return;
        }

        if (targetEntity.ownerUUID() == null && targetEntity.ballType() == null) {
            targetEntity.ballType(Objects.requireNonNull(wrappedSnowBall.wrappedItem().ballType()));
            targetEntity.ownerUUID(player.getUniqueId());

            final var wrappedItem = wrappedSnowBall.wrappedItem();
            wrappedItem.entity(targetEntity);

            final var location = wrappedSnowBall.snowball().getLocation();
            wrappedSnowBall.snowball().getWorld().dropItem(location, wrappedItem.itemStack());
            targetEntity.entity().remove();
        }
    }

    private void trySummon(final WrappedSnowBall wrappedSnowBall, final boolean eventCanceled) {
        if (eventCanceled) {
            this.dropBall(wrappedSnowBall.snowball());
            return;
        }

        if (wrappedSnowBall.snowball().getOwnerUniqueId() == null) {
            this.dropBall(wrappedSnowBall.snowball());
            return;
        }

        if (MobBall.gpLoaded()
                && this.configManager.primaryConfig().integration().griefPrevention().enable()
                && !GriefPreventionHooks.hasClaimPermissionOrIsNotClaim(wrappedSnowBall.snowball().getOwnerUniqueId(), wrappedSnowBall.snowball().getLocation(), ClaimPermission.Inventory)) {
            this.dropBall(wrappedSnowBall.snowball());
            return;
        }

        final var capturedEntity = Objects.requireNonNull(wrappedSnowBall.wrappedItem().entity());
        capturedEntity.spawnAt(wrappedSnowBall.snowball().getLocation());
    }

    private void dropBall(final Snowball snowball) {
        snowball.getWorld().dropItem(snowball.getLocation(), snowball.getItem());
    }
}
