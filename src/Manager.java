import java.lang.module.FindException;
import java.util.HashMap;
import java.util.Scanner;

public class Manager {

    private int index = 1;
    HashMap<Integer, Epic> NEW = new HashMap<>();
    HashMap<Integer, Epic> IN_PROGRESS = new HashMap<>();
    HashMap<Integer, Epic> DONE = new HashMap<>();

    public void start(){
        Scanner scanner = new Scanner(System.in);

        while (true) {
            print();
            switch (scanner.nextInt()) {
                case 1:
                    addEpicNew();
                    break;
                case 2:
                    System.out.println("Список новых задач:");
                    if (!(NEW.isEmpty())) {
                        returnAllTasks(NEW);
                    } else System.out.println("Новых задач нет");

                    System.out.println("Список задач в процессе выполнения:");
                    if (!(IN_PROGRESS.isEmpty())) {
                        returnAllTasks(IN_PROGRESS);
                    } else System.out.println("В процессе выполнения задач нет");

                    System.out.println("Список выполненных задач:");
                    if (!(DONE.isEmpty())) {
                        returnAllTasks(DONE);
                    } else System.out.println("Выполненных задач нет");
                    break;
                case 3:
                    removeAllTasks();
                    break;
                case 4:
                    System.out.println("Введите номер задачи");
                    getTasks(scanner.nextInt());
                    break;
                case 5:
                    System.out.println("Введите номер задачи, которую нужно обновить");
                    updateTasks(scanner.nextInt());
                    break;
                case 6:
                    System.out.println("Введите номер задачи, которую нужно удалить");
                    removeTask(scanner.nextInt());
                    break;
                case 0:
                    System.out.println("Программа успешно завершила работу");
                    System.exit(0);
            default:
                System.out.println("Такой команды пока нет");
            }
        }
    }

    public String statusSubTask(SubTask o){
        String result = "";
        if (o.status == 0){
            result = "новая";
        } else if (o.status == 1) {
            result = "в процессе выполнения";
        } else result = "выполнена";
        return result;
    }

    public void updateEpic(HashMap map, int j, int i, Epic o, int index){//Метод обновляет и перезаписывает епик
        if ((i > 0) && (i < j * 2)) {
            IN_PROGRESS.put(index, o);
            map.remove(index);
            System.out.println("Задача " + index + " теперь в процессе выполнения");
        } else if (i == j * 2) {
            DONE.put(index, o);
            map.remove(index);
            System.out.println("Задача " + index + " выполнена");
        }
    }//Метод обновляет и перезаписывает епик

    public void updateTasks(int index){
        Scanner scanner = new Scanner(System.in);

        if (!(NEW.isEmpty()) && NEW.containsKey(index)) {
            if (NEW.get(index).list == null) {
                IN_PROGRESS.put(index, NEW.get(index));
                NEW.remove(index);
            } else {
                getTasks(index);
                System.out.println("Введите номер подзадачи");
                int indexST = scanner.nextInt();
                Epic o = (Epic) NEW.get(index);//Объект для работы с эпиком
                int j = o.list.size();//переменная для подсчёт
                if ((indexST > 0) && (indexST < j+1)) {
                    SubTask sT = (SubTask) o.list.get(indexST - 1);//Объект для работы с подзадачей
                    if (sT.status < 2) {
                        sT.status++;
                    } else System.out.println("Выбранная подзадача уже выполнена");

                    int i = 0;
                    for (SubTask st : o.list) {
                        i += st.status;
                    }
                    if ((i == 1) || (i == 2 * j)) {
                        updateEpic(NEW, j, i, o, index);
                    }
                } else System.out.println("Такой подзадачи в списке нет");
            }
        } else if(!(IN_PROGRESS.isEmpty()) && IN_PROGRESS.containsKey(index)){
            if (IN_PROGRESS.get(index).list == null){
                DONE.put(index, IN_PROGRESS.get(index));
                IN_PROGRESS.remove(index);
            } else {
                getTasks(index);
                System.out.println("Введите номер подзадачи");
                Epic o = (Epic) IN_PROGRESS.get(index);//Объект для работы с эпиком
                int indexST = scanner.nextInt();
                int j = o.list.size();//переменная для подсчёт
                if ((indexST > 0) && (indexST < j+1)) {
                    SubTask sT = (SubTask) o.list.get(indexST - 1);//Объект для работы с подзадачей
                    if (sT.status < 2) {
                        sT.status++;
                    } else System.out.println("Выбранная подзадача уже выполнена");

                    int i = 0;
                    for (SubTask st : o.list) {
                        i += st.status;
                    }
                    if ((i == 1) || (i == 2 * j)) {
                        updateEpic(IN_PROGRESS, j, i, o, index);
                    }
                } else System.out.println("Такой подзадачи в списке нет");
            }
        } else if(!(DONE.isEmpty()) && DONE.containsKey(index)) {
            System.out.println("Задача уже выполнена");
        } else System.out.println("Задачи под таким номером нет");
    }

    public void getTasks(int index){//Метод возвращает задачу по идентификатору
        if (!(NEW.isEmpty()) && NEW.containsKey(index)){
            System.out.println("Задача " + index + ": " + NEW.get(index).getName());
            System.out.println("Статус: новая");
            System.out.println("Описание: " + NEW.get(index).getDescription());
            if (NEW.get(index).list != null){
                Epic o = (Epic) NEW.get(index);
                int j = 1;
                for(SubTask st: o.list){
                    System.out.println("\tПодзадача " + j + ": " + st.getName() + " " + statusSubTask(st));
                    System.out.println("\tОписание: " + st.getName());
                    j++;
                }
            }
        } else if (!(IN_PROGRESS.isEmpty()) && IN_PROGRESS.containsKey(index)) {
            System.out.println("Задача " + index + ": " + IN_PROGRESS.get(index).getName());
            System.out.println("Статус: в процессе выполнения");
            System.out.println("Описание: " + IN_PROGRESS.get(index).getDescription());
            if (IN_PROGRESS.get(index).list != null) {
                Epic o = (Epic) IN_PROGRESS.get(index);
                int j = 1;
                for (SubTask st : o.list) {
                    System.out.println("\tПодзадача " + j + ": " + st.getName() + " " + statusSubTask(st));
                    System.out.println("\tОписание: " + st.getName());
                    j++;
                }
            }
        } else if (!(DONE.isEmpty()) && DONE.containsKey(index)) {
            System.out.println("Задача " + index + ": " + DONE.get(index).getName());
            System.out.println("Статус: новая");
            System.out.println("Описание: " + DONE.get(index).getDescription());
            if (DONE.get(index).list != null) {
                Epic o = (Epic) DONE.get(index);
                int j = 1;
                for (SubTask st : o.list) {
                    System.out.println("\tПодзадача " + j + ": " + st.getName() + " " + statusSubTask(st));
                    System.out.println("\tОписание: " + st.getName());
                    j++;
                }
            }
        } else System.out.println("Задачи с таким номером в списке нет");
    }//Метод возвращает задачу по идентификатору

    public void removeAllTasks(){//Удаление всех задач
        NEW.clear();
        IN_PROGRESS.clear();
        DONE.clear();
    }//Удаление всех задач

    public void removeTask(int i){
        if (!(NEW.isEmpty()) && NEW.containsKey(i)){
            NEW.remove(i);
            System.out.println("Задача " + i + " удалена");
        } else if (!(IN_PROGRESS.isEmpty()) && IN_PROGRESS.containsKey(i)){
            IN_PROGRESS.remove(i);
            System.out.println("Задача " + i + " удалена");
        } else if (!(DONE.isEmpty()) && DONE.containsKey(i)){
            DONE.remove(i);
            System.out.println("Задача " + i + " удалена");
        } else System.out.println("Задачи с номером " + i + " в списке задач нет");
    }

    public void returnAllTasks(HashMap map){//Вывод всех задач
        if (!(map.isEmpty())){
            for (int i = 0; i < index; i++){
                if (map.containsKey(i)){
                    Epic o = (Epic) map.get(i);
                    System.out.println("Задача " + i + ": " + o.getName());
                    if (o.list != null) {
                        int j = 1;
                        for(SubTask st: o.list){
                            System.out.println("\tПодзадача " + j + ": " + st.getName() + " " + statusSubTask(st));
                            j++;
                        }
                    }
                }
            }
        }
    }//Вывод всех задач

    public static void print(){
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Ввести новую задачу");
        System.out.println("2 - Получение списка всех задач");
        System.out.println("3 - Удаление всех задач");
        System.out.println("4 - Получение задачи по номеру");
        System.out.println("5 - Обновить статус задачи");
        System.out.println("6 - Удалить задачу");
        System.out.println("0 - Завершить программу");

    }

    public void addEpicNew(){//Добавление новой задачи
        Scanner scanner = new Scanner(System.in);
        Epic epic = new Epic();
        epic.add();
        boolean tr = true;
        String yn = "";
        while (!(yn.equals("y") || yn.equals("n"))) {
            System.out.println("Добавить подзадачу? y - да, n - нет");
            yn = scanner.nextLine();
            if (yn.equals("y") || yn.equals("n")) {
                switch (yn) {
                    case "y": epic.addSubTask();
                        break;
                    case "n":
                        tr = false;
                        break;
                    default:
                }
            } else
                System.out.println("Такой команды пока нет");

        }
        NEW.put(index, epic);
        index++;
    }//Добавление новой задачи

}
