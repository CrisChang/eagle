package com.poison.eagle.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import com.poison.eagle.entity.BookTalkInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.TagInfo;
import com.poison.eagle.entity.UserAlbumInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.entity.UserTagInfo;
import com.poison.resource.client.BigFacade;
import com.poison.resource.model.BigLevelValue;
import com.poison.resource.model.BookTalk;
import com.poison.resource.model.Tag;
import com.poison.resource.model.UserTag;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserAttention;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;

public class TagUtils {
	private static TagUtils userUtils;
	public TagUtils(){}
	public static TagUtils getInstance(){
		if(userUtils == null){
			return new TagUtils();
		}else{
			return userUtils;
		}
	}
	
	private FileUtils fileUtils = FileUtils.getInstance();
	
	/**
	 * 将标签格式化
	 * @param tag
	 * @return
	 */
	public List<TagInfo> putTagToInfo(List<Tag> tags){
		List<TagInfo> tagInfos = new ArrayList<TagInfo>();
		TagInfo tagInfo = new TagInfo();
		//名称倒序
		//Collections.sort(tags);
		
		if (tags.size() == 0) {
			return tagInfos;
		} else {
			Tag tag = tags.get(0);
			if (tag.getId() != 0) {
				// 可以执行转换操作
				for (int i = 0; i < tags.size(); i++) {
					tag = tags.get(i);
					if (tagInfo.getTagGroup().equals(tag.getTagGroup())) {
						// 如果page相同则继续添加
						tagInfo.getTags().add(putTagToInfo(tag));
//						tagInfo.getTags().add(tag.getTagName());
					} else {
						// 否则重新初始化
						tagInfo = new TagInfo();
						tagInfos.add(tagInfo);
						tagInfo.setTagGroup(tag.getTagGroup());
//						tagInfo.getTags().add(tag.getTagName());
						tagInfo.getTags().add(putTagToInfo(tag));
					}
				}

			}

		}
		
		
		return tagInfos;
	}
	
	
	private static Map putTagToInfo(Tag tag){
		Map map = new HashMap();
//		map.put(TagInfo.ID, tag.getId());
//		map.put(TagInfo.TAG, tag.getTagName());
		map.put(tag.getResourceLinkType(), tag.getTagName());
		
		return map;
	}
	
	/**
	 * 将用户标签格式化
	 * @param userTag
	 * @return
	 */
	public UserTagInfo putUserTagToInfo(UserTag userTag){
		UserTagInfo userTagInfo = new UserTagInfo();
		
		userTagInfo.setId(userTag.getId());
		userTagInfo.setCount(userTag.getSelectCount());
		userTagInfo.setTagId(userTag.getTagId());
		userTagInfo.setTagName(userTag.getTagName());
		userTagInfo.setType(userTag.getType());
		userTagInfo.setUid(userTag.getUserId());
		
		return userTagInfo;
	}
	
}
