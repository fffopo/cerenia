package com.cerenia.client.settings;

public final class NumberSetting extends Setting<Double> {
    private final double min, max, step;

    public NumberSetting(String name, double value, double min, double max, double step) {
        super(name, value);
        if (min > max || step <= 0) throw new IllegalArgumentException("Invalid number range");
        this.min = min;
        this.max = max;
        this.step = step;
        set(value);
    }

    @Override
    protected Double validate(Double value) {
        double clamped = Math.max(min, Math.min(max, value));
        double snapped = min + Math.round((clamped - min) / step) * step;
        return Math.max(min, Math.min(max, snapped));
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getStep() { return step; }
}
