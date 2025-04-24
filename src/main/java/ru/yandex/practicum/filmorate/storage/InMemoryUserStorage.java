package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1L;

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        User existingUser = users.get(user.getId());
        existingUser.setLogin(user.getLogin());
        existingUser.setEmail(user.getEmail());
        existingUser.setBirthday(user.getBirthday());
        existingUser.setName(user.getName());

        return existingUser;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    @Override
    public Set<Long> addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user.getFriends();
    }

    @Override
    public Set<Long> removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (user.getFriends() != null) {
            user.getFriends().remove(friendId);
        }
        if (friend.getFriends() != null) {
            friend.getFriends().remove(userId);
        }
        return user.getFriends();
    }

    @Override
    public Set<Long> getCommonFriends(Long userId, Long otherUserId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);
        Set<Long> commonFriends = new HashSet<>();
        if (user.getFriends() != null && otherUser.getFriends() != null) {
            commonFriends.addAll(user.getFriends());
            commonFriends.retainAll(otherUser.getFriends());
        }
        return commonFriends;
    }

    @Override
    public Set<Long> getFriends(Long userId) {
        User user = getUserById(userId);
        Set<Long> friends = user.getFriends();
        return friends;
    }
}