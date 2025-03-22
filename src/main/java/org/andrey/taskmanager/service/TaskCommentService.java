package org.andrey.taskmanager.service;

import org.andrey.taskmanager.domain.task.TaskComment;
import org.andrey.taskmanager.repository.TaskCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс сервис для выполнения операций над комментариями к задачам
 */
@Service
public class TaskCommentService {

    private final TaskCommentRepository repository;

    @Autowired
    public TaskCommentService(TaskCommentRepository repository){
        this.repository = repository;
    }

    public List<TaskComment> findAllCommentsForTask(Long taskId, int offset, int limit){
        return repository.findAllCommentsForTask(taskId, offset, limit);
    }

    public TaskComment findCommentById(Long id){
        return repository.findCommentById(id);
    }

    public TaskComment createComment(TaskComment taskComment){
        return repository.createComment(taskComment);
    }

    public TaskComment updateComment(TaskComment taskComment){
        return repository.updateComment(taskComment);
    }

    public void deleteCommentById(Long id){
        repository.deleteCommentById(id);
    }
}
