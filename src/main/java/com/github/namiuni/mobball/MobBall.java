package com.github.namiuni.mobball;

import com.github.namiuni.mobball.command.MobBallCommand;
import com.github.namiuni.mobball.command.commands.GiveCommand;
import com.github.namiuni.mobball.command.commands.ListCommand;
import com.github.namiuni.mobball.command.commands.ReloadCommand;
import com.github.namiuni.mobball.event.MobBallEventHandler;
import com.github.namiuni.mobball.listeners.DesignedBallDamageListener;
import com.github.namiuni.mobball.listeners.EmptyBallHitListener;
import com.github.namiuni.mobball.listeners.MobInBallHitListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import io.papermc.lib.PaperLib;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Level;

@Singleton
@DefaultQualifier(NonNull.class)
public class MobBall extends JavaPlugin {

    private static final Set<Class<? extends Listener>> LISTENER_CLASSES = Set.of(
            EmptyBallHitListener.class, MobInBallHitListener.class, DesignedBallDamageListener.class
    );
    private static final Set<Class<? extends MobBallCommand>> COMMAND_CLASSES = Set.of(
            ReloadCommand.class, GiveCommand.class, ListCommand.class
    );
    private final MobBallEventHandler eventHandler = new MobBallEventHandler();
    private @MonotonicNonNull Injector injector;
    private @MonotonicNonNull Logger logger;

    @Override
    public void onLoad() {
        if (!PaperLib.isPaper()) {
            this.getLogger().log(Level.SEVERE, "* MobBallはPaperAPIを使って書かれています。");
            this.getLogger().log(Level.SEVERE, "* サーバーをPaperにアップグレードしてください。");
            PaperLib.suggestPaper(this, Level.SEVERE);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.injector = Guice.createInjector(new MobBallModule(this, this.dataDirectory()));
        this.logger = LogManager.getLogger("MobBall");
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

    public void onDisable() {
    }

    public Logger logger() {
        return this.logger;
    }

    public Path dataDirectory() {
        return this.getDataFolder().toPath();
    }

    public @NonNull MobBallEventHandler eventHandler() {
        return this.eventHandler;
    }

    public boolean gpLoaded() {
        return Bukkit.getPluginManager().isPluginEnabled("GriefPrevention");
    }
}
