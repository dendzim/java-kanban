package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskValidationException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    private Gson gson;
    private TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                        List<Epic> epicList = taskManager.getEpicList();
                        String response = gson.toJson(epicList);
                        System.out.println("Получили список эпиков");
                        sendText(exchange, response, 200);
                        break;
                    } else if ((path.split("/").length > 3) && "subtasks".equals(path.split("/")[3])) {
                        Epic epic = taskManager.getEpicForId(idFromPath);
                        if (epic != null) {
                            List<Subtask> epicSubList = taskManager.getEpicSubtask(idFromPath);
                            String response = gson.toJson(epicSubList);
                            System.out.println("Получили список подзадач эпика");
                            sendText(exchange, response, 200);
                            break;
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        Epic epic = taskManager.getEpicForId(idFromPath);
                        if (epic != null) {
                            String response = gson.toJson(epic);
                            System.out.println("Получили эпик по id " + idFromPath);
                            sendText(exchange, response, 200);
                        } else {
                            sendNotFound(exchange);
                        }
                        break;
                    }
                }
                case "POST": {
                    String body = readText(exchange);
                    Epic epic = gson.fromJson(body, Epic.class);
                    try {
                        taskManager.addEpic(epic);
                        int epicId = epic.getId();
                        System.out.println("Создали эпик с id: " + epicId);
                        String response = gson.toJson(epic);
                        sendText(exchange, response, 201);
                    } catch (TaskValidationException ex) {
                        sendHasOverlaps(exchange);
                    }
                    break;
                }
                case "DELETE": {
                    taskManager.removeEpicForId(idFromPath);
                    System.out.println("Удалили эпик по id " + idFromPath);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                    break;
                }
                default: {
                    System.out.println("/epics получил: " + exchange.getRequestMethod());
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
