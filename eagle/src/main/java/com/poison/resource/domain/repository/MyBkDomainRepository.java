package com.poison.resource.domain.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.MyBkDAO;
import com.poison.resource.dao.ResReportDAO;
import com.poison.resource.dao.SensitiveWordDAO;
import com.poison.resource.dao.TagCategoryDAO;
import com.poison.resource.dao.TagDAO;
import com.poison.resource.dao.UserTagDAO;
import com.poison.resource.model.MyBk;
import com.poison.resource.model.ResReport;
import com.poison.resource.model.Tag;
import com.poison.resource.model.TagCategory;
import com.poison.resource.model.UserTag;

public class MyBkDomainRepository {

	private MyBkDAO myBkDAO;
	private TagDAO tagDAO;
	private UserTagDAO userTagDAO;
	private ResReportDAO resReportDAO;
	private TagCategoryDAO tagCategoryDAO;
	private SensitiveWordDAO sensitiveWordDAO;

	public void setMyBkDAO(MyBkDAO myBkDAO) {
		this.myBkDAO = myBkDAO;
	}
	
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

	public void setUserTagDAO(UserTagDAO userTagDAO) {
		this.userTagDAO = userTagDAO;
	}

	public void setResReportDAO(ResReportDAO resReportDAO) {
		this.resReportDAO = resReportDAO;
	}

	public void setTagCategoryDAO(TagCategoryDAO tagCategoryDAO) {
		this.tagCategoryDAO = tagCategoryDAO;
	}

	public void setSensitiveWordDAO(SensitiveWordDAO sensitiveWordDAO) {
		this.sensitiveWordDAO = sensitiveWordDAO;
	}


	/**
	 * 
	 * <p>Title: addMyBkInfo</p> 
	 * <p>Description: 插入一本书</p> 
	 * @author :changjiang
	 * date 2014-8-7 下午2:51:18
	 * @param myBk
	 * @return
	 */
	public int addMyBkInfo(MyBk myBk){
		return myBkDAO.insertMyBk(myBk);
	}
	
	/**
	 * 
	 * <p>Title: findMyBkIsExist</p> 
	 * <p>Description: 查询用户添加的书是否已经存在</p> 
	 * @author :changjiang
	 * date 2014-8-9 上午2:20:04
	 * @param myBk
	 * @return
	 */
	public MyBk findMyBkIsExist(MyBk myBk){
		return myBkDAO.findMyBkIsExist(myBk);
	}
	
	/**
	 * 
	 * <p>Title: findMyBkList</p> 
	 * <p>Description: 查询自己库中的信息</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午3:14:52
	 * @param userId
	 * @param name
	 * @return
	 */
	public List<MyBk> findMyBkList(long userId,String name){
		List<MyBk> bkList = myBkDAO.findMyBkList(userId, name);
		if(null==bkList||bkList.size()==0){
			bkList = myBkDAO.findLikeMyBkList(userId, name);
		}
		return bkList;
	}
	
	/**
	 * 
	 * <p>Title: findMyBkInfo</p> 
	 * <p>Description: 根据ID查询自己库中书的信息</p> 
	 * @author :changjiang
	 * date 2014-8-9 下午4:20:36
	 * @return
	 */
	public MyBk findMyBkInfo(int id){
		return myBkDAO.findMyBkInfo(id);
	}
	
	/**
	 * 
	 * <p>Title: findTagById</p> 
	 * <p>Description: 根据ID查询tag</p> 
	 * @author :changjiang
	 * date 2014-11-19 下午1:09:44
	 * @param id
	 * @return
	 */
	public Tag findTagById(long id){
		return tagDAO.findTagById(id);
	}
	
	/**
	 * 
	 * <p>Title: findTagListByType</p> 
	 * <p>Description: 根据type查询标签列表</p> 
	 * @author :changjiang
	 * date 2014-11-19 下午1:14:55
	 * @param type
	 * @return
	 */
	public List<Tag> findTagListByType(String type){
		return tagDAO.findTagListByType(type);
	}
	
	/**
	 * 
	 * <p>Description: 根据type查询标签列表按自增id倒序</p> 
	 */
	public List<Tag> findTagByTypeOrderById(String type){
		return tagDAO.findTagByTypeOrderById(type);
	}
	
	/**
	 * 
	 * <p>Title: insertUserTag</p> 
	 * <p>Description: 插入用户标签</p> 
	 * @author :changjiang
	 * date 2014-11-20 下午9:21:12
	 * @param userTag
	 * @return
	 */
	public List<UserTag> insertUserTag(UserTag userTag){
		//long id = userTag.getId();
		String tagName = userTag.getTagName();
		long userId = userTag.getUserId();
		List<UserTag> userTagArray = new ArrayList<UserTag>();
		//查询用户之前的标签列表
		List<UserTag> userTagList = userTagDAO.findUserTagList(userId);
		//拿到用户最新的标签
		List<String> tagStrList = new ArrayList<String>();
		if(tagName.contains(",")){
			String[] tagNameList = tagName.split(",");
			//标签变为list存储
			for(int i=0;i<tagNameList.length;i++){
				tagStrList.add(tagNameList[i]);
			}
		}
		int flag = ResultUtils.ERROR;
		String userOneTag = "";
		UserTag userBeforeTag = new UserTag();
		UserTag uTag= new UserTag();
		Iterator<String> tagStrListIt = tagStrList.iterator();
		long id = 0;
		UKeyWorker uk=new UKeyWorker(0,1);
		while(tagStrListIt.hasNext()){
			userOneTag = tagStrListIt.next();
			//对用户之前的标签列表进行遍历
			Iterator<UserTag> userTagListIt = userTagList.iterator();
			while(userTagListIt.hasNext()){
				userBeforeTag = userTagListIt.next();
				//当用户之前的标签和现在的标签相同时,更新该标签的次数
				if(userBeforeTag.getTagName().equals(userOneTag)){
					id = userBeforeTag.getId();
					userTagDAO.updateUserTagCount(id);
					userTagListIt.remove();
					tagStrListIt.remove();
				}
			}
		}
		//对未处理的标签做处理
		Iterator<String> userAfterTagStr = tagStrList.iterator();
		while(userAfterTagStr.hasNext()){
			userOneTag = userAfterTagStr.next();
			//得到其他的不存在之前的列表标签
			uTag = userTagDAO.findUserTagByTagName(userId, userOneTag);
			flag = uTag.getFlag();
			//当用户已经存在这个标签时
			if(ResultUtils.SUCCESS==flag){
				id = uTag.getId();
				userTagDAO.updateUserTagCountAndIsDel(id);
			}else if(ResultUtils.DATAISNULL==flag){
				userTag.setId(uk.getId());
				userTag.setTagName(userOneTag);
				flag = userTagDAO.insertUserTag(userTag);
			}
		}
		//之前的标签置为删除状态
		Iterator<UserTag> userAfterTagList = userTagList.iterator();
		UserTag userAfterTag = new UserTag();
		while(userAfterTagList.hasNext()){
			userAfterTag = userAfterTagList.next();
			id = userAfterTag.getId();
			userTagDAO.deleteUserTag(id);
		}
		List<UserTag> resultUserTagList = userTagDAO.findUserTagList(userId);
		return resultUserTagList;
	}
	
	/**
	 * 
	 * <p>Title: findUserHistoryTagListByUid</p> 
	 * <p>Description: 查询用户历史记录标签</p> 
	 * @author :changjiang
	 * date 2014-11-21 下午1:50:34
	 * @param userId
	 * @return
	 */
	public List<UserTag> findUserHistoryTagListByUid(long userId){
		return userTagDAO.findUserHistoryTagListByUid(userId);
	}
	
	/**
	 * 
	 * <p>Title: findUserFavoriteTagListByUid</p> 
	 * <p>Description: 查询用户常用标签</p> 
	 * @author :changjiang
	 * date 2014-11-21 下午1:51:02
	 * @param userId
	 * @return
	 */
	public List<UserTag> findUserFavoriteTagListByUid(long userId){
		return userTagDAO.findUserFavoriteTagListByUid(userId);
	}
	
	/**
	 * 
	 * <p>Title: insertResReport</p> 
	 * <p>Description: 示例类</p> 
	 * @author :changjiang
	 * date 2014-12-1 下午9:00:41
	 * @param report
	 * @return
	 */
	public ResReport insertResReport(ResReport report){
		long id = report.getId();
		long userId = report.getUserId();
		long resId = report.getResourceId();
		ResReport resReport = new ResReport();
		resReport = resReportDAO.findResReportIsExist(userId, resId);
		int flag = resReport.getFlag();
		if(ResultUtils.SUCCESS==flag){
			resReport.setFlag(ResultUtils.IS_ALREADY_REPORT);
			return resReport;
		}
		flag = resReportDAO.insertResReport(report);
		resReport.setFlag(ResultUtils.ERROR);
		if(ResultUtils.SUCCESS==flag){
			resReport = resReportDAO.findResReportById(id);
		}
		return resReport;
	}
	
	/**
	 * 
	 * <p>Title: findTagCategoryByLevel</p> 
	 * <p>Description: 查找分类列表</p> 
	 * @author :changjiang
	 * date 2014-12-3 下午9:10:12
	 * @return
	 */
	public List<TagCategory> findTagCategoryByLevel(){
		return tagCategoryDAO.findTagCategoryByLevel();
	}
	
	/**
	 * 
	 * <p>Title: findHotTagListByTagGroup</p> 
	 * <p>Description: 查询热门标签</p> 
	 * @author :changjiang
	 * date 2014-12-4 上午11:34:02
	 * @param tagGroup
	 * @return
	 */
	public List<Tag> findHotTagListByTagGroup(String tagGroup){
		return tagDAO.findHotTagListByTagGroup(tagGroup);
	}
	
	/**
	 * 
	 * <p>Title: findAllTagListByTagGroup</p> 
	 * <p>Description: 查询所有的标签</p> 
	 * @author :changjiang
	 * date 2014-12-4 上午11:34:57
	 * @param tagGroup
	 * @return
	 */
	public List<Tag> findAllTagListByTagGroup(String tagGroup){
		return tagDAO.findAllTagListByTagGroup(tagGroup);
	}
	
	/**
	 * 
	 * <p>Title: findTagCategoryByType</p> 
	 * <p>Description: 根据type查询类别</p> 
	 * @author :changjiang
	 * date 2014-12-4 下午5:12:11
	 * @param type
	 * @return
	 */
	public List<TagCategory> findTagCategoryByType(String type) {
		return tagCategoryDAO.findTagCategoryByType(type);
	}
	
	public List<String> selectSensitiveWord (Map<String, Object> map) {
		return sensitiveWordDAO.selectSensitiveWord(map);
	}
	
	/**
	 * 
	 * <p>Title: findTaggroupByTagName</p> 
	 * <p>Description: 根据标签名字查询标签分组</p> 
	 * @author :changjiang
	 * date 2015-1-19 下午2:33:34
	 * @param tagName
	 * @param type
	 * @return
	 */
	public Tag findTaggroupByTagName(String tagName, String type){
		List<Tag> list = tagDAO.findTaggroupByTagName(tagName, type);
		Tag tag = new Tag();
		tag.setFlag(ResultUtils.ERROR);
		if(list.size()>0){
			tag = list.get(0);
			tag.setFlag(ResultUtils.SUCCESS);
		}
		return tag;
	}
	
}
 