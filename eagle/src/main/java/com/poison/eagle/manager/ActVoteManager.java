package com.poison.eagle.manager;

import com.poison.act.client.ActVoteFacade;
import com.poison.act.model.ActCollect;
import com.poison.eagle.utils.*;
import com.poison.story.client.StoryFacade;
import com.poison.story.client.StoryStatisticFacade;
import com.poison.story.model.Story;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 16:17
 */
public class ActVoteManager extends BaseManager {

    private static final Log LOG = LogFactory.getLog(ActVoteManager.class);

    private ActVoteFacade actVoteFacade;
    private StoryStatisticFacade storyStatisticFacade;
    private StoryFacade storyFacade;

    public void setStoryFacade(StoryFacade storyFacade) {
        this.storyFacade = storyFacade;
    }

    public void setStoryStatisticFacade(StoryStatisticFacade storyStatisticFacade) {
        this.storyStatisticFacade = storyStatisticFacade;
    }

    public void setActVoteFacade(ActVoteFacade actVoteFacade) {
        this.actVoteFacade = actVoteFacade;
    }

    public String recommendVote(String reqs,Long uid){
        Map<String, Object> req ;
        Map<String, Object> dataq;
        Map<String, Object> datas ;
        String resString;//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error;
//		LOG.info("客户端json数据："+reqs);
        long id = 0;
        String type = "";
        int status = 0;

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            id = Long.valueOf(dataq.get("id").toString());
            type = CheckParams.objectToStr(dataq.get("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        int flagint = actVoteFacade.insertRecommendVote(uid,id,type,1);

        datas = new HashMap<String, Object>();
        if(flagint == ResultUtils.SUCCESS){
            //收藏数量
            Story story =storyFacade.findStoryById(id);
            //int fNum = actFacade.findCollectCount(actCollect.getResourceId());
            //增加小说的推荐票
            storyStatisticFacade.addRecommendVoteByResourceId(story.getId(),story.getChannel(),story.getType());
            flag = CommentUtils.RES_FLAG_SUCCESS;
            //datas.put("num", fNum);
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
