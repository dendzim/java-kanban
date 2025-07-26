package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    public void testHistoryAddWithChangeTaskStatus() {
        Task task = new Task("переезд", "переезд в другой город", TaskStatus.NEW);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Задача не добавлена");
        task.setStatus(TaskStatus.IN_PROGRESS);
        historyManager.add(task);
        assertEquals(TaskStatus.NEW, historyManager.getHistory().get(0).getStatus());
    }

    @Test
    void add() {
        Task task = new Task("переезд", "переезд в другой город", TaskStatus.NEW);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи, история не должна быть пустой.");
    }
}