package me.ele.bluelakex.loadbalance;

public enum LoadBalanceEnum {
	LeastActive(LeastActiveLoadBalance.getInstance()),
	Random(RandomLoadBalance.getInstance()),
	RoundRobin(RoundRobinLoadBalance.getInstance());
	
	private AbstractLoadBalance lb;
	
	private LoadBalanceEnum(AbstractLoadBalance lb){
		this.lb = lb;
	}
	
	public AbstractLoadBalance getLoadBalance(){
		return this.lb;
	}
}
