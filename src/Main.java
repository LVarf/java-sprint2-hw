import java.util.HashMap;
import java.util.Scanner;

public class Main {

    HashMap<Integer, Task> NEW;
    HashMap<Integer, Task> IN_PROGRESS;
    HashMap<Integer, Task> DONE;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        manager.start();
    }
}
