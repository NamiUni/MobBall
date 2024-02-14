package com.github.namiuni.mobball.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@DefaultQualifier(NonNull.class)
public final class Integration {

    private GriefPrevention griefPrevention = new GriefPrevention(true);

    public GriefPrevention griefPrevention() {
        return this.griefPrevention;
    }

    @ConfigSerializable
    public record GriefPrevention(boolean enable) {
    }
}
