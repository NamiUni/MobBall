package com.github.namiuni.mobball.listeners;

import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.config.ConfigFactory;
import com.github.namiuni.mobball.util.GriefPreventionChecker;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;
import java.util.Objects;

@DefaultQualifier(NonNull.class)
public class EmptyBallHitListener extends BallHitListener implements Listener {

    @Inject
    public EmptyBallHitListener(
            final MobBall mobBall,
            final NamespacedKey mobBallKey,
            final ConfigFactory configFactory
    ) {
        super(mobBall, mobBallKey, configFactory);
    }

    @Override
    @EventHandler
    public void onBallHit(final @NonNull ProjectileHitEvent event) {
        var projectile = event.getEntity();
        var targetEntity = event.getHitEntity();

        if (this.isDesignedBall(projectile, PersistentDataType.STRING)) {

            var projectileBall = (Snowball) projectile;

            if (this.canCatchMob(projectile.getShooter(), targetEntity)) {

                this.catchMob(projectileBall, targetEntity);

            } else {

                this.dropBall(projectileBall);
            }
        }
    }

    private void catchMob(Snowball projectileBall, Entity targetMob) {

        var ballItem = projectileBall.getItem();
        var rawMobData = Bukkit.getUnsafe().serializeEntity(targetMob);

        ballItem.editMeta(itemMeta -> {
            itemMeta.getPersistentDataContainer().set(this.mobBallKey(), PersistentDataType.BYTE_ARRAY, rawMobData);

            var mobName = targetMob.customName() != null ? targetMob.customName() : targetMob.name();

            // フォーマットをConfigで設定できるようにするかも？
            // TODO: 体力やスピードなども見られるようにする
            var description = Component.text()
                    .append(Component.text("名前: ")
                            .color(NamedTextColor.WHITE)
                            .decorate(TextDecoration.BOLD))
                    .append(mobName.color(NamedTextColor.YELLOW))
                    .decoration(TextDecoration.ITALIC, false)
                    .build();
            itemMeta.lore(List.of(description));
        });

        targetMob.getWorld().dropItem(targetMob.getLocation(), ballItem);
        targetMob.remove();
    }

    private boolean canCatchMob(ProjectileSource shooter, @Nullable Entity targetEntity) {
        return shooter instanceof Player player &&
                this.isObtainableMob(targetEntity, player) &&
                this.isGpChecked(player, targetEntity.getLocation());
    }

    private boolean isObtainableMob(@Nullable Entity targetEntity, Player player) {
        if (targetEntity == null) return false;
        if (!this.configFactory().primaryConfig().allowedMOBs().contains(targetEntity.getType())) return false;

        if (targetEntity instanceof Tameable tameable && tameable.isTamed()) {
            var owner = Objects.requireNonNull(tameable.getOwner());
            return owner.equals(player);
        } else {
            return true;
        }
    }
}
