package com.poison.eagle.manager; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HtmlUtil;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.SelectedFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.Selected;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
/**
 * 精选manager
 * @author Administrator
 *
 */
public class SelectedManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(SelectedManager.class);
	
	private static int flagint;
	
	private SelectedFacade  selectedFacade;
	
	private UcenterFacade ucenterFacade;
	
	private ArticleFacade articleFacade;
	
	private ActFacade actFacade;
	
	private MvFacade mvFacade;
	
	private MyMovieFacade myMovieFacade;
	
	private BkFacade bkFacade;
	
	private GetResourceInfoFacade getResourceInfoFacade;
	
	private MovieUtils movieUtils = MovieUtils.getInstance();
	/**
	 * 资源关系
	 */
	private JedisSimpleClient relationToUserandresClient;
	
	/**
	 * 用户的相关缓存
	 */
	private UserJedisManager userJedisManager;
	
	private ResourceManager resourceManager;
	
	private MomentJedisManager momentJedisManager;
	private ResourceJedisManager resourceJedisManager;
	
	private ResStatJedisManager resStatJedisManager;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	
	/**
	 * 查询精选列表的方法
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getSelected(String reqs,final Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		
		Long score = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String scorestr =dataq.get("score")+"";
			if(StringUtils.isInteger(scorestr)){
				score = Long.valueOf(scorestr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		//List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();
		if(score!=null && score == UNID){
			score = null;
		}
		
		//先查出推荐的用户
		int userpagesize = 2;
		List<Selected> userselecteds = null;
		try{
			userselecteds = selectedFacade.findSelectedUserByScore(null, userpagesize);
		}catch(Exception e){
			e.printStackTrace();
		}
		List<String> userstrings = new ArrayList<String>();
		if(userselecteds!=null && userselecteds.size()==1 && userselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//查询推荐用户失败
			
		}else if(userselecteds!=null && userselecteds.size()>0){
			//
			List<Long> userids = new ArrayList<Long>(userselecteds.size());
			for(int i=0;i<userselecteds.size();i++){
				userids.add(userselecteds.get(i).getResid());
			}
			List<UserAllInfo> userAllInfos = null;
			try{
				userAllInfos = ucenterFacade.findUserAllInfoListByUseridList(null, userids);
			}catch(Exception e){
				LOG.error(e.getMessage(),e.fillInStackTrace());
			}
			if(userAllInfos!=null && userAllInfos.size()>0){
				for(int i=0;i<userAllInfos.size();i++){
					//需要判断当前用户与该用户的关系（是否关注了）
					UserEntity userEntity = fileUtils.copyUserInfo(userAllInfos.get(i), TRUE);
					int isInterest = userUtils.getIsInterest(uid,userEntity.getId(), ucenterFacade,userJedisManager);
					userEntity.setIsInterest(isInterest);
					try {
						String userstring = getObjectMapper().writeValueAsString(userEntity);
						userstrings.add(userstring);
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
		
		//从缓存中取出精选数据
		List<String> list =  new ArrayList<String>();
		Long[] newscore=new Long[1];
		newscore[0]=0l;
		//list = momentJedisManager.getSelectedSquareMoment(uid,score,newscore);
		
		if(list != null && list.size()>0){
			String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
			resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
		}else{
			list =  new ArrayList<String>();
			//如果查出来是空，进行查库
			int pagesize = 10;
			List<Selected> selecteds = null;
			try{
				selecteds = selectedFacade.findSelectedByScore(score, pagesize);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(selecteds!=null && selecteds.size()==1 && selecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				flagint=ResultUtils.QUERY_ERROR;
			}else if(selecteds!=null && selecteds.size()>0){
				List<Long> resourceids = new ArrayList<Long>(pagesize);
				for(int i=0;i<selecteds.size();i++){
					Selected selected = selecteds.get(i);
					if(selected.getResid()>0){
						String resourcestr = null;
						/*try{
							resourcestr = resourceJedisManager.getOneResourceWithType(selected.getResid(),selected.getType());
							//书评、影评、文字需要加标题和封面
							resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}*/
						if(resourcestr!=null){
							//System.out.println("=======================缓存中查出的数据:"+resourcestr);
							list.add(resourcestr);
							resourceids.add(selected.getResid());
						}else{
							ResourceInfo resourceInfo = null;
							try{
								resourceInfo = resourceManager.getResourceByIdAndType(selected.getResid(), selected.getType(), uid);
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							if(resourceInfo!=null && resourceInfo.getRid()>0){
								//infos.add(resourceInfo);
								try {
									//书评、影评、文字需要加标题和封面
									replaceResTitle(resourceInfo, selected.getInfo(),selected.getImage());
									resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
									resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
									list.add(resourcestr);
									resourceids.add(selected.getResid());
								} catch (JsonGenerationException e) {
									e.printStackTrace();
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
								try{
									//if(CommentUtils.TYPE_BOOK.equals(resourceInfo.getType()) || CommentUtils.TYPE_MOVIE.equals(resourceInfo.getType()) || CommentUtils.TYPE_NETBOOK.equals(resourceInfo.getType())){
										//是书、影、网络小说，需要直接将信息存入缓存，不需存入缓存列表
										resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
									/*}else{
										int result = resourceManager.setResourceToJedis(resourceInfo, uid, uid,0l);
										//System.out.println("=======================放入缓存的结果:"+result);
									}*/
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}
							}
						}
					}
				}
				try{
					list = handleJsonValue(uid, list, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				if(list.size()>CommentUtils.RESOURCE_PAGE_SIZE){
					list = list.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
				}
				
				//每条信息截取100字
				
				//actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
				
				//资源列表放入缓存中
				setSelectedResourcesToJedis(selecteds, uid);
			}
			
			datas = new HashMap<String, Object>();
			if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
				if(selecteds!=null && selecteds.size()>0){
					newscore[0]=selecteds.get(selecteds.size()-1).getScore();
				}
				String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
				resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
				datas.put("flag", flag);
				//处理返回数据
				resString = getResponseData(datas);
			}
		}
//		System.out.println(resString);
		return resString;
	}
	
	/**
	 * 查询精选列表的方法--新的方法，获取不重复的
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getSelecteds(String reqs,final Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		
		Long score = null;
		List<Map<String,Object>> idList = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String scorestr =dataq.get("score")+"";
			if(StringUtils.isInteger(scorestr)){
				score = Long.valueOf(scorestr);
			}
			idList = (List<Map<String, Object>>) dataq.get("idList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(idList!=null && idList.size()>0){
			//根据客户端提交的资源id集合查询历史记录
			List<String> list =  new ArrayList<String>();
			
			List<Long> resourceids = new ArrayList<Long>(idList.size());
			for(int i=0;i<idList.size();i++){
				Map<String,Object> map = idList.get(i);
				String idStr = map.get("id")+"";
				String type = (String) map.get("type");
				long id = 0;
				if(StringUtils.isInteger(idStr)){
					id = Long.valueOf(idStr);
				}
				if(id>0){
					String resourcestr = null;
					String info = "";
					String image = "";
					/*try{
						resourcestr = resourceJedisManager.getOneResourceWithType(id,type);
						//增加额外信息
						resourcestr = addResourcestrInfo(resourcestr,id, type, info, image);
					}catch(Exception e){
						LOG.error(e.getMessage(),e.fillInStackTrace());
					}*/
					if(resourcestr!=null){
						//System.out.println("=======================缓存中查出的数据:"+resourcestr);
						list.add(resourcestr);
						resourceids.add(id);
					}else{
						ResourceInfo resourceInfo = null;
						try{
							resourceInfo = resourceManager.getResourceByIdAndType(id, type, uid);
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}
						if(resourceInfo!=null && resourceInfo.getRid()>0){
							try{
								setResourceInfoForRedis(resourceInfo);
								resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							//infos.add(resourceInfo);
							try {
								//if(CommentUtils.TYPE_DIARY.equals(type) || CommentUtils.TYPE_BOOK_COMMENT.equals(type) || CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
									Selected selected = selectedFacade.findSelectedByResidAndType(id, type);
									if(selected!=null && selected.getId()>0){
										info = selected.getInfo();
										image = selected.getImage();
										replaceResTitle(resourceInfo, selected.getInfo(),selected.getImage());
									}
								//}
								//需要判断是否是书单影单，书单影单需要带最多30个电影或者书
								setBkMvList(resourceInfo);
								
								resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
								resourcestr = addResourcestrInfo(resourcestr,id, type, info, image);
								list.add(resourcestr);
								resourceids.add(id);
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
				list = handleJsonValue(uid, list, resourceids);//处理 赞、评 等信息
			}catch(Exception e){
				LOG.error(e.getMessage(),e.fillInStackTrace());
			}
			
			datas = new HashMap<String, Object>();
			if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
				String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
				resString = RES_DATA_RIGHT_BEGIN+"\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+RES_DATA_RIGHT_END;
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
				datas.put("flag", flag);
				//处理返回数据
				resString = getResponseData(datas);
			}
		}else{
			//需要查询置顶的
			List<Selected> topselecteds = null;
			try{
				topselecteds = selectedFacade.findSelectedByTopshow();
			}catch(Exception e){
				e.printStackTrace();
			}
			List<String> toplist =  new ArrayList<String>();//存放置顶信息的
			if(topselecteds!=null && topselecteds.size()==1 && topselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				//置顶的信息查询失败
				
			}else if(topselecteds!=null && topselecteds.size()>0){
				List<Long> resourceids = new ArrayList<Long>(topselecteds.size());
				for(int i=0;i<topselecteds.size();i++){
					Selected selected = topselecteds.get(i);
					if(selected.getResid()>0){
						String resourcestr = null;
						/*try{
							resourcestr = resourceJedisManager.getOneResourceWithType(selected.getResid(),selected.getType());
							//书评、影评、文字需要加标题和封面
							resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}*/
						if(resourcestr!=null){
							//System.out.println("=======================缓存中查出的数据:"+resourcestr);
							toplist.add(resourcestr);
							resourceids.add(selected.getResid());
						}else{
							ResourceInfo resourceInfo = null;
							try{
								resourceInfo = resourceManager.getResourceByIdAndType(selected.getResid(), selected.getType(), uid);
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
									//书评、影评、文字需要加标题和封面
									replaceResTitle(resourceInfo, selected.getInfo(),selected.getImage());
									resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
									resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
									toplist.add(resourcestr);
									resourceids.add(selected.getResid());
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
					toplist = handleJsonValue(uid, toplist, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
			}
			
			
			
			
			if(score!=null && score == UNID){
				score = null;
			}
			
			//先查出推荐的用户
			int userpagesize = 2;
			List<Selected> userselecteds = null;
			try{
				userselecteds = selectedFacade.findSelectedUserByScore(null, userpagesize);
			}catch(Exception e){
				e.printStackTrace();
			}
			List<String> userstrings = new ArrayList<String>();
			if(userselecteds!=null && userselecteds.size()==1 && userselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				//查询推荐用户失败
				
			}else if(userselecteds!=null && userselecteds.size()>0){
				//
				List<Long> userids = new ArrayList<Long>(userselecteds.size());
				for(int i=0;i<userselecteds.size();i++){
					userids.add(userselecteds.get(i).getResid());
				}
				List<UserAllInfo> userAllInfos = null;
				try{
					userAllInfos = ucenterFacade.findUserAllInfoListByUseridList(null, userids);
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				if(userAllInfos!=null && userAllInfos.size()>0){
					for(int i=0;i<userAllInfos.size();i++){
						//需要判断当前用户与该用户的关系（是否关注了）
						UserEntity userEntity = fileUtils.copyUserInfo(userAllInfos.get(i), TRUE);
						int isInterest = userUtils.getIsInterest(uid,userEntity.getId(), ucenterFacade,userJedisManager);
						userEntity.setIsInterest(isInterest);
						try {
							String userstring = getObjectMapper().writeValueAsString(userEntity);
							userstrings.add(userstring);
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
			
			//从缓存中取出精选数据
			List<String> list =  new ArrayList<String>();
			Long[] newscore=new Long[1];
			newscore[0]=0l;
			//list = momentJedisManager.getSelectedSquareMoment(uid,score,newscore);
			
			if(list != null && list.size()>0){
				String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
				resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
			}else{
				list =  new ArrayList<String>();
				//如果查出来是空，进行查库
				int pagesize = 20;
				List<Selected> selecteds = null;
				//查询当天的一个精选和之前的九个精选
				//long todaystarttime = DateUtil.getTodayStartTime();//当天开始时间毫秒值
				try{
					//区分游客和正式用户,游客走其他逻辑，游客id为11
					if(uid==11){
						long endtime = System.currentTimeMillis();
						long starttime = endtime - 30*24*60*60*1000L;
						int size = 10;
						selecteds = selectedFacade.findSelectedRandomWithoutTopshow(starttime, endtime, size);
					}else{
						selecteds = this.getUserSelectList(uid);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				if(selecteds==null){
					selecteds = new ArrayList<Selected>(pagesize);
				}
				int newCount = selecteds.size();//查询的资源数量
				
				if(selecteds.size()==0){
					
				}else{
					List<Long> resourceids = new ArrayList<Long>(pagesize);
					for(int i=0;i<selecteds.size();i++){
					//for(int i=0;i<selecteds.size();i++){
						Selected selected = selecteds.get(i);
						if(selected.getResid()>0){
							String resourcestr = null;
							/*try{
								resourcestr = resourceJedisManager.getOneResourceWithType(selected.getResid(),selected.getType());
								//书评、影评、文字需要加标题和封面
								resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}*/
							if(resourcestr!=null){
								//System.out.println("=======================缓存中查出的数据:"+resourcestr);
								list.add(resourcestr);
								resourceids.add(selected.getResid());
							}else{
								ResourceInfo resourceInfo = null;
								try{
									resourceInfo = resourceManager.getResourceByIdAndType(selected.getResid(), selected.getType(), uid);
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
										//书评、影评、文字需要加标题和封面
										replaceResTitle(resourceInfo, selected.getInfo(),selected.getImage());
										resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
										resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
										list.add(resourcestr);
										resourceids.add(selected.getResid());
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
						list = handleJsonValue(uid, list, resourceids);//处理 赞、评 等信息
					}catch(Exception e){
						LOG.error(e.getMessage(),e.fillInStackTrace());
					}
					
					//每条信息截取100字
					
					//actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
					
					//资源列表放入缓存中
					//setSelectedResourcesToJedis(selecteds, uid);
				
				}
				
				datas = new HashMap<String, Object>();
				if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
					if(list!=null && list.size()>0){
						if(selecteds!=null && selecteds.size()>0){
							newscore[0]=selecteds.get(selecteds.size()-1).getScore();
						}
						String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
						resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"newCount\":\""+newCount+"\",\"systemtime\":\""+systemtime+"\",\"toplist\":"+toplist.toString()+",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
					}else{
						//数据为空
						if(selecteds!=null && selecteds.size()>0){
							newscore[0]=selecteds.get(selecteds.size()-1).getScore();
						}
						String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
						String RES_DATA_RIGHT_BEGIN2 = "{\"res\":{\"data\":{\"flag\":\"2\",";
						resString = RES_DATA_RIGHT_BEGIN2+"\"score\":"+newscore[0]+",\"newCount\":\""+newCount+"\",\"systemtime\":\""+systemtime+"\",\"toplist\":"+toplist.toString()+",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
					}
				}else{
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(flagint);
					LOG.error("错误代号:"+flagint+",错误信息:"+error);
					datas.put("error", error);
					datas.put("flag", flag);
					//处理返回数据
					resString = getResponseData(datas);
				}
			}
		}
		return resString;
	}
	
	/**
	 * 查询精选列表的方法
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getSelectedNew(String reqs,final Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		
		Long score = null;
		List<Map<String,Object>> idList = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String scorestr =dataq.get("score")+"";
			if(StringUtils.isInteger(scorestr)){
				score = Long.valueOf(scorestr);
			}
			idList = (List<Map<String, Object>>) dataq.get("idList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(idList!=null && idList.size()>0){
			//不返回历史记录了
			datas = new HashMap<String, Object>();
			String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
			resString = RES_DATA_RIGHT_BEGIN+"\"systemtime\":\""+systemtime+"\",\"list\":[]"+RES_DATA_RIGHT_END;
		}else{
			//需要查询置顶的
			List<Selected> topselecteds = null;
			try{
				topselecteds = selectedFacade.findSelectedByTopshow();
			}catch(Exception e){
				e.printStackTrace();
			}
			List<String> toplist =  new ArrayList<String>();//存放置顶信息的
			if(topselecteds!=null && topselecteds.size()==1 && topselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				//置顶的信息查询失败
				
			}else if(topselecteds!=null && topselecteds.size()>0){
				List<Long> resourceids = new ArrayList<Long>(topselecteds.size());
				for(int i=0;i<topselecteds.size();i++){
					Selected selected = topselecteds.get(i);
					if(selected.getResid()>0){
						String resourcestr = null;
						/*try{
							resourcestr = resourceJedisManager.getOneResourceWithType(selected.getResid(),selected.getType());
							//书评、影评、文字需要加标题和封面
							resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}*/
						if(resourcestr!=null){
							//System.out.println("=======================缓存中查出的数据:"+resourcestr);
							toplist.add(resourcestr);
							resourceids.add(selected.getResid());
						}else{
							ResourceInfo resourceInfo = null;
							try{
								resourceInfo = resourceManager.getResourceByIdAndType(selected.getResid(), selected.getType(), uid);
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							if(resourceInfo!=null && resourceInfo.getRid()>0){
								//infos.add(resourceInfo);
								try {
									//书评、影评、文字需要加标题和封面
									replaceResTitle(resourceInfo, selected.getInfo(),selected.getImage());
									resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
									resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
									toplist.add(resourcestr);
									resourceids.add(selected.getResid());
								} catch (JsonGenerationException e) {
									e.printStackTrace();
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
								try{
									//if(CommentUtils.TYPE_BOOK.equals(resourceInfo.getType()) || CommentUtils.TYPE_MOVIE.equals(resourceInfo.getType()) || CommentUtils.TYPE_NETBOOK.equals(resourceInfo.getType())){
										//是书、影、网络小说，需要直接将信息存入缓存，不需存入缓存列表
										resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
									/*}else{
										int result = resourceManager.setResourceToJedis(resourceInfo, uid, uid,0l);
										//System.out.println("=======================放入缓存的结果:"+result);
									}*/
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}
							}
						}
					}
				}
				try{
					toplist = handleJsonValue(uid, toplist, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
			}
			
			
			
			
			if(score!=null && score == UNID){
				score = null;
			}
			
			//先查出推荐的用户
			int userpagesize = 2;
			List<Selected> userselecteds = null;
			try{
				userselecteds = selectedFacade.findSelectedUserByScore(null, userpagesize);
			}catch(Exception e){
				e.printStackTrace();
			}
			List<String> userstrings = new ArrayList<String>();
			if(userselecteds!=null && userselecteds.size()==1 && userselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				//查询推荐用户失败
				
			}else if(userselecteds!=null && userselecteds.size()>0){
				//
				List<Long> userids = new ArrayList<Long>(userselecteds.size());
				for(int i=0;i<userselecteds.size();i++){
					userids.add(userselecteds.get(i).getResid());
				}
				List<UserAllInfo> userAllInfos = null;
				try{
					userAllInfos = ucenterFacade.findUserAllInfoListByUseridList(null, userids);
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				if(userAllInfos!=null && userAllInfos.size()>0){
					for(int i=0;i<userAllInfos.size();i++){
						//需要判断当前用户与该用户的关系（是否关注了）
						UserEntity userEntity = fileUtils.copyUserInfo(userAllInfos.get(i), TRUE);
						int isInterest = userUtils.getIsInterest(uid,userEntity.getId(), ucenterFacade,userJedisManager);
						userEntity.setIsInterest(isInterest);
						try {
							String userstring = getObjectMapper().writeValueAsString(userEntity);
							userstrings.add(userstring);
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
			
			//从缓存中取出精选数据
			List<String> list =  new ArrayList<String>();
			Long[] newscore=new Long[1];
			newscore[0]=0l;
			//list = momentJedisManager.getSelectedSquareMoment(uid,score,newscore);
			
			if(list != null && list.size()>0){
				String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
				resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
			}else{
				list =  new ArrayList<String>();
				//如果查出来是空，进行查库
				int pagesize = 20;
				int pagesize1 = 1;
				List<Selected> selecteds = null;
				//查询当天的一个精选和之前的九个精选
				long todaystarttime = DateUtil.getTodayStartTime();//当天开始时间毫秒值
				try{
					selecteds = selectedFacade.findSelectedRandomWithoutTopshow(todaystarttime,null,pagesize1);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				if(selecteds!=null && selecteds.size()==1 && selecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
					flagint=ResultUtils.QUERY_ERROR;
				}else{
					if(selecteds==null){
						selecteds = new ArrayList<Selected>(pagesize);
					}
					int pagesize2 = pagesize - selecteds.size();
					List<Selected> selecteds2 = null;
					try{
						selecteds2 = selectedFacade.findSelectedRandomWithoutTopshow(null,todaystarttime,pagesize2);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(selecteds2!=null && selecteds2.size()==1 && selecteds2.get(0).getFlag()==ResultUtils.QUERY_ERROR){
						flagint=ResultUtils.QUERY_ERROR;
					}else{
						if(selecteds2!=null && selecteds2.size()>0){
							selecteds.addAll(selecteds2);
						}
						List<Long> resourceids = new ArrayList<Long>(pagesize);
						for(int i=0;i<selecteds.size();i++){
						//for(int i=0;i<selecteds.size();i++){
							Selected selected = selecteds.get(i);
							if(selected.getResid()>0){
								String resourcestr = null;
								/*try{
									resourcestr = resourceJedisManager.getOneResourceWithType(selected.getResid(),selected.getType());
									//书评、影评、文字需要加标题和封面
									resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}*/
								if(resourcestr!=null){
									//System.out.println("=======================缓存中查出的数据:"+resourcestr);
									list.add(resourcestr);
									resourceids.add(selected.getResid());
								}else{
									ResourceInfo resourceInfo = null;
									try{
										resourceInfo = resourceManager.getResourceByIdAndType(selected.getResid(), selected.getType(), uid);
									}catch(Exception e){
										LOG.error(e.getMessage(),e.fillInStackTrace());
									}
									if(resourceInfo!=null && resourceInfo.getRid()>0){
										//infos.add(resourceInfo);
										try {
											//书评、影评、文字需要加标题和封面
											replaceResTitle(resourceInfo, selected.getInfo(),selected.getImage());
											resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
											resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
											list.add(resourcestr);
											resourceids.add(selected.getResid());
										} catch (JsonGenerationException e) {
											e.printStackTrace();
										} catch (JsonMappingException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										}
										try{
											//if(CommentUtils.TYPE_BOOK.equals(resourceInfo.getType()) || CommentUtils.TYPE_MOVIE.equals(resourceInfo.getType()) || CommentUtils.TYPE_NETBOOK.equals(resourceInfo.getType())){
												//是书、影、网络小说，需要直接将信息存入缓存，不需存入缓存列表
												resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
											/*}else{
												int result = resourceManager.setResourceToJedis(resourceInfo, uid, uid,0l);
												//System.out.println("=======================放入缓存的结果:"+result);
											}*/
										}catch(Exception e){
											LOG.error(e.getMessage(),e.fillInStackTrace());
										}
									}
								}
							}
						}
						try{
							list = handleJsonValue(uid, list, resourceids);//处理 赞、评 等信息
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}
						
						//每条信息截取100字
						
						//actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
						
						//资源列表放入缓存中
						setSelectedResourcesToJedis(selecteds, uid);
					}
				}
				
				datas = new HashMap<String, Object>();
				if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
					if(list!=null && list.size()>0){
						if(selecteds!=null && selecteds.size()>0){
							newscore[0]=selecteds.get(selecteds.size()-1).getScore();
						}
						String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
						resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"toplist\":"+toplist.toString()+",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
					}else{
						//为了客户端界面不显示为空而抛出错误
						flag = CommentUtils.RES_FLAG_ERROR;
						error = "已是最新数据";
						//LOG.error("错误代号:"+flagint+",错误信息:"+error);
						datas.put("error", error);
						datas.put("flag", flag);
						//处理返回数据
						resString = getResponseData(datas);
					}
				}else{
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(flagint);
					LOG.error("错误代号:"+flagint+",错误信息:"+error);
					datas.put("error", error);
					datas.put("flag", flag);
					//处理返回数据
					resString = getResponseData(datas);
				}
			}
		}
		return resString;
	}
	
	/**
	 * 查询精选列表的方法
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getSelectedNew2(String reqs,final Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		
		Long score = null;
		List<Map<String,Object>> idList = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String scorestr =dataq.get("score")+"";
			if(StringUtils.isInteger(scorestr)){
				score = Long.valueOf(scorestr);
			}
			idList = (List<Map<String, Object>>) dataq.get("idList");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(idList!=null && idList.size()>0){
			//从缓存中取出精选数据
			List<String> list =  new ArrayList<String>();
			
			List<Long> resourceids = new ArrayList<Long>(idList.size());
			for(int i=0;i<idList.size();i++){
				Map<String,Object> map = idList.get(i);
				String idStr = map.get("id")+"";
				String type = (String) map.get("type");
				long id = 0;
				if(StringUtils.isInteger(idStr)){
					id = Long.valueOf(idStr);
				}
				if(id>0){
					String resourcestr = null;
					String info = "";
					String image = "";
					//书评、影评、文字需要加标题和封面
					try{
						if(CommentUtils.TYPE_DIARY.equals(type) || CommentUtils.TYPE_BOOK_COMMENT.equals(type) || CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
							Selected selected = selectedFacade.findSelectedByResidAndType(id, type);
							if(selected!=null && selected.getId()>0){
								info = selected.getInfo();
								image = selected.getImage();
							}
						}
					}catch(Exception e){
						LOG.error(e.getMessage(),e.fillInStackTrace());
					}
					try{
						resourcestr = resourceJedisManager.getOneResourceWithType(id,type);
						//书评、影评、文字需要加标题和封面
						resourcestr = addResourcestrInfo(resourcestr,id, type, info, image);
					}catch(Exception e){
						LOG.error(e.getMessage(),e.fillInStackTrace());
					}
					if(resourcestr!=null){
						//System.out.println("=======================缓存中查出的数据:"+resourcestr);
						list.add(resourcestr);
						resourceids.add(id);
					}else{
						ResourceInfo resourceInfo = null;
						try{
							resourceInfo = resourceManager.getResourceByIdAndType(id, type, uid);
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}
						if(resourceInfo!=null && resourceInfo.getRid()>0){
							//infos.add(resourceInfo);
							try {
								//书评、影评、文字需要加标题和封面
								replaceResTitle(resourceInfo, info,null);
								resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
								resourcestr = addResourcestrInfo(resourcestr,id, type, info, image);
								list.add(resourcestr);
								resourceids.add(id);
							} catch (JsonGenerationException e) {
								e.printStackTrace();
							} catch (JsonMappingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							try{
								//if(CommentUtils.TYPE_BOOK.equals(resourceInfo.getType()) || CommentUtils.TYPE_MOVIE.equals(resourceInfo.getType()) || CommentUtils.TYPE_NETBOOK.equals(resourceInfo.getType())){
									//是书、影、网络小说，需要直接将信息存入缓存，不需存入缓存列表
									resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
								/*}else{
									int result = resourceManager.setResourceToJedis(resourceInfo, uid, uid,0l);
									//System.out.println("=======================放入缓存的结果:"+result);
								}*/
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
						}
					}
				}
			}
			try{
				list = handleJsonValue(uid, list, resourceids);//处理 赞、评 等信息
			}catch(Exception e){
				LOG.error(e.getMessage(),e.fillInStackTrace());
			}
			if(list.size()>CommentUtils.RESOURCE_PAGE_SIZE){
				list = list.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
			}
			
			datas = new HashMap<String, Object>();
			if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
				String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
				resString = RES_DATA_RIGHT_BEGIN+"\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+RES_DATA_RIGHT_END;
			}else{
				flag = CommentUtils.RES_FLAG_ERROR;
				error = MessageUtils.getResultMessage(flagint);
				LOG.error("错误代号:"+flagint+",错误信息:"+error);
				datas.put("error", error);
				datas.put("flag", flag);
				//处理返回数据
				resString = getResponseData(datas);
			}
		}else{
			//需要查询置顶的
			List<Selected> topselecteds = null;
			try{
				topselecteds = selectedFacade.findSelectedByTopshow();
			}catch(Exception e){
				e.printStackTrace();
			}
			List<String> toplist =  new ArrayList<String>();//存放置顶信息的
			if(topselecteds!=null && topselecteds.size()==1 && topselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				//置顶的信息查询失败
				
			}else if(topselecteds!=null && topselecteds.size()>0){
				List<Long> resourceids = new ArrayList<Long>(topselecteds.size());
				for(int i=0;i<topselecteds.size();i++){
					Selected selected = topselecteds.get(i);
					if(selected.getResid()>0){
						String resourcestr = null;
						try{
							resourcestr = resourceJedisManager.getOneResourceWithType(selected.getResid(),selected.getType());
							//书评、影评、文字需要加标题和封面
							resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}
						if(resourcestr!=null){
							//System.out.println("=======================缓存中查出的数据:"+resourcestr);
							toplist.add(resourcestr);
							resourceids.add(selected.getResid());
						}else{
							ResourceInfo resourceInfo = null;
							try{
								resourceInfo = resourceManager.getResourceByIdAndType(selected.getResid(), selected.getType(), uid);
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							if(resourceInfo!=null && resourceInfo.getRid()>0){
								//infos.add(resourceInfo);
								try {
									//书评、影评、文字需要加标题和封面
									replaceResTitle(resourceInfo, selected.getInfo(),null);
									resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
									resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
									toplist.add(resourcestr);
									resourceids.add(selected.getResid());
								} catch (JsonGenerationException e) {
									e.printStackTrace();
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
								try{
									//if(CommentUtils.TYPE_BOOK.equals(resourceInfo.getType()) || CommentUtils.TYPE_MOVIE.equals(resourceInfo.getType()) || CommentUtils.TYPE_NETBOOK.equals(resourceInfo.getType())){
										//是书、影、网络小说，需要直接将信息存入缓存，不需存入缓存列表
										resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
									/*}else{
										int result = resourceManager.setResourceToJedis(resourceInfo, uid, uid,0l);
										//System.out.println("=======================放入缓存的结果:"+result);
									}*/
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}
							}
						}
					}
				}
				try{
					toplist = handleJsonValue(uid, toplist, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
			}
			
			
			
			
			if(score!=null && score == UNID){
				score = null;
			}
			
			//先查出推荐的用户
			int userpagesize = 2;
			List<Selected> userselecteds = null;
			try{
				userselecteds = selectedFacade.findSelectedUserByScore(null, userpagesize);
			}catch(Exception e){
				e.printStackTrace();
			}
			List<String> userstrings = new ArrayList<String>();
			if(userselecteds!=null && userselecteds.size()==1 && userselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				//查询推荐用户失败
				
			}else if(userselecteds!=null && userselecteds.size()>0){
				//
				List<Long> userids = new ArrayList<Long>(userselecteds.size());
				for(int i=0;i<userselecteds.size();i++){
					userids.add(userselecteds.get(i).getResid());
				}
				List<UserAllInfo> userAllInfos = null;
				try{
					userAllInfos = ucenterFacade.findUserAllInfoListByUseridList(null, userids);
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				if(userAllInfos!=null && userAllInfos.size()>0){
					for(int i=0;i<userAllInfos.size();i++){
						//需要判断当前用户与该用户的关系（是否关注了）
						UserEntity userEntity = fileUtils.copyUserInfo(userAllInfos.get(i), TRUE);
						int isInterest = userUtils.getIsInterest(uid,userEntity.getId(), ucenterFacade,userJedisManager);
						userEntity.setIsInterest(isInterest);
						try {
							String userstring = getObjectMapper().writeValueAsString(userEntity);
							userstrings.add(userstring);
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
			
			//从缓存中取出精选数据
			List<String> list =  new ArrayList<String>();
			Long[] newscore=new Long[1];
			newscore[0]=0l;
			//list = momentJedisManager.getSelectedSquareMoment(uid,score,newscore);
			
			if(list != null && list.size()>0){
				String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
				resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
			}else{
				list =  new ArrayList<String>();
				//如果查出来是空，进行查库
				int pagesize = 10;
				List<Selected> selecteds = null;
				int hours = 5;//几点钟是分隔线
				long onedaytime = 24*60*60*1000L;//一天的毫秒值
				long todaystarttime = DateUtil.getTodayStartTime();//当天开始时间毫秒值
				long separatetime = todaystarttime + hours*60*60*1000L;//今天的分隔时间毫秒值
				long nowtime = System.currentTimeMillis();
				long starttime = 0;
				long endtime = 0;
				if(nowtime>=separatetime){
					//查询今天早上5点到明天早上5点之间的精选
					starttime = separatetime;
					endtime = starttime + onedaytime;
				}else{
					//查询昨天早上五点到今天早上5点之间的精选
					endtime = separatetime;
					starttime = endtime - onedaytime;
				}
				try{
					selecteds = selectedFacade.findSelectedByScoreWithoutTopshow(score, pagesize, starttime, endtime);//排除了置顶的
				}catch(Exception e){
					e.printStackTrace();
				}
				
				if(selecteds!=null && selecteds.size()==1 && selecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
					flagint=ResultUtils.QUERY_ERROR;
				}else{
					if(selecteds==null){
						selecteds = new ArrayList<Selected>(pagesize);
					}
					int size = 0;
					size = selecteds.size();
					//去掉循环刷新
					/*if(size==pagesize){
						
					}else{
						//不够pagesize条数，需要从头查询缺少的
						int cha = pagesize - size;
						List<Selected> chaselecteds = null;
						try{
							chaselecteds = selectedFacade.findSelectedByScore(null, cha);
						}catch(Exception e){
							e.printStackTrace();
						}
						if(chaselecteds!=null && chaselecteds.size()==1 && chaselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
							flagint=ResultUtils.QUERY_ERROR;
						}else{
							selecteds.addAll(chaselecteds);
						}
					}*/
					List<Long> resourceids = new ArrayList<Long>(pagesize);
					//改为了正序查询数据库，需要倒序返回给客户端
					for(int i=selecteds.size()-1;i>=0;i--){
					//for(int i=0;i<selecteds.size();i++){
						Selected selected = selecteds.get(i);
						if(selected.getResid()>0){
							String resourcestr = null;
							try{
								resourcestr = resourceJedisManager.getOneResourceWithType(selected.getResid(),selected.getType());
								//书评、影评、文字需要加标题和封面
								resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							if(resourcestr!=null){
								//System.out.println("=======================缓存中查出的数据:"+resourcestr);
								list.add(resourcestr);
								resourceids.add(selected.getResid());
							}else{
								ResourceInfo resourceInfo = null;
								try{
									resourceInfo = resourceManager.getResourceByIdAndType(selected.getResid(), selected.getType(), uid);
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}
								if(resourceInfo!=null && resourceInfo.getRid()>0){
									//infos.add(resourceInfo);
									try {
										//书评、影评、文字需要加标题和封面
										replaceResTitle(resourceInfo, selected.getInfo(),null);
										resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
										resourcestr = addResourcestrInfo(resourcestr,selected.getResid(), selected.getType(), selected.getInfo(), selected.getImage());
										list.add(resourcestr);
										resourceids.add(selected.getResid());
									} catch (JsonGenerationException e) {
										e.printStackTrace();
									} catch (JsonMappingException e) {
										e.printStackTrace();
									} catch (IOException e) {
										e.printStackTrace();
									}
									try{
										//if(CommentUtils.TYPE_BOOK.equals(resourceInfo.getType()) || CommentUtils.TYPE_MOVIE.equals(resourceInfo.getType()) || CommentUtils.TYPE_NETBOOK.equals(resourceInfo.getType())){
											//是书、影、网络小说，需要直接将信息存入缓存，不需存入缓存列表
											resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
										/*}else{
											int result = resourceManager.setResourceToJedis(resourceInfo, uid, uid,0l);
											//System.out.println("=======================放入缓存的结果:"+result);
										}*/
									}catch(Exception e){
										LOG.error(e.getMessage(),e.fillInStackTrace());
									}
								}
							}
						}
					}
					try{
						list = handleJsonValue(uid, list, resourceids);//处理 赞、评 等信息
					}catch(Exception e){
						LOG.error(e.getMessage(),e.fillInStackTrace());
					}
					if(list.size()>CommentUtils.RESOURCE_PAGE_SIZE){
						list = list.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
					}
					
					//每条信息截取100字
					
					//actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
					
					//资源列表放入缓存中
					setSelectedResourcesToJedis(selecteds, uid);
				}
				
				datas = new HashMap<String, Object>();
				if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
					if(list!=null && list.size()>0){
						if(selecteds!=null && selecteds.size()>0){
							newscore[0]=selecteds.get(selecteds.size()-1).getScore();
						}
						String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
						resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"toplist\":"+toplist.toString()+",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
					}else{
						//为了客户端界面不显示为空而抛出错误
						flag = CommentUtils.RES_FLAG_ERROR;
						error = "已是最新数据";
						//LOG.error("错误代号:"+flagint+",错误信息:"+error);
						datas.put("error", error);
						datas.put("flag", flag);
						//处理返回数据
						resString = getResponseData(datas);
					}
				}else{
					flag = CommentUtils.RES_FLAG_ERROR;
					error = MessageUtils.getResultMessage(flagint);
					LOG.error("错误代号:"+flagint+",错误信息:"+error);
					datas.put("error", error);
					datas.put("flag", flag);
					//处理返回数据
					resString = getResponseData(datas);
				}
			}
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
	
	//替换资源类里面的的标题(精选列表中优先显示运营加入的标题和封面)
	public void replaceResTitle(ResourceInfo resourceInfo,String title,String image){
		if(resourceInfo!=null && title!=null && title.length()>0){
			//String type = resourceInfo.getType();
			//if(CommentUtils.TYPE_DIARY.equals(type) || CommentUtils.TYPE_BOOK_COMMENT.equals(type) || CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
			//去掉标题的换行符
			if(title!=null && title.length()>0){
				title = title.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
			}
			resourceInfo.setTitle(title);
			//}
		}
		if(resourceInfo!=null && image!=null && image.length()>0){
			resourceInfo.setImageUrl(image);
		}
	}
	
	//为书评、影评、文字的json字符窜信息添加标题、封面图片的信息
	public String addResourcestrInfo(String resourcestr,long resid,String type,String title,String image){
		try{
			if(CommentUtils.TYPE_DIARY.equals(type) || CommentUtils.TYPE_BOOK_COMMENT.equals(type) || CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
				if(resourcestr!=null && resourcestr.length()>0){
					//去掉标题的换行符
					if(title!=null && title.length()>0){
						title = title.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
					}
					if(title!=null && title.length()>0){
						if(resourcestr.indexOf("\"title\":\"\"")>-1){
							resourcestr = resourcestr.replaceFirst("\"title\":\"\"","\"title\":\""+title+"\"");
						}else if( resourcestr.indexOf("\"title\":\"")<0 && resourcestr.indexOf("\"type\":\"")>-1){
							resourcestr = resourcestr.replaceFirst("\"type\":\"","\"title\":\""+title+"\",\"type\":\"");
						}else{
							//替换原来的的title
							//在类里面替换了
						}
					}
					
					if(resourcestr.indexOf("\"imageUrl\":\"\"")>-1){
						resourcestr = resourcestr.replaceFirst("\"imageUrl\":\"\"","\"imageUrl\":\""+image+"\"");
					}else if(resourcestr.indexOf("\"imageUrl\":\"")<0 && resourcestr.indexOf("\"type\":\"")>-1){
						resourcestr = resourcestr.replaceFirst("\"type\":\"","\"imageUrl\":\""+image+"\",\"type\":\"");
					}
				}
			}
			if(CommentUtils.TYPE_NEWARTICLE.equals(type)){
				if(resourcestr!=null && resourcestr.length()>0){
					if(resourcestr.indexOf("\"summary\":\"\"")>-1){
						//
						Article article = articleFacade.queryArticleById(resid);
						if(article!=null && article.getId()>0){
							String summary = HtmlUtil.getTextFromHtml(article.getContent());
							if(summary!=null){
								summary = summary.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
							}
							if(summary!=null && summary.length()>50){
								summary = summary.substring(0,50);
							}
							if(summary!=null && summary.length()>0){
								resourcestr = resourcestr.replaceFirst("\"summary\":\"\"","\"summary\":\""+summary+"\"");
							}
							article.setContent("");
							article.setSummary(summary);
							if(article.getName()!=null){
								String name = article.getName().replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
								article.setName(name);
							}
							ResourceInfo resourceInfo = fileUtils.putObjectToResource(article, ucenterFacade, actFacade);
							String result = resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
						}
					}else if(resourcestr.indexOf("\"summary\":\"")<0){
						//
						if(resourcestr.indexOf("\"type\":\"")>-1){
							Article article = articleFacade.queryArticleById(resid);
							if(article!=null && article.getId()>0){
								String summary = HtmlUtil.getTextFromHtml(article.getContent());
								if(summary!=null){
									summary = summary.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
								}
								if(summary!=null && summary.length()>50){
									summary = summary.substring(0,50);
								}
								if(summary!=null && summary.length()>0){
									resourcestr = resourcestr.replaceFirst("\"type\":\"","\"summary\":\""+summary+"\",\"type\":\"");
								}
								article.setContent("");
								article.setSummary(summary);
								if(article.getName()!=null){
									String name = article.getName().replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\"", "");
									article.setName(name);
								}
								ResourceInfo resourceInfo = fileUtils.putObjectToResource(article, ucenterFacade, actFacade);
								String result = resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
							}
						}
					}
				}
			}
			if(!CommentUtils.TYPE_NEWARTICLE.equals(type)){
				//增加阅读量
				int readCount = resStatJedisManager.getReadNum(resid, type);
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
			}
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
	
	/**
	 * 查询推荐用户列表的方法
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String getSelectedUsers(String reqs,final Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		//去掉空格
		if(reqs!=null){
			reqs = reqs.trim();
		}
		
		Long score = null;
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String scorestr =dataq.get("score")+"";
			if(StringUtils.isInteger(scorestr)){
				score = Long.valueOf(scorestr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		//List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();
		if(score!=null && score == UNID){
			score = null;
		}
		
		//先查出推荐的用户
		int userpagesize = 10;
		List<Selected> userselecteds = null;
		try{
			userselecteds = selectedFacade.findSelectedUserByScore(score, userpagesize);
		}catch(Exception e){
			e.printStackTrace();
		}
		long newscore = 0;
		
		Map<String,String> usermaps = new HashMap<String,String>();
		
		List<String> userstrings = new ArrayList<String>(userpagesize);
		//
		List<String> list =  new ArrayList<String>(userpagesize);
		List<Long> theuserids = new ArrayList<Long>(userpagesize);
		
		if(userselecteds!=null && userselecteds.size()==1 && userselecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//查询推荐用户失败
			flagint=ResultUtils.QUERY_ERROR;
		}else if(userselecteds!=null && userselecteds.size()>0){
			newscore = userselecteds.get(userselecteds.size()-1).getScore();
			//
			List<Long> userids = new ArrayList<Long>(userselecteds.size());
			for(int i=0;i<userselecteds.size();i++){
				userids.add(userselecteds.get(i).getResid());
			}
			List<UserAllInfo> userAllInfos = null;
			try{
				userAllInfos = ucenterFacade.findUserAllInfoListByUseridList(null, userids);
			}catch(Exception e){
				LOG.error(e.getMessage(),e.fillInStackTrace());
			}
			if(userAllInfos!=null && userAllInfos.size()>0){
				for(int i=0;i<userAllInfos.size();i++){
					//需要判断当前用户与该用户的关系（是否关注了）
					UserEntity userEntity = fileUtils.copyUserInfo(userAllInfos.get(i), TRUE);
					int isInterest = userUtils.getIsInterest(uid,userEntity.getId(), ucenterFacade,userJedisManager);
					userEntity.setIsInterest(isInterest);
					try {
						String userstring = getObjectMapper().writeValueAsString(userEntity);
						usermaps.put(userEntity.getId()+"", userstring);
					} catch (JsonGenerationException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		
			List<Selected> selecteds = null;
			try{
				selecteds = selectedFacade.findSelectedUserResource(userids);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(selecteds!=null && selecteds.size()==1 && selecteds.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				flagint=ResultUtils.QUERY_ERROR;
			}else if(selecteds!=null && selecteds.size()>0){
				List<Long> resourceids = new ArrayList<Long>(selecteds.size());
				for(int i=0;i<selecteds.size();i++){
					Selected selected = selecteds.get(i);
					if(selected.getResid()>0){
						String resourcestr = null;
						try{
							resourcestr = resourceJedisManager.getOneResource(selected.getResid());
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}
						if(resourcestr!=null){
							//System.out.println("=======================缓存中查出的数据:"+resourcestr);
							list.add(resourcestr);
							theuserids.add(selected.getUserid());
							resourceids.add(selected.getResid());
						}else{
							ResourceInfo resourceInfo = null;
							try{
								resourceInfo = resourceManager.getResourceByIdAndType(selected.getResid(), selected.getType(), uid);
							}catch(Exception e){
								LOG.error(e.getMessage(),e.fillInStackTrace());
							}
							if(resourceInfo!=null && resourceInfo.getRid()>0){
								//infos.add(resourceInfo);
								try {
									resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
									list.add(resourcestr);
									theuserids.add(selected.getUserid());
									resourceids.add(selected.getResid());
								} catch (JsonGenerationException e) {
									e.printStackTrace();
								} catch (JsonMappingException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}
								try{
									//int result = resourceManager.setResourceToJedis(resourceInfo, uid, uid,0l);
									resourceJedisManager.saveOneResource(resourceInfo.getRid(), resourceInfo);
									//System.out.println("=======================放入缓存的结果:"+result);
								}catch(Exception e){
									LOG.error(e.getMessage(),e.fillInStackTrace());
								}
							}
						}
					}
				}
				try{
					list = handleJsonValue(uid, list, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				//合并用户信息和资源信息
				Map<String,String> resmaps = new HashMap<String,String>();
				for(int i=0;i<list.size();i++){
					resmaps.put(theuserids.get(i)+"", list.get(i));
				}
				
				for(int i=0;i<userids.size();i++){
					//需要去掉没有资源的用户
					String res = resmaps.get(userids.get(i)+"");
					if(res!=null && res.length()>0){
						//为了旧客户端不崩，暂时去掉没有score字段的资源和用户
						if(res.indexOf("score")>-1){
							String str = "{\"user\":"+usermaps.get(userids.get(i)+"")+",\"res\":"+res+"}";
							userstrings.add(str);
						}
					}
				}
			}
			
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
		}
//		System.out.println(resString);
		return resString;
	}
	
	/**
	 * 将库中查出的资源列表放入缓存中
	 * @param resourceInfos
	 * @param uid
	 */
	public void setSelectedResourcesToJedis(List<Selected> selecteds,Long uid){
		//long begin = System.currentTimeMillis();
		Iterator<Selected> iter = selecteds.iterator();
		while(iter.hasNext()){
			Selected selected = iter.next();
			int flag = ResultUtils.ERROR;
			if(selected != null && selected.getResid() != 0){
				long frid = selected.getResid();
				long score = selected.getScore();
				String restype = selected.getType();
				try {
					momentJedisManager.saveOneSelectedMoment(frid, score,restype);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}
		}
	}
	
	public List<Selected> getUserSelectList(long userId){
		List<Selected> selectedList = new ArrayList<Selected>();

		//查询的间隔时间
		long timeSeparation = System.currentTimeMillis()-1000*60*60*24*31l;
		//查询用户看过的队列，如果没有查询数据库
		List<String> useSelectList = momentJedisManager.getUserSelectedList(userId);
		//缓存中没有的话查询数据库并且重建缓存队列
		if(null==useSelectList||useSelectList.size()==0){
			selectedList = selectedFacade.findSelectedByIdOrderDesc();
			//并且新建缓存逻辑
			long endIndex = selectedList.get(0).getId();
			long startIndex = selectedList.get(selectedList.size()-1).getId();
			List<Long> inputList = new ArrayList<Long>();
			inputList.add(startIndex);
			inputList.add(endIndex);
			String inputStr = inputList.toString();
			momentJedisManager.saveUserSelectedItem(inputStr, userId);
		}else{//如果有缓存的话
			//查询队列中的第一条第二条item
			int tempIndex = 0;
			long length = momentJedisManager.getUserSelectedLength(userId);
			String firstStr = useSelectList.get(tempIndex);
			int bigIndex = Integer.valueOf(firstStr.substring(firstStr.indexOf(",") + 1, firstStr.indexOf("]")).replace(" ", ""));
			//dsd
			List<Selected> firstSelectedList = selectedFacade.findSelectedOrderId(bigIndex);
			int selectedSize = firstSelectedList.size();

			//添加大于的数量
			selectedList.addAll(firstSelectedList);
			if(selectedSize==10){//当数量等于10的时候，加入新的item
				long startIndex = firstSelectedList.get(selectedSize-1).getId();
				long endIndex = firstSelectedList.get(0).getId();
				List<Long> inputList = new ArrayList<Long>();
				inputList.add(startIndex);
				inputList.add(endIndex);
				String inputStr = inputList.toString();
				momentJedisManager.saveUserSelectedItem(inputStr, userId);
			}else if(selectedSize<10){//当数量不等于10的时候，合并item
				//更新第一个item的最大游标
				long firstItemEndIndex = bigIndex;
				if(selectedSize>0){
					firstItemEndIndex = firstSelectedList.get(0).getId();
				}
				while(selectedSize<10){
					List<Selected> secondSelectedList = new ArrayList<Selected>();
					int secondBigIndex = Integer.valueOf(firstStr.substring(firstStr.indexOf("[")+1,firstStr.indexOf(",")).replace(" ", ""));
					int secondSmallIndex = 0;
					String secondStr = "";
					if(length>1){//当缓存的长度大于0时，取出第二个item
						tempIndex++;
						secondStr = useSelectList.get(tempIndex);
						secondSmallIndex = Integer.valueOf(secondStr.substring(secondStr.indexOf(",")+1,secondStr.indexOf("]")).replace(" ", ""));
						//firstItemStartIndex =  Long.valueOf(secondStr.substring(secondStr.indexOf("[")+1,secondStr.indexOf(",")).replace(" ", ""));
					}
					int pageSize = 10 - selectedSize;
					//第二个精选队列，补全十个
					secondSelectedList = selectedFacade.findSelectedByMiddle(secondBigIndex, secondSmallIndex,pageSize,timeSeparation);
					int secondSelectSize = secondSelectedList.size();
					List<Long> tempList = new ArrayList<Long>();
					long	firstItemStartIndex = Integer.valueOf(firstStr.substring(firstStr.indexOf("[")+1,firstStr.indexOf(",")).replace(" ", ""));
					if(secondSmallIndex==0&&secondSelectSize==0){//所有的都刷新完了
						//更新最大游标
						momentJedisManager.delUserSelectedItem(userId, tempIndex, firstStr);
						tempList.add(firstItemStartIndex);
						tempList.add(firstItemEndIndex);
						momentJedisManager.saveUserSelectedItem(tempList.toString(), userId);
						break;
					}
					//添加小于的数量
					selectedList.addAll(secondSelectedList);

					selectedSize = selectedSize+secondSelectedList.size();
					//合并item

					if(secondSelectSize>0){
						firstItemStartIndex = secondSelectedList.get(secondSelectedList.size()-1).getId();
					}
					//判断第二个item的最大id和查询出来的最小id，如果小于 更新第一个item，如果大于则合并
					//更新第一个item的最大值和最小值

					//删除上一个item，更新该item
					momentJedisManager.delUserSelectedItem(userId, tempIndex, firstStr);
					if(secondSmallIndex>=firstItemStartIndex){//如果大于 合并两个item
						momentJedisManager.delUserSelectedItem(userId, tempIndex+1, secondStr);
						if(length>1){
							firstItemStartIndex = Integer.valueOf(secondStr.substring(secondStr.indexOf("[")+1,secondStr.indexOf(",")).replace(" ", ""));
						}
						//初始化游标
						tempIndex=0;
						length--;
					}
					tempList.add(firstItemStartIndex);
					tempList.add(firstItemEndIndex);
					momentJedisManager.saveUserSelectedItem(tempList.toString(), userId);
					
					//更新前一个item的游标
					//firstItemEndIndex = firstItemStartIndex;
					//初始化第一个item
					useSelectList = momentJedisManager.getUserSelectedList(userId);
					firstStr = useSelectList.get(tempIndex);
				}
			}
			
		}
		//System.out.println(firstSelectedList.toString());
		return selectedList;
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
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setMomentJedisManager(MomentJedisManager momentJedisManager) {
		this.momentJedisManager = momentJedisManager;
	}

	public void setResourceJedisManager(ResourceJedisManager resourceJedisManager) {
		this.resourceJedisManager = resourceJedisManager;
	}
	

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	public void setRelationToUserandresClient(
			JedisSimpleClient relationToUserandresClient) {
		this.relationToUserandresClient = relationToUserandresClient;
	}
	public void setSelectedFacade(SelectedFacade selectedFacade) {
		this.selectedFacade = selectedFacade;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}

	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}
}
