package com.keel.common.cache.redis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.common.cache.redis.JedisWorker.JedisWorkerRuntimeException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisSimpleClient {
	private static final  Log LOG = LogFactory.getLog(JedisSimpleClient.class);
	
	private static final int MAX_TRY_FOR_FIND_REDIS_CONN = 10;
	
	private JedisPool jedisPool;

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	@PostConstruct
	private void afterPropertiesSet(){
		if (null == this.jedisPool){
			throw new IllegalArgumentException(
					String
							.format("JedisSimpleClient need jedis pool!"));
		}
	}

	@PreDestroy
	private void destroy(){
		this.jedisPool.destroy();
	}
	
	/**
	 * 注意：
	 * 1. Jedis非线程安全；
	 * 2. 使用完注意归还资源；
	 * */
	public Jedis findWorkingRedisConn(){
		Jedis j = null;
		
		for(int i = 0; i < JedisSimpleClient.MAX_TRY_FOR_FIND_REDIS_CONN; i++){
			try {
				j = this.jedisPool.getResource();
				if(j.isConnected()){
					return j;
				} else {
					this.returnBrokenRedisConn(j);
				}
			} catch(JedisConnectionException e){
				if(j != null) {
					this.returnBrokenRedisConn(j);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 将连接返回池子
	 * */
	public void returnRedisConn(Jedis j){
		if(null != this.jedisPool) {
			this.jedisPool.returnResource(j);
		}
	}
	
	/**
	 * 摧毁无效链接
	 * */
	public void returnBrokenRedisConn(Jedis j){
		if(null != this.jedisPool){
			this.jedisPool.returnBrokenResource(j);
		}
	}
	
	/**
	 * 一个方便的Jedis执行模板，只需要关注业务即可！
	 * */
	public <T> T execute(JedisWorker<T> work) throws NullJedisConnRuntimeException{
		Jedis j = null;
		T ret = null;
		
		try {
			j = this.findWorkingRedisConn();
			if(null == j) {
				throw new NullJedisConnRuntimeException("Can not obtain a jedis connection!");
			}
			ret  = work.work(j);
		} catch(NullJedisConnRuntimeException e){
			throw e;
	    } catch(JedisWorkerRuntimeException e) {
			// 如果是work中业务异常，返回redis连接
			this.returnRedisConn(j);
			j = null;
			LOG.info(e.getMessage(), e.fillInStackTrace());
		} catch(Exception e) {
			// 如果是其他异常，为了清理环境，毁掉连接
			// FIXME：有误伤！ 其实可以细分，list出访问redis可能出现的所有异常
			this.returnBrokenRedisConn(j);
			j = null;
			LOG.info(e.getMessage(), e.fillInStackTrace());
		} finally {
		    if(j != null){
		    	this.returnRedisConn(j);
		    	j = null;
		    }
		}
		
		return ret;
	}
	
	public class NullJedisConnRuntimeException extends RuntimeException {
		private static final long serialVersionUID = -4392275148172275083L;

		public NullJedisConnRuntimeException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public NullJedisConnRuntimeException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}
	}
}
