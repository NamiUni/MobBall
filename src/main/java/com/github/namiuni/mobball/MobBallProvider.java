package com.github.namiuni.mobball;

import org.checkerframework.checker.nullness.qual.Nullable;

public final class MobBallProvider {

    private static @Nullable MobBall instance;

    private MobBallProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static void register(final MobBall mobBall) {
        MobBallProvider.instance = mobBall;
    }

    public static MobBall mobBall() {
        if (MobBallProvider.instance == null) {
            throw new IllegalStateException("MobBall not initialized!");
        }

        return MobBallProvider.instance;
    }
}
