package com.github.namiuni.mobball.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.ByteBuffer;
import java.util.UUID;

@DefaultQualifier(NonNull.class)
public final class UUIDDataType implements PersistentDataType<byte[], UUID> {

    public static final UUIDDataType INSTANCE = new UUIDDataType();

    private UUIDDataType() {
        // none
    }

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public byte[] toPrimitive(final UUID complex, final PersistentDataAdapterContext context) {
        final var wrapped = ByteBuffer.wrap(new byte[16]);
        wrapped.putLong(complex.getMostSignificantBits());
        wrapped.putLong(complex.getLeastSignificantBits());
        return wrapped.array();
    }

    @Override
    public UUID fromPrimitive(final byte[] primitive, final PersistentDataAdapterContext context) {
        final var bb = ByteBuffer.wrap(primitive);
        final long firstLong = bb.getLong();
        final long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }
}
