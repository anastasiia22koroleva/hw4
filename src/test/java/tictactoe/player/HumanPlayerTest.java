package tictactoe.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;
import tictactoe.testutil.FakeUI;

class HumanPlayerTest {

    @Test
    void parsesMoveFromOneBasedInput() {
        assertEquals(new Move(1, 2), HumanPlayer.parseMove("2 3", 3));
        assertEquals(new Move(0, 0), HumanPlayer.parseMove(" 1,1 ", 3));
        assertEquals(new Move(9, 9), HumanPlayer.parseMove("10 10", 10));
    }

    @Test
    void rejectsBadInput() {
        assertThrows(IllegalArgumentException.class, () -> HumanPlayer.parseMove("", 3));
        assertThrows(IllegalArgumentException.class, () -> HumanPlayer.parseMove("2", 3));
        assertThrows(IllegalArgumentException.class, () -> HumanPlayer.parseMove("a b", 3));
        assertThrows(IllegalArgumentException.class, () -> HumanPlayer.parseMove("0 1", 3));
        assertThrows(IllegalArgumentException.class, () -> HumanPlayer.parseMove("4 1", 3));
    }

    @Test
    void asksAgainUntilMoveIsValid() {
        Board board = Board.builder().mark(0, 0, Mark.O).build();
        FakeUI ui = new FakeUI("привет", "5 5", "1 1", "2 2");
        HumanPlayer player = new HumanPlayer("Аня", Mark.X, ui);

        Move move = player.nextMove(board);

        assertEquals(new Move(1, 1), move);
        assertEquals(3, ui.getMessages().size());
        assertEquals(0, ui.answersLeft());
    }
}
