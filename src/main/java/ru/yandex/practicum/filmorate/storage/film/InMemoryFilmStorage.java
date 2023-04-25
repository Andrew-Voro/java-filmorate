package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.FilmIdGenreId;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static int idCounter;
    private final Map<Integer, Film> films = new HashMap<>();

    public Film create(Film film) {
        validation(film, "POST");
        if (!films.containsValue(film.hashCode())) {
            film.setId(++idCounter);
            films.put(film.getId(), film);
            log.debug("POST: Фильм {} выпущенный {} внесен в список", film.getName(), film.getReleaseDate());
        } else {
            log.debug("POST: ValidationException, фильм уже внесен в коллекцию");
            throw new ValidationException("Фильм уже внесен в коллекцию");
        }
        return film;
    }

    @Override
    public Film findFilmById(Integer id) {
        return films.get(id);
    }

    public Film put(Film film) {
        validation(film, "Put");
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.debug("PUT: ValidationException фильм c id = {}  отсутствует в базе.", film.getId());
            throw new ObjectNotFoundException("PUT: ObjectNotFoundException фильм c  id = " + film.getId() +
                    " отсутствует в базе. ");
        } else {
            films.put(film.getId(), film);
            log.debug("PUT: Фильм  c id = {} ", film.getId());
        }
        return film;
    }


    public Collection<Film> findAll() {
        return films.values();
    }

    public List<Film> findPopular(Integer count) {
        for (Film film : films.values()
        ) {
            try {
                film.getLikes().size();
            } catch (Throwable e) {
                film.setLikes(new HashSet<>() {
                });
            }
        }
        return films.values().stream().sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size()) //likes
                .limit(count).collect(Collectors.toList());
    }

    public void delete(Integer id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new ValidationException("Delete: ValidationException фильм c id = " + id +
                    " отсутствует в базе. ");
        }

    }

    static void validation(Film film, String request) {
        if (film == null) {
            log.debug(request + ": ValidationException, тело запроса не может быть пустым.");
            throw new ValidationException("Тело запроса не может быть пустым.");
        }
        try {
            if (film.getName().equals(null) || film.getName().isBlank()) {
                log.debug(request + ": ValidationException, название фильма не может быть пустым.");
                throw new ValidationException("Название фильма не может быть пустым.");
            }
        } catch (NullPointerException e) {
            log.debug(request + ": ValidationException, название фильма не может быть пустым.");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.debug(request + ": ValidationException, максимальная длина описания фильма {} — 200 символов.",
                    film.getName());
            throw new ValidationException("Максимальная длина описания фильма — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug(request + ": ValidationException, дата релиза фильмы {} — не раньше 28 декабря 1895 года.",
                    film.getName());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.debug(request + ": ValidationException, продолжительность фильма {} должна быть положительной.",
                    film.getName());
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }

    }

    public Collection<FilmIdGenreId> findAllFilmIdGenreId() {
        List<FilmIdGenreId> list = new ArrayList();
        FilmIdGenreId filmIdGenreId = FilmIdGenreId.builder().build();
        list.add(filmIdGenreId);
        return list;

    }

    public Collection<Genre> findAllGenre() {
        List<Genre> list = new ArrayList();
        Genre genre = Genre.builder().build();
        list.add(genre);
        return list;
    }

    public Map<Integer, Genre> getGenres() {
        Map<Integer, Genre> map = new HashMap<>();
        Genre genre = Genre.builder().build();
        map.put(1, genre);
        return map;
    }

    @Override
    public void addLike(Integer id, Integer userId) {

    }

    @Override
    public void deleteLike(Integer id, Integer userId) {

    }

    public Collection<Film> findAllPopular() {
        return new ArrayList<>();
    }
}
