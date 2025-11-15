package example.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Минимальные тесты add/remove
 *
 * @author  Daniil Mezev
 */
class ContainerTest {

    /**
     * Проверяем работу добавления обьекта в контейнер
     * и работу метода получения размера контейнера
     */
    @Test
    void add_and_size() {
        Container container = new Container();
        Item item1 = new Item(1);
        Item item2 = new Item(2);

        container.add(item1);
        container.add(item2);

        Assertions.assertEquals(2, container.size());
        Assertions.assertTrue(container.contains(item1));
        Assertions.assertTrue(container.contains(item2));
    }

    /**
     * Удаление обьекта
     */
    @Test
    void remove() {
        Container container = new Container();
        Item item1 = new Item(1);
        Item item2 = new Item(2);

        container.add(item1);
        container.add(item2);

        container.remove(item1);

        Assertions.assertEquals(1, container.size());
        Assertions.assertFalse(container.contains(item1));
        Assertions.assertTrue(container.contains(item2));
    }

}
