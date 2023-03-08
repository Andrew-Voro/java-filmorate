package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class Film {

    @EqualsAndHashCode.Exclude Integer id;
    String name;
    @EqualsAndHashCode.Exclude String description;
    LocalDate releaseDate;
    @EqualsAndHashCode.Exclude int duration;

}
