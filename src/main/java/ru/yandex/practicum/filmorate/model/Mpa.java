package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    private Long id;
    private String name;

    public Mpa() {
    }

    public Mpa(Long id, String name) {
        this.id = id;
        this.name = name;
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