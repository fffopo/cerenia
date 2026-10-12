package com.cerenia.client.settings;

import java.util.List;

public final class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String initial, String... modes) {
        super(name, initial);
        this.modes = List.of(modes);
        if (this.modes.isEmpty()) throw new IllegalArgumentException("Modes cannot be empty");
        set(initial);
    }

    @Override
    protected String validate(String value) {
        if (!modes.contains(value)) throw new IllegalArgumentException("Unsupported mode: " + value);
        return value;
    }

    public List<String> getModes() { return modes; }

    public void cycle() {
        int next = (modes.indexOf(get()) + 1) % modes.size();
        set(modes.get(next));
    }
}
