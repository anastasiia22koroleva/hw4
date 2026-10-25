package tictactoe.player.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;

class StrategyTest {

    private final Random random = new Random(42);

    @Test
    void randomAlwaysPicksFreeCell() {
        RandomStrategy strategy = new RandomStrategy(random);
        Board board = Board.builder()
                .mark(0, 0, Mark.X).mark(0, 1, Mark.O).mark(0, 2, Mark.X)
                .mark(1, 0, Mark.O).mark(1, 1, Mark.X).mark(1, 2, Mark.O)
                .mark(2, 0, Mark.O).mark(2, 1, Mark.X)
                .build();

        for (int i = 0; i < 20; i++) {
            assertEquals(new Move(2, 2), strategy.chooseMove(board, Mark.O));
        }
    }

    @Test
    void basicTakesWinFirst() {
        // у O две в ряд сверху, у X - две в ряд в середине; ходит O и должен выиграть, а не блокировать
        Board board = Board.builder()
                .mark(0, 0, Mark.O).mark(0, 1, Mark.O)
                .mark(1, 0, Mark.X).mark(1, 1, Mark.X)
                .build();

        assertEquals(new Move(0, 2), new BasicStrategy(random).chooseMove(board, Mark.O));
    }

    @Test
    void basicBlocksOpponent() {
        Board board = Board.builder()
                .mark(0, 0, Mark.X).mark(1, 0, Mark.X)
                .mark(1, 1, Mark.O)
                .build();

        assertEquals(new Move(2, 0), new BasicStrategy(random).chooseMove(board, Mark.O));
    }

    @Test
    void basicTakesCenterOnEmptyBoard() {
        assertEquals(new Move(2, 2), new BasicStrategy(random).chooseMove(Board.builder().size(5).build(), Mark.X));
    }

    @Test
    void minimaxDoesNotFallIntoFork() {
        // X в противоположных углах, O в центре. Угол для O проигрывает (X делает вилку),
        // правильный ответ - любая клетка на стороне.
        Board board = Board.builder()
                .mark(0, 0, Mark.X).mark(1, 1, Mark.O).mark(2, 2, Mark.X)
                .build();

        Move move = hard().chooseMove(board, Mark.O);

        boolean isSide = (move.row() + move.col()) % 2 == 1;
        assertTrue(isSide, "ожидали ход на сторону, а получили " + move);
    }

    @Test
    void twoHardPlayersAlwaysDraw() {
        Board board = Board.builder().build();
        MoveStrategy strategy = hard();
        Mark turn = Mark.X;
        while (!board.isFull() && !board.hasLine(Mark.X) && !board.hasLine(Mark.O)) {
            board.place(strategy.chooseMove(board, turn), turn);
            turn = turn.opposite();
        }

        assertFalse(board.hasLine(Mark.X));
        assertFalse(board.hasLine(Mark.O));
    }

    @Test
    void hardNeverLosesToRandomPlayer() {
        MoveStrategy hardStrategy = hard();
        MoveStrategy randomStrategy = new RandomStrategy(random);
        for (int game = 0; game < 30; game++) {
            Board board = Board.builder().build();
            // по очереди начинает то один, то другой
            Mark hardMark = game % 2 == 0 ? Mark.X : Mark.O;
            Mark turn = Mark.X;
            while (!board.isFull() && !board.hasLine(Mark.X) && !board.hasLine(Mark.O)) {
                MoveStrategy s = turn == hardMark ? hardStrategy : randomStrategy;
                board.place(s.chooseMove(board, turn), turn);
                turn = turn.opposite();
            }
            assertFalse(board.hasLine(hardMark.opposite()), "компьютер проиграл в партии " + game);
        }
    }

    @Test
    void hardUsesFallbackOnBigEmptyBoard() {
        // запасная стратегия всегда отвечает одной и той же клеткой - по ней и видно, что сработала она
        Move marker = new Move(6, 6);
        MinimaxStrategy strategy = new MinimaxStrategy((board, mark) -> marker);

        assertEquals(marker, strategy.chooseMove(Board.builder().size(7).build(), Mark.X));
    }

    @Test
    void strategiesDoNotChangeBoard() {
        Board board = Board.builder().mark(0, 0, Mark.X).build();

        hard().chooseMove(board, Mark.O);
        new BasicStrategy(random).chooseMove(board, Mark.O);

        assertEquals(8, board.freeCells().size());
    }

    private MoveStrategy hard() {
        return new MinimaxStrategy(new BasicStrategy(random));
    }
}
