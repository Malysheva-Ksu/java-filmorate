package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "films")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @Setter
    @ReleaseDateConstraint(message = "Дата релиза не может быть раньше 28 декабря 1895 года.")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма не может быть null.")
    @PositiveDuration(message = "Продолжительность фильма должна быть положительным числом.")
    private Duration duration;

    @NotNull(message = "Продолжительность фильма не может быть null.")
    private Long durationInSeconds;

    public Long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(Long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
        if (durationInSeconds != null) {
            this.duration = Duration.ofSeconds(durationInSeconds);
        } else {
            this.duration = null;
        }
    }



    public boolean isValidDuration() {
        return duration != null && !duration.isNegative() && !duration.isZero();
    }
}