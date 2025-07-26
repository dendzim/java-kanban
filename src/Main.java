import managers.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Переезд", "переезд в другой город", TaskStatus.NEW);
        Task task2 = new Task("Покупка", "покупаем гитару", TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.addTask(task1); //добавляем в коллекцию
        inMemoryTaskManager.addTask(task2);

        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println(inMemoryTaskManager.getTaskForId(2)); //поиск по id

        int id = task2.getId();
        task1.setId(id);
        inMemoryTaskManager.updateTask(task1);

        System.out.println(inMemoryTaskManager.getTaskForId(id)); //вывод после замены таска
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("Тестирование эпиков");
        Epic epic1 = new Epic("Эпик1", "Описание 1",TaskStatus.NEW);
        inMemoryTaskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("подзадача 1", "тело", epic1.getId(), TaskStatus.NEW);

        Epic epic2 = new Epic("Эпик2", "Описание 1",TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.addEpic(epic2);
        Subtask subtask2 = new Subtask("подзадача 1", "тело", epic2.getId(), TaskStatus.NEW);
        Subtask subtask3 = new Subtask("подзадача 2", "тело", epic2.getId(), TaskStatus.NEW);

        inMemoryTaskManager.addSubtask(subtask1);
        inMemoryTaskManager.addSubtask(subtask2);
        inMemoryTaskManager.addSubtask(subtask3);
        System.out.println("печатаем список тасков");
        System.out.println(inMemoryTaskManager.getTaskList());
        System.out.println("печатаем список эпиков");
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println("печатаем все подзадачи");
        System.out.println(inMemoryTaskManager.getAllSubtask());
        System.out.println("печатаем субтаски 2 эпика");
        System.out.println(inMemoryTaskManager.getEpicSubtask(4));
        System.out.println("выводим статус подзадачи 2 эпика 2");
        System.out.println(subtask2.getStatus());
        System.out.println("меняем статус этой подзадачи");
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask2);
        System.out.println("выводим статус подзадачи 2 эпика 2");
        System.out.println(subtask2.getStatus());
        System.out.println("выводим эпик по ай ди ");
        System.out.println(inMemoryTaskManager.getEpicForId(4)); //проверка смены статуса
        System.out.println("меняем статус 3 подзадачи эпика 2");
        subtask3.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateSubtask(subtask3);
        System.out.println(subtask3.getStatus());
        System.out.println("выводим этот же эпик");
        System.out.println(inMemoryTaskManager.getEpicForId(4));
        System.out.println("меняем статус 2 подзадачи эпика 2");
        subtask2.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateSubtask(subtask2);
        System.out.println(subtask2.getStatus());
        System.out.println("выводим этот же эпик должно быть done");
        System.out.println(inMemoryTaskManager.getEpicForId(4));
        System.out.println("меняем статус 2 подзадачи эпика 2");
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateSubtask(subtask3);
        System.out.println(subtask3.getStatus());
        System.out.println("выводим этот же эпик должно быть in progress");
        System.out.println(inMemoryTaskManager.getEpicForId(4));
        System.out.println();
        System.out.println(inMemoryTaskManager.getEpicSubtask(4));
        System.out.println("удаляем подзадачу 1 эпика 2");
        inMemoryTaskManager.removeSubtaskForId(6);
        System.out.println(inMemoryTaskManager.getEpicSubtask(4));
        System.out.println("выводим этот же эпик ");
        System.out.println(inMemoryTaskManager.getEpicForId(4));
        System.out.println("удаляем подзадачу 2 эпика 2");
        inMemoryTaskManager.removeSubtaskForId(7);
        System.out.println(inMemoryTaskManager.getEpicSubtask(4));
        System.out.println("выводим этот же эпик");
        System.out.println(inMemoryTaskManager.getEpicForId(4));
        inMemoryTaskManager.deleteTaskList();
        inMemoryTaskManager.deleteAllSubtask();
        inMemoryTaskManager.deleteAllEpic();
        System.out.println();
        System.out.println(inMemoryTaskManager.getAllSubtask());
        System.out.println(inMemoryTaskManager.getEpicList());
        System.out.println(inMemoryTaskManager.getTaskList());
    }
}
