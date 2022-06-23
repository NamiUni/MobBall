package com.github.namiuni.mobball.listeners;

import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.config.ConfigFactory;
import com.github.namiuni.mobball.util.GriefPreventionChecker;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;

public abstract class BallHitListener {

    private final MobBall mobBall;
    private final NamespacedKey mobBallKey;
    private final ConfigFactory configFactory;

    public BallHitListener(
            final MobBall mobBall,
            final NamespacedKey mobBallKey,
            final ConfigFactory configFactory
    ) {
        this.mobBall = mobBall;
        this.mobBallKey = mobBallKey;
        this.configFactory = configFactory;
    }

    public abstract void onBallHit(ProjectileHitEvent event);

    void dropBall(Snowball projectileBall) {
        projectileBall.getWorld().dropItem(projectileBall.getLocation(), projectileBall.getItem());
    }

    boolean isDesignedBall(Entity projectile, PersistentDataType type) {
        if (projectile instanceof Snowball snowball) {
            return snowball.getItem().getItemMeta().getPersistentDataContainer().has(this.mobBallKey(), type);
        } else {
            return false;
        }
    }

    boolean isGpChecked(Player player, Location targetLocation) {
        var gpHookEnabled = this.configFactory().primaryConfig().pluginHooks().griefPrevention();
        if (this.mobBall().gpLoaded() && gpHookEnabled) {
            return GriefPreventionChecker.hasClaimPermission(player, targetLocation);
        } else {
            return true;
        }
    }

    public MobBall mobBall() {
        return this.mobBall;
    }

    public NamespacedKey mobBallKey() {
        return mobBallKey;
    }

    public ConfigFactory configFactory() {
        return this.configFactory;
    }
}
