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

    // Получить все MPA рейтинги
    public List<Mpa> getAllRatings() {
        return Arrays.stream(MpaRating.values())
                .map(rating -> new Mpa(rating.getId(), rating.name()))
                .collect(Collectors.toList());
    }

    // Получить MPA рейтинг по ID
    public Mpa getRatingById(Long id) {
        MpaRating rating = findMpaRatingById(id);
        if (rating == null) {
            throw new MpaNotFoundException("MPA рейтинг с ID " + id + " не найден");
        }
        return new Mpa(rating.getId(), rating.name());
    }

    // Проверить существование MPA рейтинга
    public void checkMpaExists(Long id) {
        if (findMpaRatingById(id) == null) {
            throw new MpaNotFoundException("MPA рейтинг с ID " + id + " не найден");
        }
    }

    // Вспомогательный метод для поиска MpaRating по ID
    private MpaRating findMpaRatingById(Long id) {
        return Arrays.stream(MpaRating.values())
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}