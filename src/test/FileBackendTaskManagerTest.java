import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskManager.FileBackendTaskManager;
import tasks.Epic;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackendTaskManagerTest{

    private static FileBackendTaskManager fileBackendTaskManager;

    @BeforeEach
    void createObject() {
        fileBackendTaskManager = new FileBackendTaskManager(new File("tasks.csv"));
    }

    @Test
    void loadFromFileWithEmptyListTasks() {
        FileBackendTaskManager.loadFromFile(fileBackendTaskManager.getTasksFile());

        assertTrue(fileBackendTaskManager.getTasks().isEmpty()
                && fileBackendTaskManager.getEpics().isEmpty()
                && fileBackendTaskManager.getSubTasks().isEmpty()
                && fileBackendTaskManager.getHistory().isEmpty()
        );
    }

    @Test
    void loadFromFileWithEpicWithoutSubTasks() {
        Epic epic = new Epic();
        fileBackendTaskManager.addNewEpic(epic);

        assertTrue(fileBackendTaskManager.getEpics().containsValue(epic), "Задачи нет в списке");
        assertTrue(epic.getListSubTask().isEmpty(), "Список подзадач не пустой");
    }

    @Test
    void loadFromFileWithoutHistory() {
        assertTrue(fileBackendTaskManager.getHistory().isEmpty(), "В истории есть записи");
    }
}