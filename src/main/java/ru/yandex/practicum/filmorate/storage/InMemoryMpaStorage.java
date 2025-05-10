package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.MpaStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("inMemoryMpaStorage")
public class InMemoryMpaStorage implements MpaStorage {

    private final Map<Long, MpaRating> ratings = new HashMap<>();

    public InMemoryMpaStorage() {
        ratings.put(1L, MpaRating.G);
        ratings.put(2L, MpaRating.PG);
        ratings.put(3L, MpaRating.PG_13);
        ratings.put(4L, MpaRating.R);
        ratings.put(5L, MpaRating.NC_17);
    }

    @Override
    public List<MpaRating> getAllRatings() {
        return new ArrayList<>(ratings.values());
    }

    @Override
    public MpaRating getRatingById(Long id) {
        MpaRating rating = ratings.get(id);
        if (rating == null) {
            throw new MpaNotFoundException("MPA-рейтинг с идентификатором " + id + " не найден");
        }
        return rating;
    }

    public boolean existsById(Long id) {
        return ratings.containsKey(id);
    }
}