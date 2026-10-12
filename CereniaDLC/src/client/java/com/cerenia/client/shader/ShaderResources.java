package com.cerenia.client.shader;

import net.minecraft.resources.Identifier;

public final class ShaderResources {
    private ShaderResources() {}

    public static final Identifier ROUNDED_PANEL =
        Identifier.fromNamespaceAndPath("cereniadlc", "shaders/rounded_panel");
    public static final Identifier GRADIENT =
        Identifier.fromNamespaceAndPath("cereniadlc", "shaders/gradient");
    public static final Identifier BLUR =
        Identifier.fromNamespaceAndPath("cereniadlc", "shaders/blur");

    public static final int BACKGROUND = 0xE61A1A1A;
    public static final int PANEL = 0xF0101010;
    public static final int ACCENT = 0xFF969696;
    public static final int TEXT = 0xFFF0F0F0;
    public static final int MUTED = 0xFFAAAAAA;
}
