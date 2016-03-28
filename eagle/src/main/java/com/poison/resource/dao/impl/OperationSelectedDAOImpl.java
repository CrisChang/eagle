package com.poison.resource.dao.impl;

import com.poison.resource.dao.OperationSelectedDAO;
import com.poison.resource.model.OperationSelected;
import com.poison.resource.model.Selected;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/9/15
 * Time: 14:24
 */
public class OperationSelectedDAOImpl extends SqlMapClientDaoSupport implements OperationSelectedDAO{

    private static final Log LOG = LogFactory.getLog(OperationSelectedDAOImpl.class);

    /**
     * 查询运营中间的id
     * @param firstIndex
     * @param secondIndex
     * @param pageSize
     * @param timeSeparation
     * @return
     */
    @Override
    public List<OperationSelected> findOperationSelectedByMiddle(int firstIndex, int secondIndex, int pageSize, long timeSeparation) {
        List<OperationSelected> selectList = new ArrayList<OperationSelected>();
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            map.put("firstIndex", firstIndex);
            map.put("secondIndex", secondIndex);
            map.put("pageSize", pageSize);
            map.put("timeSeparation",timeSeparation);
            selectList = getSqlMapClientTemplate().queryForList("findOperationSelectedByMiddle",map);
            if(null==selectList){
                selectList = new ArrayList<OperationSelected>();
            }
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            selectList = new ArrayList<OperationSelected>();
        }
        return selectList;
    }

    /**
     * 查询最新的id
     * @param bigIndex
     * @return
     */
    @Override
    public List<OperationSelected> findOperationSelectedOrderId(int bigIndex) {
        List<OperationSelected> selectList = new ArrayList<OperationSelected>();
        try{
            selectList = getSqlMapClientTemplate().queryForList("findOperationSelectedOrderId",bigIndex);
            if(null==selectList){
                selectList = new ArrayList<OperationSelected>();
            }
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            selectList = new ArrayList<OperationSelected>();
        }
        return selectList;
    }

    /**
     * 查询最新的精选
     * @return
     */
    @Override
    public List<OperationSelected> findOperationSelectedByIdOrderDesc() {
        List<OperationSelected> selectedList = new ArrayList<OperationSelected>();
        try{
            selectedList = getSqlMapClientTemplate().queryForList("findOperationSelectedByIdOrderDesc");
            if(null==selectedList){
                selectedList = new ArrayList<OperationSelected>();
            }
        }catch (Exception e) {
            LOG.error(e.getMessage(),e.fillInStackTrace());
            selectedList = new ArrayList<OperationSelected>();
        }
        return selectedList;
    }
}
