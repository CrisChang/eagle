package com.poison.eagle.utils.resourceinfo;

import com.keel.utils.time.TimeUtils;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.eagle.utils.story.StoryUtils;
import com.poison.story.model.StoryComment;
import com.poison.ucenter.client.StoryUserFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.StoryUser;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;

/**
 * 小说评论返回信息实体 创建类
 * @author songdan
 * @date 2016年3月2日
 * @Description TODO
 * @version V1.0
 */
public class StoryCommentResourceInfoCreator extends AbstractResourceInfoCreator{

	private StoryUserFacade storyUserFacade;
	
	public StoryCommentResourceInfoCreator() {
		super();
	}
	public StoryCommentResourceInfoCreator(StoryUserFacade storyUserFacade) {
		super();
		this.storyUserFacade = storyUserFacade;
	}

	@Override
	public ResourceInfo create(Object source) {
		if (StoryComment.class.isInstance(source)) {
			StoryComment storyComment = (StoryComment) source;
			ResourceInfo info = new ResourceInfo();
			info.setRid(storyComment.getId());
			info.setType(CommentUtils.TYPE_STORY_COMMENT);
			info.setTitle(storyComment.getTitle());
			info.setContList(StoryUtils.str2Data(storyComment.getCommentBody()));
			info.setBtime(TimeUtils.Date2String(storyComment.getCreateDate()));
			Long uid = storyComment.getUserId();
			if (uid != null && uid != 0 && storyUserFacade != null) {
				StoryUser ui = storyUserFacade.findStoryUserByUserid(uid);
				info.setUserEntity(copyUserInfo(ui));
			}
			return info;
		} else {
			throw new IllegalArgumentException("参数错误，type必须是" + StoryComment.class.getName());
		}
	}

	protected UserEntity copyUserInfo(StoryUser userInfo) {
		UserEntity entity = new UserEntity();
		entity.setId(userInfo.getUserId());
		entity.setNickName(userInfo.getName());
		entity.setFace_url(userInfo.getFaceAddress());
		return entity;
	}

}
