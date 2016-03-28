package com.poison.eagle.manager.bige;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActWeixinComment;
import com.poison.act.model.ActWeixinUser;
import com.poison.eagle.action.bige.BigeController;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.HttpRequest;


/**
 * @author wei
 *
 */
public class BigeManager extends BaseManager{
	/**
	 * 日志
	 */
	private static final Log LOG = LogFactory.getLog(BigeManager.class);
	
	private ActFacade actFacade;
	
	private static List<Map<String,Object>> questions = new ArrayList<Map<String,Object>>();

	static{
		//暂时写死微信分享的逼格题目
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 1);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "你认为哪种电影可以让你有心跳加快的感觉？");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、惊悚片");
			option1.put("value", 4);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、爱情片");
			option2.put("value", 2);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、恐怖片");
			option3.put("value", 6);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、悬疑片");
			option4.put("value", 8);
			options.add(option4);
			
			question.put("options", options);
			
			questions.add(question);
		}
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 2);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "想象一下，假如你有500万，你最想做什么？");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、把钱存起来生蛋");
			option1.put("value", 2);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、去美容医院做整形");
			option2.put("value", 4);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、带爸爸妈妈去马尔代夫旅行");
			option3.put("value", 6);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、投资最有潜力的市场行业");
			option4.put("value", 8);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 3);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "你对电影的评价标准是？");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、明星阵容");
			option1.put("value", 2);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、电影剧情");
			option2.put("value", 8);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、观众美誉度");
			option3.put("value", 6);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、演员表现力");
			option4.put("value", 4);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 4);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "如果要送一本书给你的初恋情人，你会选");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、《小王子》");
			option1.put("value", 6);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、《彼得潘》");
			option2.put("value", 8);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、《格林兄弟童话集》");
			option3.put("value", 2);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、《哈利波特》");
			option4.put("value", 4);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 5);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "你觉得你的人生是一本：");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、回忆录");
			option1.put("value", 8);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、笑话集");
			option2.put("value", 4);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、人物写真");
			option3.put("value", 2);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、职场指南");
			option4.put("value", 6);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 6);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "在商店看到一件梦寐以求的奢侈品，你的心理感受是：");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A：它距离我很远，这辈子也不可能拥有它了。");
			option1.put("value", 2);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B：努力工作赚钱，宁可晚一点也要买正品，绝不买赝品。");
			option2.put("value", 4);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C：没什么了不起，不靠奢侈品来“武装”自己。");
			option3.put("value", 6);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D：完全买得起（包括临时买不起+以后能买得起）时，选择不买。");
			option4.put("value", 8);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 7);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "电影《辛普森一家》里，玛吉在突如其来的火灾里义无反顾地返回失火的屋子里面，就是为了抢回自己的结婚录像带。玛吉对荷马的爱意让我们深深感动。如果是你，遇到深夜火灾这种情况，你会拿着什么东西逃生呢？");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、钱 ");
			option1.put("value", 2);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、时钟 ");
			option2.put("value", 6);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、食物 ");
			option3.put("value", 4);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、日记");
			option4.put("value", 8);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 8);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "远在另一个城市的朋友过生日，大家考虑送礼物的问题，在相同的价格下你会选择？");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、电话在朋友所在地订购大束百合花和生日蛋糕");
			option1.put("value", 2);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、上网订购一个来自澳大利亚黄金海岸的深海贝壳");
			option2.put("value", 4);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、送他喜欢的东西快递过去");
			option3.put("value", 6);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、买一个精致的工艺品亲自坐飞机送给他");
			option4.put("value", 8);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}		
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 9);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "去太空旅行会倾其所有，或者花费多少你能接受？");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、花费所有积蓄都能接受");
			option1.put("value", 6);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、没钱，花一分我都心疼");
			option2.put("value", 2);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、只要留给吃喝的钱就行");
			option3.put("value", 4);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、一半家产吧");
			option4.put("value", 8);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}
		if(true){
			Map<String, Object> question = new HashMap<String, Object>();
			question.put("no", 10);
			question.put("bannerImage", "images/ad.jpg");
			question.put("question", "一对儿老年夫妇卖掉了车和房，去环游世界，你的看法是？");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A、牛逼人儿，膜拜");
			option1.put("value", 4);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B、老了老了，没个家真是可怜");
			option2.put("value", 2);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C、追求心灵的净化，大赞");
			option3.put("value", 6);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D、每人的追求不一样，过的开心就好");
			option4.put("value", 8);
			options.add(option4);
			
			question.put("options", options);
			questions.add(question);
		}
	}
	/**
	 * 查询逼格的题
	 * @param reqs
	 * @return
	 */
	public String getQuestion(String reqs) {
		Map<String, Object> req;
		//Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		//String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		try {
			int no = 2;
			if(reqs!=null){
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				String nostr = req.get("questionNo")+"";
				no = Integer.valueOf(nostr);
			}
			if(no<1){
				no=1;
			}
			datas = questions.get(no-1);//集合索引从0开始的
			
			/**
			 {
			    "no": 2,
			    "bannerImage": "images/ad.jpg",
			    "question": "听着黄绮珊的歌，你觉得做点什么最应景儿？",
			    "options": [{
			        "option": "A.学韩红深情对唱",
			        "value": 2
			        }, {
			        "option": "B.照镜子看看自己的牙",
			        "value": 6
			        }, {
			        "option": "C.捂住耳朵",
			        "value": 8
			        }, {
			        "option": "D.戳瞎自己的眼睛",
			        "value": 4
			        }]
			}
			 */
			/*datas.put("no", no);
			datas.put("bannerImage", "images/ad.jpg");
			datas.put("question", "听着黄绮珊的歌，你觉得做点什么最应景儿？");
			List<Object> options = new ArrayList<Object>();
			Map<String,Object> option1 = new HashMap<String,Object>();
			option1.put("option", "A.学韩红深情对唱");
			option1.put("value", 2);
			options.add(option1);
			
			Map<String,Object> option2 = new HashMap<String,Object>();
			option2.put("option", "B.照镜子看看自己的牙");
			option2.put("value", 6);
			options.add(option2);
			
			Map<String,Object> option3 = new HashMap<String,Object>();
			option3.put("option", "C.捂住耳朵");
			option3.put("value", 8);
			options.add(option3);
			
			Map<String,Object> option4 = new HashMap<String,Object>();
			option4.put("option", "D.戳瞎自己的眼睛");
			option4.put("value", 4);
			options.add(option4);
			
			datas.put("options", options);*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 * 提交用户选择题目的答案的分值
	 * @param reqs
	 * @return
	 */
	public String postValue(HttpServletRequest request,String reqs) {
		Map<String, Object> req;
		//Map<String, Object> dataq;
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		//String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		try {
			if(reqs!=null){
				req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
				String valueStr =  req.get("radioValue")+"";
				String nostr = req.get("questionNo")+"";
				int value = Integer.valueOf(valueStr);
				int no = Integer.valueOf(nostr);
				System.out.println("no:"+no+"             value:"+value);
				//不能用一下代码，因为该系统架构调整影响了session，导致session不可用
				/*if(no>=1 && no<=10){
					request.getSession().setAttribute("questionNo"+no, value);
				}
				if(no==10){
					//证明答完题了，需要在session中放入一个标志，用于区分结果页面是跳转到end.jsp还是label.jsp
					request.getSession().setAttribute("result", "result");
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 * 请求微信连接获取用户信息
	 */
	public ActWeixinUser getWeixinUserinfo(String code,String state) {
		BigeController.message = "";
		if(code!=null && code.length()>0){
			try{
				//code不为空，说明用户同意授权获取用户信息了
				//第二部，通过code换取网页授权access_token
				String secret = "e6c65b57845846986637f7c341b0eefd";
				String appid="wx1441086740e20837";
				String urladdress1 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
				BigeController.message = "urladdress1:"+urladdress1;
				String json1 = HttpRequest.getUrl(urladdress1);
				BigeController.message = BigeController.message+"-json1:"+json1;
				//json1 = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200,\"refresh_token\":\"REFRESH_TOKEN\",\"openid\":\"OPENID\",\"scope\":\"SCOPE\"}";
				/**
				 {
				   "access_token":"ACCESS_TOKEN",
				   "expires_in":7200,
				   "refresh_token":"REFRESH_TOKEN",
				   "openid":"OPENID",
				   "scope":"SCOPE"
				}
				 * 
				 */
				Map<String, Object> map1 = getObjectMapper().readValue(json1,  new TypeReference<Map<String, Object>>(){});
				
				String access_token = (String) map1.get("access_token");
				String openid = (String) map1.get("openid");
				String refresh_token = (String) map1.get("refresh_token");
				
				//第三步：刷新access_token（如果需要）
				
				//String urladdress2 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+appid+"&grant_type=refresh_token&refresh_token="+refresh_token;
				//String json2 = HttpRequest.getUrl(urladdress2);
				
				//json2 = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200,\"refresh_token\":\"REFRESH_TOKEN\",\"openid\":\"OPENID\",\"scope\":\"SCOPE\"}";
				/**
				 {
				   "access_token":"ACCESS_TOKEN",
				   "expires_in":7200,
				   "refresh_token":"REFRESH_TOKEN",
				   "openid":"OPENID",
				   "scope":"SCOPE"
				}
				 */
				
				
				
				//第四步：拉取用户信息(需scope为 snsapi_userinfo)
				String urladdress3 = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
				BigeController.message = BigeController.message+"-urladdress3:"+urladdress3;
				String json3 = HttpRequest.getUrl(urladdress3);
				BigeController.message = BigeController.message+"-json3:"+json3;
				//json3 = "{\"openid\":\"OPENID\",\"nickname\":\"NICKNAME\",\"sex\":\"1\",\"province\":\"PROVINCE\",\"city\":\"CITY\",\"country\":\"COUNTRY\",\"headimgurl\":\"http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46\",\"unionid\": \"o6_bmasdasdsad6_2sgVt7hMZOPfL\"}";
				/**
				 {
				   "openid":"OPENID",
				   "nickname": NICKNAME,
				   "sex":"1",
				   "province":"PROVINCE"
				   "city":"CITY",
				   "country":"COUNTRY",
				    "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46", 
					"privilege":[
					"PRIVILEGE1"
					"PRIVILEGE2"
				    ]
				    "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
				}
				 */
				Map<String, Object> map3 = getObjectMapper().readValue(json3,  new TypeReference<Map<String, Object>>(){});
				String nickname = (String) map3.get("nickname");
				String sex = map3.get("sex")+"";
				String province = (String) map3.get("province");
				String city = (String) map3.get("city");
				String country = (String) map3.get("country");
				String headimgurl = (String) map3.get("headimgurl");
				String unionid = (String) map3.get("unionid");
				if(openid!=null && openid.length()>0 && !"null".equals(openid)){
					ActWeixinUser wxuser = actFacade.findUserById(openid);
					if(wxuser==null || wxuser.getOpenId()==null){
						wxuser = new ActWeixinUser();
						wxuser.setOpenId(openid);
						wxuser.setSaveTime(System.currentTimeMillis());
					}
					wxuser.setCity(city);
					wxuser.setCountry(country);
					wxuser.setHeadimgUrl(headimgurl);
					wxuser.setNickName(nickname);
					wxuser.setProvince(province);
					wxuser.setSex(Integer.valueOf(sex));
					wxuser.setUnionId(unionid);
					wxuser.setUpdateTime(System.currentTimeMillis());
					//如果不存在则插入，存在则更新
					int result = 0;
					if(wxuser.getId()>0){
						result = actFacade.updateUser(wxuser);
					}else{
						result = actFacade.insertUser(wxuser);
					}
					return wxuser;
				}
				return null;
			}catch(Exception e){
				e.printStackTrace();
				BigeController.message = BigeController.message+"-e.getMessage():"+e.getMessage()+"-e.getCause():"+e.getCause()+"-e.getLocalizedMessage():"+e.getLocalizedMessage();
				return null;
			}
		}else{
			//用户拒绝获取用户信息
			return null;
		}
	}
	
	/**
	 * 请求微信连接获取用户信息
	 */
	public ActWeixinUser getWeixinUserinfo2(String code,String state) {
		BigeController.message = "";
		if(code!=null && code.length()>0){
			try{
				//code不为空，说明用户同意授权获取用户信息了
				//第二部，通过code换取网页授权access_token
				String secret = "4d8d8657447fbe67c0b2ea3ff90d9c9a";
				String appid="wx759bb191ad5aa2d6";
				String urladdress1 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
				BigeController.message = "urladdress1:"+urladdress1;
				String json1 = HttpRequest.getUrl(urladdress1);
				BigeController.message = BigeController.message+"-json1:"+json1;
				//json1 = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200,\"refresh_token\":\"REFRESH_TOKEN\",\"openid\":\"OPENID\",\"scope\":\"SCOPE\"}";
				/**
				 {
				   "access_token":"ACCESS_TOKEN",
				   "expires_in":7200,
				   "refresh_token":"REFRESH_TOKEN",
				   "openid":"OPENID",
				   "scope":"SCOPE"
				}
				 * 
				 */
				Map<String, Object> map1 = getObjectMapper().readValue(json1,  new TypeReference<Map<String, Object>>(){});
				
				String access_token = (String) map1.get("access_token");
				String openid = (String) map1.get("openid");
				String refresh_token = (String) map1.get("refresh_token");
				
				//第三步：刷新access_token（如果需要）
				
				//String urladdress2 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+appid+"&grant_type=refresh_token&refresh_token="+refresh_token;
				//String json2 = HttpRequest.getUrl(urladdress2);
				
				//json2 = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200,\"refresh_token\":\"REFRESH_TOKEN\",\"openid\":\"OPENID\",\"scope\":\"SCOPE\"}";
				/**
				 {
				   "access_token":"ACCESS_TOKEN",
				   "expires_in":7200,
				   "refresh_token":"REFRESH_TOKEN",
				   "openid":"OPENID",
				   "scope":"SCOPE"
				}
				 */
				
				
				
				//第四步：拉取用户信息(需scope为 snsapi_userinfo)
				String urladdress3 = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
				BigeController.message = BigeController.message+"-urladdress3:"+urladdress3;
				String json3 = HttpRequest.getUrl(urladdress3);
				BigeController.message = BigeController.message+"-json3:"+json3;
				//json3 = "{\"openid\":\"OPENID\",\"nickname\":\"NICKNAME\",\"sex\":\"1\",\"province\":\"PROVINCE\",\"city\":\"CITY\",\"country\":\"COUNTRY\",\"headimgurl\":\"http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46\",\"unionid\": \"o6_bmasdasdsad6_2sgVt7hMZOPfL\"}";
				/**
				 {
				   "openid":"OPENID",
				   "nickname": NICKNAME,
				   "sex":"1",
				   "province":"PROVINCE"
				   "city":"CITY",
				   "country":"COUNTRY",
				    "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46", 
					"privilege":[
					"PRIVILEGE1"
					"PRIVILEGE2"
				    ]
				    "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
				}
				 */
				Map<String, Object> map3 = getObjectMapper().readValue(json3,  new TypeReference<Map<String, Object>>(){});
				String nickname = (String) map3.get("nickname");
				String sex = map3.get("sex")+"";
				String province = (String) map3.get("province");
				String city = (String) map3.get("city");
				String country = (String) map3.get("country");
				String headimgurl = (String) map3.get("headimgurl");
				String unionid = (String) map3.get("unionid");
//				if(openid!=null && openid.length()>0 && !"null".equals(openid)){
//					ActWeixinUser wxuser = actFacade.findUserById(openid);
//					if(wxuser==null || wxuser.getOpenId()==null){
//						wxuser = new ActWeixinUser();
//						wxuser.setOpenId(openid);
//						wxuser.setSaveTime(System.currentTimeMillis());
//					}
//					wxuser.setCity(city);
//					wxuser.setCountry(country);
//					wxuser.setHeadimgUrl(headimgurl);
//					wxuser.setNickName(nickname);
//					wxuser.setProvince(province);
//					wxuser.setSex(Integer.valueOf(sex));
//					wxuser.setUnionId(unionid);
//					wxuser.setUpdateTime(System.currentTimeMillis());
//					//如果不存在则插入，存在则更新
//					int result = 0;
//					if(wxuser.getId()>0){
//						result = actFacade.updateUser(wxuser);
//					}else{
//						result = actFacade.insertUser(wxuser);
//					}
//					return wxuser;
//				}
				
				return null;
			}catch(Exception e){
				e.printStackTrace();
				BigeController.message = BigeController.message+"-e.getMessage():"+e.getMessage()+"-e.getCause():"+e.getCause()+"-e.getLocalizedMessage():"+e.getLocalizedMessage();
				return null;
			}
		}else{
			//用户拒绝获取用户信息
			return null;
		}
	}
	
	/**
	 * 查询对一个微信用户的评价信息和得分信息
	 */
	public String getUserTags(String openid) {
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		//String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		try {
			
			/**
			 {
			    "gameUserID": "Mars1", //游戏用户名
			    "gameUserAvatar": "images/photo/bb.jpg", //游戏用户头像
			    "currentUserID": "testUser", //当前用户信息
			    "userScore": "与TA逼格接近的明星是Angelababy 黄晓明", //用户分数
			    "defaultLabels": ["猴子", "掉节操", "狂街", "吃饭", "烛光晚餐", "压马路11"], //默认载入
			    "tags": [
			        {
			            "tagName": "高B格",
			            "tagNO": 2
			        }, {
			            "tagName": "一起上厕所",
			            "tagNO": 1
			        }, {
			            "tagName": "上厕所",
			            "tagNO": 1
			        }, {
			            "tagName": "一起加班",
			            "tagNO": 1
			        }, {
			            "tagName": "2B青年",
			            "tagNO": 5
			        }, {
			            "tagName": "工作狂",
			            "tagNO": 4
			        }, {
			            "tagName": "山炮",
			            "tagNO": 1
			        }, {
			            "tagName": "上厕所",
			            "tagNO": 10
			        }, {
			            "tagName": "一起玩耍",
			            "tagNO": 1
			        }
			    ]
			}
			 */
			List<String> labels = new ArrayList<String>();
			labels.add("猴子");
			labels.add("掉节操");
			labels.add("吃饭");
			labels.add("烛光晚餐");
			labels.add("压马路");
			datas.put("defaultLabels", labels);
			
			if(openid!=null && openid.length()>0 && !"null".equals(openid)){
				ActWeixinUser user = actFacade.findUserById(openid);
				if(user!=null && user.getOpenId()!=null && user.getOpenId().length()>0){
					datas.put("gameUserID", user.getNickName());
					datas.put("gameUserAvatar", user.getHeadimgUrl());
					datas.put("currentUserID", user.getOpenId());
					
					long count1 = actFacade.findCountByScore(user.getScore());
					long count2 = actFacade.findUserCount();
					double percent = 0;
					if(count2<=0){
						percent = 100;
					}else{
						percent = (count1*100)/count2;
					}
					datas.put("userScore", "TA的得分为"+user.getScore()+",PK掉了"+percent+"%的人");
					
					List<Map<String,Object>> tags = new ArrayList<Map<String,Object>>();
					
					List<ActWeixinComment> comments = actFacade.findWeixinComment(openid);
					if(comments!=null && comments.size()>0){
						//需要处理相同标签
						Map<String,Integer> handletags = new HashMap<String,Integer>();
						for(int i=0;i<comments.size();i++){
							ActWeixinComment comment = comments.get(i);
							Integer times = 0;
							if(handletags.get(comment.getCommentContext())!=null && handletags.get(comment.getCommentContext()) instanceof Integer ){
								times = handletags.get(comment.getCommentContext());
							}
							times++;
							handletags.put(comment.getCommentContext(), times);
						}
						
						Set<String> keys = handletags.keySet();
						
						for(String key : keys){
							Map<String,Object> tag = new HashMap<String,Object>();
							tag.put("tagName",key);
							tag.put("tagNO",handletags.get(key));
							
							tags.add(tag);
						}
					}
					datas.put("tags", tags);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	/**
	 * 查询PK结果--结果页面使用
	 */
	public double getScorePK(int score) {
		try {
			long count1 = actFacade.findCountByScore(score);
			long count2 = actFacade.findUserCount();
			double percent = 0;
			if(count2<=0){
				percent = 100;
			}else{
				percent = (count1*100)/count2;
			}
			return percent;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return 0;
		}
	}
	
	/**
	 * 保存逼格得分
	 */
	public int updateScore(String openid,int score) {
		try {
			if(openid!=null && openid.length()>0 && !"null".equals(openid)){
				ActWeixinUser user = actFacade.findUserById(openid);
				if(user!=null && user.getOpenId()!=null && user.getOpenId().length()>0){
					user.setScore(score);
					user.setUpdateTime(System.currentTimeMillis());
					int result = actFacade.updateUser(user);
					return result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return -1;
		}
		return -1;
	}
	
	/**
	 * 插入对一个用户的评价
	 */
	public String saveUserTag(String openid,String uopenid,String tag) {
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = null;
		//String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		try {
			if(openid!=null && uopenid!=null && tag!=null && tag.length()>0){
				int count = actFacade.existUserComment(uopenid, openid, tag);
				if(count==0){
					//不存在，可以保存评价
					ActWeixinComment comment = new ActWeixinComment();
					comment.setCommentContext(tag);
					comment.setOpenId(uopenid);
					comment.setSopenId(openid);
					comment.setSaveTime(System.currentTimeMillis());
					comment.setUpdateTime(System.currentTimeMillis());
					int result = actFacade.insertWeixinComment(comment);
					if(result==1){
						datas.put("flag", 1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(e.getMessage(),e.fillInStackTrace());
			return RES_DATA_NOTGET;
		}
		
		//处理返回数据
		resString = getResponseData(datas);
		return resString;
	}
	
	
	/**
	 * 服务端返回json数据公共方法---重写了---专为逼格设计
	 * @param data
	 * @return
	 */
	@Override
	public String getResponseData(Map<String, Object> data){
		String jsonString = null;
		try {
			jsonString = getObjectMapper().writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
			jsonString= RES_DATA_NOTRESULT;
		}
		if(jsonString == null){
			jsonString = RES_DATA_NOTRESULT;
		}
		return jsonString;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
}
