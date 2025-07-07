import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Переезд", "переезд в другой город", TaskStatus.NEW);
        Task task2 = new Task("Покупка", "покупаем гитару", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1); //добавляем в коллекцию
        taskManager.addTask(task2);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getTaskForId(2)); //поиск по id

        int id = task2.getId();
        task1.setId(id);
        taskManager.updateTask(task1);

        System.out.println(taskManager.getTaskForId(id)); //вывод после замены таска
        System.out.println(taskManager.getTaskList());
        System.out.println();
        Epic epic1 = new Epic("Эпик1", "Описание 1",TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("подзадача 1", "тело", epic1.getId(), TaskStatus.NEW);

        Epic epic2 = new Epic("Эпик2", "Описание 1",TaskStatus.IN_PROGRESS);
        taskManager.addEpic(epic2);
        Subtask subtask2 = new Subtask("подзадача 1", "тело", epic2.getId(), TaskStatus.NEW);
        Subtask subtask3 = new Subtask("подзадача 2", "тело", epic2.getId(), TaskStatus.NEW);

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        System.out.println();
        System.out.println(taskManager.getTaskList());
        System.out.println("****************");
        System.out.println(taskManager.getEpicList());
        System.out.println("****************");
        System.out.println(taskManager.getAllSubtask());
        System.out.println("****************");
        System.out.println(subtask2.getStatus());
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        System.out.println(subtask2.getStatus());
        System.out.println(taskManager.getEpicForId(4)); //проверка смены статуса
        System.out.println();
        subtask3.setStatus(TaskStatus.DONE);
        System.out.println(taskManager.getEpicForId(4));
        subtask2.setStatus(TaskStatus.DONE);
        System.out.println(taskManager.getEpicForId(4));
        System.out.println();
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        System.out.println(subtask2);
        taskManager.removeSubtaskForId(6);
        System.out.println();
        System.out.println(taskManager.getEpicForId(4)); //проверка статуса эпика после удаления подзадачи
        taskManager.deleteTaskList();
        taskManager.deleteAllSubtask();
        taskManager.deleteAllEpic();
        System.out.println();
        System.out.println(taskManager.getAllSubtask());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getTaskList());
    }
}
