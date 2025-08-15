package tasks;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    TaskManager taskManager;

    @BeforeEach
    public void createTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    @DisplayName("Добавление подзадачи")
    void addNewSubtask() {
        Epic epic = new Epic("Эпик", "Тело эпика",TaskStatus.NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("подзадача", "тело", epic.getId(), TaskStatus.NEW);
        taskManager.addSubtask(subtask);
        final int taskId = subtask.getId();

        final Subtask savedSubtask = taskManager.getSubtaskForId(taskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        //проверка что сабтаски с одинаковым айди совпадают
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getAllSubtask();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask, subtasks.get(0), "Задачи не совпадают.");
    }
}