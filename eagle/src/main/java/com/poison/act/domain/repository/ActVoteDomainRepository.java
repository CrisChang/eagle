package com.poison.act.domain.repository;

import com.poison.act.dao.ActRecommendVoteDAO;
import com.poison.act.model.ActRecommendVote;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.ResultUtils;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 15:52
 */
public class ActVoteDomainRepository {

    private ActRecommendVoteDAO actRecommendVoteDAO;

    public void setActRecommendVoteDAO(ActRecommendVoteDAO actRecommendVoteDAO) {
        this.actRecommendVoteDAO = actRecommendVoteDAO;
    }

    /**
     * 插入推荐票信息
     * @param actRecommendVote
     * @return
     */
    public int insertRecommendVote(ActRecommendVote actRecommendVote){
        //查询最后一次推荐
        ActRecommendVote recommendVote = actRecommendVoteDAO.findRecommendVoteByUserIdAndResourceId(actRecommendVote.getUserId(),actRecommendVote.getResourceId());
        int flag = recommendVote.getFlag();
        if(flag == ResultUtils.SUCCESS){//当有记录时
            //上次时间
            long latestTime = recommendVote.getLatestRevisionDate();
            //当前时间
            long systime = System.currentTimeMillis();
            if(DateUtil.compareDateNow(latestTime,systime)){//一天只能投一次票
                flag = ResultUtils.EXISTED_IS_VOTE;
            }else{
                flag = ResultUtils.DATAISNULL;
            }
        }

        if(flag == ResultUtils.DATAISNULL){
            flag = actRecommendVoteDAO.insertRecommendVote(actRecommendVote);
        }
        return flag;
    }
}
