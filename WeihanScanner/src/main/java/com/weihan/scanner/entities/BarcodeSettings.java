package com.weihan.scanner.entities;

public class BarcodeSettings {

    private static final BarcodeSettings instance = new BarcodeSettings();
    private static String serverip, machineCode, tempArea, userid;
    private static int materialCodeLength, poCodeLength, batchCodePrefixLength, batchCodeLength, warehouseCodeLength, moCodeLength;

    private BarcodeSettings() {
    }

    public static String getUserid() {
        return userid;
    }

    public static void setUserid(String userid) {
        BarcodeSettings.userid = userid;
    }

    public static String getServerip() {
        return serverip;
    }

    public static void setServerip(String serverip) {
        BarcodeSettings.serverip = serverip;
    }

    public static String getMachineCode() {
        return machineCode;
    }

    public static void setMachineCode(String machineCode) {
        BarcodeSettings.machineCode = machineCode;
    }

    public static String getTempArea() {
        return tempArea;
    }

    public static void setTempArea(String tempArea) {
        BarcodeSettings.tempArea = tempArea;
    }

    public static int getMaterialCodeLength() {
        return materialCodeLength;
    }

    public static void setMaterialCodeLength(int materialCodeLength) {
        BarcodeSettings.materialCodeLength = materialCodeLength;
    }

    public static int getPoCodeLength() {
        return poCodeLength;
    }

    public static void setPoCodeLength(int poCodeLength) {
        BarcodeSettings.poCodeLength = poCodeLength;
    }

    public static int getBatchCodePrefixLength() {
        return batchCodePrefixLength;
    }

    public static void setBatchCodePrefixLength(int batchCodePrefixLength) {
        BarcodeSettings.batchCodePrefixLength = batchCodePrefixLength;
    }

    public static int getBatchCodeLength() {
        return batchCodeLength;
    }

    public static void setBatchCodeLength(int batchCodeLength) {
        BarcodeSettings.batchCodeLength = batchCodeLength;
    }

    public static int getWarehouseCodeLength() {
        return warehouseCodeLength;
    }

    public static void setWarehouseCodeLength(int warehouseCodeLength) {
        BarcodeSettings.warehouseCodeLength = warehouseCodeLength;
    }

    public static int getMoCodeLength() {
        return moCodeLength;
    }

    public static void setMoCodeLength(int moCodeLength) {
        BarcodeSettings.moCodeLength = moCodeLength;
    }

    public static BarcodeSettings getInstance() {
        return instance;
    }
}
