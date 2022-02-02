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
                case 0:
                    System.out.println("Программа успешно завершила работу");
                    System.exit(0);
            default:
                System.out.println("Такой команды пока нет");
            }
        }
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
                    System.out.println("\tПодзадача " + j + ": " + st.getName());
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
                    System.out.println("\tПодзадача " + j + ": " + st.getName());
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
                    System.out.println("\tПодзадача " + j + ": " + st.getName());
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

    public void returnAllTasks(HashMap map){//Вывод всех задач
        if (!(map.isEmpty())){
            for (int i = 0; i < index; i++){
                if (map.containsKey(i)){
                    Epic o = (Epic) map.get(i);
                    System.out.println("Задача " + i + ": " + o.getName());
                    if (o.list != null) {
                        int j = 1;
                        for(SubTask st: o.list){
                            System.out.println("\tПодзадача " + j + ": " + st.getName());
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
