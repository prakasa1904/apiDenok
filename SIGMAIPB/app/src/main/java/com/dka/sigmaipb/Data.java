package com.dka.sigmaipb;

/**
 * Created by prakasa on 07/01/16.
 */
public class Data {

    private String name;
    private String merk;
    private String image;

    public Data() {
        // TODO Auto-generated constructor stub
    }

    public Data(String name, String merk, String image) {
        super();
        this.name = name;
        this.merk = merk;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
