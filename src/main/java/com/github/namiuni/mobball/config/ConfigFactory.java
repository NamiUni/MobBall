package com.github.namiuni.mobball.config;

import com.github.namiuni.mobball.MobBall;
import com.github.namiuni.mobball.event.events.MobBallReloadEvent;
import com.github.namiuni.mobball.serialisation.LocaleSerializerConfigurate;
import com.google.inject.Inject;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
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

@DefaultQualifier(NonNull.class)
public class ConfigFactory {

    private final Path dataDirectory;
    private final LocaleSerializerConfigurate locale;

    private @Nullable PrimaryConfig primaryConfig = null;

    @Inject
    public ConfigFactory(
            final MobBall mobBall,
            final Path dataDirectory,
            final LocaleSerializerConfigurate locale
    ) {
        this.dataDirectory = dataDirectory;
        this.locale = locale;

        mobBall.eventHandler().subscribe(MobBallReloadEvent.class, event -> {
            this.reloadPrimaryConfig();
        });
    }

    public @Nullable PrimaryConfig reloadPrimaryConfig() {
        try {
            this.primaryConfig = this.load(PrimaryConfig.class, "config.conf");
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return this.primaryConfig;
    }

    public @Nullable PrimaryConfig primaryConfig() {
        if (this.primaryConfig == null) {
            return this.reloadPrimaryConfig();
        }

        return this.primaryConfig;
    }

    public ConfigurationLoader<?> configurationLoader(final Path file) {
        return HoconConfigurationLoader.builder()
                .prettyPrinting(true)
                .defaultOptions(opts -> {
                    final ConfigurateComponentSerializer serializer =
                            ConfigurateComponentSerializer.configurate();

                    return opts.shouldCopyDefaults(true).serializers(serializerBuilder ->
                            serializerBuilder.registerAll(serializer.serializers())
                                    .register(Locale.class, this.locale)
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
