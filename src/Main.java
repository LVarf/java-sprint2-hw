import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        //start(manager);
    }

    public static void start(Manager manager) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            print();
            switch (scanner.nextInt()) {
                case 1://Создание нового объекта
                    createNewTask(manager);
                    break;
                case 2:
                    writeAllTasks(manager);
                    break;
                case 3:
                    changeStatusOfTask(manager);
                    break;
                case 4:
                    removeTask(manager);
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

    static void removeTask(Manager manager) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите номер задачи");
        manager.removeTask(scanner.nextInt());
    }

    static void changeStatusOfTask(Manager manager) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите номер задачи");
        Object o = manager.getTasks(scanner.nextInt());
        Epic epic = (Epic) o;
        if (epic.getList() == null) {
            System.out.println("Теперь она: 1 - новая, 2 - в процессе, 3 - выполнена");
            switch (scanner.nextInt()){
                case 1:
                    epic.setStatus("new");
                    break;
                case 2:
                    epic.setStatus("in_progress");
                    break;
                case 3:
                    epic.setStatus("done");
                    break;
                default:
                    System.out.println("Такой статус не предусмотрен");
            }
        } else {
            System.out.println("Введите номер подзадачи");
            int nST = scanner.nextInt() - 1;
            if ((nST >= 0) && (nST < epic.getList().size())) {
                Object o1 = epic.getList().get(nST);
                SubTask sT = (SubTask) o1;
                System.out.println("Теперь она: 1 - новая, 2 - в процессе, 3 - выполнена");
                switch (scanner.nextInt()){
                    case 1:
                        sT.setStatus("new");
                        break;
                    case 2:
                        sT.setStatus("in_progress");
                        break;
                    case 3:
                        sT.setStatus("done");
                        break;
                    default:
                        System.out.println("Такой статус не предусмотрен");
                }
            } else System.out.println("Нет подзадачи с таким номером");
        }
        manager.updateTasks(epic);
    }

    static void print() {
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Создать новую задачу");
        System.out.println("2 - Вывести все задачи");
        System.out.println("3 - Поменять статус задачи");
        System.out.println("4 - Удалить задачу по номеру");
        System.out.println("0 - Завершить программу");
    }

    static void writeAllTasks(Manager manager){
        for(Object o: manager.returnAllTasks()){
            Epic epic = (Epic) o;
            System.out.println("Задача " + epic.getId() + ": " + epic.getName() + " - " + changeStatusInRU(epic.getStatus()));
            System.out.println("Описание " + epic.getDescription());
            if (epic.getList() != null) {
                for (SubTask sT: epic.getList()){
                    System.out.println("\tПодзадача " + ": " + sT.getId() +" - " + sT.getName() + " - " + changeStatusInRU(sT.getStatus()));
                    System.out.println("\tОписание " + sT.getDescription());
                }
            }
        }
    }

    static void createNewTask(Manager manager){
        Scanner scanner = new Scanner(System.in);
        Epic epic = new Epic();
        ArrayList<SubTask> list = new ArrayList<>();

        System.out.println("Введите название задачи");
        epic.setName(scanner.nextLine());
        System.out.println("Введите описание задачи");
        epic.setDescription(scanner.nextLine());
        int i = 1;
        while (true){
            System.out.println("У задачи есть подзадачи?: y - да, n - нет");
            String yn = scanner.nextLine();
            if((yn.equals("y")) || yn.equals("n")) {
                if (yn.equals("y")) {
                    list.add(addSubTask(i));
                    i++;
                } else {
                    if (list.size() == 0) list = null;
                    break;
                }
            } else
                System.out.println("Вы ввели неверную команду");
        }
        epic.setList(list);
        manager.addNewTask(epic);
    }

    static SubTask addSubTask(int i){
        Scanner scanner = new Scanner(System.in);
        SubTask o = new SubTask();
        o.setId(i);
        System.out.println("Введите название подзадачи");
        o.setName(scanner.nextLine());
        System.out.println("Введите описание подзадачи");
        o.setDescription(scanner.nextLine());
        return o;
    }

    static String changeStatusInRU(String str) {
        if (str.equals("new")) {
            str = "новая";
        } else if (str.equals("in_progress")) {
            str = "в процессе";
        } else if (str.equals("done")){
            str = "выполнена";
        } else str = "error";
        return str;
    }
}