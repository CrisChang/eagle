package com.poison.act.service.impl;

import com.poison.act.domain.repository.ActVoteDomainRepository;
import com.poison.act.model.ActRecommendVote;
import com.poison.act.service.ActVoteService;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 16:02
 */
public class ActVoteServiceImpl implements ActVoteService{

    private ActVoteDomainRepository actVoteDomainRepository;

    public void setActVoteDomainRepository(ActVoteDomainRepository actVoteDomainRepository) {
        this.actVoteDomainRepository = actVoteDomainRepository;
    }

    /**
     * 插入推荐票
     * @param actRecommendVote
     * @return
     */
    @Override
    public int insertRecommendVote(ActRecommendVote actRecommendVote) {
        return actVoteDomainRepository.insertRecommendVote(actRecommendVote);
    }
}
