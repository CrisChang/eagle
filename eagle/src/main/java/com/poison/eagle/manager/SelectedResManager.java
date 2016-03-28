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
import com.poison.eagle.entity.ResourceInfo;
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
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.SelectedResFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.SelectedRes;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.ucenter.client.UcenterFacade;
/**
 * 精选资源manager
 * @author Administrator
 *
 */
public class SelectedResManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(SelectedResManager.class);
	
	private static int flagint;
	
	private SelectedResFacade  selectedResFacade;
	
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
	public String getSelectedRes(String reqs,final Long uid){
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
		String type = null;//精选类型
		String restype = null;//资源类型
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			String scorestr =dataq.get("score")+"";
			type = (String) dataq.get("type");
			restype = dataq.get("restype")+"";
			if(StringUtils.isInteger(scorestr)){
				score = Long.valueOf(scorestr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		//List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();
		if(score!=null && score == 0){
			score = null;
		}
		//从缓存中取出精选数据
		List<String> list =  new ArrayList<String>(0);
		List<String> toplist = new ArrayList<String>(0);//置顶
		Long[] newscore=new Long[1];
		newscore[0]=0l;
		//list = momentJedisManager.getSelectedSquareMoment(uid,score,newscore);
		
		if(list != null && list.size()>0){
			String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
			//resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+",\"userlist\":"+userstrings.toString()+RES_DATA_RIGHT_END;
		}else{
			int pagesize = 10;
			
			list =  new ArrayList<String>(pagesize);
			//如果查出来是空，进行查库
			List<SelectedRes> allselectedRess = null;//所有的精选资源(包含置顶和普通的)
			List<SelectedRes> selectedRess = null;
			List<SelectedRes> topselectedRess = null;//置顶的（第一页显示）
			try{
				topselectedRess = selectedResFacade.findSelectedResByTopshow(type,restype);
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				selectedRess = selectedResFacade.findSelectedResByScoreWithoutTopshow(type,restype,score, pagesize);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(selectedRess!=null && selectedRess.size()==1 && selectedRess.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				flagint=ResultUtils.QUERY_ERROR;
			}else if((selectedRess!=null && selectedRess.size()>0) || (topselectedRess!=null && topselectedRess.size()>0)){
				int num = 0;//集合初始化分配长度
				int topnum = 0;
				if(selectedRess!=null){
					num = num+selectedRess.size();
				}
				if(topselectedRess!=null){
					num = num+topselectedRess.size();
					topnum = topselectedRess.size();
				}
				allselectedRess = new ArrayList<SelectedRes>(num);
				if(topselectedRess!=null && topselectedRess.size()>0 && topselectedRess.get(0).getId()>0){
					allselectedRess.addAll(topselectedRess);
				}
				if(selectedRess!=null && selectedRess.size()>0){
					allselectedRess.addAll(selectedRess);
				}
				
				List<Long> resourceids = new ArrayList<Long>(num-topnum);
				List<Long> topids = new ArrayList<Long>(topnum);
				for(int i=0;i<allselectedRess.size();i++){
					SelectedRes selectedres = allselectedRess.get(i);
					if(selectedres.getResid()>0){
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
							resourceids.add(selectedres.getResid());
						}else{
							ResourceInfo resourceInfo = null;
							try{
								resourceInfo = resourceManager.getResourceByIdAndType(selectedres.getResid(), selectedres.getRestype(), uid);
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
									//setBkMvList(resourceInfo);
									//书评、影评、文字需要加标题和封面
									resourceInfo.setBtime(selectedres.getCreatetime());
									replaceResTitle(resourceInfo, selectedres.getInfo(),selectedres.getImage());
									resourcestr = getObjectMapper().writeValueAsString(resourceInfo);
									resourcestr = addResourcestrInfo(resourcestr,selectedres.getResid(), selectedres.getRestype(), selectedres.getInfo(), selectedres.getImage());
									
									//区分置顶和普通的
									if(selectedres.getTopshow()>0){
										toplist.add(resourcestr);
										topids.add(selectedres.getResid());
									}else{
										list.add(resourcestr);
										resourceids.add(selectedres.getResid());
									}
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
					toplist = handleJsonValue(uid, toplist, topids);//处理 赞、评 等信息
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				try{
					list = handleJsonValue(uid, list, resourceids);//处理 赞、评 等信息
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				
				//每条信息截取100字
				
				//actUtils.subStringResourceListContent(resoureList, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
			}
			
			datas = new HashMap<String, Object>();
			if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
				if(selectedRess!=null && selectedRess.size()>0){
					newscore[0]=selectedRess.get(selectedRess.size()-1).getScore();
				}
				String systemtime = DateUtil.format(System.currentTimeMillis(), DateUtil.timeformat);
				resString = RES_DATA_RIGHT_BEGIN+"\"score\":"+newscore[0]+",\"systemtime\":\""+systemtime+"\",\"list\":"+list.toString()+",\"toplist\":"+toplist.toString()+RES_DATA_RIGHT_END;
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
	public void setSelectedResFacade(SelectedResFacade selectedResFacade) {
		this.selectedResFacade = selectedResFacade;
	}
}
