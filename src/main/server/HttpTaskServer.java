package server;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import taskManager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utility.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import main.enums.Status;
import main.enums.TaskTypes;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter <LocalDateTime>(){
                private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy, hh:mm");
                private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy, hh:mm");

                @Override
                public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
                    jsonWriter.value(localDate.format(formatterWriter));
                }

                @Override
                public LocalDateTime read(final JsonReader jsonReader) throws IOException {
                    return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
                }
            })
            .create();;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) {
        try {
            HttpServer httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TaskHandler());
            httpServer.start();
            new KVTaskServer().start();

        } catch (IOException ex) {
            System.out.println("Start server ERROR...");
        }

    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response;
            TaskManager taskManager = Managers.getFileBackendTaskManager();

            Long id;
            String path = httpExchange.getRequestURI().getPath();
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    if (path.split("/").length < 3) {
                        response = gson.toJson(taskManager.getAllTasks().toString());
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()){
                            os.write(response.getBytes());
                        }
                    } else if (path.split("/")[2].equals("task") ||
                            path.split("/")[2].equals("epic") ||
                            path.split("/")[2].equals("subtask")) {
                        id = Long.parseLong(httpExchange.getRequestURI().toString().split("=")[1]);

                        response = gson.toJson(taskManager.getTasks(id).toString());
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()){
                            os.write(response.getBytes());
                        }
                    } else if (path.split("/")[2].equals("history")) {
                        response = gson.toJson(taskManager.getHistory().toString());
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream os = httpExchange.getResponseBody()){
                            os.write(response.getBytes());
                        }
                    } else {
                        response = "Wrong command";
                        httpExchange.sendResponseHeaders(400, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

                    JsonElement jsonElement = JsonParser.parseString(body);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String taskType = jsonObject.get("taskType").getAsString();
                    String name = jsonObject.get("name").getAsString();
                    String description = jsonObject.get("description").getAsString();
                    String status = jsonObject.get("status").getAsString();
                    response = taskType;
                    if (TaskTypes.TASK.toString().toLowerCase().equals(taskType.toLowerCase())) {
                        Task task = new Task();
                        task.setTaskTypes(TaskTypes.TASK);
                        task.setName(name);
                        task.setDescription(description);
                        if (Status.NEW.toString().equals(status.toUpperCase()))
                            task.setStatus(Status.NEW);
                        else if (Status.IN_PROGRESS.toString().equals(status.toUpperCase()))
                            task.setStatus(Status.IN_PROGRESS);
                        else if (Status.DONE.toString().equals(status.toUpperCase()))
                            task.setStatus(Status.DONE);
                        String startTime = jsonObject.get("startTime").getAsString();
                        Long duration = jsonObject.get("duration").getAsLong();
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");
                        task.setStartTime(LocalDateTime.parse(startTime, dateTimeFormatter));
                        task.setDuration(duration);
                        taskManager.addNewTask(task);
                    } else if (TaskTypes.EPIC.toString().toLowerCase().equals(taskType.toLowerCase())) {
                        Epic task = new Epic();
                        task.setTaskTypes(TaskTypes.EPIC);
                        task.setName(name);
                        task.setDescription(description);
                        taskManager.addNewEpic(task);
                    } else if (TaskTypes.SUBTASK.toString().toLowerCase().equals(taskType.toLowerCase())) {
                        SubTask task = new SubTask();
                        task.setTaskTypes(TaskTypes.SUBTASK);
                        task.setName(name);
                        task.setDescription(description);
                        if (Status.NEW.toString().equals(status.toUpperCase()))
                            task.setStatus(Status.NEW);
                        else if (Status.IN_PROGRESS.toString().equals(status.toUpperCase()))
                            task.setStatus(Status.IN_PROGRESS);
                        else if (Status.DONE.toString().equals(status.toUpperCase()))
                            task.setStatus(Status.DONE);
                        String startTime = jsonObject.get("startTime").getAsString();
                        Long duration = jsonObject.get("duration").getAsLong();
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm");
                        task.setStartTime(LocalDateTime.parse(startTime, dateTimeFormatter));
                        task.setDuration(duration);
                        Long epicId = jsonObject.get("epicId").getAsLong();
                        task.setEpicId(epicId);
                        taskManager.addNewSubTask(task);
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                    break;
                case "DELETE":
                    if (path.split("/").length < 3) {
                        taskManager.removeAllTasks();
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                    } else {
                        id = Long.parseLong(httpExchange.getRequestURI().toString().split("=")[1]);
                        taskManager.removeTask(id);
                        httpExchange.sendResponseHeaders(200, 0);
                        httpExchange.close();
                    }
                    break;
            }
        }
    }
}
