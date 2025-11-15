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
    private static final String QUESTION_1_TEXT = "Вычислите степень: 10^2";
    private static final String QUESTION_1_ANS  = "100";
    private static final String QUESTION_2_TEXT = "Сколько будет 2 + 2 * 2";
    private static final String QUESTION_2_ANS = "6";

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

        Assertions.assertTrue(
                user.getCurrentWrongAnswerQuestion().isEmpty(),
                "После правильного ответа список неправильных вопросов должен быть пустым"
        );

        Assertions.assertEquals(
                QUESTION_1_TEXT,
                bot.lastMessage(chat),
                "После команды /test ожидали первый вопрос"
        );

        logic.processCommand(user, QUESTION_1_ANS);

        Assertions.assertEquals(
                QUESTION_2_TEXT,
                bot.lastMessage(chat),
                "После правильного ответа ожидали следующий вопрос"
        );

        logic.processCommand(user, QUESTION_2_ANS);
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
                QUESTION_1_TEXT,
                bot.lastMessage(chat),
                "После команды /test ожидали первый вопрос"
        );

        logic.processCommand(user, "неверно");

        Assertions.assertTrue(
                user.getCurrentWrongAnswerQuestion().isPresent(),
                "После неправильного ответа ожидаем, что текущий неправильный вопрос существует"
        );

        Assertions.assertEquals(
                QUESTION_2_TEXT,
                bot.lastMessage(chat),
                "После неправильного ответа ожидали следующий вопрос"
        );
    }
}
