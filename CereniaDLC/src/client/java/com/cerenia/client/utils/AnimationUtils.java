package com.cerenia.client.utils;

public final class AnimationUtils {
    private AnimationUtils() {}

    public static float approach(float current, float target, float deltaSeconds, float speed) {
        float factor = 1.0f - (float) Math.exp(-Math.max(0, speed) * Math.max(0, deltaSeconds));
        return current + (target - current) * Math.min(1.0f, factor);
    }

    public static float easeInOutQuad(float t) {
        t = clamp01(t);
        return t < 0.5f ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2;
    }

    public static float easeOutBack(float t) {
        t = clamp01(t);
        float c1 = 1.70158f;
        float c3 = c1 + 1;
        return 1 + c3 * (float) Math.pow(t - 1, 3) + c1 * (float) Math.pow(t - 1, 2);
    }

    public static float clamp01(float value) {
        return Math.max(0, Math.min(1, value));
    }
}
