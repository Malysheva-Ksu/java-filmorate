package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1L;

    private final Map<Long, Map<Long, FriendshipStatus>> friendships = new HashMap<>();

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
        getUserById(userId);
        getUserById(friendId);

        Map<Long, FriendshipStatus> friendRequests = friendships.getOrDefault(friendId, new HashMap<>());
        FriendshipStatus reverseStatus = friendRequests.get(userId);
        if (reverseStatus != null && reverseStatus == FriendshipStatus.PENDING) {
            friendships.computeIfAbsent(friendId, k -> new HashMap<>()).put(userId, FriendshipStatus.CONFIRMED);
            friendships.computeIfAbsent(userId, k -> new HashMap<>()).put(friendId, FriendshipStatus.CONFIRMED);
        } else {
            friendships.computeIfAbsent(userId, k -> new HashMap<>()).put(friendId, FriendshipStatus.PENDING);
        }
        return getFriends(userId);
    }

    @Override
    public Set<Long> removeFriend(Long userId, Long friendId) {
        Map<Long, FriendshipStatus> userFriends = friendships.get(userId);
        if (userFriends != null) {
            userFriends.remove(friendId);
        }

        return getFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        Set<Long> userConfirmedFriends = getFriends(userId);
        Set<Long> otherConfirmedFriends = getFriends(otherUserId);

        Set<Long> commonFriendIds = userConfirmedFriends.stream()
                .filter(otherConfirmedFriends::contains)
                .collect(Collectors.toSet());

        List<User> commonFriends = new ArrayList<>();
        for (Long id : commonFriendIds) {
            commonFriends.add(getUserById(id));
        }
        return commonFriends;
    }

    @Override
    public Set<Long> getFriends(Long userId) {
        Map<Long, FriendshipStatus> friendsMap = friendships.getOrDefault(userId, Collections.emptyMap());
        return friendsMap.entrySet().stream()
                .filter(e -> e.getValue() == FriendshipStatus.CONFIRMED)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public boolean existsUserById(Long userId) {
        return users.containsKey(userId);
    }
}