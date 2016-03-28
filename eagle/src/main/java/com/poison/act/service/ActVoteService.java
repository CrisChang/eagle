package com.poison.act.service;

import com.poison.act.model.ActRecommendVote;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 16:00
 */
public interface ActVoteService {

    /**
     * 插入推荐票
     * @param actRecommendVote
     * @return
     */
    public int insertRecommendVote(ActRecommendVote actRecommendVote);
}
