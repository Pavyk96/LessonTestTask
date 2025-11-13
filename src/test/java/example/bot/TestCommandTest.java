package example.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * Тесты команды /test
 *
 * @author Daniil Mezev
 */
class TestCommandTest {

    private final String CMD_TEST = "/test";

    private final String Q1_TEXT = "Вычислите степень: 10^2";
    private final String Q1_ANS  = "100";
    private final String Q2_TEXT = "Сколько будет 2 + 2 * 2";
    private final String Q2_ANS  = "6";

    /**
     * Правильный ответ на вопрос
     */
    @Test
    void testAnswerTrue() {
        Long chat = 11L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        Assertions.assertEquals(State.TEST, user.getState());

        List<String> historyAfterStart = bot.messagesOf(chat);
        Assertions.assertEquals(
                List.of(Q1_TEXT),
                historyAfterStart,
                "После команды /test ожидали первый вопрос"
        );

        logic.processCommand(user, Q1_ANS);

        List<String> historyAfterAnswer = bot.messagesOf(chat);
        Assertions.assertEquals(
                List.of(
                        Q1_TEXT,
                        "Правильный ответ!",
                        Q2_TEXT
                ),
                historyAfterAnswer,
                "После правильного ответа ожидали следущий вопрос"
        );
    }

    /**
     * Не правильный ответ на вопрос
     */
    @Test
    void testAnswerFalse() {
        Long chat = 12L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        Assertions.assertEquals(State.TEST, user.getState());
        Assertions.assertTrue(bot.hasMessage(chat, Q1_TEXT));

        logic.processCommand(user, "неверно");

        Assertions.assertTrue(bot.hasMessage(chat, "Вы ошиблись, верный ответ: " + Q1_ANS),
                "Не верный вывод");
        Assertions.assertTrue(bot.hasMessage(chat, Q2_TEXT),
                "Следущий вопрос не вывелся");

    }

}
