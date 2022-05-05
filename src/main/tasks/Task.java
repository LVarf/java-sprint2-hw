package tasks;

import main.enums.Status;
import main.enums.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;//описание задачи
    private String description; //описание задачи
    private Status status; //статус задачи
    private Long id;//id задачи
    protected TaskTypes taskTypes;
    protected Duration duration;
    protected LocalDateTime startTime;



    public Task() {
        this.status = Status.NEW;
        this.taskTypes = TaskTypes.TASK;
    }

    public LocalDateTime getEndTime() {
        if (duration != null)
        return startTime.plus(duration);
        return null;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public TaskTypes getTaskTypes() {
        return this.taskTypes;
    }

    public void setTaskTypes(TaskTypes taskTypes) {
        if(taskTypes.equals(TaskTypes.TASK))
            this.taskTypes = TaskTypes.TASK;
        if(taskTypes.equals(TaskTypes.EPIC))
            this.taskTypes = TaskTypes.EPIC;
        if(taskTypes.equals(TaskTypes.SUBTASK))
            this.taskTypes = TaskTypes.SUBTASK;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(taskTypes, task.taskTypes) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (name != null) {
            hash += name.hashCode();
        }
        if (id != 0) {
            hash += id*19;
        }
        hash *= 31;

        if (description != null) {
            hash += description.hashCode();
        }
        return hash;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
