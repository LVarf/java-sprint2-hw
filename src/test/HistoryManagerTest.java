import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    static HistoryManager historyManager;

    @BeforeEach
    void createObject() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void getHistoryWithEmptyList() {
        assertTrue(historyManager.getHistory().isEmpty(), "Список задач не пустой");
    }

    @Test
    void getHistoryWithDouble() {
        Task task = new Task();
        task.setId(1L);
        historyManager.add(task);
        historyManager.add(task);

        assertTrue(historyManager.getHistory().size() == 1, "Дублирование истории");
    }

    @Test
    void add() {
        Task task = new Task();
        task.setId(1L);
        historyManager.add(task);

        assertTrue(historyManager.getHistory().contains(task), "Задачи нет в истории");
    }

    @Test
    void removeFromStart() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("name1");
        historyManager.add(task1);
        Task task2 = new Task();
        task2.setId(2L);
        task1.setName("name2");
        historyManager.add(task2);

        historyManager.remove(1L);

        assertFalse(historyManager.getHistory().contains(task1), "Задача всё ещё в истории");
    }

    @Test
    void removeFromMiddle() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("name1");
        historyManager.add(task1);
        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("name2");
        historyManager.add(task2);
        Task task3 = new Task();
        task3.setId(3L);
        task3.setName("name3");
        historyManager.add(task3);

        historyManager.remove(2L);

        assertFalse(historyManager.getHistory().contains(task2), "Задача всё ещё в истории");
    }

    @Test
    void removeFromEnd() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("name1");
        historyManager.add(task1);
        Task task2 = new Task();
        task2.setId(2L);
        task1.setName("name2");
        historyManager.add(task2);

        historyManager.remove(2L);

        assertFalse(historyManager.getHistory().contains(task2), "Задача всё ещё в истории");
    }
}