package com.poison.eagle.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.poison.eagle.manager.UserPOIManager;
import com.poison.eagle.utils.BaseController;

@Controller  
@RequestMapping("/state/poi")
public class UserPOIFlushByIDController extends BaseController {
	private static final  Log LOG = LogFactory.getLog(UserPOIFlushByIDController.class);
	
	private UserPOIManager userPOIManager;
	
	
	@RequestMapping(value = "/put", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String put(HttpServletRequest request, HttpServletResponse response){
		/*fun 1: keyword and fields*/
		String lon = request.getParameterValues("lon") != null ? request.getParameterValues("lon")[0] : null;
		String lat = request.getParameterValues("lat") != null ? request.getParameterValues("lat") [0] : null;
		String mark = request.getParameterValues("mark") != null ? request.getParameterValues("mark") [0] : "";
        long userId = 0L;
		if(checkUserIsLogin()){
			userId = getUserId();
		}
		
		String msg = null;
		if ( StringUtils.isBlank(lon) || StringUtils.isBlank(lat) || 0L == userId) {
			msg = "invalid param!";
			return "{\"flag\":\"1\",\"msg\":\"" + msg + "\"}";
		}
		
		userPOIManager.put(userId, lon, lat, mark);
		
		msg = "success!";
		return "{\"flag\":\"0\",\"msg\":\"" + msg + "\"}";
	}
	
	public void setUserPOIManager(UserPOIManager userPOIManager) {
		this.userPOIManager = userPOIManager;
	}
}
