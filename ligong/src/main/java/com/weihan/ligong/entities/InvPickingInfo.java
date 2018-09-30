/**
 * Copyright 2018 bejson.com
 */
package com.weihan.ligong.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Auto-generated: 2018-09-30 16:20:1
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class InvPickingInfo {

    @SerializedName("odata.etag")
    private String odata_etag;

    private String Inv_Document_Type;
    private String Inv_Document_No;
    private int Line_No;
    private String Item_No;
    private String Description;
    private String Quantity_Base;
    private String ETag;

    public String getOdata_etag() {
        return odata_etag;
    }

    public void setOdata_etag(String odata_etag) {
        this.odata_etag = odata_etag;
    }

    public String getInv_Document_Type() {
        return Inv_Document_Type;
    }

    public void setInv_Document_Type(String Inv_Document_Type) {
        this.Inv_Document_Type = Inv_Document_Type;
    }

    public String getInv_Document_No() {
        return Inv_Document_No;
    }

    public void setInv_Document_No(String Inv_Document_No) {
        this.Inv_Document_No = Inv_Document_No;
    }

    public int getLine_No() {
        return Line_No;
    }

    public void setLine_No(int Line_No) {
        this.Line_No = Line_No;
    }

    public String getItem_No() {
        return Item_No;
    }

    public void setItem_No(String Item_No) {
        this.Item_No = Item_No;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getQuantity_Base() {
        return Quantity_Base;
    }

    public void setQuantity_Base(String Quantity_Base) {
        this.Quantity_Base = Quantity_Base;
    }

    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }

}