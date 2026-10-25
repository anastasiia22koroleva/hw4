package tictactoe.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Игровое поле. Напрямую не создаётся - только через {@link #builder()}.
 */
public class Board {

    public static final int MIN_SIZE = 3;
    public static final int MAX_SIZE = 10;

    // направления, в которых ищем линию: вправо, вниз и две диагонали
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    private final int size;
    private final int winLength;
    private final char emptySymbol;
    private final Mark[][] cells;
    private int filled;

    private Board(Builder builder) {
        this.size = builder.size;
        this.winLength = builder.winLength;
        this.emptySymbol = builder.emptySymbol;
        this.cells = new Mark[size][size];
        for (Builder.Preset preset : builder.presets) {
            place(preset.move(), preset.mark());
        }
    }

    private Board(Board other) {
        this.size = other.size;
        this.winLength = other.winLength;
        this.emptySymbol = other.emptySymbol;
        this.cells = new Mark[size][];
        for (int r = 0; r < size; r++) {
            this.cells[r] = other.cells[r].clone();
        }
        this.filled = other.filled;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getSize() {
        return size;
    }

    public int getWinLength() {
        return winLength;
    }

    public char getEmptySymbol() {
        return emptySymbol;
    }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public boolean isFree(int row, int col) {
        return isInside(row, col) && cells[row][col] == null;
    }

    /** Знак в клетке или null, если клетка пустая. */
    public Mark getMark(int row, int col) {
        checkInside(row, col);
        return cells[row][col];
    }

    public char symbolAt(int row, int col) {
        Mark mark = getMark(row, col);
        return mark == null ? emptySymbol : mark.getSymbol();
    }

    public void place(Move move, Mark mark) {
        if (mark == null) {
            throw new IllegalArgumentException("Знак не задан");
        }
        checkInside(move.row(), move.col());
        if (cells[move.row()][move.col()] != null) {
            throw new IllegalStateException("Клетка " + move + " уже занята");
        }
        cells[move.row()][move.col()] = mark;
        filled++;
    }

    public List<Move> freeCells() {
        List<Move> result = new ArrayList<>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (cells[r][c] == null) {
                    result.add(new Move(r, c));
                }
            }
        }
        return result;
    }

    public boolean isFull() {
        return filled == size * size;
    }

    /** Есть ли на поле winLength знаков mark подряд. */
    public boolean hasLine(Mark mark) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (cells[r][c] != mark) {
                    continue;
                }
                for (int[] d : DIRECTIONS) {
                    if (countInRow(r, c, d[0], d[1], mark) >= winLength) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void clear() {
        for (Mark[] row : cells) {
            Arrays.fill(row, null);
        }
        filled = 0;
    }

    /** Независимая копия - компьютер на ней «примеряет» ходы. */
    public Board copy() {
        return new Board(this);
    }

    private int countInRow(int row, int col, int dRow, int dCol, Mark mark) {
        int count = 0;
        while (isInside(row, col) && cells[row][col] == mark) {
            count++;
            row += dRow;
            col += dCol;
        }
        return count;
    }

    private void checkInside(int row, int col) {
        if (!isInside(row, col)) {
            throw new IllegalArgumentException(
                    "Клетка " + (row + 1) + " " + (col + 1) + " за пределами поля " + size + "x" + size);
        }
    }

    public static class Builder {

        private record Preset(Move move, Mark mark) {
        }

        private int size = MIN_SIZE;
        private Integer winLength;
        private char emptySymbol = '.';
        private final List<Preset> presets = new ArrayList<>();

        private Builder() {
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        /** Сколько знаков подряд нужно для победы. По умолчанию - размер поля, но не больше 5. */
        public Builder winLength(int winLength) {
            this.winLength = winLength;
            return this;
        }

        public Builder emptySymbol(char emptySymbol) {
            this.emptySymbol = emptySymbol;
            return this;
        }

        /** Заранее поставить знак (строка и столбец с нуля). Удобно для тестов и задачек. */
        public Builder mark(int row, int col, Mark mark) {
            presets.add(new Preset(new Move(row, col), mark));
            return this;
        }

        public Board build() {
            if (size < MIN_SIZE || size > MAX_SIZE) {
                throw new IllegalArgumentException(
                        "Размер поля должен быть от " + MIN_SIZE + " до " + MAX_SIZE + ", а не " + size);
            }
            if (winLength == null) {
                winLength = Math.min(size, 5);
            }
            if (winLength < MIN_SIZE || winLength > size) {
                throw new IllegalArgumentException(
                        "Длина линии должна быть от " + MIN_SIZE + " до " + size + ", а не " + winLength);
            }
            if (Character.isWhitespace(emptySymbol)
                    || emptySymbol == Mark.X.getSymbol() || emptySymbol == Mark.O.getSymbol()) {
                throw new IllegalArgumentException("Нельзя обозначать пустую клетку символом '" + emptySymbol + "'");
            }
            return new Board(this);
        }
    }
}
