package com.github.namiuni.mobball.config;

import com.github.namiuni.mobball.config.serialisation.EnchantmentSerializer;
import com.github.namiuni.mobball.config.serialisation.BallItemSerializer;
import com.github.namiuni.mobball.config.serialisation.LocaleSerializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;

@Singleton
@DefaultQualifier(NonNull.class)
public final class ConfigManager {

    private final Path dataDirectory;
    private final ComponentLogger logger;
    private final LocaleSerializer localeSerializer;
    private final EnchantmentSerializer enchantmentSerializer;
    private final BallItemSerializer ballItemSerializer;

    private PrimaryConfig primaryConfig = null;

    @Inject
    public ConfigManager(
            final Path dataDirectory,
            final LocaleSerializer localeSerializer,
            final ComponentLogger logger,
            final EnchantmentSerializer enchantmentSerializer,
            final BallItemSerializer ballItemSerializer
    ) {
        this.dataDirectory = dataDirectory;
        this.localeSerializer = localeSerializer;
        this.logger = logger;
        this.enchantmentSerializer = enchantmentSerializer;
        this.ballItemSerializer = ballItemSerializer;
    }

    public PrimaryConfig reloadPrimaryConfig() {
        try {
            this.primaryConfig = this.load(PrimaryConfig.class, "config.conf");
        } catch (final IOException exception) {
            this.logger.error("Failed to load config.conf", exception);
        }

        return Objects.requireNonNull(this.primaryConfig);
    }

    public PrimaryConfig primaryConfig() {
        return Objects.requireNonNullElseGet(this.primaryConfig, this::reloadPrimaryConfig);
    }

    public ConfigurationLoader<?> configurationLoader(final Path file) {
        return HoconConfigurationLoader.builder()
                .prettyPrinting(true)
                .defaultOptions(opts -> {
                    final var miniMessageSerializer =
                            ConfigurateComponentSerializer.builder()
                                    .scalarSerializer(MiniMessage.miniMessage())
                                    .outputStringComponents(true)
                                    .build();

                    final var kyoriSerializer =
                            ConfigurateComponentSerializer.configurate();

                    return opts.shouldCopyDefaults(true).serializers(serializerBuilder ->
                            serializerBuilder
                                    .registerAll(miniMessageSerializer.serializers())
                                    .registerAll(kyoriSerializer.serializers())
                                    .register(Locale.class, this.localeSerializer)
                                    .register(EnchantmentSerializer.Enchant.class, this.enchantmentSerializer)
                                    .register(ItemStack.class, this.ballItemSerializer)
                    );
                })
                .path(file)
                .build();
    }

    public <T> @Nullable T load(final Class<T> clazz, final String fileName) throws IOException {
        if (!Files.exists(this.dataDirectory)) {
            Files.createDirectories(this.dataDirectory);
        }

        final Path file = this.dataDirectory.resolve(fileName);

        final var loader = this.configurationLoader(file);

        try {
            final var root = loader.load();
            final @Nullable T config = root.get(clazz);

            if (!Files.exists(file)) {
                root.set(clazz, config);
                loader.save(root);
            }

            return config;

        } catch (final ConfigurateException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
