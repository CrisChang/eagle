package com.poison.easemob.client.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poison.eagle.utils.MD5Utils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.easemob.client.EasemobUserFacade;
import com.poison.easemob.ext.method.EasemobIMUsers;
import com.poison.easemob.ext.utils.EasemobUserUtils;
import com.poison.easemob.model.EasemobUser;
import com.poison.easemob.service.EasemobUserService;

public class EasemobUserFacadeImpl implements EasemobUserFacade{

	private EasemobUserService easemobUserService;

	public void setEasemobUserService(EasemobUserService easemobUserService) {
		this.easemobUserService = easemobUserService;
	}

	/**
	 * 数据库插入环信用户
	 */
	@Override
	public EasemobUser insertEasemobUser(long userId) {
		String easemobUserId = EasemobUserUtils.getEasemobUserId(userId);
		long sysdate = System.currentTimeMillis();
		EasemobUser easemobUser = new EasemobUser(); 
		easemobUser.setUserId(userId);
		easemobUser.setEasemobId(easemobUserId);
		easemobUser.setCreateDate(sysdate);
		return easemobUserService.insertEasemobUser(easemobUser);
	}
	
	/**
	 * 
	 * <p>Title: findEasemobUserByUid</p> 
	 * <p>Description: 根据uid查询环信用户</p> 
	 * @author :changjiang
	 * date 2014-12-26 下午3:25:07
	 * @param userId
	 * @return
	 */
	public EasemobUser findEasemobUserByUid(long userId){
		return easemobUserService.findEasemobUserByUid(userId);
	}
	
	
	/**
	 * 向环信注册IM用户[单个]
	 * 
	 * 给指定Constants.APPKEY创建一个新的用户
	 * 
	 * @param dataNode
	 * @return
	 */
	@Override
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
			//先从数据库查询是否已经注册过了
			EasemobUser easemobUser2 = null;
			try{
				easemobUser2 = findEasemobUserByUid(userId);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(easemobUser2!=null && easemobUser2.getFlag()==ResultUtils.SUCCESS && easemobUser2.getUserId()==userId){
				//已经存在了
				easemobUser2.setDescription("经过检查数据库用户已经存在了");
				return easemobUser2;
			}else{
				ObjectNode objectNode = EasemobIMUsers.createNewIMUserSingle(datanode);
				
				if(objectNode!=null){
					int statusCode = 0;
					if(objectNode.get("statusCode")!=null){
						statusCode = objectNode.get("statusCode").asInt();
					}
					if(statusCode==200){
						insertEasemobUser(userId);
						//无论插入数据库是否成功，因为注册环信成功了，所以返回成功
						easemobUser.setFlag(ResultUtils.SUCCESS);
						easemobUser.setDescription("注册环信成功了");
						return easemobUser;
					}else if(statusCode==400){
						//环信已经注册过了
						insertEasemobUser(userId);
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
	@Override
	public boolean existIMUser(long userId){
		String easemobUserId = EasemobUserUtils.getEasemobUserId(userId);
        ObjectNode getIMUsersByPrimaryKeyNode = EasemobIMUsers.getIMUsersByPrimaryKey(easemobUserId);
        if (null != getIMUsersByPrimaryKeyNode && getIMUsersByPrimaryKeyNode.get("statusCode")!=null) {
           // LOG.info("获取IM用户[主键查询]: " + getIMUsersByPrimaryKeyNode.toString());
        	int statusCode = getIMUsersByPrimaryKeyNode.get("statusCode").asInt();
			if(statusCode==200){
				return true;
			}
        }
		return false;
	}
}
