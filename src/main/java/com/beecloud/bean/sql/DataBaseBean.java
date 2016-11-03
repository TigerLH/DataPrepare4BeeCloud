package com.beecloud.bean.sql;

import java.util.List;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016��11��3�� ����1:40:27
 * @version v1.0
 */
public class DataBaseBean {
	/**
	 * ���ݿ�����
	 */
	private String name;
	
	/**
	 * ��������:depend/target
	 */
	private String type;
	
	/**
	 * ���ݿ�����:mysql/redis
	 */
	private String dbType;
	
	/**
	 * ��ṹ
	 */
	private List<TableBean> listTable;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TableBean> getListTable() {
		return listTable;
	}

	public void setListTable(List<TableBean> listTable) {
		this.listTable = listTable;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	@Override
	public String toString() {
		return "DataBaseBean [name=" + name + ", type=" + type + ", dbType=" + dbType + ", listTable=" + listTable
				+ "]";
	}
	
	
}
