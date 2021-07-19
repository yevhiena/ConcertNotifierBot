package com.alevel.concertnotifierbot.model.dto;


import java.time.Instant;
import java.util.Date;

public class ConcertDto {

    private long id;
    private String artist;
    private Date date;
    private String sourceUrl;
    private String address;

    private String price;

    public ConcertDto(long id, String artist, Date date, String sourceUrl, String address, String price) {
        this.id = id;
        this.artist = artist;
        this.date = date;
        this.sourceUrl = sourceUrl;
        this.address = address;
        this.price = price;
    }
    public  ConcertDto(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Date getDate() {
        return date;
    }
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ConcertDto{" +
                "artist='" + artist + '\'' +
                ", date=" + date +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", address='" + address + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
