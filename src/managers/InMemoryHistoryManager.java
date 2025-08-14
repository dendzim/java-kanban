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
        mapHistory.put(id, linkLast(task));
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node copyHead = head;
        while (copyHead != null) { //идем по копии головы пока не пустая
            historyList.add(copyHead.task);
            copyHead = copyHead.next; //копия головы ссылается теперь на соседа справа
        }
        return historyList;
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private Node linkLast(Task task) {
        final Node oldTail = tail;
        Node newNode = new Node(oldTail, task, null); //создаем узел
        if (oldTail == null) { //если соседа слева нет
            head = newNode; //голова новый узел
        } else {
            oldTail.next = newNode; // у соседа слева делаем ссылку на новый узел
        }
        tail = newNode; //теперь хвост ссылается на новый узел
        return tail;
    }

    private void removeNode(int id) {
        if (mapHistory.containsKey(id)) { //если история содержит элемент с id нужно для удаления
            final Node node = mapHistory.remove(id);
            if (node == null) { //Если узлов не было
                return;
            }
            if (node.prev != null) { //Если есть узел слева
                node.prev.next = node.next; //переписываем у соседа слева ссылку на соседа справа
                if (node.next == null) { //но если соседа справа нет
                    tail = node.prev; //хвост списка сосед слева
                } else {
                    node.next.prev = node.prev; //сосед справа хвостом ссылается на соседа слева
                }
            } else { //Если это первый узел
                head = node.next; //голова ссылается на соседа справа
                if (head == null) { //
                    tail = null;
                } else {
                    head.prev = null;
                }
            }
        } else {
            return;
        }
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    @Override
    public void deleteHistory() { //для тестов
        mapHistory.clear();
        tail = null;
        head = null;
    }
}
