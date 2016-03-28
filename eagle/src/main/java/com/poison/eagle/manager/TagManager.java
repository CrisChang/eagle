package com.poison.eagle.manager; 

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.CategoryInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.ResInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.TagInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.entity.UserTagInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HtmlUtil;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StringUtils;
import com.poison.eagle.utils.TagUtils;
import com.poison.eagle.utils.UserUtils;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyBkFacade;
import com.poison.resource.client.TopicFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvComment;
import com.poison.resource.model.Tag;
import com.poison.resource.model.TagCategory;
import com.poison.resource.model.Topic;
import com.poison.resource.model.UserTag;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.client.NetBookFacade;
import com.poison.store.client.PopChartsFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.store.model.NetBook;
import com.poison.store.model.PopCharts;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class TagManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(TagManager.class);
	private final static String BUSINESS = "business";
	private int flagint;
	private TagUtils tagUtils = TagUtils.getInstance();
	private MyBkFacade myBkFacade;
	private MvFacade mvFacade;
	private BkFacade bkFacade;
	private NetBookFacade netBookFacade;
	private MvCommentFacade mvCommentFacade;
	private BkCommentFacade bkCommentFacade;
	private PopChartsFacade popChartsFacade;
	private UcenterFacade ucenterFacade;
	private TopicFacade topicFacade;
	private ArticleFacade articleFacade;
	private BookListManager bookListManager;
	private MovieListManager movieListManager;
	private RankingManager rankingManager;
	private ResourceManager resourceManager;
	/**
	 * 用户的相关缓存
	 */
	private UserJedisManager userJedisManager;
	private ResStatJedisManager resStatJedisManager;
	private MovieUtils movieUtils = MovieUtils.getInstance();
	private FileUtils fileUtils = FileUtils.getInstance();
	private UserUtils userUtils = UserUtils.getInstance();
	
	/**
	 * 发现页分类列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewCategory(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String tag ="";
		Long lastId = null;
		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		
		List<TagCategory> tagCategories = myBkFacade.findTagCategoryByType(type);
		List<CategoryInfo> categories = new ArrayList<CategoryInfo>();
		//正序
		Collections.sort(tagCategories);
		for (TagCategory tagCategory : tagCategories) {
			CategoryInfo category = new CategoryInfo();
			category.setId(tagCategory.getId());
			category.setName(tagCategory.getTagCategory());
			category.setImageUrl(tagCategory.getCoverPage());
			categories.add(category);
		}
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", categories);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 发现页分类下，标签列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewCategoryTag(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String name = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			name = dataq.get("name").toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}

		//全部标签
		List<Tag> tags = myBkFacade.findAllTagListByTagGroup(name);
		List<String> tagInofs = new ArrayList<String>();
		for (Tag tag : tags) {
			tagInofs.add(tag.getTagName());
		}
		//热门标签
		List<Tag> hotTags = myBkFacade.findHotTagListByTagGroup(name);
		List<String> hotTagInfos = new ArrayList<String>();
		for (Tag tag : hotTags) {
			hotTagInfos.add(tag.getTagName());
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("tagInfos", tagInofs);
			datas.put("hotTagInfos", hotTagInfos);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 排行榜列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewRank(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String type = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		List<PopCharts> popCharts = popChartsFacade.findPopChartsByType(type);
		List<CategoryInfo> categoryInfos = new ArrayList<CategoryInfo>();
		
		//正序
		Collections.sort(popCharts);
		for (PopCharts popChart : popCharts) {
			CategoryInfo category = new CategoryInfo();
			category.setId(popChart.getId());
			category.setName(popChart.getChartName());
			category.setImageUrl(popChart.getCoverPage());
			categoryInfos.add(category);
		}
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", categoryInfos);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 排行榜中的电影或书
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewResourceByLink(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String type = "";
		String name = "";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			type = (String) dataq.get("type");
			name = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		
		List<BookInfo> bookInfos = new ArrayList<BookInfo>();
		List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
		if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
			bookInfos = bookListManager.getHotBookList();
		}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
			movieInfos = movieListManager.getHotMovieList(CommentUtils.TYPE_MOVIELIST);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			if(CommentUtils.TYPE_BOOK_COMMENT.equals(type)){
				datas.put("list", bookInfos);
			}else if(CommentUtils.TYPE_MOVIE_COMMENT.equals(type)){
				datas.put("list", movieInfos);
			}
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	/**
	 * 可选标签列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewTagList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String tag ="";
		Long lastId = null;
		String type = "";
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		//全部标签
		List<Tag> tags = myBkFacade.findTagListByType(type);
		List<String> tagInfos = new ArrayList<String>();
		tagInfos = getTagStringList(tags, tagInfos);
		
		//用户常用标签
		List<String> userTagInfos = new ArrayList<String>();
		
		userTagInfos = getUserTagList(uid);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("tagInfos", tagInfos);
			datas.put("userTagInfos", userTagInfos);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}

	/**
	 * 小说标签列表
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String viewStoryTagList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";

		String tag ="";
		Long lastId = null;
		String type = "";
		//去掉空格
		reqs = reqs.trim();

		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		//全部标签
		List<Tag> tags = myBkFacade.findTagListByType(type);
		//List<TagInfo> tagInfos = tagUtils.putTagToInfo(tags);

		List<Map<String,Object>> tagInfos = new ArrayList<Map<String,Object>>();
		tagInfos = getStoryTagStringList(tags,tagInfos);//TagStringList(tags, tagInfos);

		List<Tag> femaleTags = myBkFacade.findTagListByType("300");
		List<Map<String,Object>> femaleTagInfos = new ArrayList<Map<String,Object>>();
		femaleTagInfos = getStoryTagStringList(femaleTags,femaleTagInfos);
		//用户常用标签
		//List<String> userTagInfos = new ArrayList<String>();

		//userTagInfos = getUserTagList(uid);

		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("maleChannel", tagInfos);
			datas.put("femaleChannel", femaleTagInfos);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);

		return resString;
	}

	/**
	 * 书或影的筛选标签
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewSearchTag(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String tag ="";
		String type = "";
		Long lastId = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		//全部标签
		List<Tag> tags = myBkFacade.findTagListByType(type);
		
		List<TagInfo> tagInfos = tagUtils.putTagToInfo(tags);
		List<String> sort = new ArrayList<String>();
		List<Map<String, Object>> tagList = new ArrayList<Map<String,Object>>();
		
		if(tagInfos != null && tagInfos.size()>0){
			Iterator<TagInfo> iter = tagInfos.iterator();
			while(iter.hasNext()){
				TagInfo tagInfo = iter.next();
				String group = tagInfo.getTagGroup();
				if("sort".equals(group)){
//					sort = tagInfo.getTags();
				}else{
					Map<String, Object> tagMap = new HashMap<String, Object>();
					tagMap.put(tagInfo.getTagGroup(), tagInfo.getTags());
					tagList.add(tagMap);
				}
			}
		}
		
		//运营分类
		List<Map<String, String>> typeList = new ArrayList<Map<String,String>>();
		List<Tag> fuckList = myBkFacade.findTagListByType(BUSINESS+type);
//		List<TagInfo> funckLists = tagUtils.putTagToInfo(fuckList);
		if(fuckList!= null && fuckList.size()>0 && fuckList.get(0).getFlag() != ResultUtils.DATAISNULL){
			Iterator<Tag> iterator = fuckList.iterator();
			while(iterator.hasNext()){
				Tag tag2 = iterator.next();
				Map<String, String> map = new HashMap<String, String>();
				String sortStr = "";
				if(CommentUtils.CASETYPE_BUSINESS_NEW_MOVIELIST.equals(tag2.getTagGroup())){
					sortStr = "addTime";
				}else if(CommentUtils.CASETYPE_BUSINESS_HOT_MOVIELIST.equals(tag2.getTagGroup())){
					sortStr = "reviewNum";
				}else if(CommentUtils.CASETYPE_BUSINESS_COLLECT_MOVIELIST.equals(tag2.getTagGroup())){
					sortStr = "collectNum";
				}else if(CommentUtils.CASETYPE_BUSINESS_NEW_BOOKLIST.equals(tag2.getTagGroup())){
					sortStr = "addTime";
				}else if(CommentUtils.CASETYPE_BUSINESS_HOT_BOOKLIST.equals(tag2.getTagGroup())){
					sortStr = "reviewNum";
				}else if(CommentUtils.CASETYPE_BUSINESS_COLLECT_BOOKLIST.equals(tag2.getTagGroup())){
					sortStr = "collectNum";
				}
				map.put("sort", sortStr);
				map.put("enum", tag2.getTagGroup());
				map.put("title", tag2.getTagName());
				typeList.add(map);
			}
		}
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("sort", sort);
			datas.put("tagList", tagList);
			datas.put("typeList", typeList);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 书或影的筛选标签
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewSearchTagNew(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> res =null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		String tag ="";
		String type = "";
		Long lastId = null;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			//参数
			type = (String) dataq.get("type");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
		//全部标签
		List<Tag> tags = myBkFacade.findTagByTypeOrderById(type);
		
		List<ResInfo> resInfos = null;
		String TYPE_ALL = "188";//"全部"标签
		
		//大家都在搜的标签值【188:"全部",189:"长影评",190:"长书评"】
		
		int flagint = 0;
		if(tags!=null && tags.size()>0 && tags.get(0).getId()>0){
			resInfos = new ArrayList<ResInfo>(tags.size());
			long ids[]=new long[tags.size()];
			List<Long> idlist = new ArrayList<Long>(tags.size());
			
			long bkids[]=new long[tags.size()];//图书id
			long netids[]=new long[tags.size()];//网络小说id
			List<Long> bkidlist = new ArrayList<Long>(tags.size());
			List<Long> netidlist = new ArrayList<Long>(tags.size());
			
			
			List<Long> userids = new ArrayList<Long>(tags.size());
			
			List<Long> topicids = new ArrayList<Long>(tags.size());
			
			List<Long> articleids = new ArrayList<Long>(tags.size());
			String restype = null;
			for(int i=0;i<tags.size();i++){
				if(!"sort".equals(tags.get(i).getTagGroup())){
					if(restype==null || "".equals(restype)){
						restype = tags.get(i).getRestype();
					}
					if(TYPE_ALL.equals(type)){
						//“全部”标签
						
					}else if(CommentUtils.TYPE_MOVIE.equals(tags.get(i).getRestype())){
						ids[i]=tags.get(i).getResid();
						idlist.add(tags.get(i).getResid());
					}else if(CommentUtils.TYPE_BOOK.equals(tags.get(i).getRestype())){
						bkids[i]=tags.get(i).getResid();
						bkidlist.add(tags.get(i).getResid());
					}else if(CommentUtils.TYPE_NETBOOK.equals(tags.get(i).getRestype())){
						netids[i]=tags.get(i).getResid();
						netidlist.add(tags.get(i).getResid());
					}else if(CommentUtils.TYPE_USER.equals(tags.get(i).getRestype())){
						userids.add(tags.get(i).getResid());
					}else if(CommentUtils.TYPE_TOPIC.equals(tags.get(i).getRestype())){
						topicids.add(tags.get(i).getResid());
					}else if(CommentUtils.TYPE_NEWARTICLE.equals(tags.get(i).getRestype())){
						//新的长文章
						articleids.add(tags.get(i).getResid());
					}else if(CommentUtils.TYPE_ARTICLE_BOOK.equals(tags.get(i).getRestype())){
						//长书评
						//ids[i]=tags.get(i).getResid();
						idlist.add(tags.get(i).getResid());
					}else if(CommentUtils.TYPE_ARTICLE_MOVIE.equals(tags.get(i).getRestype())){
						//长影评
						//ids[i]=tags.get(i).getResid();
						idlist.add(tags.get(i).getResid());
					}
				}else{
					tags.remove(i);
					i--;
				}
			}
			if(TYPE_ALL.equals(type)){
				//“全部”标签
				for(int i=0;i<tags.size();i++){
					if(!"sort".equals(tags.get(i).getTagGroup())){
						ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(tags.get(i).getResid(), tags.get(i).getRestype(), uid);
						if(resourceInfo!=null && resourceInfo.getRid()>0){
							ResInfo resInfo = ResInfo.convert(resourceInfo);
							resInfos.add(resInfo);
						}
					}
				}
			}else if(CommentUtils.TYPE_MOVIE.equals(restype)){
				List<MvInfo> mvInfos = mvFacade.findMvInfosByIds(ids);
				List<MvAvgMark> mvAvgMarks = mvCommentFacade.findMvAvgMarkByMvIds(idlist);
				if(mvInfos!=null && mvInfos.size()==1 && mvInfos.get(0).getFlag()==ResultUtils.QUERY_ERROR){
					flagint = ResultUtils.QUERY_ERROR;
				}else{
					if(mvInfos!=null && mvInfos.size()>0){
						for(int i=0;i<tags.size();i++){
							for(int j=0;j<mvInfos.size();j++){
								if(mvInfos.get(j).getId()==tags.get(i).getResid()){
									//电影
									MvInfo mvInfo = mvInfos.get(j);
									MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, FALSE);
									movieInfo.setDescribe(HtmlUtil.getTextFromHtml(movieInfo.getDescribe()));
									//需要赋值搜索量和热度
									int totalSearchNum = resStatJedisManager.getTotalSearchNum(movieInfo.getId(), CommentUtils.TYPE_MOVIE);
									long searchNum = getSearchNum(movieInfo.getId(), totalSearchNum);
									movieInfo.setSearchNum(searchNum+"");
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
									ResInfo resInfo = ResInfo.convert(movieInfo);
									resInfos.add(resInfo);
									break;
								}
							}
						}
					}
				}
			}else if(CommentUtils.TYPE_BOOK.equals(restype) || CommentUtils.TYPE_NETBOOK.equals(restype)){
				List<BkInfo> bkInfos = bkFacade.findBkInfosByIds(bkids);
				
				List<NetBook> netBooks = netBookFacade.findNetBkInfosByIds(netids);
				
				List<BkAvgMark> bkAvgMarks = bkCommentFacade.findBkAvgMarkByBkIds(bkidlist, CommentUtils.TYPE_BOOK);
				List<BkAvgMark> netbkAvgMarks = bkCommentFacade.findBkAvgMarkByBkIds(netidlist, CommentUtils.TYPE_NETBOOK);
				
				for(int i=0;i<tags.size();i++){
					if(CommentUtils.TYPE_BOOK.equals(tags.get(i).getRestype())){
						if(bkInfos!=null && bkInfos.size()>0 && bkInfos.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
							for(int j=0;j<bkInfos.size();j++){
								if(bkInfos.get(j).getId()==tags.get(i).getResid()){
									//图书
									BkInfo bkInfo = bkInfos.get(j);
									BookInfo bookInfo = fileUtils.putBKToBookInfo(bkInfo, FALSE);
											//bookUtils.putBKToBookInfoIndex(bkInfo, FALSE,userId,bkCommentFacade,ucenterFacade);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									int totalSearchNum = resStatJedisManager.getTotalSearchNum(bookInfo.getId(), CommentUtils.TYPE_BOOK);
									long searchNum = rankingManager.getSearchNum(bookInfo.getId(), totalSearchNum);
									bookInfo.setSearchNum(searchNum+"");
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
									ResInfo resInfo = ResInfo.convert(bookInfo);
									resInfos.add(resInfo);
									break;
								}
							}
						}
					}else if(CommentUtils.TYPE_NETBOOK.equals(tags.get(i).getRestype())){
						if(netBooks!=null && netBooks.size()>0 && netBooks.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
							for(int j=0;j<netBooks.size();j++){
								if(netBooks.get(j).getId()==tags.get(i).getResid()){
									//网络小说
									NetBook netBook = netBooks.get(j);
									BookInfo bookInfo = fileUtils.putBKToBookInfo(netBook, FALSE);
											//bookUtils.putBKToBookInfoIndex(netBook, FALSE,userId,bkCommentFacade,ucenterFacade);
									bookInfo.setIntroduction(HtmlUtil.getTextFromHtml(bookInfo.getIntroduction()));
									int totalSearchNum = resStatJedisManager.getTotalSearchNum(bookInfo.getId(), CommentUtils.TYPE_NETBOOK);
									long searchNum = rankingManager.getSearchNum(bookInfo.getId(), totalSearchNum);
									bookInfo.setSearchNum(searchNum+"");
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
									ResInfo resInfo = ResInfo.convert(bookInfo);
									resInfos.add(resInfo);
									break;
								}
							}
						}
					}
				}
			}else if(CommentUtils.TYPE_USER.equals(restype)){
				List<UserAllInfo> userAllInfos = null;
				try{
					userAllInfos = ucenterFacade.findUserAllInfoListByUseridList(null, userids);
				}catch(Exception e){
					LOG.error(e.getMessage(),e.fillInStackTrace());
				}
				if(userAllInfos!=null && userAllInfos.size()>0){
					for(int i=0;i<userids.size();i++){
						for(int j=0;j<userAllInfos.size();j++){
							if(userAllInfos.get(j).getUserId()==userids.get(i)){
								//需要判断当前用户与该用户的关系（是否关注了）
								UserEntity userEntity = fileUtils.copyUserInfo(userAllInfos.get(j), TRUE);
								int isInterest = userUtils.getIsInterest(uid,userEntity.getId(), ucenterFacade,userJedisManager);
								userEntity.setIsInterest(isInterest);
								ResInfo resInfo = ResInfo.convert(userEntity);
								resInfos.add(resInfo);
								userAllInfos.remove(j);
								break;
							}
						}
					}
				}
			}else if(CommentUtils.TYPE_TOPIC.equals(restype)){
				List<Topic> topics = topicFacade.findTopicsByTopicIds(topicids);
				if(topics!=null && topics.size()>0 && topics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
					for(int i=0;i<topicids.size();i++){
						for(int j=0;j<topics.size();j++){
							if(topicids.get(i)==topics.get(j).getId()){
								ResInfo resInfo = ResInfo.convert(topics.get(j));
								resInfos.add(resInfo);
								topics.remove(j);
								break;
							}
						}
					}
				}
			}else if(CommentUtils.TYPE_NEWARTICLE.equals(restype)){
				List<Article> articles = articleFacade.findArticlesByIds(articleids);
				if(articles!=null && articles.size()>0 && articles.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
					//需要查询出文章所属的用户信息
					List<Long> uids = new ArrayList<Long>();
					for(int j=0;j<articles.size();j++){
						uids.add(articles.get(j).getUid());
					}
					List<UserInfo> userinfos = null;
					try{
						userinfos = ucenterFacade.findUserListByUseridList(null, uids);
					}catch(Exception e){
						LOG.error(e.getMessage(),e.fillInStackTrace());
					}
					for(int i=0;i<articleids.size();i++){
						for(int j=0;j<articles.size();j++){
							if(articleids.get(i)==articles.get(j).getId()){
								ResInfo resInfo = ResInfo.convert(articles.get(j));
								if(userinfos!=null && userinfos.size()>0){
									for(int z=0;z<userinfos.size();z++){
										if(userinfos.get(z).getUserId()==resInfo.getUserid()){
											UserInfo userInfo = userinfos.get(z);
											UserEntity userEntity = fileUtils.copyUserInfo(userInfo, TRUE);
											resInfo.setUserEntity(userEntity);
											//resInfo.setHeadImage(userInfo.getFaceAddress());
											//resInfo.setAuthorName(userInfo.getName());
											//resInfo.setSex(userInfo.getSex());
											//resInfo.setAge(userInfo.getAge());
											//resInfo.setUserType(userInfo.getLevel());
											break;
										}
									}
								}
								resInfos.add(resInfo);
								articles.remove(j);
								break;
							}
						}
					}
				}
			}else if(CommentUtils.TYPE_ARTICLE_MOVIE.equals(restype)){
				//长影评
				List<MvComment> mvComments = mvCommentFacade.findMvCommentsByIds(idlist);
				if(mvComments!=null && mvComments.size()>0 && mvComments.get(0).getId()>0){
					//需要查询出文章所属的用户信息
					List<Long> uids = new ArrayList<Long>();
					for(int j=0;j<mvComments.size();j++){
						uids.add(mvComments.get(j).getUserId());
					}
					List<UserInfo> userinfos = null;
					try{
						userinfos = ucenterFacade.findUserListByUseridList(null, uids);
					}catch(Exception e){
						LOG.error(e.getMessage(),e.fillInStackTrace());
					}
					for(int i=0;i<idlist.size();i++){
						for(int j=0;j<mvComments.size();j++){
							if(idlist.get(i)==mvComments.get(j).getId()){
								ResInfo resInfo = ResInfo.convert(mvComments.get(j));
								if(userinfos!=null && userinfos.size()>0){
									for(int z=0;z<userinfos.size();z++){
										if(userinfos.get(z).getUserId()==resInfo.getUserid()){
											UserInfo userInfo = userinfos.get(z);
											UserEntity userEntity = fileUtils.copyUserInfo(userInfo, TRUE);
											resInfo.setUserEntity(userEntity);
											//resInfo.setHeadImage(userInfo.getFaceAddress());
											//resInfo.setAuthorName(userInfo.getName());
											//resInfo.setSex(userInfo.getSex());
											//resInfo.setAge(userInfo.getAge());
											//resInfo.setUserType(userInfo.getLevel());
											break;
										}
									}
								}
								resInfos.add(resInfo);
								break;
							}
						}
					}
				}
			}else if(CommentUtils.TYPE_ARTICLE_BOOK.equals(restype)){
				//长影评
				List<BkComment> bkComments = bkCommentFacade.findBkCommentListByIds(idlist);
				if(bkComments!=null && bkComments.size()>0 && bkComments.get(0).getId()>0){
					//需要查询出文章所属的用户信息
					List<Long> uids = new ArrayList<Long>();
					for(int j=0;j<bkComments.size();j++){
						uids.add(bkComments.get(j).getUserId());
					}
					List<UserInfo> userinfos = null;
					try{
						userinfos = ucenterFacade.findUserListByUseridList(null, uids);
					}catch(Exception e){
						LOG.error(e.getMessage(),e.fillInStackTrace());
					}
					for(int i=0;i<idlist.size();i++){
						for(int j=0;j<bkComments.size();j++){
							if(idlist.get(i)==bkComments.get(j).getId()){
								ResInfo resInfo = ResInfo.convert(bkComments.get(j));
								if(userinfos!=null && userinfos.size()>0){
									for(int z=0;z<userinfos.size();z++){
										if(userinfos.get(z).getUserId()==resInfo.getUserid()){
											UserInfo userInfo = userinfos.get(z);
											UserEntity userEntity = fileUtils.copyUserInfo(userInfo, TRUE);
											resInfo.setUserEntity(userEntity);
											//resInfo.setHeadImage(userInfo.getFaceAddress());
											//resInfo.setAuthorName(userInfo.getName());
											//resInfo.setSex(userInfo.getSex());
											//resInfo.setAge(userInfo.getAge());
											//resInfo.setUserType(userInfo.getLevel());
											break;
										}
									}
								}
								resInfos.add(resInfo);
								break;
							}
						}
					}
				}
			}
		}else{
			resInfos = new ArrayList<ResInfo>(0);
		}
		
		//运营分类
		List<Map<String, String>> typeList = new ArrayList<Map<String,String>>();
		/*List<Tag> fuckList = myBkFacade.findTagListByType(BUSINESS+type);
//		List<TagInfo> funckLists = tagUtils.putTagToInfo(fuckList);
		if(fuckList!= null && fuckList.size()>0 && fuckList.get(0).getFlag() != ResultUtils.DATAISNULL){
			Iterator<Tag> iterator = fuckList.iterator();
			while(iterator.hasNext()){
				Tag tag2 = iterator.next();
				Map<String, String> map = new HashMap<String, String>();
				String sortStr = "";
				if(CommentUtils.CASETYPE_BUSINESS_NEW_MOVIELIST.equals(tag2.getTagGroup())){
					sortStr = "addTime";
				}else if(CommentUtils.CASETYPE_BUSINESS_HOT_MOVIELIST.equals(tag2.getTagGroup())){
					sortStr = "reviewNum";
				}else if(CommentUtils.CASETYPE_BUSINESS_COLLECT_MOVIELIST.equals(tag2.getTagGroup())){
					sortStr = "collectNum";
				}else if(CommentUtils.CASETYPE_BUSINESS_NEW_BOOKLIST.equals(tag2.getTagGroup())){
					sortStr = "addTime";
				}else if(CommentUtils.CASETYPE_BUSINESS_HOT_BOOKLIST.equals(tag2.getTagGroup())){
					sortStr = "reviewNum";
				}else if(CommentUtils.CASETYPE_BUSINESS_COLLECT_BOOKLIST.equals(tag2.getTagGroup())){
					sortStr = "collectNum";
				}
				map.put("sort", sortStr);
				map.put("enum", tag2.getTagGroup());
				map.put("title", tag2.getTagName());
				typeList.add(map);
			}
		}*/
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("sort", sort);
			datas.put("tagList", resInfos);
			datas.put("typeList", typeList);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	public String getArticlesByIds(Map<String, Object> retJson){
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = "";
		String flag = CommentUtils.RES_FLAG_SUCCESS;//0：成功、1：失败
		String error="";
		int flagint = 0;
		if(retJson==null || retJson.size()==0){
			flag = CommentUtils.RES_FLAG_SUCCESS;//0：成功、1：失败
			datas.put("flag", flag);
			//处理返回数据
			resString = getResponseData(datas);
			return resString;
		}
		flag = (String) retJson.get("flag");
		List<ResInfo> resInfos = null;
		if(CommentUtils.RES_FLAG_SUCCESS.endsWith(flag)){
			List<Map<String,Object>> maps = (List<Map<String,Object>>)retJson.get("subjects");
			if(maps!=null && maps.size()>0){
				List<Long> articleids = new ArrayList<Long>(maps.size());
				for(int i=0;i<maps.size();i++){
					String articleidstr = maps.get(i).get("id")+"";
					if(StringUtils.isInteger(articleidstr)){
						articleids.add(Long.valueOf(articleidstr));
					}
				}
				if(articleids.size()>0){
					resInfos = new ArrayList<ResInfo>(articleids.size());
					List<Article> articles = articleFacade.findArticlesByIds(articleids);
					if(articles!=null && articles.size()>0 && articles.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
						//需要查询出文章所属的用户信息
						List<Long> uids = new ArrayList<Long>();
						for(int j=0;j<articles.size();j++){
							uids.add(articles.get(j).getUid());
						}
						List<UserInfo> userinfos = null;
						try{
							userinfos = ucenterFacade.findUserListByUseridList(null, uids);
						}catch(Exception e){
							LOG.error(e.getMessage(),e.fillInStackTrace());
						}
						for(int i=0;i<articleids.size();i++){
							for(int j=0;j<articles.size();j++){
								if(articleids.get(i)==articles.get(j).getId()){
									ResInfo resInfo = ResInfo.convert(articles.get(j));
									if(userinfos!=null && userinfos.size()>0){
										for(int z=0;z<userinfos.size();z++){
											if(userinfos.get(z).getUserId()==resInfo.getUserid()){
												UserInfo userInfo = userinfos.get(z);
												UserEntity userEntity = fileUtils.copyUserInfo(userInfo, TRUE);
												resInfo.setUserEntity(userEntity);
												//resInfo.setHeadImage(userInfo.getFaceAddress());
												//resInfo.setAuthorName(userInfo.getName());
												//resInfo.setSex(userInfo.getSex());
												//resInfo.setAge(userInfo.getAge());
												//resInfo.setUserType(userInfo.getLevel());
												break;
											}
										}
									}
									resInfos.add(resInfo);
									articles.remove(j);
									break;
								}
							}
						}
					}
				}else{
					new ArrayList<ResInfo>(0);
				}
			}else{
				new ArrayList<ResInfo>(0);
			}
		}else{
			flagint = ResultUtils.QUERY_ERROR;
		}
		
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
//			datas.put("sort", sort);
			if(resInfos==null){
				resInfos = new ArrayList<ResInfo>(0);
			}
			datas.put("tagList", resInfos);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			flag = CommentUtils.RES_FLAG_ERROR;
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	//获取用户常用标签
	public List<String> getUserTagList(long uid){
		List<String> Tags = new ArrayList<String>();
		List<UserTag> userTags = myBkFacade.findUserFavoriteTagListByUid(uid);
		
		Tags = getUserTagStringList(userTags, Tags);
//		Collections.sort(userTagInfos);
		
		return Tags;
	}
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<UserTagInfo> getUserTagList(List<UserTag> reqList , List<UserTagInfo> resList){
		UserTagInfo tagInfo = null;
		if(reqList.size()>0){
			UserTag object = reqList.get(0);
			if(object.getId() != UNID){
				flagint = ResultUtils.SUCCESS;
				for (UserTag obj : reqList) {
					tagInfo = tagUtils.putUserTagToInfo(obj);
					resList.add(tagInfo);
				}
			}else{
				flagint = ResultUtils.DATAISNULL;
			}
			
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<String> getUserTagStringList(List<UserTag> reqList , List<String> resList){
		if(reqList.size()>0){
			UserTag object = reqList.get(0);
			if(object.getId() != UNID){
				flagint = ResultUtils.SUCCESS;
				for (UserTag obj : reqList) {
					
					resList.add(obj.getTagName());
				}
			}else{
				flagint = ResultUtils.DATAISNULL;
			}
			
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List<String> getTagStringList(List<Tag> reqList , List<String> resList){
		if(reqList.size()>0){
			Tag object = reqList.get(0);
			if(object.getId() != UNID){
				flagint = ResultUtils.SUCCESS;
				for (Tag obj : reqList) {
					if(obj.getIsTop() != 0){
						resList.add(obj.getTagName());
					}
				}
			}else{
				flagint = ResultUtils.DATAISNULL;
			}
			
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}
		return resList;
	}

	/**
	 * 查询小说的类型
	 * @param reqList
	 * @return
	 */
	public List<Map<String,Object>> getStoryTagStringList(List<Tag> reqList , List<Map<String,Object>> resList){
		if(reqList.size()>0) {
			for (Tag obj : reqList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", obj.getTagName());
				map.put("imageUrl", obj.getCover());
				resList.add(map);
			}
		}
		return resList;
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
	
	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}
	public void setPopChartsFacade(PopChartsFacade popChartsFacade) {
		this.popChartsFacade = popChartsFacade;
	}
	public void setBookListManager(BookListManager bookListManager) {
		this.bookListManager = bookListManager;
	}
	public void setMovieListManager(MovieListManager movieListManager) {
		this.movieListManager = movieListManager;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}
	public void setNetBookFacade(NetBookFacade netBookFacade) {
		this.netBookFacade = netBookFacade;
	}
	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}
	public void setTopicFacade(TopicFacade topicFacade) {
		this.topicFacade = topicFacade;
	}
	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}
	public void setRankingManager(RankingManager rankingManager) {
		this.rankingManager = rankingManager;
	}
	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
}
