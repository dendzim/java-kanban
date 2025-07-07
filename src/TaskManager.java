import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int idCounter = 0;
    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, Epic> epicList = new HashMap<>();

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    public ArrayList<Subtask> getEpicSubtask(Integer epicId){
        return  new ArrayList<>(epicList.get(epicId).subtaskList.values());
    }

    public ArrayList<Subtask> getAllSubtask() {
        ArrayList<Subtask> allSubtask = new ArrayList<>();
        for (Epic epic : epicList.values()) { //идем по списку эпиков
            allSubtask.addAll(epic.subtaskList.values()); //закидываем в список всю коллекцию эпика
        }
        return allSubtask;
    }

    public void deleteTaskList() {
        taskList.clear();
    }

    public void deleteAllSubtask() {
        for (Epic epic : epicList.values()) { //идем по списку эпиков
            epic.subtaskList.clear();
        }
    }

    public void deleteAllEpic() {
        epicList.clear();
    }

    public void deleteEpicSubtask(Epic epic) {
        epic.subtaskList.clear();
    }

    public void removeTaskForId(Integer id) {
        taskList.remove(id);
    }

    public void removeEpicForId(Integer id) {
        epicList.remove(id);
    }

    public void removeSubtaskForId(Integer id) {
        for (Epic epic : epicList.values()) {
            epic.subtaskList.remove(id);
            checkStatus(epic); //отдельный метод на проверку статуса
        }
    }

    public Task getTaskForId(Integer id) {
        return taskList.get(id);
    }

    public Epic getEpicForId(Integer id) {
        checkStatus(epicList.get(id));
        return epicList.get(id);
    }

    public Subtask getSubtaskForId(Integer id) {
        for (Epic epic : epicList.values()) {
            if (epic.subtaskList.containsKey(id)) {
                return epic.subtaskList.get(id);
            }
        }
        return null;
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
        if (epic.subtaskList.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public void addSubtask(Subtask subtask) {
        idCounter++;
        subtask.setId(idCounter);
        int currentEpicId = subtask.getEpicId();
        epicList.get(currentEpicId).subtaskList.put(subtask.getId(), subtask);
        checkStatus(epicList.get(currentEpicId)); //проверка статуса эпика после добавления подзадачи
    }

    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    public  void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask oldSubtask = epicList.get(epicId).subtaskList.get(id);

        oldSubtask.setTitle(subtask.getTitle());
        oldSubtask.setDescription(subtask.getDescription());
        epicList.get(epicId).subtaskList.put(id, oldSubtask);
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
        HashMap<Integer, Subtask> tasks = epic.subtaskList;
        for (Subtask task : tasks.values()) { //идем по списку подзадач
            if (task.getStatus().equals(TaskStatus.DONE)) {
                alldoneCounter++; //повышаем если статус подзадачи done
            }
            if (task.getStatus().equals(TaskStatus.NEW)) {
                allNewCounter++; //повышаем если статус подзадачи new
            }
        }
        if (alldoneCounter == tasks.size()) { //если все задачи в списке done
            epic.setStatus(TaskStatus.DONE);
        } else if (allNewCounter == tasks.size()) { //если все задачи в списке new
            epic.setStatus(TaskStatus.NEW);
        } else { //если пересортится
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}