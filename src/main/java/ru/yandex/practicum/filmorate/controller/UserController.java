package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        user.setId(currentId++);
        users.put(user.getId(), user);

        log.info("Добавлен новый пользователь: {}", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
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

        users.put(existingUser.getId(), existingUser);

        log.info("Обновлен пользователь с ID {}: {}", user.getId(), existingUser);
        return ResponseEntity.ok(existingUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = users.values().stream().collect(Collectors.toList());
        return ResponseEntity.ok(userList);
    }
}