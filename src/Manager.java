import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private int index = 1;
    HashMap<Integer, Epic> NEW = new HashMap<>();
    HashMap<Integer, Epic> IN_PROGRESS = new HashMap<>();
    HashMap<Integer, Epic> DONE = new HashMap<>();

    public Epic getTasks(int index){//Метод возвращает задачу по идентификатору
        if (!(NEW.isEmpty()) && NEW.containsKey(index)){
            return NEW.get(index);
        } else if (!(IN_PROGRESS.isEmpty()) && IN_PROGRESS.containsKey(index)) {
            return IN_PROGRESS.get(index);
        } else if (!(DONE.isEmpty()) && DONE.containsKey(index)) {
            return DONE.get(index);
        } else return null;
    }//Метод возвращает задачу по идентификатору

    public void removeTask(Integer i){//Удаляет задачу по идентификатору
        if (!(NEW.isEmpty()) && NEW.containsKey(i)){
            NEW.remove(i);
        } else if (!(IN_PROGRESS.isEmpty()) && IN_PROGRESS.containsKey(i)){
            IN_PROGRESS.remove(i);
        } else if (!(DONE.isEmpty()) && DONE.containsKey(i)){
            DONE.remove(i);
        }
    }//Удаляет задачу по идентификатору

    public void removeAllTasks(){//Удаление всех задач
        NEW.clear();
        IN_PROGRESS.clear();
        DONE.clear();
    }//Удаление всех задач

    public ArrayList returnAllTasks(){//Вывод всех задач
        ArrayList<Epic> ls = new ArrayList<>();
        for (int i = 1; i < index; i++){
            if (!(NEW.isEmpty()) && NEW.containsKey(i)) {
                ls.add(NEW.get(i));
            } else if(!(IN_PROGRESS.isEmpty()) && IN_PROGRESS.containsKey(i)){
                ls.add(IN_PROGRESS.get(i));
            } else if(!(DONE.isEmpty()) && DONE.containsKey(i)) {
                ls.add(DONE.get(i));
            }
        }
        return ls;
    }//Вывод всех задач

    public void updateTasks(Epic o){//Метод для обновления задачи (всех полей, включая статус)
        int index = o.getId();
        if (!(NEW.isEmpty()) && NEW.containsKey(index)) {
            if (o.getList() == null) {
                remoteJustObject(NEW, o.getStatus(), index, o);
            } else {
                updateEpicStatus(o);
                remoteJustObject(NEW, o.getStatus(), index, o);
            }

        } else if(!(IN_PROGRESS.isEmpty()) && IN_PROGRESS.containsKey(index)){
            if (o.getList() == null){
                remoteJustObject(IN_PROGRESS, o.getStatus(), index, o);
            } else {
                updateEpicStatus(o);
                remoteJustObject(IN_PROGRESS, o.getStatus(), index, o);
            }
        } else if(!(DONE.isEmpty()) && DONE.containsKey(index)) {
            if (o.getList() == null){
                remoteJustObject(DONE, o.getStatus(), index, o);
            } else {
                updateEpicStatus(o);
                remoteJustObject(DONE, o.getStatus(), index, o);
            }
        }
    }//Метод для обновления задачи (всех полей, включая статус)

    public void updateEpicStatus(Epic o){//метод актуализирует поле-статаус эпика на основе подзадач

        boolean checkStatus = false;//дополнительная переменная для определения статуса эпика
        for (SubTask sT: o.getList()){
            if (!sT.getStatus().equals(o.getList().get(0).getStatus())) checkStatus = true;
        }
        if (checkStatus) {
            o.setStatus("in_progress");
        } else {
            for (SubTask sT: o.getList()){
                if (!sT.getStatus().equals("new")) checkStatus = true;
            }
            if (!checkStatus) {
                o.setStatus("new");
            } else {
                o.setStatus("done");
            }
        }
    }//ВСПОМОГАТЕЛЬНЫЙ метод актуализирует поле-статаус эпика на основе подзадач

    public void addNewTask(Epic o){//Добавление новой задачи
//        Epic epic = (Epic) o;
        NEW.put(index, o);
        o.setId(index);
        index++;
    }//Добавление новой задачи

    public void remoteJustObject(HashMap map, String status, int index, Epic o){//метод для перезаписи простой задачи (без подзадач)
        if(status.equals("new")){//При такой логике пользователь сможет переводить любую задачу в любой статус
            map.remove(index);
            NEW.put(index, o);
        } else if(status.equals("in_progress")) {
            map.remove(index);
            IN_PROGRESS.put(index, o);
        } else if(status.equals("done")){
            map.remove(index);
            DONE.put(index, o);
        }
    }//ВСПОМОГАТЕЛЬНЫЙ метод
    //для перезаписи задачи

    /*
        public String statusSubTask(SubTask o){
            String result = "";
            if (o.stat == 0){
                result = "новая";
            } else if (o.stat == 1) {
                result = "в процессе выполнения";
            } else result = "выполнена";
            return result;
        }
    */
}
