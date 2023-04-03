package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.HashSet;
import java.util.List;


@Service
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Film addLike(Integer id, Integer userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("addLike: Нет фильма с Id = " + id);
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("addLike: Нет юзера с userId = " + userId);
        }
        try {
            filmStorage.getFilms().get(id).getLikes().add(userId);
            return filmStorage.getFilms().get(id);
        } catch (Throwable e) {
            filmStorage.getFilms().get(id).setLikes(new HashSet<>());
            filmStorage.getFilms().get(id).getLikes().add(userId);
            return filmStorage.getFilms().get(id);
        }
    }

    public Film deleteLike(Integer id, Integer userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("deleteLike: Нет фильма с Id = " + id);
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("deleteLike: Нет юзера с userId = " + userId);
        }
        try {
            filmStorage.getFilms().get(id).getLikes().remove(userId);
            return filmStorage.getFilms().get(id);
        } catch (Throwable e) {
            filmStorage.getFilms().get(id).setLikes(new HashSet<>());
            return filmStorage.getFilms().get(id);
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.findPopular(count);
    }
}
