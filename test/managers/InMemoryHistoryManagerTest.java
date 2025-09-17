package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;

    @BeforeEach
    public void createHistoryManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    @DisplayName("Проверка что Таск в истории меняет статус если мы его поменяли и добавили в список")
    public void testHistoryAddWithChangeTaskStatus() {
        Task task = new Task("переезд", "переезд в другой город", TaskStatus.NEW);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Задача не добавлена");
        task.setStatus(TaskStatus.IN_PROGRESS);
        historyManager.add(task);
        assertEquals(TaskStatus.IN_PROGRESS, historyManager.getHistory().get(0).getStatus());
    }

    @Test
    @DisplayName("Проверка добавления тасков в историю их количества")
    void add() {
        Task task1 = new Task("1", "тело1", TaskStatus.NEW);
        task1.setId(1);
        historyManager.add(task1);
        Task task2 = new Task("2", "тело2", TaskStatus.IN_PROGRESS);
        task2.setId(2);
        historyManager.add(task2);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(2, history.size(), "После добавления задачи, история не должна быть пустой.");

    }

    @Test
    @DisplayName("Проверка удаления Таска из истории")
    public void remove() {
        Task task = new Task("переезд", "переезд в другой город", TaskStatus.NEW);
        final List<Task> history = historyManager.getHistory();
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertNotNull(history, "История должна быть пустой после удаления");
    }

    @Test
    @DisplayName("Проверка корректного расположения таска после повторного получения его")
    public void testHistoryChangePositions() {
        Task task1 = new Task("1", "тело1", TaskStatus.NEW);
        task1.setId(1);
        historyManager.add(task1);
        Task task2 = new Task("2", "тело2", TaskStatus.IN_PROGRESS);
        task2.setId(2);
        historyManager.add(task2);
        final List<Task> history1 = historyManager.getHistory();
        historyManager.add(task1);
        final List<Task> history2 = historyManager.getHistory();
        assertNotEquals(history1, history2, "Списки должны различаться расположением элементов");
    }
}