package me.ele.bluelakex.loadbalance;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.ele.bluelakex.model.RequestEntity;
import me.ele.bluelakex.utils.AtomicPositiveInteger;

public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private final ConcurrentMap<String, me.ele.bluelakex.utils.AtomicPositiveInteger> sequences =
    		new ConcurrentHashMap<String, AtomicPositiveInteger>();
    
	private volatile static RoundRobinLoadBalance instance;
	
	private RoundRobinLoadBalance(){}
	
	public static RoundRobinLoadBalance getInstance(){
		if(instance == null){
			synchronized(RoundRobinLoadBalance.class){
				if(instance == null){
					instance = new RoundRobinLoadBalance();
				}
			}
		}
		return instance;
	}
	
	@Override
	protected String doSelect(List<String> serverList, RequestEntity requset) {
		String key = requset.getClassName() + requset.getMethodName();
		int length = serverList.size();
		AtomicPositiveInteger sequence = sequences.get(key);
		if (sequence == null) {
			sequences.putIfAbsent(key, new AtomicPositiveInteger());
			sequence = sequences.get(key);
	    }
	    // 取模轮循
	    return serverList.get(sequence.getAndIncrement() % length);
	}
}
