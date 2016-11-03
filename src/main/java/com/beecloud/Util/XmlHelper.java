package com.beecloud.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import com.beecloud.bean.ColumnInfo;
import com.beecloud.bean.config.DbInfo;
import com.beecloud.bean.config.RedisInfo;
import com.beecloud.bean.sql.DataBaseBean;
import com.beecloud.bean.sql.TableBean;
import com.beecloud.main.JWindow;


public class XmlHelper {	
	/**
	 * 合并数据
	 * @param filename
	 * @param path
	 * @param filelist
	 * @throws Exception
	 */
	public static void merge(String filename,String path,List<String> filelist) throws Exception{
		File data = new File(filename);
		if(data.exists()){
			JOptionPane.showMessageDialog(null, "文件已存在,将被覆盖", "提示", JOptionPane.YES_NO_OPTION);
		}
		data.delete();
		FileUtil.Write(filename, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		FileUtil.Write(filename, "<data>");
		for(int i=0;i<filelist.size();i++){
			FileInputStream fs = new FileInputStream(path+File.separator+filelist.get(i));
			InputStreamReader ir = new InputStreamReader(fs);
			BufferedReader br = new BufferedReader(ir);
			String line = br.readLine(); //去除第一行
			while((line = br.readLine())!=null){
				if(!line.equals("<data>")&&!line.equals("</data>")){
					FileUtil.Write(filename,line);
				}
			}
		}
		FileUtil.Write(filename, "</data>");
	}
	
	
	
	
	
	/**
	 * 加载本地Mysql数据
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws DocumentException 
	 */
	public static List<Map<String,Object>> loadXmlData(String filename) throws SAXException, IOException, ParserConfigurationException, DocumentException{
		StringBuilder b = new StringBuilder();
		BufferedReader bf  = new BufferedReader(new FileReader(new File(filename)));
		String line;
		while ((line=bf.readLine())!=null) {
			b.append(line);
		}
		Document document = DocumentHelper.parseText(b.toString());
		Element root = document.getRootElement();
		List<Element> databases = root.elements("database");
		List<Map<String,Object>> data_list = new ArrayList<Map<String,Object>>();
		for(Element database : databases) {
			String type = database.attributeValue("type");
			Element table = (Element) database.elements("table").get(0);
			String uniqueKey = table.attributeValue("uniqueKey");	//获取uniqueKey
			List<Element> columns = table.elements("column");
			List<String> column_name = new ArrayList<String>();
			for(Element column : columns) {	
				column_name.add(column.getTextTrim());	
			}
			System.out.println(column_name);
			List<Element> rows = table.elements("row");
			for(Element row : rows) {
				Map<String,Object> map = new HashMap<String,Object>();
				List<Element> values = row.elements("value");
				for(int v=0 ;v<values.size();v++) {
					map.put(column_name.get(v), values.get(v).getTextTrim());
				}
				map.put("数据类型", type);
				map.put("唯一索引", uniqueKey);
				data_list.add(map);
			}
		}
		System.out.println(data_list);
		return data_list;
	}
	
	

	
	/**
	 * 保存单个表数据
	 * @param path
	 * @param dbName
	 * @param map
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public static boolean saveDataXml(String path,String dbName,String tableName,List<Map<String,String>> map) throws ParserConfigurationException, IOException{
		boolean isSaveSucess = true;
		boolean isKeyInvaild = true;
		if(map.size()<1){	//数据为空
			return false;
		}
		File file = new File(path);
		if(!file.exists()){
			file.mkdir();
		}
		File data_file = new File(path+File.separator+dbName+"_"+tableName+".xml");
		
		/**
		 * 获取字段映射关系
		 */
		DbInfo dbinfo = JWindow.dbMap.get(dbName);
		List<ColumnInfo> ci = DbUtil.getColumnInfo(dbinfo.getUserName(), dbinfo.getPassword(), dbinfo.getUrl(), tableName);
		List<String> header = new ArrayList<String>();
		for(ColumnInfo item:ci){
			header.add(item.getName());
		}
		//过滤数据
		List<Map<String,String>> targetList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> dependList = new ArrayList<Map<String,String>>();
		List<Map<String,String>> checkList = new ArrayList<Map<String,String>>();
		for(Map<String,String> item:map){
			String type = item.get("数据类型");
			if("".equals(type)){
				JOptionPane.showMessageDialog(null, "\"数据类型\"不能为空", "提示", JOptionPane.CLOSED_OPTION);
				return false;
			}
			if("target".equals(type)){
				targetList.add(item);
			}
			if("depend".equals(type)){
				dependList.add(item);
			}
			if("check".equals(type)){
				checkList.add(item);
			}
		}
		System.out.println("dependList:"+dependList);
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("data");
	
		/**
		 * 保存type为target的数据
		 */
		if(targetList.size()>0){
			Element database = root.addElement("database");
			database.addAttribute("name", dbName);
			database.addAttribute("type", "target");
			Element table = database.addElement("table");
			table.addAttribute("name", tableName);
			for(int i=0;i<ci.size();i++){
				Map<String,String> index_0 = targetList.get(0);	//取第一条数据过滤,确定列名
				table.addAttribute("uniqueKey",index_0.get("唯一索引"));
				if(!header.contains(index_0.get("唯一索引"))&&!"".equals(index_0.get("唯一索引"))){
						isKeyInvaild = false;
					}
					Element column = table.addElement("column");
					column.addAttribute("type", ci.get(i).getType());
					column.setText(ci.get(i).getName());
				}
			
			for(int i=0;i<targetList.size();i++){
				Map<String,String> target = targetList.get(i);
				Element row = table.addElement("row");
				for(int j=0;j<header.size();j++){
					Element value = row.addElement("value");
					value.setText(target.get(header.get(j)));
				}
			}
		}
		
		/**
		 * 保存type为depend的数据
		 */
		if(dependList.size()>0){
			Element database = root.addElement("database");
			database.addAttribute("name", dbName);
			database.addAttribute("type", "depend");
			Element table = database.addElement("table");
			table.addAttribute("name", tableName);
			for(int i=0;i<ci.size();i++){
				Map<String,String> index_0 = dependList.get(0);	//取第一条数据过滤,确定列名
				table.addAttribute("uniqueKey",index_0.get("唯一索引"));
				if(!header.contains(index_0.get("唯一索引"))&&!"".equals(index_0.get("唯一索引"))){
						isKeyInvaild = false;
					}
					Element column = table.addElement("column");
					column.addAttribute("type", ci.get(i).getType());
					column.setText(ci.get(i).getName());
				}
			
			for(int i=0;i<dependList.size();i++){
				Map<String,String> depend = dependList.get(i);
				Element row = table.addElement("row");
				for(int j=0;j<header.size();j++){
					Element value = row.addElement("value");
					value.setText(depend.get(header.get(j)));
				}
			}
		}
		
		
		
		/**
		 * 保存type为check的数据
		 */
		if(checkList.size()>0){
			Element database = root.addElement("database");
			database.addAttribute("name", dbName);
			database.addAttribute("type", "check");
			Element table = database.addElement("table");
			table.addAttribute("name", tableName);
			for(int i=0;i<ci.size();i++){
				Map<String,String> index_0 = checkList.get(0);	//取第一条数据过滤,确定列名
				table.addAttribute("uniqueKey",index_0.get("唯一索引"));
				if(!header.contains(index_0.get("唯一索引"))&&!"".equals(index_0.get("唯一索引"))){
						isKeyInvaild = false;
					}
					Element column = table.addElement("column");
					column.addAttribute("type", ci.get(i).getType());
					column.setText(ci.get(i).getName());
				}
			
			for(int i=0;i<checkList.size();i++){
				Map<String,String> check = checkList.get(i);
				Element row = table.addElement("row");
				for(int j=0;j<header.size();j++){
					Element value = row.addElement("value");
					value.setText(check.get(header.get(j)));
				}
			}
		}
			
			if(!isKeyInvaild){	
				JOptionPane.showMessageDialog(null, "\"唯一索引\"填写错误,应为表中某一字段", "提示", JOptionPane.CLOSED_OPTION);
				return false;
			}
			
		FileOutputStream fos = new FileOutputStream(data_file);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		OutputFormat ouf = new OutputFormat();
		ouf.setEncoding("UTF-8");
		ouf.setIndent(true);
		ouf.setNewlines(true);
		XMLWriter writer = new XMLWriter(osw, ouf);
		writer.write(document);
		writer.close();
		return isSaveSucess;
	}
	
	
	
	/**
	 * 保存Redis连接配置
	 * @param Path
	 * @param dbName
	 * @param host
	 * @param port
	 * @param index
	 * @param auth
	 * @throws ParserConfigurationException
	 * @throws IOException 
	 */
	public static void createRedisXml(String Path,String dbName,String host,int port,int index,String auth) throws ParserConfigurationException, IOException{
		File file = new File(Path);
		if(!file.exists()){
			file.mkdir();
		}
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("database");
		root.addAttribute("name", dbName);
		
		Element element_ip = root.addElement("host");
		element_ip.setText(host);
		
		Element element_port = root.addElement("port");
		element_port.setText(String.valueOf(port));
		
		Element element_auth = root.addElement("auth");
		element_auth.setText(auth);
		
		Element element_index = root.addElement("index");
		element_index.setText(String.valueOf(index));
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		OutputFormat ouf = new OutputFormat();
		ouf.setEncoding("UTF-8");
		ouf.setIndent(true);
		ouf.setNewlines(true);
		XMLWriter writer = new XMLWriter(osw, ouf);
		writer.write(document);
		writer.close();	
	}
	
	
	
	/**
	 * 创建Mysql数据库配置文件
	 * @param ip
	 * @param port
	 * @param userName
	 * @param passWord
	 * @param dbName
	 * @throws ParserConfigurationException
	 * @throws IOException 
	 */
	public static void createXml(String Path,String ip,String port,String userName,String passWord,String dbName) throws ParserConfigurationException, IOException{
		File file = new File(Path);
		if(!file.exists()){
			file.mkdir();
		}
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("database");
		root.addAttribute("name", dbName);
	
		Element element_ip = root.addElement("ip");
		element_ip.setText(ip);
		
		Element element_port = root.addElement("port");
		element_port.setText(port);
		
		Element element_userName = root.addElement("userName");
		element_userName.setText(userName);
		
		Element element_passWord = root.addElement("passWord");
		element_passWord.setText(passWord);

		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		OutputFormat ouf = new OutputFormat();
		ouf.setEncoding("UTF-8");
		ouf.setIndent(true);
		ouf.setNewlines(true);
		XMLWriter writer = new XMLWriter(osw, ouf);
		writer.write(document);
		writer.close();
	}
	
	
	
	/**
	 * 加载本地Reids数据
	 * @param filename
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws DocumentException 
	 */
	public static Map<String,Map<String,String>> loadRedisData(String filename) throws ParserConfigurationException, SAXException, IOException, DocumentException{
		Map<String,Map<String,String>> redisMap = new  HashMap<String,Map<String,String>>();
		StringBuilder b = new StringBuilder();
		BufferedReader bf  = new BufferedReader(new FileReader(new File(filename)));
		String line;
		while ((line=bf.readLine())!=null) {
			b.append(line);
		}
		Document document = DocumentHelper.parseText(b.toString());
		Element root = document.getRootElement();
		List<Element> elements = root.elements("database");
		for(Element database : elements) {
			String type = database.attributeValue("type");
			Element table = (Element) database.elements("table").get(0);
			List<Element> columns = table.elements("column");
			List<Element> rows = table.elements("value");
			for(int r=0;r<rows.size();r++){
				Map<String, String> map = new HashMap<String,String>();
				List<Element> values = rows.get(r).elements("value");
					for(int v=0;v<values.size();v++){
						String value = values.get(v).getTextTrim();
						map.put(columns.get(r).getTextTrim(),value);
				}
					redisMap.put(type, map);
			}
		}
				return redisMap;
	}
	/**
	 * 保存Redis数据
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public static void saveRedisData(String path ,String dbName ,String tableName,Map<String,Map<String,String>> map) throws ParserConfigurationException, IOException{
		  File file = new File(path+File.separator+dbName+".xml");
		  Document document = DocumentHelper.createDocument();
			Element root = document.addElement("data");
			for(String set:map.keySet()){
				if("check".equals(set)){
					Element database = root.addElement("database");
					database.addAttribute("name", dbName);
					database.addAttribute("dbType", "redis");
					Element table = database.addElement("table");
					table.addAttribute("name", "index");
					database.addAttribute("type", "check");
					Map<String,String> kv = map.get(set);
					for(String item:kv.keySet()){
						Element column = table.addElement("column");	
						column.setText(item);
					}
			
					Element row = table.addElement("row");
					for(String item:kv.keySet()){
						Element value = row.addElement("value");
						value.setText(kv.get(item));		
					}		
				}else if("depend".equals(set)){
					Element database = root.addElement("database");
					database.addAttribute("name", dbName);
					database.addAttribute("dbType", "redis");
					Element table = database.addElement("table");
					table.addAttribute("name", "index");
					database.addAttribute("type", "depend");
					Map<String,String> kv = map.get(set);
					for(String item:kv.keySet()){
						Element column = table.addElement("column");	
						column.setText(item);
					}
			
					Element row = table.addElement("row");
					for(String item:kv.keySet()){
						Element value = row.addElement("value");
						value.setText(kv.get(item));		
					}		
				}else if("target".equals(set)){
					Element database = root.addElement("database");
					database.addAttribute("name", dbName);
					database.addAttribute("dbType", "redis");
					Element table = database.addElement("table");
					table.addAttribute("name", "index");
					database.addAttribute("type", "target");
					Map<String,String> kv = map.get(set);
					for(String item:kv.keySet()){
						Element column = table.addElement("column");	
						column.setText(item);
					}
			
					Element row = table.addElement("row");
					for(String item:kv.keySet()){
						Element value = row.addElement("value");
						value.setText(kv.get(item));		
					}		
				
					FileOutputStream fos = new FileOutputStream(file);
					OutputStreamWriter osw = new OutputStreamWriter(fos);
					OutputFormat ouf = new OutputFormat();
					ouf.setEncoding("UTF-8");
					ouf.setIndent(true);
					ouf.setNewlines(true);
					XMLWriter writer = new XMLWriter(osw, ouf);
					writer.write(document);
					writer.close();
				}
			}
	}
	
	
	/**
	 * 获取Redis配置信息
	 * @param filename
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public static RedisInfo getRedisInfo(String filename) throws ParserConfigurationException, SAXException, IOException, DocumentException{
		RedisInfo redisInfo = new RedisInfo();
		DbInfo dbInfo = new DbInfo();
		Map<String,Map<String,String>> redisMap = new  HashMap<String,Map<String,String>>();
		StringBuilder b = new StringBuilder();
		BufferedReader bf  = new BufferedReader(new FileReader(new File(filename)));
		String line;
		while ((line=bf.readLine())!=null) {
			b.append(line);
		}
		Document document = DocumentHelper.parseText(b.toString());
		Element root = document.getRootElement();
		String name = root.attributeValue("name");
		String host = root.elementText("host");
		int port = Integer.parseInt(root.elementText("port"));
		String auth = root.elementText("auth");
		int index = Integer.parseInt(root.elementText("index"));
		redisInfo.setHost(host);
		redisInfo.setPort(port);
		redisInfo.setAuth(auth);
		redisInfo.setIndex(index);
		redisInfo.setName(name);
		return redisInfo;
	}
	
	
	/**
	 * 通过配置文件获取DbInfo对象
	 * @param filename
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public static DbInfo getDbInfo(String filename) throws ParserConfigurationException, SAXException, IOException, DocumentException{
		DbInfo dbInfo = new DbInfo();
		Map<String,Map<String,String>> redisMap = new  HashMap<String,Map<String,String>>();
		StringBuilder b = new StringBuilder();
		BufferedReader bf  = new BufferedReader(new FileReader(new File(filename)));
		String line;
		while ((line=bf.readLine())!=null) {
			b.append(line);
		}
		Document document = DocumentHelper.parseText(b.toString());
		Element root = document.getRootElement();
	
		String name = root.attributeValue("name");
		
		
		String ip = root.elementText("ip");
		String port = root.elementText("port");
		String userName = root.elementText("userName");
		String passWord = root.elementText("passWord");
		dbInfo.setDbName(name);
		dbInfo.setIp(ip);
		dbInfo.setPort(port);
		dbInfo.setUserName(userName);
		dbInfo.setPassword(passWord);
		return dbInfo;
	}
	
	
	public static List<DataBaseBean> getDataBase(String path){
		List<DataBaseBean> data = null;
		StringBuffer b = new StringBuffer();
		try {
			@SuppressWarnings("resource")
			BufferedReader bf  = new BufferedReader(new FileReader(new File(path)));
			String line;
			while ((line=bf.readLine())!=null) {
				b.append(line);
			}
			Document document = DocumentHelper.parseText(b.toString());
			Element root = document.getRootElement();
			data = getDataFromRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return data;
	}
	

	
	@SuppressWarnings("unchecked")
	private static List<DataBaseBean> getDataFromRoot(Element root) {
		List<Element> databases = root.elements("database");
		List<DataBaseBean> listDatabase = new ArrayList<DataBaseBean>();
		//解析database
		for (Element database : databases){
			DataBaseBean dataBaseBean = new DataBaseBean();
			List<TableBean> listTable = new ArrayList<TableBean>();			
			String name = database.attributeValue("name");
			String type = database.attributeValue("type");
			String dbType = database.attributeValue("dbType")==null?"mysql":database.attributeValue("dbType");
			dataBaseBean.setDbType(dbType);
			dataBaseBean.setName(name);
			dataBaseBean.setType(type);
			List<Element> tables = database.elements("table");
			//解析table
			for (Element table : tables){
				TableBean tableBean = new TableBean();
				String tableName = table.attributeValue("name");
				tableBean.setName(tableName);
				String uniqueKey = table.attributeValue("uniqueKey")==null?"":table.attributeValue("uniqueKey");
				tableBean.setUniqueKey(uniqueKey);
				List<Element> columns = table.elements("column");
				List<Element> rows = table.elements("row");
				List<Map<String, String>> listKV = new ArrayList<Map<String, String>>();
				for (Element row : rows){			
				  	List<Element> values = row.elements("value");
				  	Map<String, String> m = new HashMap<String, String>();
				  	if (values.size()== columns.size()){
				  		for (int i = 0 ;i < values.size() ; i++){
				  			m.put(columns.get(i).getTextTrim(), values.get(i).getTextTrim());	
				  		}
				  	}
				  	else {
				  		System.out.println("数据有误");
				  	}
				  	listKV.add(m);
				}
				tableBean.setListKV(listKV);
				listTable.add(tableBean);
			}
			dataBaseBean.setListTable(listTable);
			listDatabase.add(dataBaseBean);
		}
		return listDatabase;
	}
	
	
	public static void main(String[] args) throws IOException, DocumentException {
		System.out.println(getDataBase("data/mock_rule.xml"));
	}
}
