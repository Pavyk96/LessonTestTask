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

    private final String cmdNotify = "/notify";

    private final String notifyPrefix = "Сработало напоминание: '";
    private final String notifySuffix = "'";

    private FakeBot bot;
    private BotLogic logic;

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
        User user = createUser(chatId);

        String noteText = "разминка";
        int delaySec = 1;
        String notify = notifyPrefix + noteText + notifySuffix;

        logic.processCommand(user, cmdNotify);
        logic.processCommand(user, noteText);
        logic.processCommand(user, String.valueOf(delaySec));

        Thread.sleep(1100L);

        Assertions.assertTrue(
                bot.hasMessage(chatId, notify),
                "Не появилось уведомление: '" +
                        notify + "'. История: " + bot.messagesOf(chatId)
        );
    }

    /**
     * Разные тайминги уведомлений приходят в корректном порядке
     */
    @Test
    void testTwoNotifiesComeInOrder() throws InterruptedException {
        long chatId = 502L;
        User user = createUser(chatId);

        logic.processCommand(user, cmdNotify);
        String lateText = "позже";
        logic.processCommand(user, lateText);
        int lateSec = 2;
        logic.processCommand(user, String.valueOf(lateSec));

        logic.processCommand(user, cmdNotify);
        String earlyText = "раньше";
        logic.processCommand(user, earlyText);
        int earlySec = 1;
        logic.processCommand(user, String.valueOf(earlySec));

        Thread.sleep(1100L);

        String earlyMsg = notifyPrefix + earlyText + notifySuffix;
        Assertions.assertTrue(
                bot.hasMessage(chatId, earlyMsg),
                "Не появилось раннее уведомление: '" +
                        earlyMsg + "'. История: " + bot.messagesOf(chatId)
        );

        String lateMsg = notifyPrefix + lateText + notifySuffix;
        Assertions.assertFalse(
                bot.hasMessage(chatId, lateMsg),
                "Позднее уведомление пришло слишком рано. История: "
                        + bot.messagesOf(chatId)
        );

        Thread.sleep(1100L);

        Assertions.assertTrue(
                bot.hasMessage(chatId, lateMsg),
                "Не появилось позднее уведомление: '" +
                        lateMsg + "'. История: " + bot.messagesOf(chatId)
        );
    }

    /**
     * Создать пользователя
     */
    private User createUser(long chatId) {
        return new User(chatId);
    }

}
