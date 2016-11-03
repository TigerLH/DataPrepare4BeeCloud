package com.beecloud.bean.config;

/**
 * @author Linhong
 */
public class DbInfo {
	/**
	 * 数据库连接的用户名
	 */
	private String userName;
	
	/**
	 * 数据库连接的密码
	 */
	private String password;
	
	/**
	 * 数据库连接的ip
	 */
	private	String ip;
	
	/**
	 * 数据库连接的端口号
	 */
	private String port;
	
	/**
	 * 需要连接的数据库名称
	 */
	private String dbName;
	
	/**
	 * JDBC连接参数Url
	 */
	private String url;
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUrl() {
		return "jdbc:mysql://"+ip+":"+port+"/"+dbName;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "dbInfo [userName=" + userName + ", password=" + password
				+ ", ip=" + ip + ", port=" + port + ", dbName=" + dbName
				+ ", url=" + url + "]";
	}
}
