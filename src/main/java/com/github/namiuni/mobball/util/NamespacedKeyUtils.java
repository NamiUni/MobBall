package com.github.namiuni.mobball.util;

import org.bukkit.NamespacedKey;

public final class NamespacedKeyUtils {

    private NamespacedKeyUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String NAMESPACE = "mob_ball";
    private static final NamespacedKey BALL_TYPE_KEY = new NamespacedKey(NAMESPACE, "ball_type");
    private static final NamespacedKey RAW_ENTITY_KEY = new NamespacedKey(NAMESPACE, "mob_data");
    private static final NamespacedKey OWNER_UUID_KEY = new NamespacedKey(NAMESPACE, "owner_uuid");

    public static NamespacedKey ballTypeKey() {
        return BALL_TYPE_KEY;
    }

    public static NamespacedKey rawEntityKey() {
        return RAW_ENTITY_KEY;
    }

    public static NamespacedKey ownerUuidKey() {
        return OWNER_UUID_KEY;
    }
}
