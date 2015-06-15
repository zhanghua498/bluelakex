package me.ele.bluelakex.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import me.ele.bluelakex.client.annotation.Async;
import me.ele.bluelakex.client.annotation.RpcCall;
import me.ele.bluelakex.client.async.RpcAsyncExecutor;
import me.ele.bluelakex.client.async.RpcAsyncResult;
import me.ele.bluelakex.loadbalance.AbstractLoadBalance;
import me.ele.bluelakex.model.RequestEntity;
import me.ele.bluelakex.model.ResponseEntity;
import me.ele.bluelakex.zookeeper.ZooKeeperServiceDiscover;

public class RpcProxy {
	
	private ExecutorService threadPool;
	
	private RpcClientContext clientContext;
	
	public RpcProxy(RpcClientContext clientContext){
		this.clientContext = clientContext;
		this.threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*10);
	}
	
	public RpcProxy(RpcClientContext clientContext,RpcAsyncExecutor threadPool){
		this.clientContext = clientContext;
		this.threadPool = threadPool.init();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T create(Class<?> interfaceClass) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] { interfaceClass },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

						final RequestEntity request = buildRequest(method,args);
						
						RpcCall rpcCall = method.getDeclaringClass().getAnnotation(RpcCall.class);												
						final RpcClient client = buildClient(rpcCall,request);
						final boolean async;
						Class<?> returnClass = method.getReturnType();
						if(method.getAnnotation(Async.class) != null){
							async = true;
						}
						else
							async = false;
						if(async && canCglibProxy(returnClass)){
							Future<ResponseEntity> future = threadPool.submit(new Callable<ResponseEntity>(){
								@Override
								public ResponseEntity call() throws Exception {
									return client.send(request);
								}						
							});
							RpcAsyncResult result = new RpcAsyncResult(returnClass,future);
							return result.getProxy();
						}
						else{
							ResponseEntity response = client.send(request);
							
							if (response.isError()) {
								throw response.getError();
							} else {
								return response.getResult();
							}
						}
					}
					
					private boolean canCglibProxy(Class<?> returnClass){
			            if (Void.TYPE.isAssignableFrom(returnClass)) {// 判断返回值是否为void
			                // 不处理void的函数调用
			                return false;
			            } else if (!Modifier.isPublic(returnClass.getModifiers())) {
			                // 处理如果是非public属性，则不进行代理，强制访问会出现IllegalAccessException，比如一些内部类或者匿名类不允许直接访问
			            	return false;
			            } else if (Modifier.isFinal(returnClass.getModifiers())) {
			                // 处理特殊的final类型，目前暂不支持，后续可采用jdk proxy
			                return false;
			            } else if (returnClass.isPrimitive() || returnClass.isArray()) {
			                // 不处理特殊类型，因为无法使用cglib代理
			                return false;
			            } else if (returnClass == Object.class) {
			                // 针对返回对象是Object类型，不做代理。没有具体的method，代理没任何意义
			                return false;
			            }
			            else
			            	return true;
					}
					
					private RequestEntity buildRequest(Method method, Object[] args){
						RequestEntity request = new RequestEntity();
						request.setRequestId(UUID.randomUUID().toString());
						request.setClassName(method.getDeclaringClass().getName());
						request.setMethodName(method.getName());
						request.setParameterTypes(method.getParameterTypes());
						request.setParameters(args);
						return request;
					}			
					
					private RpcClient buildClient(RpcCall rpcCall,RequestEntity request){
						String serverAddress =null;
						List<String> serverList = null;
							
						String zkServer = rpcCall.zkServer();
						AbstractLoadBalance lb = rpcCall.loadbalance().getLoadBalance();
						if("".endsWith(zkServer)){
							String servers = clientContext.getServerMap().get(rpcCall.server());
							serverList = Arrays.asList(servers.split(","));
						}
						else{
							ZooKeeperServiceDiscover  zkDiscover = clientContext.getZkServerMap().get(zkServer);
							serverList = zkDiscover.getServerList();												
						}
						serverAddress = lb.select(serverList, request);
						String[] array = serverAddress.split(":");
						String host = array[0];
						int port = Integer.parseInt(array[1]);
						return new RpcClient(host, port, rpcCall.serializer().getSerializer());
					}
				});
	}
}
