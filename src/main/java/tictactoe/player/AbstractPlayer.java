package tictactoe.player;

import java.util.Objects;

import tictactoe.model.Mark;

abstract class AbstractPlayer implements Player {

    private final String name;
    private final Mark mark;

    protected AbstractPlayer(String name, Mark mark) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("У игрока должно быть имя");
        }
        this.name = name;
        this.mark = Objects.requireNonNull(mark, "mark");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Mark getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return name + " (" + mark.getSymbol() + ")";
    }
}
