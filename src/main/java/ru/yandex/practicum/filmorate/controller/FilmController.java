package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {

        return filmStorage.put(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new ValidationException("getFilm: Введите положительный id.");
        }
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new ObjectNotFoundException("getFilm: Фильма c id = " + id + " нет.");
        }
        return filmStorage.getFilms().get(id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new ValidationException("deleteFilm: Введите положительный id.");
        }
        filmStorage.delete(id);
    }


    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        if (id < 0) {
            throw new ValidationException(" addLike: Введите положительный id.");
        }
        if (id < 0) {
            throw new ValidationException(" addLike: Введите положительный userId.");
        }
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        if (id < 0) {
            throw new ValidationException(" deleteLike: Введите положительный id.");
        }
        if (id < 0) {
            throw new ValidationException(" deleteLike: Введите положительный userId.");
        }
        return filmService.deleteLike(id, userId);
    }


    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        if (Integer.parseInt(count) < 0) {
            throw new ValidationException(" getPopularFilms: Введите положительный count.");
        }
        return filmService.getPopularFilms(Integer.parseInt(count));
    }
}
