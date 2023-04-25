package ru.yandex.practicum.filmorate.storage.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    void findUserById() {
        User user = User.builder().id(1).email("mail@mail.ru").name("Lucy").login("Lucy")
                .birthday(LocalDate.of(2020, 02, 03)).build();
        userStorage.create(user);
        userStorage.findUserById(1);
        assertThat(user).hasFieldOrPropertyWithValue("id", 1);
        assertThat(user).hasFieldOrPropertyWithValue("name", "Lucy");
        userStorage.delete(user.getId());
    }

    @Test
    void create() {
        User user = User.builder()
                .name("TestUser")
                .email("TestUser@gg.ru")
                .birthday(LocalDate.of(1990, 12, 1))
                .login("login")
                .build();
        User userCreated = userStorage.create(user);
        User userFromDb = userStorage.findUserById(userCreated.getId());
        assertThat(userFromDb).hasFieldOrPropertyWithValue("name", "TestUser");
        userStorage.delete(userCreated.getId());
    }

    @Test
    void put() {
        User user = User.builder()
                .name("TestUser")
                .email("TestUser@gg.ru")
                .birthday(LocalDate.of(1990, 12, 1))
                .login("login")
                .build();
        User userCreated = userStorage.create(user);
        User user2 = userStorage.findUserById(1);
        assertThat(user2).hasFieldOrPropertyWithValue("name", "TestUser");
        user2.setName("TestUserUpdated");
        userStorage.put(user2);
        User userUpdated = userStorage.findUserById(1);
        assertThat(userUpdated).hasFieldOrPropertyWithValue("name", "TestUserUpdated");
        userStorage.delete(userCreated.getId());
    }

    @Test
    void findAll() {
        User user = User.builder()
                .name("TestUser")
                .email("TestUser@gg.ru")
                .birthday(LocalDate.of(1990, 12, 1))
                .login("login")
                .build();
        User userCreated = userStorage.create(user);
        Collection<User> users = userStorage.findAll();
        assertThat(users.size()).isEqualTo(1);
        userStorage.delete(userCreated.getId());
    }

}