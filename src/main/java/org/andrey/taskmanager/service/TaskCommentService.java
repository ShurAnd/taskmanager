package org.andrey.taskmanager.service;

import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.domain.task.TaskComment;
import org.andrey.taskmanager.exception.OperationNotAllowedException;
import org.andrey.taskmanager.repository.TaskCommentRepository;
import org.andrey.taskmanager.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс сервис для выполнения операций над комментариями к задачам
 */
@Service
public class TaskCommentService {

    private final TaskCommentRepository repository;
    private final TaskService taskService;

    @Autowired
    public TaskCommentService(TaskCommentRepository repository,
                              TaskService taskService){
        this.repository = repository;
        this.taskService = taskService;
    }

    public List<TaskComment> findAllCommentsForTask(Long taskId, int offset, int limit){
        return repository.findCommentsByTaskId(taskId, Pageable.ofSize(10));
    }

    public TaskComment findCommentById(Long id){
        return repository.findById(id).orElse(null);
    }

    public TaskComment createComment(String comment, Long taskId){
        Task task = taskService.findTaskById(taskId);
        SecurityContext context = SecurityContextHolder.getContext();

        if (context.getAuthentication().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(r -> r.equals("ROLE_ADMIN")) &&
                !task.getAuthor().getUsername().equals(context.getAuthentication().getName())){
            throw new OperationNotAllowedException("Вы не имеете права оставлять комментарии под этой задачей");
        }

        SecurityUser securityUser =
                (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaskComment taskComment = new TaskComment();
        taskComment.setComment(comment);
        taskComment.setTaskId(taskId);
        taskComment.setAuthor(securityUser.user());
        return repository.save(taskComment);
    }

    public TaskComment updateComment(TaskComment taskComment){
        return repository.save(taskComment);
    }

    public void deleteCommentById(Long id){
        repository.deleteById(id);
    }
}
