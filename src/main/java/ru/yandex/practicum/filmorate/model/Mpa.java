package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MpaDeserializer;

@JsonDeserialize(using = MpaDeserializer.class)
@Data
public class Mpa {
    private Long id;
    private String name;

    public Mpa(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Mpa() {
    }

    public static MpaRating getMpaRatingById(Long id) {
        for (MpaRating rating : MpaRating.values()) {
            if (rating.getId().equals(id)) {
                return rating;
            }
        }
        return null;
    }

    public static Mpa fromMpaRating(MpaRating mpaRating) {
        return new Mpa(mpaRating.getId(), mpaRating.name());
    }
}