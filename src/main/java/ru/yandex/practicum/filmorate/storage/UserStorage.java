package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Collection<User> getAllUsers();

    User getUserById(Long id);

    Set<Long> addFriend(Long userId, Long friendId);

    Set<Long> removeFriend(Long userId, Long friendId);

    Set<Long> getCommonFriends(Long userId, Long otherUserId);

    Set<Long> getFriends(Long userId);
}