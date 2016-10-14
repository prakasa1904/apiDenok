package com.dka.sigmaipb;

/**
 * Created by prakasa on 07/01/16.
 */
public class Data {

    private String name;
    private String tahun;
    private String wing;
    private String merk;
    private String image;

    public Data() {
        // TODO Auto-generated constructor stub
    }

    public Data(String name, String merk, String image, String tahun, String wing) {
        super();
        this.name = name;
        this.merk = merk;
        this.tahun = tahun;
        this.wing = wing;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
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
