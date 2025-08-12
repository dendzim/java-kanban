package managers;

import tasks.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> testHistory = new HashMap<>();

    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        linkLast(task);
        if (testHistory.containsKey(task.getId())) {

        }

    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> historyList = new ArrayList<>(testHistory)
        return copyHistory;
    }

    @Override
    public void remove(int id) {
        testHistory.remove(id);
    }

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
    }

    public void removeNode(Node node) {

    }
}
