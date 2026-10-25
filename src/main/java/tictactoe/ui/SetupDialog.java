package tictactoe.ui;

import java.util.Locale;
import java.util.Objects;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.player.Player;
import tictactoe.player.PlayerFactory;
import tictactoe.player.PlayerType;

/**
 * Вопросы перед началом игры: размер поля, кто играет, сыграть ли ещё.
 * Пустой ответ - значение по умолчанию, оно показано в скобках.
 */
public class SetupDialog {

    private final UserInterface ui;

    public SetupDialog(UserInterface ui) {
        this.ui = Objects.requireNonNull(ui, "ui");
    }

    public Board askBoard() {
        int size = askNumber("Размер поля, от " + Board.MIN_SIZE + " до " + Board.MAX_SIZE,
                Board.MIN_SIZE, Board.MAX_SIZE, Board.MIN_SIZE);
        Board.Builder builder = Board.builder().size(size);
        if (size > Board.MIN_SIZE) {
            builder.winLength(askNumber("Сколько знаков в ряд нужно для победы, от 3 до " + size,
                    Board.MIN_SIZE, size, Math.min(size, 5)));
        }
        return builder.build();
    }

    public Player askPlayer(PlayerFactory factory, Mark mark, PlayerType defaultType) {
        ui.showMessage("");
        ui.showMessage("Кто играет за " + mark.getSymbol() + "?");
        PlayerType[] types = PlayerType.values();
        for (int i = 0; i < types.length; i++) {
            ui.showMessage("  " + (i + 1) + " - " + types[i].getTitle());
        }
        PlayerType type = types[askNumber("Ваш выбор", 1, types.length, defaultType.ordinal() + 1) - 1];
        String name = ui.readLine("Имя (Enter - по умолчанию): ");
        return factory.create(type, name, mark);
    }

    public boolean askPlayAgain() {
        while (true) {
            String answer = ui.readLine("Сыграть ещё раз? (д/н): ").toLowerCase(Locale.ROOT);
            switch (answer) {
                case "д", "да", "y", "yes" -> {
                    return true;
                }
                case "н", "нет", "n", "no" -> {
                    return false;
                }
                default -> ui.showMessage("Ответьте «д» или «н».");
            }
        }
    }

    private int askNumber(String question, int min, int max, int defaultValue) {
        while (true) {
            String line = ui.readLine(question + " [" + defaultValue + "]: ");
            if (line.isEmpty()) {
                return defaultValue;
            }
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // ниже попросим ввести ещё раз
            }
            ui.showMessage("Введите число от " + min + " до " + max + ".");
        }
    }
}
