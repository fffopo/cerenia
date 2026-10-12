package com.cerenia.client.gui.clickgui;

import com.cerenia.client.module.Category;
import com.cerenia.client.module.Module;
import com.cerenia.client.module.ModuleManager;
import com.cerenia.client.settings.BooleanSetting;
import com.cerenia.client.settings.ModeSetting;
import com.cerenia.client.settings.NumberSetting;
import com.cerenia.client.settings.Setting;
import com.cerenia.client.shader.ShaderResources;
import com.cerenia.client.utils.AnimationUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class ClickGuiScreen extends Screen {
    private Category selected = Category.MOVEMENT;
    private String search = "";
    private final List<Module> visibleModules = new ArrayList<>();
    private float openAnimation;
    private long lastFrame = System.nanoTime();

    private int panelX, panelY, panelW, panelH;
    private int sidebarW, contentX, contentW;

    public ClickGuiScreen() {
        super(Component.literal("CereniaDLC"));
    }

    @Override
    protected void init() {
        clearWidgets();
        panelW = Math.min(720, width - 24);
        panelH = Math.min(410, height - 24);
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;
        sidebarW = 142;
        contentX = panelX + sidebarW + 16;
        contentW = panelW - sidebarW - 30;

        int cy = panelY + 90;
        for (Category category : Category.values()) {
            addRenderableWidget(Button.builder(
                Component.literal((selected == category ? "●  " : "   ") + category.getDisplayName()),
                b -> { selected = category; init(); }
            ).bounds(panelX + 12, cy, sidebarW - 24, 25).build());
            cy += 31;
        }

        addRenderableWidget(Button.builder(
            Component.literal("Search: " + (search.isBlank() ? "All" : search)),
            b -> {
                search = search.isBlank() ? "sprint" : "";
                init();
            }
        ).bounds(contentX, panelY + 76, 150, 22).build());

        visibleModules.clear();
        for (Module module : ModuleManager.getModulesByCategory(selected)) {
            if (module.getName().toLowerCase().contains(search.toLowerCase())
                || module.getDescription().toLowerCase().contains(search.toLowerCase())) {
                visibleModules.add(module);
            }
        }

        int cardW = (contentW - 10) / 2;
        int top = panelY + 111;
        for (int i = 0; i < visibleModules.size(); i++) {
            Module module = visibleModules.get(i);
            int col = i % 2;
            int row = i / 2;
            int x = contentX + col * (cardW + 10);
            int y = top + row * 80;

            addRenderableWidget(Button.builder(
                Component.literal(module.isEnabled() ? "ON" : "OFF"),
                b -> { module.toggle(); init(); }
            ).bounds(x + cardW - 58, y + 8, 48, 20).build());

            for (Setting<?> setting : module.getSettings()) {
                if (!setting.isVisible()) continue;
                if (setting instanceof BooleanSetting bool) {
                    addRenderableWidget(Button.builder(
                        Component.literal(setting.getName() + ": " + (bool.get() ? "ON" : "OFF")),
                        b -> { bool.toggle(); init(); }
                    ).bounds(x + 8, y + 36, cardW - 16, 20).build());
                } else if (setting instanceof ModeSetting mode) {
                    addRenderableWidget(Button.builder(
                        Component.literal(setting.getName() + ": " + mode.get()),
                        b -> { mode.cycle(); init(); }
                    ).bounds(x + 8, y + 36, cardW - 16, 20).build());
                } else if (setting instanceof NumberSetting number) {
                    addRenderableWidget(Button.builder(
                        Component.literal(setting.getName() + ": " + String.format("%.2f", number.get())),
                        b -> {
                            double next = number.get() + number.getStep();
                            number.set(next > number.getMax() ? number.getMin() : next);
                            init();
                        }
                    ).bounds(x + 8, y + 36, cardW - 16, 20).build());
                }
            }
        }

        addRenderableWidget(Button.builder(Component.literal("Close"), b -> onClose())
            .bounds(panelX + panelW - 90, panelY + panelH - 31, 76, 21).build());
    }

    @Override
    public void extractRenderState(
        GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick
    ) {
        long now = System.nanoTime();
        float dt = Math.min(0.05f, (now - lastFrame) / 1_000_000_000.0f);
        lastFrame = now;
        openAnimation = AnimationUtils.approach(openAnimation, 1.0f, dt, 10.0f);

        int bg = ShaderResources.BACKGROUND;
        int panel = ShaderResources.PANEL;

        // Soft ambient background particles; intentionally lightweight and shader-independent.
        g.fill(0, 0, width, height, 0xB9080A12);
        double t = now / 1_000_000_000.0;
        for (int i = 0; i < 34; i++) {
            int px = (int) ((i * 97L + (long) (t * (8 + i % 9))) % Math.max(1, width));
            int py = (int) ((i * 53L + (long) (t * (5 + i % 7))) % Math.max(1, height));
            int alpha = 18 + (int) (14 * (0.5 + 0.5 * Math.sin(t * 1.2 + i)));
            g.fill(px, py, px + 2, py + 2, (alpha << 24) | 0x008FA8FF);
        }
        int slide = (int) ((1.0f - openAnimation) * 10.0f);
        int px = panelX;
        int py = panelY + slide;
        rounded(g, px - 4, py - 4, px + panelW + 4, py + panelH + 4, 14, 0x42000000);
        rounded(g, px, py, px + panelW, py + panelH, 12, bg);
        rounded(g, px, py, px + panelW, py + 4, 2, pulseAccent(now));
        g.fill(px + 1, py + 62, px + panelW - 1, py + 63, 0xFF272D40);
        rounded(g, px + 1, py + 64, px + sidebarW, py + panelH - 1, 8, 0xFF111522);
        g.fill(px + sidebarW, py + 64, px + sidebarW + 1, py + panelH - 1, 0xFF252B3A);

        g.text(font, "CERENIA", panelX + 18, panelY + 16, 0xFFF2F2F2, false);
        g.text(font, "CLIENT  /  26.2", panelX + 18, panelY + 37, 0xFF969696, false);
        g.text(font, "CATEGORIES", panelX + 15, panelY + 72, 0xFF8C8C8C, false);
        g.text(font, selected.getDisplayName().toUpperCase(), contentX, panelY + 60, 0xFFF0F0F0, false);
        g.text(font, "ACTIVE: " + ModuleManager.getModules().stream().filter(Module::isEnabled).count(),
            panelX + 15, panelY + panelH - 18, 0xFFAAAAAA, false);

        int cardW = (contentW - 10) / 2;
        int top = panelY + 111;
        for (int i = 0; i < visibleModules.size(); i++) {
            Module module = visibleModules.get(i);
            int col = i % 2;
            int row = i / 2;
            int x = contentX + col * (cardW + 10);
            int y = top + row * 80;
            int cardColor = module.isEnabled() ? 0xFF292929 : 0xFF202020;

            rounded(g, x, y, x + cardW, y + 68, 9,
                module.isEnabled() ? 0xFF202B42 : cardColor);
            rounded(g, x + 1, y + 1, x + cardW - 1, y + 67, 8,
                module.isEnabled() ? 0xFF182033 : 0xFF171B27);
            rounded(g, x + 8, y + 8, x + 30, y + 30, 6,
                module.isEnabled() ? 0xFF344C7D : 0xFF252B3A);
            drawModuleGlyph(g, module.getName(), x + 8, y + 8);
            g.text(font, module.getName(), x + 38, y + 10, 0xFFF3F5FF, false);
            g.text(font, module.isEnabled() ? "● ENABLED" : "○ DISABLED",
                x + 38, y + 27, module.isEnabled() ? 0xFF8FB5FF : 0xFF747D91, false);
            g.text(font, module.getDescription(), x + 10, y + 51, 0xFF9BA5BA, false);
        }

        super.extractRenderState(g, mouseX, mouseY, partialTick);
    }

    private static int pulseAccent(long now) {
        double pulse = 0.5 + 0.5 * Math.sin(now / 1_000_000_000.0 * 1.7);
        int r = (int) (92 + 30 * pulse);
        int g = (int) (105 + 35 * pulse);
        int b = (int) (238 + 17 * pulse);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    // Rounded rectangles built from horizontal spans so they work with the game's normal GUI renderer.
    private static void rounded(GuiGraphicsExtractor g, int left, int top, int right, int bottom,
                                int radius, int color) {
        int r = Math.max(0, Math.min(radius, Math.min((right - left) / 2, (bottom - top) / 2)));
        for (int y = top; y < bottom; y++) {
            int dy = Math.min(y - top, bottom - 1 - y);
            int inset = 0;
            if (dy < r) {
                double d = r - dy - 0.5;
                inset = (int) Math.ceil(r - Math.sqrt(Math.max(0.0, r * r - d * d)));
            }
            g.fill(left + inset, y, right - inset, y + 1, color);
        }
    }

    private static void drawModuleGlyph(GuiGraphicsExtractor g, String name, int x, int y) {
        // Small purpose-drawn pictograms; no external icon font required.
        if (name.equalsIgnoreCase("KillAura") || name.toLowerCase().contains("combat")) {
            // Pixel sword silhouette.
            g.fill(x + 14, y + 3, x + 17, y + 14, 0xFFE3EAFF);
            g.fill(x + 12, y + 4, x + 15, y + 7, 0xFFE3EAFF);
            g.fill(x + 15, y + 12, x + 20, y + 15, 0xFF8FAEFF);
            g.fill(x + 10, y + 15, x + 15, y + 18, 0xFF8FAEFF);
        } else {
            String glyph = name.isEmpty() ? "•" : name.substring(0, 1).toUpperCase();
            g.text(net.minecraft.client.Minecraft.getInstance().font, glyph, x + 7, y + 6, 0xFFE3EAFF, false);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
