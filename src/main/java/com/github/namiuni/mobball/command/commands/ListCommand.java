package com.github.namiuni.mobball.command.commands;

import com.github.namiuni.mobball.command.MobBallCommand;
import com.github.namiuni.mobball.config.ConfigManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.CommandManager;

@DefaultQualifier(NonNull.class)
public final class ListCommand implements MobBallCommand {

    private final CommandManager<CommandSender> commandManager;
    private final ConfigManager configFactory;

    @Inject
    public ListCommand(
            final CommandManager<CommandSender> commandManager,
            final ConfigManager configFactory
    ) {
        this.commandManager = commandManager;
        this.configFactory = configFactory;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("mobball", "mb")
                .literal("list")
                .permission("mobball.list")
                .senderType(CommandSender.class)
                .handler(context -> {
                    final var capturableMobs = configFactory.primaryConfig().capturableMOBs().stream()
                            .map(Component::translatable)
                            .reduce((mobName1, mobName2) -> mobName1
                                    .append(Component.text(", "))
                                    .append(mobName2))
                            .orElse(Component.translatable(""));

                    final var mm = MiniMessage.miniMessage();
                    final var message = Component.text()
                            .append(mm.deserialize("<red><st>                            </st><white>ゲット出来るMOB一覧</white><st>                            </st></red>"))
                            .append(capturableMobs)
                            .append(Component.newline())
                            .append(mm.deserialize("<red><st>                                                                               </st><red>"))
                            .build();

                    context.sender().sendMessage(message);
                })
                .build();

        this.commandManager.command(command);

    }
}
