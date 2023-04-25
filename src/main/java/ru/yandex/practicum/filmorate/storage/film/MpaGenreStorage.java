package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaGenreStorage {
    Collection<Genre> findAllGenre();

    Genre findGenreById(Integer genreID);

    Collection<Mpa> findAllMpa();

    Mpa findMpaById(Integer MpaID);

}
