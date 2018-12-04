package com.scancode.model;

import java.util.ArrayList;
import java.util.List;

public class Ship {
	private String LineNo;//发货单行号
	private String OrderNo;//销售单号
	private String OrderLine;//销售单行号
	private String ItemNo;//物料编号
	private String Name1;//物料名称1
	private String Name2;//物料名称2
	private String OrderQty;//订单数量
	private String ShipQty;//本次计划发货数量
	private String OutstandingQty;//销售订单未发货数量
	
	public int getPackQty() {
		return PackQty;
	}
	public void setPackQty(int packQty) {
		PackQty = packQty;
	}
	private String outNum = "0" ;//出货数量
	
	private int PackQty;//已扫描装箱数量，扫描托盘进行匹配出货行时出货行剩余出货数量=ShipQty-PackQty
	
	private List<String> tuopan = new ArrayList<String>();//对应的托盘
	
	private List<Integer> tpNum = new ArrayList<Integer>();//对应托盘中添加的数量
	
	public Ship() {
		super();
	}
	public String getLineNo() {
		return LineNo;
	}
	public void setLineNo(String lineNo) {
		LineNo = lineNo;
	}
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public String getOrderLine() {
		return OrderLine;
	}
	public void setOrderLine(String orderLine) {
		OrderLine = orderLine;
	}
	public String getItemNo() {
		return ItemNo;
	}
	public void setItemNo(String itemNo) {
		ItemNo = itemNo;
	}
	public String getName1() {
		return Name1;
	}
	public void setName1(String name1) {
		Name1 = name1;
	}
	public String getName2() {
		return Name2;
	}
	public void setName2(String name2) {
		Name2 = name2;
	}
	public String getOrderQty() {
		return OrderQty;
	}
	public void setOrderQty(String orderQty) {
		OrderQty = orderQty;
	}
	public String getShipQty() {
		return ShipQty;
	}
	public void setShipQty(String shipQty) {
		ShipQty = shipQty;
	}
	public String getOutstandingQty() {
		return OutstandingQty;
	}
	public void setOutstandingQty(String outstandingQty) {
		OutstandingQty = outstandingQty;
	}
	public String getOutNum() {
		return outNum;
	}
	public void setOutNum(String outNum) {
		this.outNum = outNum;
	}
	public List<String> getTuopan() {
		return tuopan;
	}
	public void addTuopan(String tuopan){
		this.tuopan.add(tuopan);
	}
	public void setTuopan(List<String> tuopan) {
		this.tuopan = tuopan;
	}
	public List<Integer> getTpNum() {
		return tpNum;
	}
	public void setTpNum(List<Integer> tpNum) {
		this.tpNum = tpNum;
	}
	public void addTpNum(int num){
		this.tpNum.add(num);
	}
	//获取该行使用托盘的数量,如果返回－1则没有使用
	public int getTpNum(String tp){
		if (!tuopan.contains(tp)) {
			return -1;
		}
		return tpNum.get(tuopan.indexOf(tp));
	}
}
