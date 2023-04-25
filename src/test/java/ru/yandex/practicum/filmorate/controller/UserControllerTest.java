package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {


    @Autowired
    private UserController controller;


    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }


    @Test
    public void nullOrIncorrectMail() {
        User user = User.builder().birthday(LocalDate.of(1907, 12, 12)).email(null)
                .id(0).login("Mat").name("Mattue").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(user);
                    }
                });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @."
                , exception.getMessage());

    }

    @Test
    public void nullUser() {
        User user = null;
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(user);
                    }
                });
        assertEquals("Тело запроса не может быть пустым."
                , exception.getMessage());

    }

    @Test
    public void nullLogin() {
        User user = User.builder().birthday(LocalDate.of(1907, 12, 12)).email("udo@mail.ru")
                .id(0).login(null).name("Mattue").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(user);
                    }
                });
        assertEquals("Логин не может быть пустым и содержать пробелы."
                , exception.getMessage());

    }

    @Test
    public void blankOrWithSpaceLogin() {
        User user = User.builder().birthday(LocalDate.of(1907, 12, 12)).email("udo@mail.ru")
                .id(0).login(" ").name("Mattue").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(user);
                    }
                });
        assertEquals("Логин не может быть пустым и содержать пробелы."
                , exception.getMessage());

    }


    @Test
    public void futureDateOfBirthday() {
        User user = User.builder().birthday(LocalDate.of(2107, 12, 12)).email("udo@mail.ru")
                .id(0).login("Matt").name("Mattue").build();
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        // здесь блок кода, который хотим проверить
                        controller.create(user);
                    }
                });
        assertEquals("Дата рождения не может быть в будущем."
                , exception.getMessage());

    }

}