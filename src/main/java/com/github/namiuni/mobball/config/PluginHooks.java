package com.github.namiuni.mobball.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class PluginHooks {

    private boolean griefPrevention = false;

    public boolean griefPrevention() {
        return this.griefPrevention;
    }
}
