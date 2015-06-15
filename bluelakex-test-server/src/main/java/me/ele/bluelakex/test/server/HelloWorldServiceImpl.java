package me.ele.bluelakex.test.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import me.ele.bluelakex.server.annotation.RpcService;
import me.ele.bluelakex.test.client.ComplexBean;
import me.ele.bluelakex.test.client.HelloWorldService;
import me.ele.bluelakex.test.client.SimpleBean;

@RpcService(value=HelloWorldService.class)
public class HelloWorldServiceImpl implements HelloWorldService {

	@Override
	public String hello(String name) {		
		return "Hello! "+name;
	}

	@Override
	public List<SimpleBean> bean(int a, String b, SimpleBean c,
			Map<String, SimpleBean> d) {		
		List<SimpleBean> list = new ArrayList<SimpleBean>();
		list.add(c);
		list.add(createSimpleBean());
		list.add(d.get(b));
		return list;
	}
	
	
	private SimpleBean createSimpleBean(){
		SimpleBean bean = new SimpleBean();
		bean.setNumber(123);
		bean.setNumber2(234);
		bean.setDouble1(1234.1234);
		bean.setLulu(12345678901234L);
		bean.setBd(new BigDecimal(3456.789));
		bean.setStr("张华 hello Servere");
//		bean.setList(Arrays.asList("a","b","c"));
		return bean;
	}
	
	private List<SimpleBean> createSimpleBeans(int size){

		List<SimpleBean> list = new ArrayList<SimpleBean>(10);
		for(int i=0;i<size;i++){
			SimpleBean bean = new SimpleBean();
			bean.setNumber(123);
			bean.setNumber2(234);
			bean.setDouble1(1234.1234);
			bean.setLulu(12345678901234L);
			bean.setBd(new BigDecimal(3456.789));
			bean.setStr("张华 hello from  server "+i);
//			bean.setList(Arrays.asList("a"+i,"b"+i,"c"+i));
			list.add(bean);
		}
		
		return list;
	}

	@Override
	public ComplexBean complexBean(ComplexBean cb) {
		cb.getSimpleBean().setStr("Hello World From Server!!!");
		return cb;
	}

	@Override
	public List<SimpleBean> beans(List<SimpleBean> beans) {
		int size = beans.size();
		int i=0;
		for(SimpleBean sb:beans){
			sb.setNumber(ThreadLocalRandom.current().nextInt(size));
			sb.setStr("张华 hello Servere" + ++i);
		}
		return beans;
	}

	@Override
	public void nothing() {
		System.out.println("NoThing is called!");
	}

	@Override
	public Map<String, List<SimpleBean>> complexMap(int size) {
		Map<String, List<SimpleBean>> map = new HashMap<String, List<SimpleBean>>();
		map.put("zhanghua", createSimpleBeans(size));
		return map;
	}

	@Override
	public SimpleBean testAsync(int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SimpleBean sb = createSimpleBean();
		sb.setStr("张华 hello Servere" + i);
		return sb;
	}

}
