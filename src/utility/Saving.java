package utility;

import taskException.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import enums.Status;
import enums.TaskTypes;
import historyManager.HistoryManager;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Saving {

    public static void writeTaskInFile(File  tasksFile, String str) throws IOException {
        String[] strFileArray = Files.readString(tasksFile.toPath()).split("\n");

        try (BufferedWriter bw = new BufferedWriter (new FileWriter(tasksFile))) {
            List<String> list = new ArrayList<>(List.of(strFileArray));

            if (list.size() > 1) {
                if (list.get(list.size() - 2).isBlank()) {//There is a history
                    list.set(list.size() - 2, str + "\n");
                    for (String st : list) {
                        bw.append(st + "\n");
                    }
                } else {
                    list.add(str);
                    for (String st : list) {
                        bw.append(st + "\n");
                    }
                }
            } else if (list.size() == 1) {
                list.add(str);
                for (String st : list) {
                    bw.append(st + "\n");
                }
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
            List<String> list = new ArrayList<>(List.of(strFileArray));

            for (String st: list) {
                String[] newArray = st.split(",");
                if (newArray[0].equals(id.toString())) {
                    newArray[3] = status.toString();
                    bw
                            .append(
                                    String.join(",", newArray)
                            )
                            .append("\n");
                } else {
                    bw
                            .append(st)
                            .append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
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
            List<String> list = new ArrayList<>(List.of(strFileArray));

            if(list.size() > 1) {
                bw.append(list.get(0) + "\n");//rewrite first line
                list.remove(0);

                if (list.get(list.size() - 2).isBlank()) {
                    long j = -1;
                    for (int i = 0; i < list.size() - 2; i++) {
                        String[] newArray = list.get(i).split(",");
                        try {
                            j = Long.parseLong(newArray[0]);
                        } catch (IllegalStateException e) {
                            continue;
                        }
                        if (j != id && j != -1) {
                            bw
                                    .append(list.get(i) + "\n");
                        }
                        if (i == list.size() - 3)
                            bw
                                    .append("\n");
                    }

                    if (!list.get(list.size() - 1).isEmpty()) {
                        List<Long> listHistory = Stream.of(list.get(list.size() - 1).split(","))
                                .map(Long::parseLong).collect(Collectors.toList());//Might a queue is broke

                        StringBuilder sB = new StringBuilder();//There no "," like last symbol

                        for (Long l : listHistory) {
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
                    }

                } else {
                    long j = -1;
                    for (String st: list) {
                        String[] newArray = st.split(",");
                        try {
                            j = Long.parseLong(newArray[0]);
                        } catch (IllegalStateException e) {
                            continue;
                        }
                        if (j == id) {
                            list.remove(j);
                        }
                    }
                    for (String st : list) {
                        bw.append(st);
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
            List<String> list = new ArrayList<>(List.of(strFileArray));

            if(list.size() > 1) {
                if (!list.get(list.size() - 2).isBlank()) {
                    list.add("\n" + task.getId());
                    for (String st : list) {
                        bw.append(st + "\n");
                    }
                } else {
                    List<Long> longs = Arrays.stream(list.get(list.size() - 1).split(","))
                            .map(Long::parseLong).collect(Collectors.toList());
                    longs.remove(task.getId());
                    longs.add(task.getId());
                    list.remove(list.size() - 1);
                    for (String st : list) {
                        bw.append(st + "\n");
                    }
                    for (Long l : longs) {
                        bw.append(l + ",");
                    }
                }
            }
        } catch (IOException e) {
            e.toString();
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
            e.toString();
        } catch (IOException e) {
            e.toString();
        }
        return i > 1;
    }


}
