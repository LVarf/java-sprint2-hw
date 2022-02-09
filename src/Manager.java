import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int index = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Task> epic = new HashMap<>();
    private HashMap<Integer, Task> subTasks = new HashMap<>();

    public Task getTasks(int index){//Метод возвращает задачу по идентификатору
        if (!(tasks.isEmpty()) && tasks.containsKey(index)){
            return tasks.get(index);
        } else if (!(epic.isEmpty()) && epic.containsKey(index)) {
            return epic.get(index);
        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(index)) {
            return subTasks.get(index);
        } else return null;
    }//Метод возвращает задачу по идентификатору

    public void removeTask(Integer i){//Удаляет задачу по идентификатору
        if (!(tasks.isEmpty()) && tasks.containsKey(i)){
            tasks.remove(i);
        } else if (!(epic.isEmpty()) && epic.containsKey(i)){
            epic.remove(i);
        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(i)){
            subTasks.remove(i);
        }
    }//Удаляет задачу по идентификатору

    public void updateTasks(Task o){//Метод для обновления задачи
        int id = o.getId();
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

    public ArrayList returnAllTasks(){//Вывод всех задач
        ArrayList<Task> ls = new ArrayList<>();
        for (int i = 1; i < index; i++){
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
        for (Integer i: o.getListSubTask()){
            SubTask sT = (SubTask) subTasks.get(i);
            if (!sT.getStatus().equals(subTasks.get(o.getListSubTask().get(0)).getStatus())) checkStatus = true;
        }
        if (checkStatus) {
            o.setStatus(Status.IN_PROGRESS);
        } else {
            for (Integer i: o.getListSubTask()){
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
        index++;
        updateEpicStatus(e);
    }//Добавление новой подзадачи

    public void addNewTask(Task obj){//Добавление новой задачи
        Task o = (Task) obj;
        tasks.put(index, o);
        o.setId(index);
        index++;
    }//Добавление новой задачи

    public void addNewEpic(Epic obj){//Добавление нового эпика
        Epic o = (Epic) obj;
        epic.put(index, o);
        o.setId(index);
        index++;
    }//Добавление нового эпика
}
