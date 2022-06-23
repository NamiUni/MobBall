package com.github.namiuni.mobball.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class BallSettings {

    private String name = "<#66bb6a><b>もりぱボール";
    private List<String> lore = List.of(
            "<white>MOBを捕まえられるボール",
            "<white>捕まえたMOBはボールを投げれば召喚できる",
            "<white>一度召喚するとボールは消滅する"
    );
    private Integer customModelData = 10;

    public Component name() {
        var mm = MiniMessage.miniMessage();
        return mm.deserialize(this.name);
    }

    public List<Component> lore() {
        var mm = MiniMessage.miniMessage();
        return lore.stream()
                .map(mm::deserialize)
                .toList();
    }

    public Integer customModelData() {
        return customModelData;
    }
}
