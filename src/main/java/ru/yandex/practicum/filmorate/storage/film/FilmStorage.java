package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmIdGenreId;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film create(Film film);

    Film put(Film film);

    Film findFilmById(Integer id);

    Collection<Film> findAll();

    List<Film> findPopular(Integer count);

    void delete(Integer id);

    Map<Integer, Film> getFilms();

    Collection<FilmIdGenreId> findAllFilmIdGenreId();

    Map<Integer, Genre> getGenres();

    Collection<Film> findAllPopular();

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

}
