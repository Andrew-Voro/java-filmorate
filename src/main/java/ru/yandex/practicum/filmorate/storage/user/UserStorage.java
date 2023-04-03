package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User create(User user);

    User put(User user);

    Collection<User> findAll();

    Map<Integer, User> getUsers();

    void delete(Integer id);
}
