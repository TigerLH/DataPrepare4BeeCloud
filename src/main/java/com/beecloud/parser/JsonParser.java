package com.beecloud.parser;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016年11月2日 下午2:30:48
 * @version v1.0
 */
public class JsonParser {
	/**
	 * 获取登录后的Session Id
	 * @param body
	 * @return
	 */
	public static String parseSid(String body){
		JSONObject mJSONObject = JSONObject.fromObject(body);
		String content = mJSONObject.getString("content");
		JSONObject sidObject = JSONObject.fromObject(content);
		String sid = sidObject.getString("sid");
		return sid;
	}
	
	
	/**
	 * 解析产品信息
	 * @param body
	 * @return
	 */
	public static Map<String,String> parseProduct(String body){
		Map<String,String> map = new HashMap<String,String>();
		JSONObject mJSONObject = JSONObject.fromObject(body);
		String content = mJSONObject.getString("content");
		JSONArray mJsonArray = JSONArray.fromObject(content);
		for(int i=0;i<mJsonArray.size();i++){
			JSONObject jb = mJsonArray.getJSONObject(i);
			String productUuid = jb.getString("productUuid");
			String productName = jb.getString("productName");
			map.put(productName, productUuid);
		}
		return map;
	}
	
	
	/**
	 * 解析版本信息
	 * @param body
	 * @return
	 */
	public static Map<String,String> parseVersion(String body){
		Map<String,String> map = new HashMap<String,String>();
		JSONObject mJSONObject = JSONObject.fromObject(body);
		String content = mJSONObject.getString("content");
		JSONArray mJsonArray = JSONArray.fromObject(content);
		for(int i=0;i<mJsonArray.size();i++){
			JSONObject jb = mJsonArray.getJSONObject(i);
			String uuid = jb.getString("uuid");
			String version = jb.getString("version");
			map.put(version, uuid);
		}
		return map;
	}
	
	
	
	/**
	 * 解析API信息
	 * @param body
	 * @return
	 */
	public static Map<String,String> parseAPI(String body){
		Map<String,String> map = new HashMap<String,String>();
		JSONArray mJsonArray = JSONObject.fromObject(body).getJSONObject("content").getJSONObject("page").getJSONArray("datas");
		for(int i=0;i<mJsonArray.size();i++){
			JSONObject jb = mJsonArray.getJSONObject(i);
			String uuid = jb.getString("uuid");
			String name = jb.getString("name");
			map.put(name, uuid);
		}
		return map;
	}
	
	
	
}
