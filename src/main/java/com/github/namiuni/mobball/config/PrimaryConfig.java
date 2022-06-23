package com.github.namiuni.mobball.config;

import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.format.TextDecoration;
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
public class PrimaryConfig {

    private PluginHooks pluginHooks = new PluginHooks();
    private Map<String, BallSettings> ballItems = Map.of("moripa", new BallSettings());
    private List<String> allowedMobs = List.of(
            "pig", "cow", "chicken", "mushroom_cow", "sheep", "rabbit", "fox",
            "parrot", "cat", "wolf", "llama",
            "horse", "donkey", "mule",
            "allay", "polar_bear", "panda", "goat", "squid", "glow_squid"
    );

    public PluginHooks pluginHooks() {
        return this.pluginHooks;
    }

    public Map<String, ItemStack> bollItems() {
        return this.ballItems.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), createBall(entry.getValue())))
                .collect(Collectors.toMap(Pair::key, Pair::value));
    }

    public Set<EntityType> allowedMOBs() {
        return this.allowedMobs.stream()
                .map(String::toUpperCase)
                .map(EntityType::valueOf)
                .collect(Collectors.toSet());
    }

    private ItemStack createBall(BallSettings ballSettings) {
        ItemStack ball = new ItemStack(Material.SNOWBALL);
        ball.editMeta(itemMeta -> {
            if (ballSettings.customModelData() != null) {
                itemMeta.setCustomModelData(ballSettings.customModelData());
            }
            itemMeta.displayName(ballSettings.name().decoration(TextDecoration.ITALIC, false));
            itemMeta.lore(ballSettings.lore().stream()
                    .map(component -> component.decoration(TextDecoration.ITALIC, false))
                    .toList());
        });
        return ball;
    }
}
