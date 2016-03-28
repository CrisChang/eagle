package com.poison.eagle.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.eagle.manager.PostManager;

public class MessageUtils {
	private static String msg ;
	private static final  Log LOG = LogFactory.getLog(MessageUtils.class);
	
	
	/*
	 * 通常情况下项目的数据库的相关的配置信息 并不写到java类中  应该让用户可以去更改配置信息
	 * 所有解决办法 ：将数据库的配置信息放到一个用户可以更改的文件中  而在使用该类时 只需读取
	 * 该文件 将配置内容赋给上面的变量即可
	 * 存放类似于数据库的配置信息的文件  .xml,.properties
	 * 
	 * 在src的根目录下面创建message.properties文件
	 */
	
	/**
	 * 项目中的错误信息通常不会写在java类中，而是放在用户可以修改的配置文件中
	 * 在 /com/poison/eagle/utils 目录下创建message.properties
	 * @param keys
	 * @return
	 */
	public static String getResultMessage(int keys){
		
		String key = String.valueOf(keys);
		InputStream is = null;
		try {
			is = MessageUtils.class.getResourceAsStream("/com/poison/eagle/utils/message.properties"); 
			Properties p = new Properties();
			p.load(is);
			msg = p.getProperty(key);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		} finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(msg== null){
			msg = CommentUtils.ERROR_NOTUNKOWN;
		}
		return msg;
	}
}
