package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User user;
    private User friend;
    private FriendshipStatus status;

    public Friendship(User user, User friend, FriendshipStatus status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
    }
}