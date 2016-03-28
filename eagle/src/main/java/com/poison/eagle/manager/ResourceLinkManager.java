package com.poison.eagle.manager; 

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.ResourceGroupInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.TopicInfo;
import com.poison.eagle.utils.ActUtils;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.msg.client.MsgFacade;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.GraphicFilmFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.ResourceLinkFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.ext.constant.MemcacheResourceLinkConstant;
import com.poison.resource.model.Article;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.Diary;
import com.poison.resource.model.GraphicFilm;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.Post;
import com.poison.resource.model.ResourceLink;
import com.poison.resource.model.Topic;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.ext.utils.BookRelationUtil;
import com.poison.store.ext.utils.MovieRelationUtil;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.store.model.NetBook;
import com.poison.ucenter.client.UcenterFacade;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class ResourceLinkManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(ResourceLinkManager.class);
	
	private static int flagint;
	private GetResourceInfoFacadeImpl getResourceInfoFacade;
	private ActFacade actFacade;
	private UcenterFacade ucenterFacade;
	private DiaryFacade diaryFacade;
	private PostFacade postFacade;
	private ArticleFacade articleFacade;
	private BkFacade bkFacade;
	private NetBookFacade netBookFacade;
	private BkCommentFacade bkCommentFacade;
	private MyBkFacade myBkFacade;
	private SerializeFacade serializeFacade;
	private ResourceLinkFacade resourceLinkFacade;
	//电影相关
	private MvCommentFacade mvCommentFacade;
	private MyMovieFacade myMovieFacade;
	private MvFacade mvFacade;
	private MsgFacade msgFacade;
	
	private TopicFacade topicFacade;
	
	
	private JedisSimpleClient jedisSimpleClient;
	private GraphicFilmFacade graphicFilmFacade;
	
	private ResourceManager resourceManager;
	private MovieListManager movieListManager;
	private BookListManager bookListManager;
	private MvBkManager mvBkManager;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();
	private ActUtils actUtils = ActUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	
	private static final int CASETYPE_HOT_COUNT =12;//精选页，第二排数据默认数量
	private static final int CASETYPE_LIST_COUNT =3;//精选页，第三排数据默认数量
	private static final int CASETYPE_PRODUCT_RANK_COUNT =3;//精选页，毒药排行数据默认数量
	private int pageSize = CommentUtils.BOOK_MOVIE_LIST_SIZE;
	
	//显示书列表
	public static final String[] BOOKS = {
		"200","201","202","203","204","205","206","207","208","209",//文学：名家作品，青春文学，小说，随笔,职场，官场，武侠，悬疑，推理，恐怖
		"240","241","242","243","244","245","246","247","248","249",//文学：历史，军事，传记，情感，科幻，校园，青春，言情，外文，纪实，
		"250","251",//文学：散文，诗歌
		"210","211","212","213",//动漫：日漫，漫画，二次元，绘本
		"220","221","222","223","224","225","226","227","228","229",//经管励志：经济，管理，金融，互联网，励志，心理，成功，鸡汤，投资，电商
		"230","231","232","233","234","235","236","237","238","239",//网络文学：奇幻，穿越，都市，言情，玄幻，武侠，仙侠，游戏，科幻，灵异
		"270","271","272","273","274",//网络文学：历史，军事，同人，竞技，耽美
		"260","261","262","263","264","265","266","267","268","269",//社科：生活，哲学，宗教，艺术，科技，政治，法律，文化，国学，少儿
		CommentUtils.CASETYPE_HOT_BOOK,CommentUtils.CASETYPE_HOT_BOOK_MORE,
		CommentUtils.CASETYPE_BUSINESS_HOT_BOOK,CommentUtils.CASETYPE_BUSINESS_NEW_BOOK,
		CommentUtils.CASETYPE_BUSINESS_COLLECT_BOOK};
	
	//显示电影列表
	public static final String[] MOVIES = {CommentUtils.CASETYPE_HOT_MOVIE,CommentUtils.CASETYPE_HOT_MOVIE_MORE,
		CommentUtils.CASETYPE_UPCOMING_MOVIE,CommentUtils.CASETYPE_BUSINESS_HOT_MOVIE,
		CommentUtils.CASETYPE_BUSINESS_NEW_MOVIE,CommentUtils.CASETYPE_BUSINESS_COLLECT_MOVIE,
		"300","301","302","303","304","305","306","307",//地区：大陆，香港，台湾，韩国，日本，美国，法国，欧洲
		"310","311","312","313","314","315",//人物：安吉丽娜朱莉，诺兰，王家卫，徐克，姜文，郭敬明
		"320","321","322","323","324","325","326","327","328","329","340",//类型：重口味，恐怖，动画，科幻，犯罪，逗B，武侠，悬疑，动作，喜剧，爱情
		"330","331","332","333","334","335","336","337","338","339"//时代：2015,2014,2013,2012,2011,2010,00年代，90年代，80年代，更早
		};
	
	
	//显示书单影单列表
	public static final String[] BOOK_MOVIE_LISTS = {CommentUtils.CASETYPE_MOVIELIST,CommentUtils.CASETYPE_BOOKLIST,
		CommentUtils.CASETYPE_PRODUCT_RANK_BOOK,CommentUtils.CASETYPE_PRODUCT_RANK_BOOK_MORE,
		CommentUtils.CASETYPE_PRODUCT_RANK_MOVIE,CommentUtils.CASETYPE_PRODUCT_RANK_MOVIE_MORE,
		CommentUtils.CASETYPE_BUSINESS_COLLECT_MOVIELIST,CommentUtils.CASETYPE_BUSINESS_HOT_MOVIELIST,
		CommentUtils.CASETYPE_BUSINESS_NEW_MOVIELIST,CommentUtils.CASETYPE_BUSINESS_HOT_BOOKLIST,
		CommentUtils.CASETYPE_BUSINESS_NEW_BOOKLIST,CommentUtils.CASETYPE_BUSINESS_COLLECT_BOOKLIST,
		"143",//影单：网友精选
		"153"//书单：网页精选
		};
	
	//任意资源列表
	public static final String[] RESOURCES = {CommentUtils.CASETYPE_TOP_MOVIE,CommentUtils.CASETYPE_TOP_BOOK,
		CommentUtils.CASETYPE_INDEX,CommentUtils.CASETYPE_BUSINESS_INDEX,
		CommentUtils.CASETYPE_WRITING_CLASS,
		CommentUtils.CASETYPE_MV_BOX_OFFICE,CommentUtils.CASETYPE_MAY_WATCHED_MV,
		CommentUtils.CASETYPE_MAY_WATCHED_BK};
	
	//有关联id的任意资源列表
	public static final String[] RESOURCE_BY_ID = {CommentUtils.CASETYPE_MOVIE_ARTICLE,CommentUtils.CASETYPE_BOOK_ARTICLE,
		"111",//图解电影
		};
	
	
	
	private MemcachedClient operationMemcachedClient;
	/**
	 * 显示接口内容
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String fuckBusiness(String reqs,final Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		String caseType = "";
		Long id = null;
//		Long lastId = null;
		Integer page = 0;
		String sort = "";
		String desc = "";
		String newType="";
		int showtype = 0;//推荐影单的展示类型，0为默认列表，1为首页展示一个或者少量几个影单
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			try{
				String idstr = dataq.get("id")+"";
				if(com.poison.eagle.utils.StringUtils.isInteger(idstr)){
					id = Long.valueOf(idstr);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			caseType = (String) dataq.get("caseType");
			try {
				sort = (String) dataq.get("sort");
//				desc = (String) dataq.get("desc");
			} catch (Exception e) {
				sort = "";
//				desc = "";
			}
			try {
				page = Integer.valueOf(dataq.get("page").toString());
				if(page == null){
					page = 0;
				}
			} catch (Exception e) {
				page = 0;
			}
//			try {
//				lastId = Long.valueOf(dataq.get("lastId").toString());
//				if(lastId == 0){
//					lastId = null;
//				}
//			} catch (Exception e) {
//				lastId = null;
//			}
			try {
				newType = (String) dataq.get("newType");
			} catch (Exception e) {
				newType = "";
			}
			try{
				String showtypeStr = dataq.get("showType")+"";
				if(com.poison.eagle.utils.StringUtils.isInteger(showtypeStr)){
					showtype = Integer.valueOf(showtypeStr);
				}
			}catch(Exception e){
				showtype = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(sort == null){
			sort = "";
		}
//		if(desc == null){
//			desc = "";
//		}
		/*System.out.println("id为"+id);
		System.out.println("caseType为"+caseType);
		System.out.println("sort为"+sort);
		System.out.println("page为"+page);*/
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		List<ResourceGroupInfo> resourceGroupInfos = new ArrayList<ResourceGroupInfo>();
		List<TopicInfo> topicInfos = new ArrayList<TopicInfo>();
		long startTime = System.currentTimeMillis();
		if(caseType.equals("170")||caseType.equals("171")||caseType.equals("172")
				||caseType.equals("142")||caseType.equals("141")||caseType.equals("140")
				||caseType.equals("160")||caseType.equals("161")||caseType.equals("162")
				||caseType.equals("152")||caseType.equals("151")||caseType.equals("150")){
			if (StringUtils.equals(newType, "171")) {
				resourceGroupInfos = mvBkManager.getResourcesFromLink(id, caseType, uid,sort,null,page,newType);
			}else{
				resourceInfos = mvBkManager.getResourcesFromLink(id, caseType, uid,sort,null,page);
//				resourceInfos = getResourcesFromLink(id, caseType, uid,sort,null,page);
			}
		    flagint = ResultUtils.SUCCESS;
		}else if(CommentUtils.CASETYPE_REC_MVLIST.equals(caseType)
				|| CommentUtils.CASETYPE_HOT_MVCOMMENT.equals(caseType)
				|| CommentUtils.CASETYPE_MV_BOX_OFFICE.equals(caseType)
				|| CommentUtils.CASETYPE_GOOD_MVCOMMENT.equals(caseType)
				|| CommentUtils.CASETYPE_SELECTED_BKCOMMENT.equals(caseType)
				|| CommentUtils.CASETYPE_SELECTED_DIGEST.equals(caseType)
				|| CommentUtils.CASETYPE_REC_BKLIST.equals(caseType)
				|| CommentUtils.CASETYPE_MV_SELECTED_ARTICLE.equals(caseType)){
			//推荐影单列表特殊处理（需要区分单个影单和影单列表，并且每个影单需要返回最多30个电影信息）
			resourceInfos = getResourceInfosFromLinkByPage(id, caseType, uid, sort, null, page);
			flagint = ResultUtils.SUCCESS;
		}else if(CommentUtils.CASETYPE_MV_LINK_TOPIC.equals(caseType)){
			//电影关联的话题
			List<ResourceLink> resourceLinks= resourceLinkFacade.findResListByResLinkIdAndType(id, caseType);
			if(resourceLinks!=null && resourceLinks.size()>0){
				List<Long> topicids = new ArrayList<Long>(resourceLinks.size());
				for(int i=0;i<resourceLinks.size();i++){
					topicids.add(resourceLinks.get(i).getResId());
				}
				List<Topic> topics = topicFacade.findTopicsByTopicIds(topicids);
				if(topics!=null && topics.size()>0){
					for(int i=0;i<topics.size();i++){
						TopicInfo topicInfo = TopicInfo.copy(topics.get(i));
						topicInfos.add(topicInfo);
					}
					
				}
			}
		}else {
			resourceInfos = getResourcesFromLink(id, caseType, uid,sort,null,page);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("获取res总共用时"+(endTime-startTime)+"资源页的id为"+id+"资源也得type类型为"+caseType);
		actUtils.subStringResourceListContent(resourceInfos, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
		long endTime1 = System.currentTimeMillis();
		System.out.println("窃取评论用时"+(endTime1-endTime));
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			if(StringUtils.equals(newType,"171")){
				datas.put("list", resourceGroupInfos);
			}else if(CommentUtils.CASETYPE_MV_LINK_TOPIC.equals(caseType)){
				//电影关联的话题
				datas.put("list", topicInfos);
			}else {
				datas.put("list", resourceInfos);
			}
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
//		System.out.println(resString);
		return resString;
	}
	//推荐影单列表
	public List<ResourceInfo> getResourceInfosFromLinkByPage(Long id,String caseType,Long uid,String sort,Long lastId,int page){
		List<ResourceLink> resourceLinks = new ArrayList<ResourceLink>(0);
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>(2);
		try{
			if(page<1){
				page=1;
			}
			int pagesize = 10;
			long start = PageUtils.getRecordStart(pagesize, page);
			if(CommentUtils.CASETYPE_GOOD_MVCOMMENT.equals(caseType)){
				resourceLinks = resourceLinkFacade.findResListByResLinkIdAndType(id, caseType,start,pagesize);
			}else{
				resourceLinks = resourceLinkFacade.findResListByType(caseType,start,pagesize);
			}
			if(resourceLinks!=null && resourceLinks.size()>0){
				//需要区分资源
				if(CommentUtils.CASETYPE_REC_MVLIST.equals(caseType) || CommentUtils.CASETYPE_INDEX_MVLIST.endsWith(caseType)){
					//影单
					for(int i=0;i<resourceLinks.size();i++){
						MovieList movieList = myMovieFacade.findMovieListById(resourceLinks.get(i).getResLinkId());
						//MovieListInfo movieListInfo = movieUtils.putMVListToMovieList(movieList, myMovieFacade, mvFacade, ucenterFacade,actFacade,myBkFacade,uid);
						ResourceInfo ri = fileUtils.putObjectToResource(movieList, ucenterFacade, actFacade);
						if(movieList!=null && movieList.getId()>0){
							List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(movieList.getId(),null, 30);
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
									ri.setMovieInfos(mvInfos);
								}
							}
							resourceInfos.add(ri);
						}
					}
				}else if(CommentUtils.CASETYPE_REC_BKLIST.equals(caseType)){
					//书单推荐
					for(int i=0;i<resourceLinks.size();i++){
						BookList bookList = getResourceInfoFacade.queryByIdBookList(resourceLinks.get(i).getResLinkId());
						//MovieListInfo movieListInfo = movieUtils.putMVListToMovieList(movieList, myMovieFacade, mvFacade, ucenterFacade,actFacade,myBkFacade,uid);
						ResourceInfo ri = fileUtils.putObjectToResource(bookList, ucenterFacade, actFacade);
						if(bookList!=null && bookList.getId()>0){
							List<BookListLink> bkListLinks = getResourceInfoFacade.findBookListInfo(bookList.getId(),null, 30);
							if(bkListLinks!=null && bkListLinks.size()>0){
								long ids[] = new long[bkListLinks.size()];
								long netids[]=new long[bkListLinks.size()];//网络小说的id
								for(int j=0;j<bkListLinks.size();j++){
									if(CommentUtils.TYPE_NETBOOK.equals(bkListLinks.get(j).getResType())){
										//网络小说
										ids[j]=bkListLinks.get(j).getBookId();
									}else if(CommentUtils.TYPE_BOOK.equals(bkListLinks.get(j).getResType())){
										//图书
										netids[j]=bkListLinks.get(j).getBookId();
									}
								}
								List<BkInfo> bkinfos = bkFacade.findBkInfosByIds(ids);
								List<NetBook> netBooks = netBookFacade.findNetBkInfosByIds(netids);
								if((bkinfos!=null && bkinfos.size()>0) || (netBooks!=null && netBooks.size()>0)){
									List<BookInfo> bookInfos = new ArrayList<BookInfo>(bkListLinks.size());
									for(int j=0;j<bkinfos.size();j++){
										BkInfo bkInfo = bkinfos.get(j);
										if(bkInfo!=null && bkInfo.getId()>0){
											BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, TRUE);
											bookInfos.add(bookInfo);
										}
									}
									for(int j=0;j<netBooks.size();j++){
										NetBook netBook = netBooks.get(j);
										if(netBook!=null && netBook.getId()>0){
											BookInfo bookInfo = fileUtils.putBKToBookInfo(netBook, TRUE);
											bookInfos.add(bookInfo);
										}
									}
									ri.setBookInfos(bookInfos);
								}
							}
							resourceInfos.add(ri);
						}
					}
					
					
				}else if(CommentUtils.CASETYPE_HOT_MVCOMMENT.equals(caseType) 
						|| CommentUtils.CASETYPE_MV_BOX_OFFICE.equals(caseType)
						|| CommentUtils.CASETYPE_SELECTED_BKCOMMENT.equals(caseType)
						|| CommentUtils.CASETYPE_SELECTED_DIGEST.equals(caseType)
						|| CommentUtils.CASETYPE_MV_SELECTED_ARTICLE.equals(caseType)){
					//热门影评、电影票房榜、电影详情页的精彩影评
					resourceInfos = resourceManager.getResponseList(resourceLinks, uid, resourceInfos);
					//对资源进行倒序排列
					Collections.sort(resourceInfos);
				}else if(CommentUtils.CASETYPE_GOOD_MVCOMMENT.equals(caseType)){
					List<Long> ids = new ArrayList<Long>(resourceLinks.size());
					for(int i=0;i<resourceLinks.size();i++){
						ids.add(resourceLinks.get(i).getResId());
					}
					List<MvComment> mvComments = mvCommentFacade.findMvCommentsByIds(ids);
					if(mvComments!=null && mvComments.size()>0){
						for(int i=0;i<mvComments.size();i++){
							ResourceInfo resourceInfo = resourceManager.putObjectToResource(mvComments.get(i), uid, 1);
							resourceInfos.add(resourceInfo);
						}
						
					}
					//对资源进行倒序排列
					Collections.sort(resourceInfos);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resourceInfos;
	}
	
	/**
	 * 获取resource列表
	 * @param id
	 * @param caseType
	 * @param uid
	 * @return
	 */
	public List<ResourceInfo> getResourcesFromLink(Long id,String caseType,Long uid,String sort,Long lastId,int page){
		List<ResourceLink> resourceLinks = new ArrayList<ResourceLink>();
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		long begin = System.currentTimeMillis();
		try {
			//排除首页banner不查缓存
			if(CommentUtils.CASETYPE_INDEX.equals(caseType) || CommentUtils.CASETYPE_BK_BANNER.equals(caseType)){
				resourceLinks = resourceLinkFacade.findResListByType(caseType);
				resourceInfos = resourceManager.getResponseList(resourceLinks, uid, resourceInfos);
				//对资源进行倒序排列
				//Collections.sort(resourceInfos);
				List<ResourceInfo> resourceInfos2 = new ArrayList<ResourceInfo>(resourceInfos.size());
				//按加入banner的先后顺序排序
				if(resourceInfos!=null && resourceInfos.size()>0){
					for(int i=0;i<resourceLinks.size();i++){
						for(int j=0;j<resourceInfos.size();j++){
							if(resourceLinks.get(i).getResLinkId()==resourceInfos.get(j).getRid()){
								resourceInfos2.add(resourceInfos.get(j));
								break;
							}else if(CommentUtils.TYPE_OUT_LINK.equals(resourceInfos.get(j).getType()) 
									&& CommentUtils.TYPE_OUT_LINK.equals(resourceLinks.get(i).getLinkType()) 
									&& resourceLinks.get(i).getId()==resourceInfos.get(j).getRid()){
								resourceInfos2.add(resourceInfos.get(j));
								break;
							}
						}
					}
					resourceInfos = resourceInfos2;
				}
			}else{
				resourceInfos = operationMemcachedClient.get(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+caseType+"_"+id+"_"+sort+"_"+0);
				if(resourceInfos == null || resourceInfos.size() ==0){
					resourceInfos = new ArrayList<ResourceInfo>();
					if(caseTypeInList(caseType, RESOURCES)){//任意资源列表
						
						resourceLinks = resourceLinkFacade.findResListByType(caseType);
						resourceInfos = resourceManager.getResponseList(resourceLinks, uid, resourceInfos);
						//对资源进行倒序排列
						Collections.sort(resourceInfos);
						
					}else if(caseTypeInList(caseType, MOVIES)){//电影
						ResourceLink resourceLink  = new ResourceLink();
						List movieInfos = new ArrayList();
						//查出发现页，推荐影单的列表
						if(CommentUtils.CASETYPE_HOT_MOVIE_MORE.equals(caseType)){
							resourceLinks = resourceLinkFacade.findResListByType(CommentUtils.CASETYPE_HOT_MOVIE);
						}else{
							resourceLinks = resourceLinkFacade.findResListByType(caseType);
						}
						//查出list。size不为零，取第一个影单
						if(resourceLinks.size()>0){
							resourceLink = resourceLinks.get(0);
						}
						//获取该影单的link列表
						List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(resourceLink.getResLinkId(),lastId, null);
						//区分首页显示和更多
						if(CommentUtils.CASETYPE_HOT_MOVIE.equals(caseType) && mvListLinks.size()>CASETYPE_HOT_COUNT){
							mvListLinks.subList(0, CASETYPE_HOT_COUNT);
						}else if(CommentUtils.CASETYPE_UPCOMING_MOVIE.equals(caseType) && mvListLinks.size()>CASETYPE_HOT_COUNT){
							mvListLinks.subList(0, CASETYPE_HOT_COUNT);
						}
						
						//			Collections.sort(mvListLinks);
						//将link列表转化为movieinfo列表
						movieInfos = movieListManager.getMovieInfoResponseList(mvListLinks, uid, movieInfos);
						//排序
						if("score".equals(sort)){
							movieInfos = sortListByScore(movieInfos);
						}else if("reviewNum".equals(sort)){
							movieInfos = sortListByReviewNum(movieInfos);
						}else if("addTime".equals(sort)){
							movieInfos  = sortListByTime(movieInfos);
						}
//						//分页
//						movieInfos = CheckParams.getListByPage(movieInfos, page, pageSize);
						
						//将movieinfo列表转换为resource列表
						resourceInfos = resourceManager.getResponseList(movieInfos, uid, resourceInfos);
					}else if(caseTypeInList(caseType, BOOKS)){//书
						ResourceLink resourceLink  = new ResourceLink();
						List bookInfos = new ArrayList();
						//查出发现页，推荐书单的列表
						if(CommentUtils.CASETYPE_HOT_BOOK_MORE.equals(caseType)){
							resourceLinks = resourceLinkFacade.findResListByType(CommentUtils.CASETYPE_HOT_BOOK);
						}else{
							resourceLinks = resourceLinkFacade.findResListByType(caseType);
						}
						//查出list。size不为零，取第一个书单
						if(resourceLinks.size()>0){
							resourceLink = resourceLinks.get(0);
						}
						//获取该书单的link列表
						List<BookListLink> bookListLinks =  getResourceInfoFacade.findBookListInfo(resourceLink.getResLinkId(),lastId, null);
						//区分首页和更多
						if(CommentUtils.CASETYPE_HOT_BOOK.equals(caseType) && bookListLinks.size()>CASETYPE_HOT_COUNT){
							bookListLinks.subList(0, CASETYPE_HOT_COUNT);
						}
						
						//			Collections.sort(bookListLinks);
						//将link列表转化为bookinfo列表
						bookInfos = bookListManager.getBookListResponseList(bookListLinks, uid, bookInfos);
						//排序
						if("score".equals(sort)){
							bookInfos = sortListByScore(bookInfos);
						}else if("reviewNum".equals(sort)){
							bookInfos = sortListByReviewNum(bookInfos);
						}else if("addTime".equals(sort)){
							bookInfos  = sortListByTime(bookInfos);
						}
//						//分页
//						bookInfos = CheckParams.getListByPage(bookInfos, page, pageSize);
						//将bookinfo列表转换为resource列表
						resourceInfos = resourceManager.getResponseList(bookInfos, uid, resourceInfos);
					}else if(caseTypeInList(caseType, RESOURCE_BY_ID)){
						//获取跟电影或书相关联的长文章
						resourceLinks = resourceLinkFacade.findResListByResIdAndType(id, caseType);
						//entityList = getEntityListFromResourceLink(resourceLinks, uid, entityList);
						resourceInfos = resourceManager.getResponseList(resourceLinks, uid, resourceInfos);
						//对资源进行倒序排列
						Collections.sort(resourceInfos);
					}else if(caseTypeInList(caseType, BOOK_MOVIE_LISTS)){//书单影单
						if(CommentUtils.CASETYPE_PRODUCT_RANK_MOVIE_MORE.equals(caseType)){
							resourceLinks = resourceLinkFacade.findResListByType(CommentUtils.CASETYPE_PRODUCT_RANK_MOVIE);
						}else if(CommentUtils.CASETYPE_PRODUCT_RANK_BOOK_MORE.equals(caseType)){
							resourceLinks = resourceLinkFacade.findResListByType(CommentUtils.CASETYPE_PRODUCT_RANK_BOOK);
						}else{
							resourceLinks = resourceLinkFacade.findResListByType(caseType);
						}
						
						if(resourceLinks.size()>CASETYPE_PRODUCT_RANK_COUNT){
							if(CommentUtils.CASETYPE_PRODUCT_RANK_BOOK.equals(caseType)
									|| CommentUtils.CASETYPE_PRODUCT_RANK_MOVIE.equals(caseType)
									|| CommentUtils.CASETYPE_MOVIELIST.equals(caseType)
									|| CommentUtils.CASETYPE_BOOKLIST.equals(caseType)){
								resourceLinks = resourceLinks.subList(0, CASETYPE_PRODUCT_RANK_COUNT);
							}
						}
						//entityList = getEntityListFromResourceLink(resourceLinks, uid, entityList);
						resourceInfos = resourceManager.getResponseList(resourceLinks, uid, resourceInfos);
						//对资源进行倒序排列
						Collections.sort(resourceInfos);
					}
					operationMemcachedClient.set(MemcacheResourceLinkConstant.RESOURCE_LINK_TYPE+caseType+"_"+id+"_"+sort+"_"+0, 
							MemcacheResourceLinkConstant.TIME_INTERVALS_60_60, resourceInfos);
				}
			}
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("link表获取时间："+(end - begin));
		if(caseTypeInList(caseType, BOOKS) || caseTypeInList(caseType, MOVIES)){
			//分页
			resourceInfos = CheckParams.getListByPage(resourceInfos, page, pageSize);
		}
		LOG.info("link表获取时间："+(end - begin));
		
		return resourceInfos;
	}
	
	/**
	 * 对书或电影出版时间或上映时间倒序排列
	 * @param list
	 * @return
	 */
	public List<Object> sortListByTime(List<Object> list){
		if(list == null || list.size() == 0){
			return list;
		}
		Object obj = list.get(0);
		String objName = obj.getClass().getName();
		if(MovieInfo.class.getName().equals(objName)){
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < i; j++) {
					Object temp = null;
					String aStr = "";
					try {
						List<String> timeA = ((MovieInfo)list.get(i)).getYear();
						if(timeA != null && timeA.size()>0){
							aStr = timeA.get(0);
						}
					} catch (Exception e) {
						LOG.error("电影["+((MovieInfo)list.get(i)).getId()+"]的上映时间有问题"+e.getMessage(), e.fillInStackTrace());
						aStr = "";
					}
					String bStr = "";
					try {
						List<String> timeB = ((MovieInfo)list.get(j)).getYear();
						if(timeB != null && timeB.size()>0){
							bStr = timeB.get(0);
						}
					} catch (Exception e) {
						LOG.error("电影["+((MovieInfo)list.get(j)).getId()+"]的上映时间有问题"+e.getMessage(), e.fillInStackTrace());
						bStr= "";
					}
					
					
					long a = MovieRelationUtil.getMovieTimes(aStr);
					long b = MovieRelationUtil.getMovieTimes(bStr);
					
					if(a>b){
						temp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, temp);
					}
				}
			}
		}else if(BookInfo.class.getName().equals(objName)){
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < i; j++) {
					BookInfo bookInfoA = (BookInfo) list.get(i);
					BookInfo bookInfoB = (BookInfo) list.get(j);
					Object temp = null;
					String aStr = "";
					try {
						aStr = ((BookInfo)list.get(i)).getPublishTime();
					} catch (Exception e) {
						LOG.error("书["+((BookInfo)list.get(i)).getId()+"]的出版时间有问题"+e.getMessage(), e.fillInStackTrace());
						aStr = "";
					}
					String bStr = "";
					try {
						bStr = ((BookInfo)list.get(j)).getPublishTime();
					} catch (Exception e) {
						LOG.error("书["+((BookInfo)list.get(j)).getId()+"]的出版时间有问题"+e.getMessage(), e.fillInStackTrace());
						bStr = "";
					}
					
					
					long a = 0;
					long b = 0;
					if(CommentUtils.TYPE_NETBOOK.equals(bookInfoA.getType())){
						a = bookInfoA.getSortId();
						b = bookInfoB.getSortId();
					}else{
						a = BookRelationUtil.getBookTime(aStr);
						b = BookRelationUtil.getBookTime(bStr);
					}
					if(a>b){
						temp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, temp);
					}
				}
			}
			
		}
		return list;
		
	}
	/**
	 * 对书或电影评论数量的倒序排列
	 * @param list
	 * @return
	 */
	public List<Object> sortListByReviewNum(List<Object> list){
		if(list == null || list.size() == 0){
			return list;
		}
		Object obj = list.get(0);
		String objName = obj.getClass().getName();
		if(MovieInfo.class.getName().equals(objName)){
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < i; j++) {
					Object temp = null;
					MvAvgMark mvAvgMarkA = mvCommentFacade.findMvAvgMarkByMvId(((MovieInfo)list.get(i)).getId());
					MvAvgMark mvAvgMarkB = mvCommentFacade.findMvAvgMarkByMvId(((MovieInfo)list.get(j)).getId());
					int a = 0;
					int b = 0;
					if(mvAvgMarkA != null && mvAvgMarkA.getId() != 0){
						a = mvAvgMarkA.getMvTotalNum() + mvAvgMarkA.getExpertsTotalNum();
					}
					if(mvAvgMarkB != null && mvAvgMarkB.getId() != 0){
						b = mvAvgMarkB.getMvTotalNum() + mvAvgMarkB.getExpertsTotalNum();
					}
//					float a = Float.valueOf(((MovieInfo)list.get(i)).getScore());
//					float b = Float.valueOf(((MovieInfo)list.get(j)).getScore());
					if(a>b){
						temp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, temp);
					}
				}
			}
		}else if(BookInfo.class.getName().equals(objName)){
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < i; j++) {
					Object temp = null;
					BkAvgMark bkAvgMarkA = bkCommentFacade.findBkAvgMarkByBkId(((BookInfo)list.get(i)).getId());
					BkAvgMark bkAvgMarkB = bkCommentFacade.findBkAvgMarkByBkId(((BookInfo)list.get(j)).getId());
					int a = 0;
					int b = 0;
					if(bkAvgMarkA != null && bkAvgMarkA.getId() != 0){
						a = bkAvgMarkA.getBkTotalNum();
					}
					if(bkAvgMarkB != null && bkAvgMarkB.getId() != 0){
						b = bkAvgMarkB.getBkTotalNum();
					}
//					float a = Float.valueOf(((BookInfo)list.get(i)).getScore());
//					float b = Float.valueOf(((BookInfo)list.get(j)).getScore());
					if(a>b){
						temp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, temp);
					}
				}
			}
			
		}
		return list;
		
	}
	/**
	 * 对list中的数据进行倒序排列
	 * @param list
	 * @return
	 */
	public List<Object> sortListByScore(List<Object> list){
		if(list == null || list.size() == 0){
			return list;
		}
		Object obj = list.get(0);
		String objName = obj.getClass().getName();
		if(MovieInfo.class.getName().equals(objName)){
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < i; j++) {
					Object temp = null;
					float a = Float.valueOf(((MovieInfo)list.get(i)).getScore());
					float b = Float.valueOf(((MovieInfo)list.get(j)).getScore());
					if(a>b){
						temp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, temp);
					}
				}
			}
		}else if(BookInfo.class.getName().equals(objName)){
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < i; j++) {
					Object temp = null;
					float a = Float.valueOf(((BookInfo)list.get(i)).getScore());
					float b = Float.valueOf(((BookInfo)list.get(j)).getScore());
					if(a>b){
						temp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, temp);
					}
				}
			}
			
		}
		return list;
		
	}
	
	/**
	 * 将运营表中集合转化为各种对应类型集合
	 * @param resourceLinks
	 * @param uid
	 * @param entityList
	 * @return
	 */
	public List getEntityListFromResourceLink(List<ResourceLink> resourceLinks ,Long uid, List entityList){
		
		if(resourceLinks.size()>0){
			ResourceLink resourceLink = resourceLinks.get(0);
			if(resourceLink.getId() != 0){
				flagint = ResultUtils.SUCCESS;
				for (ResourceLink rl : resourceLinks) {
					String type = rl.getLinkType();
					long id = rl.getResLinkId();
					if(CommentUtils.TYPE_BOOKLIST.equals(type)){
						BookList bookList = getResourceInfoFacade.queryByIdBookList(id);
						entityList.add(bookList);
					}else if(CommentUtils.TYPE_MOVIELIST.equals(type)){
						MovieList movieList = myMovieFacade.findMovieListById(id);
						entityList.add(movieList);
					}else if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
						BkComment bkComment = bkCommentFacade.findCommentIsExistById(id);
						entityList.add(bkComment);
					}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
						MvComment mvComment = mvCommentFacade.findMvCommentIsExist(id);
						entityList.add(mvComment);
					}else if(CommentUtils.TYPE_DIARY.equals(type)){
						Diary diary = diaryFacade.queryByIdDiary(id);
						entityList.add(diary);
					}else if(CommentUtils.TYPE_ARTICLE.equals(type)){
						Post post = postFacade.queryByIdName(id);
						entityList.add(post);
					}else if(CommentUtils.TYPE_NEWARTICLE.equals(type)){
						Article article = articleFacade.queryArticleById(id);
						entityList.add(article);
					}else if (CommentUtils.TYPE_BOOK.equals(type)){
						BkInfo bkInfo = bkFacade.findBkInfo((int)id);
						entityList.add(bkInfo);
					}else if(CommentUtils.TYPE_MOVIE.equals(type)){
						MvInfo mvInfo = mvFacade.queryById(id);
						entityList.add(mvInfo);
					}else if(CommentUtils.TYPE_GRAPHIC_FILM.equals(type)){
						GraphicFilm graphicFilm = graphicFilmFacade.findGraphicFilmById(id);
						entityList.add(graphicFilm);
					}
				}
			}else{
				flagint = ResultUtils.ERROR;
			}
		}else{
			flagint = ResultUtils.SUCCESS;
		}
		
		
		
		return entityList;
	}
	
	
	
	
	
	
	
	
	/**
	 * 判断caseType是否在strs中
	 * @param caseType
	 * @param strs
	 * @return
	 */
	public static boolean caseTypeInList(String caseType,String[] strs){
		boolean flag = false;
		
		if(caseType ==null || "".equals(caseType) || strs == null || strs.length == 0){
			return flag;
		}
		
		for (String string : strs) {
			if(caseType.equals(string)){
				flag = true;
				return flag;
			}
		}
		
		
		
		return flag;
	}
	
	public static void main(String[] args) {
//		System.out.println(caseTypeInList("200", BOOKS));
	}
	
	public void setGetResourceInfoFacade(
			GetResourceInfoFacadeImpl getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setDiaryFacade(DiaryFacade diaryFacade) {
		this.diaryFacade = diaryFacade;
	}
	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}
	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}
	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}
	public void setSerializeFacade(SerializeFacade serializeFacade) {
		this.serializeFacade = serializeFacade;
	}
	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}
	public void setMsgFacade(MsgFacade msgFacade) {
		this.msgFacade = msgFacade;
	}
	public void setJedisSimpleClient(JedisSimpleClient jedisSimpleClient) {
		this.jedisSimpleClient = jedisSimpleClient;
	}



	public void setResourceLinkFacade(ResourceLinkFacade resourceLinkFacade) {
		this.resourceLinkFacade = resourceLinkFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setMovieListManager(MovieListManager movieListManager) {
		this.movieListManager = movieListManager;
	}
	public void setBookListManager(BookListManager bookListManager) {
		this.bookListManager = bookListManager;
	}
	public void setOperationMemcachedClient(MemcachedClient operationMemcachedClient) {
		this.operationMemcachedClient = operationMemcachedClient;
	}

	public void setGraphicFilmFacade(GraphicFilmFacade graphicFilmFacade) {
		this.graphicFilmFacade = graphicFilmFacade;
	}

	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}

	public void setMvBkManager(MvBkManager mvBkManager) {
		this.mvBkManager = mvBkManager;
	}
	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}
	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}
}
