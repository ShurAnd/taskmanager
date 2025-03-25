package org.andrey.taskmanager.service;

import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.domain.task.TaskStatus;
import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.exception.OperationNotAllowedException;
import org.andrey.taskmanager.exception.TaskNotFoundException;
import org.andrey.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс сервис для выполнения операций над Task
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public List<Task> findAllTasks(int offset, int limit) {
        List<Task> tasks = taskRepository.findAll(PageRequest.of(offset, limit)).getContent();
        return tasks;
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с идентификатором " + id + " не найдена!"));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task updateTaskStatus(Long taskId, int statusCode) {
        TaskStatus taskStatus = TaskStatus.fromCode(statusCode);
        Task task = findTaskById(taskId);
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(r -> r.equals("ROLE_ADMIN")) &&
                !task.getAuthor().getUsername().equals(context.getAuthentication().getName())){
            throw new OperationNotAllowedException("Вы не имеете права оставлять комментарии под этой задачей");
        }
        task.setStatus(taskStatus);
        return updateTask(task);
    }
    public Task updateTaskPerformer(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        User user = userService.getUserById(userId);
        task.setTaskPerformer(user);
        return updateTask(task);
    }


    public List<Task> findTasksByAuthor(Long authorId, int offset, int limit) {
        return taskRepository.findTasksByAuthorId(authorId);
    }

    public List<Task> findTasksByPerformer(Long performerId, Integer offset, Integer limit) {
        return taskRepository.findTasksByPerformerId(performerId);
    }
}
