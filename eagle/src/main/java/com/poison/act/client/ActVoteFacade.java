package com.poison.act.client;

import com.poison.act.model.ActRecommendVote;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 16:05
 */
public interface ActVoteFacade {

    /**
     * 插入推荐票
     * @param userId
     * @param resourceId
     * @param resourceType
     * @param recommendVote
     * @return
     */
    public int insertRecommendVote(long userId,long resourceId,String resourceType
    ,int recommendVote);
}
