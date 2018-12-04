package com.scancode.model;

import java.io.Serializable;

/** 
* @ClassName: DO 
* @Description: TODO	出货单
* @author zhaoruquan
* @date 2015-9-12 下午6:34:43 
*  
*/
public class DO implements Serializable{

	private String shipNo;//发货单号
	
	private String location;//仓库编号
	
	private String shipDate;//发货日期
	
	private int PalletCount;//已上传托盘，0是没有上传托盘，大于0则是上传托盘。

	public DO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getShipNo() {
		return shipNo;
	}

	public void setShipNo(String shipNo) {
		this.shipNo = shipNo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public int getPalletCount() {
		return PalletCount;
	}

	public void setPalletCount(int palletCount) {
		PalletCount = palletCount;
	}
}
