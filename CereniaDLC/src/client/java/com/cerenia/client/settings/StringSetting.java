package com.cerenia.client.settings;

public final class StringSetting extends Setting<String> {
    private final int maxLength;

    public StringSetting(String name, String value, int maxLength) {
        super(name, value);
        if (maxLength < 1) throw new IllegalArgumentException("maxLength must be positive");
        this.maxLength = maxLength;
        set(value);
    }

    @Override
    protected String validate(String value) {
        if (value == null) return "";
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    public int getMaxLength() { return maxLength; }
}
