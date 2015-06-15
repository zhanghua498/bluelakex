package me.ele.bluelakex.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZooKeeperServiceDiscover extends ZooKeeperConnect {
	
	private static final Logger log = LoggerFactory.getLogger(ZooKeeperServiceDiscover.class);
	
	private volatile List<String> serverList = new ArrayList<>();

	public ZooKeeperServiceDiscover(String connectString) {
		super(connectString);
		watchNode();
	}
	
	public List<String> getServerList(){
		return serverList;
	}

	public String discoverServer() {
        String server = null;
        int size = serverList.size();
        if (size > 0) {
            if (size == 1) {
                server = serverList.get(0);
            } else {
            	server = serverList.get(ThreadLocalRandom.current().nextInt(size));
            }
        }
        log.debug("discover server: {}", server);
        return server;
    	
    }
    
    private void watchNode(){
    	try {
    		if(zk.exists(registryNode,true) != null){
				List<String> nodeList = zk.getChildren(registryNode, this);
				if(nodeList != null && nodeList.size()>0){
					List<String> tmpList = new ArrayList<>();
		            for (String node : nodeList) {
		                byte[] bytes = zk.getData(registryNode + "/" + node, false, null);
		                tmpList.add(new String(bytes));
		            }           
		            this.serverList = tmpList;
		            log.info("rpc servers: {}", tmpList);
				}
    		}	
		} catch (KeeperException | InterruptedException e) {
			log.error("", e);
		}
    }

	@Override
	public void process(WatchedEvent event) {
		super.process(event);
		if (event.getType() == Event.EventType.NodeChildrenChanged) {
            watchNode();
        }
	}
}
