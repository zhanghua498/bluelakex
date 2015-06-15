package me.ele.bluelakex.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZooKeeperConnect implements Watcher{
	
	private static final Logger log = LoggerFactory.getLogger(ZooKeeperConnect.class);
	
	private int ZK_SESSION_TIMEOUT = 10000;

	protected String registryNode = "/rpcregistry";
	
	protected String serverNode = registryNode + "/server";
	
	protected ZooKeeper zk = null;
	
	private CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public ZooKeeperConnect(String connectString){
		createConnection(connectString,ZK_SESSION_TIMEOUT);
	}
	
	public ZooKeeperConnect(String connectString,String registryNode,String serverNode){
		createConnection(connectString,ZK_SESSION_TIMEOUT);
		this.registryNode = registryNode;
		this.serverNode = serverNode;
	}
	
	/**
	 * 创建ZK连接
	 * @param connectString	 ZK服务器地址列表
	 * @param sessionTimeout   Session超时时间
	 */
	public void createConnection( String connectString, int sessionTimeout ) {
		this.releaseConnection();
		try {
			zk = new ZooKeeper( connectString, sessionTimeout,this );
			connectedSemaphore.await();
		} catch ( Exception e ) {}
	}
	
    /** 
     * 关闭ZK连接 
     */ 
    public void releaseConnection() { 
        if ( this.zk != null) { 
            try { 
                this.zk.close(); 
            } catch ( InterruptedException e ) { 
            	log.error("", e);
            } 
        } 
    }
    
	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == Event.KeeperState.SyncConnected) {
			connectedSemaphore.countDown();
        }
	}
}
