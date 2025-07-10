import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCounter = 0;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    public ArrayList<Subtask> getEpicSubtask(Integer epicId) { //Выводит список подзадач эпика
        ArrayList<Subtask> currentEpic = new ArrayList<>();
        for (Subtask subtask : subtaskList.values()) {
            if (subtask.getEpicId() == epicId) {
                currentEpic.add(subtask);
            }
        }
        return currentEpic;
    }

    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtaskList.values());
    }

    public void deleteTaskList() {
        taskList.clear();
    }

    public void deleteAllSubtask() {
        subtaskList.clear();
        for (Epic epic : epicList.values()) { //идем по списку эпиков
            checkStatus(epic);
        }
    }

    public void deleteAllEpic() {
        epicList.clear();
        subtaskList.clear();
    }

    public void deleteEpicSubtask(Epic epic) {
        for (Integer id : subtaskList.keySet()) {
            if (id.equals(epic.getId())) {
                subtaskList.remove(id);
            }
        }
        checkStatus(epic);
    }

    public void removeTaskForId(Integer id) {
        taskList.remove(id);
    }

    public void removeEpicForId(Integer id) {
        epicList.remove(id);
        for (Subtask subtask : subtaskList.values()) { //удаляем подзадачи которые не существуют без эпика
            if (subtask.getEpicId() == id) {
                subtaskList.remove(subtask.getId());
            }
        }
    }

    public void removeSubtaskForId(Integer id) {
        int epicId = subtaskList.get(id).getEpicId();
        subtaskList.remove(id);
        checkStatus(epicList.get(epicId)); //проверяем статус эпика из которого удалили подзадачу
    }

    public Task getTaskForId(Integer id) {
        return taskList.get(id);
    }

    public Epic getEpicForId(Integer id) {
        return epicList.get(id);
    }

    public Subtask getSubtaskForId(Integer id) {
        return subtaskList.get(id);
    }

    public void addTask(Task task) {
        idCounter++;
        task.setId(idCounter);
        taskList.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        idCounter++;
        epic.setId(idCounter);
        epicList.put(epic.getId(), epic);
        epic.setStatus(TaskStatus.NEW);
    }

    public void addSubtask(Subtask subtask) {
        idCounter++;
        subtask.setId(idCounter);
        int currentEpicId = subtask.getEpicId();
        subtaskList.put(subtask.getId(), subtask);
        checkStatus(epicList.get(currentEpicId)); //проверка статуса эпика после добавления подзадачи
    }

    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    public  void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask oldSubtask = subtaskList.get(id);

        oldSubtask.setTitle(subtask.getTitle());
        oldSubtask.setDescription(subtask.getDescription());
        subtaskList.put(id, oldSubtask);
        checkStatus(epicList.get(epicId)); //проверка статуса эпика
    }

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