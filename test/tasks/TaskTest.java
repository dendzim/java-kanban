package tasks;

import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager;

    @BeforeEach
    public void createTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTask() {
        Task task = new Task("Таск", "Тело таска",TaskStatus.NEW);
        taskManager.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskForId(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        //проверка что таски с одинаковым айди совпадают
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
}