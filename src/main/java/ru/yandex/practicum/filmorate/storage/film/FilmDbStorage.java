package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmIdGenreId;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Primary
public class FilmDbStorage implements FilmStorage, MpaGenreStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;


    public Film create(Film film) {
        InMemoryFilmStorage.validation(film, "Create");
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());

        TreeSet<Genre> genreTree = new TreeSet<>(new Comparator<Genre>() {
            @Override
            public int compare(Genre s1, Genre s2) {
                return s1.getId() - s2.getId();
            }
        });
        try {

            genreTree.addAll(film.getGenres());
            List<Genre> genresList = new ArrayList<>(genreTree);
            if (genresList.size() > 0) {
                batch(genresList, film.getId(), "insert into FILM_GENRE(GENRE_ID,FILM_ID ) values (?, ?)");
            }
            return film;
        } catch (NullPointerException e) {
            return film;
        }

    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "select * from Films JOIN MPA  ON Films.mpa=MPA.mpa_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Collection<Film> findAllPopular() {
        String sqlQuery = "select * from Films JOIN MPA  ON Films.mpa=MPA.mpa_id ORDER BY Films.RATE DESC ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }


    public Collection<FilmIdGenreId> findAllFilmIdGenreId() {
        String sqlQuery = "select * from FILM_GENRE";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmIdGenreId);
    }


    public Map<Integer, Genre> getGenres() {
        Map<Integer, Genre> map = new HashMap<>();
        for (Genre genre : findAllGenre()) {
            map.put(genre.getId(), genre);
        }
        return map;
    }


    public List<Film> findPopular(Integer count) {
        List<Film> list = new ArrayList<>();
        list.add(Film.builder().build());
        return list;
    }

    public void delete(Integer id) {
        String sQuery = "delete from FILMS where  FILM_ID = ?";
        jdbcTemplate.update(sQuery, id);
    }

    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> map = new HashMap<>();
        for (Film film : findAll()) {
            map.put(film.getId(), film);
        }
        return map;
    }

    private FilmIdGenreId mapRowToFilmIdGenreId(ResultSet resultSet, int rowNum) throws SQLException {
        FilmIdGenreId filmIdGenreId = FilmIdGenreId.builder()
                .filmId(resultSet.getInt("film_id"))
                .genreId(resultSet.getInt("genre_id"))
                .build();
        return filmIdGenreId;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
        return genre;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
        return mpa;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("film_name"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .build();
        film.setMpa(Mpa.builder().id(resultSet.getInt("mpa")).name(resultSet.getString("mpa_name")).build());

        return film;
    }


    @Override
    public Film put(Film film) {
        InMemoryFilmStorage.validation(film, "Put");
        Integer f_id = film.getId();
        findFilmById(f_id);
        String sqlQuery = "update FILMS set " +
                "FILM_NAME = ?,   RELEASE_DATE = ?,DESCRIPTION = ? ,MPA =? ,RATE = ? ,DURATION = ? " +
                "where FILM_ID = ?";

        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getReleaseDate()
                , film.getDescription()
                , film.getMpa().getId()
                , film.getRate()
                , film.getDuration()
                , film.getId());
        TreeSet<Genre> genreTree = new TreeSet<>(new Comparator<Genre>() {
            @Override
            public int compare(Genre s1, Genre s2) {
                return s1.getId() - s2.getId();
            }
        });

        List<Genre> genresList = new ArrayList<>();
        try {
            genreTree.addAll(film.getGenres());
            genresList.addAll(genreTree);
            if (findAllFilmIdGenreIdIdFilm(f_id).size() > 0) {
                if (film.getGenres().size() == 0) {
                    String sQuery = "delete from FILM_GENRE where  FILM_ID = ?";
                    jdbcTemplate.update(sQuery, film.getId());

                } else {
                    String sQuery = "delete from FILM_GENRE where  FILM_ID = ?";
                    jdbcTemplate.update(sQuery, film.getId());

                    batch(genresList, film.getId(), "insert into FILM_GENRE(GENRE_ID, FILM_ID) values (?, ?)");
                }
            } else {
                batch(genresList, film.getId(), "insert into FILM_GENRE(GENRE_ID, FILM_ID) values (?, ?)");
            }
            return film;
        } catch (NullPointerException e) {
            genresList.addAll((genreTree));
            batch(genresList, film.getId(), "insert into FILM_GENRE(GENRE_ID, FILM_ID) values (?, ?)");
            return film;
        }
    }

    public Collection<FilmIdGenreId> findAllFilmIdGenreIdIdFilm(Integer filmId) {
        String sqlQuery = "select * from FILM_GENRE where FILM_ID = " + filmId;
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmIdGenreId);
    }

    private int[] batch(final List<Genre> genres, int film_id, String requestDb) {
        return this.jdbcTemplate.batchUpdate(requestDb
                ,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Genre genre = genres.get(i);
                        ps.setInt(1, genre.getId());
                        ps.setInt(2, film_id);
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });

    }


    @Override
    public Film findFilmById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILMS JOIN MPA  ON Films.mpa=MPA.mpa_id where FILM_ID = ?", id);


        if (filmRows.next()) {
            Film film = Film.builder().id(filmRows.getInt("FILM_ID"))
                    .name(filmRows.getString("FILM_NAME"))
                    .duration(filmRows.getInt("DURATION"))
                    .description(filmRows.getString("DESCRIPTION"))
                    .releaseDate(filmRows.getDate("RELEASE_DATE").toLocalDate())
                    .rate(filmRows.getInt("RATE"))
                    .build();
            film.setMpa(Mpa.builder().id(filmRows.getInt("mpa")).name(filmRows.getString("mpa_name")).build());

            log.info("Найден пользователь: {} {}", film.getId(), film.getName());

            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Фильм c id = " + id + " не найден.");
        }

    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into LIKES(USER_ID, FILM_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId, filmId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from LIKES where USER_ID = ? AND USER_ID =?";

        jdbcTemplate.update(sqlQuery,
                userId, filmId);
    }

    @Override
    public Collection<Genre> findAllGenre() {
        String sqlQuery = "select * from GENRE";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Genre findGenreById(Integer genreId) {

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from GENRE WHERE GENRE_ID = ?", genreId);

        if (genreRows.next()) {
            Genre genre = Genre.builder().id(genreRows.getInt("GENRE_ID"))
                    .name(genreRows.getString("GENRE_NAME"))
                    .build();

            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());

            return genre;
        } else {
            log.info("Жанр с идентификатором {} не найден.", genreId);
            throw new ObjectNotFoundException("Жанр c id = " + genreId + " не найден.");

        }


    }

    @Override
    public Collection<Mpa> findAllMpa() {
        String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Mpa findMpaById(Integer mpaID) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from MPA WHERE MPA_ID = ?", mpaID);

        if (mpaRows.next()) {
            Mpa mpa = Mpa.builder().id(mpaRows.getInt("MPA_ID"))
                    .name(mpaRows.getString("MPA_NAME"))
                    .build();

            log.info("Найден mpa: {} {}", mpa.getId(), mpa.getName());

            return mpa;
        } else {
            log.info("Mpa с идентификатором {} не найден.", mpaID);
            throw new ObjectNotFoundException("Mpa c id = " + mpaID + " не найден.");

        }
    }

}
