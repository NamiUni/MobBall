package com.github.namiuni.mobball.wrapper;

import org.bukkit.entity.Snowball;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class WrappedSnowBall {

    private final Snowball snowball;
    private final WrappedItem wrappedItem;

    private WrappedSnowBall(final Snowball snowball) {
        this.snowball = snowball;
        final var ballItem = snowball.getItem();
        this.wrappedItem = WrappedItem.create(ballItem);
    }

    public static WrappedSnowBall create(final Snowball snowball) {
        return new WrappedSnowBall(snowball);
    }

    public Snowball snowball() {
        return this.snowball;
    }

    public WrappedItem wrappedItem() {
        return this.wrappedItem;
    }
}
