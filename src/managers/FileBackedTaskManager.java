package managers;

import exceptions.ManagerSaveException;
import tasks.*;
import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File tasks;

    public FileBackedTaskManager(File file) {
        this.tasks = file;
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tasks))) {
            if (tasks.length() == 0) { //прописываем первую строку
                writer.write("id,type,name,status,description,epic\n");
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
            reader.readLine(); //пропуск первой строки
            while (reader.ready()) {
                Task task = fromString(reader.readLine());
                if (task.getType() == TaskType.TASK) {
                    taskManager.addTask(task);
                } else if (task.getType() == TaskType.EPIC) {
                    taskManager.addEpic((Epic) task);
                } else  {
                    taskManager.addSubtask((Subtask) task);
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
            stringBuilder.append(((Subtask) task).getEpicId());
        }
        return stringBuilder.toString();
    }

    public static Task fromString(String value) {
        String[] str = value.split(",");

        if (TaskType.valueOf(str[1]) == (TaskType.TASK)) {
            Task task = new Task(str[2], str[4], TaskStatus.valueOf(str[3]));
            task.setId(Integer.parseInt(str[0]));
            return task;
        } else if (TaskType.valueOf(str[1]) == (TaskType.EPIC)) {
            Epic task = new Epic(str[2], str[4], TaskStatus.valueOf(str[3]));
            task.setId(Integer.parseInt(str[0]));
            return task;
        } else {
            Subtask task = new Subtask(str[2], str[4], Integer.parseInt(str[6]), TaskStatus.valueOf(str[3]));
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
        Task task001 = new Task ("Task001", "Description", TaskStatus.NEW);
        fileBackedTaskManager.addTask(task001);
        Epic epic001 = new Epic("Epic001", "Description",TaskStatus.NEW);
        fileBackedTaskManager.addEpic(epic001);
        Subtask subtask001 = new Subtask("Subtask001", "Description", epic001.getId(), TaskStatus.NEW);
        fileBackedTaskManager.addSubtask(subtask001);
        fileBackedTaskManager.deleteAllEpic();
        fileBackedTaskManager.deleteAllSubtask();
        fileBackedTaskManager.deleteTaskList();
        FileBackedTaskManager fileBackedTaskManager1 = loadFromFile(file);
    }
}
