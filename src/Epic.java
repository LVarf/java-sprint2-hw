import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> listSubTask;

    public Epic() {
        super();
        this.listSubTask = new ArrayList<>();
    }

    public void setListSubTask(ArrayList<Integer> listSubTask) {
        this.listSubTask = listSubTask;
    }

    public ArrayList<Integer> getListSubTask() {
        return listSubTask;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public boolean equals(Object o) {
        Epic epic = (Epic) o;
        return super.equals(o) && Objects.equals(listSubTask, epic.listSubTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listSubTask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId()+
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                "listSubTask=" + listSubTask +
                '}';
    }
}
