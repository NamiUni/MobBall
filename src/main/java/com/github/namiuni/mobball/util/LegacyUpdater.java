package com.github.namiuni.mobball.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class LegacyUpdater {

    private static final NamespacedKey MOB_BALL_KEY = new NamespacedKey("mobball", "mobball");

    private LegacyUpdater() {
        throw new IllegalStateException("Utility class");
    }

    public static void updateIfNeeded(final ItemStack itemStack) {
        if (itemStack.getType() == Material.SNOWBALL) {
            itemStack.editMeta(itemMeta -> {
                final var container = itemMeta.getPersistentDataContainer();
                if (!container.has(MOB_BALL_KEY)) {
                    return;
                }

                if (container.has(MOB_BALL_KEY, PersistentDataType.BYTE_ARRAY)) {
                    final var rawEntity = Objects.requireNonNull(container.get(MOB_BALL_KEY, PersistentDataType.BYTE_ARRAY));
                    container.set(NamespacedKeyUtils.rawEntityKey(), PersistentDataType.BYTE_ARRAY, rawEntity);
                }

                container.set(NamespacedKeyUtils.ballTypeKey(), PersistentDataType.STRING, "moripa");
                container.remove(MOB_BALL_KEY);
            });
        }
    }
}
