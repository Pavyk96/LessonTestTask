package example.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Тесты команды /repeat
 *
 * @author Daniil Mezev
 */
class RepeatCommandTest {

    private final String CMD_TEST = "/test";
    private final String CMD_REPEAT = "/repeat";

    private final String Q1_TEXT = "Вычислите степень: 10^2";
    private final String Q1_ANS  = "100";

    /**
     * Нет вопросов для повторения
     */
    @Test
    void testEmptyRepeatQueue() {
        Long chat = 41L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_REPEAT);

        Assertions.assertTrue(
                bot.hasMessage(chat, "Нет вопросов для повторения"),
                "Бот не отправил сообщение 'Нет вопросов для повторения'. "
        );
        Assertions.assertEquals(State.INIT, user.getState());
    }

    /**
     * Есть вопрос для повторения
     * Проверяем, что при двойном неправильном ответе на вопрос,
     * при команде репит, вопрос будет выводиться 1 раз
     */
    @Test
    void testTwoSameQuestionToRepeat() {
        Long chat = 42L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        logic.processCommand(user, "неверно");
        logic.processCommand(user, CMD_TEST);
        logic.processCommand(user, "неверно");

        logic.processCommand(user, CMD_REPEAT);

        Assertions.assertEquals(State.REPEAT, user.getState());
        Assertions.assertTrue(
                bot.hasMessage(chat, Q1_TEXT),
                "Нет вопроса для повторения"
        );

        logic.processCommand(user, Q1_ANS);
        logic.processCommand(user, CMD_REPEAT);

        Assertions.assertTrue(
                bot.hasMessage(chat, "Нет вопросов для повторения"),
                "Вывелся лишний вопрос"
        );
    }

}
