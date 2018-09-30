package com.scancode.model;

/** 
* @ClassName: Pack 
* @Description: TODO	装箱单是否完成扫描
* @author zhaoruquan
* @date 2015-10-21 上午4:28:57 
*  
*/
public class Pack {

	private String PackLstNo;//装箱单号
	
	private String PackingComplete;//-装箱单是否已经完成扫描，0-未完成，1-完成

	public String getPackLstNo() {
		return PackLstNo;
	}

	public void setPackLstNo(String packLstNo) {
		PackLstNo = packLstNo;
	}

	public String getPackingComplete() {
		return PackingComplete;
	}

	public void setPackingComplete(String packingComplete) {
		PackingComplete = packingComplete;
	}
}
