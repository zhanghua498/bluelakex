package me.ele.bluelakex.loadbalance;

import java.util.List;
import java.util.Random;

import me.ele.bluelakex.model.RequestEntity;

public class RandomLoadBalance extends AbstractLoadBalance {
	
	private final Random random = new Random();
	
	private volatile static RandomLoadBalance instance;
	
	private RandomLoadBalance(){}
	
	public static RandomLoadBalance getInstance(){
		if(instance == null){
			synchronized(RandomLoadBalance.class){
				if(instance == null){
					instance = new RandomLoadBalance();
				}
			}
		}
		return instance;
	}

	@Override
	protected String doSelect(List<String> serverList, RequestEntity requset) {
		int length = serverList.size();
		return serverList.get(random.nextInt(length));
	}

}
