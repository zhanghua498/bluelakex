package me.ele.bluelakex.client.async;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcAsyncExecutor {
	public static final int DEFAULT_POOL_SIZE    = 20;
	public static final int DEFAULT_ACCEPT_COUNT = 100;
	 
	private int corePoolSize;
	private int maximumPoolSize;
	private int acceptCount;
	
	public RpcAsyncExecutor(){
		this(DEFAULT_POOL_SIZE,DEFAULT_ACCEPT_COUNT);
	}
	
	public RpcAsyncExecutor(int poolSize,int acceptCount){
		this.corePoolSize = poolSize;
		this.maximumPoolSize = poolSize;
		this.acceptCount = acceptCount;
	} 
	
	public ExecutorService init(){
		return new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                0L, TimeUnit.MILLISECONDS,
                getBlockingQueue(acceptCount));
	}
	
	private BlockingQueue<Runnable> getBlockingQueue(int acceptCount) {
        if (acceptCount <= 0) {
            return new LinkedBlockingQueue<Runnable>();
        } else {
            return new ArrayBlockingQueue<Runnable>(acceptCount);
        }
    }
	
	public int getCorePoolSize() {
		return corePoolSize;
	}
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}
	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}
	public int getAcceptCount() {
		return acceptCount;
	}
	public void setAcceptCount(int acceptCount) {
		this.acceptCount = acceptCount;
	}
	 
	 
}
