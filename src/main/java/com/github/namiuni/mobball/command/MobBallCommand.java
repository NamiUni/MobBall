package com.github.namiuni.mobball.command;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface MobBallCommand {

    void init();

}
