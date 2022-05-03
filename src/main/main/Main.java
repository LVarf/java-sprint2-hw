package main;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utility.Managers;
import main.enums.Status;
import taskManager.TaskManager;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        start(Managers.getFileBackendTaskManager()); //раскомментировать для проверки работы программы
    }

    public static void start(TaskManager taskManager) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            print();
            switch (scanner.nextInt()) {
                case 1://Создание нового объекта
                    createNewTask(taskManager);
                    break;
                case 2:
                    writeAllTasks(taskManager);
                    break;
                case 3:
                    changeStatusOfTask(taskManager);
                    break;
                case 4:
                    removeTask(taskManager);
                    break;
                case 5:
                    writeHistory(taskManager);
                    break;
                case 6:
                    writeOnceTask(taskManager);
                    break;
                case 0:
                    System.out.println("Программа успещно завершила работу");
                    System.exit(0);
                default: {
                    System.out.println("Такой команды пока нет");
                }
            }
        }
    }

    static void writeOnceTask(TaskManager taskManager){
        System.out.println("Введите номер задачи");
        Task task;
        task = taskManager.getTasks((new Scanner(System.in)).nextInt());
        System.out.println("Задача " + task.getId() + " " + task.getName());
    }

    static void writeHistory(TaskManager taskManager) {
        for (Task task: taskManager.getHistory()) {
            System.out.println("Задача " + task.getId() + " " + task.getName());
        }
    }//История запросов

    static void print() {
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Создать новую задачу");
        System.out.println("2 - Вывести все задачи");
        System.out.println("3 - Поменять статус задачи");
        System.out.println("4 - Удалить задачу по номеру");
        System.out.println("5 - Показать историю запросов");
        System.out.println("6 - Вывести задачу по id");
        System.out.println("0 - Завершить программу");
    }//Контекстное меню

    static void removeTask(TaskManager taskManager) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите номер задачи");
        long i = scanner.nextLong();
        taskManager.removeTask(i);
        System.out.println("Задача " + i + " удалена");

    }//Удаляет задачу по id

    static void changeStatusOfTask(TaskManager taskManager) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите номер задачи или подзадачи");
        Task o = taskManager.getTasks(scanner.nextInt());
        if ((o.getClass() != SubTask.class) && (o.getClass() != Epic.class)){
            Task task = o;
            System.out.println("Теперь задача: 1 - новая, 2 - в процессе, 3 - выполнена");
            switch (scanner.nextInt()){
                case 1:
                    task.setStatus(Status.NEW);
                    break;
                case 2:
                    task.setStatus(Status.IN_PROGRESS);
                    break;
                case 3:
                    task.setStatus(Status.DONE);
                    break;
                default:
                    System.out.println("Такой статус не предусмотрен");
            }
            taskManager.updateTasks(task);
        } else if (o.getClass() != Epic.class) {
            SubTask sT = (SubTask) o;
                System.out.println("Теперь подзадача: 1 - новая, 2 - в процессе, 3 - выполнена");
                switch (scanner.nextInt()){
                    case 1:
                        sT.setStatus(Status.NEW);
                        break;
                    case 2:
                        sT.setStatus(Status.IN_PROGRESS);
                        break;
                    case 3:
                        sT.setStatus(Status.DONE);
                        break;
                    default:
                        System.out.println("Такой статус не предусмотрен");
                }
            taskManager.updateTasks(sT);
        } else System.out.println("Нет задачи / подзадачи с таким номером");
    }//Изменяет статус задачи

    static void createNewTask(TaskManager taskManager){
        System.out.println("Что вы хотите создать?");
        System.out.println("1 - задачу");
        System.out.println("2 - эпик");
        System.out.println("3 - подзадачу");

        switch ((new Scanner(System.in)).nextInt()){
            case 1:
                System.out.println("Введите название задачи");
                Task task = new Task();
                task.setName((new Scanner(System.in)).nextLine());
                System.out.println("Введите описание задачи");
                task.setDescription((new Scanner(System.in)).nextLine());
                taskManager.addNewTask(task);
                break;
            case 2:
                System.out.println("Введите название эпика");
                Epic epic = new Epic();
                epic.setName((new Scanner(System.in)).nextLine());
                System.out.println("Введите описание эпика");
                epic.setDescription((new Scanner(System.in)).nextLine());
                taskManager.addNewEpic(epic);
                break;
            case 3:
                System.out.println("К какому эпику относится подзадача");
                SubTask sT = new SubTask();
                sT.setEpicId((new Scanner(System.in)).nextLong());
                System.out.println("Введите название подзадачи");
                sT.setName((new Scanner(System.in)).nextLine());
                System.out.println("Введите описание подзадачи");
                sT.setDescription((new Scanner(System.in)).nextLine());
                taskManager.addNewSubTask(sT);
                break;
            default:
                System.out.println("Такой команды не предусмотрено");
                break;
        }
    }//Создание новой задачи

    static void writeAllTasks(TaskManager taskManager){
        for(Task task: taskManager.returnAllTasks()){
            System.out.println(task.toString());
        }
    }//Вывод всех задач
}