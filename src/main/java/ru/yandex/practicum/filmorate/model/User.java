package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    @EqualsAndHashCode.Exclude
    Integer id;
    @Email String email;
    @EqualsAndHashCode.Exclude
    String login;
    @EqualsAndHashCode.Exclude
    String name;
    @EqualsAndHashCode.Exclude
    LocalDate birthday;
    @EqualsAndHashCode.Exclude
    Set<Integer> friends;
}
