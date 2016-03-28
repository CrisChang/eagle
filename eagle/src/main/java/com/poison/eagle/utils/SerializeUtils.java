package com.poison.eagle.utils;

import java.util.List;

import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ChaperInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Serialize;
import com.poison.resource.model.SerializeListLink;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;



public class SerializeUtils {
	private static SerializeUtils userUtils;
	public SerializeUtils(){}
	public static SerializeUtils getInstance(){
		if(userUtils == null){
			return new SerializeUtils();
		}else{
			return userUtils;
		}
	}
	
	private FileUtils fileUtils = FileUtils.getInstance();
	
	
	/**
	 * 将link表转换为实例类
	 * @param serializeListLink
	 * @param actFacade
	 * @param ucenterFacade
	 * @param serializeFacade
	 * @return
	 */
	public SerializeInfo putLinkToSerialize(Object obj, ActFacade actFacade, UcenterFacade ucenterFacade,SerializeFacade serializeFacade){
		SerializeInfo serializeInfo = new SerializeInfo();
		SerializeListLink serializeListLink = (SerializeListLink) obj;
		
		Serialize serialize = serializeFacade.seleceSerializeByid(serializeListLink.getSerializeId());
		
		serializeInfo = putSerializeToInfo(serialize, actFacade, ucenterFacade, serializeFacade);
		
		return serializeInfo;
	}
	
	/**
	 * 将连载的数据表结构转换成客户端适合输出的类型
	 * @param serialize
	 * @param actFacade
	 * @param ucenterFacade
	 * @return
	 */
	public  SerializeInfo putSerializeToInfo(Serialize serialize , ActFacade actFacade, UcenterFacade ucenterFacade){
		SerializeInfo serializeInfo = new SerializeInfo();
		//跟用户id查出用户实例
		UserAllInfo userAllInfo = new UserAllInfo();
		UserEntity userEntity = new UserEntity();
		if(ucenterFacade != null){
			userAllInfo = ucenterFacade.findUserInfo(null, serialize.getUid());
			userEntity = fileUtils.copyUserInfo(userAllInfo,1);
			
		}
		
		serializeInfo.setAuthor(serialize.getAuthor());
		serializeInfo.setTitle(serialize.getName());
		serializeInfo.setId(serialize.getId());
		serializeInfo.setDescribe(serialize.getIntroduce());
		serializeInfo.setUrl(serialize.getUrl());
		serializeInfo.setType(serialize.getType());
		serializeInfo.setBtime(serialize.getBeginDate());
		serializeInfo.setUtime(serialize.getEndDate());
		serializeInfo.setUserEntity(userEntity);
		serializeInfo.setTags(serialize.getTags());
		
		long r_id = serialize.getId();
		int rNum= actFacade.findTransmitCount(null, r_id);
		int cNum = actFacade.findCommentCount(null, r_id);
		int zNum= actFacade.findPraiseCount(null, r_id);
		serializeInfo.setrNum(rNum);
		serializeInfo.setcNum(cNum);
		serializeInfo.setzNum(zNum);
		
		
		
		return serializeInfo;
	}
	/**
	 * 将连载的数据表结构转换成客户端适合输出的类型(加章节题目)
	 * @param serialize
	 * @param actFacade
	 * @param ucenterFacade
	 * @param serializeFacade
	 * @return
	 */
	public  SerializeInfo putSerializeToInfo(Serialize serialize , ActFacade actFacade, UcenterFacade ucenterFacade , SerializeFacade serializeFacade){
		SerializeInfo serializeInfo = new SerializeInfo();
		
		serializeInfo = putSerializeToInfo(serialize, actFacade, ucenterFacade);
		
		List<Chapter> chapters = serializeFacade.findChapter(serializeInfo.getId());
		
		if(chapters.size() >0){
			Chapter c = chapters.get(chapters.size()-1);
			serializeInfo.setChapterTitle(c.getName());
		}
		
		
		return serializeInfo;
	}
	
	/**
	 * 将连载章节类转换为可读类
	 * @param chapter
	 * @return
	 */
	public ChaperInfo putChaperToInfo(Chapter chapter){
		ChaperInfo chaperInfo = new ChaperInfo();
		
		chaperInfo.setId(chapter.getId());
		chaperInfo.setTitle(chapter.getName());
		chaperInfo.setContent(chapter.getContent());
		chaperInfo.setParentId(chapter.getParentId());
		chaperInfo.setBtime(chapter.getBeginDate());
		chaperInfo.setUtime(chapter.getUpdateDate());
		
		return chaperInfo;
	}
	
}
