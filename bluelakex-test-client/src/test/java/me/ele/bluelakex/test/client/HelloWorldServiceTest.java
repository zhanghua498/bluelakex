package me.ele.bluelakex.test.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import me.ele.bluelakex.client.RpcProxy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class HelloWorldServiceTest {

	@Autowired
	private RpcProxy rpcProxy;

//	@Test
	public void test1() {
		HelloWorldService helloService = rpcProxy.create(HelloWorldService.class);
		String result = helloService.hello("zhanghua");
		System.out.println(result + "---------");
		Assert.assertEquals("Hello! zhanghua", result);
		
	}
	
//	@Test
	public void test2() {
		HelloWorldService helloService = rpcProxy.create(HelloWorldService.class);
		Map<String, SimpleBean> map = new HashMap<String, SimpleBean>();
		map.put("zhanghua", createSimpleBean());
		List<SimpleBean> list = helloService.bean(1234, "zhanghua", createSimpleBean(), map);
		for(SimpleBean bean:list){
			System.out.println(bean);
		}
		Assert.assertEquals(3, list.size());
	}
	
//	@Test
	public void test3() {
		HelloWorldService helloService = rpcProxy.create(HelloWorldService.class);
		ComplexBean cb = helloService.complexBean(createComplexBean());
		System.out.println(cb);
		Assert.assertEquals("Hello World From Server!!!", cb.getSimpleBean().getStr());
	}
	
//	@Test
	public void test4() {
		HelloWorldService helloService = rpcProxy.create(HelloWorldService.class);
		List<SimpleBean> clist = createSimpleBeans();
		List<SimpleBean> slist = helloService.beans(clist);
		for(SimpleBean bean:slist){
			System.out.println(bean);
		}
		Assert.assertEquals(clist.size(), slist.size());
	}
	
//	@Test
	public void testNothing() {
		HelloWorldService helloService = rpcProxy.create(HelloWorldService.class);
		helloService.nothing();
		Assert.assertEquals(1, 1);
	}
	
//	@Test
	public void test5() {
		int size = 100;
		HelloWorldService helloService = rpcProxy.create(HelloWorldService.class);
		Map<String, List<SimpleBean>> map =  helloService.complexMap(size);
		List<SimpleBean> list = map.get("zhanghua");
		for(SimpleBean bean:list){
			System.out.println(bean);
		}
		Assert.assertEquals(size, list.size());
	}
	
	@Test
	public void testAsync(){
		HelloWorldService helloService = rpcProxy.create(HelloWorldService.class);
		SimpleBean sb1 = helloService.testAsync(1);
		SimpleBean sb2 = helloService.testAsync(2);
		SimpleBean sb3 = helloService.testAsync(3);
		SimpleBean sb4 = helloService.testAsync(4);
		SimpleBean sb5 = helloService.testAsync(5);
		SimpleBean sb6 = helloService.testAsync(6);
		SimpleBean sb7 = helloService.testAsync(7);
		SimpleBean sb8 = helloService.testAsync(8);
		SimpleBean sb9 = helloService.testAsync(9);
		SimpleBean sb10 = helloService.testAsync(10);
		System.out.println("sb1:"+sb1.getStr());
		System.out.println("sb2:"+sb2.getStr());
		System.out.println("sb3:"+sb3.getStr());
		System.out.println("sb4:"+sb4.getStr());
		System.out.println("sb5:"+sb5.getStr());
		System.out.println("sb6:"+sb6.getStr());
		System.out.println("sb7:"+sb7.getStr());
		System.out.println("sb8:"+sb8.getStr());
		System.out.println("sb9:"+sb9.getStr());
		System.out.println("sb10:"+sb10.getStr());
	}
	
	private ComplexBean createComplexBean(){
		ComplexBean cb = new ComplexBean();
		cb.setNumber(ThreadLocalRandom.current().nextInt(111));
		cb.setSimpleBean(createSimpleBean());
		return cb;
	}
	
	private SimpleBean createSimpleBean(){
		SimpleBean bean = new SimpleBean();
		bean.setNumber(123);
		bean.setNumber2(234);
		bean.setDouble1(1234.1234);
		bean.setLulu(12345678901234L);
		bean.setBd(new BigDecimal(3456.789));
		bean.setStr("张华 hello Client");
//		bean.setList(Arrays.asList("a","b","c"));
		return bean;
	}
	
	private List<SimpleBean> createSimpleBeans(){

		List<SimpleBean> list = new ArrayList<SimpleBean>(10);
		for(int i=0;i<100;i++){
			SimpleBean bean = new SimpleBean();
			bean.setNumber(123);
			bean.setNumber2(234);
			bean.setDouble1(1234.1234);
			bean.setLulu(12345678901234L);
			bean.setBd(new BigDecimal(3456.789));
			bean.setStr("张华 hello");
			bean.setStr("张华 hello"+i);
//			bean.setList(Arrays.asList("a"+i,"b"+i,"c"+i));
			list.add(bean);
		}
		
		return list;
	}
}
