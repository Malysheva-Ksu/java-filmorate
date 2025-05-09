package ru.yandex.practicum.filmorate.model;

public enum MpaRating {
    G("No age restrictions", 1L),
    PG("Parental guidance suggested", 2L),
    PG_13("Parents strongly cautioned", 3L),
    R("Restricted", 4L),
    NC_17("Adults only", 5L);

    private final String description;
    private final Long id;

    MpaRating(String description, Long id) {
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }
}