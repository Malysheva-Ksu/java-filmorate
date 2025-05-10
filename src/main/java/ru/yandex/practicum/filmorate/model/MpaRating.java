package ru.yandex.practicum.filmorate.model;

public enum MpaRating {
    G(1L,"G"),
    PG(2L, "PG"),
    PG_13(3L, "PG-13"),
    R(4L,"R"),
    NC_17(5L, "NC-17");

    private final String name;
    private final Long id;

    MpaRating(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }
}