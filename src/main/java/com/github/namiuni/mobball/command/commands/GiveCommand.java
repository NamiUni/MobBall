package com.github.namiuni.mobball.command.commands;

import com.github.namiuni.mobball.wrapper.WrappedItem;
import com.github.namiuni.mobball.command.BallItemParser;
import com.github.namiuni.mobball.command.MobBallCommand;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.bukkit.parser.PlayerParser;

@DefaultQualifier(NonNull.class)
public final class GiveCommand implements MobBallCommand {

    private final CommandManager<CommandSender> commandManager;
    private final BallItemParser<CommandSender> ballItemParser;

    @Inject
    public GiveCommand(
            final CommandManager<CommandSender> commandManager,
            final BallItemParser<CommandSender> ballItemParser
    ) {
        this.commandManager = commandManager;
        this.ballItemParser = ballItemParser;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("mobball", "mb")
                .literal("give")
                .required("player", PlayerParser.playerParser())
                .required("ball", ballItemParser.ballParser())
                .permission("mobball.give")
                .senderType(CommandSender.class)
                .handler(context -> {
                    final Player target = context.get("player");
                    final WrappedItem ballItem = context.get("ball");
                    target.getInventory().addItem(ballItem.itemStack());
                })
                .build();

        this.commandManager.command(command);
    }
}
