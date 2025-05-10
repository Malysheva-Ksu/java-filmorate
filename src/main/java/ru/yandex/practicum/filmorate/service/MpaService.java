package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.InMemoryMpaStorage;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.MpaStorage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(@Qualifier("inMemoryMpaStorage") MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getAllRatings() {
        List<MpaRating> ratingEnums = mpaStorage.getAllRatings();
        return ratingEnums.stream()
                .map(ratingEnum -> new Mpa(ratingEnum.getId(), ratingEnum.getName()))
                .collect(Collectors.toList());
    }

    public Mpa getRatingById(Long id) {
        MpaRating rating = findMpaRatingById(id);
        if (rating == null) {
            throw new MpaNotFoundException("MPA рейтинг с ID " + id + " не найден");
        }
        return new Mpa(rating.getId(), rating.name());
    }

    public void checkMpaExists(Long id) {
        if (findMpaRatingById(id) == null) {
            throw new MpaNotFoundException("MPA рейтинг с ID " + id + " не найден");
        }
    }

    private MpaRating findMpaRatingById(Long id) {
        return Arrays.stream(MpaRating.values())
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}