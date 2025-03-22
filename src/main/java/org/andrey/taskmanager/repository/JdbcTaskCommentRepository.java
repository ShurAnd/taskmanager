package org.andrey.taskmanager.repository;

import org.andrey.taskmanager.domain.task.TaskComment;
import org.andrey.taskmanager.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC реализация TaskCommentRepository
 */
public class JdbcTaskCommentRepository implements TaskCommentRepository{

    private static final String TABLE_NAME = "comments";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcTaskCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<TaskComment> findAllCommentsForTask(Long taskId, int offset, int limit) {
        String sql = "SELECT " +
                "cm.* " +
                "u.id, u.first_name, u.last_name, u.username " +
                "FROM " + TABLE_NAME + " AS cm " +
                "INNER JOIN users u ON cm.author = u.id " +
                "WHERE task_id = ?";

        return jdbcTemplate.query(sql,
                (r, i) -> createTaskCommentFromResultSet(r)
                ,taskId);
    }

    @Override
    public TaskComment findCommentById(Long id) {
        String sql = "SELECT " +
                "cm.* " +
                "u.id, u.first_name, u.last_name, u.username " +
                "FROM " + TABLE_NAME + " AS cm " +
                "INNER JOIN users u ON cm.author = u.id " +
                "WHERE id = ?";


        return jdbcTemplate.query(sql, rs -> {
            TaskComment tc = null;
            while (rs.next()) {
                tc = createTaskCommentFromResultSet(rs);
            }
            return tc;
        });
    }

    @Override
    public TaskComment createComment(TaskComment comment) {
        Map<String, Object> params = new HashMap<>();
        params.put("comment", comment.getComment());
        params.put("author", comment.getAuthor().getId());
        params.put("task_id", comment.getTaskId());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        comment.setId(id);

        return comment;
    }

    @Override
    public TaskComment updateComment(TaskComment comment) {
        String sql = "UPDATE " + TABLE_NAME +
                " SET " +
                "comment = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, comment.getComment(),
                comment.getId());

        return comment;
    }

    @Override
    public void deleteCommentById(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME +
                " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Метод для создания объекта TaskComment из ResultSet
     */
    private TaskComment createTaskCommentFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("cm.id");
        Long taskId = rs.getLong("cm.task_id");
        String comment = rs.getString("cm.comment");
        User user = new User();
        user.setId(rs.getLong("u.id"));
        user.setFirstName(rs.getString("u.first_name"));
        user.setLastName(rs.getString("u.last_name"));
        user.setUsername(rs.getString("u.username"));

        return new TaskComment(id, taskId, comment, user);
    }
}
