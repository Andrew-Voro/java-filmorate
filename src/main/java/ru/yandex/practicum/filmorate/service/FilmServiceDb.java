package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmIdGenreId;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Primary
@Component
@RequiredArgsConstructor
public class FilmServiceDb implements FilmService {


    private final FilmStorage filmStorage;

    @Override
    public Film addLike(Integer id, Integer userId) {
        filmStorage.addLike(id, userId);
        Film film = findFilmById(id);
        film.setRate(film.getRate() + 1);
        return put(film);

    }

    public Film deleteLike(Integer id, Integer userId) {
        filmStorage.deleteLike(id, userId);
        Film film = findFilmById(id);
        film.setRate(film.getRate() - 1);
        return put(film);

    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        Collection<Film> films = filmStorage.findAllPopular();
        Map<Integer, Genre> genres = filmStorage.getGenres();
        Collection<FilmIdGenreId> filmIdGenreIds = filmStorage.findAllFilmIdGenreId();
        for (Film film : films) {

            filmSetGenres(film, filmIdGenreIds, genres);
        }
        return films.stream().limit(count).collect(Collectors.toList());

    }

    private void filmSetGenres(Film film, Collection<FilmIdGenreId> filmIdGenreIds, Map<Integer, Genre> genres) {
        film.setGenres(new ArrayList<>());
        for (FilmIdGenreId fIdgId : filmIdGenreIds) {

            if (film.getId().equals(fIdgId.getFilm_Id())) {

                film.getGenres().add(genres.get(fIdgId.getGenre_Id()));
            }
        }

    }

    @Override
    public Film findFilmById(Integer id) {
        Map<Integer, Genre> genres = filmStorage.getGenres();
        Collection<FilmIdGenreId> filmIdGenreIds = filmStorage.findAllFilmIdGenreId();
        Film film = filmStorage.findFilmById(id);
        filmSetGenres(film, filmIdGenreIds, genres);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        Collection<Film> films = filmStorage.findAll();
        Map<Integer, Genre> genres = filmStorage.getGenres();
        Collection<FilmIdGenreId> filmIdGenreIds = filmStorage.findAllFilmIdGenreId();
        for (Film film : films) {
            filmSetGenres(film, filmIdGenreIds, genres);
        }
        return films;
    }

    @Override
    public Film put(Film film) {
        filmStorage.put(film);
        return findFilmById(film.getId());
    }

    @Override
    public Film create(Film film) {
        filmStorage.create(film);
        return findFilmById(film.getId());
    }


}
