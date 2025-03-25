package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.task.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс объявляющий методы для взаимодействия Task с БД
 */
@Repository
public interface TaskRepository extends PagingAndSortingRepository<Task, Long>,
        CrudRepository<Task, Long> {
    @Query(value = "SELECT * FROM tasks WHERE author_id = :authorId", nativeQuery = true)
    List<Task> findTasksByAuthorId(Long authorId);
    @Query(value = "SELECT * FROM tasks WHERE performer_id = :performerId", nativeQuery = true)
    List<Task> findTasksByPerformerId(Long performerId);
}
