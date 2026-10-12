package com.cerenia.client.module;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player"),
    MISC("Misc"),
    WORLD("World");

    private final String displayName;

    Category(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
