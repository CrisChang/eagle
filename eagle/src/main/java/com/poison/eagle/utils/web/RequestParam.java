package com.poison.eagle.utils.web;

import java.util.Map;
/**
 * 请求参数封装对象
 * @author songdan
 * @date 2016-3-1
 * @Description TODO
 * @version V1.0
 */
public class RequestParam{
	private RequestBody req;
	
	public RequestBody getReq() {
		return req;
	}
	public void setReq(RequestBody req) {
		this.req = req;
	}
	
	public Map<String, Object> getData(){
		return req.getData();
	}
	static class RequestBody{
		private Map<String, Object> data;

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(Map<String, Object> data) {
			this.data = data;
		}
	}
	
}

