package com.beecloud.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.beecloud.bean.ColumnInfo;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年11月1日 上午10:01:57
 * @version v1.0
 */
public class DbUtil {
	/**
	 * 获取数据库中所有的表
	 * @param username
	 * @param password
	 * @param url
	 * @return
	 * @author Linhong
	 */
	public static List<String> getTableList(String username,String password,String url){
		  List<String> table_list = new ArrayList<String>();
		  try {
	            Class.forName("com.mysql.jdbc.Driver");
	            System.out.println("加载驱动成功!");
	        } catch (ClassNotFoundException e) {
	            System.out.println(e.getLocalizedMessage());
	            e.printStackTrace();
	        }
	        // 建立连接
	        try {
	        	Connection con = DriverManager.getConnection(url, username, password);
	        	Statement stmt = con.createStatement();
	        	ResultSet rs = stmt.executeQuery("show tables");
	        	while(rs.next()) {
	        		table_list.add(rs.getString(1));
	        		System.out.println(rs.getString(1));
	        		}
	        	stmt.close();
	        	con.close();
	        } catch(SQLException e) {
	            System.out.println(e.getLocalizedMessage());
	        }
	        return table_list;
	}
	
	
	public static List<Map<String,Object>> getDataFromDb(String username,String password,String url,String table,String condition,String limit){
		List<Map<String,Object>> data_list = new ArrayList<Map<String,Object>>();
		 try {
	            Class.forName("com.mysql.jdbc.Driver");
	            System.out.println("加载驱动成功!");
	        } catch (ClassNotFoundException e) {
	            System.out.println(e.getLocalizedMessage());
	            e.printStackTrace();
	        }
	        try {
	        	Connection con = DriverManager.getConnection(url, username, password);
	        	Statement stmt = con.createStatement();
	        	ResultSet resultSet = null;
	        	if("".equals(limit)){
	        		limit = "50";
	        	}
	        	
	        	if("".equals(condition)){
	        		resultSet = stmt.executeQuery("select * from "+table+" limit "+limit);
	        	}else{
	        		try{
	        			String sql = "select * from "+table+" where "+condition+" limit "+limit;
	        			resultSet = stmt.executeQuery(sql);
	        		}catch(Exception e){
	        			JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "错误", JOptionPane.CLOSED_OPTION);
	        		}
	        	}
	        	ResultSetMetaData metaData = resultSet.getMetaData();
	             // 获取列名  
	        	while(resultSet.next()){
	        		 Map<String,Object> map = new HashMap<String,Object>();
		        	for (int i = 0; i < metaData.getColumnCount(); i++) {  
		                // resultSet数据下标从1开始  
		                String columnName = metaData.getColumnName(i+1); 
		                String type = metaData.getColumnTypeName(i+1);
		                Object columnValue = resultSet.getObject(i+1);
		                if("DATETIME".equals(type)){							//BIT转换为0和1存储
		                	columnValue = columnValue.toString().replace(".0", "");
		                }
//		                if("id".equals(columnName)){	//过滤掉主键
//		                	continue;
//		                }
		                map.put(columnName, columnValue);
		                map.put("唯一索引", "");
		                map.put("数据类型", "");
		            }
		        	 data_list.add(map);
	        	}
	        	System.out.println(data_list);
	        	stmt.close();
	        	con.close();
	        } catch(SQLException e) {
	            e.printStackTrace();
	        }
	        return data_list;
	}
	
	
	
	
	/**
	 * 获取表结构信息:字段和类型映射关系
	 * @param username
	 * @param password
	 * @param url
	 * @param table
	 * @return Map(字段名称为key,字段类型为value)
	 */
	public static List<ColumnInfo> getColumnInfo(String username,String password,String url,String table){
		  List<ColumnInfo>  list = new ArrayList<ColumnInfo>();
		  try {
	            Class.forName("com.mysql.jdbc.Driver"); 
	            System.out.println("加载驱动成功!");
	        } catch (ClassNotFoundException e) {
	            System.out.println(e.getLocalizedMessage());
	            e.printStackTrace();
	        }
	        try {
	        	Connection con = DriverManager.getConnection(url, username, password);
	        	Statement stmt = con.createStatement();
	        	ResultSet resultSet = stmt.executeQuery("select * from "+table+" limit 1");  
	             // 获取列名  
	        	ResultSetMetaData metaData = resultSet.getMetaData();
	        	for (int i = 0; i < metaData.getColumnCount(); i++) {  
	                // resultSet数据下标从1开始  
	        		ColumnInfo ci = new ColumnInfo();
	                String columnName = metaData.getColumnName(i + 1);
	                String columnType = metaData.getColumnTypeName(i + 1);
	                int NullAble = metaData.isNullable(i+1);
	                boolean isNullAble = (NullAble==ResultSetMetaData.columnNullable)?true:false;
	                ci.setName(columnName);
	                ci.setType(columnType);
	                ci.setNullable(isNullAble);
	                list.add(ci);
	            }
	        	stmt.close();
	        	con.close();
	        } catch(SQLException e) {
	            System.out.println(e.getLocalizedMessage());
	        }
	        System.out.println("list:"+list);
	        return list;
	}
	
	
	/**
	 * 获取数据表结构
	 * @param username
	 * @param password
	 * @param url
	 * @param table
	 * @return
	 * @author Linhong
	 */
	public static List<String> getFieldList(String username,String password,String url,String table){
		  List<String> field_list = new ArrayList<String>();
		  try {
	            Class.forName("com.mysql.jdbc.Driver"); 
	            System.out.println("加载驱动成功!");
	        } catch (ClassNotFoundException e) {
	            System.out.println(e.getLocalizedMessage());
	            e.printStackTrace();
	        }
	        try {
	        	Connection con = DriverManager.getConnection(url, username, password);
	        	Statement stmt = con.createStatement();
	        	ResultSet resultSet = stmt.executeQuery("select * from "+table+" limit 1");  
	             // 获取列名  
	        	ResultSetMetaData metaData = resultSet.getMetaData();
	        	for (int i = 0; i < metaData.getColumnCount(); i++) {  
	                // resultSet数据下标从1开始  
	                String columnName = metaData.getColumnName(i + 1);  
	                field_list.add(columnName);  
	            }
	        	stmt.close();
	        	con.close();
	        	field_list.add("唯一索引");
	        	field_list.add("数据类型");
	        } catch(SQLException e) {
	            System.out.println(e.getLocalizedMessage());
	        }
	        return field_list;
	}
	
}
