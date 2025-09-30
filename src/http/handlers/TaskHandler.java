package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskValidationException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    private Gson gson;
    private TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
        String path = exchange.getRequestURI().getPath();
        Integer idFromPath = getId(path);
        String method = exchange.getRequestMethod();
        System.out.println("Request: " + method + " " + path + " (id: " + idFromPath + ")");
            switch (method) {
                case "GET": {
                    if (idFromPath == null) {
                        List<Task> tList = taskManager.getTaskList();
                        String response = gson.toJson(tList);
                        System.out.println("Получили список задач");
                        sendText(exchange, response, 200);
                        break;
                    }
                    Task task = taskManager.getTaskForId(idFromPath);
                    if (task != null) {
                        String response = gson.toJson(task);
                        System.out.println("Получили задачу по id " + idFromPath);
                        sendText(exchange, response, 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                case "POST": {
                    String body = readText(exchange);
                    Task task = gson.fromJson(body, Task.class);
                    int taskId = task.getId();
                    if (taskId > 0) {
                        taskManager.updateTask(task);
                        System.out.println("Обновили задачу с id: " + taskId);
                        exchange.sendResponseHeaders(200, -1);
                    } else {
                        try {
                            taskManager.addTask(task);
                            int addId = task.getId();
                            System.out.println("Создали задачу с id: " + addId);
                            String response = gson.toJson(task);
                            sendText(exchange, response, 201);
                        } catch (TaskValidationException ex) {
                            sendHasOverlaps(exchange);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    taskManager.removeTaskForId(idFromPath);
                    System.out.println("Удалили задачу с id " + idFromPath);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                    break;
                }
                default: {
                    System.out.println("/tasks получил: " + exchange.getRequestMethod());
                    exchange.sendResponseHeaders(405, 0);
                    exchange.close();
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка обработки: " + e.getMessage());
            e.printStackTrace();
        }
    }
}