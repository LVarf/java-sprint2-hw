package utitlityHistories;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;

public class HistoryLinkedList<T extends Task> extends LinkedList<T> {

    @Override
    public void addLast(T task) {
       super.addLast(task);
    }



   public ArrayList<Task> getTask(HistoryLinkedList list) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Object task: list) {
            tasks.add((Task) task);
        }
        return tasks;
    }


}
