package com.poison.eagle.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poison.eagle.entity.VersionInfo;
import com.poison.eagle.manager.VersionsUpdateManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;

/**
 * 
 * <p>Title: VersionsUpdateController.java</p> 
 * <p>Description: 更新版本</p> 
 * @author :changjiang
 * date 2014-8-3 下午11:29:46
 */
@Controller
public class VersionsUpdateController {

	private VersionsUpdateManager versionsUpdateManager;
	

	public void setVersionsUpdateManager(VersionsUpdateManager versionsUpdateManager) {
		this.versionsUpdateManager = versionsUpdateManager;
	}


	@RequestMapping(value="/version/updateversion",method=RequestMethod.GET,produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String plusInter(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{

		//获取客户端json数据
		String json = "";
		VersionInfo versionInfo = versionsUpdateManager.findLatestVersion();
		if(null==versionInfo){
			versionInfo = new VersionInfo();
			versionInfo.setFlag(0);
			json = transToJson(versionInfo);
			return json;
		}
		json = transToJson(versionInfo);
		return json;
	}
	
	public String transToJson(Object obj){
		ObjectMapper objectMaper = new ObjectMapper();
		String json = "";
		try {
			json = objectMaper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
