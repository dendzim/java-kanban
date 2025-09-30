package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskValidationException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    private Gson gson;
    private TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
                        List<Subtask> subList = taskManager.getAllSubtask();
                        String response = gson.toJson(subList);
                        System.out.println("Получили список подзадач");
                        sendText(exchange, response, 200);
                        break;
                    }
                    Subtask subtask = taskManager.getSubtaskForId(idFromPath);
                    if (subtask != null) {
                        String response = gson.toJson(subtask);
                        System.out.println("Получили подзадачу по id " + idFromPath);
                        sendText(exchange, response, 200);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                }
                case "POST": {
                    String body = readText(exchange);
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    int subtaskId = subtask.getId();
                    if (subtaskId > 0) {
                        taskManager.updateSubtask(subtask);
                        System.out.println("Обновили подзадачу с id: " + subtaskId);
                        exchange.sendResponseHeaders(200, -1);
                    } else {
                        try {
                            taskManager.addSubtask(subtask);
                            int addId = subtask.getId();
                            System.out.println("Создали подзадачу с id: " + addId);
                            String response = gson.toJson(subtask);
                            sendText(exchange, response, 201);
                        } catch (TaskValidationException ex) {
                            sendHasOverlaps(exchange);
                        }
                    }
                    break;
                }
                case "DELETE": {
                    taskManager.removeSubtaskForId(idFromPath);
                    System.out.println("Удалили подзадачу по id " + idFromPath);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                    break;
                }
                default: {
                    System.out.println("/subtasks получил: " + exchange.getRequestMethod());
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
