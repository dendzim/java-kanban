package managers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    @DisplayName("Проверка создания менеджеров")
    public void createManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        TaskManager taskManager = Managers.getDefault();;
        assertNotNull(historyManager, "Менеджер не найден.");
        assertNotNull(taskManager, "Менеджер не найден.");
    }
}