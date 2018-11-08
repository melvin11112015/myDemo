/**
 * Copyright 2018 bejson.com
 */
package com.weihan.scanner.entities;


import com.google.gson.annotations.SerializedName;

/**
 * Auto-generated: 2018-10-23 15:26:36
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PhysicalInvtInfo {

    @SerializedName("odata_etag")
    private String odata_etag;
    private String Journal_Template_Name;
    private String Journal_Batch_Name;
    private int Line_No;
    private String Posting_Date;
    private String Item_No;
    private String Location_Code;
    private String Bin_Code;
    private String Qty_Calculated;
    private String Qty_Phys_Inventory;
    private String ETag;

    public String getOdata_etag() {
        return odata_etag;
    }

    public void setOdata_etag(String odata_etag) {
        this.odata_etag = odata_etag;
    }

    public String getJournal_Template_Name() {
        return Journal_Template_Name;
    }

    public void setJournal_Template_Name(String Journal_Template_Name) {
        this.Journal_Template_Name = Journal_Template_Name;
    }

    public String getJournal_Batch_Name() {
        return Journal_Batch_Name;
    }

    public void setJournal_Batch_Name(String Journal_Batch_Name) {
        this.Journal_Batch_Name = Journal_Batch_Name;
    }

    public int getLine_No() {
        return Line_No;
    }

    public void setLine_No(int Line_No) {
        this.Line_No = Line_No;
    }

    public String getPosting_Date() {
        return Posting_Date;
    }

    public void setPosting_Date(String Posting_Date) {
        this.Posting_Date = Posting_Date;
    }

    public String getItem_No() {
        return Item_No;
    }

    public void setItem_No(String Item_No) {
        this.Item_No = Item_No;
    }

    public String getLocation_Code() {
        return Location_Code;
    }

    public void setLocation_Code(String Location_Code) {
        this.Location_Code = Location_Code;
    }

    public String getBin_Code() {
        return Bin_Code;
    }

    public void setBin_Code(String Bin_Code) {
        this.Bin_Code = Bin_Code;
    }

    public String getQty_Calculated() {
        return Qty_Calculated;
    }

    public void setQty_Calculated(String Qty_Calculated) {
        this.Qty_Calculated = Qty_Calculated;
    }

    public String getQty_Phys_Inventory() {
        return Qty_Phys_Inventory;
    }

    public void setQty_Phys_Inventory(String Qty_Phys_Inventory) {
        this.Qty_Phys_Inventory = Qty_Phys_Inventory;
    }

    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }

}