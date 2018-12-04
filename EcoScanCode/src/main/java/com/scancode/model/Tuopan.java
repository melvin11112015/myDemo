package com.scancode.model;

import java.util.ArrayList;
import java.util.List;

public class Tuopan {

	private String code;//托盘编码
	private List<Split> splits = new ArrayList<Split>();//物料中的剩余数量
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<Split> getSplits() {
		return splits;
	}
	public void setSplits(List<Split> splits) {
		this.splits = splits;
	}
	public Tuopan() {
		super();
	}
}
