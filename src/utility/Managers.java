package utility;

import utilityTasks.InMemoryTaskManager;
import utilityTasks.TaskManager;
import utitlityHistories.HistoryManager;
import utitlityHistories.InMemoryHistoryManager;

public final class Managers {
    public static TaskManager getTaskManager() {
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    public static HistoryManager getHistoryManager() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
