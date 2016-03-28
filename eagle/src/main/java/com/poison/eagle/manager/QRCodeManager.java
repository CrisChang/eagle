package com.poison.eagle.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.web.WebConstant;
import com.keel.framework.web.security.UserSecurityBeanOnCookie;
import com.keel.utils.UKeyWorker;
import com.keel.utils.web.HttpHeaderUtils;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.AsciiUTils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.eagle.utils.qrcode.QRCodeUtils;
import com.poison.store.ext.constant.MemcacheStoreConstant;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public class QRCodeManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(QRCodeManager.class);
//	private Map<String, Object> req ;
//	private Map<String, Object> dataq;
//	private Map<String, Object> datas ;
//	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//	private String error;
//	private String resString;//返回数据
	
	
	private int flagint;
	private UcenterFacade ucenterFacade;
	private QRCodeUtils qrCodeUtils = QRCodeUtils.getInstance();
	private HttpUtils httpUtils = HttpUtils.getInstance();
	private String savePath ;
	private String webServer;
	private MemcachedClient operationMemcachedClient;
	private UserSecurityBeanOnCookie userSecurityBeanOnCookie;
	
	//二维码保存到本地的地址
//	private static final String savePath = java.io.File.separator+"src"+
//			java.io.File.separator+"main"+java.io.File.separator+
//			"webapp"+java.io.File.separator+"images"+java.io.File.separator;
	//保存的内容
	private static  String qrCodeContent = "/m/clientaction/scanning_qrcode/";
	//书影保存的内容
//	private static String BMQRcodeContent = webServer+"/clientaction/comment_bm_byqrcode/";
	
	/**
	 * 显示用户二维码
	 * @return
	 */
	public String viewUserQRCode(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String type = "";
		long id = 0;
		String status = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			try {
				status = dataq.get("status").toString();//0:刷新，1：不刷新
			} catch (Exception e) {
				status = "1";
			}
			type = dataq.get("type").toString();
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		String qrcodeImg = "";
		if(status == null){
			status = "1";
		}
		String content = webServer +qrCodeContent+type+"/"+id;
		if(CommentUtils.TYPE_USER.equals(type)){
			
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, id);
			String logoImg = "";
			String filePath = savePath +"user_"+UUID.randomUUID()+"."+QRCodeUtils.imgType;
			
			//判断用户的可用性
			if(userAllInfo.getUserId() != UNID){
				qrcodeImg = userAllInfo.getTwoDimensionCode();
				logoImg = userAllInfo.getFaceAddress();
			}
			
			if(qrcodeImg == null){
				qrcodeImg ="";
			}
			if(logoImg == null || "".equals(logoImg)){
				logoImg = CommentUtils.WEB_PUBLIC_PLOGO;
			}
			
			//生成二维码
			if("".equals(qrcodeImg.trim()) || "0".equals(status)){
				try {
					//生成二维码
					qrCodeUtils.encoderQRCode(content, filePath ,logoImg);
					//将生成的缓存图片上传到服务器
					qrcodeImg = httpUtils.uploadFile(filePath, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_QRCODE);
					//生成的图片保存到数据库中
					flagint = ucenterFacade.addTwoDimensionCode(id, qrcodeImg);
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}else{
				flagint = ResultUtils.SUCCESS;
			}
		}
		
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("qrcodeImage", qrcodeImg);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);

		return resString;
	}
	/**
	 * 扫描二维码
	 * @return
	 */
	public String scanningQrcode(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		int type = 0;
		long id = 0;
		//常江修改
		if(null==reqs||reqs.equals("")){
			return "";
		}
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			//type = Integer.valueOf(dataq.get("type").toString());
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, id);
		String logoImg = "";
		String qrcodeImg = "";
		String filePath = savePath +"user_"+UUID.randomUUID()+"."+QRCodeUtils.imgType;
		
		//判断用户的可用性
		if(userAllInfo.getUserId() != UNID){
			qrcodeImg = userAllInfo.getTwoDimensionCode();
			logoImg = userAllInfo.getFaceAddress();
		}
		
		if(qrcodeImg == null){
			qrcodeImg ="";
		}
		if(logoImg == null || "".equals(logoImg)){
			logoImg = CommentUtils.WEB_PUBLIC_PLOGO;
		}
		
		//生成二维码
		if("".equals(qrcodeImg.trim())){
			try {
				//生成二维码
				qrCodeUtils.encoderQRCode(qrCodeContent+id, filePath ,logoImg);
				//将生成的缓存图片上传到服务器
				qrcodeImg = httpUtils.uploadFile(filePath, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_QRCODE);
				//生成的图片保存到数据库中
				flagint = ucenterFacade.addTwoDimensionCode(id, qrcodeImg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("qrcodeImage", qrcodeImg);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 显示书或影的二维码
	 * @return
	 */
	public String viewBMQRcode(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String type = "";
		Long id = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = (String) dataq.get("type");
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		
		String bmQrcode = "";
		if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
			
			savePath = savePath+"/" +"book_"+UUID.randomUUID()+"."+QRCodeUtils.imgType;
		}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
			savePath = savePath+"/" +"movie_"+UUID.randomUUID()+"."+QRCodeUtils.imgType;
		}
		
		if("".equals(bmQrcode)){
//			String content = BMQRcodeContent+type+"/"+id+"/W";
			try {
//				qrCodeUtils.getQrcode(content, savePath);
				
				bmQrcode = httpUtils.uploadFile(savePath, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_QRCODE);
				flagint = ResultUtils.SUCCESS;
			} catch (Exception e) {
			}
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("qrcodeImage", bmQrcode);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 通过二维码关注人
	 * @return
	 */
	public String plusUserByQRCode(Long id,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
//		
//		//去掉空格
//		reqs = reqs.trim();
//		
//		//转化成可读类型
//		try {
//			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			req = (Map<String, Object>) req.get("req");
//			dataq = (Map<String, Object>) req.get("data");
//			
//			type = Integer.valueOf(dataq.get("type").toString());
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(req);
		
//		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
//		String logoImg = "";
//		String qrcodeImg = "";
//		String savePath = savePath+java.io.File.separator+UUID.randomUUID()+"."+qrCodeUtils.imgType;  
//		
//		qrcodeImg = userAllInfo.getTwoDimensionCode();
//		
//		if(type == TRUE){
//			if(qrcodeImg == null || "".equals(qrcodeImg)){
//				logoImg = userAllInfo.getFaceAddress();
//				if(logoImg == null || "".equals(logoImg)){
//					logoImg = CommentUtils.WEB_PUBLIC_PLOGO;
//				}
//				try {
//					//生成二维码
//					qrCodeUtils.encoderQRCode(qrCodeContent, savePath ,logoImg);
//					//将生成的缓存图片上传到服务器
//					qrcodeImg = httpUtils.uploadImage(savePath, CommentUtils.WEB_UPLOAD_IMG);
//				} catch (Exception e) {
//					
//				}
//			}
//			
////			ucenterFacade.in
//			
//		}
//		
//		
		if(uid != 0 && id != 0){
			flagint = ResultUtils.SUCCESS;
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", id);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: webLoginByQrcode</p> 
	 * <p>Description: web端通过二维码登录</p> 
	 * @author :changjiang
	 * date 2015-5-5 下午4:39:28
	 * @param reqs
	 * @return
	 */
	public String getTwoCode(){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> datas =new HashMap<String, Object>();
		
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String type = "";
		Long id = null;
		
		String bmQrcode = "";
		String uuid = "login_poison:"+UUID.randomUUID()+"";
		String localPath = savePath +"weblogin"+uuid+"."+QRCodeUtils.imgType;
		//localPath = localPath.replace("-", "");
//			String content = BMQRcodeContent+type+"/"+id+"/W";
		try {
			System.out.println("生成二维码唯一标示为"+uuid+",路径为"+localPath+"图片的类型为"+QRCodeUtils.imgType);
			qrCodeUtils.getQrcode(uuid, localPath);
			//encoderQRCode(qrCodeContent+id, localPath );
			//QRCodeUtils qrCodeUtils1 = new QRCodeUtils();
					//QRCodeUtils.getInstance();
			/*OutputStream output = new FileOutputStream(localPath);
			qrCodeUtils.encoderQRCode(uuid, output);*/
		       
			bmQrcode = httpUtils.uploadFile(localPath, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_QRCODE);
			flagint = ResultUtils.SUCCESS;
		} catch (Exception e) {
		}
		
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//memcache中记录这个状态
			try {
				operationMemcachedClient.set(MemcacheStoreConstant.WEB_LOGIN_CODE+uuid, MemcacheStoreConstant.TIME_INTERVALS, "000");
				System.out.println("memecache中记录的key状态为"+MemcacheStoreConstant.WEB_LOGIN_CODE+uuid);
				String str = operationMemcachedClient.get(MemcacheStoreConstant.WEB_LOGIN_CODE+uuid);
				//System.out.println(str);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (MemcachedException e) {
				e.printStackTrace();
			}
			datas.put("qrcodeImage", bmQrcode);
			datas.put("uniqune", uuid);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: webLoginByQrcode</p> 
	 * <p>Description: 根据二维码登录</p> 
	 * @author :changjiang
	 * date 2015-5-6 下午4:20:57
	 * @param reqs
	 * @return
	 */
	public String webLoginByQrcode(String reqs,String uniqune,HttpServletResponse response){
//		LOG.info("客户端json数据："+reqs);
		/*Map<String, Object> req =new HashMap<String, Object>();
		Map<String, Object> dataq=new HashMap<String, Object>();*/
		Map<String, Object> datas =new HashMap<String, Object>();
		
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		String nickName = "";
		String headImage = "";
		int f = ResultUtils.ERROR;
		UserInfo userInfo = new UserInfo();
		/*req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");*/
		String codeValue = "";
		try {
			codeValue = operationMemcachedClient.get(MemcacheStoreConstant.WEB_LOGIN_CODE+uniqune);
			System.out.println("得到的最终的值为"+codeValue);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		String definityWebLogin = "";
		if(null!=codeValue&&codeValue.contains("{")){//当有这个记录时
			String userId = "";
			try {
				Map<String, Object> map = getObjectMapper().readValue(codeValue, new TypeReference<Map<String, Object>>(){});
				if(null!=map&&!map.isEmpty()){
					userId =  (String) map.get("userId");
					definityWebLogin =(String) map.get("definityWebLogin");
				}
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(!"".equals(userId)){
				long uid = Long.valueOf(userId);
				userInfo = ucenterFacade.findUserInfoByUserId(null, uid);
				System.out.println("用户的id为"+uid);
			}
		}
		if(null!=userInfo&&userInfo.getFlag()==ResultUtils.SUCCESS&&definityWebLogin.equals("1")){
			nickName = userInfo.getName();
			headImage = userInfo.getFaceAddress();
			this.userSecurityBeanOnCookie.setShortUserSecurityData(response, userInfo.getUserId(), ProductContextHolder.getProductContext().getEnv().getClientIP());
			System.out.println("记录用户的cookie为"+nickName);
			 int seconds=1*24*60*60;  
            Cookie cookie = new Cookie("user", "");  
            cookie.setMaxAge(0);  
            cookie.setDomain(WebConstant.ROOT_DOMAIN);
            cookie.setPath("/");
            response.addCookie(cookie); 
            if("".equals(nickName)){
            	nickName = "NONAME";
            }
            String sign = AsciiUTils.stringToAscii(nickName);
            HttpHeaderUtils.saveCookie(response, "U", sign, -1, WebConstant.ROOT_DOMAIN, false);
            
			f = ResultUtils.SUCCESS;
		}
		
		if(f == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("nickName", nickName);
			datas.put("headImage", headImage);
			datas.put("definityWebLogin", definityWebLogin);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(f);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public void setWebServer(String webServer) {
		this.webServer = webServer;
	}
	
	public void setOperationMemcachedClient(MemcachedClient operationMemcachedClient) {
		this.operationMemcachedClient = operationMemcachedClient;
	}
	
	public void setUserSecurityBeanOnCookie(
			UserSecurityBeanOnCookie userSecurityBeanOnCookie) {
		this.userSecurityBeanOnCookie = userSecurityBeanOnCookie;
	}
	public static void main(String[] args) {
//		try {
//			System.out.println(new File("").getCanonicalPath()+savePath+UUID.randomUUID()+".png" );
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
