package me.ele.bluelakex.zookeeper;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZooKeeperServiceRegister extends ZooKeeperConnect {

	private static final Logger log = LoggerFactory.getLogger(ZooKeeperServiceRegister.class);
	
	public ZooKeeperServiceRegister(String connectString) {
		super(connectString);
	}
	
	public ZooKeeperServiceRegister(String connectString,String serverPaths){
		this(connectString,serverPaths,false);
	}
	
	public ZooKeeperServiceRegister(String connectString,String serverPaths,boolean isInit) {
		super(connectString);
		if(isInit){
			registerServerAndInit(serverPaths);
		}
		else{
			registerServer(serverPaths);
		}
	}
	
	public void registerServerAndInit(String serverPaths) {
    	deleteNode();
    	registerServer(serverPaths);
    }
    
    public void registerServer(String serverPaths) {
        if (serverPaths != null) { 	 
             try {
            	if(zk.exists(registryNode,true) == null){
            		String path = zk.create(registryNode, registryNode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
              		log.debug("create zk node ({} : {})", path, serverPaths);
            	}
            	String[] servers = serverPaths.split(",");
            	for(String serverPath:servers){
            		String path = zk.create(serverNode, serverPath.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            		log.debug("create zk node ({} : {})", path, serverPath);
            	}
             } catch (KeeperException | InterruptedException e) {
				log.error("", e);
			}
        }
    }

	private void deleteNode(){
    	try {
    		if(zk.exists(registryNode,true) != null){
	    		List<String> nodeList = zk.getChildren(registryNode, true);
	    		for(String node:nodeList){
	    			zk.delete(registryNode + "/" + node,-1);
	    		}
				zk.delete(registryNode,-1);
				log.debug("delete zk node : {}", registryNode);
    		}
		} catch (InterruptedException | KeeperException e) {
			log.error("", e);
		}
    }
}
