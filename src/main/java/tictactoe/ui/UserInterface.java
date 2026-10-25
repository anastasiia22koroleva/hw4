package tictactoe.ui;

import tictactoe.model.Board;

/**
 * Всё общение с пользователем идёт через этот интерфейс.
 * В игре это консоль, в тестах - подставной объект со списком заготовленных ответов.
 */
public interface UserInterface {

    void showMessage(String message);

    void showBoard(Board board);

    String readLine(String prompt);
}
