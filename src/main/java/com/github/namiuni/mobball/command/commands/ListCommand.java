package com.github.namiuni.mobball.command.commands;

import cloud.commandframework.CommandManager;
import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.command.MobBallCommand;
import com.github.namiuni.mobball.config.ConfigFactory;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;

@DefaultQualifier(NonNull.class)
public class ListCommand implements MobBallCommand {

    final MobBall mobBall;
    final CommandManager<CommandSender> commandManager;
    final ConfigFactory configFactory;

    @Inject
    public ListCommand(
            MobBall mobBall,
            CommandManager<CommandSender> commandManager,
            ConfigFactory configFactory
    ) {
        this.mobBall = mobBall;
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
                    var mobNames = configFactory.primaryConfig().allowedMOBs().stream()
                            .map(Component::translatable)
                            .map(mobNameComponent -> Component.text()
                                    .append(mobNameComponent)
                                    .append(Component.text(", "))
                                    .build()
                            )
                            .toList();
                    var mm = MiniMessage.miniMessage();
                    List<Component> component = List.of(
                            mm.deserialize("<red><st>                            </st><white>ゲット出来るMOB一覧</white><st>                            </st></red>"),
                            Component.text().append(mobNames).build(),
                            mm.deserialize("<red><st>                                                                               </st><red>")
                    );
                    component.forEach(message -> context.getSender().sendMessage(message));
                })
                .build();

        this.commandManager.command(command);

    }
}
