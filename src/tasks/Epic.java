package tasks;

public class Epic extends Task {
    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }
}