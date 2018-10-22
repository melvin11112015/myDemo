/**
 * Copyright 2018 bejson.com
 */
package com.weihan.scanner.entities;

import com.google.gson.annotations.SerializedName;

;

/**
 * Auto-generated: 2018-09-19 10:19:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BinContentInfo {

    @SerializedName("odata.etag")
    private String odata_etag;

    private String Location_Code;
    private String Bin_Code;
    private String Item_No;
    private String Variant_Code;
    private String Unit_of_Measure_Code;
    private String Zone_Code;
    private String Bin_Type_Code;
    private String Block_Movement;
    private int Bin_Ranking;
    private boolean Fixed;
    private boolean Default;
    private boolean Dedicated;
    private String Warehouse_Class_Code;
    private String CalcQtyUOM;
    private String Quantity_Base;
    private String Min_Qty;
    private String Max_Qty;
    private String Qty_per_Unit_of_Measure;
    private String Lot_No_Filter;
    private String Serial_No_Filter;
    private String ETag;
    private boolean Temp_Receipt, Store_Issue, Temp_Ship;

    public boolean isFixed() {
        return Fixed;
    }

    public boolean isDefault() {
        return Default;
    }

    public boolean isDedicated() {
        return Dedicated;
    }

    public boolean isTemp_Receipt() {
        return Temp_Receipt;
    }

    public void setTemp_Receipt(boolean temp_Receipt) {
        Temp_Receipt = temp_Receipt;
    }

    public boolean isStore_Issue() {
        return Store_Issue;
    }

    public void setStore_Issue(boolean store_Issue) {
        Store_Issue = store_Issue;
    }

    public boolean isTemp_Ship() {
        return Temp_Ship;
    }

    public void setTemp_Ship(boolean temp_Ship) {
        Temp_Ship = temp_Ship;
    }

    public String getOdata_etag() {
        return odata_etag;
    }

    public void setOdata_etag(String odata_etag) {
        this.odata_etag = odata_etag;
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

    public String getItem_No() {
        return Item_No;
    }

    public void setItem_No(String Item_No) {
        this.Item_No = Item_No;
    }

    public String getVariant_Code() {
        return Variant_Code;
    }

    public void setVariant_Code(String Variant_Code) {
        this.Variant_Code = Variant_Code;
    }

    public String getUnit_of_Measure_Code() {
        return Unit_of_Measure_Code;
    }

    public void setUnit_of_Measure_Code(String Unit_of_Measure_Code) {
        this.Unit_of_Measure_Code = Unit_of_Measure_Code;
    }

    public String getZone_Code() {
        return Zone_Code;
    }

    public void setZone_Code(String Zone_Code) {
        this.Zone_Code = Zone_Code;
    }

    public String getBin_Type_Code() {
        return Bin_Type_Code;
    }

    public void setBin_Type_Code(String Bin_Type_Code) {
        this.Bin_Type_Code = Bin_Type_Code;
    }

    public String getBlock_Movement() {
        return Block_Movement;
    }

    public void setBlock_Movement(String Block_Movement) {
        this.Block_Movement = Block_Movement;
    }

    public int getBin_Ranking() {
        return Bin_Ranking;
    }

    public void setBin_Ranking(int Bin_Ranking) {
        this.Bin_Ranking = Bin_Ranking;
    }

    public boolean getFixed() {
        return Fixed;
    }

    public void setFixed(boolean Fixed) {
        this.Fixed = Fixed;
    }

    public boolean getDefault() {
        return Default;
    }

    public void setDefault(boolean Default) {
        this.Default = Default;
    }

    public boolean getDedicated() {
        return Dedicated;
    }

    public void setDedicated(boolean Dedicated) {
        this.Dedicated = Dedicated;
    }

    public String getWarehouse_Class_Code() {
        return Warehouse_Class_Code;
    }

    public void setWarehouse_Class_Code(String Warehouse_Class_Code) {
        this.Warehouse_Class_Code = Warehouse_Class_Code;
    }

    public String getCalcQtyUOM() {
        return CalcQtyUOM;
    }

    public void setCalcQtyUOM(String CalcQtyUOM) {
        this.CalcQtyUOM = CalcQtyUOM;
    }

    public String getQuantity_Base() {
        return Quantity_Base;
    }

    public void setQuantity_Base(String Quantity_Base) {
        this.Quantity_Base = Quantity_Base;
    }

    public String getMin_Qty() {
        return Min_Qty;
    }

    public void setMin_Qty(String Min_Qty) {
        this.Min_Qty = Min_Qty;
    }

    public String getMax_Qty() {
        return Max_Qty;
    }

    public void setMax_Qty(String Max_Qty) {
        this.Max_Qty = Max_Qty;
    }

    public String getQty_per_Unit_of_Measure() {
        return Qty_per_Unit_of_Measure;
    }

    public void setQty_per_Unit_of_Measure(String Qty_per_Unit_of_Measure) {
        this.Qty_per_Unit_of_Measure = Qty_per_Unit_of_Measure;
    }

    public String getLot_No_Filter() {
        return Lot_No_Filter;
    }

    public void setLot_No_Filter(String Lot_No_Filter) {
        this.Lot_No_Filter = Lot_No_Filter;
    }

    public String getSerial_No_Filter() {
        return Serial_No_Filter;
    }

    public void setSerial_No_Filter(String Serial_No_Filter) {
        this.Serial_No_Filter = Serial_No_Filter;
    }

    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }

}