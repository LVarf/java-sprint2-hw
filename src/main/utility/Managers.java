package utility;

import taskManager.FileBackendTaskManager;
import taskManager.HTTPTaskManager;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;
import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public final class Managers {
    public static TaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    public static FileBackendTaskManager getFileBackendTaskManager() {
        File file = new File("tasks.csv");
        return FileBackendTaskManager.loadFromFile(file);
    }

    public static HTTPTaskManager getHTTPTaskManager() {
        try {
            URL url = new URL("http://localhost:8078/");
            return new HTTPTaskManager(url);
        }catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

}
