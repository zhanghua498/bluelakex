package me.ele.bluelakex.loadbalance;

import java.util.List;

import me.ele.bluelakex.model.RequestEntity;

public class LeastActiveLoadBalance extends AbstractLoadBalance {

	private volatile static LeastActiveLoadBalance instance;
	
	private LeastActiveLoadBalance(){}
	
	public static LeastActiveLoadBalance getInstance(){
		if(instance == null){
			synchronized(LeastActiveLoadBalance.class){
				if(instance == null){
					instance = new LeastActiveLoadBalance();
				}
			}
		}
		return instance;
	}
	
	@Override
	protected String doSelect(List<String> serverList, RequestEntity requset) {
		return null;
	}

}
