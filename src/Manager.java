import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private long index = 1;
    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Task> epic = new HashMap<>();
    private HashMap<Long, Task> subTasks = new HashMap<>();

    public Task getTasks(long index){//Метод возвращает задачу по идентификатору
        if (!(tasks.isEmpty()) && tasks.containsKey(index)){
            return tasks.get(index);
        } else if (!(epic.isEmpty()) && epic.containsKey(index)) {
            return epic.get(index);
        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(index)) {
            return subTasks.get(index);
        } else return null;
    }//Метод возвращает задачу по идентификатору

    public void removeTask(Long i){//Удаляет задачу по идентификатору
        if (!(tasks.isEmpty()) && tasks.containsKey(i)){
            tasks.remove(i);
        } else if (!(epic.isEmpty()) && epic.containsKey(i)){
            epic.remove(i);
        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(i)){
            subTasks.remove(i);
        }
    }//Удаляет задачу по идентификатору

    public void updateTasks(Task o){//Метод для обновления задачи
        long id = o.getId();
        if (!(tasks.isEmpty()) && tasks.containsKey(id)) {
            tasks.remove(id);
            tasks.put(id, o);
        } else if(!(epic.isEmpty()) && epic.containsKey(id)){
            Epic obj = (Epic) o;
            if (obj.getListSubTask().size() == 0 ){
                epic.remove(id);
                epic.put(id, o);
            } else {
                updateEpicStatus(obj);
                epic.remove(id);
                epic.put(id, o);
            }
        } else if(!(subTasks.isEmpty()) && subTasks.containsKey(id)) {
            SubTask obj = (SubTask) o;
            subTasks.remove(id);
            subTasks.put(id, obj);
            updateEpicStatus((Epic) epic.get(obj.getEpicId()));
        }
    }//Метод для обновления задачи

    public void removeAllTasks(){//Удаление всех задач
        tasks.clear();
        epic.clear();
        subTasks.clear();
    }//Удаление всех задач

    public ArrayList<Task> returnAllTasks(){//Вывод всех задач
        ArrayList<Task> ls = new ArrayList<>();
        for (long i = 1; i < index; i++){
            if (!(tasks.isEmpty()) && tasks.containsKey(i)) {
                ls.add(tasks.get(i));
            } else if(!(epic.isEmpty()) && epic.containsKey(i)){
                ls.add(epic.get(i));
            } else if(!(subTasks.isEmpty()) && subTasks.containsKey(i)) {
                ls.add(subTasks.get(i));
            }
        }
        return ls;
    }//Вывод всех задач

    public void updateEpicStatus(Epic o){//метод актуализирует поле-статаус эпика на основе подзадач

        boolean checkStatus = false;//дополнительная переменная для определения статуса эпика
        for (Long i: o.getListSubTask()){
            SubTask sT = (SubTask) subTasks.get(i);
            if (!sT.getStatus().equals(subTasks.get(o.getListSubTask().get(0)).getStatus())) checkStatus = true;
        }
        if (checkStatus) {
            o.setStatus(Status.IN_PROGRESS);
        } else {
            for (Long i: o.getListSubTask()){
                SubTask sT = (SubTask) subTasks.get(i);
                if (!Status.NEW.equals(sT.getStatus())) checkStatus = true;
            }
            if (!checkStatus) {
                o.setStatus(Status.NEW);
            } else {
                o.setStatus(Status.DONE);
            }
        }
    }//ВСПОМОГАТЕЛЬНЫЙ метод актуализирует поле-статаус эпика на основе подзадач

    public void addNewSubTask(SubTask obj){//Добавление новой подзадачи
        SubTask sT = (SubTask) obj;
        Epic e = (Epic) epic.get(sT.getEpicId());
        subTasks.put(index, sT);
        sT.setId(index);
        e.getListSubTask().add(sT.getId());
        generateId(index);
        updateEpicStatus(e);
    }//Добавление новой подзадачи

    public void addNewTask(Task obj){//Добавление новой задачи
        Task o = (Task) obj;
        tasks.put(index, o);
        o.setId(index);
        generateId(index);
    }//Добавление новой задачи

    public void addNewEpic(Epic obj){//Добавление нового эпика
        Epic o = (Epic) obj;
        epic.put(index, o);
        o.setId(index);
        generateId(index);
    }//Добавление нового эпика

    private void generateId(long index) {
        setIndex(++index);
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
