package me.ele.bluelakex.client;

import java.util.HashMap;

import me.ele.bluelakex.zookeeper.ZooKeeperServiceDiscover;

public class RpcClientContext{
	
	private HashMap<String, String> serverMap;
	private HashMap<String, ZooKeeperServiceDiscover> zkServerMap;
	
	public HashMap<String, String> getServerMap() {
		return serverMap;
	}
	public void setServerMap(HashMap<String, String> serverMap) {
		this.serverMap = serverMap;
	}
	public HashMap<String, ZooKeeperServiceDiscover> getZkServerMap() {
		return zkServerMap;
	}
	public void setZkServerMap(HashMap<String, ZooKeeperServiceDiscover> zkServerMap) {
		this.zkServerMap = zkServerMap;
	}
}
