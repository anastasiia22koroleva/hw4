package tictactoe.player;

import java.util.Objects;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;
import tictactoe.player.strategy.MoveStrategy;

/**
 * Компьютерный игрок. Как именно он выбирает ход, решает стратегия,
 * которую ему передаёт фабрика, - от неё и зависит сложность.
 */
public class AIPlayer extends AbstractPlayer {

    private final MoveStrategy strategy;

    public AIPlayer(String name, Mark mark, MoveStrategy strategy) {
        super(name, mark);
        this.strategy = Objects.requireNonNull(strategy, "strategy");
    }

    public MoveStrategy getStrategy() {
        return strategy;
    }

    @Override
    public Move nextMove(Board board) {
        return strategy.chooseMove(board, getMark());
    }
}
