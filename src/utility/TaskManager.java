package utility;

import java.util.ArrayList;

public interface TaskManager {

        public Task getTasks(long index);//Метод возвращает задачу по идентификатору

        public void removeTask(Long i);//Удаляет задачу по идентификатору

        public void updateTasks(Task o);//Метод для обновления задачи

        public void removeAllTasks();//Удаление всех задач

        public ArrayList<Task> returnAllTasks();//Вывод всех задач

        public void addNewSubTask(SubTask obj);//Добавление новой подзадачи

        public void addNewTask(Task obj);//Добавление новой задачи

        public void addNewEpic(Epic obj);//Добавление нового эпика


}
