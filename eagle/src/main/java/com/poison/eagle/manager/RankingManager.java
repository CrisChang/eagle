package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.BookListInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.MovieListInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.TipEntity;
import com.poison.eagle.entity.TopicInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.BookUtils;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HtmlUtil;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.TipUtils;
import com.poison.paycenter.client.PaycenterFacade;
import com.poison.paycenter.model.RewardPesonStatistical;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.RankingFacade;
import com.poison.resource.client.RankingListFacade;
import com.poison.resource.client.ResourceRankingFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.Ranking;
import com.poison.resource.model.RankingList;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.model.ResourceRanking;
import com.poison.resource.model.Topic;
import com.poison.resource.service.ResStatisticService;
import com.poison.store.client.BkFacade;
import com.poison.store.client.BkSearchFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.MvSearchFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.BkSearch;
import com.poison.store.model.MvInfo;
import com.poison.store.model.MvSearch;
import com.poison.store.model.NetBook;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserInfo;
/**
 * 排行manager
 * @author Administrator
 *
 */
public class RankingManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(RankingManager.class);
	
	//private int flagint;
	
	private TopicFacade topicFacade;
	
	private ResourceRankingFacade resourceRankingFacade;
	
	private RankingFacade rankingFacade;
	
	private PaycenterFacade paycenterFacade;
	
	private BkSearchFacade bkSearchFacade;
	
	private MvSearchFacade mvSearchFacade;
	
	private BkFacade bkFacade;
	
	private NetBookFacade netBookFacade;
	
	private MvFacade mvFacade;
	
	private UcenterFacade ucenterFacade;
	
	private BkCommentFacade bkCommentFacade;
	
	private MvCommentFacade mvCommentFacade;
	
	private ActFacade actFacade;
	
	private ResourceManager resourceManager;
	
	private ResourceJedisManager resourceJedisManager;
	
	private GetResourceInfoFacade getResourceInfoFacade;
	
	private ResStatisticService resStatisticService;
	
	private MyMovieFacade myMovieFacade;
	
	private MyBkFacade myBkFacade;
	
	/**
	 * 资源关系
	 */
	private JedisSimpleClient relationToUserandresClient;
	
	/**
	 * 用户的缓存
	 */
	private UserJedisManager userJedisManager;
	
	
	private ResStatJedisManager resStatJedisManager;
	
	/** 原创榜，上周回复榜 **/
	private RankingListFacade rankingListFacade;

	private TipUtils tipUtils = TipUtils.getInstance();
	
	private BookUtils bookUtils = BookUtils.getInstance();
	
	private MovieUtils movieUtils = MovieUtils.getInstance();
	
	private FileUtils fileUtils = FileUtils.getInstance();
	
	
	/**
	 * 查询各种排行信息 
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String ViewRanking(String reqs,long userId){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_SUCCESS;//0：成功、1：失败(该排行方法默认为成功，因为有5个不同的排行，不能因为一个排行的失败而返回失败)
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		//long page = 1;
		long endtime = DateUtil.getTodayStartTime();//当天开始时间毫秒值
		//转化成可读类型
		/*try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}*/
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =3;//每页显示数量
		long start = 0;
		
		long period = 1*24*60*60*1000L;
		//long endtime = System.currentTimeMillis();
		long starttime = endtime - period;
		
		
		//话题
		
		long endtime2 = System.currentTimeMillis();
		long period2 = 7*24*60*60*1000L;
		long starttime2 = endtime2-period2;
		List<Topic> topics = null;
		try{
			topics = topicFacade.findTopicRanking(starttime2, endtime2, start);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		List<TopicInfo> topicInfos = new ArrayList<TopicInfo>();
		int size1 = 0;//当前返回数据集合的长度
		int flagint;
		if(topics!=null && topics.size()==1 && topics.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(topics!=null){
				size1 = topics.size();
			}
			if(size1>3){
				size1=3;
			}
			//封装一次topics
			if(topics!=null && topics.size()>0){
				List<Long> userids=new ArrayList<Long>(size1);
				for(int i=0;i<size1;i++){
					TopicInfo topicInfo = TopicInfo.copy(topics.get(i));
					topicInfos.add(topicInfo);
					userids.add(topicInfo.getUserid());
				}
				List<UserInfo> users = null;
				try{
					users = ucenterFacade.findUserListByUseridList(null, userids);
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				if(users!=null && users.size()>0){
					for(int i=0;i<topicInfos.size();i++){
						for(int j=0;j<users.size();j++){
							if(topicInfos.get(i).getUserid()==users.get(j).getUserId()){
								topicInfos.get(i).setNickname(users.get(j).getName());
								break;
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("topics", topicInfos);
			//datas.put("size", size1);
			//datas.put("page", page);
			//datas.put("pagesize", pagesize);
			//datas.put("endtime", endtime+"");
		}else{
			//flag = CommentUtils.RES_FLAG_ERROR;
			//error = MessageUtils.getResultMessage(flagint);
			//LOG.error("错误代号:"+flagint+",错误信息:"+error);
			//datas.put("error", error);
			datas.put("topics", topicInfos);
		}
		
		//内容
		List<ResourceRanking> resourceRankings = null;
		try{
			resourceRankings = resourceRankingFacade.findResRanking(starttime, endtime, start);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		List<String> list = new ArrayList<String>(pagesize);
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		List<Long> resourceids = new ArrayList<Long>(pagesize);
		int size2 = 0;//当前返回数据集合的长度
		if(resourceRankings!=null && resourceRankings.size()==1 && resourceRankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(resourceRankings!=null){
				size2 = resourceRankings.size();
			}
			if(size2>3){
				size2=3;
			}
			if(resourceRankings!=null && resourceRankings.size()>0){
				for(int i=0;i<resourceRankings.size();i++){
					ResourceRanking resourceRanking = resourceRankings.get(i);
					if(resourceRanking!=null && resourceRanking.getResId()>0){
						if(list.size()<size2){
							//当小于需要的数量时候
							//System.out.println("=================资源id:"+resourceRanking.getResId()+" 资源类型:"+resourceRanking.getResType());
							String resourcestr = null;
							try{
								resourcestr = resourceJedisManager.getOneResource(resourceRanking.getResId());
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							if(resourcestr!=null){
								//System.out.println("=======================缓存中查出的数据:"+resourcestr);
								list.add(resourcestr);
								resourceids.add(resourceRanking.getResId());
							}else{
								ResourceInfo resourceInfo = null;
								try{
									resourceInfo = resourceManager.getResourceByIdAndType(resourceRanking.getResId(), resourceRanking.getResType(), userId);
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}
								if(resourceInfo!=null && resourceInfo.getRid()>0){
									//infos.add(resourceInfo);
									try {
										resourceInfo.setcNum(resourceRanking.getCommentAmount());
										//resourceInfos.add(resourceInfo);
										resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
										list.add(resourcestr);
										resourceids.add(resourceRanking.getResId());
									} catch (JsonGenerationException e) {
										e.printStackTrace();
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
									try{
										//int result = resourceManager.setResourceToJedis(resourceInfo, userId, userId,0l);
										resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
										//System.out.println("=======================放入缓存的结果:"+result);
									}catch(Exception e){
										LOG.error(e.getMessage(),e.fillInStackTrace());
									}
								}
							}
						}else{
							break;
						}
					}
				}
				try{
					list = handleJsonValue(userId, list, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("size", size);
			//datas.put("page", page);
			//datas.put("pagesize", pagesize);
			//resString = RES_DATA_RIGHT_BEGIN+"\"size\":"+size2+",\"page\":"+page+",\"pagesize\":"+pagesize+",\"endtime\":\""+endtime+"\",\"list\":"+list.toString()+RES_DATA_RIGHT_END;
			datas.put("resourceInfos", resourceInfos);
		}else{
			datas.put("resourceInfos", resourceInfos);
		}
		
		//被打赏
		List<RewardPesonStatistical> pesonStatisticals = null;
		try{
			pesonStatisticals = paycenterFacade.getCollListInfoByUserIdDesc(start);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size3 = 0;
		List<TipEntity> tipEntitys = new ArrayList<TipEntity>();
		if(pesonStatisticals!=null && pesonStatisticals.size()==1 && pesonStatisticals.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//查询失败
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(pesonStatisticals!=null){
				size3 = pesonStatisticals.size();
			}
			if(size3>3){
				size3=3;
				pesonStatisticals = pesonStatisticals.subList(0, 3);
			}
			tipEntitys = getRewardPesonStatisticalList(pesonStatisticals, userId, "1", tipEntitys);
		}
		
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("tips", tipEntitys);
			//datas.put("size", size3);
			//datas.put("page", page);
			//datas.put("pagesize", pagesize);
		}else{
			//flag = CommentUtils.RES_FLAG_ERROR;
			//error = MessageUtils.getResultMessage(flagint);
			//LOG.error("错误代号:"+flagint+",错误信息:"+error);
			//datas.put("error", error);
			datas.put("tips", tipEntitys);
		}
		
		
		//图书热搜
		long bkstarttime = starttime;
		long bkendtime = endtime;
		String  startdate = DateUtil.format(bkstarttime, DateUtil.dateformat);
		String enddate = DateUtil.format(bkendtime-1, DateUtil.dateformat);
		List<BkSearch> bkSearchs= null;
		try{
			bkSearchs = bkSearchFacade.findBkRanking(startdate, enddate, start, pagesize);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		List<BookInfo> bookInfos = new ArrayList<BookInfo>();
		int size4 = 0;
		if(bkSearchs!=null && bkSearchs.size()==1 && bkSearchs.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//查询失败
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(bkSearchs!=null){
				size4 = bkSearchs.size();
			}
			if(size4==0){
				//当昨天开始时间没有数据，则查询前天的
				if(start==0 && DateUtil.getTodayStartTime()==bkendtime){
					long onedaytime = 1*24*60*60*1000L;
					boolean c = false;
					int times = 3;//最多循环次数
					int thetime = 0;//当前次数
					do{
						thetime++;
						bkendtime = bkendtime - onedaytime;
						bkstarttime = bkendtime - period;
						startdate = DateUtil.format(bkstarttime, DateUtil.dateformat);
						enddate = DateUtil.format(bkendtime-1, DateUtil.dateformat);
						bkSearchs= bkSearchFacade.findBkRanking(startdate, enddate, start, pagesize);
						if(bkSearchs!=null && bkSearchs.size()==1 && bkSearchs.get(0).getFlag()==ResultUtils.QUERY_ERROR){
							//查询失败
							flagint = ResultUtils.QUERY_ERROR;
							c = false;
							break;
						}else{
							flagint = ResultUtils.SUCCESS;
							if(bkSearchs!=null){
								size4 = bkSearchs.size();
							}
							if(size4>0){
								c = false;
								break;
							}else{
								if(thetime>=times){
									c = false;
									break;
								}else{
									c = true;
								}
							}
						}
					}while(c);
				}
			}
			if(flagint == ResultUtils.SUCCESS && size4>0){
			//if(size4>0){
				for(int i=0;i<size4;i++){
					if(CommentUtils.TYPE_BOOK.equals(bkSearchs.get(i).getBookType())){
						//图书
						BkInfo bkInfo = bkFacade.findBkInfo((int)bkSearchs.get(i).getBookId());
						BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
								//bookUtils.putBKToBookInfoIndex(bkInfo, FALSE,userId,bkCommentFacade,ucenterFacade);
						bookInfos.add(bookInfo);
					}else if(CommentUtils.TYPE_NETBOOK.equals(bkSearchs.get(i).getBookType())){
						//网络小说
						NetBook netBook = netBookFacade.findNetBookInfoById(bkSearchs.get(i).getBookId());
						BookInfo bookInfo = fileUtils.putBKToBookInfo(netBook, FALSE);
								//bookUtils.putBKToBookInfoIndex(netBook, FALSE,userId,bkCommentFacade,ucenterFacade);
						bookInfos.add(bookInfo);
					}
				}
			}
		}
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("bookInfos", bookInfos);
		}else{
			datas.put("bookInfos", bookInfos);
		}
		
		//电影热搜 
		long mvstarttime = starttime;
		long mvendtime = endtime;
		String  mvstartdate = DateUtil.format(mvstarttime, DateUtil.dateformat);
		String mvenddate = DateUtil.format(mvendtime-1, DateUtil.dateformat);
		List<MvSearch> mvSearchs= null;
		try{
			mvSearchs = mvSearchFacade.findMvRanking(mvstartdate, mvenddate, start, pagesize);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		int size5 = 0;
		if(mvSearchs!=null && mvSearchs.size()==1 && mvSearchs.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//查询失败
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(mvSearchs!=null){
				size5 = mvSearchs.size();
			}
			if(size5==0){
				//当昨天开始时间没有数据，则查询前天的
				if(start==0 && DateUtil.getTodayStartTime()==mvendtime){
					long onedaytime = 1*24*60*60*1000L;
					boolean c = false;
					int times = 3;//最多循环次数
					int thetime = 0;//当前次数
					do{
						thetime++;
						mvendtime = mvendtime - onedaytime;
						mvstarttime = mvendtime - period;
						mvstartdate = DateUtil.format(mvstarttime, DateUtil.dateformat);
						mvenddate = DateUtil.format(mvendtime-1, DateUtil.dateformat);
						mvSearchs= mvSearchFacade.findMvRanking(mvstartdate, mvenddate, start, pagesize);
						if(mvSearchs!=null && mvSearchs.size()==1 && mvSearchs.get(0).getFlag()==ResultUtils.QUERY_ERROR){
							//查询失败
							flagint = ResultUtils.QUERY_ERROR;
							c = false;
							break;
						}else{
							flagint = ResultUtils.SUCCESS;
							if(mvSearchs!=null){
								size5 = mvSearchs.size();
							}
							if(size5>0){
								c = false;
								break;
							}else{
								if(thetime>=times){
									c = false;
									break;
								}else{
									c = true;
								}
							}
						}
					}while(c);
				}
			}
			if(flagint == ResultUtils.SUCCESS && size5>0){
			//if(size5>0){
				for(int i=0;i<size5;i++){
					MvInfo mvInfo = mvFacade.queryById(mvSearchs.get(i).getMovieId());
					MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, FALSE);
					movieInfos.add(movieInfo);
				}
			}
		}
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("movieInfos", movieInfos);
		}else{
			datas.put("movieInfos", movieInfos);
		}
		
		
		
		
		
		
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		//需要替换 内容排行榜的 字符串
		try{
			resString = resString.replace("\"resourceInfos\":[]", "\"resourceInfos\":"+list.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return resString;
	}
	
	/**
	 * 查询图书热搜榜
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String ViewBookSearchRanking(String reqs,long userId){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		long page = 1;
		long endtime = DateUtil.getTodayStartTime();//当天开始时间毫秒值
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			String endtimeStr = dataq.get("endtime")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.valueOf(pageStr);
			}
			if(page<=0){
				page = 1;
			}
			if(StringUtils.isInteger(endtimeStr)){
				endtime = Long.parseLong(endtimeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		long period = 1*24*60*60*1000L;
		//long endtime = System.currentTimeMillis();
		long starttime = endtime - period;
		
		int alllimit = 50;//显示的总条数
		if(start<alllimit){
			if(alllimit-start<pagesize){
				pagesize = (int) (alllimit-start);
			}
		}else{
			//已经查完了显示的总条数
			pagesize = 0;
		}
		
		//图书热搜
		//需要也查询出排名第一的搜索信息
		List<BkSearch> firstbkSearchs = null;
		
		String  startdate = DateUtil.format(starttime, DateUtil.dateformat);
		String enddate = DateUtil.format(endtime-1, DateUtil.dateformat);
		List<BkSearch> bkSearchs = null;
		if(pagesize>0){
			bkSearchs= bkSearchFacade.findBkRanking(startdate, enddate, start, pagesize);
		}
		List<BookInfo> bookInfos = new ArrayList<BookInfo>();
		int size = 0;
		int flagint;
		if(bkSearchs!=null && bkSearchs.size()==1 && bkSearchs.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//查询失败
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(bkSearchs!=null){
				size = bkSearchs.size();
			}
			long needSearchNum = 70000;
			
			int bs = 0;
			
			long lastSearchNum = 0;//排行中上一次的搜索量
			
			long firstSearchNum = 0;//排名第一的搜索量
			if(size==0){
				//当昨天开始时间没有数据，则查询前天的
				if(start==0 && DateUtil.getTodayStartTime()==endtime){
					long onedaytime = 1*24*60*60*1000L;
					boolean c = false;
					int times = 3;//最多循环次数
					int thetime = 0;//当前次数
					do{
						thetime++;
						endtime = endtime - onedaytime;
						starttime = endtime - period;
						startdate = DateUtil.format(starttime, DateUtil.dateformat);
						enddate = DateUtil.format(endtime-1, DateUtil.dateformat);
						bkSearchs= bkSearchFacade.findBkRanking(startdate, enddate, start, pagesize);
						if(bkSearchs!=null && bkSearchs.size()==1 && bkSearchs.get(0).getFlag()==ResultUtils.QUERY_ERROR){
							//查询失败
							flagint = ResultUtils.QUERY_ERROR;
							c = false;
							break;
						}else{
							flagint = ResultUtils.SUCCESS;
							if(bkSearchs!=null){
								size = bkSearchs.size();
							}
							if(size>0){
								//排名第一的
								firstbkSearchs = new ArrayList<BkSearch>(1);
								firstbkSearchs.add(bkSearchs.get(0));
								c = false;
								break;
							}else{
								if(thetime>=times){
									c = false;
									break;
								}else{
									c = true;
								}
							}
						}
					}while(c);
				}
			}else{
				if(start==0){
					firstbkSearchs = new ArrayList<BkSearch>(1);
					firstbkSearchs.add(bkSearchs.get(0));
				}else{
					//排名第一的
					firstbkSearchs = bkSearchFacade.findBkRanking(startdate, enddate, 0, 1);
					//还需要查出上一页的最后一条
					List<BkSearch> lastBkSearchs = bkSearchFacade.findBkRanking(startdate, enddate, start-1, 1);
					if(lastBkSearchs!=null && lastBkSearchs.size()>0 && lastBkSearchs.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
						if(bs==0){
							if(firstbkSearchs!=null && firstbkSearchs.size()>0 && firstbkSearchs.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
								long searchNum = getSearchNum(firstbkSearchs.get(0).getBookId(),firstbkSearchs.get(0).getSearchNum(), firstbkSearchs.get(0).getCreatetime(),0,bs,0);
								bs = (int) (needSearchNum/searchNum);
								firstSearchNum = getSearchNum(firstbkSearchs.get(0).getBookId(),firstbkSearchs.get(0).getSearchNum(), firstbkSearchs.get(0).getCreatetime(),0,bs,0);
							}
						}
						long searchNum = getSearchNum(lastBkSearchs.get(0).getBookId(),lastBkSearchs.get(0).getSearchNum(), lastBkSearchs.get(0).getCreatetime(),0,bs,firstSearchNum);
						searchNum = searchNum - searchNum/10;
						searchNum = getSearchNum(lastBkSearchs.get(0).getBookId(),lastBkSearchs.get(0).getSearchNum(), lastBkSearchs.get(0).getCreatetime(),searchNum,bs,firstSearchNum);
						lastSearchNum = searchNum;
					}
				}
			}
			if(flagint == ResultUtils.SUCCESS && size>0){
				
				long ids[]=new long[size];//图书id
				
				long netids[]=new long[size];//网络小说id
				
				List<Long> bkids = new ArrayList<Long>(size);
				List<Long> netbkids = new ArrayList<Long>(size);
				
				for(int i=0;i<size;i++){
					if(CommentUtils.TYPE_BOOK.equals(bkSearchs.get(i).getBookType())){
						ids[i]=bkSearchs.get(i).getBookId();
						bkids.add(bkSearchs.get(i).getBookId());
					}else if(CommentUtils.TYPE_NETBOOK.equals(bkSearchs.get(i).getBookType())){
						netids[i]=bkSearchs.get(i).getBookId();
						netbkids.add(bkSearchs.get(i).getBookId());
					}
				}
				
				List<BkInfo> bkInfos = bkFacade.findBkInfosByIds(ids);
				
				List<NetBook> netBooks = netBookFacade.findNetBkInfosByIds(netids);
				
				List<BkAvgMark> bkAvgMarks = bkCommentFacade.findBkAvgMarkByBkIds(bkids, CommentUtils.TYPE_BOOK);
				List<BkAvgMark> netbkAvgMarks = bkCommentFacade.findBkAvgMarkByBkIds(netbkids, CommentUtils.TYPE_NETBOOK);
				
				for(int i=0;i<size;i++){
					if(CommentUtils.TYPE_BOOK.equals(bkSearchs.get(i).getBookType())){
						if(bkInfos!=null && bkInfos.size()>0 && bkInfos.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
							for(int j=0;j<bkInfos.size();j++){
								if(bkInfos.get(j).getId()==bkSearchs.get(i).getBookId()){
									//图书
									BkInfo bkInfo = bkInfos.get(j);
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
											//bookUtils.putBKToBookInfoIndex(bkInfo, FALSE,userId,bkCommentFacade,ucenterFacade);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									//需要赋值搜索量和热度
									if(bs==0){
										if(firstbkSearchs!=null && firstbkSearchs.size()>0 && firstbkSearchs.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
											long searchNum = getSearchNum(firstbkSearchs.get(0).getBookId(),firstbkSearchs.get(0).getSearchNum(), firstbkSearchs.get(0).getCreatetime(),0,bs,0);
											bs = (int) (needSearchNum/searchNum);
											firstSearchNum = getSearchNum(firstbkSearchs.get(0).getBookId(),firstbkSearchs.get(0).getSearchNum(), firstbkSearchs.get(0).getCreatetime(),0,bs,0);
										}
									}
									
									long searchNum = getSearchNum(bkSearchs.get(i).getBookId(),bkSearchs.get(i).getSearchNum(), bkSearchs.get(i).getCreatetime(),lastSearchNum,bs,firstSearchNum);
									
									lastSearchNum = searchNum;
									bookInfo.setSearchNum(searchNum+"");
									//需要查询热度
									Map<String, Object> hotmap = actFacade.findHotCount(bkInfo.getId(), CommentUtils.TYPE_BOOK);
									int hotnum = 0;
									if(hotmap!=null){
										if(hotmap.get("flag")!=null && (ResultUtils.SUCCESS+"").equals(hotmap.get("flag")+"")){
											String hotnumstr = hotmap.get("count")+"";
											if(StringUtils.isInteger(hotnumstr)){
												hotnum = Integer.valueOf(hotnumstr);
											}
										}
									}
									hotnum = getHotNum(searchNum, hotnum);
									bookInfo.setHotNum(hotnum+"");
									//需要赋值评分
									float expertsAvgMark = 0;
									if(bkAvgMarks!=null && bkAvgMarks.size()>0 && bkAvgMarks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
										for(int z=0;z<bkAvgMarks.size();z++){
											if(bkAvgMarks.get(z).getBkId()==bkInfo.getId()){
												//bookInfo.setScore(bkAvgMarks.get(z).getBkAvgMark()+"");
												//bookInfo.setTalentScore(bkAvgMarks.get(z).getExpertsAvgMark()+"");
												//暂时调换分数
												expertsAvgMark = bkAvgMarks.get(z).getBkAvgMark();
												bookInfo.setScore(bkAvgMarks.get(z).getExpertsAvgMark()+"");
												bookInfo.setTalentScore(bkAvgMarks.get(z).getBkAvgMark()+"");
											}
										}
									}
									//需要判断如果神人评分小于等于0，则不返回
									if(expertsAvgMark>0){
										bookInfos.add(bookInfo);
									}
									break;
								}
							}
						}
					}else if(CommentUtils.TYPE_NETBOOK.equals(bkSearchs.get(i).getBookType())){
						if(netBooks!=null && netBooks.size()>0 && netBooks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
							for(int j=0;j<netBooks.size();j++){
								if(netBooks.get(j).getId()==bkSearchs.get(i).getBookId()){
									//网络小说
									NetBook netBook = netBooks.get(j);
									BookInfo bookInfo = fileUtils.putBKToBookInfo(netBook, FALSE);
											//bookUtils.putBKToBookInfoIndex(netBook, FALSE,userId,bkCommentFacade,ucenterFacade);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									//需要赋值搜索量和热度
									if(bs==0){
										if(firstbkSearchs!=null && firstbkSearchs.size()>0 && firstbkSearchs.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
											long searchNum = getSearchNum(firstbkSearchs.get(0).getBookId(),firstbkSearchs.get(0).getSearchNum(), firstbkSearchs.get(0).getCreatetime(),0,bs,0);
											bs = (int) (needSearchNum/searchNum);
											//bs = (int) (needSearchNum/firstbkSearchs.get(0).getSearchNum());
											firstSearchNum = getSearchNum(firstbkSearchs.get(0).getBookId(),firstbkSearchs.get(0).getSearchNum(), firstbkSearchs.get(0).getCreatetime(),0,bs,0);
										}
									}
									long searchNum = getSearchNum(bkSearchs.get(i).getBookId(),bkSearchs.get(i).getSearchNum(), bkSearchs.get(i).getCreatetime(),lastSearchNum,bs,firstSearchNum);
									lastSearchNum = searchNum;
									bookInfo.setSearchNum(searchNum+"");
									//需要查询热度
									Map<String, Object> hotmap = actFacade.findHotCount(netBook.getId(), CommentUtils.TYPE_NETBOOK);
									int hotnum = 0;
									if(hotmap!=null){
										if(hotmap.get("flag")!=null && (ResultUtils.SUCCESS+"").equals(hotmap.get("flag")+"")){
											String hotnumstr = hotmap.get("count")+"";
											if(StringUtils.isInteger(hotnumstr)){
												hotnum = Integer.valueOf(hotnumstr);
											}
										}
									}
									hotnum = getHotNum(searchNum, hotnum);
									bookInfo.setHotNum(hotnum+"");
									//需要赋值评分
									float expertsAvgMark = 0;
									if(netbkAvgMarks!=null && netbkAvgMarks.size()>0 && netbkAvgMarks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
										for(int z=0;z<netbkAvgMarks.size();z++){
											if(netbkAvgMarks.get(z).getBkId()==netBook.getId()){
												//bookInfo.setScore(netbkAvgMarks.get(z).getBkAvgMark()+"");
												//bookInfo.setTalentScore(netbkAvgMarks.get(z).getExpertsAvgMark()+"");
												//暂时调换分数
												expertsAvgMark = netbkAvgMarks.get(z).getBkAvgMark();
												bookInfo.setScore(netbkAvgMarks.get(z).getExpertsAvgMark()+"");
												bookInfo.setTalentScore(netbkAvgMarks.get(z).getBkAvgMark()+"");
											}
										}
									}
									if(expertsAvgMark>0){
										bookInfos.add(bookInfo);
									}
									break;
								}
							}
						}
					}
				}
			}
		}
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", bookInfos);
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("endtime", endtime+"");
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
	 * 查询电影热搜榜
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String ViewMovieSearchRanking(String reqs,long userId){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		long page = 1;
		long endtime = DateUtil.getTodayStartTime();//当天开始时间毫秒值
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			String endtimeStr = dataq.get("endtime")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.valueOf(pageStr);
			}
			if(page<=0){
				page = 1;
			}
			if(StringUtils.isInteger(endtimeStr)){
				endtime = Long.parseLong(endtimeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		long period = 1*24*60*60*1000L;
		//long endtime = System.currentTimeMillis();
		long starttime = endtime - period;
		
		int alllimit = 50;//显示的总条数
		if(start<alllimit){
			if(alllimit-start<pagesize){
				pagesize = (int) (alllimit-start);
			}
		}else{
			//已经查完了显示的总条数
			pagesize = 0;
		}
		
		//图书热搜
		//需要也查询出排名第一的搜索信息
		List<MvSearch> firstMvSearchs = null;
				
		String  startdate = DateUtil.format(starttime, DateUtil.dateformat);
		String enddate = DateUtil.format(endtime-1, DateUtil.dateformat);
		List<MvSearch> mvSearchs = null;
		if(pagesize>0){
			mvSearchs= mvSearchFacade.findMvRanking(startdate, enddate, start, pagesize);
		}
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		int size = 0;
		int flagint;
		if(mvSearchs!=null && mvSearchs.size()==1 && mvSearchs.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//查询失败
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(mvSearchs!=null){
				size = mvSearchs.size();
			}
			long lastSearchNum = 0;//排行中上一次的搜索量
			int bs = 0;
			long needSearchNum = 90000;
			long firstSearchNum = 0;//排名第一的搜索量
			if(size==0){
				//当昨天开始时间没有数据，则查询前天的
				if(start==0 && DateUtil.getTodayStartTime()==endtime){
					long onedaytime = 1*24*60*60*1000L;
					boolean c = false;
					int times = 3;//最多循环次数
					int thetime = 0;//当前次数
					do{
						thetime++;
						endtime = endtime - onedaytime;
						starttime = endtime - period;
						startdate = DateUtil.format(starttime, DateUtil.dateformat);
						enddate = DateUtil.format(endtime-1, DateUtil.dateformat);
						mvSearchs= mvSearchFacade.findMvRanking(startdate, enddate, start, pagesize);
						if(mvSearchs!=null && mvSearchs.size()==1 && mvSearchs.get(0).getFlag()==ResultUtils.QUERY_ERROR){
							//查询失败
							flagint = ResultUtils.QUERY_ERROR;
							c = false;
							break;
						}else{
							flagint = ResultUtils.SUCCESS;
							if(mvSearchs!=null){
								size = mvSearchs.size();
							}
							if(size>0){
								//排名第一的
								firstMvSearchs = new ArrayList<MvSearch>(1);
								firstMvSearchs.add(mvSearchs.get(0));
								c = false;
								break;
							}else{
								if(thetime>=times){
									c = false;
									break;
								}else{
									c = true;
								}
							}
						}
					}while(c);
				}
			}else{
				if(start==0){
					firstMvSearchs = new ArrayList<MvSearch>(1);
					firstMvSearchs.add(mvSearchs.get(0));
				}else{
					firstMvSearchs = mvSearchFacade.findMvRanking(startdate, enddate, 0, 1);
					//还需要查出上一页的最后一条，为了防止当前页第一条的搜索量多于上一页的最后一条
					List<MvSearch> lastMvSearchs = mvSearchFacade.findMvRanking(startdate, enddate, start-1, 1);
					if(lastMvSearchs!=null && lastMvSearchs.size()>0 && lastMvSearchs.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
						if(bs==0){
							if(firstMvSearchs!=null && firstMvSearchs.size()>0 && firstMvSearchs.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
								long searchNum = getSearchNum(firstMvSearchs.get(0).getMovieId(),firstMvSearchs.get(0).getSearchNum(), firstMvSearchs.get(0).getCreatetime(),0,bs,0);
								bs = (int) (needSearchNum/searchNum);
								firstSearchNum = getSearchNum(firstMvSearchs.get(0).getMovieId(),firstMvSearchs.get(0).getSearchNum(), firstMvSearchs.get(0).getCreatetime(),0,bs,0);
							}
						}
						long searchNum = getSearchNum(lastMvSearchs.get(0).getMovieId(),lastMvSearchs.get(0).getSearchNum(), lastMvSearchs.get(0).getCreatetime(),0,bs,firstSearchNum);
						searchNum = searchNum - searchNum/10;
						searchNum = getSearchNum(lastMvSearchs.get(0).getMovieId(),lastMvSearchs.get(0).getSearchNum(), lastMvSearchs.get(0).getCreatetime(),searchNum,bs,firstSearchNum);
						lastSearchNum = searchNum;
					}
				}
			}
			if(flagint == ResultUtils.SUCCESS && size>0){
				
				long ids[]=new long[size];//图书id
				List<Long> mvids = new ArrayList<Long>(size);
				for(int i=0;i<size;i++){
					ids[i]=mvSearchs.get(i).getMovieId();
					mvids.add(mvSearchs.get(i).getMovieId());
				}
				
				List<MvInfo> mvInfos = mvFacade.findMvInfosByIds(ids);
				List<MvAvgMark> mvAvgMarks = mvCommentFacade.findMvAvgMarkByMvIds(mvids);
				
				if(mvInfos!=null && mvInfos.size()==1 && mvInfos.get(0).getFlag()==ResultUtils.QUERY_ERROR){
					flagint = ResultUtils.QUERY_ERROR;
				}else{
					if(mvInfos!=null && mvInfos.size()>0){
						for(int i=0;i<size;i++){
							for(int j=0;j<mvInfos.size();j++){
								if(mvInfos.get(j).getId()==mvSearchs.get(i).getMovieId()){
									//电影
									MvInfo mvInfo = mvInfos.get(j);
									MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, FALSE);
									movieInfo.setDescribe(HtmlUtil.getTextFromHtml(movieInfo.getDescribe()));
									//需要赋值搜索量和热度
									if(bs==0){
										if(firstMvSearchs!=null && firstMvSearchs.size()>0 && firstMvSearchs.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
											long searchNum = getSearchNum(firstMvSearchs.get(0).getMovieId(),firstMvSearchs.get(0).getSearchNum(), firstMvSearchs.get(0).getCreatetime(),0,bs,0);
											bs = (int) (needSearchNum/searchNum);
											firstSearchNum = getSearchNum(firstMvSearchs.get(0).getMovieId(),firstMvSearchs.get(0).getSearchNum(), firstMvSearchs.get(0).getCreatetime(),0,bs,0);
										}
									}
									long searchNum = getSearchNum(mvSearchs.get(i).getMovieId(),mvSearchs.get(i).getSearchNum(), mvSearchs.get(i).getCreatetime(),lastSearchNum,bs,firstSearchNum);
									lastSearchNum = searchNum;
									movieInfo.setSearchNum(searchNum+"");
									//需要查询热度
									Map<String, Object> hotmap = actFacade.findHotCount(mvInfo.getId(), CommentUtils.TYPE_MOVIE);
									int hotnum = 0;
									if(hotmap!=null){
										if(hotmap.get("flag")!=null && (ResultUtils.SUCCESS+"").equals(hotmap.get("flag")+"")){
											String hotnumstr = hotmap.get("count")+"";
											if(StringUtils.isInteger(hotnumstr)){
												hotnum = Integer.valueOf(hotnumstr);
											}
										}
									}
									hotnum = getHotNum(searchNum, hotnum);
									movieInfo.setHotNum(hotnum+"");
									//movieInfo.setHotNum(mvSearchs.get(i).getSearchNum()+"");
									//需要赋值评分
									float expertsAvgMark = 0;
									if(mvAvgMarks!=null && mvAvgMarks.size()>0 && mvAvgMarks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
										for(int z=0;z<mvAvgMarks.size();z++){
											if(mvAvgMarks.get(z).getMvId()==mvInfo.getId()){
												expertsAvgMark = mvAvgMarks.get(z).getExpertsAvgMark();
												movieInfo.setScore(mvAvgMarks.get(z).getMvAvgMark()+"");
												movieInfo.setTalentScore(mvAvgMarks.get(z).getExpertsAvgMark()+"");
											}
										}
									}
									if(expertsAvgMark>0){
										movieInfos.add(movieInfo);
									}
									break;
								}
							}
						}
					}
				}
			}
		}
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", movieInfos);
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("endtime", endtime+"");
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
	 * 根据评论数量查询资源的排行
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewResourceRanking(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		long page = 1;
		long endtime = DateUtil.getTodayStartTime();//当天开始时间毫秒值
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			String endtimeStr = dataq.get("endtime")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
			if(StringUtils.isInteger(endtimeStr)){
				endtime = Long.parseLong(endtimeStr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		long period = 1*24*60*60*1000L;
		//long endtime = System.currentTimeMillis();
		long starttime = endtime - period;
		
		List<ResourceRanking> resourceRankings = null;
		try{
			resourceRankings = resourceRankingFacade.findResRanking(starttime, endtime, start);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		List<String> list = new ArrayList<String>(pagesize);
		List<Long> resourceids = new ArrayList<Long>(pagesize);
		int size = 0;//当前返回数据集合的长度
		int flagint;
		if(resourceRankings!=null && resourceRankings.size()==1 && resourceRankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(resourceRankings!=null){
				size = resourceRankings.size();
			}
			if(resourceRankings!=null && resourceRankings.size()>0){
				for(int i=0;i<resourceRankings.size();i++){
					ResourceRanking resourceRanking = resourceRankings.get(i);
					if(resourceRanking!=null && resourceRanking.getResId()>0){
						//System.out.println("=================资源id:"+resourceRanking.getResId()+" 资源类型:"+resourceRanking.getResType());
						String resourcestr = null;
						try{
							resourcestr = resourceJedisManager.getOneResource(resourceRanking.getResId());
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}
						if(resourcestr!=null){
							//System.out.println("=======================缓存中查出的数据:"+resourcestr);
							list.add(resourcestr);
							resourceids.add(resourceRanking.getResId());
						}else{
							ResourceInfo resourceInfo = null;
							try{
								resourceInfo = resourceManager.getResourceByIdAndType(resourceRanking.getResId(), resourceRanking.getResType(), userId);
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							if(resourceInfo!=null && resourceInfo.getRid()>0){
								//infos.add(resourceInfo);
								try {
									resourceInfo.setcNum(resourceRanking.getCommentAmount());
									resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
									list.add(resourcestr);
									resourceids.add(resourceRanking.getResId());
								} catch (JsonGenerationException e) {
									e.printStackTrace();
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
								try{
									//int result = resourceManager.setResourceToJedis(resourceInfo, userId, userId,0l);
									//System.out.println("=======================放入缓存的结果:"+result);
									resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}
							}
						}
					}
				}
				try{
					list = handleJsonValue(userId, list, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("size", size);
			//datas.put("page", page);
			//datas.put("pagesize", pagesize);
			resString = RES_DATA_RIGHT_BEGIN+"\"size\":"+size+",\"page\":"+page+",\"pagesize\":"+pagesize+",\"endtime\":\""+endtime+"\",\"list\":"+list.toString()+RES_DATA_RIGHT_END;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
		}
		return resString;
	}
	
	//最新电影推荐榜
	public String latestMoviesRanking(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String ranktype = Ranking.type_latestmovierec;//最新电影推荐榜排行
		String restype = CommentUtils.TYPE_MOVIE;//电影资源类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Ranking> rankings = null;
		try{
			rankings = rankingFacade.findRanking(start, pagesize, ranktype,restype);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		if(rankings!=null && rankings.size()==1 && rankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(rankings!=null){
				size = rankings.size();
			}
			if(rankings!=null && rankings.size()>0){
				long resourceids[] = new long[rankings.size()];
				for(int i=0;i<rankings.size();i++){
					resourceids[i]=rankings.get(i).getResid();
				}
				List<MvInfo> mvInfos = mvFacade.findMvInfosByIds(resourceids);
				if(mvInfos!=null && mvInfos.size()>0){
					for(int i=0;i<rankings.size();i++){
						for(int j=0;j<mvInfos.size();j++){
							if(mvInfos.get(j).getId()==rankings.get(i).getResid()){
								MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfos.get(j), FALSE);
								movieInfos.add(movieInfo);
								mvInfos.remove(j);
								break;
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", movieInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	//最新剧集推荐榜
	public String latestDramasRanking(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String ranktype = Ranking.type_latestdramarec;//最新剧集推荐榜
		String restype = CommentUtils.TYPE_MOVIE;//电影资源类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Ranking> rankings = null;
		try{
			rankings = rankingFacade.findRanking(start, pagesize, ranktype,restype);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		if(rankings!=null && rankings.size()==1 && rankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(rankings!=null){
				size = rankings.size();
			}
			if(rankings!=null && rankings.size()>0){
				long resourceids[] = new long[rankings.size()];
				for(int i=0;i<rankings.size();i++){
					resourceids[i]=rankings.get(i).getResid();
				}
				List<MvInfo> mvInfos = mvFacade.findMvInfosByIds(resourceids);
				if(mvInfos!=null && mvInfos.size()>0){
					for(int i=0;i<rankings.size();i++){
						for(int j=0;j<mvInfos.size();j++){
							if(mvInfos.get(j).getId()==rankings.get(i).getResid()){
								MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfos.get(j), FALSE);
								movieInfos.add(movieInfo);
								mvInfos.remove(j);
								break;
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", movieInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	//每周电影热搜榜
	public String weekMoviesSearchRanking(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String ranktype = Ranking.type_weekmoviesearch;//每周电影热搜榜
		String restype = CommentUtils.TYPE_MOVIE;//电影资源类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Ranking> rankings = null;
		try{
			rankings = rankingFacade.findRanking(start, pagesize, ranktype,restype);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		if(rankings!=null && rankings.size()==1 && rankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(rankings!=null){
				size = rankings.size();
			}
			if(rankings!=null && rankings.size()>0){
				long resourceids[] = new long[rankings.size()];
				for(int i=0;i<rankings.size();i++){
					resourceids[i]=rankings.get(i).getResid();
				}
				List<MvInfo> mvInfos = mvFacade.findMvInfosByIds(resourceids);
				if(mvInfos!=null && mvInfos.size()>0){
					for(int i=0;i<rankings.size();i++){
						for(int j=0;j<mvInfos.size();j++){
							if(mvInfos.get(j).getId()==rankings.get(i).getResid()){
								MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfos.get(j), FALSE);
								movieInfos.add(movieInfo);
								mvInfos.remove(j);
								break;
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", movieInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	//每周剧集热搜榜
	public String weekDramasSearchRanking(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String ranktype = Ranking.type_weekdramasearch;//每周剧集热搜榜
		String restype = CommentUtils.TYPE_MOVIE;//电影资源类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Ranking> rankings = null;
		try{
			rankings = rankingFacade.findRanking(start, pagesize, ranktype,restype);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		if(rankings!=null && rankings.size()==1 && rankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(rankings!=null){
				size = rankings.size();
			}
			if(rankings!=null && rankings.size()>0){
				long resourceids[] = new long[rankings.size()];
				for(int i=0;i<rankings.size();i++){
					resourceids[i]=rankings.get(i).getResid();
				}
				List<MvInfo> mvInfos = mvFacade.findMvInfosByIds(resourceids);
				if(mvInfos!=null && mvInfos.size()>0){
					for(int i=0;i<rankings.size();i++){
						for(int j=0;j<mvInfos.size();j++){
							if(mvInfos.get(j).getId()==rankings.get(i).getResid()){
								MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfos.get(j), FALSE);
								movieInfos.add(movieInfo);
								mvInfos.remove(j);
								break;
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", movieInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	
	//每周热门影单榜
	public String weekHotMovielist(String reqs,Long uid){
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String ranktype = Ranking.type_weekmovielist;//每周热门影单榜
		String restype = CommentUtils.TYPE_MOVIELIST;//影单类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Ranking> rankings = null;
		try{
			rankings = rankingFacade.findRanking(start, pagesize, ranktype,restype);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<MovieListInfo> movieListInfos = new ArrayList<MovieListInfo>(); 
		if(rankings!=null && rankings.size()==1 && rankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(rankings!=null){
				size = rankings.size();
			}
			if(rankings!=null && rankings.size()>0){
				List<Long> resourceids = new ArrayList<Long>(rankings.size());
				for(int i=0;i<rankings.size();i++){
					resourceids.add(rankings.get(i).getResid());
				}
				List<MovieList> movieLists = null;
				if(resourceids.size()>0){
					movieLists = myMovieFacade.findMvListsByIds(resourceids);
				}
				if(movieLists!=null && movieLists.size()>0){
					List<MovieList> ordermovieLists = new ArrayList<MovieList>();
					for(int i=0;i<rankings.size();i++){
						Ranking ranking = rankings.get(i);
						for(int j=0;j<movieLists.size();j++){
							MovieList movieList = movieLists.get(j);
							if(movieList.getId()==ranking.getResid()){
								ordermovieLists.add(movieList);
								movieLists.remove(j);
								break;
							}
						}
					}
					movieListInfos = getResponseMovieList(ordermovieLists, uid, movieListInfos);
					//getResponseList(movieLists,uid, movieListInfos);
					if(movieListInfos == null){
						movieListInfos = new ArrayList<MovieListInfo>();
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", movieListInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	
	//每周图书热搜榜
	public String weekBooksSearchRanking(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String ranktype = Ranking.type_weekbooksearch;//每周图书热搜榜
		String restype = null;
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Ranking> rankings = null;
		try{
			rankings = rankingFacade.findRanking(start, pagesize, ranktype,restype);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<BookInfo> bookInfos = new ArrayList<BookInfo>();
		if(rankings!=null && rankings.size()==1 && rankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(rankings!=null){
				size = rankings.size();
			}
			if(rankings!=null && rankings.size()>0){
				//需要区分图书和网络小说
				List<Long> resourceids = new ArrayList<Long>(rankings.size());
				//long resourceids[] = new long[rankings.size()];
				List<Long> netbkresourceids = new ArrayList<Long>(rankings.size());
				for(int i=0;i<rankings.size();i++){
					if(CommentUtils.TYPE_BOOK.equals(rankings.get(i).getRestype())){
						resourceids.add(rankings.get(i).getResid());
					}else if(CommentUtils.TYPE_NETBOOK.equals(rankings.get(i).getRestype())){
						netbkresourceids.add(rankings.get(i).getResid());
					}
				}
				List<BkInfo> bkInfos = null;
				if(resourceids.size()>0){
					long arryresourceids[] = new long[resourceids.size()];
					for(int i=0;i<resourceids.size();i++){
						arryresourceids[i]=resourceids.get(i);
					}
					bkInfos = bkFacade.findBkInfosByIds(arryresourceids);
				}
				List<NetBook> netBooks = null;
				if(netbkresourceids.size()>0){
					long arryresourceids[] = new long[netbkresourceids.size()];
					for(int i=0;i<netbkresourceids.size();i++){
						arryresourceids[i]=netbkresourceids.get(i);
					}
					netBooks = netBookFacade.findNetBkInfosByIds(arryresourceids);
				}
				
				if((bkInfos!=null && bkInfos.size()>0) || (netBooks!=null && netBooks.size()>0)){
					for(int i=0;i<rankings.size();i++){
						Ranking ranking = rankings.get(i);
						if(CommentUtils.TYPE_BOOK.equals(ranking.getRestype()) && bkInfos!=null){
							for(int j=0;j<bkInfos.size();j++){
								BkInfo bkInfo = bkInfos.get(j);
								if(bkInfo.getId()==ranking.getResid()){
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									bookInfos.add(bookInfo);
									bkInfos.remove(j);
									break;
								}
							}
						}else if(CommentUtils.TYPE_NETBOOK.equals(ranking.getRestype()) && netBooks!=null){
							for(int j=0;j<netBooks.size();j++){
								NetBook bkInfo = netBooks.get(j);
								if(bkInfo.getId()==ranking.getResid()){
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									bookInfos.add(bookInfo);
									netBooks.remove(j);
									break;
								}
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", bookInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	//最新图书推荐榜
	public String latestBooksRanking(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String ranktype = Ranking.type_latestbookrec;//最新图书推荐榜排行
		String restype = null;
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Ranking> rankings = null;
		try{
			rankings = rankingFacade.findRanking(start, pagesize, ranktype,restype);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<BookInfo> bookInfos = new ArrayList<BookInfo>();
		if(rankings!=null && rankings.size()==1 && rankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(rankings!=null){
				size = rankings.size();
			}
			if(rankings!=null && rankings.size()>0){
				//需要区分图书和网络小说
				List<Long> resourceids = new ArrayList<Long>(rankings.size());
				//long resourceids[] = new long[rankings.size()];
				List<Long> netbkresourceids = new ArrayList<Long>(rankings.size());
				for(int i=0;i<rankings.size();i++){
					if(CommentUtils.TYPE_BOOK.equals(rankings.get(i).getRestype())){
						resourceids.add(rankings.get(i).getResid());
					}else if(CommentUtils.TYPE_NETBOOK.equals(rankings.get(i).getRestype())){
						netbkresourceids.add(rankings.get(i).getResid());
					}
				}
				List<BkInfo> bkInfos = null;
				if(resourceids.size()>0){
					long arryresourceids[] = new long[resourceids.size()];
					for(int i=0;i<resourceids.size();i++){
						arryresourceids[i]=resourceids.get(i);
					}
					bkInfos = bkFacade.findBkInfosByIds(arryresourceids);
				}
				List<NetBook> netBooks = null;
				if(netbkresourceids.size()>0){
					long arryresourceids[] = new long[netbkresourceids.size()];
					for(int i=0;i<netbkresourceids.size();i++){
						arryresourceids[i]=netbkresourceids.get(i);
					}
					netBooks = netBookFacade.findNetBkInfosByIds(arryresourceids);
				}
				
				if((bkInfos!=null && bkInfos.size()>0) || (netBooks!=null && netBooks.size()>0)){
					for(int i=0;i<rankings.size();i++){
						Ranking ranking = rankings.get(i);
						if(CommentUtils.TYPE_BOOK.equals(ranking.getRestype()) && bkInfos!=null){
							for(int j=0;j<bkInfos.size();j++){
								BkInfo bkInfo = bkInfos.get(j);
								if(bkInfo.getId()==ranking.getResid()){
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									bookInfos.add(bookInfo);
									bkInfos.remove(j);
									break;
								}
							}
						}else if(CommentUtils.TYPE_NETBOOK.equals(ranking.getRestype()) && netBooks!=null){
							for(int j=0;j<netBooks.size();j++){
								NetBook bkInfo = netBooks.get(j);
								if(bkInfo.getId()==ranking.getResid()){
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									bookInfos.add(bookInfo);
									netBooks.remove(j);
									break;
								}
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", bookInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	
	
	//每周热门书单榜
	public String weekHotBooklist(String reqs,Long uid){
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String ranktype = Ranking.type_weekmovielist;//每周热门影单榜
		String restype = CommentUtils.TYPE_MOVIELIST;//影单类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =20;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<Ranking> rankings = null;
		try{
			rankings = rankingFacade.findRanking(start, pagesize, ranktype,restype);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<BookListInfo> bookListInfos = new ArrayList<BookListInfo>(); 
		if(rankings!=null && rankings.size()==1 && rankings.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(rankings!=null){
				size = rankings.size();
			}
			if(rankings!=null && rankings.size()>0){
				List<Long> resourceids = new ArrayList<Long>(rankings.size());
				for(int i=0;i<rankings.size();i++){
					resourceids.add(rankings.get(i).getResid());
				}
				List<BookList> bookLists = null;
				if(resourceids.size()>0){
					bookLists = getResourceInfoFacade.findBookListsByIds(resourceids);
				}
				if(bookLists!=null && bookLists.size()>0){
					List<BookList> orderbookLists = new ArrayList<BookList>();
					for(int i=0;i<rankings.size();i++){
						Ranking ranking = rankings.get(i);
						for(int j=0;j<bookLists.size();j++){
							BookList bookList = bookLists.get(j);
							if(bookList.getId()==ranking.getResid()){
								orderbookLists.add(bookList);
								bookLists.remove(j);
								break;
							}
						}
					}
					bookListInfos = getResponseBookList(bookLists, uid, bookListInfos);
					//getResponseList(movieLists,uid, movieListInfos);
					if(bookListInfos == null){
						bookListInfos = new ArrayList<BookListInfo>();
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", bookListInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	
	//根据阅读数查询影视排行
	public String movieRankingByReadNum(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String restype = CommentUtils.TYPE_MOVIE;//电影资源类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =10;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<ResStatistic> resStatistics = null;
		try{
			resStatistics = resStatisticService.findResStatisticRankByReadNum(restype,null, start, pagesize);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		if(resStatistics!=null && resStatistics.size()==1 && resStatistics.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(resStatistics!=null){
				size = resStatistics.size();
			}
			if(resStatistics!=null && resStatistics.size()>0){
				long resourceids[] = new long[resStatistics.size()];
				List<Long> mvids = new ArrayList<Long>(resStatistics.size());
				for(int i=0;i<resStatistics.size();i++){
					resourceids[i]=resStatistics.get(i).getResId();
					mvids.add(resStatistics.get(i).getResId());
				}
				List<MvInfo> mvInfos = mvFacade.findMvInfosByIds(resourceids);
				List<MvAvgMark> mvAvgMarks = mvCommentFacade.findMvAvgMarkByMvIds(mvids);
				if(mvInfos!=null && mvInfos.size()>0){
					for(int i=0;i<resStatistics.size();i++){
						for(int j=0;j<mvInfos.size();j++){
							if(mvInfos.get(j).getId()==resStatistics.get(i).getResId()){
								MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfos.get(j), FALSE);
								
								if(mvAvgMarks!=null && mvAvgMarks.size()>0 && mvAvgMarks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
									for(int z=0;z<mvAvgMarks.size();z++){
										if(mvAvgMarks.get(z).getMvId()==movieInfo.getId()){
											movieInfo.setScore(mvAvgMarks.get(z).getMvAvgMark()+"");
											movieInfo.setTalentScore(mvAvgMarks.get(z).getExpertsAvgMark()+"");
										}
									}
								}
								
								movieInfos.add(movieInfo);
								mvInfos.remove(j);
								break;
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", movieInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	
	//根据阅读数查询图书排行
	public String bookRankingByReadNum(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String restype = CommentUtils.TYPE_BOOK;//图书类型
		String secondtype = CommentUtils.TYPE_NETBOOK;//网络小说类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =10;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<ResStatistic> resStatistics = null;
		try{
			resStatistics = resStatisticService.findResStatisticRankByReadNum(restype,secondtype, start, pagesize);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<BookInfo> bookInfos = new ArrayList<BookInfo>();
		if(resStatistics!=null && resStatistics.size()==1 && resStatistics.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(resStatistics!=null){
				size = resStatistics.size();
			}
			if(resStatistics!=null && resStatistics.size()>0){
				//需要区分图书和网络小说
				List<Long> resourceids = new ArrayList<Long>(resStatistics.size());
				//long resourceids[] = new long[rankings.size()];
				List<Long> netbkresourceids = new ArrayList<Long>(resStatistics.size());
				for(int i=0;i<resStatistics.size();i++){
					if(CommentUtils.TYPE_BOOK.equals(resStatistics.get(i).getType())){
						resourceids.add(resStatistics.get(i).getResId());
					}else if(CommentUtils.TYPE_NETBOOK.equals(resStatistics.get(i).getType())){
						netbkresourceids.add(resStatistics.get(i).getResId());
					}
				}
				List<BkInfo> bkInfos = null;
				if(resourceids.size()>0){
					long arryresourceids[] = new long[resourceids.size()];
					for(int i=0;i<resourceids.size();i++){
						arryresourceids[i]=resourceids.get(i);
					}
					bkInfos = bkFacade.findBkInfosByIds(arryresourceids);
				}
				List<NetBook> netBooks = null;
				if(netbkresourceids.size()>0){
					long arryresourceids[] = new long[netbkresourceids.size()];
					for(int i=0;i<netbkresourceids.size();i++){
						arryresourceids[i]=netbkresourceids.get(i);
					}
					netBooks = netBookFacade.findNetBkInfosByIds(arryresourceids);
				}
				
				if((bkInfos!=null && bkInfos.size()>0) || (netBooks!=null && netBooks.size()>0)){
					for(int i=0;i<resStatistics.size();i++){
						ResStatistic resStatistic = resStatistics.get(i);
						if(CommentUtils.TYPE_BOOK.equals(resStatistic.getType()) && bkInfos!=null){
							for(int j=0;j<bkInfos.size();j++){
								BkInfo bkInfo = bkInfos.get(j);
								if(bkInfo.getId()==resStatistic.getResId()){
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									bookInfos.add(bookInfo);
									bkInfos.remove(j);
									break;
								}
							}
						}else if(CommentUtils.TYPE_NETBOOK.equals(resStatistic.getType()) && netBooks!=null){
							for(int j=0;j<netBooks.size();j++){
								NetBook bkInfo = netBooks.get(j);
								if(bkInfo.getId()==resStatistic.getResId()){
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									bookInfos.add(bookInfo);
									netBooks.remove(j);
									break;
								}
							}
						}
					}
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("size", size);
			datas.put("page", page);
			datas.put("pagesize", pagesize);
			datas.put("list", bookInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	//毒药排行榜首页面（包含 根据阅读数的电影榜、根据阅读数的图书榜、毒药资源榜的接口）
	public String duYaoRanking(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		String brestype = CommentUtils.TYPE_BOOK;//图书类型
		String bsecondtype = CommentUtils.TYPE_NETBOOK;//网络小说类型
		String mrestype = CommentUtils.TYPE_MOVIE;//电影类型
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =3;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<ResStatistic> bresStatistics = null;
		try{
			bresStatistics = resStatisticService.findResStatisticRankByReadNum(brestype,bsecondtype, start, pagesize);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		//int size = 0;//当前返回数据集合的长度
		//int flagint;
		List<BookInfo> bookInfos = new ArrayList<BookInfo>();
		if(bresStatistics!=null && bresStatistics.size()==1 && bresStatistics.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//flagint = ResultUtils.QUERY_ERROR;
		}else{
			//flagint = ResultUtils.SUCCESS;
			//if(bresStatistics!=null){
				//size = bresStatistics.size();
			//}
			if(bresStatistics!=null && bresStatistics.size()>0){
				//需要区分图书和网络小说
				List<Long> resourceids = new ArrayList<Long>(bresStatistics.size());
				//long resourceids[] = new long[rankings.size()];
				List<Long> netbkresourceids = new ArrayList<Long>(bresStatistics.size());
				for(int i=0;i<bresStatistics.size();i++){
					if(CommentUtils.TYPE_BOOK.equals(bresStatistics.get(i).getType())){
						resourceids.add(bresStatistics.get(i).getResId());
					}else if(CommentUtils.TYPE_NETBOOK.equals(bresStatistics.get(i).getType())){
						netbkresourceids.add(bresStatistics.get(i).getResId());
					}
				}
				List<BkInfo> bkInfos = null;
				if(resourceids.size()>0){
					long arryresourceids[] = new long[resourceids.size()];
					for(int i=0;i<resourceids.size();i++){
						arryresourceids[i]=resourceids.get(i);
					}
					bkInfos = bkFacade.findBkInfosByIds(arryresourceids);
				}
				List<NetBook> netBooks = null;
				if(netbkresourceids.size()>0){
					long arryresourceids[] = new long[netbkresourceids.size()];
					for(int i=0;i<netbkresourceids.size();i++){
						arryresourceids[i]=netbkresourceids.get(i);
					}
					netBooks = netBookFacade.findNetBkInfosByIds(arryresourceids);
				}
				
				if((bkInfos!=null && bkInfos.size()>0) || (netBooks!=null && netBooks.size()>0)){
					for(int i=0;i<bresStatistics.size();i++){
						ResStatistic resStatistic = bresStatistics.get(i);
						if(CommentUtils.TYPE_BOOK.equals(resStatistic.getType()) && bkInfos!=null){
							for(int j=0;j<bkInfos.size();j++){
								BkInfo bkInfo = bkInfos.get(j);
								if(bkInfo.getId()==resStatistic.getResId()){
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									bookInfos.add(bookInfo);
									bkInfos.remove(j);
									break;
								}
							}
						}else if(CommentUtils.TYPE_NETBOOK.equals(resStatistic.getType()) && netBooks!=null){
							for(int j=0;j<netBooks.size();j++){
								NetBook bkInfo = netBooks.get(j);
								if(bkInfo.getId()==resStatistic.getResId()){
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									bookInfos.add(bookInfo);
									netBooks.remove(j);
									break;
								}
							}
						}
					}
				}
			}
		}
		
		List<ResStatistic> mresStatistics = null;
		try{
			mresStatistics = resStatisticService.findResStatisticRankByReadNum(mrestype,null, start, pagesize);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		//int size = 0;//当前返回数据集合的长度
		//int flagint;
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		if(mresStatistics!=null && mresStatistics.size()==1 && mresStatistics.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//flagint = ResultUtils.QUERY_ERROR;
		}else{
			//flagint = ResultUtils.SUCCESS;
			//if(mresStatistics!=null){
				//size = mresStatistics.size();
			//}
			if(mresStatistics!=null && mresStatistics.size()>0){
				long resourceids[] = new long[mresStatistics.size()];
				for(int i=0;i<mresStatistics.size();i++){
					resourceids[i]=mresStatistics.get(i).getResId();
				}
				List<MvInfo> mvInfos = mvFacade.findMvInfosByIds(resourceids);
				if(mvInfos!=null && mvInfos.size()>0){
					for(int i=0;i<mresStatistics.size();i++){
						for(int j=0;j<mvInfos.size();j++){
							if(mvInfos.get(j).getId()==mresStatistics.get(i).getResId()){
								MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfos.get(j), FALSE);
								movieInfos.add(movieInfo);
								mvInfos.remove(j);
								break;
							}
						}
					}
				}
			}
		}
		
		
		String restype = "191";
		String type = null;
		int pageSize = 3;
		int pageStart = 0;
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		try {
			List<RankingList> rankingLists = rankingListFacade.findRankingListByTopshow(restype, type,pageStart,pageSize);
			for (RankingList ranking : rankingLists) {
				ResourceInfo resourceInfo = null;
				long resId = ranking.getResid();
				String typeProxy = ranking.getType();
				long uidProxy = ranking.getUserid();
				try {
					resourceInfo = resourceManager.getResourceByIdAndType(resId, typeProxy, uidProxy);
					if (resourceInfo.getFlag() == ResultUtils.SUCCESS) {
						resourceInfos.add(resourceInfo);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		
		
		int flagint = ResultUtils.SUCCESS;
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("size", size);
			//datas.put("page", page);
			//datas.put("pagesize", pagesize);
			datas.put("bookInfos", bookInfos);
			datas.put("movieInfos", movieInfos);
			datas.put("resourceInfos", resourceInfos);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		resString = getResponseData(datas);
		return resString;
	}
	
	
	//根据阅读数查询资源排行榜
	public String resourceRankingByReadNum(String reqs,long userId){
		//	LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		long page = 1;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String pageStr = dataq.get("page")+"";
			if(StringUtils.isInteger(pageStr)){
				page = Long.parseLong(pageStr);
			}
			if(page<=0){
				page = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//return RES_DATA_NOTGET;
		}
		String resString = null;
		datas = new HashMap<String, Object>();
		//根据topicid查询关联资源列表
		final int pagesize =10;//每页显示数量
		long start = PageUtils.getRecordStart(pagesize, page);//计算出开始索引
		
		List<ResStatistic> resStatistics = null;
		try{
			resStatistics = resStatisticService.findAllResStatisticRankByReadNum(start, pagesize);
		}catch(Exception e){
			LOG.error(e.getMessage(),e.fillInStackTrace());
		}
		int size = 0;//当前返回数据集合的长度
		int flagint;
		List<String> list = null;
		if(resStatistics!=null && resStatistics.size()==1 && resStatistics.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			flagint = ResultUtils.QUERY_ERROR;
		}else{
			flagint = ResultUtils.SUCCESS;
			if(resStatistics!=null){
				size = resStatistics.size();
			}
			//List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>(size);
			List<Long> resourceids = new ArrayList<Long>(size);
			list = new ArrayList<String>(size);
			if(resStatistics!=null && resStatistics.size()>0){
				for(int i=0;i<resStatistics.size();i++){
					long resid = resStatistics.get(i).getResId();
					String restype = resStatistics.get(i).getType();
					String resourcestr = null;
					long readnum = resStatistics.get(i).getReadNumber()+resStatistics.get(i).getReadRandomNumber();
					if(resid>0){
						try{
							resourcestr = resourceJedisManager.getOneResourceWithType(resid,restype);
							//增加额外信息
							resourcestr = addResourcestrInfo(resourcestr,resid, restype,readnum);
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
							e.printStackTrace();
						}
						if(resourcestr!=null){
							list.add(resourcestr);
							resourceids.add(resid);
						}else{
							ResourceInfo resourceInfo = null;
							try{
								resourceInfo = resourceManager.getResourceByIdAndType(resid, restype, userId);
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							if(resourceInfo!=null && resourceInfo.getRid()>0){
								//infos.add(resourceInfo);
								try{
									setResourceInfoForRedis(resourceInfo);
									resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}
								try {
									//需要判断是否是书单影单，书单影单需要带最多30个电影或者书
									setBkMvList(resourceInfo);
									resourceInfo.setReadingCount(readnum+"");
									resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
									//resourcestr = addResourcestrInfo(resourcestr,resid, restype,readnum);
									list.add(resourcestr);
									resourceids.add(resid);
								} catch (JsonGenerationException e) {
									e.printStackTrace();
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				try{
					list = handleJsonValue(userId, list, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
			}
		}
		
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			resString = RES_DATA_RIGHT_BEGIN+"\"size\":"+size+",\"page\":"+page+",\"pagesize\":"+pagesize+",\"list\":"+list.toString()+RES_DATA_RIGHT_END;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
			datas.put("flag", flag);
			resString = getResponseData(datas);
		}
		return resString;
	}
	
	/**
	 * 查询书单或者影单中的电影放入书单影单返回
	 * @param resourceInfo
	 */
	private void setBkMvList(ResourceInfo resourceInfo){
		try{
			if(resourceInfo!=null && resourceInfo.getRid()>0 && CommentUtils.TYPE_MOVIELIST.equals(resourceInfo.getType())){
				List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(resourceInfo.getRid(),null, 30);
				if(mvListLinks!=null && mvListLinks.size()>0){
					long ids[] = new long[mvListLinks.size()];
					for(int j=0;j<mvListLinks.size();j++){
						ids[j]=mvListLinks.get(j).getMovieId();
					}
					List<MvInfo> mvinfos = mvFacade.findMvInfosByIds(ids);
					if(mvinfos!=null && mvinfos.size()>0){
						List<MovieInfo> mvInfos = new ArrayList<MovieInfo>(mvListLinks.size());
						for(int j=0;j<mvinfos.size();j++){
							MvInfo mvInfo = mvinfos.get(j);
							if(mvInfo!=null && mvInfo.getId()>0){
								MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, TRUE);
								mvInfos.add(movieInfo);
							}
						}
						resourceInfo.setMovieInfos(mvInfos);
					}
				}
			}else if(resourceInfo!=null && resourceInfo.getRid()>0 && CommentUtils.TYPE_BOOKLIST.equals(resourceInfo.getType())){
				List<BookListLink> bkListLinks = getResourceInfoFacade.findBookListInfo(resourceInfo.getRid(),null, 30);
				if(bkListLinks!=null && bkListLinks.size()>0){
					long ids[] = new long[bkListLinks.size()];
					for(int j=0;j<bkListLinks.size();j++){
						ids[j]=bkListLinks.get(j).getBookId();
					}
					List<BkInfo> bkInfos = bkFacade.findBkInfosByIds(ids);
					if(bkInfos!=null && bkInfos.size()>0){
						List<BookInfo> bookInfos = new ArrayList<BookInfo>(bkListLinks.size());
						for(int j=0;j<bkInfos.size();j++){
							BkInfo bkInfo = bkInfos.get(j);
							if(bkInfo!=null && bkInfo.getId()>0){
								BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, 0);
								bookInfos.add(bookInfo);
							}
						}
						resourceInfo.setBookInfos(bookInfos);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//为资源字符窜处理一些必要信息
	public String addResourcestrInfo(String resourcestr,long resid,String type,long readCount){
		try{
			//if(!CommentUtils.TYPE_NEWARTICLE.equals(type)){
				//增加阅读量
				//int readCount = resStatJedisManager.getReadNum(resid, type);
				if(0!=readCount){
					resourcestr = resourcestr.replaceAll("\"readingCount\": ?\"[0-9]+\"", "\"readingCount\": \""+readCount+"\"");
					resourcestr = resourcestr.replaceAll("\"readingCount\": ?[0-9]+", "\"readingCount\": \""+readCount+"\"");
					if(resourcestr.indexOf("\"readingCount\":")<0){
						//不存在这个字段
						if(resourcestr.indexOf("\"type\":\"")>-1){
							resourcestr = resourcestr.replaceFirst("\"type\":\"","\"readingCount\": \""+readCount+"\",\"type\":\"");
						}else if(resourcestr.indexOf("\"type\": \"")>-1){
							resourcestr = resourcestr.replaceFirst("\"type\": \"","\"readingCount\": \""+readCount+"\",\"type\":\"");
						}
					}
				}
			//}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resourcestr;
	}
	
	//资源放入缓存前的赋值操作
	public void setResourceInfoForRedis(ResourceInfo resourceInfo){
		try{
			if(resourceInfo!=null){
				if(CommentUtils.TYPE_NEWARTICLE.equals(resourceInfo.getType())){
					String summary = HtmlUtil.getTextFromHtml(resourceInfo.getContUrl());
					if(summary!=null){
						summary = summary.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
					}
					if(summary!=null && summary.length()>50){
						summary = summary.substring(0,50);
					}
					if(summary!=null && summary.length()>0){
						resourceInfo.setSummary(summary);
					}
					resourceInfo.setContUrl("");
				}
				if(!CommentUtils.TYPE_NEWARTICLE.equals(resourceInfo.getType())){
					//增加阅读量
					int readCount = resStatJedisManager.getReadNum(resourceInfo.getRid(), resourceInfo.getType());
					if(0!=readCount){
						resourceInfo.setReadingCount(readCount+"");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public List<String> handleJsonValue(final long userId,List<String> values,List<Long> resourceids){
		if(values==null || values.size()==0){
			return values;
		}
		List<String> resultLists = new LinkedList<String>();
		// pipeline.sync();
		String inList = "";
		String isPraise = "";
		String isCollect = "";
		String zNum = "";
		String cNum = "";
		for(int i=0;i<values.size();i++){
			String value = values.get(i);
			if(null!=value){//查找用户对这个资源的关系,当前只有是否赞过
				try{
					//获取用户信息的id
					String uid = "0";
					try{
						uid = value.substring(value.indexOf("\"userEntity\":{\"id\":")+19, value.indexOf(",\"nickName\""));
					}catch(Exception e){
						e.printStackTrace();
					}
					final Long resourceid = resourceids.get(i);
					if(!uid.equals("0")){//用户id不为0时
						Map<String, String> userInfoMap = userJedisManager.getUserInfo(Long.valueOf(uid));
						if(null!=userInfoMap&&!userInfoMap.isEmpty()&&null!=userInfoMap.get(JedisConstant.USER_HASH_ID)){//当用户的缓存信息不为空时
							//value = value.replaceAll("\"nickName\": ?.*,", "\"isPraise\": "+isPraise+"");
							value = value.substring(0,value.indexOf("nickName\":\"")+11)+userInfoMap.get(JedisConstant.USER_HASH_NAME)+"\","+value.substring(value.indexOf("\"face_url\""));
							value = value.replaceAll("\"face_url\":\" ?[0-9,A-z,\\.,/,_,:]+\"", "\"face_url\":\""+userInfoMap.get(JedisConstant.USER_HASH_FACE)+"\"");
							/*System.out.println("返回用户的信息威"+userInfoMap);
							System.out.println("返回的头像为"+userInfoMap.get(JedisConstant.USER_HASH_FACE));
							System.out.println("返回的资源信息为"+value);*/
						}
								//new HashMap<String, String>();
					}
					Map<String,String> map = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
						@Override
						public Map<String,String> work(Jedis jedis) {
							//改动
							String relationKey = userId+JedisConstant.RELATION_TO_USER_AND_RES+resourceid;
							return jedis.hgetAll(relationKey);
						}
					} );
					if(null!=map&&!map.isEmpty()){//当关系不为空时
						/*inList = map.get(JedisConstant.RELATION_IS_INLIST);
						if(null!=inList){
							value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
						}*/
						isPraise = map.get(JedisConstant.RELATION_IS_PRAISE);
						if(null!=isPraise&&!"".equals(isPraise)){
							value = value.replaceAll("\"isPraise\": ?[0-9]+", "\"isPraise\": "+isPraise+"");
						}
						isCollect = map.get(JedisConstant.RELATION_IS_COLLECT);
						if(null!=isCollect&&!"".equals(isCollect)){
							value = value.replaceAll("\"isCollect\": ?[0-9]+", "\"isCollect\": "+inList+"");
						}
					}
					//添加资源的附加信息 比如说评论数，点赞数
					final String rid = value.substring(value.indexOf("\"rid\":\"")+8, value.indexOf(","));
					if(!rid.equals("0")){
						Map<String, String> otherMap = relationToUserandresClient.execute(new JedisWorker<Map<String,String>>(){
							//查询资源的附加信息
							@Override
							public Map<String, String> work(
									Jedis jedis) {
								String otherInfo = JedisConstant.RELATION_TO_RES_OTHERINFO+rid;
								return jedis.hgetAll(otherInfo);
							}
						});
						if(null!=otherMap&&!otherMap.isEmpty()){
							zNum = otherMap.get(JedisConstant.RELATION_PRAISE_NUM);
							if(null!=zNum&&!"".equals(zNum)){
								value = value.replaceAll("\"zNum\": ?[0-9]+", "\"zNum\": "+zNum+"");
							}
							cNum = otherMap.get(JedisConstant.RELATION_COMMENT_NUM);
							if(null!=cNum&&!"".equals(cNum)){
								value = value.replaceAll("\"cNum\": ?[0-9]+", "\"cNum\": "+cNum+"");
							}
						}
					}
					//添加是否已经加入书单
					String type = value.substring(value.indexOf("\"type\":\"")+8, value.indexOf("\"type\":\"")+9);
					if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)){//当为书评的时候 查找用户对这个书的关系
						String bookId = "";
						Pattern bookIdPattern = Pattern.compile("\"bookInfo\":\\{\"id\":([0-9]+)");
						Matcher bookIdMatcher = bookIdPattern.matcher(value);
						if(bookIdMatcher.find()){
							bookId = bookIdMatcher.group(1);
						}
						if(!"".equals(bookId)){//当电影的id不为空时
							final String bkId = bookId;
							inList = relationToUserandresClient.execute(new JedisWorker<String>(){
								@Override
								public String work(Jedis jedis) {
									String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_BOOK+bkId;
											return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
								}});
						}
					}else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)){//当为影评的时候 查找用户对这个电影的关系
						String movieId = "";
						Pattern movieIdPattern = Pattern.compile("\"movieInfo\":\\{\"id\":([0-9]+)");
						Matcher movieIdMatcher = movieIdPattern.matcher(value);
						if(movieIdMatcher.find()){
							movieId = movieIdMatcher.group(1);
						}
						if(!"".equals(movieId)){//当电影的id不为空时
							final String mvId = movieId;
							inList = relationToUserandresClient.execute(new JedisWorker<String>(){
								@Override
								public String work(Jedis jedis) {
									String relationkey = userId +JedisConstant.RELATION_TO_USER_AND_MOVIE+mvId;
											return jedis.hget(relationkey, JedisConstant.RELATION_IS_INLIST);
								}});
						}
					}
//					System.out.println(value);
					if(null!=inList&&!"".equals(inList)){
						value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": "+inList+"");
					}else{
						value = value.replaceAll("\"inList\": ?[0-9]+", "\"inList\": 1");
					}
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
//				System.out.println(value);
				resultLists.add(value);
				/*long end = System.currentTimeMillis();
				System.out.println("缓存中遍历附加信息的耗时："+(end-begin));*/
			}
		}
		
		return resultLists;
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
		int flagint;
		if(reqList==null || reqList.size() == 0){
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
	
	//false搜索量算法
	public long getSearchNum(long resid,long searchNum,String createtime,long lastSearchNum,int bs,long firstsearchnum){
		int b = 100;
		int day = DateUtil.getTodayDay(createtime);
		if(day%2>0){
			//奇数日期
			b = 99;
		}
		
		searchNum = searchNum*b+searchNum/day+day;
		if(resid<10){
			searchNum = searchNum + resid;
		}else{
			searchNum = searchNum + resid%10;
		}
		
		if(bs>0){
			searchNum = searchNum*(bs+1);
		}
		if(firstsearchnum>0){
			searchNum = searchNum + ((firstsearchnum-searchNum)/3);
		}
		if(lastSearchNum>0 && searchNum>=lastSearchNum){
			//如果搜索量大于等于排行中的上一个搜索量，则将现在的搜索量重新赋值
			searchNum = lastSearchNum - resid%100;
		}
		return searchNum;
	}
	//false热度算法
	public int getHotNum(long searchNum,int hotnum){
		double numd = (Math.log(searchNum)/Math.log(2))*100;
		int num = (int)numd + hotnum;
		return num;
	}
	
	
	
	//false搜索量算法
	public long getSearchNum(long resid,long searchNum){
		//50000到120000之间
		int maxnum = 120000;
		if(searchNum>maxnum){
			return searchNum;
		}
		int gw = (int) (resid % 10);//个位数字，通过个位来确定数量值
		int needNum = 50000+10000*gw;
		if(needNum>maxnum){
			needNum=maxnum;
		}
		searchNum = needNum + searchNum%10000 + resid%10000 + resid/10000;
		long days=System.currentTimeMillis()/1000/60/60/24;
		searchNum = searchNum+(days-45*365-200)*(9+resid%10);
		return searchNum;
	}
	
	public List<MovieListInfo> getResponseMovieList(List<MovieList> reqList ,Long uid,  List<MovieListInfo> resList){
		MovieListInfo movieListInfo = null;
		if(reqList.size()>0){
			MovieList object = reqList.get(0);
			if(object.getId() != UNID){
				for (MovieList obj : reqList) {
					
					movieListInfo = movieUtils.putMVListToMovieList(obj, myMovieFacade, mvFacade,ucenterFacade,actFacade,myBkFacade,uid);
					if(movieListInfo.getId() != 0){
						resList.add(movieListInfo);
					}
				}
			}else{
				
			}
		}else if(reqList.size() == 0){
			
		}
		return resList;
	}
	
	public List<BookListInfo> getResponseBookList(List<BookList> reqList ,Long uid, List<BookListInfo> resList){
		BookListInfo bookListInfo = null;
		if(reqList.size()>0){
			BookList object = reqList.get(0);
			if(object.getId() != UNID){
				for (BookList obj : reqList) {
					//新方法
					bookListInfo = fileUtils.putObjToBookListInfo(obj,actFacade,getResourceInfoFacade,bkFacade,ucenterFacade,myBkFacade,netBookFacade,uid);
					if(bookListInfo.getId() != 0){
						resList.add(bookListInfo);
					}
				}
			}else{
			}
			
		}else if(reqList.size() == 0){
		}
		return resList;
	}
	
	public String viewRankingList(String reqs, final Long uid) {
		// LOG.info("客户端json数据："+reqs);
		Map<String, Object> req = null;
		Map<String, Object> dataq = null;
		Map<String, Object> datas = null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		String restype = "";
		String type = null;
		int pageSize = 10;
		int pageStart = 0;
		int pageIndex = 0;
		// 去掉空格
		reqs = reqs.trim();

		// 转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs, new TypeReference<Map<String, Object>>() {
			});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			restype = (String) dataq.get("restype");
//			type = (String) dataq.get("type");
			if (org.apache.commons.lang.StringUtils.isEmpty(restype)) {
				return RES_DATA_NOTGET;
			}
			pageIndex = Integer.valueOf(((String)dataq.get("pageIndex")));
			pageStart = Long.valueOf(PageUtils.getRecordStart(pageSize, pageIndex)).intValue();//计算出开始索引
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		int flagint = 0;
		String resString = "";
		try {
			List<RankingList> rankingLists = rankingListFacade.findRankingListByTopshow(restype, type,pageStart,pageSize);
			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
			for (RankingList ranking : rankingLists) {
				ResourceInfo resourceInfo = null;
				long resId = ranking.getResid();
				String typeProxy = ranking.getType();
				long uidProxy = ranking.getUserid();
				try {
					resourceInfo = resourceManager.getResourceByIdAndType(resId, typeProxy, uidProxy);
					if (resourceInfo.getFlag() == ResultUtils.SUCCESS) {
						resourceInfos.add(resourceInfo);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}
			datas = new HashMap<String, Object>();
			if (flagint == ResultUtils.SUCCESS || flagint == UNID) {
				flag = CommentUtils.RES_FLAG_SUCCESS;
				datas.put("resourceInfos", resourceInfos);
			} else {
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:" + flagint + ",错误信息:" + error);
				datas.put("error", error);
			}
			datas.put("flag", flag);
			// 处理返回数据
			resString = getResponseData(datas);
		} catch (Exception e) {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
			datas.put("error", error);
		}
		return resString;
	}
	
	/**
	 * 获取原创榜内容
	 * @param restype
	 * @param type
	 * @return
	 */
	public List<ResourceInfo> viewPageIndex(String restype, String type, int pageStart,int pageSize) {
		type=null;
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String error = "";
		// 去掉空格
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		// 转化成可读类型
		try {
			if (org.apache.commons.lang.StringUtils.isEmpty(restype)) {
				return resourceInfos;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return resourceInfos;
		}
		int flagint = 0;
		try {
			List<RankingList> rankingLists = rankingListFacade.findRankingListByTopshow(restype, type,pageStart,pageSize);
			for (RankingList ranking : rankingLists) {
				ResourceInfo resourceInfo = null;
				long resId = ranking.getResid();
				String typeProxy = ranking.getType();
				long uidProxy = ranking.getUserid();
				try {
					resourceInfo = resourceManager.getResourceByIdAndType(resId, typeProxy, uidProxy);
					if (resourceInfo.getFlag() == ResultUtils.SUCCESS) {
						resourceInfos.add(resourceInfo);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}
		} catch (Exception e) {
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:" + flagint + ",错误信息:" + error);
		}
		return resourceInfos;
	}
	
	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setResourceJedisManager(ResourceJedisManager resourceJedisManager) {
		this.resourceJedisManager = resourceJedisManager;
	}
	public void setRelationToUserandresClient(
			JedisSimpleClient relationToUserandresClient) {
		this.relationToUserandresClient = relationToUserandresClient;
	}
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	public void setResourceRankingFacade(ResourceRankingFacade resourceRankingFacade) {
		this.resourceRankingFacade = resourceRankingFacade;
	}
	public void setPaycenterFacade(PaycenterFacade paycenterFacade) {
		this.paycenterFacade = paycenterFacade;
	}
	public void setBkSearchFacade(BkSearchFacade bkSearchFacade) {
		this.bkSearchFacade = bkSearchFacade;
	}
	public void setMvSearchFacade(MvSearchFacade mvSearchFacade) {
		this.mvSearchFacade = mvSearchFacade;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}
	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}
	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setRankingFacade(RankingFacade rankingFacade) {
		this.rankingFacade = rankingFacade;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}
	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}
	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}
	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}
	public void setRankingListFacade(RankingListFacade rankingListFacade) {
		this.rankingListFacade = rankingListFacade;
	}
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}
}
