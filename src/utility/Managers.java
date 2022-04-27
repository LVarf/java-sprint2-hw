package utility;

import taskManager.FileBackendTaskManager;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;
import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;

import java.io.File;

public final class Managers {
    public static TaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    public static FileBackendTaskManager getFileBackendTaskManager() {
        File file = new File("tasks.csv");
        return FileBackendTaskManager.loadFromFile(file);
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

}
