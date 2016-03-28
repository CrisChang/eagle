package com.poison.easemob.ext.method;


import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.poison.easemob.ext.comm.Constants;
import com.poison.easemob.ext.comm.HTTPMethod;
import com.poison.easemob.ext.comm.Roles;
import com.poison.easemob.ext.utils.HTTPClientUtils;
import com.poison.easemob.ext.vo.ClientSecretCredential;
import com.poison.easemob.ext.vo.Credential;

/**
 * REST API Demo : 聊天消息导出REST API HttpClient4.3实现
 * 
 * Doc URL: http://www.easemob.com/docs/rest/chatmessage/
 * 
 * @author Lynch 2014-09-15
 * 
 */
public class EasemobChatMessage {

	private static Logger LOGGER = LoggerFactory.getLogger(EasemobChatMessage.class);
	private static JsonNodeFactory factory = new JsonNodeFactory(false);
	private static final String APPKEY = Constants.APPKEY;

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
            Constants.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

    /**
     * Main Test
     *
     * @param args
     */
    public static void main(String[] args) {

        // 聊天消息 获取最新的20条记录
        /*
        ObjectNode queryStrNode = factory.objectNode();
        queryStrNode.put("ql", "select+*+where+from='mm1'+and+to='mm2'");
        queryStrNode.put("limit", "20");
        ObjectNode messages = getChatMessages(queryStrNode);*/

        // 聊天消息 获取7天以内的消息
        String currentTimestamp = String.valueOf(System.currentTimeMillis());
        String senvenDayAgo = String.valueOf(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        ObjectNode queryStrNode1 = factory.objectNode();
        queryStrNode1.put("ql", "select * where timestamp>" + senvenDayAgo + " and timestamp<" + currentTimestamp);
        ObjectNode messages1 = getChatMessages(queryStrNode1);

        /*// 聊天消息 分页获取
        ObjectNode queryStrNode2 = factory.objectNode();
        queryStrNode2.put("limit", "20");
        // 第一页
        ObjectNode messages2 = getChatMessages(queryStrNode2);
        // 第二页
        String cursor = messages2.get("cursor").asText();
        queryStrNode2.put("cursor", cursor);
        ObjectNode messages3 = getChatMessages(queryStrNode2);*/
    }

    /**
	 * 获取聊天消息
	 * 
	 * @param queryStrNode
	 *
	 */
	public static ObjectNode getChatMessages(ObjectNode queryStrNode) {

		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		try {

			String rest = "";
			if (null != queryStrNode && queryStrNode.get("ql") != null && !StringUtils.isEmpty(queryStrNode.get("ql").asText())) {
				rest = "ql="+ queryStrNode.get("ql").asText();
			}
			if (null != queryStrNode && queryStrNode.get("limit") != null && !StringUtils.isEmpty(queryStrNode.get("limit").asText())) {
				rest = rest + "&limit=" + queryStrNode.get("limit").asText();
			}
			if (null != queryStrNode && queryStrNode.get("cursor") != null && !StringUtils.isEmpty(queryStrNode.get("cursor").asText())) {
				rest = rest + "&cursor=" + queryStrNode.get("cursor").asText();
			}
			String encodedQr = java.net.URLEncoder.encode(rest, "utf-8");
			URL chatMessagesUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/chatmessages?" + encodedQr);
			
			objectNode = HTTPClientUtils.sendHTTPRequest(chatMessagesUrl, credential, null, HTTPMethod.METHOD_GET);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

}
