package tictactoe.player.strategy;

import java.util.List;
import java.util.Objects;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;

/**
 * Минимакс с альфа-бета отсечением: перебирает партию до конца и не проигрывает.
 * Полный перебор делаем, только когда свободных клеток не больше FULL_SEARCH_LIMIT
 * (на поле 3x3 - всегда). На большом пустом поле перебор слишком долгий,
 * там ход выбирает запасная стратегия.
 */
public class MinimaxStrategy implements MoveStrategy {

    static final int FULL_SEARCH_LIMIT = 9;
    private static final int WIN_SCORE = 100;

    private final MoveStrategy fallback;

    public MinimaxStrategy(MoveStrategy fallback) {
        this.fallback = Objects.requireNonNull(fallback, "fallback");
    }

    @Override
    public Move chooseMove(Board board, Mark mark) {
        List<Move> free = board.freeCells();
        if (free.isEmpty()) {
            throw new IllegalStateException("На поле не осталось свободных клеток");
        }
        if (free.size() > FULL_SEARCH_LIMIT) {
            return fallback.chooseMove(board, mark);
        }

        Move best = null;
        int bestScore = Integer.MIN_VALUE;
        for (Move move : free) {
            Board next = board.copy();
            next.place(move, mark);
            int score = minimax(next, mark.opposite(), mark, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                best = move;
            }
        }
        return best;
    }

    // Оценка позиции для игрока me. Чем быстрее победа, тем выше оценка,
    // поэтому компьютер не «тянет» выигранную партию и подольше оттягивает проигрыш.
    private int minimax(Board board, Mark turn, Mark me, int depth, int alpha, int beta) {
        if (board.hasLine(me)) {
            return WIN_SCORE - depth;
        }
        if (board.hasLine(me.opposite())) {
            return depth - WIN_SCORE;
        }
        if (board.isFull()) {
            return 0;
        }

        boolean myTurn = turn == me;
        int best = myTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Move move : board.freeCells()) {
            Board next = board.copy();
            next.place(move, turn);
            int score = minimax(next, turn.opposite(), me, depth + 1, alpha, beta);
            if (myTurn) {
                best = Math.max(best, score);
                alpha = Math.max(alpha, best);
            } else {
                best = Math.min(best, score);
                beta = Math.min(beta, best);
            }
            if (beta <= alpha) {
                break;
            }
        }
        return best;
    }
}
