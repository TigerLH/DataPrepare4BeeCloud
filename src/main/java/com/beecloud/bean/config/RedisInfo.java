package com.beecloud.bean.config;
/** 
 * @author  linhong: 
 * @date 2016年5月4日 下午8:28:47 
 * @Description: TODO
 * @version 1.0  
 */
public class RedisInfo {
	/**
	 * Redis host
	 */
	private String host;
	
	/**
	 * Redis port
	 */
	private int port;
	
	/**
	 * Redis 认证
	 */
	private String auth;
	
	/**
	 * Redis db index
	 */
	private int index;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "RedisInfo [host=" + host + ", port=" + port + ", auth=" + auth
				+ ", index=" + index + ", name=" + name + "]";
	}



	
}
