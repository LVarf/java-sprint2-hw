import java.util.HashMap;
import java.util.Scanner;
/*
Приветствую тебя, дорогой друг!
Так уж вышло, что последнюю неделю я болел, и обучение давалось мне с трудом, поэтому можно сказать, что
программу писал в последний день спринта. Именно по этой причине код продуман плохо и решения задач топорные, но всё,
вроде бы, работает))
Некоторые условия ТЗ (то, что касается изменения статуса), как мне кажется, понял неверно, прогу написал как понял.
Жду обратную связь.
Будет приятно, если напишешь в слаке, чтобы обсудить недочёты онлайн.

С уважением, Леонид Варфоломеев
lpvarfolomeev@yandex.ru
*/

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
