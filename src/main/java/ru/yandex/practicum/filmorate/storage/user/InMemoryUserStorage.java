package ru.yandex.practicum.filmorate.storage.user;


import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static int idCounter;
    private final Map<Integer, User> users = new HashMap<>();


    public User create(User user) {
        validation(user, "POST");

        if (!users.containsValue(user.hashCode())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(++idCounter);
            users.put(user.getId(), user);
            log.debug("POST: Пользователь {} с электронной почтой {} зарегистрирован. ", user.getName()
                    , user.getEmail());
        } else {
            log.debug("POST: ValidationException пользователь {} с электронной почтой {} ранее зарегистрирован. "
                    , user.getName(), user.getEmail());
            throw new ValidationException("Пользователь " + user.getName() + " с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        return user;
    }


    public User put(User user) {

        validation(user, "PUT");
        if (user.getId() == null || !users.containsKey(user.getId())) {
            log.debug("PUT: ObjectNotFoundException пользователь c id = {}  отсутствует в базе.", user.getId());
            throw new ObjectNotFoundException("PUT: ObjectNotFoundException пользователь c  id = " + user.getId() +
                    " отсутствует в базе. ");
        } else {
            users.put(user.getId(), user);
            log.debug("PUT: Данные пользователя  с id = {}  обновлены."
                    , user.getId());
        }

        return user;
    }


    public Collection<User> findAll() {
        return users.values();
    }

    public void delete(Integer id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new ValidationException("Delete: ValidationException пользователь c  id = " + id +
                    " отсутствует в базе. ");
        }

    }

    private void validation(User user, String request) {

        if (user == null) {
            log.debug(request + ": ValidationException, тело запроса не может быть пустым.");
            throw new ValidationException("Тело запроса не может быть пустым.");
        }


        try {
            if (user.getEmail().equals(null) || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
                log.debug(request + ": ValidationException, электронная почта не может" +
                        " быть пустой и должна содержать символ @.");
                throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
            }
        } catch (NullPointerException e) {
            log.debug(request + ": ValidationException, электронная почта не может" +
                    " быть пустой и должна содержать символ @.");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }

        try {
            if (user.getLogin().equals(null) || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                log.debug(request + ": ValidationException, Логин пользователя с электронной почтой {}" +
                        " не может быть пустым и содержать пробелы.", user.getEmail());
                throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
            }
        } catch (NullPointerException e) {
            log.debug(request + ": ValidationException, Логин пользователя с электронной почтой {}" +
                    " не может быть пустым и содержать пробелы.", user.getEmail());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }


        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug(request + ": Дата рождения пользователя с электронной почтой {} не может быть в будущем."
                    , user.getEmail());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

}
