package org.andrey.taskmanager.service;

import org.andrey.taskmanager.domain.task.*;
import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.exception.OperationNotAllowedException;
import org.andrey.taskmanager.exception.TaskNotFoundException;
import org.andrey.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    /**
     * Метод для пагинированного вывода задач
     */
    public Page<Task> findAllTasks(int page, int size, Map<String, String> filters) {
        List<Specification<Task>> specs = getFiltersForTask(filters);
        Page<Task> result = taskRepository.findAll(Specification.allOf(specs), PageRequest.of(page, size));
        result.getContent().forEach(this::removeUserPassword);

        return result;
    }

    /**
     * Метод для поиска задачи по Id
     */
    public Task findTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с идентификатором " + id + " не найдена!"));
        removeUserPassword(task);

        return task;
    }

    /**
     * Метод создания новой задачи
     */
    public Task saveTask(Task task) {
        User taskAuthor = userService.getUserById(task.getAuthor().getId());
        task.setAuthor(taskAuthor);
        if (task.getTaskPerformer() != null) {
            User taskPerformer = userService.getUserById(task.getTaskPerformer().getId());
            task.setTaskPerformer(taskPerformer);
        }
        task = taskRepository.save(task);
        removeUserPassword(task);

        return task;
    }

    /**
     * Метод для удаления задачи по id
     */
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Метод для обновления статуса задачи
     */
    public Task updateTaskStatus(Long taskId, int statusCode) {
        TaskStatus taskStatus = TaskStatus.fromCode(statusCode);
        Task task = findTaskById(taskId);
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(r -> r.equals("ROLE_ADMIN")) &&
                !task.getAuthor().getEmail().equals(context.getAuthentication().getName())) {
            throw new OperationNotAllowedException("Вы не имеете права оставлять комментарии под этой задачей");
        }
        task.setStatus(taskStatus);
        task = saveTask(task);
        removeUserPassword(task);

        return task;
    }

    /**
     * Метод для назначения исполнителя задачи
     */
    public Task updateTaskPerformer(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        User user = userService.getUserById(userId);
        task.setTaskPerformer(user);
        task = saveTask(task);
        removeUserPassword(task);

        return task;
    }

    /**
     * Метод для поиска задач по автору задач
     */
    public Page<Task> findTasksByAuthor(Long authorId, int page, int size, Map<String, String> filterParams) {
        List<Specification<Task>> specs = getFiltersForTask(filterParams);
        Specification<Task> specification = Specification.where(TaskSpecification.author(authorId));
        for (Specification<Task> ts : specs){
            specification = specification.and(ts);
        }
        Page<Task> result = taskRepository.findAll(specification, PageRequest.of(page, size));
        result.getContent().forEach(this::removeUserPassword);

        return result;
    }

    /**
     * Метод для поиска задач по исполнителю задач
     */
    public Page<Task> findTasksByPerformer(Long performerId, Integer page, Integer size, Map<String, String> filters) {
        List<Specification<Task>> specs = getFiltersForTask(filters);
        Specification<Task> specification = Specification.where(TaskSpecification.performer(performerId));
        for (Specification<Task> ts : specs){
            specification = specification.and(ts);
        }
        Page<Task> result = taskRepository.findAll(specification, PageRequest.of(page, size));
        result.getContent().forEach(this::removeUserPassword);

        return result;
    }

    /**
     * Метод для удаления пароля пользователя из записи задач
     */
    private void removeUserPassword(Task task) {
        if (task.getAuthor() != null) {
            task.getAuthor().setPassword("");
        }
        if (task.getTaskPerformer() != null) {
            task.getTaskPerformer().setPassword("");
        }
    }

    /**
     * Метод для создания списка поисковых фильтров
     */
    private List<Specification<Task>> getFiltersForTask(Map<String, String> filters){
        List<Specification<Task>> specs = new ArrayList<>();
        for (String key : filters.keySet()){
            if (Arrays.stream(TaskFilters.values()).map(TaskFilters::getFilterName).anyMatch(fn -> fn.equals(key))){
                specs.add(TaskFilters.fromFilterName(key).getSpecification(filters.get(key)));
            }
        }

        return specs;
    }
}
