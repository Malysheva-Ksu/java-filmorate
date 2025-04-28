package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private Long currentId = 1L;
    private final InMemoryUserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(currentId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм с ID " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return films.values().stream().collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(Long filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Фильм с ID " + filmId + " не найден");
        }
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Фильм с ID " + filmId + " не найден");
        }

        if (!userStorage.existsUserById(userId)) {
            throw new UserNotFoundException("Некорректный ID пользователя");
        }

            if (!film.getLikes().contains(userId)) {
                film.getLikes().add(userId);
            }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        if (!userStorage.existsUserById(userId)) {
            throw new UserNotFoundException("Некорректный ID пользователя");
        }

        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> getTopFilms() {
        return films.values().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .collect(Collectors.toList());
    }
}