package tictactoe.player;

import java.util.Objects;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;
import tictactoe.ui.UserInterface;

public class HumanPlayer extends AbstractPlayer {

    private final UserInterface ui;

    public HumanPlayer(String name, Mark mark, UserInterface ui) {
        super(name, mark);
        this.ui = Objects.requireNonNull(ui, "ui");
    }

    @Override
    public Move nextMove(Board board) {
        while (true) {
            String line = ui.readLine(this + ", ваш ход (строка и столбец через пробел): ");
            Move move;
            try {
                move = parseMove(line, board.getSize());
            } catch (IllegalArgumentException e) {
                ui.showMessage(e.getMessage());
                continue;
            }
            if (board.isFree(move.row(), move.col())) {
                return move;
            }
            ui.showMessage("Клетка " + move + " уже занята, выберите другую.");
        }
    }

    /** Разбирает ввод вида "2 3" или "2,3". Номера с единицы, в Move - с нуля. */
    static Move parseMove(String line, int size) {
        String[] parts = line == null ? new String[0] : line.trim().split("[\\s,]+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Нужно два числа: строка и столбец, например: 2 3");
        }
        int row;
        int col;
        try {
            row = Integer.parseInt(parts[0]);
            col = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Это не числа: " + line);
        }
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("Числа должны быть от 1 до " + size);
        }
        return new Move(row - 1, col - 1);
    }
}
