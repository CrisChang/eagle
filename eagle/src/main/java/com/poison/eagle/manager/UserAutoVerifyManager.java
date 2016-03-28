package com.poison.eagle.manager;

import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.runtime.ProductUser;
import com.keel.framework.web.manager.IUserManager;

public class UserAutoVerifyManager implements IUserManager{

	@Override
	public ProductUser autoLogin(long userId) {
		ProductUser productUser = new ProductUser();
		productUser.setUserId(""+userId);
		return productUser;
	}

}
