package tictactoe.testutil;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

import tictactoe.model.Board;
import tictactoe.ui.UserInterface;

/** Интерфейс без консоли: отвечает заготовленными строками и запоминает, что ему показали. */
public class FakeUI implements UserInterface {

    private final Deque<String> answers;
    private final List<String> messages = new ArrayList<>();
    private int boardsShown;

    public FakeUI(String... answers) {
        this.answers = new ArrayDeque<>(Arrays.asList(answers));
    }

    @Override
    public void showMessage(String message) {
        messages.add(message);
    }

    @Override
    public void showBoard(Board board) {
        boardsShown++;
    }

    @Override
    public String readLine(String prompt) {
        if (answers.isEmpty()) {
            throw new NoSuchElementException("Ответы закончились");
        }
        return answers.poll();
    }

    public List<String> getMessages() {
        return messages;
    }

    public int getBoardsShown() {
        return boardsShown;
    }

    public int answersLeft() {
        return answers.size();
    }
}
