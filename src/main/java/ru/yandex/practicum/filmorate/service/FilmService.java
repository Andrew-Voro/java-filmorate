package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {

    Film addLike(Integer id, Integer userId);

    Film deleteLike(Integer id, Integer userId);

    List<Film> getPopularFilms(Integer count);

    Film findFilmById(Integer id);

    Collection<Film> findAll();

    Film put(Film film);

    Film create(Film film);
}
