package com.cerenia.client.settings;

public final class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, boolean value) {
        super(name, value);
    }

    public boolean isEnabled() { return get(); }
    public void toggle() { set(!get()); }
}
