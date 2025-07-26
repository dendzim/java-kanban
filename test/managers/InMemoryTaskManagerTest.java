package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void createTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getDifferentTypes() {
        Task task = new Task("Таск", "Тело таска", TaskStatus.NEW);
        taskManager.addTask(task);
        Epic epic = new Epic("Эпик", "Тело эпика",TaskStatus.NEW);
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.getTaskForId(1).getId(), "Задача с этим id не найдена");
        assertEquals(2, taskManager.getEpicForId(2).getId(), "Задача с этим id не найдена");
    }
}