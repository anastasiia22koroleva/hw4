package tictactoe.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import tictactoe.model.Mark;
import tictactoe.player.strategy.BasicStrategy;
import tictactoe.player.strategy.MinimaxStrategy;
import tictactoe.player.strategy.RandomStrategy;
import tictactoe.testutil.FakeUI;

class PlayerFactoryTest {

    private final PlayerFactory factory = new PlayerFactory(new FakeUI(), new Random(1));

    @Test
    void createsHuman() {
        Player player = factory.create(PlayerType.HUMAN, "Аня", Mark.X);

        assertInstanceOf(HumanPlayer.class, player);
        assertEquals("Аня", player.getName());
        assertEquals(Mark.X, player.getMark());
    }

    @Test
    void createsComputerWithStrategyForDifficulty() {
        assertInstanceOf(RandomStrategy.class, strategyOf(factory.create(PlayerType.AI_EASY, "Бот", Mark.O)));
        assertInstanceOf(BasicStrategy.class, strategyOf(factory.create(PlayerType.AI_MEDIUM, "Бот", Mark.O)));
        assertInstanceOf(MinimaxStrategy.class, strategyOf(factory.create(PlayerType.AI_HARD, "Бот", Mark.O)));
    }

    @Test
    void acceptsTypeAsString() {
        assertInstanceOf(HumanPlayer.class, factory.create("human", "Аня", Mark.X));
        assertInstanceOf(HumanPlayer.class, factory.create("1", "Аня", Mark.X));
        assertInstanceOf(MinimaxStrategy.class, strategyOf(factory.create("HARD", "Бот", Mark.O)));
        assertInstanceOf(BasicStrategy.class, strategyOf(factory.create("ai", "Бот", Mark.O)));
    }

    @Test
    void unknownTypeIsError() {
        assertThrows(IllegalArgumentException.class, () -> factory.create("robot", "Бот", Mark.O));
        assertThrows(IllegalArgumentException.class, () -> factory.create("5", "Бот", Mark.O));
    }

    @Test
    void emptyNameGetsDefault() {
        assertEquals("Игрок", factory.create(PlayerType.HUMAN, "  ", Mark.X).getName());
        assertEquals("Компьютер", factory.create(PlayerType.AI_EASY, null, Mark.O).getName());
    }

    private static Object strategyOf(Player player) {
        return assertInstanceOf(AIPlayer.class, player).getStrategy();
    }
}
