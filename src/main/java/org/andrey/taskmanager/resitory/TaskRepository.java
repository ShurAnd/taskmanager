package org.andrey.taskmanager.resitory;

import org.andrey.taskmanager.domain.task.Task;

import java.util.List;

/**
 * Интерфейс описывающий операции с Task в БД
 */
public interface TaskRepository {
    Task save(Task task);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    void deleteTaskById(Long id);
}
