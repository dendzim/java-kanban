package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {
    private LocalDateTime endTime;

    public Epic(String title, String description, TaskStatus status) {
        super(title, description, status);
    }

    public Epic(String title, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        super(title, description, status, duration, startTime);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}