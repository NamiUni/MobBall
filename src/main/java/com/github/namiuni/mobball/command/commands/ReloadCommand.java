package com.github.namiuni.mobball.command.commands;

import com.github.namiuni.mobball.capture.BallRegistry;
import com.github.namiuni.mobball.command.MobBallCommand;
import com.github.namiuni.mobball.config.ConfigManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.CommandManager;

@DefaultQualifier(NonNull.class)
public final class ReloadCommand implements MobBallCommand {

    private final ConfigManager configManager;
    private final BallRegistry ballRegistry;
    private final CommandManager<CommandSender> commandManager;

    @Inject
    public ReloadCommand(
            final ConfigManager configManager,
            final BallRegistry ballRegistry,
            final CommandManager<CommandSender> commandManager
    ) {
        this.configManager = configManager;
        this.ballRegistry = ballRegistry;
        this.commandManager = commandManager;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("mobball", "mb")
                .literal("reload")
                .permission("mobball.reload")
                .senderType(CommandSender.class)
                .handler(context -> {
                    this.configManager.reloadPrimaryConfig();
                    this.ballRegistry.reloadRegisteredBallType();
                    context.sender().sendRichMessage("<aqua>設定をリロードしました");
                })
                .build();

        this.commandManager.command(command);
    }
}
