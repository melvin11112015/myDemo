/**
 * Copyright 2018 bejson.com
 */
package com.weihan.ligong.entities;

import com.google.gson.annotations.SerializedName;

;

/**
 * Auto-generated: 2018-09-19 10:25:30
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class OutstandingPurchLineInfo {

    @SerializedName("odata.etag")
    private String odata_etag;

    private String Document_Type;
    private String Document_No;
    private int Line_No;
    private String No;
    private String Description;
    private String Outstanding_Quantity;
    private String ETag;

    public String getOdata_etag() {
        return odata_etag;
    }

    public String getDocument_Type() {
        return Document_Type;
    }

    public String getDocument_No() {
        return Document_No;
    }

    public int getLine_No() {
        return Line_No;
    }

    public String getNo() {
        return No;
    }

    public String getDescription() {
        return Description;
    }

    public String getOutstanding_Quantity() {
        return Outstanding_Quantity;
    }

    public String getETag() {
        return ETag;
    }
}