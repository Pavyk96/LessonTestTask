package example.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * Тесты команды /repeat
 *
 * @author Daniil Mezev
 */
class RepeatCommandTest {

    private static final String CMD_TEST = "/test";
    private static final String CMD_REPEAT = "/repeat";
    private static final String QUESTION_1_TEXT = "Вычислите степень: 10^2";
    private static final String QUESTION_1_ANS  = "100";

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

        Assertions.assertTrue(
                bot.hasMessage(chat, QUESTION_1_TEXT),
                "Нет вопроса для повторения"
        );

        logic.processCommand(user, QUESTION_1_ANS);
        logic.processCommand(user, CMD_REPEAT);

        Assertions.assertTrue(
                bot.hasMessage(chat, "Нет вопросов для повторения"),
                "Вывелся лишний вопрос"
        );
    }

    /**
     * Вопрос не уходит на повтор после положительного ответа:
     * если сразу ответить верно в режиме /test, /repeat не находит вопросов
     */
    @Test
    void testQuestionNotAddedToRepeatAfterCorrectAnswer() {
        Long chat = 43L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        logic.processCommand(user, QUESTION_1_ANS);

        logic.processCommand(user, CMD_REPEAT);

        Assertions.assertTrue(
                bot.hasMessage(chat, "Нет вопросов для повторения"),
                "После правильного ответа в /test вопрос не должен попадать в очередь повторения"
        );
    }

    /**
     * После неправильного ответа вопрос остается:
     * при повторном /repeat после второй ошибки вопрос снова появляется
     */
    @Test
    void testQuestionStaysInRepeatAfterWrongAnswer() {
        Long chat = 44L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        logic.processCommand(user, "неверно");

        logic.processCommand(user, CMD_REPEAT);

        logic.processCommand(user, "неверно");

        logic.processCommand(user, CMD_REPEAT);
        Assertions.assertTrue(
                bot.hasMessage(chat, QUESTION_1_TEXT),
                "После неправильного ответа в режиме повторения вопрос должен остаться в очереди"
        );
    }

    /**
     * После правильного ответа вопрос уходит из очереди:
     * ошиблись, попали в очередь /repeat, потом ответили верно и вопрос исчез
     */
    @Test
    void testQuestionRemovedFromRepeatAfterCorrectAnswer() {
        Long chat = 45L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        logic.processCommand(user, "неверно");

        logic.processCommand(user, CMD_REPEAT);

        logic.processCommand(user, QUESTION_1_ANS);

        logic.processCommand(user, CMD_REPEAT);
        Assertions.assertTrue(
                bot.hasMessage(chat, "Нет вопросов для повторения"),
                "После правильного ответа в режиме повторения вопрос должен уйти из очереди"
        );
    }

}
