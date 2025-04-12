package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
            user.setName(null);
        }

        user.setId(currentId++);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping({ "", "/" })
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Попытка обновления несуществующего пользователя с ID {}", user.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (user.getName() != null && user.getName().trim().isEmpty()) {
            user.setName(null);
        }

        users.put(user.getId(), user);
        log.info("Обновлен пользователь с ID {}: {}", user.getId(), user);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = users.values()
                .stream()
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

}