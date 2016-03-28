package com.poison.eagle.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.poison.eagle.manager.ActManager;

public class HttpUtils {
	public static final String HTTPUTIHL_UPLOAD_IMAGE_URL = CommentUtils.WEB_SERVER_FOR_IMAGE+"/upload_image.do";
	public static final String TTPUTIL_UPLOAD_IMAGE_URL_FOR_SIZE = CommentUtils.WEB_SERVER_FOR_IMAGE+"/upload_image_s.do";
	public static final String HTTPUTIL_UPLOAD_IMAGE_URL_FOR_QRCODE = CommentUtils.WEB_SERVER_FOR_IMAGE+"/qrcode/upload_image_s.do";
	public static final String HTTPUTIL_UPLOAD_IMAGE_URL_FOR_ARTICLE = CommentUtils.WEB_SERVER_FOR_IMAGE+"/article/upload_image_s.do";
	public static final String HTTPUTIL_UPLOAD_IMAGE_URL_FOR_GRAPHIC_FILM = CommentUtils.WEB_SERVER_FOR_IMAGE+"/graphic_film/upload_image_s.do";
	public static final String HTTPUTIL_UPLOAD_IMAGE_URL_FOR_SERIALIZE = CommentUtils.WEB_SERVER_FOR_IMAGE+"/serialize/upload_image_s.do";
	public static final String HTTPUTIL_UPLOAD_FILE_URL_FOR_ARTICLE = CommentUtils.WEB_SERVER_FOR_FILE+"/article/upload_file.do";
	public static final String HTTPUTIL_UPLOAD_IMAGE_URL_FOR_LOGIN = CommentUtils.WEB_SERVER_FOR_IMAGE+"/twocode/upload_image_s.do";
	private static HttpUtils httpUtils;
	public HttpUtils() {
	}
	public static HttpUtils getInstance() {
		if (httpUtils == null) {
			return new HttpUtils();
		} else {
			return httpUtils;
		}
	}
	private static final  Log LOG = LogFactory.getLog(HttpUtils.class);
	
	private static int maxSize = 10*1024*1024;

	public String uploadFile(String filePath, String savePath) {
		String targetURL = null; // -- 指定URL
		File targetFile = null; // -- 指定上传文件
		String imgPath = "";
		
		targetFile = new File(filePath);//"E:\\workspases\\MyEclipse10\\eagle.product\\eagle\\src\\main\\webapp\\images\\index_phone.png");
		targetURL = savePath;//"http://112.126.68.72:8001/upload_image.do"; // servleturl
		PostMethod filePost = new PostMethod(targetURL);

		try {

			// 通过以下方法可以模拟页面参数提交
//			filePost.setParameter("name", "files");
//			 filePost.setParameter("filename", "sss.png");
//			 filePost.setParameter("Content-Type","image/png");
//			 filePost.setParameter("Content-Transfer-Encoding","binary");

			Part[] parts = { new FilePart("files", targetFile.getName(), targetFile) }; 
			System.out.println("图片名称为"+targetFile.getName());
			filePost.setRequestEntity(new MultipartRequestEntity(parts,
					filePost.getParams()));
			HttpClient client = new HttpClient();
			client.getHttpConnectionManager().getParams()
					.setConnectionTimeout(5000);
			int status = client.executeMethod(filePost);
			if (status == HttpStatus.SC_OK) {
				//System.out.println("上传成功");
				LOG.info("上传成功");
				JSONObject json = new JSONObject(filePost.getResponseBodyAsString());
				imgPath = json.getString("message");
				//System.out.println("上传成功后的返回标示为"+json.toString());
				//System.out.println("得到的图片地址为"+imgPath);
				imgPath = CheckParams.replaceStringOfJson(imgPath);
				//System.out.println("处理后的图片地址为"+imgPath);
				deleteFile(filePath);
				// 上传成功
			} else {
//				System.out.println("上传失败");
				LOG.error("上传失败");
				// 上传失败
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			filePost.releaseConnection();
		}
		
		
		return imgPath;
	}
	
	
	public Map<String,String> uploadImage(HttpServletRequest request,String savePath) {
		Map<String, String> map = new HashMap<String, String>();
		String flag = "1";
		String error = "";//错误信息
		String temp = savePath; // 临时目录
//		System.out.println("temp--------"+temp);
		String loadpath = savePath; // 上传文件存放目录
//		System.out.println("Image--------"+loadpath);
		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(maxSize); // 设置允许用户上传文件大小,单位:字节
		fu.setSizeThreshold(4096); // 设置最多只允许在内存中存储的数据,单位:字节
		fu.setRepositoryPath(temp); // 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录
		// 开始读取上传信息
		int index = 0;
		List fileItems = null;
		try {
			fileItems = fu.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}
//		System.out.println("fileItems:"+fileItems.toString());
		LOG.info("fileItems:"+fileItems.toString());
		Iterator iter = fileItems.iterator(); // 依次处理每个上传的文件
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();// 忽略其他不是文件域的所有表单信息
			if (!item.isFormField()) {
				
				String type = item.getContentType();
				String name = item.getName();// 获取上传文件名,包括路径
				if ((type.equals("image/pjpeg") || type.equals("image/jpeg"))  
		                && name.substring(name.length() - 4).toLowerCase().equals(".jpg")) {  
					flag = "0";
		            //IE6上传jpg图片的headimageContentType是image/pjpeg，而IE9以及火狐上传的jpg图片是image/jpeg  
		        }else if(type.equals("image/png") && name.substring(name.length() - 4).toLowerCase().equals(".png")){  
		        	flag = "0"; 
		        }else if(type.equals("image/gif") && name.substring(name.length() - 4).toLowerCase().equals(".gif")){  
		        	flag = "0";  
		        }else if(type.equals("image/bmp") && name.substring(name.length() - 4).toLowerCase().equals(".bmp")){  
		        	flag = "0"; 
		        }else{  
		            error= "*文件格式不正确（必须为.jpg/.gif/.bmp/.png文件）";
		            flag = "1";
		        }  
				if(item.getSize()> maxSize){
					error=  "*文件大小不得大于10M";
					flag = "1";
				}
				
				if("0".equals(flag)){
					
					name = name.substring(name.lastIndexOf("\\") + 1);// 从全路径中提取文件名
					long size = item.getSize();
					if ((name == null || name.equals("")) && size == 0)
						continue;
					int point = name.indexOf(".");
					name = (new Date()).getTime()+ index
							+ name.substring(point, name.length()) ;
					index++;
					File fNew = new File(loadpath, name);
					try {
						item.write(fNew);
						savePath = loadpath  + System.getProperty ("file.separator") +name;
						flag = "0";
						//deleteFile(savePath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else // 取出不是文件域的所有表单信息
			{
				String fieldvalue = item.getString();
//				System.out.println(fieldvalue);
				map.put("id", fieldvalue);
				map.put("flag", "1");
				// 如果包含中文应写为：(转为UTF-8编码)
				// String fieldvalue = new
				// String(item.getString().getBytes(),"UTF-8");
			}
		}
		map.put("savePath", savePath);
		map.put("error", error);
		map.put("flag", flag);
//		System.out.println(map.toString());
		return map;
	}
	/**
	 * 删除文件
	 * @param fileName
	 */
	/**
	 * 
	 * @Title: deleteFile 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-3-10
	 * @param @param savePath
	 * @return void
	 * @throws
	 */
	public void deleteFile(String savePath){
		try {
			// 路徑+文件名
			String filePath = savePath;
			File myfile = new File(filePath);
			myfile.delete();
		} catch (Exception e) {
			LOG.error("LOG***error***删除文件异常***LOG", e);
		}
	}
	
	/**
     * 从网络地址获取图片
     * @param imgUrl 图片地址
     * @return 
     */ 
    public BufferedImage getBufferedImage(String imgUrl) { 
        URL url = null; 
        InputStream is = null; 
        BufferedImage img = null; 
        try { 
            url = new URL(imgUrl); 
            is = url.openStream(); 
            img = ImageIO.read(is); 
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
               
            try { 
                is.close(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
        return img; 
    } 
	
	public static void main(String[] args) {
		HttpUtils httpUtils = HttpUtils.getInstance();
		
//		httpUtils.uploadFile("D:\\邦极\\html\\weixintest.html", "http://112.126.68.72:8001/upload_image.do");
//		httpUtils.uploadFile("D:\\邦极\\html\\user.html", "http://112.126.68.72:8001/upload_image.do");
//		httpUtils.uploadFile("D:\\邦极\\html\\images\\毒药ICON_1024.png", "http://112.126.68.72:8001/upload_image.do");
		httpUtils.uploadFile("D:\\邦极\\html\\素材\\750.png", HTTPUTIL_UPLOAD_IMAGE_URL_FOR_ARTICLE);//"http://112.126.68.72:8001/article/upload_image_s.do");
//		httpUtils.uploadFile("D:\\data\\img_temp\\0262f198fc7ae60878b101b23ec4085.html", HTTPUTIL_UPLOAD_FILE_URL_FOR_ARTICLE);// "http://112.126.68.72:8001/upload_image.do");
		
//		String img = "E:\\邦极\\素材\\tuijianlingxiu_banner@2x.png";
//		httpUtils.uploadImage(img, CommentUtils.WEB_UPLOAD_IMG);
	}

}
