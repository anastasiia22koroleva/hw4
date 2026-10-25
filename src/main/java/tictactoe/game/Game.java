package tictactoe.game;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;
import tictactoe.player.Player;
import tictactoe.ui.UserInterface;

/**
 * Партия и счёт между партиями. Экземпляр может быть только один:
 * создаётся через {@link #init}, дальше берётся через {@link #getInstance()}.
 *
 * Сама Game ничего не создаёт - поле, игроков и интерфейс ей передают в конструктор,
 * поэтому в тестах вместо консоли и живых игроков легко подставить свои.
 */
public final class Game {

    private static Game instance;

    private final Board board;
    private final Player first;
    private final Player second;
    private final UserInterface ui;

    private final Map<Mark, Integer> wins = new EnumMap<>(Mark.class);
    private int draws;

    private Player starting;
    private Player current;

    private Game(Board board, Player first, Player second, UserInterface ui) {
        this.board = Objects.requireNonNull(board, "board");
        this.first = Objects.requireNonNull(first, "first");
        this.second = Objects.requireNonNull(second, "second");
        this.ui = Objects.requireNonNull(ui, "ui");
        if (first.getMark() == second.getMark()) {
            throw new IllegalArgumentException("У игроков должны быть разные знаки");
        }
        this.starting = first;
        this.current = first;
    }

    public static synchronized Game init(Board board, Player first, Player second, UserInterface ui) {
        if (instance != null) {
            throw new IllegalStateException("Игра уже создана, используйте Game.getInstance()");
        }
        instance = new Game(board, first, second, ui);
        return instance;
    }

    public static synchronized Game getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Игра ещё не создана, сначала вызовите Game.init(...)");
        }
        return instance;
    }

    // только для тестов: каждый тест начинает без созданной игры
    static synchronized void resetInstance() {
        instance = null;
    }

    /** Играет партию до победы или ничьей с текущего состояния поля. */
    public GameResult play() {
        ui.showBoard(board);
        Optional<GameResult> result = checkResult();
        while (result.isEmpty()) {
            Move move = current.nextMove(board);
            board.place(move, current.getMark());
            ui.showMessage(current + " ставит " + current.getMark().getSymbol() + " в клетку " + move);
            ui.showBoard(board);

            result = checkResult();
            if (result.isEmpty()) {
                current = opponentOf(current);
            }
        }
        record(result.get());
        return result.get();
    }

    /** Новая партия на том же поле. Начинает тот, кто в прошлый раз ходил вторым. */
    public void restart() {
        board.clear();
        starting = opponentOf(starting);
        current = starting;
    }

    public Board getBoard() {
        return board;
    }

    public Player getFirst() {
        return first;
    }

    public Player getSecond() {
        return second;
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public int getWins(Player player) {
        return wins.getOrDefault(player.getMark(), 0);
    }

    public int getDraws() {
        return draws;
    }

    public String scoreLine() {
        return "Счёт: " + first + " " + getWins(first) + " - " + getWins(second) + " " + second
                + ", ничьих: " + draws;
    }

    private Optional<GameResult> checkResult() {
        for (Player player : new Player[] {first, second}) {
            if (board.hasLine(player.getMark())) {
                return Optional.of(GameResult.win(player));
            }
        }
        if (board.isFull()) {
            return Optional.of(GameResult.draw());
        }
        return Optional.empty();
    }

    private void record(GameResult result) {
        if (result.isDraw()) {
            draws++;
            ui.showMessage("Ничья!");
        } else {
            wins.merge(result.winner().getMark(), 1, Integer::sum);
            ui.showMessage("Победил " + result.winner() + "!");
        }
    }

    private Player opponentOf(Player player) {
        return player == first ? second : first;
    }
}
