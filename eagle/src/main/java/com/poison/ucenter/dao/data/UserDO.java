package com.poison.ucenter.dao.data;

import com.keel.common.lang.BaseDO;

public class UserDO extends BaseDO {

	private static final long serialVersionUID = -6823690106526787618L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
