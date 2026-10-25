package tictactoe.player.strategy;

import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.model.Move;

public interface MoveStrategy {

    /** Выбрать свободную клетку для знака mark. Переданное поле менять нельзя. */
    Move chooseMove(Board board, Mark mark);
}
