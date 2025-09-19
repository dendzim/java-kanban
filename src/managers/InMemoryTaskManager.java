package managers;

import exceptions.TaskValidationException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;
    protected final Map<Integer, Task> taskList = new HashMap<>();
    protected final Map<Integer, Epic> epicList = new HashMap<>();
    protected final Map<Integer, Subtask> subtaskList = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
    public List<Subtask> getEpicSubtask(Integer epicId) { //Выводит список подзадач эпика
        Epic epic = epicList.get(epicId);
        if (epic == null) {
            return null;
        }
        List<Subtask> epicSub = getAllSubtask();
        return epicSub.stream()
                .filter(subtask -> subtask.getEpicId() == epicId)
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<>(subtaskList.values());
    }

    @Override
    public void deleteTaskList() {
        for (Integer id : taskList.keySet()) {
            historyManager.remove(id);
            delete(taskList.get(id));
        }
        taskList.clear();
    }

    @Override
    public void deleteAllSubtask() {
        for (Integer id : subtaskList.keySet()) {
            historyManager.remove(id);
            delete(subtaskList.get(id));
        }
        subtaskList.clear();
        for (Epic epic : epicList.values()) { //идем по списку эпиков
            updateEpic(epic);
        }
    }

    @Override
    public void deleteAllEpic() {
        for (Integer id : epicList.keySet()) {
            historyManager.remove(id);
            delete(epicList.get(id));
        }
        epicList.clear();
        for (Integer id : subtaskList.keySet()) {
            historyManager.remove(id);
            delete(subtaskList.get(id));
        }
        subtaskList.clear();
    }

    @Override
    public void deleteEpicSubtask(Epic epic) { //удаление подзадача по переданному эпику
        List<Integer> idToRemove = new ArrayList<>();
        for (Subtask subtask : subtaskList.values()) {
            if (subtask.getEpicId() == epic.getId()) {
                idToRemove.add(subtask.getId());
                delete(subtask);
            }
        }
        for (Integer key : idToRemove) {
            subtaskList.remove(key);
            historyManager.remove(key);
        }
        updateEpic(epic);
    }

    @Override
    public void removeTaskForId(Integer id) {
        taskList.remove(id);
        delete(taskList.get(id));
        historyManager.remove(id);

    }

    @Override
    public void removeEpicForId(Integer id) {
        epicList.remove(id);
        historyManager.remove(id); //чтобы избежать ConcurrentModificationException
        List<Integer> idToRemove = new ArrayList<>(); //формируем список на удаление
        for (Subtask subtask : subtaskList.values()) { //ищем подзадачи по id удаляемого эпика
            if (subtask.getEpicId() == id) {
                idToRemove.add(subtask.getId());
                delete(subtask);
            }
        }
        for (Integer key : idToRemove) {
            subtaskList.remove(key);
            historyManager.remove(key);
        }
    }

    @Override
    public void removeSubtaskForId(Integer id) {
        int epicId = subtaskList.get(id).getEpicId();
        subtaskList.remove(id);
        delete(subtaskList.get(id));
        checkStatus(epicList.get(epicId));//проверяем статус эпика из которого удалили подзадачу
        historyManager.remove(id);
        updateEpic(epicList.get(epicId));
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
        add(task);
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
        add(subtask);
        updateEpic(epicList.get(currentEpicId));
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

        delete(oldSubtask);
        subtaskList.put(id, oldSubtask);
        add(oldSubtask);
        updateEpic(epicList.get(epicId));
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic oldEpic = epicList.get(id);

        oldEpic.setTitle(epic.getTitle());
        oldEpic.setDescription(epic.getDescription());

        epicList.put(id, oldEpic);
        checkStatus(epic);
        updateEpicDuration(epic);
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

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void deleteHistory() {
        historyManager.deleteHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void updateEpicDuration(Epic epic) {
        List<Subtask> sub = getEpicSubtask(epic.getId());
        sub.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .forEachOrdered(subtask -> {
                    if (epic.getStartTime() == null || epic.getStartTime().isAfter(subtask.getStartTime())) {
                        epic.setStartTime(subtask.getStartTime());
                    }
                    if (epic.getEndTime() == null || epic.getEndTime().isBefore(subtask.getEndTime())) {
                        epic.setEndTime(subtask.getEndTime());
                    }
                });
        Duration totalDuration = sub.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        epic.setDuration(totalDuration);
    }

    public boolean isCrossed(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();

        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();
        boolean notCrossed = start1.isAfter(end2) || start2.isAfter(end1);
        return !notCrossed;
    }

    private void add(Task newTask) {
        if (newTask.getStartTime() == null) {
            return;
        }
        prioritizedTasks.stream()
                .filter(task -> isCrossed(newTask, task))
                .findFirst()
                .ifPresentOrElse(
                        task -> {
                            String message = "Задача пересекается с задачей с id: " + task.getId() + " с началом: "
                                    + task.getStartTime() + " и концом: " + task.getEndTime();
                            throw new TaskValidationException(message);
                        },
                        () -> prioritizedTasks.add(newTask)
                );
    }

    private void delete(Task task) {
        prioritizedTasks.remove(task);
    }
}