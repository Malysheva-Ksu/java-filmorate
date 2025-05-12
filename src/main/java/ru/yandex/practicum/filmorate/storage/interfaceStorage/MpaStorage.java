package ru.yandex.practicum.filmorate.storage.interfaceStorage;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaStorage {

    List<MpaRating> getAllRatings();

    MpaRating getRatingById(Long id);

    List<Mpa> getAllMpa();

    Mpa getMpaById(Long id);

    boolean existsById(Long id);
}