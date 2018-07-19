package com.weihan.bean;

import com.google.gson.Gson;

public class PackTag {
    private String LittleBarcode;
    private String BigBarcode;
    private String LittleType;
    private String BigType;

    public PackTag(String littleBarcode, String bigBarcode, String littleType, String bigType) {
        LittleBarcode = littleBarcode;
        BigBarcode = bigBarcode;
        LittleType = littleType;
        BigType = bigType;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getLittleBarcode() {
        return LittleBarcode;
    }

    public void setLittleBarcode(String littleBarcode) {
        LittleBarcode = littleBarcode;
    }

    public String getBigBarcode() {
        return BigBarcode;
    }

    public void setBigBarcode(String bigBarcode) {
        BigBarcode = bigBarcode;
    }

    public String getLittleType() {
        return LittleType;
    }

    public void setLittleType(String littleType) {
        LittleType = littleType;
    }

    public String getBigType() {
        return BigType;
    }

    public void setBigType(String bigType) {
        BigType = bigType;
    }
}
