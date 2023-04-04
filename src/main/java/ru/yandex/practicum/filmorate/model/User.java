package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class User {
    @Email String email;
    @EqualsAndHashCode.Exclude //доброго времени суток,Давид, я немного не понял, что конкретно нужно поправить, надеюсь сделал правльно.
    Integer id;
    @EqualsAndHashCode.Exclude
    String login;
    @EqualsAndHashCode.Exclude
    String name;
    @EqualsAndHashCode.Exclude
    LocalDate birthday;
    @EqualsAndHashCode.Exclude
    Set<Integer> friends;
}
