package tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager taskManager;

    @BeforeEach
    public void createTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    @DisplayName("Добавление эпика")
    void addNewEpic() {
        Epic epic = new Epic("Эпик", "Тело эпика",TaskStatus.NEW);
        taskManager.addEpic(epic);
        final int epicId = epic.getId();

        final Task savedEpic = taskManager.getEpicForId(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        //проверка что эпики с одинаковым айди совпадают
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }
}