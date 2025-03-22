package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.domain.task.TaskPriority;
import org.andrey.taskmanager.domain.task.TaskStatus;
import org.andrey.taskmanager.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC реализация TaskRepository
 */
@Repository
public class JdbcTaskRepository implements TaskRepository{
    private static final String TABLE_NAME = "tasks";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Task> findAllTasks(int offset, int limit) {
        String sql = "SELECT " +
                "t.*, " +
                "u1.id as author_id, u1.first_name as author_first_name, " +
                "u1.last_name as author_last_name, u1.username as author_username, " +
                "u2.id as performer_id, u2.first_name as performer_first_name, " +
                "u2.last_name as performer_last_name, u2.username as performer_username " +
                "FROM " + TABLE_NAME + " as t " +
                "INNER JOIN users u1 ON t.author = u1.id " +
                "INNER JOIN users u2 ON t.task_performer = u2.id " +
                "OFFSET ? LIMIT ?";

        return jdbcTemplate.query(sql,
                (r, i) -> createTaskFromResultSet(r),
                offset, limit);
    }

    @Override
    public List<Task> findTasksByAuthor(Long authorId, int offset, int limit) {
        String sql = "SELECT " +
                "t.*, " +
                "u1.id as author_id, u1.first_name as author_first_name, " +
                "u1.last_name as author_last_name, u1.username as author_username, " +
                "u2.id as performer_id, u2.first_name as performer_first_name, " +
                "u2.last_name as performer_last_name, u2.username as performer_username " +
                "FROM " + TABLE_NAME + " as t " +
                "INNER JOIN users u1 ON t.author = u1.id " +
                "INNER JOIN users u2 ON t.task_performer = u2.id " +
                "WHERE t.author = ? " +
                "OFFSET ? LIMIT ?";

        return jdbcTemplate.query(sql,
                (r, i) -> createTaskFromResultSet(r),
                authorId, offset, limit);
    }

    @Override
    public List<Task> findTasksByPerformer(Long performerId, int offset, int limit) {
        String sql = "SELECT " +
                "t.*, " +
                "u1.id as author_id, u1.first_name as author_first_name, " +
                "u1.last_name as author_last_name, u1.username as author_username, " +
                "u2.id as performer_id, u2.first_name as performer_first_name, " +
                "u2.last_name as performer_last_name, u2.username as performer_username " +
                "FROM " + TABLE_NAME + " as t " +
                "INNER JOIN users u1 ON t.author = u1.id " +
                "INNER JOIN users u2 ON t.task_performer = u2.id " +
                "WHERE t.task_performer = ? " +
                "OFFSET ? LIMIT ?";

        return jdbcTemplate.query(sql,
                (r, i) -> createTaskFromResultSet(r),
                performerId, offset, limit);
    }

    @Override
    public Task findTaskById(Long id) {
        String sql = "SELECT " +
                "t.*, " +
                "u1.id as author_id, u1.first_name as author_first_name, " +
                "u1.last_name as author_last_name, u1.username as author_username, " +
                "u2.id as performer_id, u2.first_name as performer_first_name, " +
                "u2.last_name as performer_last_name, u2.username as performer_username " +
                "FROM " + TABLE_NAME + " as t " +
                "INNER JOIN users u1 ON t.author = u1.id " +
                "INNER JOIN users u2 ON t.task_performer = u2.id " +
                "WHERE t.id = ?";

        return jdbcTemplate.query(sql, rs -> {
            Task t = null;
            while (rs.next()) {
                t = createTaskFromResultSet(rs);
            }
            return t;
        });
    }

    @Override
    public Task createTask(Task task) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", task.getTitle());
        params.put("description", task.getDescription());
        params.put("status", task.getStatus().getCode());
        params.put("priority", task.getPriority().getCode());
        params.put("author", task.getAuthor().getId());
        params.put("task_performer", task.getTaskPerformer().getId());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        task.setId(id);

        return task;
    }

    @Override
    public Task updateTask(Task task) {
        String sql = "UPDATE " + TABLE_NAME +
                " SET " +
                "title = ? " +
                "description = ? " +
                "status = ? " +
                "priority = ? " +
                "author = ? " +
                "task_performer = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, task.getTitle(),
                task.getDescription(),
                task.getStatus().getCode(),
                task.getPriority().getCode(),
                task.getAuthor().getId(),
                task.getTaskPerformer().getId());

        return task;
    }

    @Override
    public void deleteTaskById(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME +
                " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Метод для создания объекта пользователя из ResultSet
     */
    private Task createTaskFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("t.id");
        String title = rs.getString("t.title");
        String description = rs.getString("t.description");
        TaskStatus  taskStatus = TaskStatus.fromCode(rs.getInt("t.status"));
        TaskPriority taskPriority = TaskPriority.fromCode(rs.getInt("t.priority"));
        User author = createUserFromResultSet(rs, "author");
        User task_performer = createUserFromResultSet(rs, "performer");

        return new Task(id,
                title,
                description,
                taskStatus,
                taskPriority,
                author,
                task_performer);
    }

    /**
     * Метод для создания объекта пользователя из ResultSet
     */
    private User createUserFromResultSet(ResultSet rs, String prefix) throws SQLException {
        Long id = rs.getLong(prefix + "_id");
        String firstName = rs.getString(prefix + "_first_name");
        String lastName = rs.getString(prefix + "_last_name");
        String username = rs.getString(prefix + "_username");

        return new User(id, firstName, lastName, username, "");
    }
}
