package example.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

/**
 * Тесты команды /notify
 *
 * @author Daniil Mezev
 */
class NotifyCommandTest {

    private static final String CMD_NOTIFY = "/notify";
    private static final String NOTIFY_PREFIX = "Сработало напоминание: '";
    private static final String NOTIFY_SUFFIX = "'";

    private FakeBot bot;
    private BotLogic logic;

    /**
     * Пересоздание бота перед тестом
     */
    @BeforeEach
    void setUp() {
        bot = new FakeBot();
        logic = new BotLogic(bot);
    }

    /**
     * Уведомление приходит в корректный тайминг
     */
    @Test
    void testSingleNotifyArrivesOnTime() throws InterruptedException {
        long chatId = 501L;
        User user = new User(chatId);

        String noteText = "разминка";
        int delaySec = 1;
        String notify = NOTIFY_PREFIX + noteText + NOTIFY_SUFFIX;

        logic.processCommand(user, CMD_NOTIFY);
        logic.processCommand(user, noteText);
        logic.processCommand(user, String.valueOf(delaySec));

        Thread.sleep(1010L);

        Assertions.assertEquals(
                notify,
                bot.lastMessage(),
                "Последнее сообщение не совпало: ожидали '" + notify +
                        "'. История: " + bot.messagesOf()
        );

    }

    /**
     * Разные тайминги уведомлений приходят в корректном порядке
     */
    @Test
    void testTwoNotifiesComeInOrder() throws InterruptedException {
        long chatId = 502L;
        User user = new User(chatId);

        logic.processCommand(user, CMD_NOTIFY);
        String lateText = "позже";
        logic.processCommand(user, lateText);
        int lateSec = 2;
        logic.processCommand(user, String.valueOf(lateSec));

        logic.processCommand(user, CMD_NOTIFY);
        String earlyText = "раньше";
        logic.processCommand(user, earlyText);
        int earlySec = 1;
        logic.processCommand(user, String.valueOf(earlySec));

        String earlyMsg = NOTIFY_PREFIX + earlyText + NOTIFY_SUFFIX;
        String lateMsg  = NOTIFY_PREFIX + lateText  + NOTIFY_SUFFIX;

        Thread.sleep(1010L);

        Assertions.assertEquals(
                earlyMsg,
                bot.lastMessage(),
                "Сообщение пришло не в том порядке"
        );

        Thread.sleep(1100L);

        Assertions.assertEquals(
                lateMsg,
                bot.lastMessage(),
                "Сообщение пришло не в том порядке"
        );
    }

}
