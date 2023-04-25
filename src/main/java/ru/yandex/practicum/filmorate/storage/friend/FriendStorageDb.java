package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class FriendStorageDb implements FriendStorage {


    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userDbStorage;

    @Override
    public List<User> getFriends(Integer userId) {

        String sql = "select * from FRIENDS where U_ID = ?";
        List<Friend> friends = jdbcTemplate.query(sql, (rs, rowNum) -> makeFriend(rs), userId);

        Set<User> listFriends = friends.stream()
                .map(Friend::getFriendid)
                .map(userDbStorage::findUserById)
                .collect(Collectors.toSet());

        if (listFriends.isEmpty()) {
            return Collections.emptyList();
        }
        return listFriends.stream().collect(Collectors.toList());
    }

    private Friend makeFriend(ResultSet rs) throws SQLException {
        return Friend.builder().id(rs.getInt("u_id"))
                .friendid(rs.getInt("friend_id")).build();
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        Friend friend = Friend.builder().id(userId).friendid(friendId).build();
        String sqlQuery = "insert into FRIENDS(U_ID, FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                friend.getId(),//меняем местами ??
                friend.getFriendid());

        return userDbStorage.findUserById(friendId);
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "delete from FRIENDS where U_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return userDbStorage.findUserById(friendId);
    }
}


