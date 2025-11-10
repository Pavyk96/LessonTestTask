package example.bot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


/**
 * Тесты команды /notify
 *
 * @author Daniil Mezev
 */
class NotifyCommandTest {

    private final String CMD_NOTIFY = "/notify";
    private final String MSG_ENTER_TEXT = "Введите текст напоминания";
    private final String MSG_ENTER_DELAY = "Через сколько секунд напомнить?";
    private final String MSG_SET = "Напоминание установлено";

    /**
     * Уведомление приходит в корректный тайминг
     */
    @Test
    void singleNotification_arrivesAroundRequestedTime() {
        Long chat = 501L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        String noteText = "разминка";
        int seconds = 1;

        long t0 = System.currentTimeMillis();
        startNotifyDialog(logic, user, bot, chat, noteText, String.valueOf(seconds));

        String fired = "Сработало напоминание: '" + noteText + "'";
        long tFire = waitForMessageTimestamp(bot, chat, fired, 1500);

        Assertions.assertTrue(tFire > 0);
        long elapsedMs = tFire - t0;

        long expectedMs = seconds * 1000L;
        long lower = expectedMs - 350;
        long upper = expectedMs + 350;

        Assertions.assertTrue(elapsedMs >= lower && elapsedMs <= upper);
    }

    /**
     * Разные тайминги уведомлений приходят в корректное время
     * P.s первое уведомление через 5 сек, второе через 3
     * первым должно придти 2 уведомление, затем 1
     */
    @Test
    void twoNotifications_orderRespectsDelays() {
        Long chat = 502L;
        FakeBot bot = new FakeBot();
        BotLogic logic = new BotLogic(bot);
        User user = new User(chat);

        String text1 = "долгосрочное";
        int sec1 = 5;
        long t0 = System.currentTimeMillis();
        startNotifyDialog(logic, user, bot, chat, text1, String.valueOf(sec1));

        String text2 = "более раннее";
        int sec2 = 3;
        startNotifyDialog(logic, user, bot, chat, text2, String.valueOf(sec2));

        String fired1 = "Сработало напоминание: '" + text1 + "'";
        String fired2 = "Сработало напоминание: '" + text2 + "'";

        long tFire2 = waitForMessageTimestamp(bot, chat, fired2, 4000);
        long tFire1 = waitForMessageTimestamp(bot, chat, fired1, 6000);

        Assertions.assertTrue(tFire2 > 0);
        Assertions.assertTrue(tFire1 > 0);

        Assertions.assertTrue(tFire2 < tFire1);

        long elapsed2 = tFire2 - t0;
        long elapsed1 = tFire1 - t0;

        Assertions.assertTrue(elapsed2 >= 2600 && elapsed2 <= 3600);
        Assertions.assertTrue(elapsed1 >= 4600 && elapsed1 <= 5600);
    }

    /**
     * Прогоняет диалог /notify -> <текст> -> <секунды>.
     * Проверяет промежуточные сервисные сообщения и возврат состояния в INIT.
     */
    private void startNotifyDialog(BotLogic logic, User user, FakeBot bot, Long chat, String text, String delaySec) {
        logic.processCommand(user, CMD_NOTIFY);
        Assertions.assertEquals(State.SET_NOTIFY_TEXT, user.getState());
        Assertions.assertTrue(bot.hasMessage(chat, MSG_ENTER_TEXT));

        logic.processCommand(user, text);
        Assertions.assertEquals(State.SET_NOTIFY_DELAY, user.getState());
        Assertions.assertTrue(bot.hasMessage(chat, MSG_ENTER_DELAY));

        logic.processCommand(user, delaySec);
        Assertions.assertEquals(State.INIT, user.getState());
        Assertions.assertTrue(bot.hasMessage(chat, MSG_SET));
    }

    /**
     * Ожидает пока в FakeBot появится точное сообщение; возвращает timestamp его первого появления.
     * Если не дождались — возвращает -1.
     */
    private long waitForMessageTimestamp(FakeBot bot, Long chat, String text, long timeoutMs) {
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            if (bot.hasMessage(chat, text)) {
                return System.currentTimeMillis();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
        return -1;
    }

}
