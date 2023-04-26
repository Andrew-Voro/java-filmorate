package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    @Autowired
    private final UserDbStorage userStorage;

    @Test
    void findUserById() {
        User u = User.builder().id(1).email("mail@mail.ru").name("Lucy").login("Lucy")
                .birthday(LocalDate.of(2020, 02, 03)).build();
        userStorage.create(u);
        userStorage.findUserById(1);
        assertThat(u).hasFieldOrPropertyWithValue("id", 1);
        assertThat(u).hasFieldOrPropertyWithValue("name", "Lucy");
        userStorage.delete(u.getId());
    }

}