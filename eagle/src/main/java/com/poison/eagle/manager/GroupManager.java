package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.TalentUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.ucenter.client.GroupFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.Group;
import com.poison.ucenter.model.GroupUser;
import com.poison.ucenter.model.UserInfo;

public class GroupManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(GroupManager.class);
	
	private int flagint;
	private UcenterFacade ucenterFacade;
	private UserJedisManager userJedisManager;
	private GroupFacade groupFacade;
	
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setGroupFacade(GroupFacade groupFacade) {
		this.groupFacade = groupFacade;
	}

	//	private long id;
//	private String uname;//昵称
	private FileUtils fileUtils = FileUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	private TalentUtils talentUtils = TalentUtils.getInstance();
	
	/**
	 * 创建群
	 * @return
	 */
	public String createGroup(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		String groupid = null;
		String groupname = "";
		String imageurl = "";
		String tags = "";
		int amount = 200;
		String intro = "";
		List<Object> userids = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			groupid = (String) dataq.get("groupid");
			groupname = (String) dataq.get("groupname");
			imageurl =  (String) dataq.get("imageurl");
			tags = (String) dataq.get("tags");
			intro = (String) dataq.get("intro");
			String amountstr = dataq.get("amount")+"";
			if(StringUtils.isInteger(amountstr)){
				amount = Integer.valueOf(amountstr);
			}
			userids =(List<Object>) dataq.get("userids");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(groupname==null){
			groupname="";
		}
		if(imageurl==null){
			imageurl = "";
		}
		if(tags==null){
			tags="";
		}
		if(intro==null){
			intro="";
		}
		Group group = groupFacade.insertintoGroup(groupid, uid, groupname, imageurl, tags, amount, intro);
		
		flagint = group.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			try{
				if(userids!=null && userids.size()>0){
					for(int i=0;i<userids.size();i++){
						Long userid = Long.valueOf(userids.get(i)+"");
						GroupUser groupUser = groupFacade.insertintoGroupUser(groupid, userid);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			List<GroupUser> groupusers = groupFacade.findGroupUser(groupid);
			if(groupusers!=null && groupusers.size()>0){
				List<Long> memberids = new ArrayList<Long>();
				for(int i=0;i<groupusers.size();i++){
					memberids.add(groupusers.get(i).getUid());
				}
				if(!memberids.contains(group.getUid())){
					memberids.add(group.getUid());
				}
				List<UserInfo> members = ucenterFacade.findUserListByUseridList(null, memberids);
				List<UserEntity> userEntitys = handleUserList(members);
				group.setMembers(userEntitys);
			}
			datas.put("map", group);
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
	 * 修改群
	 * @return
	 */
	public String updateGroup(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		String groupid = null;
		String groupname = "";
		String imageurl = "";
		String tags = "";
		String intro = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			groupid = (String) dataq.get("groupid");
			groupname = (String) dataq.get("groupname");
			imageurl =  (String) dataq.get("imageurl");
			tags = (String) dataq.get("tags");
			intro = (String) dataq.get("intro");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(groupname==null){
			groupname="";
		}
		if(imageurl==null){
			imageurl = "";
		}
		if(tags==null){
			tags="";
		}
		if(intro==null){
			intro="";
		}
		Group group = groupFacade.updateGroup(groupid, uid, groupname, imageurl, tags, intro);
		
		flagint = group.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			List<GroupUser> groupusers = groupFacade.findGroupUser(groupid);
			if(groupusers!=null && groupusers.size()>0){
				List<Long> memberids = new ArrayList<Long>();
				for(int i=0;i<groupusers.size();i++){
					memberids.add(groupusers.get(i).getUid());
				}
				if(!memberids.contains(group.getUid())){
					memberids.add(group.getUid());
				}
				List<UserInfo> members = ucenterFacade.findUserListByUseridList(null, memberids);
				List<UserEntity> userEntitys = handleUserList(members);
				group.setMembers(userEntitys);
			}
			datas.put("map", group);
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
	 * 查询群详情
	 * @return
	 */
	public String getGroup(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		reqs = reqs.trim();
		String groupid = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			groupid = (String) dataq.get("groupid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		Group group = groupFacade.findGroup(groupid);
		flagint = group.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			List<GroupUser> groupusers = groupFacade.findGroupUser(groupid);
			if(groupusers!=null && groupusers.size()>0){
				List<Long> userids = new ArrayList<Long>();
				for(int i=0;i<groupusers.size();i++){
					userids.add(groupusers.get(i).getUid());
				}
				if(!userids.contains(group.getUid())){
					userids.add(group.getUid());
				}
				List<UserInfo> members = ucenterFacade.findUserListByUseridList(null, userids);
				List<UserEntity> userEntitys = handleUserList(members);
				group.setMembers(userEntitys);
			}
			datas.put("map",group);
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
	 * 查询用户的 群列表
	 * @return
	 */
	public String getGroups(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		reqs = reqs.trim();
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		List<Group> groups = groupFacade.getUserGroups(uid);
		//flagint = group.getFlag();
		flagint = ResultUtils.SUCCESS;
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", groups);
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
	 * 解散群(删除群)
	 * @return
	 */
	public String delGroup(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		String groupid = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			groupid = (String) dataq.get("groupid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		if(groupid==null || groupid.trim().length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "groupid不能为空");
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		Group group = groupFacade.deleteGroup(groupid, uid);
		flagint = group.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("map", group);
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
	 * 删除群成员
	 * @return
	 */
	public String delGroupUser(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		String groupid = null;
		Long userid = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			groupid = (String) dataq.get("groupid");
			userid = Long.valueOf(dataq.get("userid")+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		if(groupid==null || groupid.trim().length()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "groupid不能为空");
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		if(userid==null || userid<=0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "用户id不能为空");
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		Group group = groupFacade.findGroup(groupid);
		flagint = group.getFlag();
		if(flagint == ResultUtils.SUCCESS){
			if(userid == group.getUid()){
				//不能删除群主
				flag = CommentUtils.RES_FLAG_ERROR;
				datas.put("error", "不能删除群主");
				datas.put("flag", flag);
				resString = getResponseData(datas);
				return resString;
			}
			GroupUser groupUser = groupFacade.deleteGroupUser(groupid, userid);
			flagint = groupUser.getFlag();
			
			if(flagint == ResultUtils.SUCCESS){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				//Group group = groupFacade.findGroup(groupid);
				if(flagint == ResultUtils.SUCCESS){
					flag = CommentUtils.RES_FLAG_SUCCESS;
					List<GroupUser> groupusers = groupFacade.findGroupUser(groupid);
					if(groupusers!=null && groupusers.size()>0){
						List<Long> userids = new ArrayList<Long>();
						for(int i=0;i<groupusers.size();i++){
							userids.add(groupusers.get(i).getUid());
						}
						if(!userids.contains(group.getUid())){
							userids.add(group.getUid());
						}
						List<UserInfo> members = ucenterFacade.findUserListByUseridList(null, userids);
						List<UserEntity> userEntitys = handleUserList(members);
						group.setMembers(userEntitys);
					}
					datas.put("map",group);
				}else{
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(flagint);
					LOG.error("错误代号:"+flagint+",错误信息:"+error);
					datas.put("error", error);
				}
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
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
	 * 添加群成员
	 * @return
	 */
	public String addGroupUser(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		String groupid = null;
		List<Object> userids = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			groupid = (String) dataq.get("groupid");
			userids =(List<Object>) dataq.get("userids");
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		if(userids==null && userids.size()==0){
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("error", "用户id不能为空");
			datas.put("flag", flag);
			resString = getResponseData(datas);
			return resString;
		}
		for(int i=0;i<userids.size();i++){
			Long userid = Long.valueOf(userids.get(i)+"");
			GroupUser groupUser = groupFacade.insertintoGroupUser(groupid, userid);
			flagint = groupUser.getFlag();
			if(flagint != ResultUtils.SUCCESS){
				break;
			}
		}
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			Group group = groupFacade.findGroup(groupid);
			flagint = group.getFlag();
			if(flagint == ResultUtils.SUCCESS){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				List<GroupUser> groupusers = groupFacade.findGroupUser(groupid);
				if(groupusers!=null && groupusers.size()>0){
					List<Long> memberids = new ArrayList<Long>();
					for(int i=0;i<groupusers.size();i++){
						memberids.add(groupusers.get(i).getUid());
					}
					if(!memberids.contains(group.getUid())){
						memberids.add(group.getUid());
					}
					List<UserInfo> members = ucenterFacade.findUserListByUseridList(null, memberids);
					List<UserEntity> userEntitys = handleUserList(members);
					group.setMembers(userEntitys);
				}
				datas.put("map",group);
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
			}
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
	
	//封装用户信息集合
	private List<UserEntity> handleUserList(List<UserInfo> users){
		if(users==null){
			return null;
		}
		List<UserEntity> userEntitys = new ArrayList<UserEntity>();
		if(users.size()>0){
			for(int i=0;i<users.size();i++){
				UserInfo user = users.get(i);
				if(user!=null && user.getUserId()>0){
					UserEntity userEntity = fileUtils.copyUserInfo(user, FALSE);
					userEntitys.add(userEntity);
				}
			}
		}
		return userEntitys;
	}
}
