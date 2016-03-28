package com.keel.common.cache.redis;

import redis.clients.jedis.Jedis;

public interface JedisWorker<T> {
	
	public T work(Jedis jedis);
	
	public class JedisWorkerRuntimeException extends RuntimeException {
		private static final long serialVersionUID = -3501784555827319764L;

		public JedisWorkerRuntimeException(String message, Throwable cause) {
			super(message, cause);
			// TODO Auto-generated constructor stub
		}

		public JedisWorkerRuntimeException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}
	}
}
