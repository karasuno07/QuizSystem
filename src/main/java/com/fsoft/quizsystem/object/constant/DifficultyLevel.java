package com.fsoft.quizsystem.object.constant;

public enum DifficultyLevel {
    EASY("EASY"), MEDIUM("MEDIUM"), HARD("HARD");

    DifficultyLevel(String level) {
        this.level = level;
    }

    private final String level;

    public String getLevel() {
        return level;
    }
}
