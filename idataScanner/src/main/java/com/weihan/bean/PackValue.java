package com.weihan.bean;

public class PackValue {
    private String LittleBarcode;
    private String BigBarcode;
    private String LittleType;
    private String BigType;
    private boolean Enabled;
    private String ETag;

    public boolean isEnabled() {
        return Enabled;
    }

    public void setEnabled(boolean enabled) {
        Enabled = enabled;
    }

    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
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
