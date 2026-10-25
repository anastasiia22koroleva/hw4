package tictactoe.player;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;

public interface Player {

    String getName();

    Mark getMark();

    /** Выбрать ход. Поле не меняется - ставит знак сама игра. */
    Move nextMove(Board board);
}
