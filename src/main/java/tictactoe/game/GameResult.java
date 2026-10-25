package tictactoe.game;

import tictactoe.player.Player;

/**
 * Итог партии. winner == null означает ничью.
 */
public record GameResult(Player winner) {

    public static GameResult draw() {
        return new GameResult(null);
    }

    public static GameResult win(Player winner) {
        return new GameResult(winner);
    }

    public boolean isDraw() {
        return winner == null;
    }
}
