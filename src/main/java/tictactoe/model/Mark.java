package tictactoe.model;

public enum Mark {
    X('X'),
    O('O');

    private final char symbol;

    Mark(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public Mark opposite() {
        return this == X ? O : X;
    }
}
