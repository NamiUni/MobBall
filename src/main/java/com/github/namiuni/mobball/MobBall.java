package com.github.namiuni.mobball;

import com.github.namiuni.mobball.capture.BallRegistry;
import com.github.namiuni.mobball.command.MobBallCommand;
import com.github.namiuni.mobball.command.commands.GiveCommand;
import com.github.namiuni.mobball.command.commands.ListCommand;
import com.github.namiuni.mobball.command.commands.ReloadCommand;
import com.github.namiuni.mobball.listeners.MobBallListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;
import java.util.Set;

@Singleton
@DefaultQualifier(NonNull.class)
public final class MobBall extends JavaPlugin {

    private static final Set<Class<? extends Listener>> LISTENER_CLASSES = Set.of(
            MobBallListener.class
    );
    private static final Set<Class<? extends MobBallCommand>> COMMAND_CLASSES = Set.of(
            ReloadCommand.class, GiveCommand.class, ListCommand.class
    );

    private @MonotonicNonNull Injector injector;

    @Override
    public void onLoad() {
        this.injector = Guice.createInjector(new MobBallModule(this, this.dataDirectory()));
        MobBallProvider.register(this);
    }

    @Override
    public void onEnable() {

        // Listeners
        for (final Class<? extends Listener> listenerClass : LISTENER_CLASSES) {
            var listener = this.injector.getInstance(listenerClass);
            this.getServer().getPluginManager().registerEvents(listener, this);
        }

        // Commands
        for (final Class<? extends MobBallCommand> commandClass : COMMAND_CLASSES) {
            var command = this.injector.getInstance(commandClass);
            command.init();
        }
    }

    public Path dataDirectory() {
        return this.getDataFolder().toPath();
    }

    public BallRegistry ballRegistry() {
        return this.injector.getInstance(BallRegistry.class);
    }

    public static boolean gpLoaded() {
        return Bukkit.getPluginManager().isPluginEnabled("GriefPrevention");
    }
}
