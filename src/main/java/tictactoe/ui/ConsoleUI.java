package tictactoe.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import tictactoe.model.Board;

public class ConsoleUI implements UserInterface {

    private final Scanner in;
    private final PrintStream out;

    public ConsoleUI() {
        this(System.in, System.out);
    }

    public ConsoleUI(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    @Override
    public void showMessage(String message) {
        out.println(message);
    }

    @Override
    public void showBoard(Board board) {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int c = 0; c < board.getSize(); c++) {
            sb.append(String.format("%3d", c + 1));
        }
        sb.append('\n');
        for (int r = 0; r < board.getSize(); r++) {
            sb.append(String.format("%3d", r + 1));
            for (int c = 0; c < board.getSize(); c++) {
                sb.append("  ").append(board.symbolAt(r, c));
            }
            sb.append('\n');
        }
        out.println(sb);
    }

    /** Если ввод закончился (Ctrl+D), Scanner бросит NoSuchElementException - его ловит Main. */
    @Override
    public String readLine(String prompt) {
        out.print(prompt);
        out.flush();
        return in.nextLine().trim();
    }
}
