package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(@Qualifier("inMemoryMpaStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getAllRatings() {
        List<Mpa> allMpa = mpaStorage.getAllMpa();
        return allMpa;
    }

    public Mpa getMpaById(Long id) {
        Mpa mpa = mpaStorage.getMpaById(id);
        if (mpa == null) {
            throw new MpaNotFoundException("рейтинг с id" + id + "не найден.");
        }
        return mpa;
    }

    public void checkMpaExists(Long id) {
        if (findMpaRatingById(id) == null) {
            throw new MpaNotFoundException("рейтинг с ID " + id + " не найден");
        }
    }

    private MpaRating findMpaRatingById(Long id) {
    MpaRating rating = mpaStorage.getRatingById(id);
    if (rating == null) {
        throw new MpaNotFoundException("рейтинг с id" + id + "не найден");
    } else {
        return rating;
    }
    }
}