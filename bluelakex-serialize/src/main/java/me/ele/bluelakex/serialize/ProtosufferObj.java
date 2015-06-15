package me.ele.bluelakex.serialize;

import java.util.List;
import java.util.Map;

public class ProtosufferObj{
	private List<?> list;
	private Map<?,?> map;
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	public Map<?, ?> getMap() {
		return map;
	}
	public void setMap(Map<?, ?> map) {
		this.map = map;
	}
}