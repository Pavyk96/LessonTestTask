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
    void no_question_to_repeat() {
        Long chat = 41L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_REPEAT);

        Assertions.assertTrue(
                bot.hasMessage(chat, "Нет вопросов для повторения")
        );
        Assertions.assertEquals(State.INIT, user.getState());
    }

    /**
     * Есть вопросы для повторения
     */
    @Test
    void exist_question_to_repeat() {
        Long chat = 42L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        logic.processCommand(user, "неверно");

        Question expectedQ1 = new Question(Q1_TEXT, Q1_ANS);
        Assertions.assertTrue(
                user.getWrongAnswerQuestions().contains(expectedQ1)
        );

        logic.processCommand(user, CMD_REPEAT);

        Assertions.assertEquals(State.REPEAT, user.getState());
        Assertions.assertTrue(
                bot.hasMessage(chat, Q1_TEXT));
    }
}
