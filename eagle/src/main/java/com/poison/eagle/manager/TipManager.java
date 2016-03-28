package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.poison.act.model.ActTransmit;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.entity.TipEntity;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.TipUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.paycenter.model.RewardDetail;
import com.poison.paycenter.model.RewardPesonStatistical;
import com.poison.paycenter.model.RewardStatistical;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Serialize;
import com.poison.resource.model.SerializeList;
import com.poison.resource.model.SerializeListLink;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class TipManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(TipManager.class);
	
	private int flagint;
	
	private ResourceManager resourceManager;
	
	
	private PaycenterFacade paycenterFacade;
	private UcenterFacade ucenterFacade;
	private UserJedisManager userJedisManager;
	
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	private FileUtils fileUtils = FileUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	private TipUtils tipUtils = TipUtils.getInstance();
	/**
	 * 获取某一个资源下的打赏列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewTiplistByBesourceid(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		Long id = 0l;
		Long lastId = 0l;
		String type = "";
		int status = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			lastId = Long.valueOf(dataq.get("lastId").toString());
			if(lastId == 0){
				lastId = null;
			}
			type = dataq.get("type").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//当天的打赏列表
		List<RewardDetail> rewardDetails = new ArrayList<RewardDetail>();
//		if("1".equals(type)){
//			//rewardDetails =  paycenterFacade.getDay
//		}else if("2".equals(type)){
			rewardDetails =  paycenterFacade.getRewardListInfoBySourceId(id, lastId);
//		}
		
		List<TipEntity> list = new ArrayList<TipEntity>();
		
		list = getRewardDetailList(rewardDetails, uid, list);
		int size = 0;
		
		try {
			size = paycenterFacade.getRewardCountBySourceId(id);
		} catch (Exception e) {
			size = 0;
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
//		flagint = ResultUtils.SUCCESS; 
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", list);
			datas.put("size", size);
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
	 * 打赏管理
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewTipmanager(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		long id = 0;
//		String type = "";
		int status = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("uid").toString());
//			type = dataq.get("type").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, id);
		
		int level = userAllInfo.getLevel();
		String type = level+"";
		String dayAmount = "0";
		String totalAmount = "0";
		if(level == CommentUtils.USER_LEVEL_TALENT){
			
			int day = 0;
			try {
				day = paycenterFacade.getDayMoneyByUserId(id);
				flagint = ResultUtils.SUCCESS;
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
				day = 0;
				flagint = ResultUtils.ERROR;
			}
			float fday = 0f;
			if(day >0){
				fday = (float)day;
			}
			dayAmount = fday+"";
			int total = 0;
			try {
				total = paycenterFacade.getAccAmt(id);
				flagint = ResultUtils.SUCCESS;
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
				total = 0;
				flagint = ResultUtils.ERROR;
			}
			float ftotal = 0f;
			if(total >0){
				ftotal = (float)total;
			}
			totalAmount = ftotal+"";
		}
		
//		flagint = ResultUtils.SUCCESS; 
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("type", type);
			datas.put("dayAmount", dayAmount);
			datas.put("totalAmount", totalAmount);
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
	 * 根据用户id获取打赏列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewTiplistByUid(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		long id = 0;
		String type = "";
		int status = 0;
		Long lastId = 0l;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("uid").toString());
			lastId = Long.valueOf(dataq.get("lastId").toString());
			if(lastId == 0){
				lastId = null;
			}
			type = dataq.get("type").toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = 0;
		String usernormal = CommentUtils.USER_LEVEL_NORMAL+"";
		String usershen = CommentUtils.USER_LEVEL_TALENT+"";
		List<RewardDetail> rewardDetails = new ArrayList<RewardDetail>();
		if(usernormal.equals(type)){
			//用户打赏他人的列表
			rewardDetails = paycenterFacade.getRewardListInfoByUserId(id, lastId);
			try {
				size = paycenterFacade.getRewardCountByUserId(id);
			} catch (Exception e) {
				size = 0;
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
		}else if(usershen.equals(type)){
			//用户被打赏的列表
			rewardDetails = paycenterFacade.getRewardListInfoByRecvUserId(id, lastId);
			try {
				size = paycenterFacade.getRewardCountByRecvUserId(id);
			} catch (Exception e) {
				size = 0;
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			//消除用户被打赏的通知
			userJedisManager.saveOneUserInfo(id, JedisConstant.USER_HASH_REWARD_NOTICE, "0");
		}
		
		List<TipEntity> list = new ArrayList<TipEntity>();
		
		list = getRewardDetailList(rewardDetails, uid, list);
		
//		flagint = ResultUtils.SUCCESS; 
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", list);
			datas.put("size", size);
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
	 * 打赏排行
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewTipRank(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		long page = 1;
		String type = "";
		int status = 0;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pagestr = dataq.get("page")+"";
			if(StringUtils.isInteger(pagestr)){
				page = Long.valueOf(pagestr);
			}
			type = dataq.get("type")+"";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(page <= 0){
			page = 1;//请求的页数
		}
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		final int all = 500;//允许查询的最大数量
		int maxpage = all/pagesize;
		
		List<TipEntity> list = new ArrayList<TipEntity>();
		
		if(page<=maxpage){
			//允许的页数范围内
			List<RewardPesonStatistical> pesonStatisticals = new ArrayList<RewardPesonStatistical>();
			List<RewardStatistical> rewardStatisticals = new ArrayList<RewardStatistical>();
			if("1".equals(type)){
				//被打赏排行
				pesonStatisticals = paycenterFacade.getCollListInfoByUserIdDesc(start);
			}else if("2".equals(type)){
				//打赏排行
				pesonStatisticals = paycenterFacade.getPayListInfoByUserIdDesc(start);
			}else if("3".equals(type)){
				//资源打赏排行
				rewardStatisticals = paycenterFacade.getRewardStatisticalListBySourceIdDesc(start);
			}
			
			if("3".equals(type)){
				list = getRewardStatisticalList(rewardStatisticals, uid, list);
			}else{
				list = getRewardPesonStatisticalList(pesonStatisticals, uid, type, list);
			}
		}
		
		int size = list.size();
		
//		flagint = ResultUtils.SUCCESS; 
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", list);
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
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
	 * 格式化
	 * @param list
	 * @param type
	 * @return
	 */
	public List<TipEntity> getRewardPesonStatisticalList(List<RewardPesonStatistical> reqList  , Long uid,String type, List<TipEntity> resList){
		if("1".equals(type)){
			type = TipUtils.TYPE_COLL;
		}else if("2".equals(type)){
			type = TipUtils.TYPE_PAY;
		}
		if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else if(reqList.size() > 0){
			long id = 0;
			RewardPesonStatistical object = (RewardPesonStatistical) reqList.get(0);
			id = object.getId();
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (RewardPesonStatistical rewardPesonStatistical : reqList) {
					if(rewardPesonStatistical.getId() != 0){
//						ResourceInfo resourceInfo = resourceManager.getResourceById(rewardDetail.getSourceId(), rewardDetail.getSourceType()+"", uid);
						TipEntity tipEntity = tipUtils.putRewardPesonStatisticalToTip(rewardPesonStatistical, ucenterFacade, type);
						resList.add(tipEntity);
					}
				}
			}
		}
		return resList;
	}
	/**
	 * 格式化
	 * @param list
	 * @param type
	 * @return
	 */
	public List<TipEntity> getRewardDetailList(List<RewardDetail> reqList  , Long uid, List<TipEntity> resList){
		if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else if(reqList.size() > 0){
			long id = 0;
			RewardDetail object = (RewardDetail) reqList.get(0);
			id = object.getId();
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (RewardDetail rewardDetail : reqList) {
					if(rewardDetail.getId() != 0){
						long rid = rewardDetail.getSourceId();
						String type = rewardDetail.getSourceType()+"";
						ResourceInfo resourceInfo = resourceManager.getResourceById(rewardDetail.getSourceId(), type, uid,0);
						TipEntity tipEntity = tipUtils.putRewardDetailToTip(rewardDetail,resourceInfo, ucenterFacade);
						resList.add(tipEntity);
					}
				}
			}
		}
		return resList;
	}
	/**
	 * 格式化
	 * @param list
	 * @param type
	 * @return
	 */
	public List<TipEntity> getRewardStatisticalList(List<RewardStatistical> reqList  , Long uid, List<TipEntity> resList){
		if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else if(reqList.size() > 0){
			long id = 0;
			RewardStatistical object = (RewardStatistical) reqList.get(0);
			id = object.getId();
			if(id != UNID){
				flagint = ResultUtils.SUCCESS;
				for (RewardStatistical rewardStatistical : reqList) {
					if(rewardStatistical.getId() != 0){
						long rid = rewardStatistical.getSourceId();
						String type = rewardStatistical.getSourceType()+"";
						ResourceInfo resourceInfo = resourceManager.getResourceById(rid, type, uid,0);
						TipEntity tipEntity = tipUtils.putRewardStatisticalToTip(rewardStatistical,resourceInfo);
						resList.add(tipEntity);
					}
				}
			}
		}
		return resList;
	}
	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
}
