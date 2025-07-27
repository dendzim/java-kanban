package managers;

import tasks.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private LinkedList<Task> history = new LinkedList<>();

    public static final int MAX_HISTORY_COUNT = 10;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        history.add(new Task(task));
        if (history.size() > MAX_HISTORY_COUNT) {
            history.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        LinkedList<Task> copyHistory = new LinkedList<>();
        copyHistory.addAll(history);
        return copyHistory;
    }
}
