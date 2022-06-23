/*
 * CarbonChat
 *
 * Copyright (c) 2021 Josua Parks (Vicarious)
 *                    Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.github.namiuni.mobball.event;

import net.kyori.event.EventBus;
import net.kyori.event.EventSubscriber;
import net.kyori.event.EventSubscription;
import net.kyori.event.PostResult;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.function.Consumer;

@DefaultQualifier(NonNull.class)
public final class MobBallEventHandler {

    private final EventBus<MobBallEvent> eventBus = EventBus.create(MobBallEvent.class, (type, event, subscriber) -> {
        if (event instanceof ResultedMobBallEvent<@NonNull ?> rce) {
            return !rce.result().cancelled();
        }

        return true;
    });

    public <T extends MobBallEvent> EventSubscription subscribe(
        final Class<T> eventClass,
        final EventSubscriber<T> subscriber
    ) {
        return this.eventBus.subscribe(eventClass, subscriber);
    }

    public <T extends MobBallEvent> EventSubscription subscribe(
        final Class<T> eventClass,
        final int priority,
        final boolean acceptsCancelled,
        final Consumer<T> consumer
    ) {
        return this.eventBus.subscribe(eventClass, new EventSubscriberImpl<>(consumer, priority, acceptsCancelled));
    }

    public PostResult emit(final MobBallEvent event) {
        return this.eventBus.post(event);
    }

}
