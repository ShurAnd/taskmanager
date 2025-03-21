package org.andrey.taskmanager.service;

import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.resitory.TaskRepository;
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

    public List<Task> getAllTasks(){
        return taskRepository.getAllTasks();
    }

    public Task getTaskById(Long id){
        return taskRepository.getTaskById(id);
    }

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask(Task task){
        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        taskRepository.deleteTaskById(id);
    }
}
