package com.github.namiuni.mobball;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

@DefaultQualifier(NonNull.class)
public final class MobBallBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(final BootstrapContext context) {
        // none
    }

    @Override
    public @NotNull JavaPlugin createPlugin(final PluginProviderContext context) {
        return new MobBall();
    }
}
