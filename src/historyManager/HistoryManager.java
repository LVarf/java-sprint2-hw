package historyManager;

import tasks.Task;
import java.util.List;

public interface HistoryManager {

    public List<Task> getHistory();

    public void add(Task task);

    public boolean remove(long id);
}
