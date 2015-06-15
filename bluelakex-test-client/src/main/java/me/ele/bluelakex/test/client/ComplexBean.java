package me.ele.bluelakex.test.client;

public class ComplexBean {

	private SimpleBean simpleBean;
	private Integer number;
	public SimpleBean getSimpleBean() {
		return simpleBean;
	}
	public void setSimpleBean(SimpleBean simpleBean) {
		this.simpleBean = simpleBean;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "ComplexBean [simpleBean=" + simpleBean + ", number=" + number
				+ "]";
	}
	
}
