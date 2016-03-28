package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActHot;
import com.poison.act.model.ActUseful;
import com.poison.activity.client.ActivityFacade;
import com.poison.activity.model.ActivityRec;
import com.poison.activity.model.ActivityStage;
import com.poison.activity.model.ActivityState;
import com.poison.activity.model.ActivityUserMatch;
import com.poison.eagle.entity.ResInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.MvFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

public class ActivityManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ActivityManager.class);
	
	//private int flagint;
	private ActivityFacade activityFacade;
	
	private MvCommentFacade mvCommentFacade;
	
	private MvFacade mvFacade;
	
	private UcenterFacade ucenterFacade;
	
	private PaycenterFacade paycenterFacade;
	
	private ActFacade actFacade;
	
	private BkCommentFacade bkCommentFacade;
	
	private ResStatisticService resStatisticService;
	
	private UserJedisManager userJedisManager;
	
	private ResStatJedisManager resStatJedisManager;
	
	private RelationToUserAndResManager relationToUserAndResManager;
	
	private ActUtils actUtils = ActUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	private FileUtils fileUtils = FileUtils.getInstance();

	public void setActivityFacade(ActivityFacade activityFacade) {
		this.activityFacade = activityFacade;
	}

	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}

	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}

	public void setRelationToUserAndResManager(
			RelationToUserAndResManager relationToUserAndResManager) {
		this.relationToUserAndResManager = relationToUserAndResManager;
	}
	

	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}

	/**
	 * 我要参赛
	 * @return
	 */
	public String joinMvCommentMatch(HttpServletRequest request,Long uid){
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		
		if(uid==0){
			String uidStr = request.getParameter("uid");
			if(StringUtils.isInteger(uidStr)){
				uid = Long.valueOf(uidStr);
			}
		}
		
		datas = new HashMap<String, Object>();
		
		boolean match = false;//是否参赛
		ActivityState activityState = activityFacade.findActivityStateByUserid(uid);
		ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
		String info  = "初赛已结束，敬请关注复赛";
		if(activityState!=null && activityState.getState()==ActivityState.state_match){
			if(activityStage!=null && activityStage.getId()>0){
				//判断用户是否处于当前活动阶段
				if(activityStage.getId()==activityState.getStageid()){
					match = true;
				}
			}
		}else{
			//需要判断是否是报名阶段，如果是，则自动报名
			if(null!=activityStage&&activityStage.getState()==ActivityStage.state_start2){
				if(activityState==null || activityState.getUserid()==0){
					//没有报名数据，插入报名数据
					activityState = activityFacade.addActivityState(uid, ActivityState.state_match, activityStage.getId());
					if(activityState.getFlag()==ResultUtils.SUCCESS){
						match=true;
					}
				}else{
					//有报名数据记录，更新报名数据
					activityState = activityFacade.updateActivityState(uid, ActivityState.state_match, activityStage.getId());
					if(activityState.getFlag()==ResultUtils.SUCCESS){
						match=true;
					}
				}
			}else{
				match = false;
			}
		}
		if(match){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = info;
			//LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	
	
	/**
	 * 检查是否显示“我要参加”按钮
	 * @return
	 */
	public String getMvCommentMatchState(HttpServletRequest request,Long uid){
		String origin = request.getParameter("platform");
		int show = 0;//是否展示参赛按钮
		int match = 0;//是否已经参赛
		if(uid>0){
			ActivityState activityState = activityFacade.findActivityStateByUserid(uid);
			ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
			if(activityState!=null && activityState.getState()==ActivityState.state_match){
				if(activityStage!=null && activityStage.getId()>0){
					//判断用户是否处于当前活动阶段
					if(activityStage.getId()==activityState.getStageid()){
						match = 1;
						show = 1;
					}
				}
			}else{
				//需要判断是否是报名阶段，如果是，则展示按钮
				if(activityStage.getState()==ActivityStage.state_start2){
					show = 1;
				}
			}
		}else{
			show = 1;
			match = 0;
		}
		String resString = "uid="+uid+"&show="+show+"&match="+match+"&origin="+origin;
		return resString;
	}
	/**
	 * 检查是否显示“我要参加”按钮
	 * @return
	 */
	public String checkMvCommentMatch(HttpServletRequest request,Long uid){
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String resString="";//返回数据
		
		if(uid==0){
			String uidStr = request.getParameter("uid");
			if(StringUtils.isInteger(uidStr)){
				uid = Long.valueOf(uidStr);
			}
		}
		
		datas = new HashMap<String, Object>();
		int show = 0;//是否展示参赛按钮
		int match = 0;//是否已经参赛
		if(uid>0){
			ActivityState activityState = activityFacade.findActivityStateByUserid(uid);
			ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
			if(activityState!=null && activityState.getState()==ActivityState.state_match){
				if(activityStage!=null && activityStage.getId()>0){
					//判断用户是否处于当前活动阶段
					if(activityStage.getId()==activityState.getStageid()){
						match = 1;
						show = 1;
					}
				}
			}else{
				//需要判断是否是报名阶段，如果是，则展示按钮
				if(activityStage!=null && activityStage.getId()>0 && activityStage.getState()==ActivityStage.state_start2){
					show = 1;
				}
			}
		}else{
			show = 1;
			match = 0;
		}
		flag = CommentUtils.RES_FLAG_SUCCESS;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("show", show+"");
		map.put("match", match+"");
		datas.put("map", map);
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	
	/**
	 * 根据评委评分或影评时间查询影评列表(按评分排序--》改为按有用数排序)
	 * @return
	 */
	public String getMatchMvComments(HttpServletRequest request,Long uid){
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		int pagenum = 1;
		String type = "0";//默认按评分排序
		
		String reqs = request.getParameter("req");
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		// 转化成可读类型
		try {
			if(reqs!=null && reqs.length()>0){
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				req = (Map<String, Object>) req.get("req");
				dataq = (Map<String, Object>) req.get("data");
				String pagenumstr = dataq.get("pagenum")+"";
				type = dataq.get("type")+"";
				if(StringUtils.isInteger(pagenumstr)){
					pagenum = Integer.parseInt(pagenumstr);
				}
				if(pagenum<1){
					pagenum = 1;
				}
			}else{
				String pagenumstr = request.getParameter("pagenum")+"";
				type = request.getParameter("type");
				if(type==null || "".equals(type)){
					type = (String) request.getAttribute("type");
				}
				if(StringUtils.isInteger(pagenumstr)){
					pagenum = Integer.parseInt(pagenumstr);
				}
				if(pagenum<1){
					pagenum = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int pagesize = 9;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		String resString="";//返回数据
		datas = new HashMap<String, Object>();
		int flagint = 0;
		//需要查询出当前进行阶段，如果没有当前进行阶段则查询最近停止的阶段
		ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
		if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
			flagint = activityStage.getFlag();
		}else if(activityStage==null || activityStage.getId()==0){
			activityStage = activityFacade.findLatestActivityStageByState(ActivityStage.type_mvcomment,ActivityStage.state_stop);
			if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
				flagint = activityStage.getFlag();
			}else if(activityStage!=null && activityStage.getId()>0){
				flagint = activityStage.getFlag();
			}
		}else{
			flagint = activityStage.getFlag();
		}
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		if(flagint==ResultUtils.SUCCESS){
			//查询该阶段下的影评列表
			List<MvComment> mvComments = null;
			List<Long> commentids = new ArrayList<Long>();
			List<ResStatistic> resStatisctics = null;
			if("1".equals(type)){
				//按影评时间排序
				mvComments = mvCommentFacade.findMvCommentsByStageidOrderbyId(activityStage.getId(), start, pagesize);
				resourceInfos = getResponseList(mvComments, uid, resourceInfos);
			}else{
				//按评分排序--》改为按有用数排序
				//mvComments = mvCommentFacade.findMvCommentsByStageidOrderbyPoint(activityStage.getId(), start, pagesize);
				resStatisctics = resStatisticService.findResStatisticRankByVoteNum(0L, "", activityStage.getId(), (int)start, pagesize);
				if(resStatisctics!=null && resStatisctics.size()>0){
					for(int i=0;i<resStatisctics.size();i++){
						commentids.add(resStatisctics.get(i).getResId());
					}
					mvComments = mvCommentFacade.findMvCommentsByIdsAndStageid(activityStage.getId(), commentids);
					if(mvComments!=null && mvComments.size()>0){
						//List<MvComment> usefulMvComments = new ArrayList<MvComment>();
						for(int i=0;i<resStatisctics.size();i++){
							for(int j=0;j<mvComments.size();j++){
								if(mvComments.get(j).getId()==resStatisctics.get(i).getResId()){
									//usefulMvComments.add(mvComments.get(j));
									ResourceInfo ri = movieUtils.putMVCommentToResource(mvComments.get(j), ucenterFacade, mvFacade);
									//ri.setUsefulCount(resStatisctics.get(i).getUsefulNumber()+"");//不需要有用数了
									ri.setVoteNum(resStatisctics.get(i).getHeatNumber()+"");
									resourceInfos.add(ri);
									mvComments.remove(j);
									break;
								}
							}
						}
					}
				}
			}
			//需要赋值有用数量
			if(mvComments!=null && mvComments.size()>0){
				//区分是按时间还是按有用数
				if("1".equals(type)){
					//按影评时间的
					/*List<Long> resids = new ArrayList<Long>();
					for(int i=0;i<mvComments.size();i++){
						resids.add(mvComments.get(i).getId());
					}
					List<ActivityUserMatch> activityUserMatchs = activityFacade.findUsefulCountForRes(resids);
					if(activityUserMatchs!=null && activityUserMatchs.size()>0){
						for(int i=0;i<resourceInfos.size();i++){
							ResourceInfo resourceInfo = resourceInfos.get(i);
							for(int j=0;j<activityUserMatchs.size();j++){
								ActivityUserMatch activityUserMatch = activityUserMatchs.get(j);
								if(resourceInfo.getRid()==activityUserMatch.getResid()){
									resourceInfo.setUsefulCount(activityUserMatch.getUsecount()+"");
									activityUserMatchs.remove(j);
									break;
								}
							}
							
						}
					}*/
					
					for(int i=0;i<resourceInfos.size();i++){
						ResourceInfo resourceInfo = resourceInfos.get(i);
						//Map<String,Object> hotNum = actFacade.findHotCount(resourceInfo.getRid(),CommentUtils.TYPE_MOVIE_COMMENT);
						//int hotCount = (Integer)hotNum.get("count");
						long hotCount = getHeatNum(resourceInfo.getRid(), CommentUtils.TYPE_MOVIE_COMMENT);
						resourceInfo.setVoteNum(hotCount+"");
					}
				}
			}
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else if(flagint==UNID){
			//没有阶段信息
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else{
			//查询阶段信息出错
			flag = CommentUtils.RES_FLAG_ERROR;
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	/**
	 * 根据评委评分或影评时间查询影评列表
	 * @return
	 */
	public String getMatchMvComments2(HttpServletRequest request,Long uid){
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		int pagenum = 1;
		String type = "0";//默认按评分排序
		// 转化成可读类型
		try {
			String pagenumstr = request.getParameter("pagenum")+"";
			type = request.getParameter("type");
			if(type==null || "".equals(type)){
				type = (String) request.getAttribute("type");
			}
			if(StringUtils.isInteger(pagenumstr)){
				pagenum = Integer.parseInt(pagenumstr);
			}
			if(pagenum<1){
				pagenum = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int pagesize = 9;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		String resString="";//返回数据
		datas = new HashMap<String, Object>();
		int flagint = 0;
		//需要查询出当前进行阶段，如果没有当前进行阶段则查询最近停止的阶段
		ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
		if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
			flagint = activityStage.getFlag();
		}else if(activityStage==null || activityStage.getId()==0){
			activityStage = activityFacade.findLatestActivityStageByState(ActivityStage.type_mvcomment,ActivityStage.state_stop);
			if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
				flagint = activityStage.getFlag();
			}else if(activityStage!=null && activityStage.getId()>0){
				flagint = activityStage.getFlag();
			}
		}else{
			flagint = activityStage.getFlag();
		}
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		if(flagint==ResultUtils.SUCCESS){
			//查询该阶段下的影评列表
			List<MvComment> mvComments = null;
			if("1".equals(type)){
				//按影评时间排序
				mvComments = mvCommentFacade.findMvCommentsByStageidOrderbyId(activityStage.getId(), start, pagesize);
			}else{
				//按评分排序
				mvComments = mvCommentFacade.findMvCommentsByStageidOrderbyPoint(activityStage.getId(), start, pagesize);
			}
			resourceInfos = getResponseList(mvComments, uid, resourceInfos);
			//需要赋值有用数量
			if(mvComments!=null && mvComments.size()>0){
				List<Long> resids = new ArrayList<Long>();
				for(int i=0;i<mvComments.size();i++){
					resids.add(mvComments.get(i).getId());
				}
				List<ActivityUserMatch> activityUserMatchs = activityFacade.findUsefulCountForRes(resids);
				if(activityUserMatchs!=null && activityUserMatchs.size()>0){
					for(int i=0;i<resourceInfos.size();i++){
						ResourceInfo resourceInfo = resourceInfos.get(i);
						for(int j=0;j<activityUserMatchs.size();j++){
							ActivityUserMatch activityUserMatch = activityUserMatchs.get(j);
							if(resourceInfo.getRid()==activityUserMatch.getResid()){
								resourceInfo.setUsefulCount(activityUserMatch.getUsecount()+"");
								activityUserMatchs.remove(j);
								break;
							}
						}
					}
				}
			}
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else if(flagint==UNID){
			//没有阶段信息
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else{
			//查询阶段信息出错
			flag = CommentUtils.RES_FLAG_ERROR;
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	/**
	 * 查询推荐的影评列表
	 * @param request
	 * @param uid
	 * @return
	 */
	public String getRecMatchMvComments(HttpServletRequest request,Long uid){
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		int pagenum = 1;
		String reqs = request.getParameter("req");
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		// 转化成可读类型
		try {
			if(reqs!=null && reqs.length()>0){
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				req = (Map<String, Object>) req.get("req");
				dataq = (Map<String, Object>) req.get("data");
				String pagenumstr = dataq.get("pagenum")+"";
				if(StringUtils.isInteger(pagenumstr)){
					pagenum = Integer.parseInt(pagenumstr);
				}
				if(pagenum<1){
					pagenum = 1;
				}
			}else{
				String pagenumstr = request.getParameter("pagenum")+"";
				if(StringUtils.isInteger(pagenumstr)){
					pagenum = Integer.parseInt(pagenumstr);
				}
				if(pagenum<1){
					pagenum = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int pagesize = 9;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		String resString="";//返回数据
		datas = new HashMap<String, Object>();
		int flagint = 0;
		//需要查询出当前进行阶段，如果没有当前进行阶段则查询最近停止的阶段
		ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
		if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
			flagint = activityStage.getFlag();
		}else if(activityStage==null || activityStage.getId()==0){
			activityStage = activityFacade.findLatestActivityStageByState(ActivityStage.type_mvcomment,ActivityStage.state_stop);
			if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
				flagint = activityStage.getFlag();
			}else if(activityStage!=null && activityStage.getId()>0){
				flagint = activityStage.getFlag();
			}
		}else{
			flagint = activityStage.getFlag();
		}
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		if(flagint==ResultUtils.SUCCESS){
			//查询该阶段下的影评列表
			List<ActivityRec> activityRecs = activityFacade.findActivityRec(activityStage.getId(), CommentUtils.TYPE_MOVIE_COMMENT, start, pagesize);
			if(activityRecs!=null && activityRecs.size()>0){
				List<Long> commentids = new ArrayList<Long>();
				for(int i=0;i<activityRecs.size();i++){
					commentids.add(activityRecs.get(i).getResid());
				}
				List<MvComment> mvComments = mvCommentFacade.findMvCommentsByIdsAndStageid(activityStage.getId(), commentids);
				//需要按照推荐顺序排序
				List<MvComment> orderMvComments = new ArrayList<MvComment>();
				if(mvComments!=null && mvComments.size()>0){
					for(int i=0;i<commentids.size();i++){
						Long commentid = commentids.get(i);
						for(int j=0;j<mvComments.size();j++){
							if(commentid==mvComments.get(j).getId()){
								orderMvComments.add(mvComments.get(j));
								mvComments.remove(j);
								//j--;
								break;
							}
						}
					}
				}
				resourceInfos = getResponseList(orderMvComments, uid, resourceInfos);
				//需要赋值有用数量
				if(orderMvComments!=null && orderMvComments.size()>0){
					/*List<Long> resids = new ArrayList<Long>();
					for(int i=0;i<mvComments.size();i++){
						resids.add(mvComments.get(i).getId());
					}
					List<ActivityUserMatch> activityUserMatchs = activityFacade.findUsefulCountForRes(resids);
					if(activityUserMatchs!=null && activityUserMatchs.size()>0){
						for(int i=0;i<resourceInfos.size();i++){
							ResourceInfo resourceInfo = resourceInfos.get(i);
							for(int j=0;j<activityUserMatchs.size();j++){
								ActivityUserMatch activityUserMatch = activityUserMatchs.get(j);
								if(resourceInfo.getRid()==activityUserMatch.getResid()){
									resourceInfo.setUsefulCount(activityUserMatch.getUsecount()+"");
									activityUserMatchs.remove(j);
									break;
								}
							}
						}
					}*/
					
					for(int i=0;i<resourceInfos.size();i++){
						ResourceInfo resourceInfo = resourceInfos.get(i);
						//Map<String,Object> hotNum = actFacade.findHotCount(resourceInfo.getRid(),CommentUtils.TYPE_MOVIE_COMMENT);
						//int hotCount = (Integer)hotNum.get("count");
						long hotCount = getHeatNum(resourceInfo.getRid(), CommentUtils.TYPE_MOVIE_COMMENT);
						resourceInfo.setVoteNum(hotCount+"");
					}
				}
				
			}
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else if(flagint==UNID){
			//没有阶段信息
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else{
			//查询阶段信息出错
			flag = CommentUtils.RES_FLAG_ERROR;
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	
	/**
	 * 查询某个参赛用户的影评列表
	 * @param request
	 * @param uid
	 * @return
	 */
	public String getUserMatchMvComments(HttpServletRequest request,Long uid){
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		int pagenum = 1;
		long userid = 0;
		String reqs = request.getParameter("req");
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		// 转化成可读类型
		try {
			if(reqs!=null && reqs.length()>0){
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				req = (Map<String, Object>) req.get("req");
				dataq = (Map<String, Object>) req.get("data");
				String pagenumstr = dataq.get("pagenum")+"";
				if(StringUtils.isInteger(pagenumstr)){
					pagenum = Integer.parseInt(pagenumstr);
				}
				if(pagenum<1){
					pagenum = 1;
				}
				String useridstr = dataq.get("userid")+"";
				if(StringUtils.isInteger(useridstr)){
					userid = Long.valueOf(useridstr);
				}
			}else{
				String pagenumstr = request.getParameter("pagenum")+"";
				if(StringUtils.isInteger(pagenumstr)){
					pagenum = Integer.parseInt(pagenumstr);
				}
				if(pagenum<1){
					pagenum = 1;
				}
				String useridstr = request.getParameter("userid");
				if(StringUtils.isInteger(useridstr)){
					userid = Long.valueOf(useridstr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int pagesize = 9;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		String resString="";//返回数据
		datas = new HashMap<String, Object>();
		ActivityStage activityStage = null;
		int flagint = 0;
		if(userid>0){
			//需要查询出当前进行阶段，如果没有当前进行阶段则查询最近停止的阶段
			activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
			if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
				flagint = activityStage.getFlag();
			}else if(activityStage==null || activityStage.getId()==0){
				activityStage = activityFacade.findLatestActivityStageByState(ActivityStage.type_mvcomment,ActivityStage.state_stop);
				if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
					flagint = activityStage.getFlag();
				}else if(activityStage!=null && activityStage.getId()>0){
					flagint = activityStage.getFlag();
				}
			}else{
				flagint = activityStage.getFlag();
			}
		}else{
			flagint=(int) UNID;
		}
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		if(flagint==ResultUtils.SUCCESS){
			//查询该阶段下的某个用户的参赛影评列表
			List<MvComment> mvComments = mvCommentFacade.findMvCommentsByUseridAndStageid(userid, activityStage.getId(), start, pagesize);
			resourceInfos = getResponseList(mvComments, uid, resourceInfos);
			//需要赋值有用数量
			if(mvComments!=null && mvComments.size()>0){
				/*List<Long> resids = new ArrayList<Long>();
				for(int i=0;i<mvComments.size();i++){
					resids.add(mvComments.get(i).getId());
				}
				List<ActivityUserMatch> activityUserMatchs = activityFacade.findUsefulCountForRes(resids);
				if(activityUserMatchs!=null && activityUserMatchs.size()>0){
					for(int i=0;i<resourceInfos.size();i++){
						ResourceInfo resourceInfo = resourceInfos.get(i);
						for(int j=0;j<activityUserMatchs.size();j++){
							ActivityUserMatch activityUserMatch = activityUserMatchs.get(j);
							if(resourceInfo.getRid()==activityUserMatch.getResid()){
								resourceInfo.setUsefulCount(activityUserMatch.getUsecount()+"");
								activityUserMatchs.remove(j);
								break;
							}
						}
					}
				}*/
				for(int i=0;i<resourceInfos.size();i++){
					ResourceInfo resourceInfo = resourceInfos.get(i);
					//Map<String,Object> hotNum = actFacade.findHotCount(resourceInfo.getRid(),CommentUtils.TYPE_MOVIE_COMMENT);
					//int hotCount = (Integer)hotNum.get("count");
					long hotCount = getHeatNum(resourceInfo.getRid(), CommentUtils.TYPE_MOVIE_COMMENT);
					resourceInfo.setVoteNum(hotCount+"");
				}
			}
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else if(flagint==UNID){
			//没有阶段信息
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else{
			//查询阶段信息出错
			flag = CommentUtils.RES_FLAG_ERROR;
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	//查询某个电影的参赛的影评列表
	public String getMovieMatchMvComments(String reqs,Long uid){
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";

		int movieId = 0;
		Long resId = null;
		// 去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			// System.out.println(req);

			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");

			movieId = Integer.valueOf(dataq.get("movieId").toString());
			String resIdStr = dataq.get("resId")+"";
			if(StringUtils.isInteger(resIdStr)){
				resId = Long.valueOf(resIdStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		int pagesize = 10;
		String resString="";//返回数据
		datas = new HashMap<String, Object>();
		int flagint = 0;
		//需要查询出当前进行阶段，如果没有当前进行阶段则查询最近停止的阶段
		ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
		if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
			flagint = activityStage.getFlag();
		}else if(activityStage==null || activityStage.getId()==0){
			activityStage = activityFacade.findLatestActivityStageByState(ActivityStage.type_mvcomment,ActivityStage.state_stop);
			if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
				flagint = activityStage.getFlag();
			}else if(activityStage!=null && activityStage.getId()>0){
				flagint = activityStage.getFlag();
			}
		}else{
			flagint = activityStage.getFlag();
		}
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		if(flagint==ResultUtils.SUCCESS){
			//查询该阶段下的某个用户的参赛影评列表
			List<MvComment> mvComments = mvCommentFacade.findMvCommentsByMovieidAndStageid(movieId, activityStage.getId(), resId, pagesize);
			resourceInfos = getResponseList(mvComments, uid, resourceInfos);
			//需要赋值有用数量
			//========================================================
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else if(flagint==UNID){
			//没有阶段信息
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resourceInfos);
		}else{
			//查询阶段信息出错
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	/**
	 * 查询参赛用户排行（根据影评数量排行和根据最新影评发表时间排行）
	 * @return
	 */
	public String getMatchUsers(HttpServletRequest request,Long uid){
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		int pagenum = 1;
		String type = "0";//默认按评分排序
		// 转化成可读类型
		try {
			String pagenumstr = request.getParameter("pagenum")+"";
			type = request.getParameter("type")+"";
			if(StringUtils.isInteger(pagenumstr)){
				pagenum = Integer.parseInt(pagenumstr);
			}
			if(pagenum<1){
				pagenum = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int pagesize = 9;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		String resString="";//返回数据
		datas = new HashMap<String, Object>();
		int flagint = 0;
		//需要查询出当前进行阶段，如果没有当前进行阶段则查询最近停止的阶段
		ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
		if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
			flagint = activityStage.getFlag();
		}else if(activityStage==null || activityStage.getId()==0){
			activityStage = activityFacade.findLatestActivityStageByState(ActivityStage.type_mvcomment,ActivityStage.state_stop);
			if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
				flagint = activityStage.getFlag();
			}else if(activityStage!=null && activityStage.getId()>0){
				flagint = activityStage.getFlag();
			}
		}else{
			flagint = activityStage.getFlag();
		}
		List<ResInfo> resInfos = new ArrayList<ResInfo>();
		if(flagint==ResultUtils.SUCCESS){
			//查询该阶段下的参赛用户列表
			List<ActivityUserMatch> activityUserMatchs = null;
			if("1".equals(type)){
				//按影评时间排序
				activityUserMatchs = activityFacade.findActivityUserMatchByStageidOrderbyId(activityStage.getId(), start, pagesize);
			}else{
				//按影评数量排序
				activityUserMatchs = activityFacade.findActivityUserMatchByStageidOrderbyMvcount(activityStage.getId(), start, pagesize);
			}
			List<Long> userids = new ArrayList<Long>(pagesize);
			if(activityUserMatchs!=null && activityUserMatchs.size()>0){
				for(int i=0;i<activityUserMatchs.size();i++){
					userids.add(activityUserMatchs.get(i).getUserid());
				}
				//查询用户信息
				List<UserInfo> userInfos = null;
				try{
					userInfos = ucenterFacade.findUserListByUseridList(null, userids);
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				if(userInfos!=null && userInfos.size()>0){
					for(int i=0;i<activityUserMatchs.size();i++){
						ActivityUserMatch activityUserMatch = activityUserMatchs.get(i);
						for(int j=0;j<userInfos.size();j++){
							UserInfo userInfo = userInfos.get(j);
							if(activityUserMatch.getUserid()==userInfo.getUserId()){
								ResInfo resInfo = ResInfo.convert(userInfo);
								resInfo.setcNum(activityUserMatch.getMvcount()+"");
								int isInterest = userUtils.getIsInterest(uid,userInfo.getUserId(), ucenterFacade,userJedisManager);
								resInfo.setIsInterest(isInterest);
								resInfos.add(resInfo);
								userInfos.remove(j);
								break;
							}
						}
					}
				}
			}
			//需要赋值有用数量
			if(resInfos!=null && resInfos.size()>0){
				/*List<ActivityUserMatch> activityresids = activityFacade.findResidsByStageidAndUserids(activityStage.getId(), userids);
				List<Long> resids = new ArrayList<Long>();
				if(activityresids!=null && activityresids.size()>0){
					for(int i=0;i<activityresids.size();i++){
						resids.add(activityresids.get(i).getResid());
					}
				}
				if(resids.size()>0){
					List<ActivityUserMatch> activityUserMatchs2 = activityFacade.findUsefulCountForUser(resids);
					if(activityUserMatchs2!=null && activityUserMatchs2.size()>0){
						for(int i=0;i<resInfos.size();i++){
							ResInfo resInfo = resInfos.get(i);
							for(int j=0;j<activityUserMatchs2.size();j++){
								ActivityUserMatch activityUserMatch = activityUserMatchs2.get(j);
								if(resInfo.getRid()==activityUserMatch.getUserid()){
									resInfo.setUsefulCount(activityUserMatch.getUsecount()+"");
									activityUserMatchs2.remove(j);
									break;
								}
							}
						}
					}
				}*/
				
				for(int i=0;i<resInfos.size();i++){
					ResInfo resInfo = resInfos.get(i);
					//Map<String,Object> hotNum = actFacade.findHotCount(resourceInfo.getRid(),CommentUtils.TYPE_MOVIE_COMMENT);
					//int hotCount = (Integer)hotNum.get("count");
					long hotCount = getHeatNum(resInfo.getRid(), CommentUtils.TYPE_MOVIE_COMMENT);
					resInfo.setVoteNum(hotCount+"");
				}
			}
			
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resInfos);
		}else if(flagint==UNID){
			//没有阶段信息
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resInfos);
		}else{
			//查询阶段信息出错
			flag = CommentUtils.RES_FLAG_ERROR;
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	/**
	 * 推荐的参赛用户列表
	 * @return
	 */
	public String getRecMatchUsers(HttpServletRequest request,Long uid){
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		int pagenum = 1;
		// 转化成可读类型
		try {
			String pagenumstr = request.getParameter("pagenum")+"";
			if(StringUtils.isInteger(pagenumstr)){
				pagenum = Integer.parseInt(pagenumstr);
			}
			if(pagenum<1){
				pagenum = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		int pagesize = 9;
		long start = PageUtils.getRecordStart(pagesize, pagenum);
		String resString="";//返回数据
		datas = new HashMap<String, Object>();
		int flagint = 0;
		//需要查询出当前进行阶段，如果没有当前进行阶段则查询最近停止的阶段
		ActivityStage activityStage = activityFacade.findLatestActivityStage(ActivityStage.type_mvcomment);
		if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
			flagint = activityStage.getFlag();
		}else if(activityStage==null || activityStage.getId()==0){
			activityStage = activityFacade.findLatestActivityStageByState(ActivityStage.type_mvcomment,ActivityStage.state_stop);
			if(activityStage!=null && activityStage.getFlag()==ResultUtils.QUERY_ERROR){
				flagint = activityStage.getFlag();
			}else if(activityStage!=null && activityStage.getId()>0){
				flagint = activityStage.getFlag();
			}
		}else{
			flagint = activityStage.getFlag();
		}
		List<ResInfo> resInfos = new ArrayList<ResInfo>();
		if(flagint==ResultUtils.SUCCESS){
			//查询推荐用户信息
			List<ActivityRec> activityRecs = activityFacade.findActivityRec(activityStage.getId(), CommentUtils.TYPE_USER, start, pagesize);
			List<Long> userids = new ArrayList<Long>(pagesize);
			if(activityRecs!=null && activityRecs.size()>0){
				for(int i=0;i<activityRecs.size();i++){
					userids.add(activityRecs.get(i).getResid());
				}
				//查询用户信息
				List<UserInfo> userInfos = null;
				try{
					userInfos = ucenterFacade.findUserListByUseridList(null, userids);
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				//查询影评数量
				List<ActivityUserMatch> userMvcounts = activityFacade.findMvCountByStageidAndUserids(activityStage.getId(), userids);
				if(userInfos!=null && userInfos.size()>0){
					for(int i=0;i<activityRecs.size();i++){
						ActivityRec activityRec = activityRecs.get(i);
						for(int j=0;j<userInfos.size();j++){
							UserInfo userInfo = userInfos.get(j);
							if(activityRec.getResid()==userInfo.getUserId()){
								ResInfo resInfo = ResInfo.convert(userInfo);
								//赋值影评数量
								if(userMvcounts!=null && userMvcounts.size()>0){
									for(int z=0;z<userMvcounts.size();z++){
										ActivityUserMatch activityUserMatch = userMvcounts.get(z);
										if(activityUserMatch.getUserid()==userInfo.getUserId()){
											resInfo.setcNum(activityUserMatch.getMvcount()+"");
											userMvcounts.remove(z);
											break;
										}
									}
								}
								int isInterest = userUtils.getIsInterest(uid,userInfo.getUserId(), ucenterFacade,userJedisManager);
								resInfo.setIsInterest(isInterest);
								resInfos.add(resInfo);
								userInfos.remove(j);
								break;
							}
						}
					}
				}
			}
			//需要赋值有用数量
			if(resInfos!=null && resInfos.size()>0){
				/*List<ActivityUserMatch> activityresids = activityFacade.findResidsByStageidAndUserids(activityStage.getId(), userids);
				List<Long> resids = new ArrayList<Long>();
				if(activityresids!=null && activityresids.size()>0){
					for(int i=0;i<activityresids.size();i++){
						resids.add(activityresids.get(i).getResid());
					}
				}
				if(resids.size()>0){
					List<ActivityUserMatch> activityUserMatchs2 = activityFacade.findUsefulCountForUser(resids);
					if(activityUserMatchs2!=null && activityUserMatchs2.size()>0){
						for(int i=0;i<resInfos.size();i++){
							ResInfo resInfo = resInfos.get(i);
							for(int j=0;j<activityUserMatchs2.size();j++){
								ActivityUserMatch activityUserMatch = activityUserMatchs2.get(j);
								if(resInfo.getRid()==activityUserMatch.getUserid()){
									resInfo.setUsefulCount(activityUserMatch.getUsecount()+"");
									activityUserMatchs2.remove(j);
									break;
								}
							}
						}
					}
				}*/
				for(int i=0;i<resInfos.size();i++){
					ResInfo resInfo = resInfos.get(i);
					//Map<String,Object> hotNum = actFacade.findHotCount(resourceInfo.getRid(),CommentUtils.TYPE_MOVIE_COMMENT);
					//int hotCount = (Integer)hotNum.get("count");
					long hotCount = getHeatNum(resInfo.getRid(), CommentUtils.TYPE_MOVIE_COMMENT);
					resInfo.setVoteNum(hotCount+"");
				}
			}
			
			
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resInfos);
		}else if(flagint==UNID){
			//没有阶段信息
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resInfos);
		}else{
			//查询阶段信息出错
			flag = CommentUtils.RES_FLAG_ERROR;
			String error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	
	/**
	 * 根据用户id获取用户信息--检查登录使用
	 * @param reqs
	 * @return
	 */
	public String getUserinfo(HttpServletRequest request,Long uid){
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		long userid = 0;
		String useridstr = request.getParameter("userid");
		if(StringUtils.isInteger(useridstr)){
			userid = Long.valueOf(useridstr);
		}
		Map<String, Object> datas = new HashMap<String, Object>();
		UserEntity userEntity = null;
		int flagint = 0;
		if(userid>0){
			UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userid);
			flagint = userAllInfo.getFlag();
			userEntity = fileUtils.copyUserInfo(userAllInfo, TRUE);
			int fansNum = ucenterFacade.findUserFensCount(null, userid);
			/*long end4 = System.currentTimeMillis();
			System.out.println("用户粉丝数数耗时："+(end4-start4)+"毫秒");*/
			
			//long start5 = System.currentTimeMillis();
			int plusNum = ucenterFacade.findUserAttentionCount(null, userid);
			int total = 0;
			try {
				total = paycenterFacade.getAccAmt(userid);
				flagint = ResultUtils.SUCCESS;
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
				total = 0;
			}
			float ftotal = 0f;
			if(total >0){
				ftotal = (float)total /100;
			}
			String totalAmount = ftotal+"";
			datas.put("fansNum", fansNum);
			datas.put("plusNum", plusNum);
			datas.put("totalAmount", totalAmount);
		}else{
			flagint = ResultUtils.ERROR;
		}
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("map",userEntity);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			//request.setAttribute("error", error);
			datas.put("error", error);
		}
		//request.setAttribute("flag", flag);
		datas.put("flag", flag);
		//处理返回数据
		String resString = getResponseData(datas);
		String callback = request.getParameter("callback");
		if(callback!=null && callback.trim().length()>0){
			resString = callback+"("+resString+")";
		}
		return resString;
	}
	
	
	//查询参赛用户排行榜
	
	
	
	
	//查询参赛用户排行榜
	public String chooseVote(String reqs,Long uid,String ipAddress){
			Map<String, Object> req ;
			Map<String, Object> dataq = null;
			Map<String, Object> datas ;
			String resString;//返回数据
			String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
			String error;
//			LOG.info("客户端json数据："+reqs);
			Long lastId = null;

			//去掉空格
			if(reqs!=null){
				reqs = reqs.trim();
			}
			Long id = 0L;
			//转化成可读类型
			try {
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				req = (Map<String, Object>) req.get("req");
				dataq = (Map<String, Object>) req.get("data");
				String idstr = dataq.get("id")+"";
				if(StringUtils.isInteger(idstr)){
					id = Long.valueOf(idstr);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String type = "7";//(String) dataq.get("type");
		/*Long userid = 0l;
		try {
			userid = Long.valueOf(dataq.get("resUserid").toString());
			if(userid == null){
				userid = 0l;
			}
		} catch (Exception e) {
			userid = 0l;
		}*/

			//ActHot actHot = actFacade.doHot(uid, id, type,ipAddress);
			int flagint = ResultUtils.ERROR;
					//actHot.getFlag();
			datas = new HashMap<String, Object>();
			if(flagint == ResultUtils.SUCCESS){
				flag = CommentUtils.RES_FLAG_SUCCESS;
				//更新投票
				if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){
					MvComment mvComment = mvCommentFacade.findMvCommentIsExist(id);

					if(null!=mvComment&&mvComment.getFlag()==ResultUtils.SUCCESS){
						//更细点赞的数量
						//System.out.println("影评点赞更新");
						//resStatJedisManager.doNousefulNum(resourceId, type);
						//Map<String,Object> hotNum = actFacade.findHotCount(id,type);
						//int hotCount = (Integer)hotNum.get("count");
						long hotCount = getHeatNum(id, type)+1;
						resStatJedisManager.doHeatNum(id, type, mvComment.getMovieId(), CommentUtils.TYPE_MOVIE,hotCount,mvComment.getStageid());
						//resStatJedisManager.updateActPraiseNum(id, type, act,mvComment.getMovieId(),CommentUtils.TYPE_MOVIE);
						
					}
				}
				datas.put("error","投票成功");
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", "初赛已结束，敬请关注复赛");
			}
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);

			return resString;
	}
	
	
	/**
	 * 
	 * <p>Title: choseUseable</p> 
	 * <p>Description: 判断大赛影评有用</p> 
	 * @author :changjiang
	 * date 2015-6-8 下午7:55:22
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String chooseUseful(HttpServletRequest request,Long uid){
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		Long lastId = null;
		
		if(uid==0){
			String uidStr = request.getParameter("uid");
			if(StringUtils.isInteger(uidStr)){
				uid = Long.valueOf(uidStr);
			}
		}
		
		Long id = 0L;
		String idstr = request.getParameter("id");
		if(StringUtils.isInteger(idstr)){
			id = Long.valueOf(idstr);
		}
		
		//String type = request.getParameter("type");
		String type = CommentUtils.TYPE_MOVIE_COMMENT;
		//String status = request.getParameter("status");
		String status = CommentUtils.COMMENT_USEFUL;
		Long res_userid = 0l;
		try {
			res_userid = Long.valueOf(request.getParameter("resUserid"));
			if(res_userid == null){
				res_userid = 0l;
			}
		} catch (Exception e) {
			res_userid = 0l;
		}
		
		ActUseful actUseful = new ActUseful();
		
		//有用没用或者取消选择
		if(CommentUtils.COMMENT_USEFUL.equals(status)||CommentUtils.COMMENT_USELESS.equals(status)||CommentUtils.COMMENT_NOCHOOSEUSE.equals(status)){
			actUseful = actFacade.doUseful(id, type, res_userid, Integer.valueOf(status), uid);
		}
		int flagint = actUseful.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			

			//有用的个数加一
			if(0l!=res_userid&&CommentUtils.COMMENT_USEFUL.equals(status)){
				userJedisManager.incrOneUserInfo(res_userid, JedisConstant.USER_HASH_USEFUL_NOTICE);
			}
			//用户对资源是否有用这里状态更新
			relationToUserAndResManager.saveOneRelation(uid, id, JedisConstant.RLEATION_IS_USEFUL, actUseful.getIsUseful()+"");
			//资源的有用没用数更新
			Map<String, Object> usefulMap = new HashMap<String, Object>();
			usefulMap = actFacade.findUsefulCount(id);
			int usefulCount = (Integer) usefulMap.get("usefulCount");
			Map<String, Object> uselessMap = new HashMap<String, Object>();
			uselessMap = actFacade.findUselessCount(id);
			int uselessCount = (Integer) uselessMap.get("uselessCount");
			relationToUserAndResManager.saveOneRelationToRes(id, JedisConstant.RELATION_USEFUL_NUM, usefulCount+"");
			relationToUserAndResManager.saveOneRelationToRes(id, JedisConstant.RELATION_USELESS_NUM, uselessCount+"");
			datas.put("usefulCount", usefulCount);
			datas.put("uselessCount", uselessCount);



			if(actUseful.getResType().equals(CommentUtils.TYPE_BOOK_COMMENT)){
				BkComment bkComment = bkCommentFacade.findCommentIsExistById(actUseful.getResourceId());
				if(null!=bkComment&&bkComment.getId()!=0){
					//更细点赞的数量
					//System.out.println("书评点赞更新");
					resStatJedisManager.doUsefulNum(id, type, bkComment.getBookId(), bkComment.getResType(),usefulCount,0);
					//resStatJedisManager.doNousefulNum(resourceId, type);
					//resStatJedisManager.updateActPraiseNum(id, type, act,bkComment.getBookId(),bkComment.getResType());
				}
			}else if(actUseful.getResType().equals(CommentUtils.TYPE_MOVIE_COMMENT)){
				MvComment mvComment = mvCommentFacade.findMvCommentIsExist(actUseful.getResourceId());
				if(null!=mvComment&&mvComment.getFlag()==ResultUtils.SUCCESS){
					//更细点赞的数量
					//System.out.println("影评点赞更新");
					//resStatJedisManager.doNousefulNum(resourceId, type);
					resStatJedisManager.doUsefulNum(id, type, mvComment.getMovieId(), CommentUtils.TYPE_MOVIE,usefulCount,mvComment.getStageid());
					//resStatJedisManager.updateActPraiseNum(id, type, act,mvComment.getMovieId(),CommentUtils.TYPE_MOVIE);
				}
			}
			//推送消息
			/*if(CommentUtils.COMMENT_USEFUL.equals(status)){
				//推送消息
				long begin = System.currentTimeMillis();
				try {
					begin = System.currentTimeMillis();
					JSONObject json = new JSONObject();
					json.put("uid", uid);
					json.put("toUid", res_userid);
					json.put("rid", id);
					json.put("type", type);
					json.put("pushType", PushManager.PUSH_PARISE_TYPE);
					json.toString();
					long end = System.currentTimeMillis();
					//System.out.println("json包装耗时："+(end-begin));
					//eagleProducer.send("pushMessage", "toBody", "", json.toString());
					//pushManager.pushResourceMSG(uid, commentUid, id, type, pushType);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}*/
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
	 * 分组list
	 * 
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> getResponseList(List<MvComment> mvCommentList, Long uid, List<ResourceInfo> resList) {
		ResourceInfo ri = null;
		if (mvCommentList!=null && mvCommentList.size() >= 1) {
			//flagint = ResultUtils.SUCCESS;
			for (Object obj : mvCommentList) {
				ri = movieUtils.putMVCommentToResource(obj, ucenterFacade, mvFacade);
				try {
					actUtils.putIsCollectToResoure(ri, uid, actFacade, 0);
				} catch (Exception e) {
					LOG.error("添加附加内容时出错，资源类型[" + ri.getType() + "]id为:" + ri.getRid() + e.getMessage(), e.fillInStackTrace());
				}
				resList.add(ri);
			}
		}
		return resList;
	}
	//获取资源相关数量表里面的heatnum
	private long getHeatNum(long resid,String type){
		//String numStr = resStatJedisManager.getResStatistic(resid, type, "heat_number");
		long hotCount = 0;
		//if(StringUtils.isInteger(numStr)){
			//hotCount = Long.valueOf(numStr);
		//}
		//if(hotCount<=0){
			ResStatistic reStat = new ResStatistic();
			reStat.setResId(resid);
			reStat.setType(type);
			ResStatistic resStatic = resStatisticService.findResStatisticById(reStat);
			
			if(resStatic!=null && resStatic.getId()>0){
				hotCount = resStatic.getHeatNumber();
			}
		//}
		return hotCount;
	}
}
