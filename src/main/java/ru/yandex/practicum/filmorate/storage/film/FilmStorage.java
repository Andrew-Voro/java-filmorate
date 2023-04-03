package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;


import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film create(Film film);
    Film put(Film film);
    Collection<Film> findAll();
    List<Film> findPopular(Integer count);
    void delete(Integer id);
    Map<Integer, Film> getFilms();
}
