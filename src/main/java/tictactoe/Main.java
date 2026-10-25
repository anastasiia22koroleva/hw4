package tictactoe;

import java.util.NoSuchElementException;
import java.util.Random;

import tictactoe.game.Game;
import tictactoe.model.Board;
import tictactoe.model.Mark;
import tictactoe.player.Player;
import tictactoe.player.PlayerFactory;
import tictactoe.player.PlayerType;
import tictactoe.ui.ConsoleUI;
import tictactoe.ui.SetupDialog;
import tictactoe.ui.UserInterface;

/**
 * Здесь собираются все объекты и связываются между собой.
 * Остальные классы получают зависимости через конструктор и сами их не создают.
 */
public class Main {

    public static void main(String[] args) {
        UserInterface ui = new ConsoleUI();
        try {
            run(ui);
        } catch (NoSuchElementException e) {
            ui.showMessage("");
            ui.showMessage("Ввод закончился, игра остановлена.");
        }
    }

    private static void run(UserInterface ui) {
        ui.showMessage("=== Крестики-нолики ===");
        ui.showMessage("Ход вводится как строка и столбец через пробел, например: 2 3");
        ui.showMessage("");

        SetupDialog setup = new SetupDialog(ui);
        PlayerFactory factory = new PlayerFactory(ui, new Random());

        Board board = setup.askBoard();
        Player first = setup.askPlayer(factory, Mark.X, PlayerType.HUMAN);
        Player second = setup.askPlayer(factory, Mark.O, PlayerType.AI_HARD);

        Game game = Game.init(board, first, second, ui);
        while (true) {
            ui.showMessage("");
            ui.showMessage("Поле " + board.getSize() + "x" + board.getSize() + ", для победы нужно "
                    + board.getWinLength() + " в ряд. Первым ходит " + game.getCurrentPlayer() + ".");
            game.play();
            ui.showMessage(game.scoreLine());
            if (!setup.askPlayAgain()) {
                break;
            }
            game.restart();
        }
        ui.showMessage("Спасибо за игру!");
    }
}
