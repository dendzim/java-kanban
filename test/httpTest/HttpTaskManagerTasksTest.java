package httpTest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import http.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tasks.Task;
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

public class HttpTaskManagerTasksTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
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
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getTaskList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>(){}.getType());

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 1", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskForId() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task);
        int taskId = task.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks" + "/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task taskFromManager = gson.fromJson(response.body(), new TypeToken<Task>() {}.getType());

        assertEquals(taskId, taskFromManager.getId(), "Некорректный id");
        assertEquals("Test 1", taskFromManager.getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTaskForId() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.addTask(task);
        int taskId = task.getId();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks" + "/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1)
                .uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task taskFromManager = manager.getTaskForId(taskId);

        assertNull(taskFromManager, "Задача не удалена");
    }
}