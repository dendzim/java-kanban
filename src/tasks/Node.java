package tasks;

public class Node {
    public Task task;
    public Node next;
    public Node prev;

    public Node(Node prev, Task task, Node next) {
        this.task = task;
        this.next = null;
        this.prev = null;
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
