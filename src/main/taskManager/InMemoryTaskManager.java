package taskManager;

import main.enums.Status;
import historyManager.HistoryManager;
import historyManager.InMemoryHistoryManager;
import taskException.TimeTaskConflictException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import utility.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected long index = 0;
    protected final HashMap<Long, Task> tasks = new HashMap<>();
    protected final HashMap<Long, Task> epics = new HashMap<>();
    protected final HashMap<Long, Task> subTasks = new HashMap<>();
    protected final HistoryManager inMemoryHistoryManager;
    protected final Set<Task> prioritisedSet;

    public InMemoryTaskManager() {
        this.inMemoryHistoryManager = Managers.getHistoryManager();
        prioritisedSet = new TreeSet<>((o1, o2) -> {
            if (o1.getStartTime() == null)
                return 1;//здесь можно добавить сортировку по id
            if (o2.getStartTime() == null)
                return -1;
            if (o1.getStartTime().equals(o2.getStartTime()))
                if (o1 instanceof SubTask && o2 instanceof Epic)
                    return 1;
                else
                    return 0;
            if (o2.getStartTime() != null) {
                return o1.getStartTime().isAfter(o2.getStartTime()) ? 1 : -1;
            }
            return 0;
        });
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
            }
            if(memory.getNodeMap().containsKey(i))
                inMemoryHistoryManager.remove(i);
            epics.remove(i);

        } else if (!(subTasks.isEmpty()) && subTasks.containsKey(i)){
            if(memory.getNodeMap().containsKey(i))
                inMemoryHistoryManager.remove(i);
            SubTask subTask = (SubTask) subTasks.get(i);
            Epic epic = (Epic) epics.get(subTask.getEpicId());
            subTasks.remove(i);
            updateEpicStatus(epic);
            getTimeEpic(epic);
        }
    }//Удаляет задачу по идентификатору

    @Override
    public ArrayList<Task> getAllTasks(){//Вывод всех задач
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
    public Set<Task> getPrioritizedTasks(){//Вывод всех задач
        return prioritisedSet;
    }

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
            prioritisedSet.remove(o);
            if(checkingTimeConflict(o)) {
                tasks.remove(id);
                prioritisedSet.add(o);
                tasks.put(id, o);
            }
            //дописать обновление в TreeSet с проверкой на пересечение
        } else if(!(epics.isEmpty()) && epics.containsKey(id)){
            Epic epic = (Epic) o;
            if (epic.getListSubTask().size() == 0 ){
                prioritisedSet.remove(o);
                if(checkingTimeConflict(o)) {
                    epics.remove(id);
                    epics.put(id, o);
                    prioritisedSet.add(o);
                }
            } else {
                updateEpicStatus(epic);
                prioritisedSet.remove(o);
                if(checkingTimeConflict(o)) {
                    epics.remove(id);
                    epics.put(id, o);
                    prioritisedSet.add(o);
                }
            }
        } else if(!(subTasks.isEmpty()) && subTasks.containsKey(id)) {
            SubTask sT = (SubTask) o;
            prioritisedSet.remove(o);
            if(checkingTimeConflict(o)) {
                subTasks.remove(id);
                subTasks.put(id, sT);
                prioritisedSet.add(o);
                Epic epic = (Epic) epics.get(sT.getEpicId());
                updateEpicStatus(epic);
                getTimeEpic(epic);
            }
        }
    }//Метод для обновления задачи

    private boolean checkingTimeConflict (Task newTask) {
        boolean isNoHaveMistake = false;
        try {
            if (newTask.getStartTime() == null)
                return true;

            for (Task task : prioritisedSet) {

                if (task.getStartTime() == null)
                    return true;
                if (!(newTask.getStartTime().isAfter(task.getEndTime())
                        || newTask.getEndTime().isBefore(task.getStartTime()))
                        && !newTask.equals(task)) {
                    if (!(newTask instanceof SubTask)) {
                        isNoHaveMistake = false;
                        break;
                    } else if (!(task instanceof Epic)) {
                        isNoHaveMistake = false;
                        break;
                    }

                    SubTask subTask = (SubTask) newTask;

                    if (subTask.getEpicId() != task.getId()) {
                        isNoHaveMistake = false;
                        break;
                    }
                } else
                    isNoHaveMistake = true;
            }

            if (!isNoHaveMistake)
            throw new TimeTaskConflictException("Задачи пересекаются по времени");
        } catch (TimeTaskConflictException exception) {
            System.out.println(exception.getMessage());
        }

        return isNoHaveMistake;
    }

    private void getTimeEpic(Epic epic) {//find an epic's fields


        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Long duration = 0L;
        try {
            for (Long st : epic.getListSubTask()) {
                if (startTime == null)
                    startTime = subTasks.get(st).getStartTime();
                else if (subTasks.get(st).getStartTime().isBefore(startTime)) {
                    startTime = subTasks.get(st).getStartTime();
                }
                if (endTime == null)
                    endTime = subTasks.get(st).getEndTime();
                else if (subTasks.get(st).getEndTime().isAfter(endTime))
                    endTime = subTasks.get(st).getEndTime();
                duration += subTasks.get(st).getDuration();

            }
        } catch (NullPointerException exception) {
            System.out.println(exception.getMessage());
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);

    }

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
        if (checkingTimeConflict(sT)) {
            subTasks.put(index, sT);//записать subTask в свою таблицу
            e.getListSubTask().add(sT.getId());//добавить id subTask в поле-список эпика
            getTimeEpic(e);
            if (checkingTimeConflict(e)) {
                updateEpicStatus(e);//обновить поле-статус эпика
                prioritisedSet.add(sT);
                prioritisedSet.remove(e);
                prioritisedSet.add(e);
            }
        }
    }//Добавление новой подзадачи

    @Override
    public void addNewTask(Task o){//Добавление новой задачи
        if (checkingTimeConflict(o)) {
            o.setId(generateId());
            tasks.put(index, o);
            prioritisedSet.add(o);
        }
    }//Добавление новой задачи

    @Override
    public void addNewEpic(Epic o){//Добавление нового эпика
        if (checkingTimeConflict(o)) {
            o.setId(generateId());
            epics.put(index, o);
            prioritisedSet.add(o);
        }
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
