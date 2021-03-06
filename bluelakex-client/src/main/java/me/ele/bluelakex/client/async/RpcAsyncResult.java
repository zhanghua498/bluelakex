package me.ele.bluelakex.client.async;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;
import me.ele.bluelakex.model.ResponseEntity;
import me.ele.bluelakex.utils.ReflectionUtils;

public class RpcAsyncResult {
	
	private static final Logger Logger = LoggerFactory.getLogger(RpcAsyncResult.class);
			
    private Class<?>  returnClass;
    private Future<ResponseEntity> future;
    
    private static Map<String,Class<?>> proxyCache = new HashMap<String,Class<?>>();
    
	public RpcAsyncResult(Class<?> returnClass, Future<ResponseEntity> future) {
		this.returnClass = returnClass;
		this.future = future;
	}
    
	public Object getProxy() {
		Class<?> proxyClass = proxyCache.get(returnClass.getName());
		if(proxyClass == null){
			Enhancer enhancer = new Enhancer();
			if (returnClass.isInterface()) 
				enhancer.setInterfaces(new Class[] {returnClass});
			else
				enhancer.setSuperclass(returnClass);
			enhancer.setCallbackType(AsyncLoadResultInterceptor.class);
			proxyClass = enhancer.createClass();
			proxyCache.put(returnClass.getName(), proxyClass);
		}
		Enhancer.registerCallbacks(proxyClass, new Callback[] {new AsyncLoadResultInterceptor()});
		try{
			return ReflectionUtils.newInstance(proxyClass);
		}
		finally{
			Enhancer.registerCallbacks(proxyClass, null);
		}
	}
	
	class AsyncLoadResultInterceptor implements LazyLoader {

        public Object loadObject() throws Exception {         
        	ResponseEntity response =  future.get();
        	if (response.isError()) {
        		Logger.error("AsyncResult caught exception",response.getError());
        		return null;
        	} else {
        		return response.getResult();
        	}
        }

    }
}
