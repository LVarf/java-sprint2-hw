package utitlityHistories;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    Map<Long, Node> nodeMap;

    Node<Task> head;
    Node<Task> tail;
    int size = 0;

    private HistoryLinkedList<Task> historyList;

    public InMemoryHistoryManager() {
        historyList = new HistoryLinkedList<>();
        nodeMap = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTask(historyList);
    }


    @Override
    public boolean remove(long id) {
        if (nodeMap.containsKey(id)) {
            historyList.remove(nodeMap.get(id).item);
            removeNode(nodeMap.get(id));
            nodeMap.remove(id);
            return true;
        }

            return false;
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        historyList.addLast(task);
        nodeMap.put(task.getId(), addTail(task));
    }

    Node addTail(Task task) {
        Node oldTail = tail;
        tail = new Node(oldTail, task, null);

        if (oldTail != null) {
            oldTail.next = tail;
        } else
            head = tail;
        size++;
        return tail;
    }

    boolean removeNode(Node node) {
        if (head == null)
            return false;
        if (head.next == null) {
            head = null;
            tail = null;
            size--;
            return true;
        }
        if (node.prev != null)
            node.prev.next = node.next;
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        node.item = null;
        size--;
        return true;
    }


    private class Node<E extends Task> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

}
