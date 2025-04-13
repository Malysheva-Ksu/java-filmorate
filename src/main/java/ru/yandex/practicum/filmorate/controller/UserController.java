package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        if (user.getName() != null && user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        user.setId(currentId++);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Попытка обновления несуществующего пользователя с ID {}", user.getId());
            throw new RuntimeException("Пользователь не найден. ID: " + user.getId());
        }

        User existingUser = users.get(user.getId());
        existingUser.setLogin(user.getLogin());
        existingUser.setEmail(user.getEmail());
        existingUser.setBirthday(user.getBirthday());
        existingUser.setName(user.getName());

        if (existingUser.getName() != null && existingUser.getName().trim().isEmpty()) {
            existingUser.setName(user.getLogin());
        }

        users.put(user.getId(), existingUser);
        log.info("Обновлен пользователь с ID {}: {}", user.getId(), existingUser);
        return ResponseEntity.ok(existingUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = users.values().stream().collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}