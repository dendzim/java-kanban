package managers;

import exceptions.TaskValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Task task1;
    protected Task task2;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void createTask() {
        task1 = new Task("Переезд", "переезд в другой город", TaskStatus.NEW);
        taskManager.addTask(task1);
        assertNotNull(task1.getId());
        assertEquals(task1, taskManager.getTaskForId(task1.getId()));
        assertTrue(taskManager.getTaskList().contains(task1));
    }

    @Test
    @DisplayName("Создание эпика и подзадачи")
    void createEpicAndSub() {
        epic = new Epic("Epic001", "Description",TaskStatus.NEW);
        taskManager.addEpic(epic);
        subtask1 = new Subtask("Subtask001", "Description", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 10, 0));
        taskManager.addSubtask(subtask1);
        assertNotNull(epic.getId());
        assertNotNull(subtask1.getId());
        assertTrue(taskManager.getEpicList().contains(epic));
        assertTrue(taskManager.getAllSubtask().contains(subtask1));
    }

    @Test
    void deleteTask() {
        task1 = new Task("Переезд", "переезд в другой город", TaskStatus.NEW);
        taskManager.addTask(task1);
        taskManager.removeTaskForId(task1.getId());
        assertFalse(taskManager.getTaskList().contains(task1));
    }

    @Test
    void getEpicSubtasks() {
        epic = new Epic("Epic001", "Description",TaskStatus.NEW);
        taskManager.addEpic(epic);
        subtask1 = new Subtask("Subtask001", "Description", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 10, 0));
        subtask2 = new Subtask("Subtask002", "Description", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 11, 20));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        List<Subtask> epicSubtasks = taskManager.getEpicSubtask(epic.getId());
        assertEquals(2, epicSubtasks.size());
        assertTrue(epicSubtasks.contains(subtask1));
        assertTrue(epicSubtasks.contains(subtask2));
    }

    @Test
    @DisplayName("Проверка присвоения разных id")
    void getDifferentTypesId() {
        task1 = new Task("Переезд", "переезд в другой город", TaskStatus.NEW);
        taskManager.addTask(task1);
        task2 = new Task("Переезд", "переезд в другой город", TaskStatus.NEW);
        taskManager.addTask(task2);
        assertEquals(1, taskManager.getTaskForId(1).getId(), "Задача с этим id не найдена");
        assertEquals(2, taskManager.getTaskForId(2).getId(), "Задача с этим id не найдена");
    }

    @Test
    void tasksWithoutTimeDoNotConflict() {
        task1 = new Task("Task 1", "Description", TaskStatus.NEW, null, null);
        task2 = new Task("Task 2", "Description", TaskStatus.NEW, null, null);

        assertDoesNotThrow(() -> {
            taskManager.addTask(task1);
            taskManager.addTask(task2);
        });
    }

    @Test
    void notCrossedDates() {
        epic = new Epic("Epic001", "Description",TaskStatus.NEW);
        taskManager.addEpic(epic);
        subtask1 = new Subtask("Subtask001", "Description", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 10, 0));
        subtask2 = new Subtask("Subtask002", "Description", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 11, 20));

        assertDoesNotThrow(() -> {
            taskManager.addSubtask(subtask1);
            taskManager.addSubtask(subtask2);
        });
    }

    @Test
    void isCrossedDates() {
        epic = new Epic("Epic001", "Description",TaskStatus.NEW);
        taskManager.addEpic(epic);
        subtask1 = new Subtask("Subtask001", "Description", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 11, 0));
        subtask2 = new Subtask("Subtask002", "Description", epic.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 11, 20));

        taskManager.addSubtask(subtask1);

        assertThrows(TaskValidationException.class, () -> {
            taskManager.addSubtask(subtask2);
        });
    }
}