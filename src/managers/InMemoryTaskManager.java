package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;
    private final Map<Integer, Task> taskList = new HashMap<>();
    private final Map<Integer, Epic> epicList = new HashMap<>();
    private final Map<Integer, Subtask> subtaskList = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public ArrayList<Subtask> getEpicSubtask(Integer epicId) { //Выводит список подзадач эпика
        ArrayList<Subtask> currentEpic = new ArrayList<>();
        for (Subtask subtask : subtaskList.values()) {
            if (subtask.getEpicId() == epicId) {
                currentEpic.add(subtask);
            }
        }
        return currentEpic;
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtaskList.values());
    }

    @Override
    public void deleteTaskList() {
        taskList.clear();
    }

    @Override
    public void deleteAllSubtask() {
        subtaskList.clear();
        for (Epic epic : epicList.values()) { //идем по списку эпиков
            checkStatus(epic);
        }
    }

    @Override
    public void deleteAllEpic() {
        epicList.clear();
        subtaskList.clear();
    }

    @Override
    public void deleteEpicSubtask(Epic epic) {
        for (Integer id : subtaskList.keySet()) {
            if (id.equals(epic.getId())) {
                subtaskList.remove(id);
            }
        }
        checkStatus(epic);
    }

    @Override
    public void removeTaskForId(Integer id) {
        taskList.remove(id);
    }

    @Override
    public void removeEpicForId(Integer id) {
        epicList.remove(id);
        for (Subtask subtask : subtaskList.values()) { //удаляем подзадачи которые не существуют без эпика
            if (subtask.getEpicId() == id) {
                subtaskList.remove(subtask.getId());
            }
        }
    }

    @Override
    public void removeSubtaskForId(Integer id) {
        int epicId = subtaskList.get(id).getEpicId();
        subtaskList.remove(id);
        checkStatus(epicList.get(epicId)); //проверяем статус эпика из которого удалили подзадачу
    }

    @Override
    public Task getTaskForId(Integer id) {
        historyManager.add(taskList.get(id));
        return taskList.get(id);
    }

    @Override
    public Epic getEpicForId(Integer id) {
        historyManager.add(epicList.get(id));
        return epicList.get(id);
    }

    @Override
    public Subtask getSubtaskForId(Integer id) {
        historyManager.add(subtaskList.get(id));
        return subtaskList.get(id);
    }

    @Override
    public void addTask(Task task) {
        idCounter++;
        task.setId(idCounter);
        taskList.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        idCounter++;
        epic.setId(idCounter);
        epicList.put(epic.getId(), epic);
        epic.setStatus(TaskStatus.NEW);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        idCounter++;
        subtask.setId(idCounter);
        int currentEpicId = subtask.getEpicId();
        subtaskList.put(subtask.getId(), subtask);
        checkStatus(epicList.get(currentEpicId)); //проверка статуса эпика после добавления подзадачи
    }

    @Override
    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask oldSubtask = subtaskList.get(id);

        oldSubtask.setTitle(subtask.getTitle());
        oldSubtask.setDescription(subtask.getDescription());
        subtaskList.put(id, oldSubtask);
        checkStatus(epicList.get(epicId)); //проверка статуса эпика
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic oldEpic = epicList.get(id);

        oldEpic.setTitle(epic.getTitle());
        oldEpic.setDescription(epic.getDescription());

        epicList.put(id, oldEpic);
    }

    public void checkStatus(Epic epic) {
        int alldoneCounter = 0;
        int allNewCounter = 0;
        int epicId = epic.getId();
        HashMap<Integer, Subtask> tasks = new HashMap<>();
        for (Subtask subtask : subtaskList.values()) { //создаем локальную мапу в которой все подзадачи эпика
            if (subtask.getEpicId() == epicId) {
                tasks.put(subtask.getId(), subtask);
            }
        }
        if (tasks.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }
        for (Subtask task : tasks.values()) { //идем по списку подзадач
            if (task.getStatus().equals(TaskStatus.DONE)) {
                alldoneCounter++; //повышаем если статус подзадачи done
            } else if (task.getStatus().equals(TaskStatus.NEW)) {
                allNewCounter++; //повышаем если статус подзадачи new
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }
        if (alldoneCounter == tasks.size()) { //если все задачи в списке done
            epic.setStatus(TaskStatus.DONE);
        } else if (allNewCounter == tasks.size()) { //если все задачи в списке new
            epic.setStatus(TaskStatus.NEW);
        }
    }
}