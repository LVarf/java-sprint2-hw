package taskManager;

import main.enums.Status;
import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utility.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected long index = 0;
    protected final HashMap<Long, Task> tasks = new HashMap<>();
    protected final HashMap<Long, Task> epics = new HashMap<>();
    protected final HashMap<Long, Task> subTasks = new HashMap<>();
    private final HistoryManager inMemoryHistoryManager;

    public InMemoryTaskManager() {
        this.inMemoryHistoryManager = Managers.getHistoryManager();
    }

    @Override
    public Task getTasks(long id){//Метод возвращает задачу по id
        if (!(tasks.isEmpty()) && tasks.containsKey(id)){
            return getTask(id);
        } else if (!(epics.isEmpty()) && epics.containsKey(id)) {
            return getEpic(id);
        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(id)) {
            return getSubTusk(id);
        } else return null;
    }//Метод возвращает задачу по идентификатору

    @Override
    public void removeTask(Long i){//Удаляет задачу по идентификатору
        InMemoryHistoryManager memory = (InMemoryHistoryManager) inMemoryHistoryManager;
        if (!(tasks.isEmpty()) && tasks.containsKey(i)){
            tasks.remove(i);
            if(memory.getNodeMap().containsKey(i))
            inMemoryHistoryManager.remove(i);

        } else if (!(epics.isEmpty()) && epics.containsKey(i)){
            Epic eT = (Epic) epics.get(i);
            if (!eT.getListSubTask().isEmpty()) {
                for (Long sT: eT.getListSubTask()){
                    if(memory.getNodeMap().containsKey(sT))
                        inMemoryHistoryManager.remove(sT);
                    subTasks.remove(sT);
                }
            } else
            if(memory.getNodeMap().containsKey(i))
                inMemoryHistoryManager.remove(i);
            epics.remove(i);

        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(i)){
            if(memory.getNodeMap().containsKey(i))
                inMemoryHistoryManager.remove(i);
            subTasks.remove(i);
        }
    }//Удаляет задачу по идентификатору

    @Override
    public ArrayList<Task> returnAllTasks(){//Вывод всех задач
        ArrayList<Task> ls = new ArrayList<>();
        for (long i = 1; i < index + 1; i++) {
            if (!(tasks.isEmpty()) && tasks.containsKey(i)) {
                ls.add(getTask(i));
            } else if (!(epics.isEmpty()) && epics.containsKey(i)) {
                ls.add(getEpic(i));
            } else if (!(subTasks.isEmpty()) && subTasks.containsKey(i)) {
                ls.add(getSubTusk(i));
            }
        }
        return ls;
    }//Вывод всех задач

    @Override
    public List<Task> getHistory() {
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
        Epic e = (Epic) epics.get(id);
        inMemoryHistoryManager.add(e);
        return e;
    }//Метод возвращает объект по id

    @Override
    public void updateTasks(Task o){//Метод для обновления задачи
        long id = o.getId();
        if (!(tasks.isEmpty()) && tasks.containsKey(id)) {
            tasks.remove(id);
            tasks.put(id, o);
        } else if(!(epics.isEmpty()) && epics.containsKey(id)){
            Epic obj = (Epic) o;
            if (obj.getListSubTask().size() == 0 ){
                epics.remove(id);
                epics.put(id, o);
            } else {
                updateEpicStatus(obj);
                epics.remove(id);
                epics.put(id, o);
            }
        } else if(!(subTasks.isEmpty()) && subTasks.containsKey(id)) {
            SubTask obj = (SubTask) o;
            subTasks.remove(id);
            subTasks.put(id, obj);
            updateEpicStatus((Epic) epics.get(obj.getEpicId()));
        }
    }//Метод для обновления задачи

    @Override
    public void removeAllTasks(){//Удаление всех задач
        InMemoryHistoryManager memory = (InMemoryHistoryManager) inMemoryHistoryManager;

        for (long ts: tasks.keySet()){
            if(memory.getNodeMap().containsKey(ts))
                inMemoryHistoryManager.remove(ts);
        }
        tasks.clear();

        for (long ts: epics.keySet()){
            if(memory.getNodeMap().containsKey(ts))
                inMemoryHistoryManager.remove(ts);
        }
        epics.clear();

        for (long ts: subTasks.keySet()) {
            if (memory.getNodeMap().containsKey(ts))
                inMemoryHistoryManager.remove(ts);
        }
        subTasks.clear();

        setIndex(0L);
    }//Удаление всех задач

    private void updateEpicStatus(Epic o){//метод актуализирует поле-статаус эпика на основе подзадач

        boolean checkStatus = false;//дополнительная переменная для определения статуса эпика

        if(o.getListSubTask().size() == 1) {
            o.setStatus(subTasks.get(o.getListSubTask().get(0)).getStatus());
        } else {
            checkStatus = !o.getListSubTask().stream()
                    .map(subTasks::get)
                    .map(Task::getStatus)
                    .allMatch(status -> subTasks.get(o.getListSubTask().get(0)).getStatus().equals(status));

            if (!checkStatus) {
                o.setStatus(subTasks.get(o.getListSubTask().get(0)).getStatus());
            } else {
                o.setStatus(Status.IN_PROGRESS);
            }
        }
    }//ВСПОМОГАТЕЛЬНЫЙ метод актуализирует поле-статаус эпика на основе подзадач

    @Override
    public void addNewSubTask(SubTask sT){//Добавление новой подзадачи
        Epic e = (Epic) epics.get(sT.getEpicId());//вернуть epic по id из subTask
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
        epics.put(index, o);
    }//Добавление нового эпика

    private long generateId() {
        return ++index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public HashMap<Long, Task> getTasks() {
        return tasks;
    }

    public HashMap<Long, Task> getEpics() {
        return epics;
    }

    public HashMap<Long, Task> getSubTasks() {
        return subTasks;
    }

    public long getIndex() {
        return index;
    }
}
