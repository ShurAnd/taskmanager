package org.andrey.taskmanager.repository;

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
 * JDBC реализация UserRepository
 */
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final String TABLE_NAME = "users";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<User> findAllUsers(int offset, int limit) {
        String sql = "SELECT * FROM " + TABLE_NAME + " OFFSET ? LIMIT ?";

        return jdbcTemplate.query(sql,
                (r, i) -> createUserFromResultSet(r)
                , offset, limit);
    }

    @Override
    public User findUserById(Long id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";

        return jdbcTemplate.query(sql, rs -> {
            User u = null;
            while (rs.next()) {
                u = createUserFromResultSet(rs);
            }
            return u;
        });
    }

    @Override
    public User createUser(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("first_name", user.getFirstName());
        params.put("last_name", user.getLastName());
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        user.setId(id);

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "UPDATE " + TABLE_NAME +
                " SET " +
                "first_name = ? " +
                "last_name = ? " +
                "username = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getId());

        return user;
    }

    @Override
    public void deleteUserById(Long id) {
        String sql = "DELETE FROM " + TABLE_NAME +
                " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    /**
     * Метод для создания объекта пользователя из ResultSet
     */
    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String username = rs.getString("username");

        return new User(id, firstName, lastName, username, "");
    }
}
