package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс объявляющий методы для взаимодействия Task с БД
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>,
        CrudRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks WHERE author_id = :authorId", nativeQuery = true)
    Page<Task> findTasksByAuthorId(Long authorId, Pageable pageable);

    @Query(value = "SELECT * FROM tasks WHERE performer_id = :performerId", nativeQuery = true)
    Page<Task> findTasksByPerformerId(Long performerId, Pageable pageable);
}
