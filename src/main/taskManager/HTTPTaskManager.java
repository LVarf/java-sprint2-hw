package taskManager;


import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackendTaskManager {
    private final KVTaskClient client;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new TypeAdapter <LocalDateTime>(){
                private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");
                private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

                @Override
                public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
                    jsonWriter.value(localDate.format(formatterWriter));
                }

                        @Override
                public LocalDateTime read(final JsonReader jsonReader) throws IOException {
                    return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
                }
            })
            .serializeNulls()
            .create();

    public HTTPTaskManager(URL url) {
        super(null);
        client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        List<String> finalList = new ArrayList<>();
        for (long i = 1; i <= index; i++) {
            if (tasks.containsKey(i)) {
                finalList.add(toString(tasks.get(i)) + "\n"); //write to file
            }
            if (epics.containsKey(i)) {
                finalList.add(toString(epics.get(i)) + "\n");
            }
            if (subTasks.containsKey(i)) {
                finalList.add(toString(subTasks.get(i)) + "\n");
            }
        }
        if (inMemoryHistoryManager.getHistory().size() > 0) {
            finalList.add("\n");
            finalList.add(toStringHistory(inMemoryHistoryManager));
        }
        try {
            client.put(client.getKey(), gson.toJson(finalList.toString()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public KVTaskClient getClient() {
        return client;
    }

    public static HTTPTaskManager loadFromFile(URL url) {

        HTTPTaskManager taskManager = new HTTPTaskManager(url);

        try {

            taskManager.client.load(taskManager.client.getKey());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return taskManager;
    }
}
