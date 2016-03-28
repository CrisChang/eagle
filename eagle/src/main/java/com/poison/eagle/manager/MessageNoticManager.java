package com.poison.eagle.manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.ResultUtils;

public class MessageNoticManager extends BaseManager{

	private UserJedisManager userJedisManager;
	
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	/**
	 * 
	 * <p>Title: ViewUserCommentNotic</p> 
	 * <p>Description: 查询用户的评论提醒</p> 
	 * @author :changjiang
	 * date 2015-3-18 下午2:02:45
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String ViewUserCommentNotic(Long uid){
		Map<String, String> userHashMap = userJedisManager.getUserInfo(uid);
		Map<String, Object> reqMap = new HashMap<String, Object>();
		String finalStr = "";
		String commentNotic = "0";
		String rewardNotic = "0";
		String praiseNotic = "0";
		String fansNotic = "0";
		String atNotic = "0";
		String momentNotic = "0";
		String usefulNotic = "0";
		if(null!=userHashMap&&!userHashMap.isEmpty()){
			commentNotic = userHashMap.get(JedisConstant.USER_HASH_COMMENT_NOTICE);
			rewardNotic = userHashMap.get(JedisConstant.USER_HASH_REWARD_NOTICE);
			praiseNotic = userHashMap.get(JedisConstant.USER_HASH_PRAISE_NOTICE);
			fansNotic = userHashMap.get(JedisConstant.USER_HASH_FANS_NOTICE);
			atNotic = userHashMap.get(JedisConstant.USER_HASH_AT_NOTICE);
			momentNotic = userHashMap.get(JedisConstant.USER_HASH_MOMENT_NOTICE);
			usefulNotic = userHashMap.get(JedisConstant.USER_HASH_USEFUL_NOTICE);
		}
		if(11==uid){
			reqMap.put("newCommentCount", "0");
			reqMap.put("newRewardCount", "0");
			reqMap.put("newPraiseCount", "0");
			reqMap.put("newFansCount", "0");
			reqMap.put("newAtCount", "0");
			reqMap.put("newMomentNotic", "0");
			reqMap.put("newUsefulNotic", "0");
			reqMap.put("flag", "0");
		}else{
			reqMap.put("newCommentCount", isNull(commentNotic));
			reqMap.put("newRewardCount", isNull(rewardNotic));
			reqMap.put("newPraiseCount", isNull(praiseNotic));
			reqMap.put("newFansCount", isNull(fansNotic));
			reqMap.put("newAtCount", isNull(atNotic));
			reqMap.put("newMomentNotic", isNull(momentNotic));
			reqMap.put("newUsefulNotic", isNull(usefulNotic));
			reqMap.put("flag", "0");
		}

		finalStr = getResponseData(reqMap);
		return finalStr;
	}
	
	/**
	 * 
	 * <p>Title: isNull</p> 
	 * <p>Description: 是否为空为空赋值0</p> 
	 * @author :changjiang
	 * date 2015-3-18 下午4:11:19
	 * @param str
	 * @return
	 */
	public String isNull(String str){
		if(null==str||"".equals(str)){
			str = "0";
		}
		return str;
	}
}
