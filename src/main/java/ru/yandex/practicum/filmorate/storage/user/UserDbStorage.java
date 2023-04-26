package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Component("userDbStorage")
@RequiredArgsConstructor
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public User findUserById(Integer id) {

        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from USERS where USER_ID = ?", id);


        if (userRows.next()) {
            User user = User.builder().email(userRows.getString("email"))
                    .id(userRows.getInt("user_id"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("user_name"))
                    .birthday(userRows.getDate("birthday").toLocalDate())
                    .build();

            return user;
        } else {
            throw new ObjectNotFoundException("Пользователь  c id = " + id + " не найден.");

        }
    }


    public User create(User user) { //new
        InMemoryUserStorage.validation(user, "Create");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());

        return user;
    }


    @Override
    public User put(User user) {
        InMemoryUserStorage.validation(user, "Create");
        findUserById(user.getId());
        String sqlQuery = "update USERS set " +
                "user_name = ?,  login = ?, birthday = ?,email = ? " +
                "where user_id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getEmail(),
                user.getId());

        return user;
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "select user_id, user_name, login, email,birthday from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Map<Integer, User> getUsers() {
        Map<Integer, User> map = new HashMap<>();
        map.put(1, User.builder().build());
        return map;
    }

    @Override
    public void delete(Integer id) {
        findUserById(id);
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .name(resultSet.getString("user_name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

}
