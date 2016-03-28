package com.poison.eagle.manager;

import com.keel.common.event.rocketmq.MessageRecv;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.story.client.StoryFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/3/1
 * Time: 12:19
 */
public class StoryPushManager implements MessageRecv {

    private static final Log LOG = LogFactory.getLog(StoryPushManager.class);
    private PushManager pushManager;
    private StoryFacade storyFacade;

    public void setStoryFacade(StoryFacade storyFacade) {
        this.storyFacade = storyFacade;
    }

    public void setPushManager(PushManager pushManager) {
        this.pushManager = pushManager;
    }

    @Override
    public ConsumeStatus recv(String topic, String tags, String key, String body) {
        //System.out.println("收到小说推送");
        JSONObject json = null;
        Long storyId=0l;
        Long chapterId=0l;
        String chapterTitle="";
        String pushType="";
        String pushContext = "";
        try {
            json = new JSONObject(body);
            storyId = json.getLong("storyId");
            chapterId = json.getLong("chapterId");
            chapterTitle = json.getString("chapterTitle");
            pushType = json.getString("pushType");
        } catch (JSONException e) {
            LOG.error(e.getMessage(), e.fillInStackTrace());
            pushContext = "";
        }

        List<Long> userIdList = storyFacade.findStoryShelfByStoryId(storyId);
        Iterator<Long> userIdListIt = userIdList.listIterator();
        long userId = 0;
        while (userIdListIt.hasNext()){//用户id列表
            userId = userIdListIt.next();
            //System.out.println("推送的小说id为"+userId);
            pushManager.jpushStoryMSG(userId,storyId,"40",PushManager.PUSH_UPDATE_CHAPTER,chapterTitle);
        }


//        if(PushManager.PUSH_UPDATE_CHAPTER.equals(pushType)){//更新章节的通知
//            pushManager.jpushResourceMSG();
//        }

        return MessageRecv.ConsumeStatus.CONSUME_SUCCESS;
    }
}
