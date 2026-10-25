package tictactoe.player.strategy;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;

public class RandomStrategy implements MoveStrategy {

    private final Random random;

    public RandomStrategy(Random random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    @Override
    public Move chooseMove(Board board, Mark mark) {
        List<Move> free = board.freeCells();
        if (free.isEmpty()) {
            throw new IllegalStateException("На поле не осталось свободных клеток");
        }
        return free.get(random.nextInt(free.size()));
    }
}
