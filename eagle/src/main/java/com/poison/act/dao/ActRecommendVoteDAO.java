package com.poison.act.dao;

import com.poison.act.model.ActRecommendVote;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 15:37
 */
public interface ActRecommendVoteDAO {

    /**
     * 插入推荐票
     * @param actRecommendVote
     * @return
     */
    public int insertRecommendVote(ActRecommendVote actRecommendVote);

    /**
     *
     * @param userId
     * @param resourceId
     * @return
     */
    public ActRecommendVote findRecommendVoteByUserIdAndResourceId(long userId,long resourceId);
}
