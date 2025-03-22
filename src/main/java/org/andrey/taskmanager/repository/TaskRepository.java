package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.task.Task;

import java.util.List;

/**
 * Интерфейс объявляющий методы для взаимодействия Task с БД
 */
public interface TaskRepository {
    Task createTask(Task task);
    Task updateTask(Task task);
    Task findTaskById(Long id);
    List<Task> findAllTasks(int offset, int limit);
    List<Task> findTasksByAuthor(Long authorId, int offset, int limit);
    List<Task> findTasksByPerformer(Long performerId, int offset, int limit);
    void deleteTaskById(Long id);
}
