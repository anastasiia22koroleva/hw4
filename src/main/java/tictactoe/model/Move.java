package tictactoe.model;

/**
 * Ход: строка и столбец, считаются с нуля.
 */
public record Move(int row, int col) {

    /** Для вывода игроку - нумерация с единицы, как он и вводит. */
    @Override
    public String toString() {
        return (row + 1) + " " + (col + 1);
    }
}
