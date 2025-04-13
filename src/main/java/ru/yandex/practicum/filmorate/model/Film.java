package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Название не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @NotNull(message = "Дата релиза не должна быть null.")
    @ReleaseDateConstraint
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма не может быть null.")
    private Long durationInSeconds;

    @JsonProperty("duration")
    @PositiveDuration
    public Long getDurationInSeconds() {
        return durationInSeconds;
    }

    @JsonProperty("duration")
    public void setDurationInSeconds(Long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public String getDisplayName() {
        return (name == null || name.trim().isEmpty()) ? String.valueOf(id) : name;
    }
}