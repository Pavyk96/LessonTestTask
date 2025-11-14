package example.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


/**
 * Тесты команды /test
 *
 * @author Daniil Mezev
 */
class TestCommandTest {

    private static final String CMD_TEST = "/test";
    private static final String Q1_TEXT = "Вычислите степень: 10^2";
    private static final String Q1_ANS  = "100";
    private static final String Q2_TEXT = "Сколько будет 2 + 2 * 2";
    private static final String Q2_ANS = "6";

    /**
     * Правильный ответ на вопрос + выводится следующий вопрос
     */
    @Test
    void testAnswerTrue() {
        Long chat = 11L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);

        Assertions.assertEquals(
                Q1_TEXT,
                bot.lastMessage(chat),
                "После команды /test ожидали первый вопрос"
        );

        logic.processCommand(user, Q1_ANS);

        Assertions.assertEquals(
                Q2_TEXT,
                bot.lastMessage(chat),
                "После правильного ответа ожидали следующий вопрос"
        );

        logic.processCommand(user, Q2_ANS);
        Assertions.assertEquals(
                "Тест завершен",
                bot.lastMessage(chat),
                "Тест не завершился"
        );
    }

    /**
     * Неправильный ответ на вопрос
     */
    @Test
    void testAnswerFalse() {
        Long chat = 12L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        Assertions.assertEquals(State.TEST, user.getState());
        Assertions.assertEquals(
                Q1_TEXT,
                bot.lastMessage(chat),
                "После команды /test ожидали первый вопрос"
        );

        logic.processCommand(user, "неверно");

        Assertions.assertTrue(
                bot.hasMessage(chat, "Вы ошиблись, верный ответ: " + Q1_ANS),
                "Не верный вывод"
        );
        Assertions.assertEquals(
                Q2_TEXT,
                bot.lastMessage(chat),
                "После неправильного ответа ожидали следующий вопрос"
        );
    }
}
