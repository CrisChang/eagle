package com.poison.eagle.manager;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.keel.utils.UKeyWorker;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActPublish;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.otherinterface.umeng_java_sdk.push.Demo;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.model.BkComment;
import com.poison.store.client.BkFacade;
import com.poison.store.model.BkInfo;
import com.poison.ucenter.client.HelloworldFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAlbum;
import com.poison.ucenter.model.UserInfo;

public class HelloworldManager {
	private static final  Log LOG = LogFactory.getLog(HelloworldManager.class);

	private HelloworldFacade helloworldFacade;
	private BkCommentFacade bkCommentFacade;
	private UcenterFacade ucenterFacade;
	private BkFacade bkFacade;
	private ActFacade actFacade;
	private PushManager pushManager;
	private UKeyWorker reskeyWork;
	
	
	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}

	public void setPushManager(PushManager pushManager) {
		this.pushManager = pushManager;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}

	public void setHelloworldFacade(HelloworldFacade helloworldFacade) {
		this.helloworldFacade = helloworldFacade;
	}
	
	public String hello(String param1,String param2){
		String msg = "";//helloworldFacade.hello();
		//msg = msg + "<br /> I am HelloworldManager!";
		//UserInfo list = ucenterFacade.findUserInfoByName("用户名注册");
		//BkInfo list = bkFacade.findBkInfoBybookurl("http://www.douban.com/subject/1000001/");
		//UserInfo list = ucenterFacade.findUserInfoByNameOrMobilePhone("18611128524");
		//ActComment list = actFacade.doOneComment(null, 1, 22074835190546432L, "0", "context");
		//List<UserInfo> list = ucenterFacade.findUserFens(null, 27, 50, 0, 50);
		//ActPublish list = actFacade.cancelPublish(26985486652272640L);
		System.out.println("成员1为："+param1);
		System.out.println("成员2为："+param2);
		/*Demo demo = new Demo("548671c7fd98c5590200033c", "7lbz2nffeuf8f1gueu06v3zu9qxsgpdb","尹楠在毒药中提到了你",param1,param2);
		try{
			demo.sendIOSGroupcast();
		}catch (Exception e) {
			e.printStackTrace();
		}*/
		//pushManager.pushAttentionMSG(Long.valueOf(param1),Long.valueOf(param2));
		//List<UserAlbum> list = ucenterFacade.findUserAlbumByUid(2);
		//BkComment list = bkCommentFacade.addOneBkComment(15, 1, "1", "score", 1, 1, "type",null);
		System.out.println(reskeyWork.getId());
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			msg = objectMapper.writeValueAsString("");
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
}
