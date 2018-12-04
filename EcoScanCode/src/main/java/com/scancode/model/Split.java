package com.scancode.model;

/** 
* @ClassName: Split 
* @Description: TODO	拆分model
* @author zhaoruquan
* @date 2015-9-12 下午6:14:29 
*  
*/
public class Split {
	private String number;//采购单号
	
	private String matterNum;//物料编号
	
	private int amount;//物料数量
	
	private int sur;//剩余物料数量
	
	private String tag;

	public Split() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMatterNum() {
		return matterNum;
	}

	public void setMatterNum(String matterNum) {
		this.matterNum = matterNum;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getSur() {
		return sur;
	}

	public void setSur(int sur) {
		this.sur = sur;
	}
}
