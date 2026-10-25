package tictactoe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BoardBuilderTest {

    @Test
    void defaultBoardIsEmptyThreeByThree() {
        Board board = Board.builder().build();

        assertEquals(3, board.getSize());
        assertEquals(3, board.getWinLength());
        assertEquals('.', board.getEmptySymbol());
        assertEquals(9, board.freeCells().size());
    }

    @Test
    void customSizeAndWinLength() {
        Board board = Board.builder().size(7).winLength(4).emptySymbol('-').build();

        assertEquals(7, board.getSize());
        assertEquals(4, board.getWinLength());
        assertEquals('-', board.symbolAt(0, 0));
    }

    @Test
    void winLengthDefaultsToFiveOnBigBoard() {
        assertEquals(4, Board.builder().size(4).build().getWinLength());
        assertEquals(5, Board.builder().size(10).build().getWinLength());
    }

    @Test
    void presetMarksArePlaced() {
        Board board = Board.builder()
                .mark(0, 0, Mark.X)
                .mark(1, 1, Mark.O)
                .build();

        assertEquals(Mark.X, board.getMark(0, 0));
        assertEquals(Mark.O, board.getMark(1, 1));
        assertNull(board.getMark(2, 2));
        assertEquals(7, board.freeCells().size());
    }

    @Test
    void rejectsWrongSize() {
        assertThrows(IllegalArgumentException.class, () -> Board.builder().size(2).build());
        assertThrows(IllegalArgumentException.class, () -> Board.builder().size(11).build());
    }

    @Test
    void rejectsWinLengthLongerThanBoard() {
        assertThrows(IllegalArgumentException.class, () -> Board.builder().size(4).winLength(5).build());
        assertThrows(IllegalArgumentException.class, () -> Board.builder().size(5).winLength(2).build());
    }

    @Test
    void rejectsEmptySymbolThatLooksLikeMark() {
        assertThrows(IllegalArgumentException.class, () -> Board.builder().emptySymbol('X').build());
        assertThrows(IllegalArgumentException.class, () -> Board.builder().emptySymbol(' ').build());
    }

    @Test
    void rejectsBadPresets() {
        assertThrows(IllegalStateException.class,
                () -> Board.builder().mark(0, 0, Mark.X).mark(0, 0, Mark.O).build());
        assertThrows(IllegalArgumentException.class, () -> Board.builder().mark(3, 0, Mark.X).build());
    }

    @Test
    void eachBuildGivesNewBoard() {
        Board.Builder builder = Board.builder().size(3);
        Board a = builder.build();
        Board b = builder.build();

        a.place(new Move(0, 0), Mark.X);

        assertTrue(b.isFree(0, 0));
    }
}
