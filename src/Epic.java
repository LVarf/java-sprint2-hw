import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<SubTask> list;



    public void setList(ArrayList<SubTask> list) {
        this.list = list;
    }

    public ArrayList<SubTask> getList() {
        return list;
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
        return super.equals(o) && Objects.equals(list, epic.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), list);
    }
}
