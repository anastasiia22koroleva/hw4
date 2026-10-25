package tictactoe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BoardTest {

    @Test
    void placeAndRead() {
        Board board = Board.builder().build();

        board.place(new Move(1, 2), Mark.O);

        assertEquals(Mark.O, board.getMark(1, 2));
        assertEquals('O', board.symbolAt(1, 2));
        assertFalse(board.isFree(1, 2));
    }

    @Test
    void cannotPlaceTwiceOrOutside() {
        Board board = Board.builder().build();
        board.place(new Move(0, 0), Mark.X);

        assertThrows(IllegalStateException.class, () -> board.place(new Move(0, 0), Mark.O));
        assertThrows(IllegalArgumentException.class, () -> board.place(new Move(-1, 0), Mark.O));
        assertThrows(IllegalArgumentException.class, () -> board.place(new Move(0, 3), Mark.O));
    }

    @Test
    void findsRowColumnAndBothDiagonals() {
        assertTrue(Board.builder().mark(1, 0, Mark.X).mark(1, 1, Mark.X).mark(1, 2, Mark.X)
                .build().hasLine(Mark.X));
        assertTrue(Board.builder().mark(0, 2, Mark.O).mark(1, 2, Mark.O).mark(2, 2, Mark.O)
                .build().hasLine(Mark.O));
        assertTrue(Board.builder().mark(0, 0, Mark.X).mark(1, 1, Mark.X).mark(2, 2, Mark.X)
                .build().hasLine(Mark.X));
        assertTrue(Board.builder().mark(0, 2, Mark.X).mark(1, 1, Mark.X).mark(2, 0, Mark.X)
                .build().hasLine(Mark.X));
    }

    @Test
    void noLineWhenBroken() {
        Board board = Board.builder()
                .mark(0, 0, Mark.X).mark(0, 1, Mark.O).mark(0, 2, Mark.X)
                .build();

        assertFalse(board.hasLine(Mark.X));
        assertFalse(board.hasLine(Mark.O));
    }

    @Test
    void lineLengthDependsOnWinLength() {
        Board board = Board.builder().size(6).winLength(4)
                .mark(2, 1, Mark.O).mark(3, 2, Mark.O).mark(4, 3, Mark.O)
                .build();
        assertFalse(board.hasLine(Mark.O));

        board.place(new Move(5, 4), Mark.O);
        assertTrue(board.hasLine(Mark.O));
    }

    @Test
    void fullAndClear() {
        Board board = Board.builder().build();
        for (Move move : board.freeCells()) {
            board.place(move, Mark.X);
        }
        assertTrue(board.isFull());

        board.clear();
        assertFalse(board.isFull());
        assertEquals(9, board.freeCells().size());
    }

    @Test
    void copyDoesNotAffectOriginal() {
        Board board = Board.builder().mark(0, 0, Mark.X).build();
        Board copy = board.copy();

        copy.place(new Move(1, 1), Mark.O);

        assertTrue(board.isFree(1, 1));
        assertEquals(Mark.X, copy.getMark(0, 0));
        assertEquals(8, board.freeCells().size());
        assertEquals(7, copy.freeCells().size());
    }
}
