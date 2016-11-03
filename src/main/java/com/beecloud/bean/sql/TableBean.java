package com.beecloud.bean.sql;

import java.util.List;
import java.util.Map;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016��11��3�� ����1:42:58
 * @version v1.0
 */
public class TableBean {
	
	/**
	 * ������
	 */
	private String name;
	/**
	 * Ψһ����
	 */
	private String uniqueKey;
	/**
	 * �����ֶ�ӳ���ϵ
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
