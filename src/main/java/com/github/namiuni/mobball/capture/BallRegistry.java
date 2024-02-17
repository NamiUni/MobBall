package com.github.namiuni.mobball.capture;

import com.github.namiuni.mobball.config.ConfigManager;
import com.github.namiuni.mobball.wrapper.WrappedItem;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
@DefaultQualifier(NonNull.class)
public final class BallRegistry {

    private final ConfigManager configManager;

    private final BiMap<String, WrappedItem> registeredTypeMap;

    @Inject
    public BallRegistry(final ConfigManager configManager) {
        this.configManager = configManager;

        this.registeredTypeMap = Maps.synchronizedBiMap(HashBiMap.create());
        this.reloadRegisteredBallType();
    }

    public void reloadRegisteredBallType() {
        this.registeredTypeMap.clear();

        final var ballItems = this.configManager.primaryConfig().ballItems().entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), WrappedItem.create(entry.getValue())))
                .peek(entry -> entry.getValue().ballType(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.registeredTypeMap.putAll(ballItems);
    }

    public Set<String> keys() {
        return this.registeredTypeMap.keySet();
    }

    public Set<WrappedItem> values() {
        return this.registeredTypeMap.values();
    }

    public @Nullable WrappedItem get(String key) {
        return WrappedItem.create(this.registeredTypeMap.get(key).itemStack().clone());
    }
}
