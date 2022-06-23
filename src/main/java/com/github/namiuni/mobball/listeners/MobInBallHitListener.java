package com.github.namiuni.mobball.listeners;

import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.config.ConfigFactory;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class MobInBallHitListener extends BallHitListener implements Listener {

    @Inject
    public MobInBallHitListener(
            final MobBall mobBall,
            final NamespacedKey mobBallKey,
            final ConfigFactory configFactory
    ) {
        super(mobBall, mobBallKey, configFactory);
    }

    @EventHandler
    public void onBallHit(ProjectileHitEvent event) {
        if (this.isDesignedBall(event.getEntity(), PersistentDataType.BYTE_ARRAY)) {

            var projectileBallWithMob = (Snowball) event.getEntity();

            if (canSentOutMob(event.getEntity())) {

                this.sentOutMob(projectileBallWithMob);

            } else {

                this.dropBall(projectileBallWithMob);

            }
        }
    }

    private void sentOutMob(Snowball projectileBallWithMob) {
        var ballItem = projectileBallWithMob.getItem();
        var mobData = Objects.requireNonNull(ballItem.getItemMeta().getPersistentDataContainer().get(this.mobBallKey(), PersistentDataType.BYTE_ARRAY));
        var entity = Bukkit.getUnsafe().deserializeEntity(mobData, projectileBallWithMob.getWorld());

        entity.spawnAt(projectileBallWithMob.getLocation());
    }

    private boolean canSentOutMob(Projectile projectile) {
        return projectile.getShooter() instanceof Player player &&
                this.isGpChecked(player, projectile.getLocation());
    }
}
