package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Integer userId, Integer friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("addFriend: Нет юзера с userId = " + userId);
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new ObjectNotFoundException("addFriend: Нет юзера с friendId = " + friendId);
        }
        try {
            userStorage.getUsers().get(userId).getFriends().add(friendId);
        } catch (Throwable e) {
            userStorage.getUsers().get(userId).setFriends(new HashSet<>());
            userStorage.getUsers().get(userId).getFriends().add(friendId);
        }

        try {
            userStorage.getUsers().get(friendId).getFriends().add(userId);
            return userStorage.getUsers().get(userId);
        } catch (Throwable e) {
            userStorage.getUsers().get(friendId).setFriends(new HashSet<>());
            userStorage.getUsers().get(friendId).getFriends().add(userId);
            return userStorage.getUsers().get(userId);
        }

    }

    public User deleteFriend(Integer userId, Integer friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("deleteFriend: Нет юзера с userId = " + userId);
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new ObjectNotFoundException("deleteFriend: Нет юзера с friendId = " + friendId);
        }
        try {
            userStorage.getUsers().get(userId).getFriends().remove(friendId);
            userStorage.getUsers().get(friendId).getFriends().remove(userId);
            return userStorage.getUsers().get(userId);
        } catch (Throwable e) {
            userStorage.getUsers().get(userId).setFriends(new HashSet<>());
            userStorage.getUsers().get(friendId).setFriends(new HashSet<>());
            return userStorage.getUsers().get(userId);
        }

    }

    public List<User> getFriends(Integer userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new ObjectNotFoundException("getFriends: Нет юзера с id = " + userId);
        }
        List<User> friends = new ArrayList<>();

        try {
            Set<Integer> friendsId = new HashSet<>(userStorage.getUsers().get(userId).getFriends());
            if (friendsId.size() == 0) {
                throw new ObjectNotFoundException("getFriends: У юзера с id = " + userId + "нет друзей");
            }
            for (Integer fId : friendsId) {
                if (userStorage.getUsers().containsKey(fId)) {
                    friends.add(userStorage.getUsers().get(fId));
                }
            }
            return friends;
        } catch (Throwable e) {
            return friends;
        }

    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("getCommonFriends: Нет юзера с userId = " + id);
        }
        if (!userStorage.getUsers().containsKey(otherId)) {
            throw new ObjectNotFoundException("getCommonFriends: Нет юзера с otherId = " + otherId);
        }
        List<User> commonFriends = new ArrayList<>();
        try {
            Set<Integer> friendsId = userStorage.getUsers().get(id).getFriends();
            Set<Integer> friendsOtherId = userStorage.getUsers().get(otherId).getFriends();
            friendsId.retainAll(friendsOtherId);
            if (friendsId.size() > 0) {
                for (Integer fId : friendsId) {
                    if (userStorage.getUsers().containsKey(fId)) {
                        commonFriends.add(userStorage.getUsers().get(fId));
                    }
                }
            }
        } catch (Throwable e) {
            return commonFriends;
        }
        return commonFriends;
    }

}
