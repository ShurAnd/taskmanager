package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.task.TaskComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс объявляющий методы для взаимодействия TaskComment с БД
 */
@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long>,
        CrudRepository<TaskComment, Long> {
    @Query(value = "SELECT * FROM comments WHERE task_id = :taskId", nativeQuery = true)
    Page<TaskComment> findCommentsByTaskId(Long taskId, Pageable pageable);
}
