package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    String name;
    LocalDate releaseDate;
    @EqualsAndHashCode.Exclude
    Integer id;
    @EqualsAndHashCode.Exclude
    String description;
    @EqualsAndHashCode.Exclude
    int duration;
    @EqualsAndHashCode.Exclude
    Set<Integer> likes;

}
