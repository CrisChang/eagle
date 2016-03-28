package com.poison.eagle.manager;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserStatistics;

public class PkManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(PkManager.class);
	
	private int flagint;
	private UserStatisticsFacade userStatisticsFacade;
	
	private BkCommentFacade bkCommentFacade;
	
	private MvCommentFacade mvCommentFacade;
	
	private UserJedisManager userJedisManager;

	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}
	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}
	/**
	 * 用户资源的PK
	 * @return
	 */
	public String getResPkResult(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String resString="";//返回数据
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		int type = 0;//0代表电影PK，1代表图书PK
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			String typeStr = dataq.get("type")+"";
			if(StringUtils.isInteger(typeStr)){
				type = Integer.valueOf(typeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		datas = new HashMap<String, Object>();
		
		//先查询缓存中的数据
		Map<String, String> userStatisticsMaps = userJedisManager.getUserResourceCount(uid);
		long count = 0;
		double percent = 0;
		int percentnum = 0;
		String info = "";
		if(type==1){
			//图书
			String bkcomment_count = userStatisticsMaps.get(JedisConstant.BKCOMMENT_COUNT);
			if(StringUtils.isInteger(bkcomment_count)){
				count = Long.valueOf(bkcomment_count);
			}
			if(count<=0){
				//如果缓存没有，再查数据库，然后存入缓存
				UserStatistics userStatistics = userStatisticsFacade.findUserStatisticsByUid(uid);
				if(userStatistics!=null && userStatistics.getId()>0){
					if(userStatistics.getBkcommentCount()>0){
						//
						flagint = ResultUtils.SUCCESS;
						userJedisManager.saveOneUserResourceCount(uid, JedisConstant.BKCOMMENT_COUNT, userStatistics.getBkcommentCount()+"");
						count = userStatistics.getBkcommentCount();
					}else{
						//需要通过查询书评表查询数量
						Map<String, Object> map = bkCommentFacade.findBkCommentCount(uid);
						if(map!=null && map.get("flag")!=null){
							flagint = (Integer) map.get("flag");
						}
						if(flagint == ResultUtils.SUCCESS){
							count = (Integer) map.get("count");
							if(count>0){
								userStatisticsFacade.updateBkcommentNewCount(uid, count);
								userJedisManager.saveOneUserResourceCount(uid, JedisConstant.BKCOMMENT_COUNT, count+"");
							}
						}else{
							//出错
							
						}
					}
				}else if(userStatistics!=null && userStatistics.getFlag()==ResultUtils.ERROR){
					//出错
					flagint = userStatistics.getFlag();
				}else{
					//不存在
					//需要通过查询书评表查询数量
					Map<String, Object> map = bkCommentFacade.findBkCommentCount(uid);
					if(map!=null && map.get("flag")!=null){
						flagint = (Integer) map.get("flag");
					}
					if(flagint == ResultUtils.SUCCESS){
						count = (Integer) map.get("count");
						if(count>0){
							userStatisticsFacade.insertUserStatistics(uid);
							userStatisticsFacade.updateBkcommentNewCount(uid, count);
							userJedisManager.saveOneUserResourceCount(uid, JedisConstant.BKCOMMENT_COUNT, count+"");
						}
					}else{
						//出错
						
					}
				}
			}else{
				//
				flagint = ResultUtils.SUCCESS;
			}
			if(flagint==ResultUtils.SUCCESS){
				//查询总人数，再查询小于当前用户书评或影评数量的人数
				if(count>0){
					Map<String, Object> map = userStatisticsFacade.findTotalNum();//查询总条数
					flagint = 0;
					if(map!=null && map.get("flag")!=null){
						flagint = (Integer) map.get("flag");
					}
					if(flagint==ResultUtils.SUCCESS){
						long totalnum = (Long) map.get("count");
						//查询小于书评数量的条数
						Map<String,Object> map2 = userStatisticsFacade.findNumByLessBkcommentCount(count);
						flagint = 0;
						if(map2!=null && map2.get("flag")!=null){
							flagint = (Integer) map2.get("flag");
						}
						if(flagint==ResultUtils.SUCCESS){
							long lessnum = (Long) map2.get("count");
							percent = (double)lessnum/(double)totalnum;
							
							//需要处理真实的百分比============================
							/*double tjz = percent/8;
							int n = 19;
							n = n- (int)Math.log(count);
							if(n<=0){
								n=1;
							}
							percent = Math.pow((percent-tjz), n);*/
							
							//应该随之时间的推移，相同数量的阅读量战胜的百分比越少，100天减少一个百分比
							long nowtime = System.currentTimeMillis()/1000/60/60/24;
							//System.out.println(nowtime);
							int days = 16637;
							int n = 18;
							double value = 0.75-(0.0001*(nowtime-days));
							//int count = 6;
							double value2 = (double)count/450;
							value = value + value2;
							if(value>0.99){
								value = 0.99;
							}
							n = n- (int)(2.9*Math.log(count));
							if(n<=0){
								n=1;
							}
							percent = Math.pow(value, n);
							//========================================
							
							percentnum = (int)(percent*100);
							if(percentnum==0){
								percentnum = 1;
							}
							/**
							 读书
							0%~20%：超凡脱俗就不看书
							20%~40%：没文化，真可怕
							40%~60%：你风骚的阅读量完全征服了我
							60%~80%：一位有内涵的人
							80%~100%请接受我的膜拜吧，大神
							 */
							if(percent>=0 && percent<0.2){
								info = "超凡脱俗就不看书！";
							}else if(percent>=0.2 && percent<0.4){
								info = "没文化，真可怕";
							}else if(percent>=0.4 && percent<0.6){
								info = "你风骚的阅读量完全征服了我";
							}else if(percent>=0.6 && percent<0.8){
								info = "一位有内涵的人";
							}else if(percent>=0.8 && percent<=1){
								info = "请接受我的膜拜吧，\r\n大神！";
							}
						}else{
//							//出错
						}
					}else{
						//出错
					}
				}else{
					//阅读了0本，战胜了0%的用户
					
				}
			}
		}else{
			//默认为电影
			String mvcomment_count = userStatisticsMaps.get(JedisConstant.MVCOMMENT_COUNT);
			if(StringUtils.isInteger(mvcomment_count)){
				count = Long.valueOf(mvcomment_count);
			}
			if(count<=0){
				//如果缓存没有，再查数据库，然后存入缓存
				UserStatistics userStatistics = userStatisticsFacade.findUserStatisticsByUid(uid);
				if(userStatistics!=null && userStatistics.getId()>0){
					if(userStatistics.getMvcommentCount()>0){
						//
						flagint = ResultUtils.SUCCESS;
						userJedisManager.saveOneUserResourceCount(uid, JedisConstant.MVCOMMENT_COUNT, userStatistics.getMvcommentCount()+"");
						count = userStatistics.getMvcommentCount();
					}else{
						//需要通过查询影评表查询数量
						Map<String, Object> map = mvCommentFacade.findMvCommentCountByUid(uid);
						if(map!=null && map.get("flag")!=null){
							flagint = (Integer) map.get("flag");
						}
						if(flagint == ResultUtils.SUCCESS){
							count = (Integer) map.get("count");
							if(count>0){
								userStatisticsFacade.updateMvcommentNewCount(uid, count);
								userJedisManager.saveOneUserResourceCount(uid, JedisConstant.MVCOMMENT_COUNT, count+"");
							}
						}else{
							//出错
							
						}
					}
				}else if(userStatistics!=null && userStatistics.getFlag()==ResultUtils.ERROR){
					//出错
					flagint = userStatistics.getFlag();
				}else{
					//不存在
					//需要通过查询书评表查询数量
					Map<String, Object> map = mvCommentFacade.findMvCommentCountByUid(uid);
					if(map!=null && map.get("flag")!=null){
						flagint = (Integer) map.get("flag");
					}
					if(flagint == ResultUtils.SUCCESS){
						count = (Integer) map.get("count");
						if(count>0){
							userStatisticsFacade.insertUserStatistics(uid);
							userStatisticsFacade.updateMvcommentNewCount(uid, count);
							userJedisManager.saveOneUserResourceCount(uid, JedisConstant.MVCOMMENT_COUNT, count+"");
						}
					}else{
						//出错
						
					}
				}
			}else{
				//
				flagint = ResultUtils.SUCCESS;
			}
			if(flagint==ResultUtils.SUCCESS){
				//查询总人数，再查询小于当前用户书评或影评数量的人数
				if(count>0){
					Map<String, Object> map = userStatisticsFacade.findTotalNum();//查询总条数
					flagint = 0;
					if(map!=null && map.get("flag")!=null){
						flagint = (Integer) map.get("flag");
					}
					if(flagint==ResultUtils.SUCCESS){
						long totalnum = (Long) map.get("count");
						//查询小于书评数量的条数
						Map<String,Object> map2 = userStatisticsFacade.findNumByLessMvcommentCount(count);
						flagint = 0;
						if(map2!=null && map2.get("flag")!=null){
							flagint = (Integer) map2.get("flag");
						}
						if(flagint==ResultUtils.SUCCESS){
							long lessnum = (Long) map2.get("count");
							percent = (double)lessnum/(double)totalnum;
							
							//需要处理真实的百分比============================
							/*double tjz = percent/8;
							int n = 20;
							n = n- (int)Math.log(count);
							if(n<=0){
								n=1;
							}
							percent = Math.pow((percent-tjz), n);*/
							
							//应该随之时间的推移，相同数量的阅读量战胜的百分比越少，100天减少一个百分比
							long nowtime = System.currentTimeMillis()/1000/60/60/24;
							//System.out.println(nowtime);
							int days = 16637;
							int n = 20;
							double value = 0.80-(0.00012*(nowtime-days));
							//int count = 50;
							double value2 = (double)count/5000;
							value = value + value2;
							if(value>0.99){
								value = 0.99;
							}
							n = n- (int)(2.5*Math.log(count));
							if(n<=0){
								n=1;
							}
							percent = Math.pow(value, n);
							//========================================
							
							
							percentnum = (int)(percent*100);
							if(percentnum==0){
								percentnum = 1;
							}
							/**
							 电影等级
							0%~20%：不食人间烟火，基本不看电影
							20%~40%：观影界的杜蕾斯
							40%~60%：你风骚的阅片量完全征服了我
							60%~80%：观影杀手！不放过任何一部电影！
							80%~100%请接受我的膜拜吧，大神
							 */
							if(percent>=0 && percent<0.2){
								info = "不食人间烟火！\r\n基本不看电影！";
							}else if(percent>=0.2 && percent<0.4){
								info = "观影界的杜蕾斯";
							}else if(percent>=0.4 && percent<0.6){
								info = "你风骚的阅片量完全征服了我";
							}else if(percent>=0.6 && percent<0.8){
								info = "观影杀手！\r\n不放过任何一部电影！";
							}else if(percent>=0.8 && percent<=1){
								info = "请接受我的膜拜吧！\r\n大神！";
							}
						}else{
//							//出错
						}
					}else{
						//出错
					}
				}else{
					//阅读了0本，战胜了0%的用户
					
				}
			}
			
			
		}
		
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("count", count);
			map.put("percent", percentnum);
			map.put("info", info);
			datas.put("map", map);
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
}
