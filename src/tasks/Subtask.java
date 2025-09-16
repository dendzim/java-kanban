package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, int epicId, TaskStatus status) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int epicId, TaskStatus status,
                   Duration duration, LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" + "title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
