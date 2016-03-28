package com.poison.eagle.utils.resourceinfo;

import com.poison.eagle.entity.ResourceInfo;
import com.poison.ucenter.client.UcenterFacade;

/**
 * ResourceInfo 创建接口
 * @author songdan
 * @date 2016年3月2日
 * @Description TODO
 * @version V1.0
 */
public interface ResourceInfoCreator {
	/**
	 * 创建ResourceInfo
	 * @param source
	 * @return
	 */
	ResourceInfo create(Object source);
}
