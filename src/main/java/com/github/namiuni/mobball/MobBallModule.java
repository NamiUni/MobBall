package com.github.namiuni.mobball;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

import java.nio.file.Path;

@DefaultQualifier(NonNull.class)
public final class MobBallModule extends AbstractModule {

    private final MobBall mobBall;
    private final Path dataDirectory;

    public MobBallModule(
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
        commandManager = new PaperCommandManager<>(
                this.mobBall,
                ExecutionCoordinator.simpleCoordinator(),
                SenderMapper.identity()
        );

        commandManager.registerAsynchronousCompletions();

        return commandManager;
    }

    @Override
    public void configure() {
        this.bind(MobBall.class).toInstance(this.mobBall);
        this.bind(Path.class).toInstance(this.dataDirectory);
        this.bind(ComponentLogger.class).toInstance(this.mobBall.getComponentLogger());
    }
}
