package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.MpaStorage;

import java.util.*;

@Repository("inMemoryMpaStorage")
public class InMemoryMpaStorage implements MpaStorage {

    private final Map<MpaRating, Mpa> ratings = new LinkedHashMap<>();

    public InMemoryMpaStorage() {
        ratings.put(MpaRating.G, new Mpa(1L, "G"));
        ratings.put(MpaRating.PG, new Mpa(2L, "PG"));
        ratings.put(MpaRating.PG_13, new Mpa(3L, "PG-13"));
        ratings.put(MpaRating.R, new Mpa(4L, "R"));
        ratings.put(MpaRating.NC_17, new Mpa(5L, "NC-17"));
    }

    @Override
    public List<MpaRating> getAllRatings() {
        List<MpaRating> allRatings = new ArrayList<>(ratings.keySet());
        return allRatings;
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> allMpa = new ArrayList<>(ratings.values());
        return allMpa;
    }

    @Override
    public MpaRating getRatingById(Long id) {
            for (Map.Entry<MpaRating, Mpa> entry : ratings.entrySet()) {
                if (entry.getValue().getId().equals(id)) {
                    return entry.getKey();
                }
            }
            throw new MpaNotFoundException("рейтинг с id " + id + " не найден");
        }



    @Override
    public Mpa getMpaById(Long id) {
        for (Map.Entry<MpaRating, Mpa> entry : ratings.entrySet()) {
            if (entry.getValue().getId().equals(id)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean existsById(Long id) {
        List<Mpa> allRatings = ratings.values().stream().toList();
        Mpa mpa = allRatings.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);

        if (mpa == null) {
            return false;
        } else {
            return true;
        }
    }
}