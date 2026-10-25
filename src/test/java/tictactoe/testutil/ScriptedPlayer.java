package tictactoe.testutil;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;
import tictactoe.player.Player;

/** Игрок, который делает заранее заданные ходы по порядку. */
public class ScriptedPlayer implements Player {

    private final String name;
    private final Mark mark;
    private final Deque<Move> moves;

    public ScriptedPlayer(String name, Mark mark, Move... moves) {
        this.name = name;
        this.mark = mark;
        this.moves = new ArrayDeque<>(Arrays.asList(moves));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Mark getMark() {
        return mark;
    }

    @Override
    public Move nextMove(Board board) {
        if (moves.isEmpty()) {
            throw new IllegalStateException(name + ": ходы закончились");
        }
        return moves.poll();
    }

    @Override
    public String toString() {
        return name;
    }
}
