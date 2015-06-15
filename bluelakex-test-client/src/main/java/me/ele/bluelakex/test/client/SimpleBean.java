package me.ele.bluelakex.test.client;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SimpleBean implements Serializable{

	private static final long serialVersionUID = -2852458829503646761L;
	private int number;
	private long lulu;
	private Integer number2;
	private Double double1;
	private BigDecimal bd;
	private Timestamp ts;
	private String str;
//	private List<String> list = new ArrayList<String>();
	
	

//	public List<String> getList() {
//		return list;
//	}
//
//	public void setList(List<String> list) {
//		this.list = list;
//	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public long getLulu() {
		return lulu;
	}

	public void setLulu(long lulu) {
		this.lulu = lulu;
	}

	public Integer getNumber2() {
		return number2;
	}

	public void setNumber2(Integer number2) {
		this.number2 = number2;
	}

	public Double getDouble1() {
		return double1;
	}

	public void setDouble1(Double double1) {
		this.double1 = double1;
	}

	public BigDecimal getBd() {
		return bd;
	}

	public void setBd(BigDecimal bd) {
		this.bd = bd;
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public SimpleBean(int number, long lulu, Integer number2, Double double1,
			BigDecimal bd, Timestamp ts) {
		super();
		this.number = number;
		this.lulu = lulu;
		this.number2 = number2;
		this.double1 = double1;
		this.bd = bd;
		this.ts = ts;
	}
	
	

	@Override
	public String toString() {
		return "SimpleBean [number=" + number + ", lulu=" + lulu + ", number2="
				+ number2 + ", double1=" + double1 + ", bd=" + bd + ", ts="
				+ ts + ", str=" + str + 
//				",list=" + list.size() +
				"]";
	}

	public SimpleBean() {
		super();
	}

}
