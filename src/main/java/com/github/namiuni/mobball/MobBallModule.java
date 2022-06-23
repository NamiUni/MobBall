package com.github.namiuni.mobball;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;

@DefaultQualifier(NonNull.class)
public final class MobBallModule extends AbstractModule {

    private final Logger logger = LogManager.getLogger("MobBall");
    private final MobBall mobBall;
    private final Path dataDirectory;

    MobBallModule(
            final MobBall mobBall,
            final Path dataDirectory
    ) {
        this.mobBall = mobBall;
        this.dataDirectory = dataDirectory;
    }

    @Provides
    @Singleton
    public CommandManager<CommandSender> commandManager() {
        final PaperCommandManager<CommandSender> commandManager;
        try {

            commandManager = new PaperCommandManager<>(
                    this.mobBall,
                    AsynchronousCommandExecutionCoordinator.<CommandSender>newBuilder().build(),
                    commandSender -> commandSender,
                    commander -> commander
            );
        } catch (final Exception exception) {
            throw new RuntimeException("コマンドマネージャーの初期化に失敗しました。", exception);
        }

        commandManager.registerAsynchronousCompletions();

        return commandManager;
    }

    @Provides
    @Singleton
    public NamespacedKey mobBallKey() {
        return new NamespacedKey(mobBall, "MobBall");
    }

    @Override
    public void configure() {
        this.bind(Logger.class).toInstance(this.logger);
        this.bind(MobBall.class).toInstance(this.mobBall);
        this.bind(Path.class).toInstance(this.dataDirectory);
    }
}
