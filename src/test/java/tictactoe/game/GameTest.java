package tictactoe.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;
import tictactoe.player.Player;
import tictactoe.testutil.FakeUI;
import tictactoe.testutil.ScriptedPlayer;

class GameTest {

    private FakeUI ui;

    @BeforeEach
    void setUp() {
        Game.resetInstance();
        ui = new FakeUI();
    }

    @AfterEach
    void tearDown() {
        Game.resetInstance();
    }

    @Test
    void getInstanceBeforeInitFails() {
        assertThrows(IllegalStateException.class, Game::getInstance);
    }

    @Test
    void onlyOneInstance() {
        Game game = Game.init(Board.builder().build(), player("A", Mark.X), player("B", Mark.O), ui);

        assertSame(game, Game.getInstance());
        assertSame(game, Game.getInstance());
        assertThrows(IllegalStateException.class,
                () -> Game.init(Board.builder().build(), player("A", Mark.X), player("B", Mark.O), ui));
    }

    @Test
    void usesInjectedDependencies() {
        Board board = Board.builder().build();
        Player first = player("A", Mark.X);
        Player second = player("B", Mark.O);

        Game game = Game.init(board, first, second, ui);

        assertSame(board, game.getBoard());
        assertSame(first, game.getFirst());
        assertSame(second, game.getSecond());
    }

    @Test
    void playersMustHaveDifferentMarks() {
        assertThrows(IllegalArgumentException.class,
                () -> Game.init(Board.builder().build(), player("A", Mark.X), player("B", Mark.X), ui));
    }

    @Test
    void firstPlayerWinsByRow() {
        Player first = player("A", Mark.X, new Move(0, 0), new Move(0, 1), new Move(0, 2));
        Player second = player("B", Mark.O, new Move(1, 0), new Move(1, 1));
        Game game = Game.init(Board.builder().build(), first, second, ui);

        GameResult result = game.play();

        assertSame(first, result.winner());
        assertEquals(1, game.getWins(first));
        assertEquals(0, game.getWins(second));
        // поле показано в начале и после каждого из пяти ходов
        assertEquals(6, ui.getBoardsShown());
    }

    @Test
    void drawWhenBoardIsFull() {
        // X O X
        // X O O
        // O X X
        Player first = player("A", Mark.X,
                new Move(0, 0), new Move(0, 2), new Move(1, 0), new Move(2, 1), new Move(2, 2));
        Player second = player("B", Mark.O,
                new Move(0, 1), new Move(1, 1), new Move(2, 0), new Move(1, 2));
        Game game = Game.init(Board.builder().build(), first, second, ui);

        GameResult result = game.play();

        assertTrue(result.isDraw());
        assertEquals(1, game.getDraws());
    }

    @Test
    void playContinuesFromPresetBoard() {
        // у O уже две в ряд, X ходит мимо - O заканчивает линию
        Board board = Board.builder().mark(2, 0, Mark.O).mark(2, 1, Mark.O).mark(0, 0, Mark.X).build();
        Player first = player("A", Mark.X, new Move(0, 1));
        Player second = player("B", Mark.O, new Move(2, 2));
        Game game = Game.init(board, first, second, ui);

        assertSame(second, game.play().winner());
    }

    @Test
    void restartClearsBoardAndSwapsFirstMove() {
        Player first = player("A", Mark.X, new Move(0, 0), new Move(0, 1), new Move(0, 2));
        Player second = player("B", Mark.O, new Move(1, 0), new Move(1, 1));
        Game game = Game.init(Board.builder().build(), first, second, ui);
        game.play();

        game.restart();

        assertEquals(9, game.getBoard().freeCells().size());
        assertSame(second, game.getCurrentPlayer());
        // счёт после новой партии не сбрасывается
        assertEquals(1, game.getWins(first));
    }

    private static Player player(String name, Mark mark, Move... moves) {
        return new ScriptedPlayer(name, mark, moves);
    }
}
