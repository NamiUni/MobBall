package com.github.namiuni.mobball.wrapper;

import com.github.namiuni.mobball.MobBallProvider;
import com.github.namiuni.mobball.util.NamespacedKeyUtils;
import com.github.namiuni.mobball.util.UUIDDataType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Tameable;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;
import java.util.UUID;

@DefaultQualifier(NonNull.class)
public final class WrappedEntity {

    private final Entity entity;
    private final @Nullable WrappedItem wrappedItem;

    private WrappedEntity(final Entity entity) {
        this.entity = entity;

        if (entity.getPersistentDataContainer().has(NamespacedKeyUtils.ballTypeKey(), PersistentDataType.STRING)) {
            final var ballType = Objects.requireNonNull(entity.getPersistentDataContainer().get(NamespacedKeyUtils.ballTypeKey(), PersistentDataType.STRING));
            this.wrappedItem = MobBallProvider.mobBall().ballRegistry().get(ballType);
        } else {
            this.wrappedItem = null;
        }
    }

    public static WrappedEntity create(final Entity entity) {
        return new WrappedEntity(entity);
    }

    public Entity entity() {
        return this.entity;
    }

    public boolean checkTamable(UUID playerUUID) {
        if (!(this.entity instanceof Tameable tameable)) {
            return true;
        }

        if (!tameable.isTamed()) {
            return true;
        }

        if (tameable.getOwner() == null) {
            return true;
        }

        return tameable.getOwner().getUniqueId().equals(playerUUID);
    }

    public @Nullable UUID ownerUUID() {
        return this.entity.getPersistentDataContainer().has(NamespacedKeyUtils.ownerUuidKey(), UUIDDataType.INSTANCE)
                ? this.entity.getPersistentDataContainer().get(NamespacedKeyUtils.ownerUuidKey(), UUIDDataType.INSTANCE)
                : null;
    }

    public void ownerUUID(final UUID ownerUUID) {
        this.entity.getPersistentDataContainer().set(NamespacedKeyUtils.ownerUuidKey(), UUIDDataType.INSTANCE, ownerUUID);
    }

    public @Nullable WrappedItem wrappedItem() {
        return this.wrappedItem;
    }

    public void wrappedItem(final String ballType) {
        this.entity.getPersistentDataContainer().set(NamespacedKeyUtils.ballTypeKey(), PersistentDataType.STRING, ballType);
    }
}
