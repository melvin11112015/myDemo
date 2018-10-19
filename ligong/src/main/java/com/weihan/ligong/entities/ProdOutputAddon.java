package com.weihan.ligong.entities;

public class ProdOutputAddon {
    private String OutputNo, Barcode, Quantity, OutputDate, CreationDate, TerminalID;
    private int LineNo;

    public String getOutputNo() {
        return OutputNo;
    }

    public void setOutputNo(String outputNo) {
        OutputNo = outputNo;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getOutputDate() {
        return OutputDate;
    }

    public void setOutputDate(String outputDate) {
        OutputDate = outputDate;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public int getLineNo() {
        return LineNo;
    }

    public void setLineNo(int lineNo) {
        LineNo = lineNo;
    }
}
