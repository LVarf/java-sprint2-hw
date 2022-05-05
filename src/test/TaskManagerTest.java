import org.junit.jupiter.api.Test;
import taskManager.TaskManager;

public abstract class TaskManagerTest {

    @Test
    abstract void getTask();

    @Test
    abstract void removeTask();

    @Test
    abstract void returnAllTasks();

    @Test
    abstract void getHistory();

    @Test
    abstract void updateTask();

    @Test
    abstract void removeAllTasks();

    @Test
    abstract void addNewSubTask();

    @Test
    abstract void addNewEpic();

    @Test
    abstract void addNewTask();

    @Test
    abstract void setIndex();
}
