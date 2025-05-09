package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MpaService {

    public List<Mpa> getAllRatings() {
        return Arrays.stream(MpaRating.values())
                .map(rating -> new Mpa(rating.getId(), rating.name()))
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