package com.example.demo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Entity
@Table(name = "RECORDS")
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "ARTIST")
    private String artist;

    @Column(name = "ALBUM")
    private String album;

    @Column(name = "URLDISCOGS")
    private String urldiscogs;

    @Column(name = "LOWERPRICE")
    private int lowerprice;

    @Column(name = "MEDIANPRICE")
    private int medianprice;

    @Column(name = "HIGHERPRICE")
    private int higherprice;

    @Column(name = "HAVE")
    private int have;

    @Column(name = "WANT")
    private int want;

    @Column(name = "STYLE")
    private String style;

    @Column(name = "RATE")
    private int rate;

    @Column(name = "RATERS")
    private String raters;


    public Record() {}


    public Record(Long id, String category, String title, String artist, String album, String urldiscogs, int lowerprice, int medianprice, int higherprice, int have, int want, String style, int rate, String raters) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.urldiscogs = urldiscogs;
        this.lowerprice = lowerprice;
        this.medianprice = medianprice;
        this.higherprice = higherprice;
        this.have = have;
        this.want = want;
        this.style = style;
        this.rate = rate;
        this.raters = raters;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUrldiscogs() {
        return urldiscogs;
    }

    public void setUrldiscogs(String urldiscogs) {
        this.urldiscogs = urldiscogs;
    }

    public int getLowerprice() {
        return lowerprice;
    }

    public void setLowerprice(int lowerprice) {
        this.lowerprice = lowerprice;
    }

    public int getMedianprice() {
        return medianprice;
    }

    public void setMedianprice(int medianprice) {
        this.medianprice = medianprice;
    }

    public int getHigherprice() {
        return higherprice;
    }

    public void setHigherprice(int higherprice) {
        this.higherprice = higherprice;
    }

    public int getHave() {
        return have;
    }

    public void setHave(int have) {
        this.have = have;
    }

    public int getWant() {
        return want;
    }

    public void setWant(int want) {
        this.want = want;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getRaters() {
        return raters;
    }

    public void setRaters(String raters) {
        this.raters = raters;
    }
}


