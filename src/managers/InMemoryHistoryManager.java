package managers;

import tasks.Node;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> mapHistory = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        removeNode(id);
        linkLast(task);
        mapHistory.put(id, head);
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> historyList = new ArrayList<>();
        for (Integer id : mapHistory.keySet()) {
            historyList.add(mapHistory.get(id).task);
        }
        return historyList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private  void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
    }

    private void removeNode(int id) {
        Node node = mapHistory.remove(id);
        if (tail == head) {
            tail.prev = null;
            head.next = null;
        }
        if (node == tail) {
            tail = node.next;
            tail.prev = null;
        } else if (node == head) {
              head = node.prev;
              head.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
}
