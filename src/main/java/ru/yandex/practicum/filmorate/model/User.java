package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    private Set<Long> friends = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Email должен быть действительным и содержать символ @.")
    private String email;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы.")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    public String getDisplayName() {
        return (name == null || name.isBlank()) ? login : name;
    }
}