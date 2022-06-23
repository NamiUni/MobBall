package com.github.namiuni.mobball.listeners;

import com.google.inject.Inject;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DesignedBallDamageListener implements Listener {

    private final NamespacedKey mobBallKey;

    @Inject
    public DesignedBallDamageListener(final NamespacedKey mobBallKey) {
        this.mobBallKey = mobBallKey;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (this.isDesignedBall(event.getDamager())) {
            event.setCancelled(true);
        }
    }

    private boolean isDesignedBall(Entity entity) {
        if (entity instanceof Snowball snowball) {
            var snowBallItem = snowball.getItem();
            return snowBallItem.getItemMeta().getPersistentDataContainer().has(this.mobBallKey);
        } else {
            return false;
        }
    }
}
