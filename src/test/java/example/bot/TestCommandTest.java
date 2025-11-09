package example.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

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

    /**
     * Правильный ответ на вопрос
     */
    @Test
    void answerTrue() {
        Long chat = 11L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        Assertions.assertEquals(State.TEST, user.getState());
        Assertions.assertTrue(bot.hasMessage(chat, Q1_TEXT));

        logic.processCommand(user, Q1_ANS);

        Assertions.assertTrue(bot.hasMessage(chat, "Правильный ответ!"));
        Assertions.assertTrue(bot.hasMessage(chat, Q2_TEXT));

        Assertions.assertTrue(user.getWrongAnswerQuestions().isEmpty());
    }

    /**
     * Не правильный ответ на вопрос, вопрос улетает в очередь неправильных ответов
     * для repeat
     */
    @Test
    void answerFalse_and_answer_add_queue() {
        Long chat = 12L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        logic.processCommand(user, CMD_TEST);
        Assertions.assertEquals(State.TEST, user.getState());
        Assertions.assertTrue(bot.hasMessage(chat, Q1_TEXT));

        logic.processCommand(user, "неверно");

        Assertions.assertTrue(bot.hasMessage(chat, "Вы ошиблись, верный ответ: " + Q1_ANS));
        Assertions.assertTrue(bot.hasMessage(chat, Q2_TEXT));

        Question expectedQ1 = new Question(Q1_TEXT, Q1_ANS);
        Assertions.assertTrue(user.getWrongAnswerQuestions().contains(expectedQ1));
    }

}
