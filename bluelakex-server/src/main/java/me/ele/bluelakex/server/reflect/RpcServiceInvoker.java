package me.ele.bluelakex.server.reflect;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import me.ele.bluelakex.model.RequestEntity;

public class RpcServiceInvoker {

	private static Map<MapMethodKey, Method> methodCache = new HashMap<MapMethodKey, Method>();
	
	public static Object invoke(RequestEntity request,Object instance) throws Throwable{
		
		Class<?> serviceClass = instance.getClass();
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();
		
		MapMethodKey methodKey = new MapMethodKey(serviceClass,methodName, parameterTypes);
		Method method = (Method)methodCache.get(methodKey);
		if(method == null){
			synchronized(RpcServiceInvoker.methodCache){
				if(method == null){
					method = serviceClass.getMethod(methodName, parameterTypes);
					methodCache.put(new MapMethodKey(serviceClass,methodName, parameterTypes), method);					
				}
			}
		}
		return method.invoke(instance, parameters);
	}
}
