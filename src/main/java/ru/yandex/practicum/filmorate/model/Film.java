package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @EqualsAndHashCode.Exclude
    ArrayList<Genre> genres;
    @EqualsAndHashCode.Exclude
    Mpa mpa;
    String name;
    LocalDate releaseDate;
    @EqualsAndHashCode.Exclude
    Integer id;
    @EqualsAndHashCode.Exclude
    String description;
    @EqualsAndHashCode.Exclude
    Integer duration;
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    Set<Integer> likes;
    @EqualsAndHashCode.Exclude
    Integer rate;


    public Map<String, Object> toMap() { //new
        Map<String, Object> values = new HashMap<>();
        values.put("RELEASE_DATE", releaseDate);
        values.put("FILM_NAME", name);
        values.put("DURATION", duration);
        values.put("DESCRIPTION", description);
        values.put("MPA", mpa.getId());
        values.put("FILM_ID", id);
        values.put("RATE", rate);

        return values;
    }


}
