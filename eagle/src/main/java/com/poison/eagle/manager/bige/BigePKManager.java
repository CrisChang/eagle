package com.poison.eagle.manager.bige;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActWeixinUser;
import com.poison.eagle.action.bige.BigeController;
import com.poison.eagle.action.bige.BigePKController;
import com.poison.eagle.entity.WeixinUserInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.HttpRequest;

/**
 * 百幕大战 manager
 * 
 * @author zhangqi
 * 
 */
public class BigePKManager extends BaseManager {

	private static final Log LOG = LogFactory.getLog(BigePKManager.class);

	public String appID = "wx1441086740e20837";
	public String appsecret = "e6c65b57845846986637f7c341b0eefd";

	private ActFacade actFacade;

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public String weinXinLogin(String code, String state) {
		Map<String, Object> datas = new HashMap<String, Object>();
		String resString = "";// 返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;// 0：成功、1：失败
		String userType = "2";// 1 毒药 2:微信 3:微博
		try {
			ActWeixinUser weixinUser = getWeixinUserinfo(code, state);
			if (weixinUser != null && weixinUser.getOpenId() != null) {
				WeixinUserInfo weixinUserInfo = new WeixinUserInfo();
				weixinUserInfo.setOpenId(weixinUser.getOpenId());
				flag = CommentUtils.RES_FLAG_SUCCESS;
				datas.put("flag", flag);
				datas.put("weixinUserInfo", weixinUserInfo);
				datas.put("userType", userType);
			} else {
				flag = CommentUtils.RES_FLAG_ERROR;
				datas.put("flag", flag);
			}
		} catch (Exception e) {
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("flag", flag);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		resString = getResponseData(datas);
		return resString;
	}

	/**
	 * 请求微信连接获取用户信息
	 */
	public ActWeixinUser getWeixinUserinfo(String code, String state) {
		BigePKController.message = "";
		if (code != null && code.length() > 0) {
			try {
				// code不为空，说明用户同意授权获取用户信息了
				// 第二部，通过code换取网页授权access_token
				String urladdress1 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appID + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";
				BigePKController.message = "urladdress1:" + urladdress1;
				String json1 = HttpRequest.getUrl(urladdress1);
				BigePKController.message = BigePKController.message + "-json1:" + json1;
				/**
				 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
				 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID",
				 * "scope":"SCOPE" }
				 * 
				 */
				// {"errcode":40029,"errmsg":"req id: oK4epa0516ns24, invalid code"}
				Map<String, Object> map1 = getObjectMapper().readValue(json1, new TypeReference<Map<String, Object>>() {
				});

				String access_token = (String) map1.get("access_token");
				String openid = (String) map1.get("openid");
				String refresh_token = (String) map1.get("refresh_token");
				if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(refresh_token)) {
					String errcode = (String) map1.get("errcode");
					String errmsg = (String) map1.get("errmsg");
				}
				// 第三步：刷新access_token（如果需要）
				String urladdress2 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appID + "&grant_type=refresh_token&refresh_token=" + refresh_token;
				String json2 = HttpRequest.getUrl(urladdress2);
				/**
				 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
				 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID",
				 * "scope":"SCOPE" }
				 */
				// 第四步：拉取用户信息(需scope为 snsapi_userinfo)
				String urladdress3 = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN";
				BigeController.message = BigeController.message + "-urladdress3:" + urladdress3;
				String json3 = HttpRequest.getUrl(urladdress3);
				BigePKController.message = BigePKController.message + "-json3:" + json3;
				/**
				 * { "openid":"OPENID", "nickname": NICKNAME, "sex":"1",
				 * "province":"PROVINCE" "city":"CITY", "country":"COUNTRY",
				 * "headimgurl":
				 * "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46"
				 * , "privilege":[ "PRIVILEGE1" "PRIVILEGE2" ] "unionid":
				 * "o6_bmasdasdsad6_2sgVt7hMZOPfL" }
				 */
				return getWeixinUser(openid, json3);
			} catch (Exception e) {
				LOG.error("getWeixinUserinfo :error=>" + e.getMessage());
				e.printStackTrace();
				BigePKController.message = BigePKController.message + "-e.getMessage():" + e.getMessage() + "-e.getCause():" + e.getCause() + "-e.getLocalizedMessage():" + e.getLocalizedMessage();
				return null;
			}
		} else {
			return null;
		}
	}

	private ActWeixinUser getWeixinUser(String openid, String json3) throws IOException, JsonParseException, JsonMappingException {
		Map<String, Object> map3 = getObjectMapper().readValue(json3, new TypeReference<Map<String, Object>>() {
		});
		String nickname = (String) map3.get("nickname");
		String sex = map3.get("sex") + "";
		String province = (String) map3.get("province");
		String city = (String) map3.get("city");
		String country = (String) map3.get("country");
		String headimgurl = (String) map3.get("headimgurl");
		String unionid = (String) map3.get("unionid");
		if (openid != null && openid.length() > 0 && !"null".equals(openid)) {
			ActWeixinUser wxuser = actFacade.findUserById(openid);
			if (wxuser == null || wxuser.getOpenId() == null) {
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
			// 如果不存在则插入，存在则更新
			int result = 0;
			if (wxuser.getId() > 0) {
				result = actFacade.updateUser(wxuser);
			} else {
				result = actFacade.insertUser(wxuser);
			}
			return wxuser;
		}
		return null;
	}

	/**
	 * 服务端返回json数据公共方法---重写了---专为逼格设计
	 * 
	 * @param data
	 * @return
	 */
	@Override
	public String getResponseData(Map<String, Object> data) {
		String jsonString = null;
		try {
			jsonString = getObjectMapper().writeValueAsString(data);
		} catch (Exception e) {
			e.printStackTrace();
			jsonString = RES_DATA_NOTRESULT;
		}
		if (jsonString == null) {
			jsonString = RES_DATA_NOTRESULT;
		}
		return jsonString;
	}
}
