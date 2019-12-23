package com.example.demo;

public class Record {



    String title;
    String artist;
    String album;
    int lowerPrice;
    int medianPrice;
    int higherPrice;
    int ratio;
    int have;
    int want;
    String urlDiscogs;


    public String getUrlDiscogs() {
        return urlDiscogs;
    }

    public void setUrlDiscogs(String urlDiscogs) {
        this.urlDiscogs = urlDiscogs;
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
    public int getLowerPrice() {
        return lowerPrice;
    }
    public void setLowerPrice(int lowerPrice) {
        this.lowerPrice = lowerPrice;
    }
    public int getMedianPrice() {
        return medianPrice;
    }
    public void setMedianPrice(int medianPrice) {
        this.medianPrice = medianPrice;
    }
    public int getHigherPrice() {
        return higherPrice;
    }
    public void setHigherPrice(int higherPrice) {
        this.higherPrice = higherPrice;
    }
    public int getRatio() {
        return ratio;
    }
    public void setRatio(int ratio) {
        this.ratio = ratio;
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
}
