package ru.yandex.practicum.filmorate.model;

public enum MpaRating {
    G("No age restrictions"),
    PG("Parental guidance suggested"),
    PG_13("Parents strongly cautioned"),
    R("Restricted"),
    NC_17("Adults only");

    private final String description;

    MpaRating(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}