package com.cerenia;

import com.cerenia.client.gui.clickgui.ClickGuiScreen;
import com.cerenia.client.module.ModuleManager;
import com.cerenia.client.module.modules.ClickGuiModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public final class CereniaDLCClient implements ClientModInitializer {
    private static KeyMapping openGui;

    @Override
    public void onInitializeClient() {
        ModuleManager.initialize();

        KeyMapping.Category category = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("cereniadlc", "client")
        );

        openGui = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.cereniadlc.open_gui",
            InputConstants.Type.KEYSYM,
            344,
            category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGui.consumeClick()) {
                client.setScreenAndShow(new ClickGuiScreen());
            }

            float deltaSeconds = 1.0f / 20.0f;
            ModuleManager.tick(client, deltaSeconds);
        });
    }
}
