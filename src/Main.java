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
        //спринт6
        inMemoryTaskManager.deleteHistory();
        Task task10 = new Task("1", "тело1", TaskStatus.NEW);
        Task task20 = new Task("2", "тело2", TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.addTask(task10);
        inMemoryTaskManager.addTask(task20);
        inMemoryTaskManager.getTaskForId(task10.getId());
        System.out.println("первый таск в истории");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();
        inMemoryTaskManager.getTaskForId(task20.getId());
        System.out.println("добавили второй");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println();
        inMemoryTaskManager.getTaskForId(task10.getId());
        System.out.println("обратились к первому должен появится в конце и пропасть в начале");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Доп задание");
        System.out.println();

        Epic epic01 = new Epic("Эпик1", "Описание 1",TaskStatus.NEW);
        inMemoryTaskManager.addEpic(epic01);
        Subtask subtask01 = new Subtask("подзадача 1", "тело", epic01.getId(), TaskStatus.NEW);
        Subtask subtask02 = new Subtask("подзадача 2", "тело", epic01.getId(), TaskStatus.NEW);
        Subtask subtask03 = new Subtask("подзадача 3", "тело", epic01.getId(), TaskStatus.NEW);
        inMemoryTaskManager.addSubtask(subtask01);
        inMemoryTaskManager.addSubtask(subtask02);
        inMemoryTaskManager.addSubtask(subtask03);
        Epic epic02 = new Epic("Эпик2", "Описание 1",TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.addEpic(epic02);
        System.out.println("Пустой список");
        inMemoryTaskManager.deleteHistory();
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getEpicForId(epic01.getId());
        System.out.println("Первый эпик");
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubtaskForId(subtask01.getId());
        System.out.println("Прибавили подзадачу 1 первого эпика");
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubtaskForId(subtask03.getId());
        System.out.println("Прибавили подзадачу 3 первого эпика");
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getSubtaskForId(subtask01.getId());
        System.out.println("Подзадача 1 первого эпика должна быть в хвосте");
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeSubtaskForId(subtask03.getId());
        System.out.println("Удалили подзадачу 3 эпика 1");
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeEpicForId(epic01.getId());
        System.out.println("Удалили эпик с подзадачей");
        System.out.println(inMemoryTaskManager.getHistory());
    }
}
