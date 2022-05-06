package taskManager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {

        public Task getTasks(long index);//Метод возвращает экземпляр по идентификатору

        public void removeTask(Long i);//Удаляет экземпляр по идентификатору

        public void removeAllTasks();//Удаление всех экземпляров

        public Set<Task> getPrioritizedTasks();

        public List<Task> getHistory();

        public void updateTasks(Task o);//Метод для обновления экземпляра

        public ArrayList<Task> getAllTasks();//Вывод всех экземпляров

        public void addNewSubTask(SubTask obj);//Добавление нового подэкземпляр

        public void addNewTask(Task obj);//Добавление нового экземпляра

        public void addNewEpic(Epic obj);//Добавление нового эпикаэкземпляра


}
