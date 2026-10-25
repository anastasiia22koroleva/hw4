package tictactoe.player;

import java.util.Objects;
import java.util.Random;

import tictactoe.model.Mark;
import tictactoe.player.strategy.BasicStrategy;
import tictactoe.player.strategy.MinimaxStrategy;
import tictactoe.player.strategy.RandomStrategy;
import tictactoe.ui.UserInterface;

/**
 * Единственное место, где создаются игроки. Чтобы добавить новый вид игрока,
 * достаточно нового значения в PlayerType и одной ветки здесь - Game об этом не узнает.
 */
public class PlayerFactory {

    private final UserInterface ui;
    private final Random random;

    public PlayerFactory(UserInterface ui, Random random) {
        this.ui = Objects.requireNonNull(ui, "ui");
        this.random = Objects.requireNonNull(random, "random");
    }

    public Player create(PlayerType type, String name, Mark mark) {
        Objects.requireNonNull(type, "type");
        if (name == null || name.isBlank()) {
            name = type.isHuman() ? "Игрок" : "Компьютер";
        }
        return switch (type) {
            case HUMAN -> new HumanPlayer(name, mark, ui);
            case AI_EASY -> new AIPlayer(name, mark, new RandomStrategy(random));
            case AI_MEDIUM -> new AIPlayer(name, mark, new BasicStrategy(random));
            case AI_HARD -> new AIPlayer(name, mark, new MinimaxStrategy(new BasicStrategy(random)));
        };
    }

    /** То же самое, но тип приходит строкой - как его вводят в консоли. */
    public Player create(String type, String name, Mark mark) {
        return create(PlayerType.parse(type), name, mark);
    }
}
