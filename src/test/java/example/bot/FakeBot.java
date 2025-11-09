package example.bot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/** Тестовая реализация Bot: накапливает отправленные сообщения по chatId. */
public final class FakeBot implements Bot {
    private final Map<Long, List<String>> messagesByChat = new ConcurrentHashMap<>();

    @Override
    public void sendMessage(Long chatId, String text) {
        messagesByChat.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>()).add(text);
    }

    /** Доступ к списку сообщений данного чата */
    public List<String> messagesOf(Long chatId) {
        List<String> list = messagesByChat.get(chatId);
        return list == null ? List.of() : List.copyOf(list);
    }

    /** Есть ли у чата сообщение, равное text */
    public boolean hasMessage(Long chatId, String text) {
        List<String> list = messagesByChat.get(chatId);
        if (list == null) return false;
        for (String s : list) {
            if (text.equals(s)) return true;
        }
        return false;
    }

}
