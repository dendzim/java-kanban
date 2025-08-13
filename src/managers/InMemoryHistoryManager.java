package managers;

import tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        public Task task;
        public Node next;
        public Node prev;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task +
                    ", next=" + next + (next == null ? null : next.task) +
                    ", prev=" + prev + (prev == null ? null : prev.task) +
                    '}';
        }
    }

    private final HashMap<Integer, Node> mapHistory = new HashMap<>();
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

    private void linkLast(Task task) {
        head = new Node(tail, task, head);
    }

    private void removeNode(int id) {
        final Node node = mapHistory.remove(id);
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                head = node.prev;
                head.next = null;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            tail = node.next;
            if (tail == null) {
                head = null;
            } else {
                tail.prev = null;
            }
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }
}
