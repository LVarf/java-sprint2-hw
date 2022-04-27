package tasks;

import enums.TaskTypes;

import java.util.Objects;

public class SubTask extends Task {
    private Long epicId;//id эпика, к которому относится объект

    public SubTask() {
        super();
        this.taskTypes = TaskTypes.SUBTASK;
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        SubTask subTask = (SubTask) o;
        return super.equals(subTask) && Objects.equals(epicId, subTask.epicId);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
