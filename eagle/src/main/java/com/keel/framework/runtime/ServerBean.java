package com.keel.framework.runtime;

import com.keel.utils.net.LocalIPUtils;

public final class ServerBean {
	
	private String ip;
	private String serverName;

	public ServerBean(String serverName) {
		super();
		this.serverName = serverName;
		
		this.ip = LocalIPUtils.getIp4Single();
	}

	public String getIp() {
		return ip;
	}

	public String getServerName() {
		return serverName;
	}
}
