package utilityTasks;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utitlityHistories.HistoryManager;
import utility.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{

    private long index = 0;
    private final HashMap<Long, Task> tasks = new HashMap<>();
    private final HashMap<Long, Task> epic = new HashMap<>();
    private final HashMap<Long, Task> subTasks = new HashMap<>();
    private final HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        this.inMemoryHistoryManager = Managers.getHistoryManager();
    }

    @Override
    public Task getTasks(long id){//Метод возвращает задачу по id
        if (!(tasks.isEmpty()) && tasks.containsKey(id)){
            return getTask(id);
        } else if (!(epic.isEmpty()) && epic.containsKey(id)) {
            return getEpic(id);
        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(id)) {
            return getSubTusk(id);
        } else return null;
    }//Метод возвращает задачу по идентификатору

    @Override
    public void removeTask(Long i){//Удаляет задачу по идентификатору
        if (!(tasks.isEmpty()) && tasks.containsKey(i)){
            tasks.remove(i);
        } else if (!(epic.isEmpty()) && epic.containsKey(i)){
            epic.remove(i);
        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(i)){
            subTasks.remove(i);
        }
    }//Удаляет задачу по идентификатору

    @Override
    public ArrayList<Task> returnAllTasks(){//Вывод всех задач
        ArrayList<Task> ls = new ArrayList<>();
        for (long i = 1; i < index + 1; i++) {
            if (!(tasks.isEmpty()) && tasks.containsKey(i)) {
                ls.add(getTask(i));
            } else if (!(epic.isEmpty()) && epic.containsKey(i)) {
                ls.add(getEpic(i));
            } else if (!(subTasks.isEmpty()) && subTasks.containsKey(i)) {
                ls.add(getSubTusk(i));
            }
        }
        return ls;
    }//Вывод всех задач

    @Override
    public List<Task> history() {
        return inMemoryHistoryManager.getHistory();
    }//Метод возвращает список истории

    private Task getTask(long id) {
        Task task = tasks.get(id);
        inMemoryHistoryManager.add(task);
        return task;
    }//Метод возвращает объект по id

    private SubTask getSubTusk(long id) {
        SubTask sT = (SubTask) subTasks.get(id);
        inMemoryHistoryManager.add(sT);
        return sT;
    }//Метод возвращает объект по id

    private Epic getEpic(long id) {
        Epic e = (Epic) epic.get(id);
        inMemoryHistoryManager.add(e);
        return e;
    }//Метод возвращает объект по id

    @Override
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

    @Override
    public void removeAllTasks(){//Удаление всех задач
        tasks.clear();
        epic.clear();
        subTasks.clear();
    }//Удаление всех задач

    private void updateEpicStatus(Epic o){//метод актуализирует поле-статаус эпика на основе подзадач

        boolean checkStatus = false;//дополнительная переменная для определения статуса эпика
        for (Long i: o.getListSubTask()){
            SubTask sT = (SubTask) subTasks.get(i);
            if (!sT.getStatus().equals(subTasks.get(o.getListSubTask().get(0)).getStatus())) checkStatus = true;
        }
        if (checkStatus && (o.getListSubTask().size() > 1)) {
            o.setStatus(Status.IN_PROGRESS);
        } else if (o.getListSubTask().size() > 1){
            for (Long i: o.getListSubTask()){
                SubTask sT = (SubTask) subTasks.get(i);
                if (!Status.NEW.equals(sT.getStatus())) checkStatus = true;
            }
            if (!checkStatus) {
                o.setStatus(Status.NEW);
            } else {
                o.setStatus(Status.DONE);
            }
        } else if (o.getListSubTask().size() == 1) {
            SubTask sT = (SubTask) subTasks.get(o.getListSubTask().get(0));
            o.setStatus(sT.getStatus());
        }
    }//ВСПОМОГАТЕЛЬНЫЙ метод актуализирует поле-статаус эпика на основе подзадач

    @Override
    public void addNewSubTask(SubTask sT){//Добавление новой подзадачи
        Epic e = (Epic) epic.get(sT.getEpicId());//вернуть epic по id из subTask
        sT.setId(generateId());//присвоить subTask свой id
        subTasks.put(index, sT);//записать subTask в свою таблицу
        e.getListSubTask().add(sT.getId());//добавить id subTask в поле-список эпика
        updateEpicStatus(e);//обновить поле-статус эпика
    }//Добавление новой подзадачи

    @Override
    public void addNewTask(Task o){//Добавление новой задачи
        o.setId(generateId());
        tasks.put(index, o);
    }//Добавление новой задачи

    @Override
    public void addNewEpic(Epic o){//Добавление нового эпика
        o.setId(generateId());
        epic.put(index, o);
    }//Добавление нового эпика

    private long generateId() {
        return ++index;
    }

}
