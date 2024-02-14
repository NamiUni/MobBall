package com.github.namiuni.mobball.config.serialisation;

import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

@DefaultQualifier(NonNull.class)
public final class BallItemSerializer implements TypeSerializer<ItemStack> {

    private final ComponentLogger logger;

    private static final String ITEM_META = "item-meta";
    private static final String DISPLAY_NAME = "display-name";
    private static final String LORE = "lore";
    private static final String CUSTOM_MODEL_DATA = "custom-model-data";
    private static final String ENCHANTMENTS = "enchantments";

    @Inject
    public BallItemSerializer(final ComponentLogger logger) {
        this.logger = logger;
    }

    @Override
    public ItemStack deserialize(final Type type, final ConfigurationNode node) throws SerializationException {

        final var itemStack = new ItemStack(Objects.requireNonNull(Material.SNOWBALL), 1);

        final var metaNode = node.node(ITEM_META);
        final var itemMeta = itemStack.getItemMeta();

        if (itemMeta != null) {
            Optional.ofNullable(metaNode.node(DISPLAY_NAME).get(Component.class))
                    .ifPresent(itemMeta::displayName);

            Optional.ofNullable(metaNode.node(LORE).getList(Component.class))
                    .ifPresent(itemMeta::lore);

            Optional.of(metaNode.node(CUSTOM_MODEL_DATA).getInt())
                    .ifPresent(itemMeta::setCustomModelData);

            Optional.ofNullable(metaNode.node(ENCHANTMENTS).getList(EnchantmentSerializer.Enchant.class))
                    .ifPresent(enchantments -> enchantments.forEach(enchant ->
                            itemMeta.addEnchant(enchant.enchantment(), enchant.level(), true)));

            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }

    @Override
    public void serialize(final Type type, final @Nullable ItemStack obj, final ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
        } else {
            if (obj.hasItemMeta()) {
                final var meta = obj.getItemMeta();
                final var metaNode = node.node(ITEM_META);
                metaNode.node(DISPLAY_NAME).set(meta.displayName());
                metaNode.node(LORE).setList(Component.class, meta.lore());
                if (meta.hasCustomModelData()) {
                    metaNode.node(CUSTOM_MODEL_DATA).set(meta.getCustomModelData());
                }
                if (meta.hasEnchants()) {
                    metaNode.node(ENCHANTMENTS).set(meta.getEnchants());
                }
            }
        }
    }
}
