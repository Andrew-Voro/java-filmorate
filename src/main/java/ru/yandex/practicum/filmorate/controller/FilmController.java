package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static int idCounter;
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validation(film,"POST");

        if (!films.containsValue(film.hashCode())) {
            film.setId(++idCounter);
            films.put(film.getId(), film);
            log.debug("POST: Фильм {} выпущенный {} внесен в список",film.getName(),film.getReleaseDate());
        } else {
            log.debug("POST: ValidationException, фильм уже внесен в коллекцию");
            throw new ValidationException("Фильм уже внесен в коллекцию");
        }
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        validation(film,"Put");
        if(film.getId() == null || !films.containsKey(film.getId())){
            log.debug("PUT: ValidationException фильм c id = {}  отсутствует в базе.", film.getId());
            throw new ValidationException("PUT: ValidationException фильм c  id = " + film.getId() +
                    " отсутствует в базе. ");
        }
        else {
            films.put(film.getId(), film);
            log.debug("PUT: Фильм  c id = {} ",film.getId());
        }

        return film;
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    private void validation(Film film, String request) {
        if(film==null) {
            log.debug(request+": ValidationException, тело запроса не может быть пустым.");
            throw new ValidationException("Тело запроса не может быть пустым.");
        }
        try {
        if (film.getName().equals(null) || film.getName().isBlank()) {
            log.debug(request+": ValidationException, название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        } catch (NullPointerException e) {
            log.debug(request+": ValidationException, название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.debug(request+": ValidationException, максимальная длина описания фильма {} — 200 символов."
                    ,film.getName());
            throw new ValidationException("Максимальная длина описания фильма — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug(request+": ValidationException, дата релиза фильмы {} — не раньше 28 декабря 1895 года."
                    ,film.getName());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.debug(request+": ValidationException, продолжительность фильма {} должна быть положительной.",
                    film.getName());
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }

    }

}
