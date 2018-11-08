package com.weihan.scanner.entities;

public class BarcodeSettings {

    private static final BarcodeSettings instance = new BarcodeSettings();
    private static String serverip, machineCode, tempArea, userid;
    private static int materialCodeLength, poCodeLength, batchCodePrefixLength, batchCodeLength, warehouseCodeLength, moCodeLength;

    private BarcodeSettings() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        BarcodeSettings.userid = userid;
    }

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        BarcodeSettings.serverip = serverip;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        BarcodeSettings.machineCode = machineCode;
    }

    public String getTempArea() {
        return tempArea;
    }

    public void setTempArea(String tempArea) {
        BarcodeSettings.tempArea = tempArea;
    }

    public int getMaterialCodeLength() {
        return materialCodeLength;
    }

    public void setMaterialCodeLength(int materialCodeLength) {
        BarcodeSettings.materialCodeLength = materialCodeLength;
    }

    public int getPoCodeLength() {
        return poCodeLength;
    }

    public void setPoCodeLength(int poCodeLength) {
        BarcodeSettings.poCodeLength = poCodeLength;
    }

    public int getBatchCodePrefixLength() {
        return batchCodePrefixLength;
    }

    public void setBatchCodePrefixLength(int batchCodePrefixLength) {
        BarcodeSettings.batchCodePrefixLength = batchCodePrefixLength;
    }

    public int getBatchCodeLength() {
        return batchCodeLength;
    }

    public void setBatchCodeLength(int batchCodeLength) {
        BarcodeSettings.batchCodeLength = batchCodeLength;
    }

    public int getWarehouseCodeLength() {
        return warehouseCodeLength;
    }

    public void setWarehouseCodeLength(int warehouseCodeLength) {
        BarcodeSettings.warehouseCodeLength = warehouseCodeLength;
    }

    public int getMoCodeLength() {
        return moCodeLength;
    }

    public void setMoCodeLength(int moCodeLength) {
        BarcodeSettings.moCodeLength = moCodeLength;
    }

    public static BarcodeSettings getInstance() {
        return instance;
    }
}
