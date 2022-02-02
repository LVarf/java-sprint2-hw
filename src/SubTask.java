import java.util.Scanner;

public class SubTask extends Task{
    public SubTask() {
        add();
    }

    @Override
    public void add() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название подзадачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание подзадачи");
        String description = scanner.nextLine();
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }
}
