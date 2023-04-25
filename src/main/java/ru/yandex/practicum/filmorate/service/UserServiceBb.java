package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class UserServiceBb implements UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User findUserById(Integer id) { //new
        return userStorage.findUserById(id);
    }

    public User addFriend(Integer userId, Integer friendId) {
        userStorage.findUserById(userId);
        userStorage.findUserById(friendId);
        return friendStorage.addFriend(userId, friendId);
    }

    public User deleteFriend(Integer userId, Integer friendId) {
        userStorage.findUserById(userId);
        userStorage.findUserById(friendId);
        return friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        userStorage.findUserById(userId);
        return friendStorage.getFriends(userId);

    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userStorage.findUserById(id);
        userStorage.findUserById(otherId);
        Set<User> friends = new HashSet<>(getFriends(id));
        Set<User> friendsOther = new HashSet<>(getFriends(otherId));
        friends.retainAll(friendsOther);
        return friends.stream().collect(Collectors.toList());
    }
}
