package utilityTasks;

import taskException.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utility.Managers;
import utility.Saving;
import utitlityHistories.HistoryManager;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackendTaskManager extends InMemoryTaskManager {

    private File tasksFile;
    private final HistoryManager historyManager;

    public FileBackendTaskManager(File tasksFilePath) {
        super();
        this.tasksFile = tasksFilePath; //файл tasks.csv
        historyManager = Managers.getHistoryManager();
    }

    public File getTasksFile() {
        return tasksFile;
    }

    public void save(Task task) {
        try {
            Saving.writeTaskInFile(tasksFile, Saving.toString(task));
        } catch (IOException ex) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackendTaskManager loadFromFile(File file) {

        FileBackendTaskManager fileBackendTaskManager = new FileBackendTaskManager(file);

        if (!fileBackendTaskManager.getTasksFile().exists())
            try {
                Files.createFile(file.toPath());
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    bw
                            .write("id,type,name,status,description,epic");
                }

            } catch (IOException ex) {
                throw new ManagerSaveException();
            }

        else if (Saving.isThereAreTasksInFile(file)){
            try {
                List<Task> list = Saving.fromStringTasks(Files.readString(file.toPath()));;
                long idMax = -1;//for saving max id in field index of this class
                for (Task task: list) {
                    if(task.getTaskTypes() == TaskTypes.TASK) {
                        fileBackendTaskManager.tasks.put(task.getId(), task);
                        if (task.getId() >= idMax)
                            idMax = task.getId();
                    } else if (task.getTaskTypes() == TaskTypes.EPIC){
                        Epic epic = (Epic) task;
                        fileBackendTaskManager.epics.put(epic.getId(), epic);
                        if (epic.getId() >= idMax)
                            idMax = epic.getId();
                    }
                    else if (task.getTaskTypes() == TaskTypes.SUBTASK){
                        SubTask sT = (SubTask) task;
                        fileBackendTaskManager.subTasks.put(sT.getId(), sT);
                        ((Epic) (fileBackendTaskManager.epics.get(sT.getEpicId())))
                                .getListSubTask().add(sT.getId());//add the subTask's ID to an epic's list
                        if (sT.getId() >= idMax)
                            idMax = sT.getId();
                    }
                }
                list.clear();
                fileBackendTaskManager.setIndex(idMax);

                List<Long> listHistory = Saving.fromStringHistory(Files.readString(file.toPath()));
                if(listHistory != null)
                for (Long ls : listHistory) {
                    if(fileBackendTaskManager.tasks.containsKey(ls))
                    fileBackendTaskManager.historyManager.add(fileBackendTaskManager.tasks.get(ls));
                    else if(fileBackendTaskManager.epics.containsKey(ls))
                        fileBackendTaskManager.historyManager.add(fileBackendTaskManager.epics.get(ls));
                    else if(fileBackendTaskManager.subTasks.containsKey(ls))
                        fileBackendTaskManager.historyManager.add(fileBackendTaskManager.subTasks.get(ls));
                }
            } catch (IOException e) {
                throw new ManagerSaveException();
            }
        }

        return fileBackendTaskManager;
    }

    @Override
    public Task getTasks(long id) {
        Task task = super.getTasks(id);
        Saving.writeHistoryInFile(tasksFile, task);
        return task;
    }

    @Override
    public void removeTask(Long id) {
        Saving.removeTask(tasksFile, id);
        if(epics.containsKey(id)){//remove all subTasks included the epic
            Epic epic = (Epic) epics.get(id);
            List<Long> subID = epic.getListSubTask();
            for (Long l : subID) {
                Saving.removeTask(tasksFile, l);
            }
        }
        super.removeTask(id);
    }

    @Override
    public ArrayList<Task> returnAllTasks() {
        ArrayList<Task> list = super.returnAllTasks();
        for (Task task : list) {
            Saving.writeHistoryInFile(tasksFile, task);
        }
        return list;
    }

    @Override
    public List<Task> history() {
        return super.history();
    }

    @Override
    public void updateTasks(Task o) {
        Saving.updateTask(tasksFile,o.getId(),o.getStatus());
        super.updateTasks(o);
    }

    @Override
    public void removeAllTasks() {
        try {
            Files.delete(tasksFile.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        super.removeAllTasks();
    }

    @Override
    public void addNewSubTask(SubTask task) {
        super.addNewSubTask(task);
        save(task);
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save(task);
    }

    @Override
    public void addNewEpic(Epic task) {
        super.addNewEpic(task);
        save(task);
    }

    @Override
    public void setIndex(long index) {
        super.setIndex(index);
    }
}
