package com.keel.framework.runtime;

import java.util.ArrayList;
import java.util.List;

import com.keel.common.lang.BaseDO;


public class ProductEnvironment extends BaseDO {
	private static final long serialVersionUID = 5836345100546060704L;
	
	public static final long CLINET_NONE = 0x0L;
	public static final long CLIENT_PC = 0x1L;
	public static final long CLINET_IOS = 0x100L;
	public static final long CLINET_ANDROID = 0x10000L;
	
	//客户端IP
	private String clientIP;
	
	//客户端操作系统
	private String clientOS;
	
	//客户端版本号
	private String clinetVersion;
	
	//当前服务{ip, serverName}
	private String [] server;
	
    //服务链路
    private List<String[]> serverStack;
	
	//用户使用的代理服务器IP，如果没有使用代理服务器proxyIP=null.
	private String proxyIP;
	//用户使用的代理服务器端口，如果没有使用代理服务器proxyPort=null.
	private String proxyPort;
	
	//客户端唯一标识
	private String uuid;
	
	//客户端类型
	private long  clientType;
	
    /**
     * 空构造器。
     */
    public ProductEnvironment() {
        super();
    }

    /**
     * 构造器。
     */
    public ProductEnvironment(String clientIp, String serverIp, String serverName) {
        super();

        this.clientIP = clientIp;
        
        this.server = new String[]{serverIp, serverName};
    }
    
    /**
     * 用户是否使用代理服务器访问
     */
    public boolean useProxy() {
        return proxyIP == null ? false : true;
    }

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String[] getServer() {
		return this.server;
	}

	public String getProxyIP() {
		return proxyIP;
	}

	public void setProxyIP(String proxyIP) {
		this.proxyIP = proxyIP;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public long getClientType() {
		return clientType;
	}

	public void setClientType(long clientType) {
		this.clientType = clientType;
	}

	public String getClientOS() {
		return clientOS;
	}

	public void setClientOS(String clientOS) {
		this.clientOS = clientOS;
	}

	public String getClinetVersion() {
		return clinetVersion;
	}

	public void setClinetVersion(String clinetVersion) {
		this.clinetVersion = clinetVersion;
	}

	/*在处理链条上加入一个服务（IP，Service）*/
	public void addService(String ip, String serviceName) {
        if (null == this.serverStack) {
            this.serverStack = new ArrayList<String[]>();
        }
        if (null != this.server) {
            this.serverStack.add(this.server);
        }
        this.server = new String[]{ip, serviceName};
	}
}
