package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.adapters.DurationAdapter;
import http.adapters.LocalDateTimeAdapter;
import http.handlers.*;
import managers.InMemoryTaskManager;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private final HttpServer kanbanServer;
    private final TaskManager manager;


    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;

        kanbanServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        kanbanServer.createContext("/tasks", new TaskHandler(manager));
        kanbanServer.createContext("/subtasks", new SubtaskHandler(manager));
        kanbanServer.createContext("/epics", new EpicHandler(manager));
        kanbanServer.createContext("/history", new HistoryHandler(manager));
        kanbanServer.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

    public void start() {
        kanbanServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        kanbanServer.stop(3);
    }

    public static void main(String[] args) {
        try {
            TaskManager manager = new InMemoryTaskManager();
            HttpTaskServer server = new HttpTaskServer(manager);
            server.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nОстанавливаем сервер...");
                server.stop();
            }));

            System.out.println("Нажмите Ctrl+C для остановки сервера");

        } catch (IOException e) {
            System.err.println("Не удалось запустить сервер: " + e.getMessage());
        }
    }
}