import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Epic extends Task {
    ArrayList<SubTask> list;

    public Epic() {

    }

    public void addSubTask(){
        Scanner scanner = new Scanner(System.in);
        list = new ArrayList<>();
        boolean tr = true;

        while (tr) {
            SubTask st = new SubTask();
            list.add(st);
            String yn = "";
            while (!(yn.equals("y") || yn.equals("n"))) {
                System.out.println("Хотите ввести ещё одну задачу? y - да, n - нет");
                yn = scanner.nextLine();
                if (yn.equals("y") || yn.equals("n")) {
                    switch (yn) {
                        case "y":
                            break;
                        case "n":
                            tr = false;
                            break;
                        default:
                    }
                } else
                    System.out.println("Такой команды пока нет");

            }
        }
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public boolean equals(Object o) {
        Epic epic = (Epic) o;
        return super.equals(o) && Objects.equals(list, epic.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), list);
    }

}
