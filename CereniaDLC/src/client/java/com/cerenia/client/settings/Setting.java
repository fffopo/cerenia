package com.cerenia.client.settings;

import java.util.Objects;
import java.util.function.BooleanSupplier;

public abstract class Setting<T> {
    private final String name;
    private T value;
    private BooleanSupplier visible = () -> true;

    protected Setting(String name, T value) {
        this.name = Objects.requireNonNull(name);
        this.value = value;
    }

    public String getName() { return name; }
    public T get() { return value; }

    public void set(T value) {
        this.value = validate(value);
    }

    protected T validate(T value) { return value; }

    public boolean isVisible() { return visible.getAsBoolean(); }

    public Setting<T> visibleWhen(BooleanSupplier condition) {
        visible = Objects.requireNonNull(condition);
        return this;
    }
}
