package com.keel.common.es;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ElasticsearchClientFactory implements FactoryBean<Client>,	InitializingBean, DisposableBean {
	private static final  Log LOG = LogFactory.getLog(ElasticsearchClientFactory.class);
	
	private Client client = null;
	
	//集群名称
	private String clusterName = null;
	//你可以设置client.transport.sniff为true来使客户端去嗅探 整个集群的状态,默认是true
	private boolean transportSniff = true;
	//ping节点的超时
	private String pingTimeout = "5s";
	//多长时间监测仪测节点是否工作正常
	private String nodesInterval = "5s";
	
	private String[] nodesAddress = null;
	

	@Override
	public Client getObject() throws Exception {
		return client;
	}

	@Override
	public Class<?> getObjectType() {
		return Client.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		LOG.info("starting ES client...");
		client = buildClient();
	}

	@Override
	public void destroy() throws Exception {
		try {
			LOG.info("Closing ES client");
			if (client != null) {
				client.close();
			}
		} catch (final Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
	}

	private Client buildClient() {
        if ( StringUtils.isBlank(clusterName)){
			throw new IllegalArgumentException(String.format(
					"ES node needs cluster name!"));
        }

		Settings settings = ImmutableSettings.settingsBuilder()
		 .put("cluster.name", clusterName)
	     .put("client.transport.sniff", transportSniff)
	     .put("client.transport.ping_timeout", pingTimeout)
	     .put("client.transport.nodes_sampler_interval", nodesInterval).build();
		 TransportClient client = new TransportClient(settings);
		  if(nodesAddress != null){
			for (int i = 0; i < nodesAddress.length; i++) {
				client.addTransportAddress(getAddressObject(nodesAddress[i]));
			}
		  }

		return client;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public boolean isTransportSniff() {
		return transportSniff;
	}

	public void setTransportSniff(boolean transportSniff) {
		this.transportSniff = transportSniff;
	}

	public String getPingTimeout() {
		return pingTimeout;
	}

	public void setPingTimeout(String pingTimeout) {
		this.pingTimeout = pingTimeout;
	}

	public String getNodesInterval() {
		return nodesInterval;
	}

	public void setNodesInterval(String nodesInterval) {
		this.nodesInterval = nodesInterval;
	}

	public String[] getNodesAddress() {
		return nodesAddress;
	}

	public void setNodesAddress(String[] nodesAddress) {
		this.nodesAddress = nodesAddress;
	}
	
	/***
	 * 将IP和端口号拆分
	 * SPRING中的配置，按照ip:9300;ip:9200;...
	 */
	private InetSocketTransportAddress getAddressObject(String address) {
		if (address == null) return null;
		
		String[] splitted = address.split(":");
		int port = 9300;
		if (splitted.length > 1) {
			port = Integer.parseInt(splitted[1]);
		}
		return new InetSocketTransportAddress(splitted[0], port);
	}
}
