package com.poison.act.dao.impl;

import com.poison.act.dao.ActRecommendVoteDAO;
import com.poison.act.model.ActRecommendVote;
import com.poison.eagle.utils.ResultUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 16/1/12
 * Time: 15:41
 */
public class ActRecommendVoteDAOImpl extends SqlMapClientDaoSupport implements ActRecommendVoteDAO{

    private static final Log LOG = LogFactory.getLog(ActRecommendVoteDAOImpl.class);

    /**
     * 插入推荐票
     * @param actRecommendVote
     * @return
     */
    @Override
    public int insertRecommendVote(ActRecommendVote actRecommendVote) {
        int flag = ResultUtils.ERROR;
        try{
            getSqlMapClientTemplate().insert("insertRecommendVote",actRecommendVote);
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            flag = ResultUtils.ERROR;
        }
        return flag;
    }

    /**
     * 查找最后一个推荐
     * @param userId
     * @param resourceId
     * @return
     */
    @Override
    public ActRecommendVote findRecommendVoteByUserIdAndResourceId(long userId, long resourceId) {
        ActRecommendVote actRecommendVote = new ActRecommendVote();
        int flag = ResultUtils.ERROR;
        try{
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("userId",userId);
            map.put("resourceId",resourceId);
            actRecommendVote = (ActRecommendVote)getSqlMapClientTemplate().queryForObject("findRecommendVoteByUserIdAndResourceId",map);
            if(null==actRecommendVote){
                actRecommendVote = new ActRecommendVote();
                actRecommendVote.setFlag(ResultUtils.DATAISNULL);
                return actRecommendVote;
            }
            flag = ResultUtils.SUCCESS;
        }catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.getMessage(),e.fillInStackTrace());
            actRecommendVote = new ActRecommendVote();
            flag = ResultUtils.ERROR;
        }
        actRecommendVote.setFlag(flag);
        return actRecommendVote;
    }
}
