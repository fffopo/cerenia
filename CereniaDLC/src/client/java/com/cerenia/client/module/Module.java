package com.cerenia.client.module;

import com.cerenia.client.settings.Setting;
import com.cerenia.client.utils.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private int keybind;
    private boolean enabled;
    private float animation;
    private final List<Setting<?>> settings = new ArrayList<>();

    protected Module(String name, String description, Category category, int keybind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keybind = keybind;
    }

    public final void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if (value) onEnable();
        else onDisable();
    }

    public final void toggle() { setEnabled(!enabled); }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}
    public void onRender() {}
    public void onKey(int key) {}

    public final void tickAnimation(float deltaSeconds) {
        animation = AnimationUtils.approach(animation, enabled ? 1.0f : 0.0f, deltaSeconds, 12.0f);
    }

    protected final <T extends Setting<?>> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }
    public boolean isEnabled() { return enabled; }
    public float getAnimation() { return animation; }
    public List<Setting<?>> getSettings() { return List.copyOf(settings); }
}
