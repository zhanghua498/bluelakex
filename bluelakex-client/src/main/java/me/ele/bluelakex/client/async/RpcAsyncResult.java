package me.ele.bluelakex.client.async;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.LazyLoader;
import me.ele.bluelakex.model.ResponseEntity;

public class RpcAsyncResult {
	
	private static final Logger Logger = LoggerFactory.getLogger(RpcAsyncResult.class);
			
    private Class<?>  returnClass;
    private Future<ResponseEntity> future;
    
	public RpcAsyncResult(Class<?> returnClass, Future<ResponseEntity> future) {
		this.returnClass = returnClass;
		this.future = future;
	}
    
	public Object getProxy() {
		Enhancer enhancer = new Enhancer();
		if (returnClass.isInterface()) 
			enhancer.setInterfaces(new Class[] {returnClass});
		else
			enhancer.setSuperclass(returnClass);
		enhancer.setCallback(new AsyncLoadResultInterceptor());
		return enhancer.create();
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
