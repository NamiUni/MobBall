package com.github.namiuni.mobball.wrapper;

import com.github.namiuni.mobball.util.ItemStackBuilder;
import com.github.namiuni.mobball.util.LegacyUpdater;
import com.github.namiuni.mobball.util.NamespacedKeyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@DefaultQualifier(NonNull.class)
public final class WrappedItem {

    private ItemStack itemStack;

    private WrappedItem(final ItemStack itemStack) {
        this.itemStack = itemStack;
        LegacyUpdater.updateIfNeeded(this.itemStack);
    }

    public static WrappedItem create(final ItemStack itemStack) {
        return new WrappedItem(itemStack);
    }

    public ItemStack itemStack() {
        return this.itemStack;
    }

    public @Nullable String ballType() {
        final var container = this.itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(NamespacedKeyUtils.ballTypeKey(), PersistentDataType.STRING)
                ? container.get(NamespacedKeyUtils.ballTypeKey(), PersistentDataType.STRING)
                : null;
    }

    public void ballType(final String ballType) {
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(NamespacedKeyUtils.ballTypeKey(), PersistentDataType.STRING, ballType));
    }

    public boolean hasEntity() {
        return this.itemStack.getItemMeta().getPersistentDataContainer().has(NamespacedKeyUtils.rawEntityKey(), PersistentDataType.BYTE_ARRAY);
    }

    public @Nullable Entity entity() {
        final var container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(NamespacedKeyUtils.rawEntityKey(), PersistentDataType.BYTE_ARRAY)
                ? Bukkit.getUnsafe().deserializeEntity(container.get(NamespacedKeyUtils.rawEntityKey(), PersistentDataType.BYTE_ARRAY), Bukkit.getWorlds().get(0))
                : null;
    }

    public void entity(final WrappedEntity wrappedEntity) {
        final var entity = wrappedEntity.entity();
        final var rawEntity = Bukkit.getUnsafe().serializeEntity(entity);

        // todo: いつか綺麗に書き直す
        final var lore = new ArrayList<Component>();
        lore.add(Component.text("Owner: ")
                .append(Objects.requireNonNull(Bukkit.getPlayer(Objects.requireNonNull(wrappedEntity.ownerUUID()))).displayName()));
        lore.add(Component.text("Type: ")
                .append(Component.translatable(entity.getType())));
        if (entity instanceof LivingEntity livingEntity) {
            final var health = livingEntity.getHealth();
            final var maxHealth = Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
            lore.add(Component.text("Health: ")
                    .append(Component.text(health + " / " + maxHealth)));

            if (entity instanceof AbstractHorse horse) {
                final var speed = 42.162962963 * Objects.requireNonNull(horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).getValue();
                lore.add(Component.text("Speed: ")
                        .append(Component.text(new DecimalFormat("0.00").format(speed)))
                        .append(Component.space())
                        .append(Component.text("b/s")));

                final var jumpStrength = horse.getJumpStrength();
                final var jump = -0.1817584952 * Math.pow(jumpStrength, 3) + 3.689713992 * Math.pow(jumpStrength, 2) + 2.128599134 * jumpStrength - 0.343930367;
                lore.add(Component.text("Jump: ")
                        .append(Component.text(new DecimalFormat("0.00").format(jump)))
                        .append(Component.space())
                        .append(Component.text("b")));
            }
        }
        lore.replaceAll(component -> component.color(NamedTextColor.WHITE));
        lore.replaceAll(component -> component.decoration(TextDecoration.ITALIC, false));

        this.itemStack = ItemStackBuilder.of(itemStack)
                .displayName(Optional.ofNullable(entity.customName())
                        .orElse(entity.name()))
                .lore(lore)
                .setData(NamespacedKeyUtils.rawEntityKey(), PersistentDataType.BYTE_ARRAY, rawEntity)
                .build();
    }
}
