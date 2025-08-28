package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    File temp;
    FileBackedTaskManager taskManager;

    @BeforeEach
    public void createHistoryManager() {
        {
            try {
                temp = File.createTempFile("Test", ".txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        taskManager = new FileBackedTaskManager(temp);
    }

    @Test
    @DisplayName("Сохранение пустого файла")
    public void saveFile() {
        taskManager.save();
        assertTrue(temp.exists());
    }

    @Test
    @DisplayName("Сохранение файлов")
    public void saveTasksToFile() {
        Task task001 = new Task ("Task001", "Description", TaskStatus.NEW);
        taskManager.addTask(task001);
        Task task002 = new Task ("Task002", "Description", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task002);
        assertTrue(temp.exists());
        assertTrue(temp.length() > 0);
    }

    @Test
    @DisplayName("Загрузка файлов")
    public void loadFile() {
        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(temp);
        assertEquals(taskManager1.getTaskList().size(), taskManager.getTaskList().size());
    }
}
