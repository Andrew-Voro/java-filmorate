package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void nullFilm() {
        Film film = null;
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {

                        controller.create(film);
                    }
                });
        assertEquals("Тело запроса не может быть пустым."
                , exception.getMessage());

    }

    @Test
    public void nullFilmsName() {
        Film film = Film.builder().description("Comedy").duration(2).id(0).name(null)
                .releaseDate(LocalDate.of(1917, 12, 12)).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(film);
                    }
                });
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());

    }

    @Test
    public void descriptionTooLong() {
        Film film = Film.builder().description("Comedy dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "").duration(2).id(0).name("Charly")
                .releaseDate(LocalDate.of(1917, 12, 12)).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(film);
                    }
                });
        assertEquals("Максимальная длина описания фильма — 200 символов.", exception.getMessage());

    }

    @Test
    public void FilmsReleaseDateTooEarly() {
        Film film = Film.builder().description("Comedy").duration(2).id(0).name("Charly")
                .releaseDate(LocalDate.of(1817, 12, 12)).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(film);
                    }
                });
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года.", exception.getMessage());

    }

    @Test
    public void durationMustBeBiggerZero() {
        Film film = Film.builder().description("Comedy").duration(-2).id(0).name("Charly")
                .releaseDate(LocalDate.of(1917, 12, 12)).build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(film);
                    }
                });
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());

    }

}