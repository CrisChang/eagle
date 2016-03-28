package com.keel.framework.web.manager;

import com.keel.framework.runtime.ProductUser;

public class UserManager implements IUserManager {

	@Override
	public ProductUser autoLogin(long userId) {
		ProductUser user = null;
		if(userId == 123456L){
			user = new ProductUser();
			user.setUserId("123456");
		}
		
		return user;
	}

}
