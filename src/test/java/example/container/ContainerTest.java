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
        Container c = new Container();
        Item i1 = new Item(1);
        Item i2 = new Item(2);
        Item i3 = new Item(3);

        c.add(i1);
        c.add(i2);
        c.add(i3);

        Assertions.assertEquals(3, c.size());
        Assertions.assertTrue(c.contains(i1));
        Assertions.assertTrue(c.contains(i2));
    }

    /**
     * Удаление обьекта
     */
    @Test
    void remove() {
        Container c = new Container();
        Item i1 = new Item(1);
        Item i2 = new Item(2);

        c.add(i1);
        c.add(i2);

        c.remove(i1);

        Assertions.assertEquals(1, c.size());
        Assertions.assertFalse(c.contains(i1));
        Assertions.assertTrue(c.contains(i2));
    }

}
