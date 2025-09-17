package managers;

import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    public void testEpicStatus() {
        epic = new Epic("Эпик", "Тело эпика",TaskStatus.NEW);
        taskManager.addEpic(epic);
        subtask1 = new Subtask("Sub1", "Description", epic.getId(), TaskStatus.NEW);
        subtask2 = new Subtask("Sub2", "Description", epic.getId(), TaskStatus.NEW);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        assertEquals(TaskStatus.NEW, epic.getStatus());

        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);

        assertEquals(TaskStatus.DONE, epic.getStatus());

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        subtask1.setStatus(TaskStatus.NEW);
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}