package managers;

import exceptions.ManagerSaveException;
import tasks.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File tasks;

    public FileBackedTaskManager(File file) {
        this.tasks = file;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tasks))) {
            if (tasks.length() == 0) { //прописываем первую строку
                writer.write("id,type,name,status,description,class,epicId,startTime,duration,endTime\n");
            }
            for (Task task : getTaskList()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Task task : getEpicList()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Task task : getAllSubtask()) {
                writer.write(toString(task));
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new ManagerSaveException("Ошибка сохранения в файл " + tasks.getName(), ex.getMessage());
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int idCounter = 0;
            reader.readLine(); //пропуск первой строки
            while (reader.ready()) {
                Task task = fromString(reader.readLine());
                final int id = task.getId();
                if (idCounter < id) {
                    idCounter = id;
                }
                if (task.getType() == TaskType.TASK) {
                    taskManager.taskList.put(task.getId(), task);
                } else if (task.getType() == TaskType.EPIC) {
                    taskManager.epicList.put(task.getId(), (Epic) task);
                } else  {
                    taskManager.subtaskList.put(task.getId(), (Subtask) task);
                }
            }
        } catch (IOException ex) {
            throw new ManagerSaveException("Ошибка чтения файла " + file.getName(), ex.getMessage());
        }
        return taskManager;
    }

    public String toString(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId() + ",");
        stringBuilder.append(task.getType() + ",");
        stringBuilder.append(task.getTitle() + ",");
        stringBuilder.append(task.getStatus() + ",");
        stringBuilder.append(task.getDescription() + ",");
        stringBuilder.append(task.getClass().getSimpleName() + ",");
        if (task.getClass() == Subtask.class) {
            stringBuilder.append(((Subtask) task).getEpicId() + ",");
        } else if (task.getClass() == Epic.class) {
            stringBuilder.append(task.getId() + ",");
        } else {
            stringBuilder.append("Null,");
        }
        if (task.getStartTime() != null && task.getEndTime() != null && task.getDuration() != null) {
            stringBuilder.append(task.getStartTime() + ",");
            stringBuilder.append(task.getDuration().toMinutes() + ",");
            stringBuilder.append(task.getEndTime());
        } else {
            stringBuilder.append("Null,Null,Null");
        }
        return stringBuilder.toString();
    }

    public static Task fromString(String value) {
        String[] str = value.split(",");

        if (TaskType.valueOf(str[1]) == (TaskType.TASK)) {
            Task task = new Task(str[2], str[4], TaskStatus.valueOf(str[3]));
            if (str[8] == null || str[7] == null) {
                task.setDuration(Duration.ofMinutes(Integer.parseInt(str[8])));
                task.setStartTime(LocalDateTime.parse(str[7]));
            }
            task.setId(Integer.parseInt(str[0]));
            return task;
        } else if (TaskType.valueOf(str[1]) == (TaskType.EPIC)) {
            Epic task = new Epic(str[2], str[4], TaskStatus.valueOf(str[3]));
            if (str[8] == null || str[7] == null) {
                task.setDuration(Duration.ofMinutes(Integer.parseInt(str[8])));
                task.setStartTime(LocalDateTime.parse(str[7]));
            }
            task.setId(Integer.parseInt(str[0]));
            return task;
        } else {
            Subtask task = new Subtask(str[2], str[4], Integer.parseInt(str[6]), TaskStatus.valueOf(str[3]));
            if (str[8] == null || str[7] == null) {
                task.setDuration(Duration.ofMinutes(Integer.parseInt(str[8])));
                task.setStartTime(LocalDateTime.parse(str[7]));
            }
            task.setId(Integer.parseInt(str[0]));
            return task;
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    public static void main(String[] args) {
        File file = new File("files/Task.txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        Task task001 = new Task("Task001", "Description", TaskStatus.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2024, 2, 2, 11, 0));
        fileBackedTaskManager.addTask(task001);
        Epic epic001 = new Epic("Epic001", "Description",TaskStatus.NEW);
        fileBackedTaskManager.addEpic(epic001);
        Subtask subtask001 = new Subtask("Subtask001", "Description", epic001.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 10, 0));
        fileBackedTaskManager.addSubtask(subtask001);
        Subtask subtask002 = new Subtask("Subtask002", "Description", epic001.getId(), TaskStatus.NEW,
                Duration.ofMinutes(70), LocalDateTime.of(2025, 1, 1, 11, 20));
        fileBackedTaskManager.addSubtask(subtask002);
        fileBackedTaskManager.deleteAllEpic();
        fileBackedTaskManager.deleteAllSubtask();
        fileBackedTaskManager.deleteTaskList();
        FileBackedTaskManager fileBackedTaskManager1 = loadFromFile(file);
    }
}
