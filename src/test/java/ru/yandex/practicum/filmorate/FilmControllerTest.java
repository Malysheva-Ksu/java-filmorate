package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @Mock
    private MpaService mpaService;

    @InjectMocks
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddFilm() {
        Film film = new Film();
        film.setName("W37JqMdroARBlsO");
        film.setMpa(new Mpa(3L, "PG-13")); // PG-13

        Film createdFilm = new Film();
        createdFilm.setId(1L);
        createdFilm.setName("W37JqMdroARBlsO");
        createdFilm.setMpa(new Mpa(3L, "PG-13"));

        when(filmService.addFilm(film)).thenReturn(createdFilm);

        ResponseEntity<Film> response = filmController.addFilm(film);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdFilm, response.getBody());
        assertEquals("W37JqMdroARBlsO", response.getBody().getName());
    }
}