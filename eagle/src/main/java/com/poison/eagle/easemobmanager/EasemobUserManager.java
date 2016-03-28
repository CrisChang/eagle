package com.poison.eagle.easemobmanager;

import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.MD5Utils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.easemob.client.EasemobUserFacade;
import com.poison.easemob.ext.comm.Constants;
import com.poison.easemob.ext.comm.HTTPMethod;
import com.poison.easemob.ext.utils.EasemobUserUtils;
import com.poison.easemob.ext.utils.HTTPClientUtils;
import com.poison.easemob.ext.utils.TokenUtils;
import com.poison.easemob.ext.vo.Credential;
import com.poison.easemob.ext.vo.EndPoints;
import com.poison.easemob.model.EasemobUser;

public class EasemobUserManager extends BaseManager{


	private static final  Log LOG = LogFactory.getLog(EasemobUserManager.class);
	private EasemobUserFacade easemobUserFacade;
	
    public void setEasemobUserFacade(EasemobUserFacade easemobUserFacade) {
		this.easemobUserFacade = easemobUserFacade;
	}
    
    private static JsonNodeFactory factory = new JsonNodeFactory(false);
	// 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = TokenUtils.getCredential();
	
	/**
	 * 注册IM用户[单个]
	 * 
	 * @param dataNode
	 * @return
	 */
	public  EasemobUser regIMUserSingle(long userId) {
		String easemobUserId = EasemobUserUtils.getEasemobUserId(userId);
		String password = EasemobUserUtils.getEasemobPassword(userId);
		ObjectNode datanode = JsonNodeFactory.instance.objectNode();
        datanode.put("username",easemobUserId);
        datanode.put("password",password);
        
        EasemobUser easemobUser = new EasemobUser();
		easemobUser.setUserId(userId);
		easemobUser.setEasemobId(easemobUserId);
		long sysdate = System.currentTimeMillis();
		easemobUser.setCreateDate(sysdate);
		try{
			EasemobUser easemobUser2 = null;
			try{
				easemobUser2 = easemobUserFacade.findEasemobUserByUid(userId);
			}catch(Exception e){
				e.printStackTrace();
			}
			//先从数据库查询是否已经注册过了
			if(easemobUser2!=null && easemobUser2.getFlag()==ResultUtils.SUCCESS && easemobUser2.getUserId()==userId){
				//已经存在了
				easemobUser2.setDescription("经过检查数据库用户已经存在了");
				return easemobUser2;
			}else{
				ObjectNode objectNode = createNewIMUserSingle(datanode);
				
				if(objectNode!=null){
					int statusCode = 0;
					if(objectNode.get("statusCode")!=null){
						statusCode = objectNode.get("statusCode").asInt();
					}
					if(statusCode==200){
						easemobUserFacade.insertEasemobUser(userId);
						//无论插入数据库是否成功，因为注册环信成功了，所以返回成功
						easemobUser.setFlag(ResultUtils.SUCCESS);
						easemobUser.setDescription("注册环信成功了");
						return easemobUser;
					}else if(statusCode==400){
						//环信已经注册过了
						easemobUserFacade.insertEasemobUser(userId);
						String error_description = "";
						if(objectNode.get("error_description")!=null){
							error_description = objectNode.get("error_description").asText();
						}
						easemobUser.setDescription(error_description);
						easemobUser.setFlag(ResultUtils.ERROR);
						return easemobUser;
					}else{
						String error_description = "";
						if(objectNode.get("error_description")!=null){
							error_description = objectNode.get("error_description").asText();
						}
						easemobUser.setDescription(error_description);
						easemobUser.setFlag(ResultUtils.ERROR);
						return easemobUser;
					}
				}else{
					easemobUser.setDescription("objectNode为null，未知错误");
					easemobUser.setFlag(ResultUtils.ERROR);
					return easemobUser;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			easemobUser.setDescription("发生异常，信息为"+e.getMessage());
			easemobUser.setFlag(ResultUtils.ERROR);
			return easemobUser;
		}
		//{"action":"post","application":"3b6341c0-89a4-11e4-9abf-e9bff7f2c9e2","path":"/users","uri":"https://a1.easemob.com/c290573375/duyao/users","entities":[{"uuid":"51a76b3a-da9b-11e4-b32d-d5bd88cbaa50","type":"user","created":1428132135267,"modified":1428132135267,"username":"kenshinnuser100","activated":true}],"timestamp":1428132135267,"duration":18,"organization":"c290573375","applicationName":"duyao","statusCode":200}
		//{"error":"duplicate_unique_property_exists","timestamp":1428134162359,"duration":0,"exception":"org.apache.usergrid.persistence.exceptions.DuplicateUniquePropertyExistsException","error_description":"Application 3b6341c0-89a4-11e4-9abf-e9bff7f2c9e2Entity user requires that property named username be unique, value of kenshinnuser100 exists","statusCode":400}
	}
	
	//检查环信用户是否存在
	public boolean existIMUser(long userId){
		String easemobUserId = EasemobUserUtils.getEasemobUserId(userId);
        ObjectNode getIMUsersByPrimaryKeyNode = getIMUsersByPrimaryKey(easemobUserId);
        if (null != getIMUsersByPrimaryKeyNode && getIMUsersByPrimaryKeyNode.get("statusCode")!=null) {
           // LOG.info("获取IM用户[主键查询]: " + getIMUsersByPrimaryKeyNode.toString());
        	int statusCode = getIMUsersByPrimaryKeyNode.get("statusCode").asInt();
			if(statusCode==200){
				return true;
			}
        }
		return false;
	}
	
	
	/**
	 * 注册IM用户[单个]
	 * 
	 * 给指定Constants.APPKEY创建一个新的用户
	 * 
	 * @param dataNode
	 * @return
	 */
	public static ObjectNode createNewIMUserSingle(ObjectNode dataNode) {

		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOG.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("error_description", "Bad format of Constants.APPKEY");

			return objectNode;
		}

		objectNode.removeAll();

		// check properties that must be provided
		if (null != dataNode && !dataNode.has("username")) {
			LOG.error("Property that named username must be provided .");

			objectNode.put("error_description", "Property that named username must be provided .");

			return objectNode;
		}
		if (null != dataNode && !dataNode.has("password")) {
			LOG.error("Property that named password must be provided .");

			objectNode.put("error_description", "Property that named password must be provided .");

			return objectNode;
		}

		try {

		    objectNode = HTTPClientUtils.sendHTTPRequest(EndPoints.USERS_URL, credential, dataNode,
					HTTPMethod.METHOD_POST);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 获取IM用户
	 * 
	 * @param userPrimaryKey
	 *            用户主键：username或者uuid
	 * @return
	 */
	public static ObjectNode getIMUsersByPrimaryKey(String userPrimaryKey) {
		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOG.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");

			return objectNode;
		}

		// check properties that must be provided
		if (StringUtils.isEmpty(userPrimaryKey)) {
			LOG.error("The primaryKey that will be useed to query must be provided .");

			objectNode.put("message", "The primaryKey that will be useed to query must be provided .");

			return objectNode;
		}

		try {

			URL userPrimaryUrl = HTTPClientUtils
					.getURL(Constants.APPKEY.replace("#", "/") + "/users/" + userPrimaryKey);
			objectNode = HTTPClientUtils.sendHTTPRequest(userPrimaryUrl, credential, null, HTTPMethod.METHOD_GET);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}
	
	
	
	
	
	
	
	/**
	 * 注册IM用户[单个]
	 * 
	 * 给指定Constants.APPKEY创建一个新的用户
	 * 
	 * @param dataNode
	 * @return
	 */
	/*public  EasemobUser createNewIMUserSingle(long userId) {

		EasemobUser easemobUser = easemobUserFacade.insertEasemobUser(userId);
		int flag = easemobUser.getFlag();
		//当存在时直接返回
		if(ResultUtils.SUCCESS==flag){
			return easemobUser;
		}
		JsonNodeFactory factory = new JsonNodeFactory(false);
		String easemobUserId = MD5Utils.md5(userId+"");
		ObjectNode objectNode = factory.objectNode();
		ObjectNode datanode = JsonNodeFactory.instance.objectNode();
        datanode.put("username",easemobUserId);
        datanode.put("password", Constants.DEFAULT_PASSWORD);
		
		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOG.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);
			easemobUser.setDescription("Bad format of Constants.APPKEY");
			easemobUser.setFlag(ResultUtils.ERROR);
			//objectNode.put("message", "Bad format of Constants.APPKEY");
			return easemobUser;
		}

		objectNode.removeAll();

		// check properties that must be provided
		if (null != datanode && !datanode.has("username")) {
			LOG.error("Property that named username must be provided .");

			//objectNode.put("message", "Property that named username must be provided .");
			easemobUser.setDescription("Property that named username must be provided .");
			easemobUser.setFlag(ResultUtils.ERROR);
			return easemobUser;
		}
		if (null != datanode && !datanode.has("password")) {
			LOG.error("Property that named password must be provided .");

			//objectNode.put("message", "Property that named password must be provided .");
			
			easemobUser.setDescription("Property that named password must be provided .");
			easemobUser.setFlag(ResultUtils.ERROR);
			
			return easemobUser;
		}

		String statusCode = "";
		String username = "";
		try {

		    objectNode = HTTPClientUtils.sendHTTPRequest(EndPoints.USERS_URL, credential, datanode,
					HTTPMethod.METHOD_POST);
		    statusCode = objectNode.get("statusCode").toString();
		    username =  objectNode.get("username").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		easemobUser.setUserId(userId);
		easemobUser.setEasemobId(easemobUserId);
		long sysdate = System.currentTimeMillis();
		easemobUser.setCreateDate(sysdate);

		return easemobUser;
	}*/
}
