package com.cerenia;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CereniaScreen extends Screen {
    private static final String[] CATEGORIES = {
        "Combat", "Movement", "Visuals"
    };

    private String selectedCategory = "Combat";

    private int panelX, panelY, panelW, panelH;
    private int contentX, contentW;

    public CereniaScreen() {
        super(Component.literal("CereniaDLC"));
    }

    @Override
    protected void init() {
        clearWidgets();

        panelW = Math.min(700, this.width - 32);
        panelH = Math.min(390, this.height - 32);
        panelX = (this.width - panelW) / 2;
        panelY = (this.height - panelH) / 2;

        int sidebarW = 135;
        contentX = panelX + sidebarW + 18;
        contentW = panelW - sidebarW - 34;

        int categoryY = panelY + 91;

        for (int i = 0; i < CATEGORIES.length; i++) {
            String category = CATEGORIES[i];
            String prefix = category.equals(selectedCategory) ? "◆  " : "    ";

            addRenderableWidget(
                Button.builder(Component.literal(prefix + category), button -> {
                    selectedCategory = category;
                    init();
                })
                .bounds(panelX + 12, categoryY + i * 35, sidebarW - 24, 27)
                .build()
            );
        }

        List<CereniaModules.Module> modules =
            CereniaModules.inCategory(selectedCategory);

        int gap = 10;
        int cardW = (contentW - gap) / 2;
        int cardTop = panelY + 111;
        int cardH = 66;

        for (int i = 0; i < modules.size(); i++) {
            CereniaModules.Module module = modules.get(i);
            int col = i % 2;
            int row = i / 2;
            int x = contentX + col * (cardW + gap);
            int y = cardTop + row * (cardH + 9);

            addRenderableWidget(
                Button.builder(
                    Component.literal(module.enabled ? "ON  ●" : "OFF  ○"),
                    button -> {
                        module.enabled = !module.enabled;
                        init();
                    }
                )
                .bounds(x + cardW - 72, y + 9, 64, 23)
                .build()
            );
        }

        addRenderableWidget(
            Button.builder(Component.literal("Done"), button -> onClose())
                .bounds(panelX + panelW - 105, panelY + panelH - 39, 88, 25)
                .build()
        );
    }

    @Override
    public void extractRenderState(
        GuiGraphicsExtractor g,
        int mouseX,
        int mouseY,
        float delta
    ) {
        // Background and panels are drawn BEFORE widgets, so buttons stay visible.
        g.fill(0, 0, this.width, this.height, 0x30000000);

        g.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xF0101113);
        g.fill(panelX, panelY, panelX + panelW, panelY + 3, 0xFF858585);
        g.fill(panelX + 1, panelY + 62, panelX + panelW - 1, panelY + 63, 0xFF414141);

        int sidebarW = 135;
        g.fill(
            panelX + 1, panelY + 64,
            panelX + sidebarW, panelY + panelH - 1,
            0xFF151618
        );

        g.fill(
            panelX + sidebarW, panelY + 64,
            panelX + sidebarW + 1, panelY + panelH - 1,
            0xFF383A3D
        );

        g.text(font, "CERENIA", panelX + 19, panelY + 17, 0xFFF1F1F1, false);
        g.text(font, "CLIENT  /  26.2", panelX + 19, panelY + 39, 0xFF999999, false);

        g.text(font, "CATEGORIES", panelX + 16, panelY + 75, 0xFF858585, false);
        g.text(font, "MODULES", contentX, panelY + 76, 0xFFE0E0E0, false);
        g.text(
            font,
            selectedCategory.toUpperCase(),
            contentX + 82, panelY + 76,
            0xFF999999, false
        );

        List<CereniaModules.Module> modules =
            CereniaModules.inCategory(selectedCategory);

        int gap = 10;
        int cardW = (contentW - gap) / 2;
        int cardTop = panelY + 111;
        int cardH = 66;

        for (int i = 0; i < modules.size(); i++) {
            CereniaModules.Module module = modules.get(i);
            int col = i % 2;
            int row = i / 2;
            int x = contentX + col * (cardW + gap);
            int y = cardTop + row * (cardH + 9);

            g.fill(x, y, x + cardW, y + cardH,
                module.enabled ? 0xFF292B2E : 0xFF1C1D1F);

            g.fill(x, y, x + 2, y + cardH,
                module.enabled ? 0xFFB8B8B8 : 0xFF484848);

            g.text(font, module.name, x + 10, y + 10,
                module.enabled ? 0xFFFFFFFF : 0xFFD0D0D0, false);

            g.text(font, module.enabled ? "ENABLED" : "DISABLED",
                x + 10, y + 28,
                module.enabled ? 0xFFBDBDBD : 0xFF777777, false);

            g.text(font, module.description, x + 10, y + 47,
                0xFF888888, false);
        }

        int footerY = panelY + panelH - 20;
        g.text(font, "CereniaDLC", panelX + 16, footerY, 0xFF777777, false);
        g.text(font, "ACTIVE: " + CereniaModules.enabledCount(),
            panelX + 125, footerY, 0xFFBDBDBD, false);

        super.extractRenderState(g, mouseX, mouseY, delta);
    }
}
