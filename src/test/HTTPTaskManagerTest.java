import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import main.enums.TaskTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManager.HTTPTaskManager;
import tasks.Task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {
    private HTTPTaskManager httpTaskManager = null;
    private Task task = null;
    private final Gson gson = new GsonBuilder()
//            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>(){
//                private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
//                private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
//
//                @Override
//                public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
//                    jsonWriter.value(localDate.format(formatterWriter));
//                }
//
//                @Override
//                public LocalDateTime read(final JsonReader jsonReader) throws IOException {
//                    return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
//                }
//            })
            .serializeNulls()
            .create();

    @BeforeEach
    void createObject() {
        try {
            httpTaskManager = new HTTPTaskManager(new URL("http://localhost:8078/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        task = new Task();
        task.setName("taskName");
        task.setId(1L);
    }

    @AfterEach
    void stopKVServer() {
        try {
            URI url = URI.create("http://localhost:8078/stop");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(url)
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            HttpResponse<String> response = client.send(request, handler);
            System.out.println("Код ответа при закрытии сервера: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void save() {
        task.setTaskTypes(TaskTypes.TASK);
        httpTaskManager.addNewTask(task);
        httpTaskManager.save();
        try {
            assertEquals(gson.toJson(task), httpTaskManager.getClient()
                    .load(httpTaskManager.getClient().getKey()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    void loadFromFile() {
        try {
            httpTaskManager.getClient().put(null, gson.toJson(task));
            HTTPTaskManager httpTaskManager = HTTPTaskManager.loadFromFile(new URL("http://localhost:8078/"));
            assertTrue(httpTaskManager.getTasks().containsValue(task));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}