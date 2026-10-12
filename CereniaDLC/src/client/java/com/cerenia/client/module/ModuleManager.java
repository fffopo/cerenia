package com.cerenia.client.module;

import com.cerenia.client.module.modules.ClickGuiModule;
import com.cerenia.client.module.modules.FullbrightModule;
import com.cerenia.client.module.modules.SprintModule;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();

    private ModuleManager() {}

    public static void initialize() {
        if (!MODULES.isEmpty()) return;
        register(new SprintModule());
        register(new FullbrightModule());
        register(new ClickGuiModule());
    }

    public static void register(Module module) {
        MODULES.add(module);
    }

    public static List<Module> getModules() {
        return List.copyOf(MODULES);
    }

    public static List<Module> getModulesByCategory(Category category) {
        return MODULES.stream().filter(m -> m.getCategory() == category).toList();
    }

    public static <T extends Module> Optional<T> findModule(Class<T> type) {
        return MODULES.stream().filter(type::isInstance).map(type::cast).findFirst();
    }

    public static void tick(Minecraft client, float deltaSeconds) {
        for (Module module : MODULES) {
            module.tickAnimation(deltaSeconds);
            if (module.isEnabled()) module.onUpdate();
        }
    }

    public static void handleKey(int key) {
        for (Module module : MODULES) {
            if (module.getKeybind() == key && key != 0) module.toggle();
            module.onKey(key);
        }
    }
}
