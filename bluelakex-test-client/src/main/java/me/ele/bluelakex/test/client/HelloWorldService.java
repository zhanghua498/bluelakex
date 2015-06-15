package me.ele.bluelakex.test.client;

import java.util.List;
import java.util.Map;

import me.ele.bluelakex.client.annotation.Async;
import me.ele.bluelakex.client.annotation.RpcCall;
import me.ele.bluelakex.loadbalance.LoadBalanceEnum;
import me.ele.bluelakex.serialize.SerializeEnum;

@RpcCall(server="server1", serializer = SerializeEnum.Protostuff, zkServer = "zk1", loadbalance = LoadBalanceEnum.RoundRobin )
public interface HelloWorldService {
	
	public void nothing();
	
	//不会异步调用，因为String为Final类，无法用Cglib代理
	@Async
	public String hello(String name);
	
	public List<SimpleBean> bean(int a, String b, SimpleBean c,Map<String, SimpleBean> d);
	
	public ComplexBean complexBean(ComplexBean cb);
	
	public List<SimpleBean> beans(List<SimpleBean> beans);
	
	public Map<String,List<SimpleBean>> complexMap(int size);
	
	@Async
	public SimpleBean testAsync(int i);
}
