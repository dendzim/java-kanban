package http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    private Gson gson;
    private TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        System.out.println("Request: " + method + " " + path);
        switch (method) {
            case "GET": {
                Set<Task> history = taskManager.getprioritizedTasks();
                String response = gson.toJson(history);
                System.out.println("Получили приоритетный список");
                sendText(exchange, response, 200);
                return;
            }
            default: {
                System.out.println("/prioritized получил: " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                break;
            }
        }
    }
}