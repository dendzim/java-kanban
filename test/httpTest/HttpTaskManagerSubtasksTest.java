package httpTest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerSubtasksTest {
    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerSubtasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTaskList();
        manager.deleteAllSubtask();
        manager.deleteAllEpic();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик1", "Описание 1", TaskStatus.NEW, null, null);
        manager.addEpic(epic1);
        Subtask subtask = new Subtask("подзадача", "тело", epic1.getId(), TaskStatus.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> tasksFromManager = manager.getAllSubtask();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("подзадача", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик1", "Описание 1",TaskStatus.NEW);
        manager.addEpic(epic1);
        Subtask subtask = new Subtask("подзадача", "тело", epic1.getId(), TaskStatus.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        manager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Subtask> tasksFromManager = gson.fromJson(response.body(),
                new TypeToken<ArrayList<Subtask>>(){}.getType());

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("подзадача", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubtaskForId() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик1", "Описание 1",TaskStatus.NEW);
        manager.addEpic(epic1);
        Subtask subtask = new Subtask("подзадача", "тело", epic1.getId(), TaskStatus.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        manager.addSubtask(subtask);
        int subId = subtask.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks" + "/" + subId);
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask taskFromManager = gson.fromJson(response.body(), new TypeToken<Subtask>() {}.getType());

        assertEquals(subId, taskFromManager.getId(), "Некорректный id");
        assertEquals("подзадача", taskFromManager.getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteSubtaskForId() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Эпик1", "Описание 1",TaskStatus.NEW);
        manager.addEpic(epic1);
        Subtask subtask = new Subtask("подзадача", "тело", epic1.getId(), TaskStatus.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        manager.addSubtask(subtask);
        int subId = subtask.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks" + "/" + subId);
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask taskFromManager = manager.getSubtaskForId(subId);

        assertNull(taskFromManager, "Задача не удалена");
    }
}