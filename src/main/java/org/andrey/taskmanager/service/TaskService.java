package org.andrey.taskmanager.service;

import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.domain.task.TaskStatus;
import org.andrey.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс сервис для выполнения операций над Task
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Task> findAllTasks(int offset, int limit){
        return taskRepository.findAllTasks(offset, limit);
    }

    public Task findTaskById(Long id){
        return taskRepository.findTaskById(id);
    }

    public Task createTask(Task task){
        return taskRepository.createTask(task);
    }

    public Task updateTask(Task task){
        return taskRepository.updateTask(task);
    }

    public void deleteTaskById(Long id){
        taskRepository.deleteTaskById(id);
    }

    public Task updateTaskStatus(Long taskId, int statusCode){
        TaskStatus taskStatus = TaskStatus.fromCode(statusCode);
        Task task = findTaskById(taskId);
        task.setStatus(taskStatus);
        return updateTask(task);
    }

    public List<Task> findTasksByAuthor(Long authorId, int offset, int limit){
        return taskRepository.findTasksByAuthor(authorId, offset, limit);
    }

    public List<Task> findTasksByPerformer(Long performerId, Integer offset, Integer limit) {
        return taskRepository.findTasksByPerformer(performerId, offset, limit);
    }
}
