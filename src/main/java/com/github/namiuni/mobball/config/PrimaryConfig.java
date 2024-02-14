package com.github.namiuni.mobball.config;

import com.github.namiuni.mobball.util.ItemStackBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ConfigSerializable
@DefaultQualifier(NonNull.class)
public final class PrimaryConfig {

    private Integration integration = new Integration();
    private Map<String, ItemStack> ballItems = Map.of(
            "moripa",
            ItemStackBuilder.of(Material.SNOWBALL)
                    .displayName(MiniMessage.miniMessage().deserialize("<#66bb6a><b>もりぱボール"))
                    .loreList(
                            MiniMessage.miniMessage().deserialize("<white>MOBを捕まえられるボール"),
                            MiniMessage.miniMessage().deserialize("<white>捕まえたMOBはボールを投げれば召喚できる"),
                            MiniMessage.miniMessage().deserialize("<white>一度召喚するとボールは消滅する")
                    )
                    .customModelData(10)
                    .build()
    );
    private List<String> capturableMobs = List.of(
            "pig", "cow", "chicken", "mushroom_cow", "sheep", "rabbit", "fox",
            "parrot", "cat", "wolf", "llama",
            "horse", "donkey", "mule",
            "allay", "polar_bear", "panda", "goat", "squid", "glow_squid"
    );

    public Integration integration() {
        return this.integration;
    }

    public Map<String, ItemStack> ballItems() {
        return this.ballItems;
    }

    public Set<EntityType> capturableMOBs() {
        return this.capturableMobs.stream()
                .map(String::toUpperCase)
                .map(EntityType::valueOf)
                .collect(Collectors.toSet());
    }
}
