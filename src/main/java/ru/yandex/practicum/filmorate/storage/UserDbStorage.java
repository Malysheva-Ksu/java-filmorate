package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.ErrorResponseException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        Date birthdayDate = rs.getDate("birthday");
        if (birthdayDate != null) {
            user.setBirthday(birthdayDate.toLocalDate());
        }
        return user;
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getId() == null) {
            user.setId(generateNextId());
        }

        String sqlQuery = "INSERT INTO users (user_id, name, login, email, birthday) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setLong(1, (Long)user.getId());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setString(4, user.getEmail());
            stmt.setDate(5, user.getBirthday() != null ? Date.valueOf(user.getBirthday()) : null);

            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    private Long generateNextId() {
        String sqlQuery = "SELECT COALESCE(MAX(user_id), 0) + 1 FROM users";
        return jdbcTemplate.queryForObject(sqlQuery, Long.class);
    }

    @Override
    public User updateUser(User user) {
        if (!existsUserById(user.getId())) {
            throw new UserNotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                Timestamp.valueOf(user.getBirthday().atStartOfDay()), user.getId());
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        });
    }

    @Override
    public User getUserById(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (Exception e) {
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }


    @Override
    public Set<Long> addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, 'pending')";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return getFriends(userId);
    }

    @Override
    public Set<Long> removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);

        try {
            getUserById(friendId);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("Невозможно удалить из друзей: пользователь с ID " + friendId + " не найден");
        }

        String checkQuery = "SELECT COUNT(*) FROM friendship WHERE user_id = ? AND friend_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class, userId, friendId);
        if (count == null || count == 0) {
            throw new UserNotFoundException("Пользователь с ID " + friendId + " не является другом для пользователя с ID " + userId);
        }

        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return getFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        String sqlQuery = "SELECT u.* FROM users u " +
                "JOIN friendship f1 ON u.user_id = f1.friend_id AND f1.user_id = ? " +
                "JOIN friendship f2 ON u.user_id = f2.friend_id AND f2.user_id = ?";
        return jdbcTemplate.query(sqlQuery, new Object[]{userId, otherUserId}, (rs, rowNum) -> {
            User u = new User();
            u.setId(rs.getLong("user_id"));
                    u.setEmail(rs.getString("email"));
            u.setLogin(rs.getString("login"));
            u.setName(rs.getString("name"));
            u.setBirthday(rs.getDate("birthday").toLocalDate());
            return u;
        });
    }

    @Override
    public Set<Long> getFriends(Long userId) {
        try {
            getUserById(userId);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("Невозможно получить список друзей: пользователь с ID " + userId + " не найден");
        }
        String sqlQuery = "SELECT friend_id FROM friendship WHERE user_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, userId).stream().collect(Collectors.toSet());
    }

    public boolean existsUserById(Long userId) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId);
        return count != null && count > 0;
    }
}