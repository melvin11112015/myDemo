package com.scancode.model;

/** 
* @ClassName: Stroage 
* @Description: TODO	 入库
* @author zhaoruquan
* @date 2015-9-12 下午5:50:41 
 *
 * @author Melvin
 * @Description 增加行号
*/
public class Storage {

	private String name;//物料名称

	private String lineNo;
	
	private String number;//物料编码
	
//	private int sum;//应该入库总数量
	
	private String stoNum;//采购单号
	
	private int surplus;//还没入库数量
	
	private int in;//当前入库数量

	public Storage() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getStoNum() {
		return stoNum;
	}

	public void setStoNum(String stoNum) {
		this.stoNum = stoNum;
	}

	public int getSurplus() {
		return surplus;
	}

	public void setSurplus(int surplus) {
		this.surplus = surplus;
	}

	public int getIn() {
		return in;
	}

	public void setIn(int in) {
		this.in = in;
	}

	@Override
	public String toString() {
		return "Storage{" +
				"name='" + name + '\'' +
				", lineNo='" + lineNo + '\'' +
				", number='" + number + '\'' +
				", stoNum='" + stoNum + '\'' +
				", surplus=" + surplus +
				", in=" + in +
				'}';
	}
}
