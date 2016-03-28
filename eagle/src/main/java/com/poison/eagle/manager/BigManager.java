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
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Serialize;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;
import com.poison.store.client.BigSelectingFacade;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BigSelecting;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserBigValue;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class BigManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(BigManager.class);
//	private Map<String, Object> req ;
//	private Map<String, Object> dataq;
//	private Map<String, Object> res ;
//	private Map<String, Object> datas ;
//	private String resString;//返回数据
//	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//	private String error;
	
	private int flagint;
	private UcenterFacade ucenterFacade;
	private BigFacade bigFacade;
	private BigSelectingFacade bigSelectingFacade;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	private BigUtils bigUtils = BigUtils.getInstance();
	/**
	 * 逼格题列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewBigSelectList(String reqs){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		List<BigSelecting> bigSelectings = bigSelectingFacade.findAllBigSelecting();
		
		List<BigSelectInfo> bigSelectInfos = new ArrayList<BigSelectInfo>();
		bigSelectInfos = getBigSelectLists(bigSelectings, bigSelectInfos);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			//收藏数量
			datas.put("list", bigSelectInfos);
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
	/**
	 * 回答逼格题
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String answerBigselect(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> res ;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
		List<Map<String, String>> list =null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			list = (List<Map<String, String>>) dataq.get("list");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Iterator<Map<String, String>> iter = list.iterator();
		//自测题的总分
		float sum = 0;
		while(iter.hasNext()){
			Map<String, String> bigSelect = iter.next();
			int id = Integer.valueOf(bigSelect.get("id").toString());
			int item = Integer.valueOf(bigSelect.get("item").toString());
			BigSelecting bigSelecting = bigSelectingFacade.findBigSelectingById(id);
			if(0 == item){
				sum += bigSelecting.getaScore();
			}else if(1== item){
				sum += bigSelecting.getbScore();
			}else if(2== item){
				sum += bigSelecting.getcScore();
			}else if(3==item){
				sum += bigSelecting.getdScore();
			}
		}
		
//		UserBigValue userBigValue = ucenterFacade.findUserBigValueByUserId(uid);
//		int level = 0;//用户的等级
//		float userBig= 0;//用户的逼格值
//		float levelBig =0;//等级总逼格值
//		float nextBig = 0;//升级需要的逼格值
//		if(userBigValue.getUserId() != 0){
//			level = userBigValue.getBigLevel();
//			userBig = userBigValue.getBigValue();
//			
//			BigLevelValue bigLevelValue = bigFacade.getLevelValue(level+1);
//			levelBig = Float.valueOf(bigLevelValue.getValue().toString());
//			if(levelBig>userBig+sum){
//				nextBig = levelBig - userBig - sum;
//				userBigValue = ucenterFacade.updateUserSelfTest(uid, sum, level);
//			}else{
//				level +=1;
//				BigLevelValue bigLevelValue2 = bigFacade.getLevelValue(level+1);
//				levelBig = Float.valueOf(bigLevelValue2.getValue().toString());
//				nextBig = levelBig - userBig - sum;
//				userBigValue = ucenterFacade.updateUserSelfTest(uid, sum, level);
//			}
//		}
//		
//		flagint = userBigValue.getFlag();
		
		
		
		UserBigInfo userBigInfo = bigUtils.getUserNowLevel(uid, sum,FALSE, ucenterFacade, bigFacade);
		
		UserBigValue userBigValue = ucenterFacade.updateUserSelfTest(uid, sum, userBigInfo.getLevel());
		
		flagint = userBigValue.getFlag();
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("userBig", userBigInfo);
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
	 * 将连载的数据表结构转换成客户端适合输出的类型
	 * @param list
	 * @param type
	 * @return
	 */
	public List<BigSelectInfo> getBigSelectLists(List<BigSelecting> reqList , List<BigSelectInfo> resList){
		BigSelectInfo ri = null;
		if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else if(reqList.size() > 0){
			long id = reqList.get(0).getId();
			
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (BigSelecting object : reqList) {
					ri = bigUtils.putBigSelectToInfo(object);
					
					resList.add(ri);
				}
			}
		}
		return resList;
	}
	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setBigFacade(BigFacade bigFacade) {
		this.bigFacade = bigFacade;
	}
	public void setBigSelectingFacade(BigSelectingFacade bigSelectingFacade) {
		this.bigSelectingFacade = bigSelectingFacade;
	}
	
}
