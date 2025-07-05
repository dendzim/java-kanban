import java.util.HashMap;

public class TaskManager {
    private int idCounter = 0;
    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, Epic> epicList = new HashMap<>();

    public int getId() {
        idCounter++;
        return idCounter;
    }

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    public HashMap<Integer, Subtask> getEpicSubtask(Epic epic){
        return epic.getSubtaskList();
    }

    public void deleteTaskList() {
        taskList.clear();
        System.out.println("Список задач очищен");
    }

    public void deleteEpicList() {
        epicList.clear();
        System.out.println("Список задач очищен");
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

    public void removeSubtaskForId(Integer id, Epic epic) {
        epic.subtaskList.remove(id);
        boolean isAlldone = false;
        HashMap<Integer, Subtask> tasks = epic.subtaskList;
        for (Subtask task : tasks.values()) {
            isAlldone = task.getStatus().equals(TaskStatus.DONE);
        }
        if (isAlldone) {
            epic.setStatus(TaskStatus.DONE);
        }

    }

    public Task getTaskForId(Integer id) {
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        }
        System.out.println("Задачи с таким id нет в списке.");
        return null;
    }

    public Epic getEpicForId(Integer id) {
        if (epicList.containsKey(id)) {
            return epicList.get(id);
        }
        System.out.println("Эпика с таким id нет в списке.");
        return null;
    }

    public Subtask getSubtaskForId(Integer id, Epic epic) {
        if (epic.subtaskList.containsKey(id)) {
            return epic.subtaskList.get(id);
        }
        System.out.println("Подзадачи с таким id нет в списке заданного эпика.");
        return null;
    }

    public void addTask(Task task) {
        taskList.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
        if (epic.subtaskList.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        }
    }

    public void addSubtask(Epic epic, Subtask subtask) {
        subtask.setEpicId(epic.getId());
        epic.subtaskList.put(subtask.getId(), subtask);
        if (subtask.getStatus().equals(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        boolean isAlldone = false;
        HashMap<Integer, Subtask> tasks = epic.subtaskList;
        for (Subtask task : tasks.values()) {
            isAlldone = task.getStatus().equals(TaskStatus.DONE);
        }
        if (isAlldone) {
            epic.setStatus(TaskStatus.DONE);
        }
    }

    public void updateTask(Task task) {
        taskList.put(task.getId(), task);
    }

    public  void updateSubtask(Subtask subtask, Epic epic) {
        int id = subtask.getId();
        Subtask oldSubtask = epic.subtaskList.get(id);

        oldSubtask.setTitle(subtask.getTitle());
        oldSubtask.setDescription(epic.getDescription());
        epic.subtaskList.put(id, oldSubtask);
        boolean isAlldone = false;
        HashMap<Integer, Subtask> tasks = epic.subtaskList;
        for (Subtask task : tasks.values()) {
            isAlldone = task.getStatus().equals(TaskStatus.DONE);
        }
        if (isAlldone) {
            epic.setStatus(TaskStatus.DONE);
        }
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic oldEpic = epicList.get(id);

        oldEpic.setTitle(epic.getTitle());
        oldEpic.setDescription(epic.getDescription());

        epicList.put(id, oldEpic);
    }
}