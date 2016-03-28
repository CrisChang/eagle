package com.poison.act.client.impl;

import com.keel.utils.UKeyWorker;
import com.poison.act.client.ActVoteFacade;
import com.poison.act.model.ActRecommendVote;
import com.poison.act.service.ActVoteService;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 16:07
 */
public class ActVoteFacadeImpl implements ActVoteFacade {

    private ActVoteService actVoteService;
    private UKeyWorker reskeyWork;

    public void setActVoteService(ActVoteService actVoteService) {
        this.actVoteService = actVoteService;
    }

    public void setReskeyWork(UKeyWorker reskeyWork) {
        this.reskeyWork = reskeyWork;
    }

    /**
     * 插入推荐票信息
     * @param userId
     * @param resourceId
     * @param resourceType
     * @param recommendVote
     * @return
     */
    @Override
    public int insertRecommendVote(long userId, long resourceId, String resourceType, int recommendVote) {
        ActRecommendVote actRecommendVote = new ActRecommendVote();
        actRecommendVote.setId(reskeyWork.getId());
        actRecommendVote.setUserId(userId);
        actRecommendVote.setResourceId(resourceId);
        actRecommendVote.setResourceType(resourceType);
        actRecommendVote.setRecommendVote(recommendVote);
        long systime = System.currentTimeMillis();
        actRecommendVote.setCreateDate(systime);
        actRecommendVote.setLatestRevisionDate(systime);
        return actVoteService.insertRecommendVote(actRecommendVote);
    }
}
