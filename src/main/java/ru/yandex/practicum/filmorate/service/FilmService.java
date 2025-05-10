package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.FilmStorage;

import java.util.List;
import java.util.Set;

@Service
public class FilmService {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final MpaService mpaService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, GenreService genreService, MpaService mpaService) {
        this.filmStorage = filmStorage;
        this.genreService = genreService;
        this.mpaService = mpaService;
    }

    public Film addFilm(Film film) {
        if (film.getMpa() != null && film.getMpa().getId() != null) {
            try {
                mpaService.getRatingById(film.getMpa().getId());
            } catch (IllegalArgumentException e) {
                throw new MpaNotFoundException("MPA рейтинг с ID " + film.getMpa().getId() + " не найден");
            }
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() != null) {
                    try {
                        genreService.getGenreById(genre.getId().longValue());
                    } catch (Exception e) {
                        throw new GenreNotFoundException("Жанр с ID " + genre.getId() + " не найден");
                    }
                }
            }
        }

        Film addedFilm = filmStorage.addFilm(film);
        log.info("Добавлен новый фильм: {}", addedFilm);
        return addedFilm;
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Обновлен фильм: {}", updatedFilm);
        return updatedFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        log.info("Получено {} фильмов", films.size());
        return films;
    }

    public Set<Long> addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
        log.info("Лайк добавлен для фильма с ID {} от пользователя {}", filmId, userId);
        Film film = filmStorage.getFilmById(filmId);
        Set<Long> likes = film.getLikes();

        return likes;
    }

    public Set<Long> removeLike(Long filmId, Long userId) {
        filmStorage.removeLike(filmId, userId);
        log.info("Лайк удалён для фильма с ID {} от пользователя {}", filmId, userId);
        Film film = filmStorage.getFilmById(filmId);
        Set<Long> likes = film.getLikes();

        return likes;
    }

    public List<Film> getTopFilms(Integer count) {
        List<Film> topFilms = filmStorage.getTopFilms().stream().limit(count).toList();
        log.info("Возвращено {} популярных фильмов", topFilms.size());
        return topFilms;
    }

    public Film getFilmById(Long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        return film;
    }
}