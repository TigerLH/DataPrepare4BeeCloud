package com.beecloud.bean.sql;

import java.util.List;
import java.util.Map;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年11月3日 下午1:42:58
 * @version v1.0
 */
public class TableBean {
	
	/**
	 * 表名称
	 */
	private String name;
	/**
	 * 唯一索引
	 */
	private String uniqueKey;
	/**
	 * 表中字段映射关系
	 */
	private List<Map<String, String>> listKV;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public String getUniqueKey() {
		return uniqueKey;
	}

	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public List<Map<String, String>> getListKV() {
		return listKV;
	}

	public void setListKV(List<Map<String, String>> listKV) {
		this.listKV = listKV;
	}

	@Override
	public String toString() {
		return "TableBean [name=" + name + ", uniqueKey=" + uniqueKey + ", listKV=" + listKV + "]";
	}
	
}
