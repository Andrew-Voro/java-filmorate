package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Email String email;
    @EqualsAndHashCode.Exclude
    Integer id;
    @EqualsAndHashCode.Exclude
    String login;
    @EqualsAndHashCode.Exclude
    String name;
    @EqualsAndHashCode.Exclude
    LocalDate birthday;
    @EqualsAndHashCode.Exclude
    Set<Integer> friends;


    public Map<String, Object> toMap() { //new
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", email);
        values.put("USER_ID", id);
        values.put("USER_NAME", name);
        values.put("LOGIN", login);
        values.put("BIRTHDAY", birthday);
        return values;
    }

}
