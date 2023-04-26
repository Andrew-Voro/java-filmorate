package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.MpaGenreStorage;

import java.util.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class MpaGenreController {
    private final MpaGenreStorage mpaGenreStorage;

    @GetMapping("/genres")
    public Collection<Genre> findAllGenre() {
        return mpaGenreStorage.findAllGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable("id") Integer id) {
        return mpaGenreStorage.findGenreById(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> findAllMpa() {
        return mpaGenreStorage.findAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Mpa findMpaById(@PathVariable("id") Integer id) {
        return mpaGenreStorage.findMpaById(id);
    }

}
