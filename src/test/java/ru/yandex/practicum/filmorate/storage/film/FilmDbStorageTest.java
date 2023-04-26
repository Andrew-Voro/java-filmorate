package ru.yandex.practicum.filmorate.storage.film;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    void findFilmById() {
        Film film = Film.builder().id(1).mpa(Mpa.builder().id(1).name("G").build()).name("Lucy2")
                .releaseDate(LocalDate.of(2020, 02, 03)).description("new film")
                .duration(120).genres(new ArrayList<>()).rate(1).build();
        filmStorage.create(film);
        filmStorage.findFilmById(1);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1);
        assertThat(film).hasFieldOrPropertyWithValue("name", "Lucy2");
        filmStorage.delete(film.getId());
    }

    @Test
    void create() {
        Film film = Film.builder().id(1).mpa(Mpa.builder().id(1).name("G").build()).name("Lucy2")
                .releaseDate(LocalDate.of(2020, 02, 03)).description("new film")
                .duration(120).genres(new ArrayList<>()).rate(1).build();
        Film filmCreated = filmStorage.create(film);
        Film filmFromDb = filmStorage.findFilmById(filmCreated.getId());
        assertThat(filmFromDb).hasFieldOrPropertyWithValue("name", "Lucy2");
        filmStorage.delete(filmCreated.getId());
    }

    @Test
    void put() {
        Film film2 = Film.builder().id(1).mpa(Mpa.builder().id(1).name("G").build()).name("Lucy2")
                .releaseDate(LocalDate.of(2020, 02, 03)).description("new film")
                .duration(120).genres(new ArrayList<>()).rate(1).build();
        Film filmCreated = filmStorage.create(film2);
        Film film = filmStorage.findFilmById(1);
        assertThat(film).hasFieldOrPropertyWithValue("name", "Lucy2");
        film.setName("Lucy2Updated");
        filmStorage.put(film);
        Film filmUpdated = filmStorage.findFilmById(1);
        assertThat(filmUpdated).hasFieldOrPropertyWithValue("name", "Lucy2Updated");
        filmStorage.delete(filmCreated.getId());
    }

    @Test
    void findAll() {
        Film film2 = Film.builder().id(1).mpa(Mpa.builder().id(1).name("G").build()).name("Lucy2")
                .releaseDate(LocalDate.of(2020, 02, 03)).description("new film")
                .duration(120).genres(new ArrayList<>()).rate(1).build();
        Film filmCreated = filmStorage.create(film2);
        Collection<Film> films = filmStorage.findAll();
        assertThat(films.size()).isEqualTo(1);
        filmStorage.delete(filmCreated.getId());
    }


    @Test
    void delete() {
        Film film2 = Film.builder().id(1).mpa(Mpa.builder().id(1).name("G").build()).name("Lucy2")
                .releaseDate(LocalDate.of(2020, 02, 03)).description("new film")
                .duration(120).genres(new ArrayList<>()).rate(1).build();
        Film filmCreated = filmStorage.create(film2);
        Collection<Film> films = filmStorage.findAll();
        assertThat(films.size()).isEqualTo(1);
        filmStorage.delete(filmCreated.getId());
        Collection<Film> films2 = filmStorage.findAll();
        assertThat(films2.size()).isEqualTo(0);
    }

    @Test
    void findAllGenre() {
        Collection<Genre> genres = filmStorage.findAllGenre();
        assertThat(genres.size()).isEqualTo(6);
    }

    @Test
    void findGenreById() {
        Genre genre = filmStorage.findGenreById(1);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void findAllMpa() {
        Collection<Mpa> mpaList = filmStorage.findAllMpa();
        assertThat(mpaList.size()).isEqualTo(5);
    }

    @Test
    void findMpaById() {
        Mpa mpa = filmStorage.findMpaById(1);
        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
    }
}