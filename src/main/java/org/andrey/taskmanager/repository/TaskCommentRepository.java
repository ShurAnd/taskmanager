package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.task.TaskComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс объявляющий методы для взаимодействия TaskComment с БД
 */
@Repository
public interface TaskCommentRepository extends PagingAndSortingRepository<TaskComment, Long>,
        CrudRepository<TaskComment, Long> {
    @Query(value = "SELECT * FROM comments WHERE task_id = :taskId", nativeQuery = true)
    List<TaskComment> findCommentsByTaskId(Long taskId, Pageable pageable);
}
