import static org.junit.jupiter.api.Assertions.*;

import main.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManager.InMemoryTaskManager;
import tasks.Epic;
import tasks.SubTask;

class EpicTest {

    private static InMemoryTaskManager inMemoryTaskManager;
    private static Epic epic;

    @BeforeEach
    void createObject() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic();
    }

    @Test
    void getListSubTask(){
        assertEquals(0, epic.getListSubTask().size());
    }

    @Test
    void whenAllSubTasksAreNew() {
        inMemoryTaskManager.addNewEpic(epic);
        SubTask subTask1 = new SubTask();
        subTask1.setEpicId(epic.getId());
        inMemoryTaskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask();
        subTask2.setEpicId(epic.getId());
        inMemoryTaskManager.addNewSubTask(subTask2);

        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void whenAllSubTasksAreDone() {
        inMemoryTaskManager.addNewEpic(epic);
        SubTask subTask1 = new SubTask();
        subTask1.setEpicId(epic.getId());
        subTask1.setStatus(Status.DONE);
        inMemoryTaskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask();
        subTask2.setEpicId(epic.getId());
        subTask2.setStatus(Status.DONE);
        inMemoryTaskManager.addNewSubTask(subTask2);

        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void whenAllSubTasksAreNewAndDone() {
        inMemoryTaskManager.addNewEpic(epic);
        SubTask subTask1 = new SubTask();
        subTask1.setEpicId(epic.getId());
        subTask1.setStatus(Status.NEW);
        inMemoryTaskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask();
        subTask2.setEpicId(epic.getId());
        subTask2.setStatus(Status.DONE);
        inMemoryTaskManager.addNewSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void whenAllSubTasksAreInProgress() {
        inMemoryTaskManager.addNewEpic(epic);
        SubTask subTask1 = new SubTask();
        subTask1.setEpicId(epic.getId());
        subTask1.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.addNewSubTask(subTask1);
        SubTask subTask2 = new SubTask();
        subTask2.setEpicId(epic.getId());
        subTask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.addNewSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}