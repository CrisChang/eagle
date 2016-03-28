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
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.BigFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.GetResourceInfoFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.SerializeListFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.Article;
import com.poison.resource.model.BigLevelValue;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.Post;
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
public class IndexManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(IndexManager.class);
	
	private int flagint;
	
	private ResourceManager resourceManager;
	private ResourceLinkManager resourceLinkManager;
	
	
	private GetResourceInfoFacade getResourceInfoFacade;
	private MyMovieFacade myMovieFacade;
	private PostFacade postFacade;
	private ArticleFacade articleFacade;
	
	private ActUtils actUtils = ActUtils.getInstance();
	
	/**
	 * 运营首页
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String index(String reqs,final Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		int flag_int = 0;
		String type = null;
		//去掉空格
		reqs = reqs.trim();
		List<Map<String, String>> idList = new ArrayList<Map<String,String>>();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			idList = (List<Map<String, String>>) dataq.get("idList");
			try {
				type = (String) dataq.get("type");
			} catch (Exception e) {
//				type = CommentUtils.TYPE_ARTICLE;
				type = null;
			}
			if("0".equals(type) || "".equals(type)){
				type = null;
			}
//			if(type == null ){
//				type = CommentUtils.TYPE_ARTICLE;
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		
		
		List<ResourceInfo> resoureList = new ArrayList<ResourceInfo>();
		
//		if(id == UNID){
//			id = null;
//		}
		
		
		long begin = System.currentTimeMillis();
		List<Object> objects = new ArrayList<Object>();
		if(idList != null && idList.size()==0){
			//获取推荐给用户的资源
			objects = resourceManager.getListFromSearch(uid,type);
		}else if(idList != null && idList.size()>0){
			Iterator<Map<String, String>> iter = idList.iterator();
			while(iter.hasNext()){
				Map<String, String> map = iter.next();
				Long id = Long.valueOf(map.get("id"));
				String resType = map.get("type");
				if(CommentUtils.TYPE_BOOKLIST.equals(resType)){
					BookList bookList = getResourceInfoFacade.queryByIdBookList(id);
					if(bookList.getId() != 0){
						objects.add(bookList);
					}
				}else if(CommentUtils.TYPE_MOVIELIST.equals(resType)){
					MovieList movieList = myMovieFacade.findMovieListById(id);
					if(movieList.getId() != 0){
						objects.add(movieList);
					}
				}else if(CommentUtils.TYPE_ARTICLE.equals(resType)){
					Post post = postFacade.queryByIdName(id);
					if(post.getId() != 0){
						objects.add(post);
					}
				}else if(CommentUtils.TYPE_NEWARTICLE.equals(resType)){
					Article article = articleFacade.queryArticleById(id);
					if(article.getId() != 0){
						objects.add(article);
					}
				}
			}
		}
		
		//如果查出来是空，进行查库
		if(resoureList != null && resoureList.size()==0){
			
			//查询全部信息
//			resoureList = getResoureListByPage(null,id,uid,resoureList);
			
			resoureList = resourceManager.getResponseList(objects, uid, resoureList);
		}
		
		
		//对资源进行倒序排列
//		Collections.sort(resoureList);
		
//		if(resoureList.size()>CommentUtils.RESOURCE_PAGE_SIZE){
//			resoureList = resoureList.subList(0, CommentUtils.RESOURCE_PAGE_SIZE);
//		}
		int index =0;
		//运营的数据
		List<ResourceInfo> resourceInfos = resourceLinkManager.getResourcesFromLink(0l, CommentUtils.CASETYPE_BUSINESS_INDEX, uid,"",null,0);
		if(resourceInfos.size()>0){
			index = resourceManager.getIndexPopularizeCount(1);
			//如果index大于list数量值为零 
			if(index >resourceInfos.size()){
				index = resourceManager.getIndexPopularizeCount(0);
			}
			//将推广数据放入list中
			ResourceInfo resourceInfo = resourceInfos.get(index-1);
			if(resourceInfo != null && resourceInfo.getRid() != 0){
				resourceInfo.setFlag(2);
				resoureList.add(resourceInfo);
			}
		}
		
		resourceInfos = new ArrayList<ResourceInfo>();
		if(resoureList.size()>0){
			Iterator<ResourceInfo> iter = resoureList.iterator();
			int size =1;
			while(size >0){
				size = resoureList.size();
				index = (int)(Math.random()*size);
				ResourceInfo ri = resoureList.get(index);
				if(ri != null && ri.getRid() != 0){
					resourceInfos.add(ri);
					resoureList.remove(ri);
				}
				size = resoureList.size();
//				if(size == 0){
//					break;
//				}
			}
		}
		
		//每条信息截取100字
		actUtils.subStringResourceListContent(resourceInfos, CommentUtils.RESOURCE_CONTENT_SIZE_INDEX);
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//去掉客户端不需要的字段的值，为了节省流量
			removeRedundantData(resourceInfos);
			datas.put("list", resourceInfos);
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
		
		long end = System.currentTimeMillis();
//		System.out.println("调用首页所需时间"+(end - begin));
//		LOG.info("调用首页所需时间"+(end - begin));
		return resString;
	}
	//去掉多余的数据，客户端不需要的,为了节省流量
	private void removeRedundantData(List<ResourceInfo> resourceInfos){
		try{
			if(resourceInfos!=null && resourceInfos.size()>0){
				for(int i=0;i<resourceInfos.size();i++){
					resourceInfos.get(i).setSummary("");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	public void setResourceLinkManager(ResourceLinkManager resourceLinkManager) {
		this.resourceLinkManager = resourceLinkManager;
	}

	public void setGetResourceInfoFacade(GetResourceInfoFacade getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}

	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}

	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}
	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}
}
