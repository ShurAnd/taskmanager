package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.task.TaskComment;

import java.util.List;

/**
 * Интерфейс объявляющий методы для взаимодействия TaskComment с БД
 */
public interface TaskCommentRepository {
    TaskComment createComment(TaskComment comment);
    TaskComment updateComment(TaskComment comment);
    TaskComment findCommentById(Long id);
    List<TaskComment> findAllCommentsForTask(Long taskId, int offset, int limit);
    void deleteCommentById(Long id);
}
