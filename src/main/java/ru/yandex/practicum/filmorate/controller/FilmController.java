package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmIdGenreId;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;


import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;
    private final UserService userService;


    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        return filmService.create(film);
    }

    @GetMapping("/FilmIdGenreId")
    public Collection<FilmIdGenreId> findAllFilmIdGenreId() {
        return filmStorage.findAllFilmIdGenreId();
    }


    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        return filmService.put(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new ValidationException("getFilm: Введите положительный id.");
        }

        return filmService.findFilmById(id);
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
        userService.findUserById(userId);
        if (id < 0) {
            throw new ValidationException(" addLike: Введите положительный id.");
        }
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        userService.findUserById(userId);
        if (id < 0) {
            throw new ValidationException(" deleteLike: Введите положительный id.");
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
