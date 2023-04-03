package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.List;



@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userStorage.put(user);
    }


    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new ValidationException("getUser: Введите положительный id.");
        }
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("getUser: Юзера c id = " + id + " нет.");
        }
        return userStorage.getUsers().get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new ValidationException("delete: Введите положительный id.");
        }
        userStorage.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        if (id < 0) {
            throw new ValidationException("addFriend: Введите положительный id.");
        }
        if (id < 0) {
            throw new ValidationException("addFriend: Введите положительный friendId.");
        }
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        if (id < 0) {
            throw new ValidationException("deleteFriend: Введите положительный id.");
        }
        if (id < 0) {
            throw new ValidationException("deleteFriend: Введите положительный friendId.");
        }
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new ValidationException("getFriends: Введите положительный id.");
        }
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        if (id < 0) {
            throw new ValidationException("getCommonFriends: Введите положительный id.");
        }
        if (otherId < 0) {
            throw new ValidationException("getCommonFriends: Введите положительный otherId.");
        }
        return userService.getCommonFriends(id, otherId);
    }

}

