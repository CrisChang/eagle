package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.poison.act.model.ActUseful;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.MvComment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActCollect;
import com.poison.act.model.ActComment;
import com.poison.act.model.ActPraise;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.JedisConstant;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.ucenter.client.UcenterFacade;

public class InsertServerManager extends BaseManager{
	
	private static final  Log LOG = LogFactory.getLog(InsertServerManager.class);

	
	private UcenterFacade ucenterFacade;
	private ActFacade actFacade;
	
	private UserJedisManager userJedisManager;
	
	private RocketProducer eagleProducer;
	
	private RelationToUserAndResManager relationToUserAndResManager;
	private ResStatJedisManager resStatJedisManager;
	private BkCommentFacade bkCommentFacade;
	private MvCommentFacade mvCommentFacade;

	/**
	 * 
	 * <p>Title: insertComment</p> 
	 * <p>Description: 内部接口</p> 
	 * @author :changjiang
	 * date 2015-6-11 下午4:01:50
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String insertComment(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		List<Map<String, String>> oList = new ArrayList<Map<String,String>>();//话题列表等信息
		//List commentList = null;
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");
		
		Long id = Long.valueOf(dataq.get("id").toString());
		String type = (String) dataq.get("type");
		String content = (String) dataq.get("content");
		
		Long commentUid = 0l;
		try {
			commentUid = Long.valueOf(dataq.get("commentUserid").toString());
			if(commentUid == null){
				commentUid = 0l;
			}
		} catch (Exception e) {
			commentUid = 0l;
		}
		Long commentId = 0l;
		try {
			commentId = Long.valueOf(dataq.get("commentResid").toString());
			if(commentId == null){
				commentId = 0l;
			}
		} catch (Exception e) {
			commentId = 0l;
		}
		Long res_userid = 0l;
		try {
			res_userid = Long.valueOf(dataq.get("resUserid").toString());
			if(res_userid == null){
				res_userid = 0l;
			}
		} catch (Exception e) {
			res_userid = 0l;
		}
		//ActPraise act = null;
		long[] uids = {2841,960,326,224,54088,327,469,581,479,576,312,587,583,54042,320,590,334,54050,54049,593,557,555,575,470,1022,329,957,31199,54060,577,156,460,31127,54045,471,188,591,53993,585,474,180,190,584,31126,54090,683,54055,463,51192,54091,594,54047,335,686,553,406,578,658,645,589,54048,54059,54046,54062,54041,333,473,181,403,31198,321,554,687,685,179,579,195,956,580,588,54093,189,346,31343,155,31128,592,126,54061,313,314,475,54087,191,54044,54063,54064,655,315,201,688,31124,54092,200,556,661,461,660,478,402,317,582,659,194,175,462,171,477,322,459,476,193,552,54057,458,31338,244,958,472,358,959,54058,468,330,316,54094,54086,328,31341,54043,174,185,167,467,586,684,339
				,56905,56904,57179,57050,56557,56983,56597,56945,57120,56607,56599,56982,56940,56462,56939,57171,57188,56943,57125,57169,57189,56238,56759,56633,56456,56937,57030,56482,57178,56710,56756,57074,57071,56909,56703,56245,56509,56666,57053,56448,56485,56540,56630,56441,57212,56526,57148,56453,57102,56444,56688,56672,56625,56689,57122,56687,56923,57115,56319,56474,57007,56953,57156,56705,57084,56912,57024,57047,57035,56970,57181,57046,57025,57209,56521,56561,56308,56551,56679,56242,56486,56949,57126,56965,56948,57163,57093,56286,56241,57123,56594,56275,56741,56553,57089,56956,57054,56539,56665,57088,57187,56626,56631,56495,56493,57134,56311,56470,56438,56459,56261,56638,56312,56463,56608,56506,56264,56779,57109,56494,56458,57180,57159,57029,56668,56658,56265,57204,56270,56621,57217,57045,56984,56963,56614,56910,56677,57086,56287,57073,57001,57210,56564,56548,56938,56907,56993,56951,57097,56751,57168,56709,57041,57022,56640,57146,57160,56629,57147,56537,56600,56674,57138,56466,56729,56469,56676,56697,56695,57002,56565,57038,56464,56562,56524,57184,57166,56991,56284,56650,56992,56936,56484,57183,56944,57190,57137,56926,57205,56758,56273,56491,56742,57165,56292,56760,56735,57219,56913,56671,56753,56681,56966,57195,56973,57150,56680,56616,56601,56566,56644,57176,56465,56478,56728,57081,57214,57080,56250,56732,56700,56538,56725,57108,57133,56542,56924,56513,56900,56541,56698,56941,57173,56747,56609,56550,57000,56437,56683,56988,56569,57010,56989,56962,57034,56519,56660,57036,57051,57042,56777,57020,57021,57131,57008,57100,57191,57026,56901,56752,56928,56967,56619,56934,56918,56774,56259,57094,57092,56257,56617,57197,56942,57124,56933,56685,56755,56598,57128,56699,56310,56522,57070,56314,57082,56708,56481,56602,56637,56694,56690,56315,56643,56244,56528,56560,57113,56247,56911,56772,56954,57182,56686,56977,56318,57152,57006,56256,56994,56258,56267,57211,57135,56454,56693,56563,57216,56986,56903,57105,56981,56976,56479,57218,56610,56623,57186,56682,56649,56763,56659,56595,57031,56544,56255,56514,56496,56935,57043,56288,56908,56467,57104,57139,56724,57032,56985,56549,56921,57136,56507,57196,57018,57027,56440,56289,56999,57158,56738,57203,57132,56452,57111,56272,56455,56969,56253,56704,56516,56254,56642,57192,57116,56293,56505,56271,56648,56769,56925,57198,56487,56508,57110,56545,56240,56473,56722,56636,56632,56490,56707,57213,56675,56902,57099,56916,56684,57112,56998,56290,56667,56480,56450,57023,57049,57033,57052,56968,56773,56701,56620,56778,56546,57194,56761,56750,57114,57106,57149,57117,56748,56696,56276,56510,56512,56930,56691,57090,57040,56776,56611,57075,56477,56957,56504,56754,56955,56978,56246,56543,57162,56556,56980,56451,56663,56723,56285,56468,57004,57048,56744,56987,56489,56997,56975,57039,56720,56645,56605,57164,56249,56947,57172,56946,57009,56475,56647,57140,57129,56618,56596,57095,56727,56657,57161,56511,56692,56568,56316,56917,56996,56915,57087,56662,56932,56730,57170,56664,56443,56447,56990,56929,56627,56670,56552,56731,56749,57107,56906,57096,56634,56964,56603,57127,57083,57028,56995,57151,57215,56734,56771,56567,56635,56615,56558,57072,56770,57121,56472,56291,57157,56979,56775,56739,57175,57005,57076,57101,56251,56971,56768,57079,56736,56746,56673,57044,56669,56641,56914,57174,56726,56520,56606,56922,57167,56733,56518,57177,56488,56446,56317,57193,56919,56554,57091,56762,56243,57207,56269,56920,56445,57119,57103,56646};
		int randomInt = (int)(Math.random()*(uids.length));
		uid = uids[randomInt];
		
		//content = getFilterContent(content, uid, id, type);
		oList = WebUtils.checkIsAt(content, oList, ucenterFacade);
		String resultStr = WebUtils.putDataToNode(content, "0", oList);
		ActComment actComment = new ActComment();
		actComment = actFacade.doOneComment(null, uid, id, type, resultStr,commentUid,commentId,res_userid);
		int flagint = actComment.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			if(res_userid.longValue()!=uid.longValue()){
				//添加评论人的消息数量
				userJedisManager.incrOneUserInfo(res_userid, JedisConstant.USER_HASH_COMMENT_NOTICE);
			}
			long commentUserId = actComment.getCommentUserId();
			if(0l!=commentUserId&&commentUserId!=res_userid){//如果回复的话添加回复人的红点提示
				userJedisManager.incrOneUserInfo(commentUserId, JedisConstant.USER_HASH_COMMENT_NOTICE);
			}
			//TODO: 将评论后的附加信息放入缓存中
			try {
				int cNum = actFacade.findCommentCount(null, id);
				updateResourceByJedis(id, uid, JedisConstant.RELATION_COMMENT_NUM, cNum+"");
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
//			//评论后的资源放入到缓存中
//			if(!CommentUtils.TYPE_COMMENT.equals(type)){
//				ResourceInfo resourceInfo = getResourceByIdAndType(id, type, uid);
//				actUtils.putIsCollectToResoure(resourceInfo, uid, actFacade, 0);
//				resourceJedisManager.saveOneResource(id, resourceInfo);
//			}
			if(null!=oList&&oList.size()>0){
				long rid = actComment.getId();
				String resType = CommentUtils.TYPE_COMMENT;
				Iterator<Map<String, String>> oListIt = oList.iterator();
				String topicName = "";
				String oListType = "";
				Long toUid = 0l;
				while(oListIt.hasNext()){
					Map<String, String> oneListmap = oListIt.next();
					topicName = oneListmap.get("name");
					oListType = oneListmap.get("type");
					if(oListType.equals(CommentUtils.TYPE_USER)){
						//添加@推送消息
						try{
							JSONObject json = new JSONObject();
							toUid = Long.valueOf(oneListmap.get("id"));
							//插入@信息
							userJedisManager.incrOneUserInfo(toUid, JedisConstant.USER_HASH_AT_NOTICE);
							json.put("uid", uid);
							json.put("toUid", toUid);
							json.put("rid", rid);
							json.put("type", CommentUtils.TYPE_COMMENT);
							json.put("pushType", PushManager.PUSH_AT_TYPE);
							json.put("context", resultStr.replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
							json.toString();
							eagleProducer.send("pushMessage", "toBody", "", json.toString());
							actFacade.insertintoActAt(uid, rid, uid, CommentUtils.TYPE_COMMENT, toUid,id,type);
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			//推送消息
			long begin = System.currentTimeMillis();
			try {
				String pushType ="";
				if(commentId == 0){
					pushType = PushManager.PUSH_COMMENT_TYPE;
				}else{
					pushType = PushManager.PUSH_COMMENT_TO_TYPE;
				}
				JSONObject json = new JSONObject();
				json.put("uid", uid);
				json.put("toUid", commentUid);
				json.put("rid", id);
				json.put("type", type);
				json.put("pushType", pushType);
				json.put("context", actComment.getCommentContext().replaceAll("<\\!\\-\\-.*\\-\\->", "").replaceAll("<[^>]*>", ""));
				json.toString();
				eagleProducer.send("pushMessage", "toBody", "", json.toString());
				//pushManager.pushResourceMSG(uid, commentUid, id, type, pushType);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			long end = System.currentTimeMillis();
			System.out.println("异步评论耗时："+(end-begin));
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: delComment</p> 
	 * <p>Description: 删除评论</p> 
	 * @author :changjiang
	 * date 2015-6-12 上午11:25:20
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String delComment(String reqs,Long uid){
		Map<String, Object> req ;
		Map<String, Object> dataq;
		Map<String, Object> datas ;
		String resString;//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error;
//		LOG.info("客户端json数据："+reqs);
		long id = 0;
		int status = 0;
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
			req = (Map<String, Object>) req.get("req");
			dataq = (Map<String, Object>) req.get("data");
			
			id = Long.valueOf(dataq.get("id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int flagint = actFacade.delCommentById(id);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			//收藏数量
			//int fNum = actFacade.findCollectCount(actCollect.getResourceId());
			flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("num", fNum);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);
		
		return resString;
	}
	
	/**
	 * 
	 * <p>Title: insidePraise</p> 
	 * <p>Description: 点赞</p> 
	 * @author :changjiang
	 * date 2015-6-12 下午12:00:58
	 * @param reqs
	 * @param uid
	 * @return
	 */
	public String insidePraise(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
		Map<String, Object> req =null;
		Map<String, Object> dataq=null;
		Map<String, Object> datas =null;
		String resString="";//返回数据
		String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
		String error="";
		
		//去掉空格
		reqs = reqs.trim();
		
		//转化成可读类型
		try {
			req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(req);
		
		
		req = (Map<String, Object>) req.get("req");
		dataq = (Map<String, Object>) req.get("data");
		
		Long id = Long.valueOf(dataq.get("id").toString());
		String type = (String) dataq.get("type");
		String status = (String) dataq.get("status");
		Long toUid = 0l;
		try {
			toUid = Long.valueOf(dataq.get("praiseUserid").toString());
			if(toUid == null){
				toUid = 0l;
			}
		} catch (Exception e) {
			toUid = 0l;
		}
		Long res_userid = 0l;
		try {
			res_userid = Long.valueOf(dataq.get("resUserid").toString());
			if(res_userid == null){
				res_userid = 0l;
			}
		} catch (Exception e) {
			res_userid = 0l;
		}
		
		long[] uids = {2841,960,326,224,54088,327,469,581,479,576,312,587,583,54042,320,590,334,54050,54049,593,557,555,575,470,1022,329,957,31199,54060,577,156,460,31127,54045,471,188,591,53993,585,474,180,190,584,31126,54090,683,54055,463,51192,54091,594,54047,335,686,553,406,578,658,645,589,54048,54059,54046,54062,54041,333,473,181,403,31198,321,554,687,685,179,579,195,956,580,588,54093,189,346,31343,155,31128,592,126,54061,313,314,475,54087,191,54044,54063,54064,655,315,201,688,31124,54092,200,556,661,461,660,478,402,317,582,659,194,175,462,171,477,322,459,476,193,552,54057,458,31338,244,958,472,358,959,54058,468,330,316,54094,54086,328,31341,54043,174,185,167,467,586,684,339
				,56905,56904,57179,57050,56557,56983,56597,56945,57120,56607,56599,56982,56940,56462,56939,57171,57188,56943,57125,57169,57189,56238,56759,56633,56456,56937,57030,56482,57178,56710,56756,57074,57071,56909,56703,56245,56509,56666,57053,56448,56485,56540,56630,56441,57212,56526,57148,56453,57102,56444,56688,56672,56625,56689,57122,56687,56923,57115,56319,56474,57007,56953,57156,56705,57084,56912,57024,57047,57035,56970,57181,57046,57025,57209,56521,56561,56308,56551,56679,56242,56486,56949,57126,56965,56948,57163,57093,56286,56241,57123,56594,56275,56741,56553,57089,56956,57054,56539,56665,57088,57187,56626,56631,56495,56493,57134,56311,56470,56438,56459,56261,56638,56312,56463,56608,56506,56264,56779,57109,56494,56458,57180,57159,57029,56668,56658,56265,57204,56270,56621,57217,57045,56984,56963,56614,56910,56677,57086,56287,57073,57001,57210,56564,56548,56938,56907,56993,56951,57097,56751,57168,56709,57041,57022,56640,57146,57160,56629,57147,56537,56600,56674,57138,56466,56729,56469,56676,56697,56695,57002,56565,57038,56464,56562,56524,57184,57166,56991,56284,56650,56992,56936,56484,57183,56944,57190,57137,56926,57205,56758,56273,56491,56742,57165,56292,56760,56735,57219,56913,56671,56753,56681,56966,57195,56973,57150,56680,56616,56601,56566,56644,57176,56465,56478,56728,57081,57214,57080,56250,56732,56700,56538,56725,57108,57133,56542,56924,56513,56900,56541,56698,56941,57173,56747,56609,56550,57000,56437,56683,56988,56569,57010,56989,56962,57034,56519,56660,57036,57051,57042,56777,57020,57021,57131,57008,57100,57191,57026,56901,56752,56928,56967,56619,56934,56918,56774,56259,57094,57092,56257,56617,57197,56942,57124,56933,56685,56755,56598,57128,56699,56310,56522,57070,56314,57082,56708,56481,56602,56637,56694,56690,56315,56643,56244,56528,56560,57113,56247,56911,56772,56954,57182,56686,56977,56318,57152,57006,56256,56994,56258,56267,57211,57135,56454,56693,56563,57216,56986,56903,57105,56981,56976,56479,57218,56610,56623,57186,56682,56649,56763,56659,56595,57031,56544,56255,56514,56496,56935,57043,56288,56908,56467,57104,57139,56724,57032,56985,56549,56921,57136,56507,57196,57018,57027,56440,56289,56999,57158,56738,57203,57132,56452,57111,56272,56455,56969,56253,56704,56516,56254,56642,57192,57116,56293,56505,56271,56648,56769,56925,57198,56487,56508,57110,56545,56240,56473,56722,56636,56632,56490,56707,57213,56675,56902,57099,56916,56684,57112,56998,56290,56667,56480,56450,57023,57049,57033,57052,56968,56773,56701,56620,56778,56546,57194,56761,56750,57114,57106,57149,57117,56748,56696,56276,56510,56512,56930,56691,57090,57040,56776,56611,57075,56477,56957,56504,56754,56955,56978,56246,56543,57162,56556,56980,56451,56663,56723,56285,56468,57004,57048,56744,56987,56489,56997,56975,57039,56720,56645,56605,57164,56249,56947,57172,56946,57009,56475,56647,57140,57129,56618,56596,57095,56727,56657,57161,56511,56692,56568,56316,56917,56996,56915,57087,56662,56932,56730,57170,56664,56443,56447,56990,56929,56627,56670,56552,56731,56749,57107,56906,57096,56634,56964,56603,57127,57083,57028,56995,57151,57215,56734,56771,56567,56635,56615,56558,57072,56770,57121,56472,56291,57157,56979,56775,56739,57175,57005,57076,57101,56251,56971,56768,57079,56736,56746,56673,57044,56669,56641,56914,57174,56726,56520,56606,56922,57167,56733,56518,57177,56488,56446,56317,57193,56919,56554,57091,56762,56243,57207,56269,56920,56445,57119,57103,56646};
		int randomInt = (int)(Math.random()*(uids.length));
		uid = uids[randomInt];
		
		//ActPraise act = null;


		ActUseful actUseful = new ActUseful();
		//有用没用或者取消选择
		if(CommentUtils.COMMENT_USEFUL.equals(status)||CommentUtils.COMMENT_USELESS.equals(status)||CommentUtils.COMMENT_NOCHOOSEUSE.equals(status)){
			actUseful = actFacade.doUseful(id, type, res_userid, Integer.valueOf(status), uid);
		}

		int flagint = actUseful.getFlag();
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;


			//有用的个数加一
			if(0l!=res_userid&&CommentUtils.COMMENT_USEFUL.equals(status)){
				userJedisManager.incrOneUserInfo(res_userid, JedisConstant.USER_HASH_USEFUL_NOTICE);
			}
			//用户对资源是否有用这里状态更新
			relationToUserAndResManager.saveOneRelation(uid, id, JedisConstant.RLEATION_IS_USEFUL, actUseful.getIsUseful()+"");
			//资源的有用没用数更新
			Map<String, Object> usefulMap = new HashMap<String, Object>();
			usefulMap = actFacade.findUsefulCount(id);
			int usefulCount = (Integer) usefulMap.get("usefulCount");
			Map<String, Object> uselessMap = new HashMap<String, Object>();
			uselessMap = actFacade.findUselessCount(id);
			int uselessCount = (Integer) uselessMap.get("uselessCount");
			relationToUserAndResManager.saveOneRelationToRes(id, JedisConstant.RELATION_USEFUL_NUM, usefulCount+"");
			relationToUserAndResManager.saveOneRelationToRes(id, JedisConstant.RELATION_USELESS_NUM, uselessCount+"");
			datas.put("usefulCount", usefulCount);
			datas.put("uselessCount", uselessCount);



			if(actUseful.getResType().equals(CommentUtils.TYPE_BOOK_COMMENT)){
				BkComment bkComment = bkCommentFacade.findCommentIsExistById(actUseful.getResourceId());
				if(null!=bkComment&&bkComment.getId()!=0){
					//更细点赞的数量
					//System.out.println("书评点赞更新");
					resStatJedisManager.doUsefulNum(id, type, bkComment.getBookId(), bkComment.getResType(),usefulCount,0);
					//resStatJedisManager.doNousefulNum(resourceId, type);
					//resStatJedisManager.updateActPraiseNum(id, type, act,bkComment.getBookId(),bkComment.getResType());
				}
			}else if(actUseful.getResType().equals(CommentUtils.TYPE_MOVIE_COMMENT)){
				MvComment mvComment = mvCommentFacade.findMvCommentIsExist(actUseful.getResourceId());
				if(null!=mvComment&&mvComment.getFlag()==ResultUtils.SUCCESS){
					//更细点赞的数量
					//System.out.println("影评点赞更新");
					//resStatJedisManager.doNousefulNum(resourceId, type);
					resStatJedisManager.doUsefulNum(id, type, mvComment.getMovieId(), CommentUtils.TYPE_MOVIE,usefulCount,mvComment.getStageid());
					//resStatJedisManager.updateActPraiseNum(id, type, act,mvComment.getMovieId(),CommentUtils.TYPE_MOVIE);
				}
			}
			//推送消息
			/*if(CommentUtils.COMMENT_USEFUL.equals(status)){
				//推送消息
				long begin = System.currentTimeMillis();
				try {
					begin = System.currentTimeMillis();
					JSONObject json = new JSONObject();
					json.put("uid", uid);
					json.put("toUid", res_userid);
					json.put("rid", id);
					json.put("type", type);
					json.put("pushType", PushManager.PUSH_PARISE_TYPE);
					json.toString();
					long end = System.currentTimeMillis();
					//System.out.println("json包装耗时："+(end-begin));
					//eagleProducer.send("pushMessage", "toBody", "", json.toString());
					//pushManager.pushResourceMSG(uid, commentUid, id, type, pushType);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}*/
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			LOG.error("错误代号:"+flagint+",错误信息:"+error);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		resString = getResponseData(datas);

		return resString;
	}
	
	public void updateResourceByJedis(Long rid,Long uid,String key,String value){
		long begin = System.currentTimeMillis();
		try {
			String result = relationToUserAndResManager.saveOneRelationToRes(rid, key, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		long end = System.currentTimeMillis();
		System.out.println("添加["+key+"]信息到资源缓存的耗时："+(end-begin));
		
	}
	
	/**
	 * 修改资源是修改缓存中的信息(isPraise)
	 * @param rid 资源id
	 * @param uid 用户id
	 * @param key 信息key
	 * @param value 信息
	 */
	public void updateIsPraiseByJedis(Long rid,Long uid,String key,String value){
		//long begin = System.currentTimeMillis();
		try {
			String result = relationToUserAndResManager.saveOneRelation(uid, rid, key, value);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		/*long end = System.currentTimeMillis();
		System.out.println("添加["+key+"]信息到资源缓存的耗时："+(end-begin));*/
		
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setUserJedisManager(UserJedisManager userJedisManager) {
		this.userJedisManager = userJedisManager;
	}

	public void setEagleProducer(RocketProducer eagleProducer) {
		this.eagleProducer = eagleProducer;
	}

	public void setRelationToUserAndResManager(
			RelationToUserAndResManager relationToUserAndResManager) {
		this.relationToUserAndResManager = relationToUserAndResManager;
	}

	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}

	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}

	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}
}
