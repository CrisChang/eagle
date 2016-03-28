package com.poison.eagle.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poison.eagle.utils.*;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.web.security.UserSecurityBeanOnCookie;
import com.keel.framework.web.security.UserSecurityBeanOnHeader;
import com.poison.eagle.easemobmanager.EasemobUserManager;
import com.poison.eagle.entity.UserEntity;
import com.poison.product.client.AccGoldFacade;
import com.poison.product.model.AccGold;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.store.ext.constant.MemcacheStoreConstant;
import com.poison.ucenter.client.ThirdPartyFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.ThirdPartyLogin;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserInfo;

public class UserRegLoginManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(UserRegLoginManager.class);
	public final String RES_USER_NOTLOGIN = CommentUtils.RES_ERROR_BEGIN+CommentUtils.ERROR_USERNOTLOGIN+CommentUtils.RES_ERROR_END;

	private UcenterFacade ucenterFacade;
	
	private Map<String, Object> req ;
	private Map<String, Object> dataq;
	private Map<String, Object> datas ;
	private String resString;//返回数据
	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
	private String error;//错误信息
	
//	private String type ;// 0:手机注册(登录)、1：用户名密码注册（登录）
//	private String pnum;//手机号码
//	private String name;
//	private String passwd;
//	private String pushToken;
	//private UserSecurityBeanOnCookie userSecurityBeanOnCookie;
	private UserSecurityBeanOnHeader userSecurityBeanOnHeader;
	private GetResourceInfoFacade getResourceInfoFacade;
	private FileUtils fileUtils = FileUtils.getInstance();
	private MyMovieFacade myMovieFacade;
	private BigFacade bigFacade;
	private ThirdPartyFacade thirdPartyFacade;
	private UserStatisticsFacade userStatisticsFacade;
	
	private UserSecurityBeanOnCookie userSecurityBeanOnCookie;
	
	private EasemobUserManager easemobUserManager;
	private MemcachedClient operationMemcachedClient;
	
	private UserUtils userUtils = UserUtils.getInstance();
	
	private AccGoldFacade accGoldFacade;
	
	public String userReg(String reqs,HttpServletResponse response){
		

//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";//错误信息
		String type ="";// 0:手机注册(登录)、1：用户名密码注册（登录）
		String pnum="";//手机号码
		String name="";
		String passwd="";
		String pushToken="";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = (String) dataq.get("type");
//		pushToken = (String) dataq.get("pushToken");
			pushToken = "Device Token";
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		
		UserInfo ui = new UserInfo();
		if(CommentUtils.REQ_ISON_TRUE.equals(type)){
			//手机注册的方法
			pnum = (String) dataq.get("pnum");
			passwd = (String) dataq.get("passwd");
			
//			if("".equals(pnum) || "".equals(passwd)){
//				flag
//			}
			ui = ucenterFacade.registUserByMobilePhone(null, pnum, passwd, pushToken);
		}else if(CommentUtils.REQ_ISON_STORY.equals(type)){//小说入口

			//毒药小说的注册方法
			name = "游客"+ RandUtils.getRandomString(5);//随机生成的毒药名字
			passwd = "duyao001";

			ui = ucenterFacade.registUserByLoginName(null, name, passwd, "duyaoStory");

		}else{
			//用户自主注册的方法
			name = (String) dataq.get("name");
			passwd = (String) dataq.get("passwd");
			ui = ucenterFacade.registUserByLoginName(null, name, passwd, pushToken);
		}
		UserEntity userEntity = fileUtils.copyUserInfo(ui, TRUE);
		
//		System.out.println(ui.toString());
		
		int f = ui.getFlag();
//		System.out.println(f);
		datas = new HashMap<String, Object>();
		if(f == ResultUtils.SUCCESS){
			//需要注册环信
			//System.out.println("注册用户的id为"+ui.getUserId()+"用户实体类的id为"+userEntity.getId());
			long startTime = System.currentTimeMillis();
			easemobUserManager.regIMUserSingle(userEntity.getId());
			long endTime = System.currentTimeMillis();
			//System.out.println("注册用户的结束时间为"+(endTime-startTime));
			
			ucenterFacade.saveUserLatestInfo(userEntity.getId(), 0, "");
			//强行让用户关注
//			long[] uids = {155,156,330,402,403,404,405,406};
//			long[] uids = {4155,1232,2725,1206,913,4147,815,495,839,721,1207,3149,4186,702,982,1732,850,815,682,1801,843,691,916,4865,4866,4867,4873,4809,2899,4869,752,710,1884,724,4800,4955,1658,5000,4945,4877,5072,5075,5093,2980,4889,4999,514};
//			for (long uid : uids) {
//				try {
//					UserAttention ua = ucenterFacade.doAttention(null, userEntity.getId(), uid, String.valueOf(userEntity.getLevel()));
//				} catch (Exception e) {
//					LOG.error(e.getMessage(), e.fillInStackTrace());
//				}
//			}
			
			
			setProdectUser(ui.getUserId(), response);
			getResourceInfoFacade.addDefaultBookList(null, ui.getUserId());
			myMovieFacade.addDefaultMvList(ui.getUserId());
//			System.out.println(ProductContextHolder.getProductContext().getProductUser().toString());
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("userEntity", userEntity);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(f);
			LOG.error("错误代号:"+f+",错误信息:"+error);
			datas.put("error", error);
//			System.out.println(error);
		}
		datas.put("flag", flag);
		
		
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}
	
	/**
	 * 登录方法
	 * @param reqs
	 * @return
	 */
	public String login(String reqs,HttpServletResponse response,Long uid){
		
		
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";//错误信息
		String pnum="";//手机号码
		
		String type ="";// 0:手机注册(登录)、1：用户名密码注册（登录）
		String name="";
		String passwd="";
		String pushToken="";
		String userId = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = (String) dataq.get("type");
			userId = (String) dataq.get("id");
		} catch (Exception e) {
			e.printStackTrace();
			return CommentUtils.ERROR_CODE_GETDATAERROR;
		}
//		System.out.println(req);
		//拼接数据
		datas = new HashMap<String, Object>();
		
		
		boolean isPhone = false;
		UserAllInfo ui = new UserAllInfo();
		if(CommentUtils.REQ_ISON_TRUE.equals(type)){
			//手机注册的方法
			pnum = (String) dataq.get("pnum");
			isPhone = WebUtils.isPhone(pnum);
			passwd = (String) dataq.get("passwd");
			ui = ucenterFacade.checkLoginByMobilePhone(null, pnum, passwd);
		}else if(CommentUtils.REQ_ISON_GUESS.equals(type)){
			setProdectUser(11l, response);
			UserEntity guessEntity = new UserEntity();
			guessEntity.setId(11l);
			guessEntity.setNickName("游客");
			guessEntity.setFace_url("");
			datas.put("userEntity", guessEntity);
			datas.put("initialized", "1");
			datas.put("flag", "0");
			resString = getResponseData(datas);
			return resString;
		}else if(CommentUtils.REQ_ISON_STORY.equals(type)){
			//当小说登录的时候 根据id查询
			if(null!=userId&&!"".equals(userId)){
				uid = Long.valueOf(userId);
			}

			if(null==uid||0==uid){//当没有id时
				return RES_USER_NOTLOGIN;
			}
			ui = ucenterFacade.findUserInfo(null,uid);
		}else{
			//用户自主注册的方法
			name = (String) dataq.get("name");
			passwd = (String) dataq.get("passwd");
			ui = ucenterFacade.checkLoginByLoginName(null, name, passwd);
		}
		
		int f = ui.getFlag();
		

		//添加逼格
//		userEntity = userUtils.putBigToUserEntity(userEntity, ucenterFacade, bigFacade);
		
		if(!isPhone && CommentUtils.REQ_ISON_TRUE.equals(type)){
			f = ResultUtils.PHONENUM_ERROR;
		}
		
		if(f == ResultUtils.SUCCESS){
			UserEntity userEntity = fileUtils.copyUserInfo(ui,TRUE);
			//添加用户数据数量
			try {
				userStatisticsFacade.insertUserStatistics(userEntity.getId());
			} catch (Exception e) {
				LOG.error("添加用户产生数据数量出错:"+e.getMessage(), e.fillInStackTrace());
			}
			//添加金币余额信息
			try{
				AccGold accGold = accGoldFacade.findAccGoldByUserId(userEntity.getId());
				if(accGold!=null && accGold.getUserId()>0){
					userEntity.setGoldAmount(accGold.getGoldamount()+"");
				}
			}catch(Exception e){
				LOG.error("登录时查询金币余额出错:"+e.getMessage(), e.fillInStackTrace());
			}
			setProdectUser(userEntity.getId(), response);
			String state = ui.getState();
			if("0".equals(state)){
				flag = CommentUtils.RES_FLAG_SUCCESS;
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
			}
//			System.out.println(ProductContextHolder.getProductContext().getProductUser().toString());
			String userNickName = userEntity.getNickName();
			if(userNickName.contains("vvv")||userNickName.contains("***")){
				datas.put("initialized", "0");
			}else{
				datas.put("initialized", "1");
			}
			datas.put("userEntity", userEntity);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(f);
			LOG.error("错误代号:"+f+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);

		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}

	/**
	 * 从微信登陆
	 * @param reqs
	 * @return
	 */
	public String loginFromWeixin(String reqs,HttpServletResponse response,long uid){
		
		
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";//错误信息
		
		String openid = "";
		String nickname = "";
		int sex = 0;
		String location = "";
		String summary = "";
		String country = "";
		String headimgurl = "";
		String pushToken = "";
		String phoneModel = "";
		String loginSource = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			openid = (String) dataq.get("openid");//用户标示id
			nickname = (String) dataq.get("nickname");//昵称
			try {
				sex = Integer.valueOf(dataq.get("sex").toString());//性别
			} catch (Exception e) {
				sex = 0;
			}
			location = (String) dataq.get("location");//地址
			summary = (String) dataq.get("summary");//描述
			country = (String) dataq.get("country");//国家
			headimgurl = (String) dataq.get("headimgurl");//头像地址
			loginSource = (String) dataq.get("loginSource");//用户来源 WX微信，WB微博
			pushToken = (String) dataq.get("pushToken");//手机识别号
			phoneModel = (String) dataq.get("phoneModel");//手机型号
			
		} catch (Exception e) {
			e.printStackTrace();
			return CommentUtils.ERROR_CODE_GETDATAERROR;
		}
//		System.out.println(req);
		UserEntity userEntity = new UserEntity();
		ThirdPartyLogin thirdPartyLogin = new ThirdPartyLogin();
		try {
			
			thirdPartyLogin = thirdPartyFacade.insertThirdParty(openid, nickname, sex, location, country, headimgurl, summary, "", loginSource, "", pushToken, phoneModel,uid);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		int f = thirdPartyLogin.getFlag();
		
		if(f ==  ResultUtils.SUCCESS){
			UserAllInfo userAllInfo = new UserAllInfo();
			
			try {
				userAllInfo = ucenterFacade.findUserInfo(null, thirdPartyLogin.getUserId());
				//f=userAllInfo.getFlag();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			f = userAllInfo.getFlag();
			if(f == ResultUtils.SUCCESS){
				
				userEntity = fileUtils.copyUserInfo(userAllInfo,TRUE);
				//微信微博注册环信用户
				easemobUserManager.regIMUserSingle(userEntity.getId());
				
//				int UserAttentionCount = ucenterFacade.findUserAttentionCount(null, userEntity.getId());
//				if(0==UserAttentionCount){//用户的关注数为0时 加入默认关注
//					long[] uids = {4155,1232,2725,1206,913,815,4147,495,839,721,1207,3149,4186,702,982,1732,850,815,1801,682,916,4865,4866,4867,4873,4809,2899,4869,843,691,752,710,1884,724,4800,4955,1658,5000,4945,4877,5072,5075,5093,2980,4889,4999,514};
//					for (long uid : uids) {
//						try {
//							UserAttention ua = ucenterFacade.doAttention(null, userEntity.getId(), uid, String.valueOf(userEntity.getLevel()));
//						} catch (Exception e) {
//							LOG.error(e.getMessage(), e.fillInStackTrace());
//						}
//					}
//				}
				
			}else{
				LOG.error("查询用户表失败");
			}
		}else{
			LOG.error("插入第三方用户表失败");
		}
		
		
		//添加逼格
//		try {
//			userEntity = userUtils.putBigToUserEntity(userEntity, ucenterFacade, bigFacade);
//		} catch (Exception e) {
//			LOG.error(e.getMessage(), e.fillInStackTrace());
//		}
		
		
		//拼接数据
		datas = new HashMap<String, Object>();
		if(f == ResultUtils.SUCCESS){
			//添加用户数据数量
			try {
				userStatisticsFacade.insertUserStatistics(userEntity.getId());
			} catch (Exception e) {
				LOG.error("添加用户产生数据数量出错:"+e.getMessage(), e.fillInStackTrace());
			}
			setProdectUser(userEntity.getId(), response);
//			System.out.println(ProductContextHolder.getProductContext().getProductUser().toString());
			
			int state = thirdPartyLogin.getState();
			int isReg = 0;
			if(state == 6003){
				isReg = 1;
			}
			String userSex = userEntity.getSex();
			if(userSex.equals("3")){
				datas.put("initialized", "0");
			}else{
				datas.put("initialized", "1");
			}
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("userEntity", userEntity);
			datas.put("isReg", isReg);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(f);
			LOG.error("错误代号:"+f+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}
	/**
	 * 注册验证码
	 * @param reqs
	 * @return
	 */
	public String code(String reqs,HttpServletResponse response){
		
		
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";//错误信息
		String pnum="";//手机号码
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			pnum = (String) dataq.get("pnum");
		} catch (Exception e) {
			e.printStackTrace();
			return CommentUtils.ERROR_CODE_GETDATAERROR;
		}
//		System.out.println(req);
		
		
		
		int code = Sendsms.sendYzmToMobilePhone(pnum);
		
		int f = ResultUtils.ERROR;
		if(code != 0){
			f = ResultUtils.SUCCESS;
		}
		
		//拼接数据
		datas = new HashMap<String, Object>();
		if(f == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("code", code);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(f);
			LOG.error("错误代号:"+f+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: loginFormTwoCode</p> 
	 * <p>Description: 从二维码登录</p> 
	 * @author :changjiang
	 * date 2015-5-6 下午3:44:08
	 * @param reqs
	 * @return
	 */
	public String loginFormTwoCode(String reqs,Long uid){
	//		LOG.info("客户端json数据："+reqs);
			Map<String, Object> req =new HashMap<String, Object>();
			Map<String, Object> dataq=new HashMap<String, Object>();
			Map<String, Object> datas =new HashMap<String, Object>();
			String resString="";//返回数据
			String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
			String error="";//错误信息
			String uniqune="";//唯一标示
			String codeValue = "";
			int f = ResultUtils.ERROR;
			//去掉空格
			reqs = reqs.trim();
			
			//转化成可读类型
			try {
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				req = (Map<String, Object>) req.get("req");
				dataq = (Map<String, Object>) req.get("data");
				
				uniqune = (String) dataq.get("uniqune");
			} catch (Exception e) {
				e.printStackTrace();
				return CommentUtils.ERROR_CODE_GETDATAERROR;
			}
			System.out.println("APP扫描的唯一标示为"+uniqune);
	//		System.out.println(req);
			if(null!=uniqune&&!"".equals(uniqune)){//获取这条状态
				try {
					codeValue = operationMemcachedClient.get(MemcacheStoreConstant.WEB_LOGIN_CODE+uniqune);
				} catch (TimeoutException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (MemcachedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("APP得到这个值为"+codeValue);
			if("000".equals(codeValue)||codeValue.equals(uid+"")){//当有这条状态时
				try {
					/*Map<String, String> map = new HashMap<String, String>();
					map.put("userId", uid+"");
					String finalValue = getObjectMapper().writeValueAsString(map);
					System.out.println("APP设置的这个值为"+finalValue);*/
					operationMemcachedClient.set(MemcacheStoreConstant.WEB_LOGIN_CODE+uniqune, MemcacheStoreConstant.TIME_INTERVALS, uid+"");
				} catch (TimeoutException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (MemcachedException e) {
					e.printStackTrace();
				} /*catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				f = ResultUtils.SUCCESS;
			}
			
			//拼接数据
			datas = new HashMap<String, Object>();
			if(f == ResultUtils.SUCCESS){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				//datas.put("code", code);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(f);
				LOG.error("错误代号:"+f+",错误信息:"+error);
				datas.put("error", error);
			}
			datas.put("flag", flag);
			System.out.println("返回的成功值为"+flag);
			//处理返回数据
			resString = getResponseData(datas);
	//		System.out.println(resString);
			return resString;
		}
	
	/**
	 * 
	 * <p>Title: definityWebLogin</p> 
	 * <p>Description: 确定web登录</p> 
	 * @author :changjiang
	 * date 2015-5-7 下午4:08:32
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String definityWebLogin(String reqs,Long uid){
		//		LOG.info("客户端json数据："+reqs);
				Map<String, Object> req =new HashMap<String, Object>();
				Map<String, Object> dataq=new HashMap<String, Object>();
				Map<String, Object> datas =new HashMap<String, Object>();
				String resString="";//返回数据
				String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
				String error="";//错误信息
				String uniqune="";//唯一标示
				String codeValue = "";
				int f = ResultUtils.ERROR;
				//去掉空格
				reqs = reqs.trim();
				//转化成可读类型
				try {
					req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
					req = (Map<String, Object>) req.get("req");
					dataq = (Map<String, Object>) req.get("data");
					uniqune = (String) dataq.get("uniqune");
				} catch (Exception e) {
					e.printStackTrace();
					return CommentUtils.ERROR_CODE_GETDATAERROR;
				}
				System.out.println("APP扫描的唯一标示为"+uniqune);
		//		System.out.println(req);
				if(null!=uniqune&&!"".equals(uniqune)){//获取这条状态
					try {
						codeValue = operationMemcachedClient.get(MemcacheStoreConstant.WEB_LOGIN_CODE+uniqune);
					} catch (TimeoutException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (MemcachedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("APP得到这个值为"+codeValue);
				if(!"000".equals(codeValue)&&null!=codeValue){//当有这条状态时,用户的id和登录状态
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("userId", codeValue);
					map.put("definityWebLogin", "1");
					try {
						String finalValue = getObjectMapper().writeValueAsString(map);
						operationMemcachedClient.set(MemcacheStoreConstant.WEB_LOGIN_CODE+uniqune, MemcacheStoreConstant.TIME_INTERVALS, finalValue);
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (TimeoutException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (MemcachedException e) {
						e.printStackTrace();
					}
					
					f = ResultUtils.SUCCESS;
				}
				
				//拼接数据
				datas = new HashMap<String, Object>();
				if(f == ResultUtils.SUCCESS){
					flag = CommentUtils.RES_FLAG_SUCCESS;
					//datas.put("code", code);
				}else{
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(f);
					LOG.error("错误代号:"+f+",错误信息:"+error);
					datas.put("error", error);
				}
				datas.put("flag", flag);
				System.out.println("返回的成功值为"+flag);
				//处理返回数据
				resString = getResponseData(datas);
		//		System.out.println(resString);
				return resString;
			}
	
	/**
	 * //;将用户id放入productUser中
	 * @param uid
	 * @param response
	 */
	public void setProdectUser(Long uid,HttpServletResponse response){
//		System.out.println(uid);
		this.userSecurityBeanOnHeader.setShortUserSecurityData(response, uid, ProductContextHolder.getProductContext().getEnv().getClientIP());
//		System.out.println(ProductContextHolder.getProductContext().getProductUser().toString());
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setUserSecurityBeanOnHeader(
			UserSecurityBeanOnHeader userSecurityBeanOnHeader) {
		this.userSecurityBeanOnHeader = userSecurityBeanOnHeader;
	}

	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}

	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}

	public void setBigFacade(BigFacade bigFacade) {
		this.bigFacade = bigFacade;
	}
	public void setThirdPartyFacade(ThirdPartyFacade thirdPartyFacade) {
		this.thirdPartyFacade = thirdPartyFacade;
	}

	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}

	public void setEasemobUserManager(EasemobUserManager easemobUserManager) {
		this.easemobUserManager = easemobUserManager;
	}

	public void setOperationMemcachedClient(MemcachedClient operationMemcachedClient) {
		this.operationMemcachedClient = operationMemcachedClient;
	}

	public void setUserSecurityBeanOnCookie(
			UserSecurityBeanOnCookie userSecurityBeanOnCookie) {
		this.userSecurityBeanOnCookie = userSecurityBeanOnCookie;
	}

	public void setAccGoldFacade(AccGoldFacade accGoldFacade) {
		this.accGoldFacade = accGoldFacade;
	}
	
}
