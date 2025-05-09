package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaceStorage.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        Date releaseDate = rs.getDate("release_date");
        if (releaseDate != null) {
            film.setReleaseDate(releaseDate.toLocalDate());
        }
        film.setDurationInSeconds(rs.getLong("durationInSeconds"));
        film.setLikes(new HashSet<>());
        return film;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, durationInSeconds, mpa_rating) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDurationInSeconds());
            stmt.setString(5, film.getMpa().getName());
            return stmt;
        });

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());

        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, durationInSeconds = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                Date.valueOf(film.getReleaseDate()), film.getDurationInSeconds(), film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        films.forEach(this::loadLikesForFilm);
        return films;
    }

    @Override
    public Film getFilmById(Long filmId) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (Exception e) {
            throw new FilmNotFoundException("Фильм с ID " + filmId + " не найден");
        }
        loadLikesForFilm(film);
        return film;
    }

    private void loadLikesForFilm(Film film) {
        String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        Set<Long> likes = new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Long.class, film.getId()));
        film.setLikes(likes);
    }

    private void checkUserExists(Long userId) {
        String sqlQuery = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId);
        if (count == null || count == 0) {
            throw new UserNotFoundException("Некорректный ID пользователя");
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        getFilmById(filmId);
        checkUserExists(userId);

        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        getFilmById(filmId);
        checkUserExists(userId);

        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getTopFilms() {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.durationInSeconds, COUNT(fl.user_id) as likes_count " +
                "FROM films f LEFT JOIN likes fl ON f.film_id = fl.film_id " +
                "GROUP BY f.film_id, f.name, f.description, f.release_date, f.durationInSeconds " +
                "ORDER BY likes_count DESC";

        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            Date releaseDate = rs.getDate("release_date");
            if (releaseDate != null) {
                film.setReleaseDate(releaseDate.toLocalDate());
            }
            film.setDurationInSeconds(rs.getLong("durationInSeconds"));
            loadLikesForFilm(film);
            return film;
        });

        return films;
    }
}