package com.weihan.scanner.entities;

public class WarehouseReceiptAddon {
    private String PurchOrderNo, Quantity, TerminalID, ItemNo, CreationDate;

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getPurchOrderNo() {
        return PurchOrderNo;
    }

    public void setPurchOrderNo(String purchOrderNo) {
        PurchOrderNo = purchOrderNo;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }
}
