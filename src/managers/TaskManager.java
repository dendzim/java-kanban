package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getTaskList();

    List<Epic> getEpicList();

    List<Subtask> getEpicSubtask(Integer epicId);

    List<Subtask> getAllSubtask();

    void deleteTaskList();

    void deleteAllSubtask();

    void deleteAllEpic();

    void deleteEpicSubtask(Epic epic);

    void removeTaskForId(Integer id);

    void removeEpicForId(Integer id);

    void removeSubtaskForId(Integer id);

    Task getTaskForId(Integer id);

    Epic getEpicForId(Integer id);

    Subtask getSubtaskForId(Integer id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    List<Task> getHistory();

    Set<Task> getprioritizedTasks();
}
