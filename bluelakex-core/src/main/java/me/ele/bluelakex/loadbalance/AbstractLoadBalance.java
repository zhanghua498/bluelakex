package me.ele.bluelakex.loadbalance;

import java.util.List;

import me.ele.bluelakex.model.RequestEntity;

public abstract class AbstractLoadBalance{
	public String select(List<String> serverList,RequestEntity requset){
		if(serverList == null || serverList.size() == 0)
			return null;
		if(serverList.size() == 1)
			return serverList.get(0);
		return doSelect(serverList,requset);
	}
	protected abstract String doSelect(List<String> serverList,RequestEntity requset);
	
}
