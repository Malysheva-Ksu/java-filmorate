package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
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

        Long mpaId = rs.getLong("mpa_id");
        Mpa mpa = new Mpa(0L, "unknown");

        if (mpaId == 1) {
            mpa = new Mpa(1L, "G");
        } else if (mpaId ==2) {
            mpa = new Mpa(2L, "PG");
        } else if (mpaId == 3) {
            mpa = new Mpa(3L, "PG-13");
        } else if (mpaId == 4) {
            mpa = new Mpa(4L, "R");
        } else if (mpaId ==5) {
            mpa = new Mpa(5L, "NC-17");
        }

        film.setMpa(mpa);
        film.setLikes(new HashSet<>());

        return film;
    }

    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, durationInSeconds, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDurationInSeconds());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        long filmId = keyHolder.getKey().longValue();
        film.setId(filmId);

        String mpaSql = "INSERT INTO film_mpa_ratings (film_id, mpa_id) VALUES (?, ?)";
        jdbcTemplate.update(mpaSql, filmId, film.getMpa().getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String genreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(genreSql, filmId, genre.getId());
            }
        }

        return getFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        Long mpaId = film.getMpa().getId();
        Mpa mpa = film.getMpa();
        String mpaRating = mpa.getName();

        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, durationInSeconds = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                Date.valueOf(film.getReleaseDate()), film.getDurationInSeconds(), mpa.getId(), film.getId());
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
        loadMpaForFilm(film);
        loadGenresForFilm(film);
        loadLikesForFilm(film);
        return film;
    }

    private void loadMpaForFilm(Film film) {
        String sqlQuery = "SELECT mpa_id FROM film_mpa_ratings WHERE film_id = ?";
        Long mpaId = jdbcTemplate.queryForObject(sqlQuery, Long.class, film.getId());

        if (mpaId != null) {
            Mpa mpa = new Mpa(mpaId, "unknown");

            if (mpaId == 1) {
                mpa = new Mpa(1L, "G");
            } else if (mpaId == 2) {
                mpa = new Mpa(2L, "PG");
            } else if (mpaId == 3) {
                mpa = new Mpa(3L, "PG-13");
            } else if (mpaId == 4) {
                mpa = new Mpa(4L, "R");
            } else if (mpaId == 5) {
                mpa = new Mpa(5L, "NC-17");
            }

            film.setMpa(mpa);
        }
    }

    private void loadGenresForFilm(Film film) {
        String sqlQuery = "SELECT g.genre_id, g.name FROM film_genres fg " +
                "JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?";

        List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, film.getId());

        film.setGenres(new LinkedHashSet<>(genres));
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