package com.poison.eagle.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keel.framework.web.filter.ProductContextFilter;
import com.keel.utils.web.HttpHeaderUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.servlet.mvc.LastModified;
import redis.clients.jedis.Jedis;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.keel.common.event.rocketmq.RocketProducer;
import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.web.filter.CookieAutoLoginFilter;
import com.keel.framework.web.security.UserSecurityBeanOnCookie;
import com.keel.framework.web.utils.WebHttpSpringUtils;
import com.poison.eagle.easemobmanager.EasemobUserManager;
import com.poison.eagle.manager.HelloworldManager;
import com.poison.eagle.manager.SendMessageManager;
import com.poison.eagle.utils.MainSend;
import com.poison.eagle.utils.SerializeServiceUtil;
import com.poison.easemob.model.EasemobUser;
import com.poison.resource.client.BkCommentFacade;
import com.poison.ucenter.model.TalentZone;


@Controller  
@RequestMapping("/index")
public class HelloworldController implements LastModified{
	
	private static final  Log LOG = LogFactory.getLog(HelloworldController.class);
	
	private HelloworldManager helloworldManger;
	
	private UserSecurityBeanOnCookie userSecurityBeanOnCookie;
	
	private RocketProducer eagleProducer;
	private JedisSimpleClient jedisSimpleClient;
	
	private SendMessageManager sendMessageManager;

	private long lastModified;
	
	//private EasemobUserManager easemobUserManager;
	
	public void setJedisSimpleClient(JedisSimpleClient jedisSimpleClient) {
		this.jedisSimpleClient = jedisSimpleClient;
	}

	/*@Autowired
	public void setEasemobUserMagager(EasemobUserManager easemobUserManager) {
		this.easemobUserManager = easemobUserManager;
	}*/

	@Autowired
	public void setSendMessageManager(SendMessageManager sendMessageManager) {
		this.sendMessageManager = sendMessageManager;
	}

	@Autowired
	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}

	@Autowired
	public void setHelloworldManger(HelloworldManager helloworldManger) {
		this.helloworldManger = helloworldManger;
	}

	@Autowired
	public void setUserSecurityBeanOnCookie(
			UserSecurityBeanOnCookie userSecurityBeanOnCookie) {
		this.userSecurityBeanOnCookie = userSecurityBeanOnCookie;
	}

	@RequestMapping(value = "/helloWorld/{param1}/{param2}", method=RequestMethod.GET,produces = { "application/octet-stream;charset=utf-8" })
	@ResponseBody
	public String helloworld(HttpServletRequest request, HttpServletResponse response,@PathVariable String param1,@PathVariable String param2){
		//try{
			String msg = "";//this.helloworldManger.hello();
//			String msg = this.helloworldManger.hello();

		//String clientStr = HttpHeaderUtils.getRequestHeader(request, ProductContextFilter.);
		String os = ProductContextHolder.getProductContext().getEnv().getClientOS();
		System.out.println("系统是"+os);
		String verison = ProductContextHolder.getProductContext().getEnv().getClinetVersion();
		System.out.println("版本是"+verison);
			long beginTime = System.currentTimeMillis();
			//sendMessageManager.sendMessage("11", "11", 1, "11", 1, "11");
			/*ObjectNode datanode = JsonNodeFactory.instance.objectNode();
	        datanode.put("username","jinyingtest001");
	        datanode.put("password", Constants.DEFAULT_PASSWORD);*/
			//ObjectNode list = easemobUserManager.createNewIMUserSingle("c4ca4238a0b923820dcc509a6f75849b");
			
			//EasemobUser  easemobUser = easemobUserManager.createNewIMUserSingle(1);
			msg = helloworldManger.hello(param1,param2);
//		if(lastModified == 0L) {
//			//TODO 此处更新的条件：如果内容有更新，应该重新返回内容最新修改的时间戳
//			lastModified = System.currentTimeMillis();
//			response.setDateHeader("Last-Modified",lastModified);
//		}

		//response.setDateHeader("ETag",0f8b0c86fe2c0c7a67791e53d660208e3);
		//response.setHeader("ETag","0f8b0c86fe2c0c7a67791e53d660208e4");

					//easemobUser.toString();
					//helloworldManger.testList();

			/*msg = msg + "<br />I am HelloworldController 开始时间: " + beginTime + " {" + "param1:" + param1 + ";parm2:" + param2  + "} <br />";
			LOG.info("I am HelloworldController: " + beginTime + " {" + "param1:" + param1 + ";parm2:" + param2  + "}");*/
			//String msg =  "";//<br />I am HelloworldController 开始时间: " + beginTime + " {" + "param1:" + param1 + ";parm2:" + param2  + "} <br />";
			//LOG.info("I am HelloworldController: " + beginTime + " {" + "param1:" + param1 + ";parm2:" + param2  + "}");
			
//			msg = msg + ProductContextHolder.getProductContext().toString();
			
			/*msg = jedisSimpleClient.execute(new JedisWorker<String>(){

				@Override
				public String work(Jedis jedis) {
					String str = jedis.get("key");
					byte[] talentZoneByte = jedis.get(("TalentZoneInfo"+100).getBytes());
					TalentZone talentZoneRds = (TalentZone) SerializeServiceUtil.unserialize(talentZoneByte);
					if(null==talentZoneRds){
						talentZoneRds = new TalentZone();
						talentZoneRds.setId(100);
//						talentZoneRds = ucenterService.findTalentZoneInfo(id);
						jedis.set(("TalentZoneInfo"+100).getBytes(), SerializeServiceUtil.serialize(talentZoneRds));
					}
					//jedis.set("key", "value");
					return str;
				}
				
			});*/
			
			return msg;
		//}
		//catch(Exception e){
		//	LOG.info(e.getMessage(), e);
		//	return "exception: " + e.getStackTrace();
		//}
	}
	
	@RequestMapping(value = "/login", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response){
		MainSend mainSend  = new MainSend();
		mainSend.sendMsgToStore("嘿嘿", "422c61ce dbf7424a 23c46618 88bc1c2f 94d840f0 548cd7a9 cc6c2551 30f6ba9a");
		
		
		this.userSecurityBeanOnCookie.setShortUserSecurityData(response, 123456L, ProductContextHolder.getProductContext().getEnv().getClientIP());
		return "success!"+ProductContextHolder.getProductContext().toString();
	}
	
	@RequestMapping(value = "/checkLogin", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String checkLogin(HttpServletRequest request, HttpServletResponse response){
		if(ProductContextHolder.getProductContext().isAuthenticated()){
			return "authenticated!"+ProductContextHolder.getProductContext().toString();
		}else{
			return "failure!"+ProductContextHolder.getProductContext().toString();
		}
	}
	
	@RequestMapping(value = "/setSession", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String setSession(HttpServletRequest request, HttpServletResponse response){
		request.getSession().setAttribute("session_test", "I am a session value!");
		return "success!";
	}
	
	@RequestMapping(value = "/getSession", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String getSession(HttpServletRequest request, HttpServletResponse response){
		
		return (String) request.getSession().getAttribute("session_test");
	}
	
	@RequestMapping(value = "/msg/{param1}", produces = { "text/html;charset=utf-8" })
	@ResponseBody
	public String sendMsg(HttpServletRequest request, HttpServletResponse response, @PathVariable String param1){
		
		try {
			this.eagleProducer.send("testTopic", "testTag", null, param1);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return "success!";
	}

	@Override
	public long getLastModified(HttpServletRequest request) {

		if(lastModified == 0L) {
			//TODO 此处更新的条件：如果内容有更新，应该重新返回内容最新修改的时间戳
			lastModified = System.currentTimeMillis();
		}
		return lastModified;
	}


//	@Override
//	public long getLastModified(HttpServletRequest request) {
//		lastModified = request.getDateHeader("Last-Modified");
//		System.out.println(lastModified);
//		if(lastModified==0l){
//			lastModified = System.currentTimeMillis();
//		}
//		System.out.println(lastModified);
//		return lastModified;
//	}
}
