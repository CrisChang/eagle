package com.poison.eagle.manager; 

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.Post;
import com.poison.resource.model.Serialize;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.client.UserStatisticsFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class PostManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(PostManager.class);
	
	private int flagint;
	private PostFacade postFacade;
	private UcenterFacade ucenterFacade;
	private UserStatisticsFacade userStatisticsFacade;
	private String savePath;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private HttpUtils httpUtils = HttpUtils.getInstance();
	
	private ResourceManager resourceManager;
	/**
	 * 写长文章方法
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String writePost(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		String type = "";
		String title="";
		String imageUrl = "";
		List list = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			list = (List) dataq.get("list");
			type = (String) dataq.get("type");
			title = (String) dataq.get("title");
			imageUrl = (String) dataq.get("imageUrl");
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		LOG.info(req);
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
		
//		String content = WebUtils.HTML_CSS_FOR_ARTICLE_BEGIN+ "<div class=\"title\">"+title
//				+"</div><div class=\"author\"><span style=\"float: left;\">作者:"+userAllInfo.getName()+"&nbsp;&nbsp;"
//				+sf.format(data)+"</span><span style=\"float: right;\"><span id=\"readNum\"></span></span></div>"
//				+"<hr SIZE=1 color=\"#f0f0f0\"  /><div class=\"content\">"+WebUtils.putDataToHTML5(list)+"</div>"+WebUtils.HTML_CSS_FOR_ARTICLE_END;
		String content=WebUtils.HTML_CSS_FOR_ARTICLE_BEGIN+ "<div class=\"title\">"+title
			+"</div><div class=\"author\"><span style=\"float: left;\">作者:"+userAllInfo.getName()+"&nbsp;&nbsp;"
			+sf.format(data)+"</span><span style=\"float: right;\">阅读量:<span id=\"readNum\"></span></span></div><br/>"
			
			+"<img src=\""+imageUrl+"\" />" +
					"<hr SIZE=1 color=\"#f0f0f0\"  />" +
			"<div class=\"content content_position\">"+WebUtils.putDataToHTML5(list)+"</div>"+WebUtils.HTML_CSS_FOR_ARTICLE_END;
		String filePath = savePath+UUID.randomUUID()+".html";
		flagint = fileUtils.writeFile(filePath, content);
		Post post = new Post();
		if(flagint == ResultUtils.SUCCESS){
			String url = httpUtils.uploadFile(filePath, HttpUtils.HTTPUTIL_UPLOAD_FILE_URL_FOR_ARTICLE);
			
			String summary ="";
			for (Object object : list) {
				Map<String, String> map = (Map<String, String>) object;
				if(WebUtils.TYPE_DIV.equals(map.get(WebUtils.TYPE))){
					summary = map.get(WebUtils.DATA);
				}
			}
			post = postFacade.addPost(type, WebUtils.putStringImageVideoAudioToHTML5(title, imageUrl, null, null), url, uid, summary);//(type, WebUtils.putStringImageVideoAudioToHTML5(title, imageUrl, null, null), url, uid);	
			flagint = post.getFlag();
		}

		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS||flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//将生成的长文章放到缓存中
			resourceManager.setResourceToJedis(post, uid,uid,0l);
			
			//增加用户长文章的数量
			try {
				userStatisticsFacade.updatePostCount(uid);
			} catch (Exception e) {
				LOG.error("增加用户长文章的数量:"+e.getMessage(), e.fillInStackTrace());
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

		return resString;
	}
	/**
	 * 增加长文章阅读量
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String addAmount(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		Long id = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
//		LOG.info(req);
		
		flagint = postFacade.updatePostReadingCount(id);
		
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
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
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public void setUserStatisticsFacade(UserStatisticsFacade userStatisticsFacade) {
		this.userStatisticsFacade = userStatisticsFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
}
