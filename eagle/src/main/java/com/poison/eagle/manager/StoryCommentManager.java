package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.runtime.ProductUser;
import com.keel.utils.time.TimeUtils;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.eagle.utils.resourceinfo.StoryCommentResourceInfoCreator;
import com.poison.eagle.utils.story.StoryUtils;
import com.poison.eagle.utils.web.RequestParam;
import com.poison.story.client.IStoryCommentFacade;
import com.poison.story.client.StoryFacade;
import com.poison.story.model.Story;
import com.poison.story.model.StoryComment;
import com.poison.ucenter.client.AuthorFacade;
import com.poison.ucenter.client.StoryUserFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.StoryUser;
/**
 * 小说评论管理类
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public class StoryCommentManager extends BaseManager{
	
	private static final Log LOG = LogFactory.getLog(StoryCommentManager.class);
	
	private IStoryCommentFacade storyCommentFacade;
	
	private StoryFacade storyFacade;
	
	private AuthorFacade authorFacade;
	
	private UcenterFacade ucenterFacade;
	
	private StoryUserFacade storyUserFacade;
	
	
	
	public void setStoryUserFacade(StoryUserFacade storyUserFacade) {
		this.storyUserFacade = storyUserFacade;
	}

	public void setStoryCommentFacade(IStoryCommentFacade storyCommentFacade) {
		this.storyCommentFacade = storyCommentFacade;
	}

	public void setStoryFacade(StoryFacade storyFacade) {
		this.storyFacade = storyFacade;
	}

	public void setAuthorFacade(AuthorFacade authorFacade) {
		this.authorFacade = authorFacade;
	}
	
	
	
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}



	private FileUtils fileUtils = FileUtils.getInstance();

	/**
	 * 写入小说评论
	 * @param requestMsg
	 * @param userId
	 * @return
	 */
	public String writeStoryComment(String requestMsg, long userId) {
		try {
			RequestParam param = readValue(requestMsg, RequestParam.class);
			Map<String, Object> data = param.getData();
			StoryComment storyComment = new StoryComment();
			storyComment.setUserId(userId);
			//使用客户端数据填充评论
			fillStoryComment(storyComment,data);
			//完善小说信息
			fillStoryInfo4Comment(storyComment);
			fillUserInfo4Comment(storyComment);
			int resultFlag = storyCommentFacade.saveStoryComment(storyComment);
			Map<String, Object> datas = extractData(resultFlag);
			//处理返回数据
			String resString = getResponseData(datas);
			return resString;
		} catch (Exception e) {
			return RES_DATA_NOTGET;
		}
	}
	
	/**为评论完善用户信息*/
	private void fillUserInfo4Comment(StoryComment storyComment) {
		ProductUser productUser = ProductContextHolder.getProductContext().getProductUser();
		storyComment.setUserId(Long.parseLong(productUser.getUserId()));
		StoryUser storyUser = storyUserFacade.findStoryUserByUserid(storyComment.getUserId());
		storyComment.setNickname(storyUser.getName());
	}

	/**为评论完善小说信息*/
	private void fillStoryInfo4Comment(StoryComment storyComment) {
		if (storyComment.getStoryId()!=0) {
			Story story = storyFacade.findStoryById(storyComment.getStoryId());
			if(story!=null){
				//小说作者名称
				storyComment.setStoryAuthor(story.getAuthor());
				//小说作者id
				storyComment.setAuthorId(story.getUserId());
				//小说封面
				storyComment.setStoryCover(story.getCover());
				//小说名称
				storyComment.setStoryName(story.getName());
				//小说类型
				storyComment.setStoryType(story.getType());
			}
		}
	}

	/**
	 * 从Facade的操作结果中抽取结果信息
	 * @param resultFlag 操作结果 int
	 * @return
	 */
	private Map<String, Object> extractData(int resultFlag) {
		Map<String, Object> datas = new HashMap<String, Object>();
		String flag = "";
		String error="";
		if(resultFlag == ResultUtils.SUCCESS || resultFlag == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(resultFlag);
			LOG.error("错误代号:"+resultFlag+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		return datas;
	}


	/**用前端数据填充小说评论*/
	private void fillStoryComment(StoryComment storyComment, Map<String, Object> data) {
		//小说id
		Long storyId=CheckParams.stringToLong((String) data.get("id"));
		//评论id
		Long commentId=CheckParams.stringToLong((String) data.get("commentId"));
		//评论列表
		List<Map<String, Object>> commentList=(List<Map<String, Object>>) data.get("list");
		String content = StoryUtils.putData2Str(commentList);
		//评论标题
		String title = CheckParams.objectToStr(data.get("title"));
		//评论类型
		int type = CheckParams.objectToInt(data.get("type"));
		storyComment.setStoryId(storyId);
		storyComment.setCommentBody(content);
		storyComment.setId(commentId);
		storyComment.setTitle(title);
		storyComment.setType(type);
	}
	
	/**查看小说评论列表*/
	public String viewStoryCommentList(String requestMsg) {
		Map<String, Object> datas = new HashMap<>();
		List<ResourceInfo> comments = new ArrayList<>();
		try {
			RequestParam param = readValue(requestMsg, RequestParam.class);
			Map<String, Object> data = param.getData();
			StoryComment queryStoryComment = new StoryComment();
			//小说id
			queryStoryComment.setStoryId(CheckParams.stringToLong((String)data.get("id")));
			//起始id，取小于此id的一页数据
			long commentId = CheckParams.stringToLong((String)data.get("lastId"));
			queryStoryComment.setId(commentId==0?null:commentId);
			List<StoryComment> result = storyCommentFacade.findStoryCommentList(queryStoryComment);
			comments = extractStoryComment(result,comments);
			datas.put("list", comments);
			datas.put("flag", CommentUtils.RES_FLAG_SUCCESS);
			//处理返回数据
			String resString = getResponseData(datas);
			return resString;
		} catch (Exception e) {
			e.printStackTrace();
			return RES_DATA_NOTGET;
		}
	}

	private List<ResourceInfo> extractStoryComment(List<StoryComment> result, List<ResourceInfo> comments) {
		for (StoryComment storyComment : result) {
			ResourceInfo resultInfo = storyComment2ResultInfo(storyComment);
			comments.add(resultInfo);
		}
		return comments;
	}

	private ResourceInfo storyComment2ResultInfo(StoryComment storyComment) {
		return fileUtils.putObjectToResource(storyComment, new StoryCommentResourceInfoCreator(storyUserFacade));
	}

}
