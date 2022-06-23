package com.github.namiuni.mobball.command.commands;

import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArrayArgument;
import cloud.commandframework.bukkit.parsers.PlayerArgument;
import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.command.MobBallCommand;
import com.github.namiuni.mobball.config.ConfigFactory;
import com.google.inject.Inject;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Arrays;

@DefaultQualifier(NonNull.class)
public class GiveCommand implements MobBallCommand {

    final MobBall mobBall;
    final CommandManager<CommandSender> commandManager;
    final ConfigFactory configFactory;
    final NamespacedKey mobBallKey;

    @Inject
    public GiveCommand(
            MobBall mobBall,
            CommandManager<CommandSender> commandManager,
            ConfigFactory configFactory,
            NamespacedKey mobBallKey
    ) {
        this.mobBall = mobBall;
        this.commandManager = commandManager;
        this.configFactory = configFactory;
        this.mobBallKey = mobBallKey;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("mobball", "mb")
                .literal("give")
                .argument(PlayerArgument.of("player"))
                // わるいじっそう！
                .argument(StringArrayArgument.of(
                                "ball",
                                (context, lastString) -> configFactory.primaryConfig().bollItems().keySet().stream().toList()
                        )
                )
                // わるいじっそうここまで！
                .permission("mobball.give")
                .senderType(CommandSender.class)
                .handler(context -> {
                    String[] balls = context.getOrDefault("ball", new String[0]);
                    Player target = context.get("player");

                    Arrays.stream(balls)
                            .map(ballName -> {
                                var ballItem = configFactory.primaryConfig().bollItems().get(ballName);
                                ballItem.editMeta(meta ->
                                        meta.getPersistentDataContainer().set(mobBallKey, PersistentDataType.STRING, "empty")
                                );
                                return ballItem;
                            })
                            .forEach(ballItem -> target.getInventory().addItem(ballItem));
                })
                .build();

        this.commandManager.command(command);

    }
}
