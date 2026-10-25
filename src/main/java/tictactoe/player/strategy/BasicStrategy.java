package tictactoe.player.strategy;

import java.util.Random;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;

/**
 * Простые правила по порядку: выиграть, если можно; помешать сопернику,
 * если он выигрывает следующим ходом; занять центр; иначе - случайная клетка.
 */
public class BasicStrategy implements MoveStrategy {

    private final RandomStrategy fallback;

    public BasicStrategy(Random random) {
        this.fallback = new RandomStrategy(random);
    }

    @Override
    public Move chooseMove(Board board, Mark mark) {
        Move win = findWinningMove(board, mark);
        if (win != null) {
            return win;
        }
        Move block = findWinningMove(board, mark.opposite());
        if (block != null) {
            return block;
        }
        int center = board.getSize() / 2;
        if (board.isFree(center, center)) {
            return new Move(center, center);
        }
        return fallback.chooseMove(board, mark);
    }

    /** Клетка, поставив в которую mark сразу выигрывает, или null. */
    static Move findWinningMove(Board board, Mark mark) {
        for (Move move : board.freeCells()) {
            Board copy = board.copy();
            copy.place(move, mark);
            if (copy.hasLine(mark)) {
                return move;
            }
        }
        return null;
    }
}
