package com.poison.eagle.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.common.lang.BaseDO;

public class ResourceGroupInfo extends BaseDO implements Serializable {
	/**
	 * serialVersionUID = -8236557556314302627L;
	 */
	private static final long serialVersionUID = -8236557556314302627L;
	private static final Log LOG = LogFactory.getLog(ResourceGroupInfo.class);

	private String month;
	private List<ResourceInfo> resourceInfos;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<ResourceInfo> getResourceInfos() {
		return resourceInfos;
	}

	public void setResourceInfos(List<ResourceInfo> resourceInfos) {
		this.resourceInfos = resourceInfos;
	}

}
