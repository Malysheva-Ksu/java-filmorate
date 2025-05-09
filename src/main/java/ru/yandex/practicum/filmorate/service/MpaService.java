package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public boolean existsById(Long id) {
        try {
            MpaRating rating = mpaStorage.getRatingById(id);
            return rating != null;
        } catch (Exception e) {
            return false;
        }
    }

    public void checkMpaExists(Long id) {
        if (!existsById(id)) {
            throw new MpaNotFoundException("MPA рейтинг с ID " + id + " не найден");
        }
    }

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MpaRating> getAllRatings() {
        return mpaStorage.getAllRatings();
    }

    public MpaRating getRatingById(Long id) {
        return mpaStorage.getRatingById(id);
    }
}