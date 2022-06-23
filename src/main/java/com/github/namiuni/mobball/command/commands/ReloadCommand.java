package com.github.namiuni.mobball.command.commands;

import cloud.commandframework.CommandManager;
import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.command.MobBallCommand;
import com.github.namiuni.mobball.event.events.MobBallReloadEvent;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class ReloadCommand implements MobBallCommand {

    final MobBall mobBall;
    final CommandManager<CommandSender> commandManager;

    @Inject
    public ReloadCommand(
            MobBall mobBall,
            CommandManager<CommandSender> commandManager
    ) {
        this.mobBall = mobBall;
        this.commandManager = commandManager;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("mobball", "mb")
                .literal("reload")
                .permission("mobball.reload")
                .senderType(CommandSender.class)
                .handler(context -> {
                    this.mobBall.eventHandler().emit(new MobBallReloadEvent());
                    context.getSender().sendRichMessage("<aqua>設定をリロードしました");
                })
                .build();

        this.commandManager.command(command);
    }
}
