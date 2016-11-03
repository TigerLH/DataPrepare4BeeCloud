package com.beecloud.bean;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年11月2日 下午2:26:51
 * @version v1.0
 */
public class ColumnInfo {
	private String name;
	private String type;
	private boolean nullable;
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
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	@Override
	public String toString() {
		return "ColumnInfo [name=" + name + ", type=" + type + ", nullable="
				+ nullable + "]";
	}
	
}
