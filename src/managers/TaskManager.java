package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<Subtask> getEpicSubtask(Integer epicId);

    ArrayList<Subtask> getAllSubtask();

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

    void checkStatus(Epic epic);
}
