package utility;

import taskException.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utilityTasks.Status;
import utilityTasks.TaskTypes;
import utitlityHistories.HistoryManager;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Saving {

    public static void writeTaskInFile(File  tasksFile, String str) throws IOException {
        String[] strFileArray = Files.readString(tasksFile.toPath()).split("\n");

        try (BufferedWriter bw = new BufferedWriter (new FileWriter(tasksFile))) {
            for (String s : strFileArray) {
                System.out.println(s);
            }

            if (strFileArray.length > 1) {
                if(!strFileArray[strFileArray.length - 2].isBlank()) {
                    for (int i = 0; i < strFileArray.length; i++) {
                        if (!strFileArray[i].isBlank())
                            bw
                                    .append(strFileArray[i])
                                    .append("\n");
                    }
                    bw
                            .append(str)
                            .append("\n\n");
                } else
                if (strFileArray[strFileArray.length - 2].isBlank()) {
                    for (int i = 0; i < strFileArray.length - 1; i++) {

                        if (!strFileArray[i].isBlank())
                            bw
                                    .append(strFileArray[i])
                                    .append("\n");
                    }
                    bw
                            .append(str)
                            .append("\n\n");
                    bw
                            .append(strFileArray[strFileArray.length - 1]);
                }
            } else if (strFileArray.length == 1) {
                bw
                        .write(strFileArray[0]);
                bw
                        .append('\n')
                        .append(str)
                        .append("\n\n");
            } else {
                System.out.println("Ты, глупышь, где-то накосячил");
            }
        }
    }

    public static void updateTask(File file, Long id, Status status) {//update task in file
        String[] strFileArray = new String[0];
        try {
            strFileArray = Files.readString(file.toPath()).split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter (new FileWriter(file))) {

            if (strFileArray[strFileArray.length - 2].isEmpty()) {
                for (int i = 0; i < strFileArray.length - 2; i++) {
                    String[] newArray = strFileArray[i].split(",");
                    if (newArray[0].equals(id.toString())) {
                        newArray[3] = status.toString();
                        bw
                                .append(
                                        String.join(",", newArray)
                                )
                                .append("\n");
                    } else {
                        bw
                                .append(strFileArray[i])
                                .append("\n");
                    }
                }
                bw
                        .append("\n");
                    bw
                            .append(strFileArray[strFileArray.length - 1]);
            } else {
                for (int i = 0; i < strFileArray.length; i++) {
                    String[] newArray = strFileArray[i].split("1");
                    if (newArray[0].equals(id.toString())) {
                        newArray[3] = status.toString();
                        bw
                                .append(
                                        String.join(",", newArray)
                                )
                                .append("\n");
                    } else {
                        bw
                                .append(strFileArray[i])
                                .append("\n");
                    }
                }
                bw
                        .append("\n");
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    public static void removeTask(File file, Long id) {
        String[] strFileArray = new String[0];
        try {
            strFileArray = Files.readString(file.toPath()).split("\n");
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        try (BufferedWriter bw = new BufferedWriter (new FileWriter(file))) {
            if(strFileArray.length > 1) {
                bw.append(strFileArray[0] + "\n");//rewrite first line

                if (strFileArray[strFileArray.length - 2].isEmpty()) {
                    long j = -1;
                    for (int i = 1; i < strFileArray.length - 2; i++) {
                        String[] newArray = strFileArray[i].split(",");
                        try {
                            j = Long.parseLong(newArray[0]);
                        } catch (IllegalStateException e) {
                            continue;
                        }
                        if (j != id && j != -1) {
                            bw
                                    .append(strFileArray[i])
                                    .append("\n");
                        }
                        if (i == strFileArray.length - 3)
                            bw
                                    .append("\n");
                    }

                    List<Long> listHistory = Stream.of(strFileArray[strFileArray.length - 1].split(","))
                            .map(Long::parseLong).collect(Collectors.toList());//Might a queue is broke

                    StringBuilder sB = new StringBuilder();//There no "," like last symbol

                    for (Long l: listHistory) {
                        if (l != id) {
                            if (sB.length() == 0) {
                                sB.append(l);
                            } else {
                                sB.append("," + l);
                            }
                        }
                    }

                    bw
                            .append(sB.toString());

                } else {
                    long j = -1;
                    for (int i = 1; i < strFileArray.length; i++) {
                        String[] newArray = strFileArray[i].split(",");
                        try {
                            j = Long.parseLong(newArray[0]);
                        } catch (IllegalStateException e) {
                            continue;
                        }
                        if (j != id && j != -1) {
                            bw
                                    .append(strFileArray[i])
                                    .append("\n");
                            if (i == strFileArray.length - 3)
                                bw
                                        .append("\n");
                        }
                    }
                }
            } else throw new ManagerSaveException();
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }


    public static void writeHistoryInFile (File tasksFile, Task task) {
        String[] strFileArray = new String[0];
        try {
            strFileArray = Files.readString(tasksFile.toPath()).split("\n");
        } catch (IOException e) {
            throw new ManagerSaveException();
        }

        try (BufferedWriter bw = new BufferedWriter (new FileWriter(tasksFile))) {

            String strFile = "";
            if(strFileArray.length>1) {
                if(!strFileArray[strFileArray.length-2].isBlank())
                    for (int i = 0; i < strFileArray.length; i++) {
                        strFile += strFileArray[i] + "\n";
                    }
                else for (int i = 0; i < strFileArray.length - 2; i++) {
                    strFile += strFileArray[i] + "\n";
                }
                strFile += "\n";
            }

            if(strFileArray.length > 1 && strFileArray[strFileArray.length-2].isBlank()) {
                List<Long> strOldHistoryList = new ArrayList<>();
                String strOldHistory = strFileArray[strFileArray.length - 1];
                for (String taskIdStr : strOldHistory.split(",")) {
                    strOldHistoryList.add(Long.parseLong(taskIdStr));
                }

                for (Long taskId : strOldHistoryList) {
                    if (taskId != task.getId()) {
                        strFile += taskId + ",";
                    }
                }
            }
            strFile += task.getId();

            bw
                    .append(strFile);

            //При запросе истории делать проверку пустой файл или нет "Have no tasks"
        } catch (IOException e) {
            System.out.println("IOException");
        }
     }

    public static String toString(Task task) {
        String str = task.getId() + "," +
                task.getTaskTypes() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription();
        if (task.getTaskTypes() == TaskTypes.SUBTASK) {
            SubTask sT =(SubTask) task;
            str +="," + sT.getEpicId();
        }
        return str;
    }

    public static String toStringHistory(HistoryManager manager) {
        List<Task> historyList = manager.getHistory();
        StringBuilder sB = new StringBuilder();//There no "," like last symbol
        for (Task task : historyList) {
            if (sB == null)
                sB.append(task.getId());
            else {
                sB.append("," + task.getId());
            }
        }
        return sB.toString();
    }

    public static List<Long> fromStringHistory(String value) {
        String[] array = value.split("\n");
        String lastLine = "";
        if (array.length < 3)
            return null;
        else if (!array[array.length - 2].isEmpty())//using empty line like flag for separate tasks and  history
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

    public static List<Task> fromStringTasks(String value) {

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
                list.add(task);
            } else if (TaskTypes.EPIC.equals(TaskTypes.valueOf(split[1]))) {
                Epic epic = new Epic();
                epic.setId(Long.parseLong(split[0]));
                epic.setTaskTypes(TaskTypes.valueOf(split[1]));
                epic.setName(split[2]);
                epic.setStatus(Status.valueOf(split[3]));
                epic.setDescription(split[4]);
                list.add(epic);
            } else if (TaskTypes.SUBTASK.equals(TaskTypes.valueOf(split[1]))) {
                SubTask subTask = new SubTask();
                subTask.setId(Long.parseLong(split[0]));
                subTask.setTaskTypes(TaskTypes.valueOf(split[1]));
                subTask.setName(split[2]);
                subTask.setStatus(Status.valueOf(split[3]));
                subTask.setDescription(split[4]);
                try {
                    subTask.setEpicId(Long.parseLong(split[5]));
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
                    list.add(task);
                } else if (TaskTypes.EPIC.equals(TaskTypes.valueOf(split[1]))) {
                    Epic epic = new Epic();
                    epic.setId(Long.parseLong(split[0]));
                    epic.setTaskTypes(TaskTypes.valueOf(split[1]));
                    epic.setName(split[2]);
                    epic.setStatus(Status.valueOf(split[3]));
                    epic.setDescription(split[4]);
                    list.add(epic);
                } else if (TaskTypes.SUBTASK.equals(TaskTypes.valueOf(split[1]))) {
                    SubTask subTask = new SubTask();
                    subTask.setId(Long.parseLong(split[0]));
                    subTask.setTaskTypes(TaskTypes.valueOf(split[1]));
                    subTask.setName(split[2]);
                    subTask.setStatus(Status.valueOf(split[3]));
                    subTask.setDescription(split[4]);
                    try {
                        subTask.setEpicId(Long.parseLong(split[5]));
                    } catch (IndexOutOfBoundsException e) {
                        throw new ManagerSaveException();
                    }
                    list.add(subTask);
                }
            }
        return list;
    }

    public static boolean isThereAreTasksInFile(File file) {
        long i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            i = br.lines().count();
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException();
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return i > 1;
    }


}
