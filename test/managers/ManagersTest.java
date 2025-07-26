package managers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void createManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();;
        assertNotNull(historyManager, "Менеджер не найден.");
        assertNotNull(taskManager, "Менеджер не найден.");
    }
}