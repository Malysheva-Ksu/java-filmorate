package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        User createdUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        Collection<User> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Set<Long>> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        Set<Long> friends = userService.addFriend(userId, friendId);
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Set<Long>> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        Set<Long> friends = userService.removeFriend(userId, friendId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public ResponseEntity<Set<Long>> getCommonFriends(@PathVariable Long userId, @PathVariable Long otherUserId) {
        Set<Long> commonFriends = userService.getCommonFriends(userId, otherUserId);
        return ResponseEntity.ok(commonFriends);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}