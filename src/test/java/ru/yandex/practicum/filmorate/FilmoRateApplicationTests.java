package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, FilmDbStorage.class})
class FilmoRateApplicationTests {

    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAddUser() {
        User newUser = new User();
        newUser.setName("Test User");
        newUser.setLogin("testUser");
        newUser.setEmail("test@example.com");
        newUser.setBirthday(LocalDate.of(1990, 5, 15));

        User addedUser = userStorage.addUser(newUser);

        assertThat(addedUser.getId()).isNotNull();

        User retrievedUser = userStorage.getUserById(addedUser.getId());

        assertThat(retrievedUser)
                .hasFieldOrPropertyWithValue("name", "Test User")
                .hasFieldOrPropertyWithValue("login", "testUser")
                .hasFieldOrPropertyWithValue("email", "test@example.com");
    }

    @Test
    public void testUpdateUser() {
        User newUser = new User();
        newUser.setName("Original Name");
        newUser.setLogin("originalLogin");
        newUser.setEmail("original@example.com");
        newUser.setBirthday(LocalDate.of(1990, 5, 15));

        User addedUser = userStorage.addUser(newUser);

        addedUser.setName("Updated Name");
        addedUser.setEmail("updated@example.com");

        User updatedUser = userStorage.updateUser(addedUser);

        assertThat(updatedUser)
                .hasFieldOrPropertyWithValue("name", "Updated Name")
                .hasFieldOrPropertyWithValue("email", "updated@example.com")
                .hasFieldOrPropertyWithValue("id", addedUser.getId());
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setName("User 1");
        user1.setLogin("user1");
        user1.setEmail("user1@example.com");
        user1.setBirthday(LocalDate.of(1990, 5, 15));

        User user2 = new User();
        user2.setName("User 2");
        user2.setLogin("user2");
        user2.setEmail("user2@example.com");
        user2.setBirthday(LocalDate.of(1985, 3, 20));

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        Collection<User> allUsers = userStorage.getAllUsers();

        assertThat(allUsers).hasSizeGreaterThanOrEqualTo(2);

        assertThat(allUsers).extracting(User::getEmail)
                .contains("user1@example.com", "user2@example.com");
    }

    @Test
    public void testAddFilm() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDurationInSeconds(120L);
        film.setMpa(new Mpa(1L, "G"));

        Film addedFilm = filmStorage.addFilm(film);

        assertNotNull(addedFilm);
        assertEquals("Film Name", addedFilm.getName());
        assertEquals("Film Description", addedFilm.getDescription());
        assertEquals(120L, addedFilm.getDurationInSeconds().longValue());
        assertEquals("G", addedFilm.getMpa().getName());
    }

    @Test
    public void testGetFilmById() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDurationInSeconds(120L);
        film.setMpa(new Mpa(1L, "G"));

        Long filmId = filmStorage.addFilm(film).getId();

        Film retrievedFilm = filmStorage.getFilmById(filmId);

        assertNotNull(retrievedFilm);
        assertEquals(filmId, retrievedFilm.getId());
        assertEquals("Film Name", retrievedFilm.getName());
        assertEquals("Film Description", retrievedFilm.getDescription());
        assertEquals(120L, retrievedFilm.getDurationInSeconds().longValue());
        assertEquals("G", retrievedFilm.getMpa().getName());
    }

    @Test
    public void testUpdateFilm() {
        Film originalFilm = new Film();
        originalFilm.setName("Original Film Name");
        originalFilm.setDescription("Original Film Description");
        originalFilm.setReleaseDate(LocalDate.of(2023, 1, 1));
        originalFilm.setDurationInSeconds(120L);
        originalFilm.setMpa(new Mpa(1L, "G"));

        Film addedFilm = filmStorage.addFilm(originalFilm);
        assertNotNull(addedFilm);
        assertEquals("Original Film Name", addedFilm.getName());
        assertEquals("Original Film Description", addedFilm.getDescription());
        assertEquals(120L, addedFilm.getDurationInSeconds().longValue());
        assertEquals("G", addedFilm.getMpa().getName());

        Film updatedFilm = new Film();
        updatedFilm.setId(addedFilm.getId());
        updatedFilm.setName("Updated Film Name");
        updatedFilm.setDescription("Updated Film Description");
        updatedFilm.setReleaseDate(LocalDate.of(2024, 1, 1));
        updatedFilm.setDurationInSeconds(150L);
        updatedFilm.setMpa(new Mpa(2L, "PG"));

        Film updatedFilmFromDb = filmStorage.updateFilm(updatedFilm);

        assertNotNull(updatedFilmFromDb);
        assertEquals("Updated Film Name", updatedFilmFromDb.getName());
        assertEquals("Updated Film Description", updatedFilmFromDb.getDescription());
        assertEquals(150L, updatedFilmFromDb.getDurationInSeconds().longValue());
        assertEquals("PG", updatedFilmFromDb.getMpa().getName());
        assertEquals(addedFilm.getId(), updatedFilmFromDb.getId());

        Film retrievedFilm = filmStorage.getFilmById(updatedFilmFromDb.getId());
        assertNotNull(retrievedFilm);
        assertEquals("Updated Film Name", retrievedFilm.getName());
        assertEquals("Updated Film Description", retrievedFilm.getDescription());
        assertEquals(150L, retrievedFilm.getDurationInSeconds().longValue());
        assertEquals("PG", retrievedFilm.getMpa().getName());
        assertEquals(addedFilm.getId(), retrievedFilm.getId());
    }

    @Test
    public void testAddLike() {
        User user = new User();
        user.setName("Test User");
        user.setLogin("testUser");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1990, 5, 15));

        User addedUser = userStorage.addUser(user);
        assertNotNull(addedUser);
        assertEquals("Test User", addedUser.getName());
        assertEquals("test@example.com", addedUser.getEmail());

        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Film Description");
        film.setReleaseDate(LocalDate.of(2023, 1, 1));
        film.setDurationInSeconds(120L);
        film.setMpa(new Mpa(1L, "G"));

        Film addedFilm = filmStorage.addFilm(film);
        assertNotNull(addedFilm);
        assertEquals("Test Film", addedFilm.getName());
        assertEquals("Test Film Description", addedFilm.getDescription());
        assertEquals(120L, addedFilm.getDurationInSeconds().longValue());
        assertEquals("G", addedFilm.getMpa().getName());

        Long filmId = addedFilm.getId();
        Long userId = addedUser.getId();

        filmStorage.addLike(filmId, userId);

        String sqlQuery = "SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?";
        Long count = jdbcTemplate.queryForObject(sqlQuery, Long.class, filmId, userId);

        assertNotNull(count);
        assertEquals(1L, count.longValue());
    }
}