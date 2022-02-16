package utility;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final List<Task> historyList;
    private static final int SIZE_OF_HISTORY_LIST = 10;

    public InMemoryHistoryManager() {
        historyList = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return null;
    }

    @Override
    public void add(Task task) {
        if (historyList.size() >= SIZE_OF_HISTORY_LIST) {
            historyList.remove(0);
        }

    }
}
