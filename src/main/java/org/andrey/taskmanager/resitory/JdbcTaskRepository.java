package org.andrey.taskmanager.resitory;

import org.andrey.taskmanager.domain.task.Task;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JDBC реализация TaskRepository
 */
@Repository
public class JdbcTaskRepository implements TaskRepository{
    @Override
    public Task save(Task task) {
        return null;
    }

    @Override
    public Task getTaskById(Long id) {
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        return List.of();
    }

    @Override
    public void deleteTaskById(Long id) {

    }
}
