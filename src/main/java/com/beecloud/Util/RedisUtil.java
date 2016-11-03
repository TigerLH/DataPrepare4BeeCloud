package com.beecloud.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisDataException;

import com.beecloud.bean.config.RedisInfo;

/** 
 * @author  linhong: 
 * @date 2016年4月7日 下午5:31:11 
 * @Description: Redis操作工具
 * @version 1.0  
 * @param <jedis>
 */
public class RedisUtil<jedis> {
	private static RedisUtil redisUtil = null;
	private static Jedis jedis = null;
	public RedisUtil(RedisInfo redisInfo){
		try{
			jedis = new JedisPool(redisInfo.getHost(),redisInfo.getPort()).getResource();
			jedis.auth(redisInfo.getAuth());
			jedis.select(redisInfo.getIndex());
		}catch(JedisDataException e){
			System.out.println("Redis Connect Exception");
		}
	}
	
//	/**
//	 * 单列模式，涉及到多次连接，不能使用单列
//	 * @return
//	 */
//	public static RedisUtil getInstance(RedisInfo redisInfo){
//		if(redisUtil == null) {    
//			synchronized (RedisUtil.class) {    
//				try {
//					redisUtil = new RedisUtil(redisInfo);
//				} catch (Exception e) {
//					// TODO 自动生成的 catch 块
//					e.printStackTrace();
//				}
//			}    
//		}    
//		return redisUtil;   
//	}
	
	
	/**
	 * 清空数据库
	 */
	public void flushDB(){
		jedis.flushDB();
	}
	
	
	/**
	 * 获取redis中所以的key
	 * @param key
	 * @return
	 */
	public  List<String> getKeys(String regex){
		List<String> list = new ArrayList<String>();
		Set<String> sets = jedis.keys(regex);
		for(String set:sets){
			list.add(set);
		}
		return list;
	}
	
	
	
	
	/**
	 * 对key赋值
	 * @param key
	 * @param value
	 */
	public void setValueBykey(String key,String value){
		jedis.set(key, value);
	}
	
	
	/**
	 * 获取Key对应的值
	 * @param key
	 * @return
	 */
	public String getValueBykey(String key){
		return jedis.get(key);
	}
	
	
	/**
	 * 删除对应的Key
	 * @param key
	 */
	public void delValueBykey(String key){
		jedis.del(key);
	}
	
	
	
	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	public boolean isKeyExists(String key){
		return jedis.exists(key);
	}
	
	
	public void bgsave(){
		String result = jedis.bgsave();
		System.out.println(result);
	}	
	
	public static void main(String[] args) {
		Test("192.168.1.8",6377);
	}
	public static void Test(String host,int port){
		jedis = new JedisPool(host,port).getResource();
		jedis.auth("56qq.cn");
		System.out.println(jedis.getDB());
//		//flushDB();
//		//bgsave();
//		Set<String> sets = jedis.keys("*");
//		for(String set:sets){
//			System.out.println(set);
//			System.out.println(jedis.get(set));
//		}
		//System.out.println(jedis.get("sessionCacheDao:userSessionCache:152_1_1065566"));
		//jedis.set("sessionCacheDao:userSessionCache:152_1_1065566", "test");
	}
}
