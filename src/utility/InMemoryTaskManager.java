package utility;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{

    private static final int SIZE_OF_HISTORY_LIST = 10;
    private long index = 1;
    private HashMap<Long, Task> tasks = new HashMap<>();
    private HashMap<Long, Task> epic = new HashMap<>();
    private HashMap<Long, Task> subTasks = new HashMap<>();
    private List<Task>  historyList= new ArrayList<>();

    @Override
    public Task getTasks(long id){//Метод возвращает задачу по идентификатору
        if (!(tasks.isEmpty()) && tasks.containsKey(id)){
            return getTusk(id);
        } else if (!(epic.isEmpty()) && epic.containsKey(id)) {
            return getEpic(id);
        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(id)) {
            return getEpic(id);
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
        for (long i = 1; i < index; i++){
            if (!(tasks.isEmpty()) && tasks.containsKey(i)) {
                ls.add(getTusk(i));
            } else if(!(epic.isEmpty()) && epic.containsKey(i)){
                ls.add(getEpic(i));
            } else if(!(subTasks.isEmpty()) && subTasks.containsKey(i)) {
                ls.add(getSubTusk(i));
            }
        }
        return ls;
    }//Вывод всех задач

    @Override
    public List<Task> history() {
        return historyList;
    }//Метод возвращает список истории

    private void checkSizeOfHistoryListByTen() {
        if (historyList.size() >= SIZE_OF_HISTORY_LIST) {
            historyList.remove(0);
        }
    }//Вспомогательный метод проверяет размер списка истории

    private Task getTusk(long id) {
        Task task = (Task) tasks.get(id);
        checkSizeOfHistoryListByTen();
        historyList.add(task);
        return task;
    }//Метод возвращает объект по id, проверяет размер списка истории и добавляет историю

    private SubTask getSubTusk(long id) {
        SubTask sT = (SubTask) subTasks.get(id);
        checkSizeOfHistoryListByTen();
        historyList.add(sT);
        return sT;
    }//Метод возвращает объект по id, проверяет размер списка истории и добавляет историю

    private Epic getEpic(long id) {
        Epic e = (Epic) epic.get(id);
        checkSizeOfHistoryListByTen();
        historyList.add(e);
        return e;
    }//Метод возвращает объект по id, проверяет размер списка истории и добавляет историю

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

    public void updateEpicStatus(Epic o){//метод актуализирует поле-статаус эпика на основе подзадач

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
    public void addNewSubTask(SubTask obj){//Добавление новой подзадачи
        SubTask sT = (SubTask) obj;
        Epic e = (Epic) epic.get(sT.getEpicId());
        sT.setId(index);
        subTasks.put(index, sT);
        e.getListSubTask().add(sT.getId());
        updateEpicStatus(e);
        generateId(index);
    }//Добавление новой подзадачи

    @Override
    public void addNewTask(Task obj){//Добавление новой задачи
        Task o = (Task) obj;
        tasks.put(index, o);
        o.setId(index);
        generateId(index);
    }//Добавление новой задачи

    @Override
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
