package com.github.namiuni.mobball.command;

import com.github.namiuni.mobball.capture.BallRegistry;
import com.github.namiuni.mobball.wrapper.WrappedItem;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

@DefaultQualifier(NonNull.class)
public final class BallItemParser<C> implements ArgumentParser<C, WrappedItem>, BlockingSuggestionProvider.Strings<C> {

    private final BallRegistry ballRegistry;

    @Inject
    public BallItemParser(final BallRegistry ballRegistry) {
        this.ballRegistry = ballRegistry;
    }

    public ParserDescriptor<C, WrappedItem> ballParser() {
        return ParserDescriptor.of(new BallItemParser<>(this.ballRegistry), WrappedItem.class);
    }

    @Override
    public ArgumentParseResult<WrappedItem> parse(
            final CommandContext<C> commandContext,
            final CommandInput commandInput
    ) {
        final var input = commandInput.readString();
        final @Nullable WrappedItem ballItem = this.ballRegistry.get(input);

        return ballItem != null
                ? ArgumentParseResult.success(ballItem)
                : ArgumentParseResult.failure(new IllegalArgumentException());
    }

    @Override
    public @NonNull Iterable<@NonNull String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
        return this.ballRegistry.keys();
    }
}
