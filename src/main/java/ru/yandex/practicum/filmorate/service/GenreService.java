package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.GenreStorage;

import java.util.List;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Long id) {
        Genre genre = genreStorage.getGenreById(id);
        if (genre == null) {
            throw new GenreNotFoundException("Жанр с ID " + id + " не найден");
        }
        return genre;
    }

    public void checkGenreExists(Long id) {
        if (genreStorage.getGenreById(id) == null) {
            throw new GenreNotFoundException("Жанр с ID " + id + " не найден");
        }
    }
}