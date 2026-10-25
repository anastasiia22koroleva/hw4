package tictactoe.player;

import java.util.Locale;

public enum PlayerType {
    HUMAN("Человек"),
    AI_EASY("Компьютер, лёгкий (ходит случайно)"),
    AI_MEDIUM("Компьютер, средний (выигрывает и блокирует)"),
    AI_HARD("Компьютер, сложный (просчитывает партию)");

    private final String title;

    PlayerType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isHuman() {
        return this == HUMAN;
    }

    /**
     * Тип по строке: номер из меню (1–4) или слово - human, easy, medium, hard, ai.
     * Просто "ai" означает среднего компьютера.
     */
    public static PlayerType parse(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Тип игрока не задан");
        }
        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "1", "human" -> HUMAN;
            case "2", "easy" -> AI_EASY;
            case "3", "medium", "ai" -> AI_MEDIUM;
            case "4", "hard" -> AI_HARD;
            default -> throw new IllegalArgumentException("Неизвестный тип игрока: " + value);
        };
    }
}
