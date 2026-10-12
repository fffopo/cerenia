package com.cerenia.client.module.modules;

import com.cerenia.client.module.Category;
import com.cerenia.client.module.Module;
import net.minecraft.client.Minecraft;

public final class SprintModule extends Module {
    public SprintModule() {
        super("Sprint", "Automatically sprint while moving forward.", Category.MOVEMENT, 0);
    }

    @Override
    public void onUpdate() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.options == null) return;
        if (client.options.keyUp.isDown() && !client.player.isCrouching()) {
            client.player.setSprinting(true);
        }
    }
}
