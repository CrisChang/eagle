package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActPublish;
import com.poison.act.model.ActSubscribe;
import com.poison.eagle.entity.BigSelectInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.entity.UserBigInfo;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.BigUtils;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisApiUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BigLevelValue;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Serialize;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;
import com.poison.store.client.BigSelectingFacade;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BigSelecting;
import com.poison.store.model.BkInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class JedisApiManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(JedisApiManager.class);
	
	private int flagint;
	
	private ResourceManager resourceManager;
	
	private ResourceJedisManager resourceJedisManager;
	
	private JedisApiUtils jedisApiUtils = JedisApiUtils.getInstance();
	
	/**
	 * 逼格题列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String jedisSet(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		String type ="";
		Map<String, Object> map = new HashMap<String, Object>();
		long uid = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			type = (String) dataq.get("type");
			map = (Map<String, Object>) dataq.get("data");
			System.out.println("得到的数据为"+map.toString());
			uid = Long.valueOf(dataq.get("uid").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Object obj = new Object();
		if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
			BkComment bkComment = jedisApiUtils.putJedisJsonToBkComment(map);
			obj = bkComment;
		}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
			MvComment mvComment = jedisApiUtils.putJedisJsonToMvComment(map);
			obj = mvComment;
		}else if(CommentUtils.TYPE_PUBLISH.equals(type)){
			ActPublish actPublish = jedisApiUtils.putJedisJsonToActPublish(map);
			obj = actPublish;
		}
		
		try {
			flagint = resourceManager.setResourceToJedis(obj, uid,uid,0l);
			/*ResourceInfo resourceInfo = resourceManager.putObjectToResource(obj, uid, 1);
			resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);*/
			flagint = ResultUtils.SUCCESS;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			flagint = ResultUtils.ERROR;
		}
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			//收藏数量
			flag = CommentUtils.RES_FLAG_SUCCESS;
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
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setResourceJedisManager(ResourceJedisManager resourceJedisManager) {
		this.resourceJedisManager = resourceJedisManager;
	}
}
