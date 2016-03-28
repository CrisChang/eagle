package com.poison.resource.dao;

import com.poison.resource.model.OperationSelected;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/9/15
 * Time: 14:19
 */
public interface OperationSelectedDAO {

    /**
     * 查询运营中间的id
     * @param firstIndex
     * @param secondIndex
     * @param pageSize
     * @param timeSeparation
     * @return
     */
    public List<OperationSelected> findOperationSelectedByMiddle(int firstIndex,int secondIndex,int pageSize,long timeSeparation);

    /**
     * 查询大于id的运营精选
     * @param bigIndex
     * @return
     */
    public List<OperationSelected> findOperationSelectedOrderId(int bigIndex);


    /**
     * 顺序查找最新的十条id
     * @return
     */
    public List<OperationSelected> findOperationSelectedByIdOrderDesc();
}
