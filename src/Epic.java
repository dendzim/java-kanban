import java.util.HashMap;

public class Epic extends  Task {
    HashMap<Integer, Subtask> subtaskList = new HashMap<>();;

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtaskList=" + subtaskList +
                '}';
    }
}
