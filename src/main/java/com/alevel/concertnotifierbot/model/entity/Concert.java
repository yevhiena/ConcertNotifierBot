package com.alevel.concertnotifierbot.model.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "artist", nullable = false)
    private String artist;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "place", nullable = false)
    private  String place;

    @Column(name = "url", nullable = false)
    private String url;


    @ManyToMany(mappedBy = "concerts", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    Set<User> users = new HashSet<>();

    public Concert(long id, String artist, Date date, String price, String place, String url) {
        this.id = id;
        this.artist = artist;
        this.date = date;
        this.price = price;
        this.place = place;
        this.url = url;
    }

    public Concert(){}
}
