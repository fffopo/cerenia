package com.cerenia.client.module.modules;

import com.cerenia.client.module.Category;
import com.cerenia.client.module.Module;
import com.cerenia.client.settings.NumberSetting;

public final class FullbrightModule extends Module {
    public final NumberSetting brightness =
        addSetting(new NumberSetting("Brightness", 1.0, 0.0, 1.0, 0.05));

    public FullbrightModule() {
        super("Fullbright", "Brightness control setting.", Category.RENDER, 0);
    }
}
