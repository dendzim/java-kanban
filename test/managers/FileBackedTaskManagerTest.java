package managers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File temp;
    FileBackedTaskManager taskManager;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        {
            try {
                temp = File.createTempFile("Test", ".txt");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        taskManager = new FileBackedTaskManager(temp);
        return taskManager;
    }

    @Test
    @DisplayName("Сохранение пустого файла")
    public void testSaveFile() {
        taskManager.save();
        assertTrue(temp.exists());
    }

    @Test
    @DisplayName("Сохранение файлов")
    public void testSaveTasksToFile() {
        task1 = new Task ("Task001", "Description", TaskStatus.NEW);
        taskManager.addTask(task1);
        task2 = new Task ("Task002", "Description", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task2);
        assertTrue(temp.exists());
        assertTrue(temp.length() > 0);
    }

    @Test
    @DisplayName("Загрузка файлов")
    public void testLoadFile() {
        FileBackedTaskManager taskManager1 = FileBackedTaskManager.loadFromFile(temp);
        assertEquals(taskManager1.getTaskList().size(), taskManager.getTaskList().size());
    }
}
