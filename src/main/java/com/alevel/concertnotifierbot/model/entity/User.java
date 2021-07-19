package com.alevel.concertnotifierbot.model.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "chat_id", nullable = false, updatable = false, unique = true)
    private long chatId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant created;

    @Column(name = "token", nullable = false)
    private String refreshToken;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "user_concert",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "concert_id"))
    private Set<Concert> concerts = new HashSet<>();

    public User(long chatId, String refreshToken) {
        this.chatId = chatId;
        this.refreshToken = refreshToken;
    }

    public User(){}

    @PrePersist
    public void onCreate(){
        this.created = Instant.now();
    }



}
