package com.cerenia.client.utils;

public final class ColorUtils {
    private ColorUtils() {}

    public static int withAlpha(int argb, int alpha) {
        return (Math.max(0, Math.min(255, alpha)) << 24) | (argb & 0x00FFFFFF);
    }

    public static int blend(int a, int b, float amount) {
        float t = AnimationUtils.clamp01(amount);
        int aa = (a >>> 24) & 255, ar = (a >>> 16) & 255;
        int ag = (a >>> 8) & 255, ab = a & 255;
        int ba = (b >>> 24) & 255, br = (b >>> 16) & 255;
        int bg = (b >>> 8) & 255, bb = b & 255;
        int alpha = Math.round(aa + (ba - aa) * t);
        int red = Math.round(ar + (br - ar) * t);
        int green = Math.round(ag + (bg - ag) * t);
        int blue = Math.round(ab + (bb - ab) * t);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}
