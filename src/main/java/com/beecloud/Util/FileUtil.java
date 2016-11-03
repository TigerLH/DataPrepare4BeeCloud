package com.beecloud.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @description 文件操作类
 * @author hong.lin@beecloud.com
 * @date 2016年11月2日 上午10:51:09
 * @version v1.0
 */
public class FileUtil {
	public static List<String> getSavelist(String path) throws Exception{
		List<String> saveList = new ArrayList<String>();
		File dir = new File(path);
		if(!dir.exists()){
			throw new Exception("dir is not exists");
		}
		File[] files = dir.listFiles();
		for(int i=0;i<files.length;i++){
			String filename = files[i].getName();
			saveList.add(filename);
		}
		return saveList;
	}
	
	
	/**
	 * 删除文件
	 * @param filename
	 */
	public static void delXml(String filename){
		File file = new File(filename);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**
	 * 写文件
	 * @param filename
	 * @param str
	 */
	public static void Write(String filename, String str) {
		File file = new File(filename);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream fs = new FileOutputStream(file,true);//追加
			OutputStreamWriter ow = new OutputStreamWriter(fs,"UTF-8");//字节流转化为字符流
			BufferedWriter bw = new BufferedWriter(ow);
			bw.write(str);
			bw.flush();
			bw.newLine();
			bw.flush();
			fs.close();
			ow.close();
			bw.close();
		} catch (IOException e1) {
				// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
