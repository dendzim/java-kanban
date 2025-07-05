import java.sql.SQLOutput;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Переезд", "переезд в другой город", taskManager.getId(), TaskStatus.NEW);
        Task task2 = new Task("Покупка", "покупаем гитару", taskManager.getId(), TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1); //добавляем в коллекцию
        taskManager.addTask(task2);

        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getTaskForId(2)); //поиск по id

        int id = task2.getId();
        task1.setId(id);
        taskManager.updateTask(task1);

        System.out.println(taskManager.getTaskForId(id)); //вывод после замены таска
        taskManager.deleteTaskList();
        System.out.println(taskManager.getTaskList());

        Epic epic1 = new Epic("Эпик1", "Описание 1", taskManager.getId(),TaskStatus.NEW);
        Subtask subtask1 = new Subtask("подзадача 1", "тело", taskManager.getId(), TaskStatus.NEW);

        Epic epic2 = new Epic("Эпик2", "Описание 1", taskManager.getId(),TaskStatus.IN_PROGRESS);
        Subtask subtask2 = new Subtask("подзадача 1", "тело", taskManager.getId(), TaskStatus.NEW);
        Subtask subtask3 = new Subtask("подзадача 2", "тело", taskManager.getId(), TaskStatus.NEW);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        taskManager.addSubtask(epic1, subtask1);
        taskManager.addSubtask(epic2, subtask2);
        taskManager.addSubtask(epic2, subtask3);

        System.out.println(epic2.getId());  //проврека присвоения id эпика подзадачам эпика
        System.out.println(subtask2.getEpicId());
        System.out.println(subtask3.getEpicId());

        System.out.println(taskManager.getEpicList());

        taskManager.removeSubtaskForId(6,epic2); //удаление по айди из эпика
        System.out.println(taskManager.getEpicSubtask(epic2));

        System.out.println(taskManager.getEpicList()); //проверка статуса эпика после удаления подзадачи
        taskManager.deleteEpicSubtask(epic2);
        System.out.println(taskManager.getEpicSubtask(epic2));
    }
}
