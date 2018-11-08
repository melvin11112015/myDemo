package com.weihan.scanner.entities;

public class WarehouseTransferMultiAddon {
    private String ItemNo, Quantity, FromLocationCode, FromBinCode, ToLocationCode, ToBinCode, SubmitDate, TerminalID, CreationDate, TransferNo;
    private long LineNo;

    public String getTransferNo() {
        return TransferNo;
    }

    public void setTransferNo(String transferNo) {
        TransferNo = transferNo;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getItemNo() {
        return ItemNo;
    }

    public void setItemNo(String itemNo) {
        ItemNo = itemNo;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getFromLocationCode() {
        return FromLocationCode;
    }

    public void setFromLocationCode(String fromLocationCode) {
        FromLocationCode = fromLocationCode;
    }

    public String getFromBinCode() {
        return FromBinCode;
    }

    public void setFromBinCode(String fromBinCode) {
        FromBinCode = fromBinCode;
    }

    public String getToLocationCode() {
        return ToLocationCode;
    }

    public void setToLocationCode(String toLocationCode) {
        ToLocationCode = toLocationCode;
    }

    public String getToBinCode() {
        return ToBinCode;
    }

    public void setToBinCode(String toBinCode) {
        ToBinCode = toBinCode;
    }

    public String getSubmitDate() {
        return SubmitDate;
    }

    public void setSubmitDate(String submitDate) {
        SubmitDate = submitDate;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

    public long getLineNo() {
        return LineNo;
    }

    public void setLineNo(long lineNo) {
        LineNo = lineNo;
    }
}
