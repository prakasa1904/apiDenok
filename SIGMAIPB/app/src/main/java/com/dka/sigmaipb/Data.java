package com.dka.sigmaipb;

// Created by prakasa on 07/01/16.

class Data {

    private String name;
    private String tahun;
    private String wing;
    private String merk;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getMerk() {
        return merk;
    }

    void setMerk(String merk) {
        this.merk = merk;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun= tahun;
    }

    public String getWing() {
        return wing;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
