package example.note;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Тестирование функционала NoteLogic
 *
 * @author Daniil Mezev
 */
public class NoteLogicTest {

     /**
     * Добавление (/add) и возврат (/notes) заметок
     */
    @Test
    void add_and_get_notes() {
        NoteLogic logic = new NoteLogic();
        String note1 = "Заметка 1";
        String note2 = "Заметка 2";

        logic.handleMessage("/add " + note1);
        logic.handleMessage("/add " + note2);

        String notes = logic.handleMessage("/notes");

        String expected =
                "Your notes:" + System.lineSeparator() +
                        note1 + System.lineSeparator() +
                        note2;

        Assertions.assertEquals(expected, notes);
    }

    /**
     * Редактирование заметки (/edit)
     */
    @Test
    void edit_note() {
        NoteLogic logic = new NoteLogic();
        String note1 = "Заметка 1";
        String note2 = "Заметка 2";
        String updated = "Новая версия 1";

        logic.handleMessage("/add " + note1);
        logic.handleMessage("/add " + note2);

        logic.handleMessage("/edit 1 " + updated);

        String notes = logic.handleMessage("/notes");

        String expected =
                "Your notes:" + System.lineSeparator() +
                        updated + System.lineSeparator() +
                        note2;

        Assertions.assertEquals(expected, notes);
    }

    /**
     * Удаление заметки (/del)
     */
    @Test
    void delete_note() {
        NoteLogic logic = new NoteLogic();
        String note1 = "Заметка 1";
        String note2 = "Заметка 2";

        logic.handleMessage("/add " + note1);
        logic.handleMessage("/add " + note2);

        logic.handleMessage("/del 1");

        String notes = logic.handleMessage("/notes");

        String expected =
                "Your notes:" + System.lineSeparator() +
                        note2;

        Assertions.assertEquals(expected, notes);
    }

}
