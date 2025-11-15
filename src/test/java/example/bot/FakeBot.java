package example.bot;

import java.util.LinkedList;
import java.util.List;

/**
 * Тестовая реализация Bot
 */
public final class FakeBot implements Bot {

    /** Очередь всех сообщений, в порядке отправки */
    private final List<String> messages = new LinkedList<>();

    @Override
    public void sendMessage(Long chatId, String text) {
        messages.add(text);
    }

    /** Все сообщения (для любого chatId — история общая) */
    public List<String> messagesOf(Long chatId) {
        return List.copyOf(messages);
    }

    /** Есть ли среди сообщений текст, равный text */
    public boolean hasMessage(Long chatId, String text) {
        return messages.contains(text);
    }

    /** Последнее отправленное сообщение */
    public String lastMessage(Long chatId) {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }

}
