package com.cerenia;

import java.util.ArrayList;
import java.util.List;

public final class CereniaModules {
    private CereniaModules() {}

    public static final class Module {
        public final String name;
        public final String category;
        public final String description;
        public boolean enabled;

        public Module(String name, String category, String description) {
            this.name = name;
            this.category = category;
            this.description = description;
        }
    }

    private static final List<Module> MODULES = new ArrayList<>();

    static {
        add("Sprint", "Movement", "Sprint module framework");
        add("Safe Walk", "Movement", "Movement module framework");
        add("Fullbright", "Visuals", "Brightness module framework");
        add("ESP", "Visuals", "Entity rendering module framework");
        add("HUD", "Visuals", "Interface overlay framework");
        add("Aim Assist", "Combat", "Combat module framework");
        add("Click Assist", "Combat", "Combat module framework");
        add("Velocity", "Combat", "Knockback module framework");
        add("Auto Clicker", "Combat", "Click automation framework");
    }

    private static void add(String name, String category, String description) {
        MODULES.add(new Module(name, category, description));
    }

    public static List<Module> all() {
        return MODULES;
    }

    public static List<Module> inCategory(String category) {
        return MODULES.stream()
            .filter(module -> module.category.equals(category))
            .toList();
    }

    public static int enabledCount() {
        return (int) MODULES.stream().filter(module -> module.enabled).count();
    }
}
