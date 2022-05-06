package taskManager;

import main.enums.Status;
import main.enums.TaskTypes;
import taskException.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import historyManager.HistoryManager;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileBackendTaskManager extends InMemoryTaskManager {

    protected final File tasksFile;

    public FileBackendTaskManager(File tasksFilePath) {
        super();
        this.tasksFile = tasksFilePath; //файл tasks.csv
    }

    public File getTasksFile() {
        return tasksFile;
    }

    public void save() {
        List<String> finalList = new ArrayList<>();
        for (long i = 1; i <= index; i++) {
            if (tasks.containsKey(i)) {
                finalList.add(toString(tasks.get(i)) + "\n"); //write to file
            }
            if (epics.containsKey(i)) {
                finalList.add(toString(epics.get(i)) + "\n");
            }
            if (subTasks.containsKey(i)) {
                finalList.add(toString(subTasks.get(i)) + "\n");
            }
        }
        if (inMemoryHistoryManager.getHistory().size() > 0) {
            finalList.add("\n");
            finalList.add(toStringHistory(inMemoryHistoryManager));
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tasksFile))) {//Clear the old file
            bw.append("id,type,name,status,description,startTime,duration,endTime,epic\n");

        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException();
        }


        try(BufferedWriter bw = new BufferedWriter(new FileWriter(tasksFile, true))) {
            for (String st : finalList) {
                bw.append(st);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException();
        }
    }

    public static String toStringHistory(HistoryManager manager) {
        List<Task> historyList = manager.getHistory();
        StringBuilder sB = new StringBuilder();//last symbol is no ","
        for (Task task : historyList) {
            if (sB.length() == 0)
                sB.append(task.getId());
            else {
                sB.append(",").append(task.getId());
            }
        }
        return sB.toString();
    }

    public static String toString(Task task) {
        String str = task.getId() + "," +
                task.getTaskTypes() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getStartTime() + "," +
                task.getDuration() + "," +
                task.getEndTime();

        if (TaskTypes.SUBTASK.equals(task.getTaskTypes())) {
            SubTask subTask = (SubTask) task;
            str += "," + subTask.getEpicId();
        }

        return str;
    }

    public static FileBackendTaskManager loadFromFile(File file) {

        FileBackendTaskManager fileBackendTaskManager = new FileBackendTaskManager(file);

        if (!fileBackendTaskManager.getTasksFile().exists())
            try {
                Files.createFile(file.toPath());
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    bw
                            .write("id,type,name,status,description,startTime,duration,endTime,epic");
                }

            } catch (IOException ex) {
                ex.getMessage();
            }

        else if (isThereAreTasksInFile(file)){
            try {
                List<Task> list = fromStringTasks(Files.readString(file.toPath()));;
                long idMax = -1;//for saving max id in field index of this class
                for (Task task: list) {
                    if(task.getTaskTypes() == TaskTypes.TASK) {
                        fileBackendTaskManager.tasks.put(task.getId(), task);
                        fileBackendTaskManager.prioritisedSet.add(task);
                        if (task.getId() >= idMax)
                            idMax = task.getId();
                    } else if (task.getTaskTypes() == TaskTypes.EPIC){
                        Epic epic = (Epic) task;
                        fileBackendTaskManager.epics.put(epic.getId(), epic);
                        fileBackendTaskManager.prioritisedSet.add(epic);
                        if (epic.getId() >= idMax)
                            idMax = epic.getId();
                    }
                    else if (task.getTaskTypes() == TaskTypes.SUBTASK){
                        SubTask sT = (SubTask) task;
                        fileBackendTaskManager.subTasks.put(sT.getId(), sT);
                        fileBackendTaskManager.prioritisedSet.add(sT);
                        ((Epic) (fileBackendTaskManager.epics.get(sT.getEpicId())))
                                .getListSubTask().add(sT.getId());//add the subTask's ID to an epic's list
                        if (sT.getId() >= idMax)
                            idMax = sT.getId();
                    }
                }
                list.clear();
                fileBackendTaskManager.setIndex(idMax);

                List<Long> listHistory = fromString(Files.readString(file.toPath()));
                if(listHistory != null)
                for (long ls : listHistory) {
                    if(fileBackendTaskManager.tasks.containsKey(ls))
                     fileBackendTaskManager.inMemoryHistoryManager.add(fileBackendTaskManager.tasks.get(ls));
                    else if(fileBackendTaskManager.epics.containsKey(ls))
                        fileBackendTaskManager.inMemoryHistoryManager.add(fileBackendTaskManager.epics.get(ls));
                    else if(fileBackendTaskManager.subTasks.containsKey(ls))
                        fileBackendTaskManager.inMemoryHistoryManager.add(fileBackendTaskManager.subTasks.get(ls));
                }
            } catch (IOException e) {
                e.toString();
            }
        }

        return fileBackendTaskManager;
    }

    @Override
    public Task getTasks(long id) {
        Task task = super.getTasks(id);
        save();
        return task;
    }

    @Override
    public void removeTask(Long id) {
        super.removeTask(id);
        save();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> list = super.getAllTasks();
        save();
        return list;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = super.getHistory();
        save();
        return list;
    }

    @Override
    public void updateTasks(Task o) {
        super.updateTasks(o);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void addNewSubTask(SubTask task) {
        super.addNewSubTask(task);
        save();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic task) {
        super.addNewEpic(task);
        save();
    }

    @Override
    public void setIndex(long index) {
        super.setIndex(index);
    }

    private static List<Long> fromString(String value) {
        String[] array = value.split("\n");
        String lastLine = "";
        if (array.length < 3)
            return null;
        else if (!array[array.length - 2].isBlank())//using empty line like flag for separate main.tasks and  history
            return null;
        else {
            lastLine = array[array.length - 1];
        }

        String[] storyList = lastLine.split(",");
        List<Long> list = new ArrayList<>();
        for (String i : storyList) {
            list.add(Long.parseLong(i));
        }
        return list;
    }

    private static List<Task> fromStringTasks(String value) {

        List<Task> list = new ArrayList<>();
        String[] splitString = value.split("\n");
        if(splitString[splitString.length -2].isBlank())
            for (int i = 1; i < splitString.length - 2; i++) {
                String[] split = splitString[i].split(",");
                if(TaskTypes.TASK.equals(TaskTypes.valueOf(split[1]))) {
                    Task task = new Task();
                    task.setId(Long.parseLong(split[0]));
                    task.setTaskTypes(TaskTypes.valueOf(split[1]));
                    task.setName(split[2]);
                    task.setStatus(Status.valueOf(split[3]));
                    task.setDescription(split[4]);
                    task.setStartTime((split[5].equals("null")) ? null : LocalDateTime.parse(split[5]));
                    task.setDuration((split[6].equals("null")) ? null : Long.parseLong(split[6]));
                    list.add(task);
                } else if (TaskTypes.EPIC.equals(TaskTypes.valueOf(split[1]))) {
                    Epic epic = new Epic();
                    epic.setId(Long.parseLong(split[0]));
                    epic.setTaskTypes(TaskTypes.valueOf(split[1]));
                    epic.setName(split[2]);
                    epic.setStatus(Status.valueOf(split[3]));
                    epic.setDescription(split[4]);
                    epic.setStartTime((split[5].equals("null")) ? null : LocalDateTime.parse(split[5]));
                    epic.setDuration((split[6].equals("null")) ? null : Long.parseLong(split[6]));
                    epic.setEndTime((split[7].equals("null")) ? null : LocalDateTime.parse(split[7]));
                    list.add(epic);
                } else if (TaskTypes.SUBTASK.equals(TaskTypes.valueOf(split[1]))) {
                    SubTask subTask = new SubTask();
                    subTask.setId(Long.parseLong(split[0]));
                    subTask.setTaskTypes(TaskTypes.valueOf(split[1]));
                    subTask.setName(split[2]);
                    subTask.setStatus(Status.valueOf(split[3]));
                    subTask.setDescription(split[4]);
                    subTask.setStartTime((split[5].equals("null")) ? null : LocalDateTime.parse(split[5]));
                    subTask.setDuration((split[6].equals("null")) ? null : Long.parseLong(split[6]));
                    try {
                        subTask.setEpicId(Long.parseLong(split[8]));
                    } catch (IndexOutOfBoundsException e) {
                        throw new ManagerSaveException();
                    }
                    list.add(subTask);
                }
            }
        else
            for (int i = 1; i < splitString.length; i++) {
                String[] split = splitString[i].split(",");
                if(TaskTypes.TASK.equals(TaskTypes.valueOf(split[1]))) {
                    Task task = new Task();
                    task.setId(Long.parseLong(split[0]));
                    task.setTaskTypes(TaskTypes.valueOf(split[1]));
                    task.setName(split[2]);
                    task.setStatus(Status.valueOf(split[3]));
                    task.setDescription(split[4]);
                    task.setStartTime((split[5].equals("null")) ? null : LocalDateTime.parse(split[5]));
                    task.setDuration((split[6].equals("null")) ? null : Long.parseLong(split[6]));
                    list.add(task);
                } else if (TaskTypes.EPIC.equals(TaskTypes.valueOf(split[1]))) {
                    Epic epic = new Epic();
                    epic.setId(Long.parseLong(split[0]));
                    epic.setTaskTypes(TaskTypes.valueOf(split[1]));
                    epic.setName(split[2]);
                    epic.setStatus(Status.valueOf(split[3]));
                    epic.setDescription(split[4]);
                    epic.setStartTime((split[5].equals("null")) ? null : LocalDateTime.parse(split[5]));
                    epic.setDuration((split[6].equals("null")) ? null : Long.parseLong(split[6]));
                    epic.setEndTime((split[7].equals("null")) ? null : LocalDateTime.parse(split[7]));
                    list.add(epic);
                } else if (TaskTypes.SUBTASK.equals(TaskTypes.valueOf(split[1]))) {
                    SubTask subTask = new SubTask();
                    subTask.setId(Long.parseLong(split[0]));
                    subTask.setTaskTypes(TaskTypes.valueOf(split[1]));
                    subTask.setName(split[2]);
                    subTask.setStatus(Status.valueOf(split[3]));
                    subTask.setDescription(split[4]);
                    subTask.setStartTime((split[5].equals("null")) ? null : LocalDateTime.parse(split[5]));
                    subTask.setDuration((split[6].equals("null")) ? null : Long.parseLong(split[6]));
                    try {
                        subTask.setEpicId(Long.parseLong(split[8]));
                    } catch (IndexOutOfBoundsException e) {
                        throw new ManagerSaveException();
                    }
                    list.add(subTask);
                }
            }
        return list;
    }

    private static boolean isThereAreTasksInFile(File file) {
        long i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            i = br.lines().count();
        } catch (FileNotFoundException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        }
        return i > 1;
    }
}
