import main.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManager.InMemoryTaskManager;
import taskManager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {

    static InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    void createObject() {
        inMemoryTaskManager = new InMemoryTaskManager();
    }

    @Override
    @Test
    void getTask() {
        Task task = new Task();
        inMemoryTaskManager.addNewTask(task);

        assertEquals(task, inMemoryTaskManager.getTasks(task.getId()));
    }

    @Test
    void getTasksWithEmptyList() {
        assertNull(inMemoryTaskManager.getTasks(0));
    }

    @Override
    @Test
    void removeTask() {
        Task task = new Task();
        inMemoryTaskManager.addNewTask(task);

        assertEquals(1, inMemoryTaskManager.getTasks().size());

        inMemoryTaskManager.removeTask(task.getId());
        assertEquals(0, inMemoryTaskManager.getTasks().size());
    }

    @Test
    void removeTaskWithEmptyList() {
        inMemoryTaskManager.removeTask(1L);

        assertEquals(0, inMemoryTaskManager.getTasks().size());
    }

    @Override
    @Test
     void returnAllTasks() {
        Task task = new Task();
        inMemoryTaskManager.addNewTask(task);
        Task task1 = new Task();
        inMemoryTaskManager.addNewTask(task1);

        assertTrue(inMemoryTaskManager.getAllTasks().contains(task));
        assertTrue(inMemoryTaskManager.getAllTasks().contains(task1));
        assertEquals(inMemoryTaskManager.getAllTasks().size(), inMemoryTaskManager.getTasks().size());
    }

    @Test
    void returnAllTasksWithEmptyList() {
        assertTrue(inMemoryTaskManager.getAllTasks().isEmpty());
        assertEquals(inMemoryTaskManager.getAllTasks().size(), inMemoryTaskManager.getTasks().size());
    }

    @Override
    @Test
    void getHistory() {
        Task task = new Task();
        inMemoryTaskManager.addNewTask(task);
        inMemoryTaskManager.getTasks(task.getId());

        assertTrue(inMemoryTaskManager.getHistory()
                .contains(inMemoryTaskManager.getTasks(task.getId())));

    }

    @Test
    void getHistoryWithEmptyList() {
        assertTrue(inMemoryTaskManager.getHistory()
                .size() == 0);
    }

    @Override
    @Test
    void updateTask() {
        Task task = new Task();
        inMemoryTaskManager.addNewTask(task);

        assertEquals(Status.NEW, task.getStatus());

        task.setStatus(Status.DONE);
        inMemoryTaskManager.updateTasks(task);

        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    void updateTaskWithEmptyList() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> inMemoryTaskManager.updateTasks(new Task())
        );

        assertEquals(null, ex.getMessage());
    }



    @Override
    @Test
    void removeAllTasks() {
        Task task = new Task();
        inMemoryTaskManager.addNewTask(task);
        Task task1 = new Task();
        inMemoryTaskManager.addNewTask(task1);

        inMemoryTaskManager.removeAllTasks();

        assertTrue(inMemoryTaskManager.getTasks().isEmpty());
    }

    @Test
    void removeAllTasksWithEmptyList() {
        inMemoryTaskManager.removeAllTasks();

        assertTrue(inMemoryTaskManager.getTasks().isEmpty());
    }

    @Override
    @Test
    void addNewSubTask() {
        Epic epic = new Epic();
        inMemoryTaskManager.addNewEpic(epic);
        SubTask subTask = new SubTask();
        assertFalse(inMemoryTaskManager.getSubTasks().containsValue(subTask));
        subTask.setEpicId(epic.getId());
        inMemoryTaskManager.addNewSubTask(subTask);
        assertTrue(inMemoryTaskManager.getSubTasks().containsValue(subTask));
    }

    @Override
    @Test
    void addNewTask() {
        Task task = new Task();
        assertFalse(inMemoryTaskManager.getTasks().containsValue(task));
        inMemoryTaskManager.addNewTask(task);
        assertTrue(inMemoryTaskManager.getTasks().containsValue(task));
    }

    @Override
    @Test
    void addNewEpic() {
        Epic epic = new Epic();
        assertFalse(inMemoryTaskManager.getEpics().containsValue(epic));
        inMemoryTaskManager.addNewEpic(epic);
        assertTrue(inMemoryTaskManager.getEpics().containsValue(epic));
    }

    @Test
    void updateEpicStatus() {
        Epic epic = new Epic();
        inMemoryTaskManager.addNewEpic(epic);
        SubTask subTask1 = new SubTask();
        subTask1.setEpicId(epic.getId());
        inMemoryTaskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask();
        subTask2.setEpicId(epic.getId());
        inMemoryTaskManager.addNewSubTask(subTask2);

        assertEquals(Status.NEW, epic.getStatus());

        subTask1.setStatus(Status.DONE);
        inMemoryTaskManager.updateTasks(epic);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        subTask2.setStatus(Status.DONE);
        inMemoryTaskManager.updateTasks(epic);

        assertEquals(Status.DONE, epic.getStatus());

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.updateTasks(epic);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Override
    @Test
    void setIndex() {
        inMemoryTaskManager.setIndex(7);
        assertEquals(7L, inMemoryTaskManager.getIndex());
    }
}
