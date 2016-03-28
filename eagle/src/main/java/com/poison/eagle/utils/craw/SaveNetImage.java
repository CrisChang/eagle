package com.poison.eagle.utils.craw;




import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SaveNetImage {
	private final Log log=LogFactory.getLog(this.getClass());
	/**
	 * 保存网络图片
	 * @param srcPath 网络图片路径
	 * @param targetDirectoryPath 保存的文件夹
	 * @param targetFileName	保存的文件名称
	 * @return
	 */
	public SaveNetImageResult save(String srcPath,String targetDirectoryPath,String targetFileName){
		SaveNetImageResult sniRs = new SaveNetImageResult();
		HttpURLConnection connection = null;
		DataInputStream in = null;
		DataOutputStream out = null;
		try {
			//创建文件夹对象
			File saveDirectory = new File(targetDirectoryPath);
			//不是文件夹的情况下创建文件夹
			if(saveDirectory.isDirectory()==false){
				saveDirectory.mkdirs();
			}
			//创建文件对象
			File saveFile = new File(targetFileName);
			
			if(saveFile.isFile() == false){
			    URL url = new URL(srcPath);
			    connection = (HttpURLConnection) url.openConnection();
			    in = new DataInputStream(connection.getInputStream());
			    out = new DataOutputStream(new FileOutputStream(targetDirectoryPath+targetFileName));
			    byte[] buffer = new byte[4096];
			    int count = 0;
			    while ((count = in.read(buffer)) > 0) {
			     out.write(buffer, 0, count);
			    }
				log.info(srcPath+"成功保存");
			}else{
				log.info(srcPath+"已经存在,不保存");
			}

		   } catch (Exception e) {
			   log.error("抓取错误:"+srcPath);
			   log.error(srcPath+"错误:"+e.getMessage());
			   sniRs.setSuccess(false);
			   sniRs.setMessage(e.getMessage());
		   }
		   finally{
			    if(connection !=null){
			    	connection.disconnect();
			    }
			    try {
			    	if(out!= null){
			    		out.close();
			    	}
					if(in!=null){
						in.close();
					}
				} catch (IOException e) {
					log.error("关闭异常"+e.getMessage());
					e.printStackTrace();
				}
		   }
		   sniRs.setSuccess(true);
		   sniRs.setMessage("抓取成功,无错误");
		   return sniRs;
	}
}
