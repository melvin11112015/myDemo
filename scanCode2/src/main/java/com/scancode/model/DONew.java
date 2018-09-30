package com.scancode.model;

import java.io.Serializable;

/** 
* @ClassName: DONew 
* @Description: TODO	新的出货清单model
* @author zhaoruquan
* @date 2015-10-8 下午3:42:05 
*  
*/
public class DONew implements Serializable {

	private String ShipNo;//发货单号
	
//	private String ShipDate;//发货日期
//	
//	private String CustNo;//客户编号
//	
//	private String CustName;//客户名称
	
	private String OrderDate;//发货单日期
	
//	private String PalletCount;//发货单卡板数，大于0表示已经输入了卡板，不要操作
	
	private String status;//出货计划单状态，1-表示可用于出货，其他值现在都不允许出货

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DONew() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getShipNo() {
		return ShipNo;
	}

	public void setShipNo(String shipNo) {
		ShipNo = shipNo;
	}

//	public String getShipDate() {
//		return ShipDate;
//	}
//
//	public void setShipDate(String shipDate) {
//		ShipDate = shipDate;
//	}
//
//	public String getCustNo() {
//		return CustNo;
//	}
//
//	public void setCustNo(String custNo) {
//		CustNo = custNo;
//	}
//
//	public String getCustName() {
//		return CustName;
//	}
//
//	public void setCustName(String custName) {
//		CustName = custName;
//	}

	public String getOrderDate() {
		return OrderDate;
	}

	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}

//	public int getPalletCount() {
//		return Integer.valueOf(PalletCount);
//	}
//
//	public void setPalletCount(String palletCount) {
//		PalletCount = palletCount;
//	}
}
