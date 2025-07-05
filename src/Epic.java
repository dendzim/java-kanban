import java.util.HashMap;

public class Epic extends  Task {
    HashMap<Integer, Subtask> subtaskList = new HashMap<>();;


    public Epic(String title, String description, int id, TaskStatus status) {
        super(title, description, id, status);
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

    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    }


}
