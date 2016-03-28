
package com.poison.eagle.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.keel.common.event.rocketmq.RocketProducer;
import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.manager.UserRegLoginManager;
import com.poison.eagle.manager.otherinterface.PushManager;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserInfo;


public class WebUtils   {
	//标示
	public  static final String TYPE_DIV = "0";//文字
	public  static final String TYPE_IMG = "1";//图片
	public  static final String TYPE_AUDIO = "2";//音频
	public  static final String TYPE_VIDEO = "3";//视频
	public static final String BR = "<br/>";
	public static final String FGF = "<!--我是分隔符woshifengefu-->";//<!--我是分隔符-->
	public static final String FGF_C = "<!--我是分隔符-->";//<!--我是分隔符-->
	//获取数据常量
	public  static final String TYPE = "type";
	public  static final String DATA = "content";
	public static final String LAZY_ORIGINALSRC = "originalSrc";
	public static final String LAZY_SRC = "src";
	public static final String HTML_CSS_FOR_ARTICLE_END = "</body></html>";
	public static final String HTML_CSS_FOR_ARTICLE_BOOK_MOVIE =
			"<style>.content_position{position:relative;top:-81px}.bm_div{margin:20px 20px 0 20px; height:79px;}" +
			".bm { left:0; top:-83px; float:left; height:100%; width:100%; position: relative !important;}" +
			"#yz{height:79px;margin: 0 0 0 70px}.dt1{font-size:15px; color:#444444;padding:9px 0 0 0;font-weight:bold}" +
			".dt2{font-size: 12px; color:#6e6e6e; padding: 6px 0 0 0;}.dt3{font-size: 12px; color:#6e6e6e;padding: 3px 0 3px 0;}</style>";
	public static final String HTML_DIV_FOR_ARTICLE_BOOK_MOVIE0 =
			"<a href=\"";
	public static final String HTML_DIV_FOR_ARTICLE_BOOK_MOVIE1 =
			"\"><div class=\"bm_div\"><img style=\"width:100%;height:100%\" src=\"http://112.126.68.72/image/common/4ac046cde77a26fb91e69a23acbf41cd.png\" />" +
			"<div class=\"bm\" ><div style=\"float:left;width:53px;border:none;\"><img src=\"";
	public static final String HTML_DIV_FOR_ARTICLE_BOOK_MOVIE2 =
			"\" width=\"53\" height=\"78\" /></div><dl id=\"yz\"  ><dt class=\"dt1\">";
	public static final String HTML_DIV_FOR_ARTICLE_BOOK_MOVIE3 =
			"</dt><dt class=\"dt2\">";
	public static final String HTML_DIV_FOR_ARTICLE_BOOK_MOVIE4 =
			"</dt><dt class=\"dt3\">我给它打";
	public static final String HTML_DIV_FOR_ARTICLE_BOOK_MOVIE5 =
			"分</dt></dl></div></div><div style=\"clear:both\"></div></a>";
	private static final  Log LOG = LogFactory.getLog(WebUtils.class);
	private static final String TYPE_HT = "0";//#
	private static final String TYPE_ACT = "1";//@
	//公共地址
	private static final String UPLOAD_IMG = CommentUtils.WEB_SERVER+"";
	private static final String UPLOAD_AUDIO = CommentUtils.WEB_SERVER+"";
	private static final String UPLOAD_VIDEO = CommentUtils.WEB_SERVER+"";
	//视频常量
	private static final String VIDEO_BEGIN = "<video src=\"";
	private static final String VIDEO_END  ="\" controls=\"contro" +
			"ls\"></video>";
	//音频常量
	private static final String AUDIO_BEGIN = "<audio src=\"";
	private static final String AUDIO_END = "\" controls=\"controls\"></audio>";
	//图片常量
	private static final String IMG_BEGIN = "<img src=\"";
	private static final String IMG_END = "\"/>";
	//文字常量
	private static final String DIV_BEGIN = "<div>";
	private static final String DIV_END = "</div>";
	//文字常量
	private static final String P_BEGIN = "<P>";
	private static final String P_END = "</p>";
	//文字常量
	private static final String DIV_BEGIN_CLASS = "<div class=\"content\">";
	private static final String DIV_END_CLASS = "</div>";
	//xml常量
	private static final String XML_BEGIN = "<!--<xml>";
	private static final String XML_END = "</xml-->";
	//id常量
	private static final String ID_BEGIN = "<!--<id>";
	private static final String ID_END = "</id>-->";
	//index常量
	private static final String INDEX_BEGIN = "<!--<index>";
	private static final String INDEX_END = "</index>-->";
	//name常量
	private static final String NAME_BEGIN = "<!--<name>";
	private static final String NAME_END = "</name>-->";
	//type常量
	private static final String TYPE_BEGIN = "<!--<type>";
	private static final String TYPE_END = "</type>-->";
	//话题@
	private static final String HT = "#";
	private static final String ACT_BEGIN = "@";
	private static final String ACT_END = " ";
	//@#等关键字的集合
	private static final String KLIST = "kList";
	//《》等关键字的集合
	private static final String OLIST = "oList";
	private static final String TYPE_INDEX = "index";
	private static final String TYPE_ID = "id";
	private static final String TYPE_NAME = "name";
	private static final String LINK_TYPE = "linkType";
	private static final String LINK_NAME = "linkName";
	private static final String RESULT_CONTENT = "resultContent";
	private static final String BOOK_TYPE_BEGIN = "《";
	private static final String BOOK_TYPE_END = "》";
	private static final String BOOK_TYPE_MID = "|";
	private static final String SIZE = "size";
	private static final String LENGTH = "length";
	private static final String SYS_FONT_PATH = "http://s.duyao001.com/static/article/zt/FZXH-GB18030.ttf";//方正细黑字体
	public static final String HTML_CSS_FOR_ARTICLE_BEGIN = "<!DOCTYPE HTML><html><head><meta charset=\"UTF-8\">" +
			"<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=no\"/></head>" +
			"<style type=\"text/css\">img{width:100% !important;}p{line-height:24px !important;color: #3d3d3d !important;font-size: 15px !important;}@font-face {src: url('"+SYS_FONT_PATH+"');}.title{color: #232323;margin: 19px 15px 9px 15px;font-size: 20px;line-height: 32px;}.author{color:#888;font-size: 12px;line-height: 12px;margin: 0 15px 29px 15px;}.content{color: #585858;margin: 20px 15px 0 15px;font-size: 15px;word-break:break-all;}</style>" +
			"<script type=\"text/javascript\">function onLoad(){var name = \"num\";var str=window.location.search;if (str.indexOf(name)!=-1){var pos_start=str.indexOf(name)+name.length+1;var pos_end=str.indexOf(\"&\",pos_start);if (pos_end==-1){document.getElementById(\"readNum\").innerHTML =str.substring(pos_start);}else{document.getElementById(\"readNum\").innerHTML =str.substring(pos_start,pos_end);}}}</script>" +
			"<body style=\"font:normal 100%  方正细黑_YS;font-family:  方正细黑_YS !important; padding: 0 auto; margin: 0 auto;text-align:justify;text-justify:inter-ideograph;\" onload=\"onLoad()\">" +
			"<script src=\""+CommentUtils.WEB_JQUERY_1_6_JS+"\" type=\"text/javascript\"></script>" +
			"<script src=\""+CommentUtils.WEB_LYZ_DELAYLOADING_MIN_JS+"\" type=\"text/javascript\">" +
			"</script><script type=\"text/javascript\">$(function (){var $imgs = $(\"img\");$imgs.each(function(i, item){var $item = $(item);var src = $item.attr(\"originalsrc\");if(src)$item.attr(\"originalsrc\", src.replace(/\\?([^=]*=[^&]*&?)+/, \"\"));});$imgs.delayLoading({defaultImg: \""+CommentUtils.WEB_LOADING_JPG+"\",errorImg: \"\",imgSrcAttr: \"originalSrc\",beforehand: 0,event: \"scroll\",duration: \"normal\",container: window,});});</script>";
	private static final String IMG_URL = "http://e.hiphotos.baidu.com/image/pic/item/8cb1cb134954092379a0ad779058d109b2de49ea.jpg";
	private static final String AUDIO_URL = "http://qzone.haoduoge.com/up/11/5py05qCRIC0g5bmz5Yeh5LmL6LevMS5tcDM=.mp3";
	private static final String WH_BEGIN = "style=\"height";
	private static final String WH_END = "px\"";
	//被act人的昵称集合
	private static List<String> actNameList;//被act人的名字集合
	private static List<String> photoList;//相册照片的集合
	private static List<Map<String, String>> oList ;
	private static List<String> tList;
	private static FileUtils fileUtils = FileUtils.getInstance();
	private static HttpUtils httpUtils = HttpUtils.getInstance();
	private UcenterFacade ucenterFacade;
	
	/**
	 * 公共的初始化方法
	 */
	private static void init(){
		if(actNameList == null){
			actNameList = new ArrayList<String>();
//			System.out.println("初始化");
		}else{
			actNameList.clear();
//			System.out.println("清空");
		}
		if(photoList == null){
			photoList = new ArrayList<String>();
		}else{
			photoList.clear();
		}
	}

	/**
	 * 将各种元素变成html
	 * @param name
	 * @param image
	 * @param video
	 * @param audio
	 * @return
	 */
	public static String putStringImageVideoAudioToHTML5(String name,String image,String video,String audio){
		StringBuffer html = new StringBuffer();
		if(name != null){

			html.append(putDataToNode(name, TYPE_DIV));
		}
		if(image != null){

			html.append(putDataToNode(image, TYPE_IMG));
		}
		if(video !=null){
			html.append(putDataToNode(image, TYPE_VIDEO));
		}
		if(audio != null){
			html.append(putDataToNode(image, TYPE_AUDIO));
		}

		return html.toString();
	}
	
	/**
	 * 将各种元素变成html
	 * @param name
	 * @param image
	 * @param video
	 * @param audio
	 * @return
	 */
	public static String putStringImageVideoAudioToHTML5OnClass(String name,String image,String video,String audio){
		StringBuffer html = new StringBuffer();
		if(name != null){

			html.append(putDataToNodeOnClass(name, TYPE_DIV));
		}
		if(image != null){

			html.append(putDataToNodeOnClass(image, TYPE_IMG));
		}
		if(video !=null){
			html.append(putDataToNodeOnClass(image, TYPE_VIDEO));
		}
		if(audio != null){
			html.append(putDataToNodeOnClass(image, TYPE_AUDIO));
		}

		return html.toString();
	}
	
	/**
	 * 将字符串转换为html格式代码
	 * @param data 0 文字
	 * @return
	 */
	public static String putStringToHTML5(String data){
		return putDataToNode(data, TYPE_DIV);
	}
	
	/**
	 * 将接收到的list分类数据拼接成为html格式代码
	 * @param list 0 文字,1 图片,2 音频,3 视频
	 * @return
	 */
	public static String putDataToHTML5(List<Map<String,Object>> list){

		if(null==list||list.isEmpty()){
			return "";
		}

		StringBuffer html = new StringBuffer("");
		for (Map<String, Object> map : list) {
			String type = (String) map.get(TYPE);
			String data = (String) map.get(DATA);

			if(TYPE_DIV.equals(type)){
				oList = (List) map.get(OLIST);
				tList = (List<String>) map.get("tList");
			}

			html.append(putDataToNode(data, type));

		}
		String str = html.toString();
		return str;
	}
	
	/**
	 *
	 * <p>Title: putDataToMap</p>
	 * <p>Description: 将类型存储为map</p>
	 * @author :changjiang
	 * date 2015-4-16 下午2:55:51
	 * @param list
	 * @return
	 */
	public static Map<String, Object> putDataToMap(List<Map<String,Object>> list,UcenterFacade ucenterFacade){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(null==list||list.isEmpty()){
			return resultMap;
		}
		StringBuffer html = new StringBuffer("");
		String LinkType = "";
		String LinkName = "";
		String content = "";
		for (Map<String, Object> map : list) {
			String type = (String) map.get(TYPE);
			String data = (String) map.get(DATA);
			//判断是否为话题
			if(TYPE_DIV.equals(type)){
				oList = (List) map.get(OLIST);
				if(null!=oList&&oList.size()>0){
					Map<String, String> oListMap = oList.get(0);
					if(null!=oListMap&&!oListMap.isEmpty()){
						LinkType = oListMap.get(TYPE);
						LinkName = oListMap.get(TYPE_NAME);
					}
				}
				//检测是否是话题
				oList = checkIsTopic(data,oList);
				if(ucenterFacade!=null){
					oList = checkIsAt(data, oList, ucenterFacade);
				}
				tList = (List<String>) map.get("tList");
			}
			html.append(putDataToNode(data, type));
		}
		String str = html.toString();
		resultMap.put(LINK_TYPE, LinkType);
		resultMap.put(LINK_NAME, LinkName);
		resultMap.put(RESULT_CONTENT, str);
		resultMap.put("oList", oList);//话题列表
		return resultMap;
	}

	/**
	 * 将接收到的list分类数据拼接成为html格式代码
	 * @param list 0 文字,1 图片,2 音频,3 视频
	 * @return
	 */
	public static String putDataToHTML5OnClass(List<Map<String,Object>> list){

		StringBuffer html = new StringBuffer();
		for (Map<String, Object> map : list) {
			String type = (String) map.get(TYPE);
			String data = (String) map.get(DATA);

//			if(TYPE_DIV.equals(type)){
//				oList = (List) map.get(OLIST);
//				tList = (List<String>) map.get("tList");
//			}
//
			html.append(putDataToNodeOnClass(data, type));

		}
		return html.toString();
	}
	
	/**
	 * 将数据拼接为html节点
	 * eq:<div>data</div>
	 * @param data 数据
	 * @param type 0 文字,1 图片,2 音频,3 视频
	 * @return
	 */
	public static String putDataToNode(String data , String type){
		StringBuffer html = new StringBuffer("");

		if("".equals(data)){
			return html.toString();
		}

		if(TYPE_DIV.equals(type)){
			//data = matchTopic(data);
//			data = checkBMListInData(data, tList);
			//StringBuffer dataSB = new StringBuffer();
			html.append(DIV_BEGIN + data );
			//收集并向act到的人发送消息
			//putDataToAct(data);

			if(oList != null){
				if(oList.size()>0){
					for (Map<String, String> map : oList) {
						String id = map.get(TYPE_ID);
						String index = map.get(TYPE_INDEX);
						String name = map.get(TYPE_NAME);
						String type_ = map.get(TYPE);
						html.append(putODataToHtml(id, index, name, type_));
					}
				}
			}
			html.append(DIV_END);
		}else if(TYPE_IMG.equals(type)){
			html.append(IMG_BEGIN + data + IMG_END);
			//将图片收集起来
			putPhotoToList(data);
		}else if(TYPE_AUDIO.equals(type)){
			html.append(AUDIO_BEGIN + data + AUDIO_END);
		}else if(TYPE_VIDEO.equals(type)){
			html.append(VIDEO_BEGIN + data + VIDEO_END);
		}
		html.append(BR+FGF);
		return html.toString();
	}
	
	/**
	 *
	 * <p>Title: putDataToNode</p>
	 * <p>Description: 添加html节点</p>
	 * @author :changjiang
	 * date 2015-4-27 下午4:37:33
	 * @param data
	 * @param type
	 * @param oList
	 * @return
	 */
	public static String putDataToNode(String data , String type,List<Map<String, String>> oList){
		StringBuffer html = new StringBuffer("");
		if("".equals(data)){
			return html.toString();
		}

		if(TYPE_DIV.equals(type)){
			//data = matchTopic(data);
//			data = checkBMListInData(data, tList);
			//StringBuffer dataSB = new StringBuffer();
			html.append(DIV_BEGIN + data );
			//收集并向act到的人发送消息
			//putDataToAct(data);
			if(oList != null){
				if(oList.size()>0){
					for (Map<String, String> map : oList) {
						String id = map.get(TYPE_ID);
						String index = map.get(TYPE_INDEX);
						String name = map.get(TYPE_NAME);
						String type_ = map.get(TYPE);
						html.append(putODataToHtml(id, index, name, type_));
					}
				}
			}
			html.append(DIV_END);
		}else if(TYPE_IMG.equals(type)){
			html.append(IMG_BEGIN + data + IMG_END);
			//将图片收集起来
			putPhotoToList(data);
		}else if(TYPE_AUDIO.equals(type)){
			html.append(AUDIO_BEGIN + data + AUDIO_END);
		}else if(TYPE_VIDEO.equals(type)){
			html.append(VIDEO_BEGIN + data + VIDEO_END);
		}
		html.append(BR+FGF);
		return html.toString();
	}
	
	/**
	 * 将数据拼接为html节点
	 * eq:<div>data</div>
	 * @param data 数据
	 * @param type 0 文字,1 图片,2 音频,3 视频
	 * @return
	 */
	public static String putDataToNodeOnClass(String data , String type){
		StringBuffer html = new StringBuffer();

		if(TYPE_DIV.equals(type)){
			data = matchTopic(data);
//			data = checkBMListInData(data, tList);
			html.append(DIV_BEGIN_CLASS + data );
			//收集并向act到的人发送消息
//			putDataToAct(data);
//
//			if(oList != null){
//				if(oList.size()>0){
//					for (Map<String, String> map : oList) {
//						String id = map.get(TYPE_ID);
//						String index = map.get(TYPE_INDEX);
//						String name = map.get(TYPE_NAME);
//						String type_ = map.get(TYPE);
//						html.append(putODataToHtml(id, index, name, type_));
//					}
//				}
//			}
			html.append(DIV_END);
		}else if(TYPE_IMG.equals(type)){
			html.append(IMG_BEGIN + data + IMG_END);
			//将图片收集起来
//			putPhotoToList(data);
		}else if(TYPE_AUDIO.equals(type)){
			html.append(AUDIO_BEGIN + data + AUDIO_END);
		}else if(TYPE_VIDEO.equals(type)){
			html.append(VIDEO_BEGIN + data + VIDEO_END);
		}
		html.append(BR+FGF);
		return html.toString();
	}

	/**
	 * 将data中的数据格式化
	 * @param data
	 * @param oList
	 * @return
	 */
	private static String checkBMListInData(String data , List<String> oList){
		for (String s : oList) {
			String str = s.substring(s.indexOf(BOOK_TYPE_BEGIN)+1, s.indexOf(BOOK_TYPE_MID));
			if(data.indexOf(str)>0){
				data = data.replace(data.substring(data.indexOf(str)-1, data.indexOf(str)+str.length()+1), s);
			}
		}

		return data;
	}
	
	/**
	 * 将数据拼接为html节点
	 * eq:<div>data</div>
	 * @param data 数据
	 * @param type 0 文字,1 图片,2 音频,3 视频
	 * @return
	 */
	public static List<Map<String, Object>> putDataToList(String data , String type){
		StringBuffer html = new StringBuffer();
		List<Map<String, Object>> kList = new ArrayList<Map<String,Object>>();//存放#@关键字的分类
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TYPE, type);
		map.put(DATA, data);
		kList = putDataToAct(data);
		map.put(KLIST, kList);
		list.add(map);
		return list;
	}
	
	/**
	 * 将《》的信息村委html
	 * @param id
	 * @param index
	 * @param name
	 * @param type
	 * @return
	 */
	private static String putODataToHtml(String id,String index,String name,String type){
		StringBuffer sb = new StringBuffer();
		sb.append(ID_BEGIN+id+ID_END);
		sb.append(INDEX_BEGIN+index+INDEX_END);
		sb.append(NAME_BEGIN+name+NAME_END);
		sb.append(TYPE_BEGIN+type+TYPE_END);
		sb.append(XML_BEGIN+XML_END);

		return sb.toString();
	}

	/**
	 * 将html节点拆分为普通数据
	 * eq:<div>data</div>
	 * @param data 数据
	 * @param type 0 文字,1 图片,2 音频,3 视频
	 * @return
	 */
	public static List putHTMLToData(String html ){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(html == null || "".equals(html)){
			return list;
		}

		//需要处理html标签

		if(html.indexOf(FGF)<0){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TYPE, TYPE_DIV);
			map.put(DATA, html.replaceAll("<[^>]*>", ""));
			list.add(map);
			return list;
		}

		String[] htmls = html.split(FGF);
		boolean flag = false;
		List<Map<String, Object>> kList = new ArrayList<Map<String,Object>>();//存放#@关键字的分类
		for (String data : htmls) {
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				if(data.startsWith(DIV_BEGIN)){//文字
					data = data.substring(data.indexOf(DIV_BEGIN)+DIV_BEGIN.length(), data.indexOf(DIV_END));
					map.put(TYPE, TYPE_DIV);
					//将#@分类出来
					kList = putDataToAct(data);
					map.put(KLIST, kList);
					//将《》等信息分离出来
					oList = new ArrayList<Map<String, String>>();
					if(data.indexOf(XML_BEGIN+XML_END)>0){
						String oData = data.substring(data.indexOf(ID_BEGIN), data.lastIndexOf(XML_END)+XML_END.length());
						oList = putDataToOList(oData);
					}
					map.put(OLIST, oList);
					if(data.indexOf(ID_BEGIN)>0){
						data = data.substring(0, data.indexOf(ID_BEGIN));
					}
				}else if(data.startsWith(P_BEGIN)){
					data = data.replaceAll("<[^>]*>", "");
							//substring(data.indexOf(P_BEGIN)+P_BEGIN.length(), data.indexOf(P_END));
					map.put(TYPE, TYPE_DIV);
					//将#@分类出来
					kList = putDataToAct(data);
					map.put(KLIST, kList);
					//将《》等信息分离出来
					oList = new ArrayList<Map<String, String>>();
					if(data.indexOf(XML_BEGIN+XML_END)>0){
						String oData = data.substring(data.indexOf(ID_BEGIN), data.lastIndexOf(XML_END)+XML_END.length());
						oList = putDataToOList(oData);
					}
					map.put(OLIST, oList);
					if(data.indexOf(ID_BEGIN)>0){
						data = data.substring(0, data.indexOf(ID_BEGIN));
					}
				}else if(data.startsWith(IMG_BEGIN)){//图片
					data = data.substring(data.indexOf(IMG_BEGIN)+IMG_BEGIN.length(), data.indexOf(IMG_END));
					map.put(TYPE, TYPE_IMG);
					//将图片的宽高放到map中
//				long begin = System.currentTimeMillis();
					Map WHmap = getImageWHMap(data);
//				long end = System.currentTimeMillis();
//				System.out.println("解析图片长宽:"+(end-begin));
					map.put(SIZE, WHmap);
				}else if(data.startsWith(AUDIO_BEGIN)){//音频
					data = data.substring(data.indexOf(AUDIO_BEGIN)+AUDIO_BEGIN.length(), data.indexOf(AUDIO_END));
					map.put(TYPE, TYPE_AUDIO);
					Map<String, String> urlMap = getLengthFromAudioUrl(data);
					String length = urlMap.get("length");
					data = urlMap.get("url");
					map.put(LENGTH, length);
				}else if(data.startsWith(VIDEO_BEGIN)){//视频
					data = data.substring(data.indexOf(VIDEO_BEGIN)+VIDEO_BEGIN.length(), data.indexOf(VIDEO_END));
					map.put(TYPE, TYPE_VIDEO);
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("转义字符窜"+data+"出错", e);
				data = data;
			}
			map.put(DATA, data);
			list.add(map);
		}
		return list;
	}

	/**
	 * 将章节转换为olist结构返回
	 * @param html
	 * @return
	 */
	public static List putChapterToData(String html ){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(html == null || "".equals(html)){
			return list;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TYPE,TYPE_DIV);
		map.put(DATA, html);
		list.add(map);

		return list;
	}
	
	/**
	 * 从视频地址中获取秒数
	 * @param url
	 * @return
	 */
	private static Map<String, String> getLengthFromAudioUrl(String url){
		String length = "";
		String resUrl = "";
		Map<String, String> map = new HashMap<String, String>();

		if(url == null || "".equals(url)){
			return null;
		}

		int length_index = url.indexOf("_");
		if(length_index>0){
			resUrl = url.substring(0,length_index) + url.substring(url.lastIndexOf("."));
			String tmp_url = url.substring(length_index+1);
			length = tmp_url.substring(0, tmp_url.indexOf("."));
		}else{
			resUrl = url;
		}

		map.put("url", resUrl);
		map.put("length", length);
		return map;
	}
	
	/**
	 * 通过网络地址获取图片宽高
	 * @param imagePath
	 * @return
	 */
	private static Map getImageWHMap(String imagePath){
		Map map = new HashMap();

//		BufferedImage image = httpUtils.getBufferedImage(imagePath);
		if(imagePath.indexOf("x")>0 && imagePath.indexOf("_")>0){
			imagePath = imagePath.substring(imagePath.indexOf("_")+1, imagePath.lastIndexOf("."));
			String w = imagePath.substring(0,imagePath.indexOf("x"));
			String h = imagePath.substring(imagePath.indexOf("x")+1);

			try {
				int W = Integer.valueOf(w);
				int H = Integer.valueOf(h);
				map.put("W", W);
				map.put("H", H);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		return map;
	}
	
	/**
	 * 将数据按#话题、@act区分开
	 *
	 * @param data
	 * @return
	 */
	public static List putDataToAct(String data){
		//初始化常量内容
		init();
		List<Map> kList = new ArrayList<Map>();
		if(data.indexOf(HT)<0&& data.indexOf(ACT_BEGIN)<0){
			return kList;
		}

		int i = 0;
		int j = -1;
		String ht = null;
		String act = null;
		Map<String, String> htMap =null;
		while(i >= 0){
			i = data.indexOf(HT,j+1);
			if(i>=0){
				j = data.indexOf(HT, i+1);
				if(j>=0){
					htMap = new HashMap<String, String>();
					ht = data.substring(i, j+1);
//					System.out.println(ht);
					htMap.put(TYPE, TYPE_HT);
					htMap.put(DATA, ht);
					kList.add(htMap);
				}else{
					break;
				}
			}
		}
		int acti = 0;
		int actj = -1;
		while(acti>=0){
			acti = data.indexOf(ACT_BEGIN, actj+1);
			if(acti>=0){
				actj = data.indexOf(ACT_END, acti+1);
				if(actj>=0){
					htMap = new HashMap<String, String>();
					act = data.substring(acti, actj+1);
//					System.out.println(act);
					htMap.put(TYPE, TYPE_ACT);
					htMap.put(DATA, act);
					kList.add(htMap);

					//收集被act人昵称集合
					putActNameList(act);
				}else{
					break;
				}
			}
		}

		return kList;
	}

	/**
	 * 将《》等分离出来
	 * @param data
	 * @return
	 */
	private static List putDataToOList(String html){
		List oList = new ArrayList();
		if(html == null || "".equals(html)){
			return oList;
		}

		String[] htmls = html.split(XML_BEGIN+XML_END);
		for (String data : htmls) {
			Map<String, String> map = new HashMap<String, String>();
			if(data.indexOf(ID_BEGIN)>=0){
				String oData = data.substring(data.indexOf(ID_BEGIN)+ID_BEGIN.length(), data.indexOf(ID_END));
				map.put(TYPE_ID, oData);
			}
			if(data.indexOf(INDEX_BEGIN)>0){
				String oData = data.substring(data.indexOf(INDEX_BEGIN)+INDEX_BEGIN.length(), data.indexOf(INDEX_END));
				map.put(TYPE_INDEX, oData);
			}
			if(data.indexOf(NAME_BEGIN)>0){
				String oData = data.substring(data.indexOf(NAME_BEGIN)+NAME_BEGIN.length(), data.indexOf(NAME_END));
				map.put(TYPE_NAME, oData);
			}
			if(data.indexOf(TYPE_BEGIN)>0){
				String oData = data.substring(data.indexOf(TYPE_BEGIN)+TYPE_BEGIN.length(), data.indexOf(TYPE_END));
				map.put(TYPE, oData);
			}
			oList.add(map);
		}

		return oList;
	}

	/**
	 * 被act人昵称的集合
	 * @param actName
	 */
	public static void putActNameList(String actName){
		if(actName == null && "".equals(actName)){
			return;
		}

		actName = actName.trim();
		actName = actName.replaceAll(ACT_BEGIN, "");

		if(actNameList==null){
			actNameList = new ArrayList<String>();
		}
		actNameList.add(actName);
//		System.out.println("被act人的名字："+actNameList);
	}

	/**
	 * 将图片放到list中
	 * @param photo
	 */
	private static void putPhotoToList(String photo){
		if(photo == null && "".equals(photo)){
			return;
		}
		if(photoList==null){
			photoList = new ArrayList<String>();
		}
		photoList.add(photo);
	}

	/**
	 * 正则匹配#话题#  后边加空格  方便前台分开
	 * @param str
	 * @return
	 */
	public static String matchTopic(String data){
		if(!data.contains("#")){
			return data;
		}
		int i = 0;
		int j = -1;
		String ht = null;
		while(i >= 0){
			i = data.indexOf(HT,j+1);
			if(i>=0){
				j = data.indexOf(HT, i+1);
				if(j>=0){
					ht = data.substring(i, j+1);
					data = data.replace(ht, ht+" ");
//					System.out.println(ht);
				}else{
					break;
				}
			}
		}
		return data;
	}
	
	/**
	 * 将各类资源的列表整合成一个资源列表
	 * @param reqList 传入各类资源的列表
	 * @param resList 返回同一类型资源的列表
	 * @param flagint 返回标示
	 * @param ucenterFacade 用户client接口
	 * @param actFacade 资源操作client接口
	 * @return
	 */
	public static List getResponseListByType(List reqList , List<ResourceInfo> resList, int flagint , UcenterFacade ucenterFacade,ActFacade actFacade){
		ResourceInfo ri = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (Object obj : reqList) {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade,actFacade);
				resList.add(ri);
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			Object object = reqList.get(0);
			ResourceInfo resourceInfo = fileUtils.putObjectToResource(object,ucenterFacade);
			flagint = resourceInfo.getFlag();
			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
				long id = resourceInfo.getRid();
				if(id != 0){
					ri = fileUtils.putObjectToResource(object, ucenterFacade, actFacade);
					resList.add(ri);
				}
			}
		}
		return resList;
	}
	
	/**
	 *
	 * <p>Title: checkIsTopic</p>
	 * <p>Description: 判断是否为话题</p>
	 * @author :changjiang
	 * date 2015-4-16 下午7:48:57
	 * @param date
	 */
	public static List<Map<String, String>> checkIsTopic(String date,List<Map<String, String>> oList){
		if(date.contains("#")){
			if(null==oList){
				oList = new ArrayList<Map<String,String>>();
			}
			Pattern str1Pattern = Pattern.compile("#+[^#]+#");
			Matcher str1Matcher = str1Pattern.matcher(date);
			while(str1Matcher.find()){
				Map<String, String> map = new HashMap<String, String>();
				System.out.println(str1Matcher.group());
				map.put("id", "0");//话题的id
				map.put("index", "0");//不知道是什么意思
				map.put("name", str1Matcher.group().replaceAll("#+", "#"));//话题的名字
				//插入话题信息
				map.put("type", CommentUtils.TYPE_TOPIC);//话题的类型
				oList.add(map);
			}
		}
		return  oList;
	}
	
	/**
	 *
	 * <p>Title: checkIsAt</p>
	 * <p>Description: 查询是否是at信息</p>
	 * @author :changjiang
	 * date 2015-4-24 下午3:30:11
	 * @param date
	 * @param oList
	 * @return
	 */
	public static List<Map<String, String>> checkIsAt(String date,List<Map<String, String>> oList,UcenterFacade ucenterFacade){
		if(date.contains("@")){

			/*UserInfo uInfo = ucenterFacade.findUserInfoByUserId(null, uid);
			String momentUserName = uInfo.getName();*/
			if(null==oList){
				oList = new ArrayList<Map<String,String>>();
			}
			Pattern atPattern = Pattern.compile("@[^@, ,\n]+");
			Matcher atMatcher = atPattern.matcher(date);
			String name = "";
			long userId = 0;
			while(atMatcher.find()){
				Map<String, String> map = new HashMap<String, String>();
				//System.out.println(atMatcher.group());
				name = atMatcher.group();
				map.put("name", name);//@的名字
				UserInfo userInfo = ucenterFacade.findUserInfoByName(name.replace("@", ""));
				userId = userInfo.getUserId();
				map.put("id", userId+"");//@的人的id
				map.put("index", "0");//不知道是什么意思
				//插入at信息
				map.put("type", CommentUtils.TYPE_USER);//话题的类型
				oList.add(map);
			}
		}
		return  oList;
	}
	
	public static String putAtComment(String data,List<Map<String, String>> oList){
		StringBuffer html = new StringBuffer("");
		html.append(putDataToNode(data, "0",oList));
		//sdsd
		return html.toString();
	}
	
	/**
	 * 匹配电话号码
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str) {
		if(str==null || str.trim().length()==0){
			return false;
		}
		if (checkInputByRegex(str, "^1[3-9]\\d{9}$")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过正则检测数据
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean checkInputByRegex(String str, String regex) {
        boolean flag = false;
        if(str!=null){
        	Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        }
        return flag;
    }
	
	public static void main(String[] args) {
//		String data = putDataToHTML5(null);
//		System.out.println(data);
//		int a =data.indexOf(AUDIO_BEGIN);
//		int b = data.indexOf(AUDIO_BEGIN, 266);
//		data = data.replaceAll(DIV_BEGIN, "");
//		data = data.replaceAll(DIV_END, "");
//		data = data.replaceAll(AUDIO_BEGIN, "");
//		data = data.replaceAll(AUDIO_END, "");
//		data = data.replaceAll(VIDEO_BEGIN, "");
//		data = data.replaceAll(VIDEO_END, "");
//		data = data.replaceAll(BR, "");
//		String[] datas = data.split(FGF);
//		System.out.println(a);
//		System.out.println(b);
//		System.out.println(data);
//		for (String d : datas) {
//			System.out.println(d);
//		}
//
		List dataList = new ArrayList();
		Map map = new HashMap();
		map.put("type", "0");
		map.put(DATA, "我喜欢看《心花怒放》");
		List<Map<String, String>> oList = new ArrayList<Map<String,String>>();
		Map<String, String> m = new HashMap<String, String>();
		m.put("id", "11");
		m.put("index", "0");
		m.put("name", "《心花怒放》");
		m.put("type", "6");
		oList.add(m);
		Map<String, String> m1 = new HashMap<String, String>();
		m1.put("id", "12");
		m1.put("index", "1");
		m1.put("name", "《sdf怒放》");
		m1.put("type", "7");
		oList.add(m1);
		map.put("oList", oList);
		dataList.add(map);
//		Map<String, String> map1 = new HashMap<String, String>();
//		map1.put("type", "1");
//		map1.put(DATA, "img");
//		Map<String, String> map2 = new HashMap<String, String>();
//		map2.put("type", "2");
//		map2.put(DATA, "yinpin2");
//		Map<String, String> map3 = new HashMap<String, String>();
//		map3.put("type", "3");
//		map3.put(DATA, "shipin3");
//		dataList.add(map);
//		dataList.add(map1);
//		dataList.add(map2);
//		dataList.add(map3);
//		String html = putDataToHTML5(dataList);
//		System.out.println(html);
//
//
//
//		List rlist = putHTMLToData(html);
//
//		for (Object object : rlist) {
//			Map<String, String> map4 = (Map<String, String>) object;
//			System.out.println(map4.toString());
//		}

//		String datas = "@尹楠 #额嗯呢#sadf#周杰伦#哈哈恩恩@温晓宁 @春晓 是吗#知道了#@温晓飞 #sdf# ";
//		String data = "#后会无期#上映了很火爆，#马达加斯加##张小喵#@温晓宁 也火了";
//		System.out.println(matchTopic(datas));

//		System.out.println(actNameList);
//		init();
//
//		System.out.println(actNameList);
//		actNameList = new ArrayList<String>();
//		actNameList.add("ddd");
//		System.out.println(actNameList);
//		init();
//		System.out.println(actNameList);
//		System.out.println(dataList);
//
//		System.out.println(putDataToHTML5(dataList));
//
//		System.out.println(putHTMLToData(putDataToHTML5(dataList)));

//		String data = "<!DOCTYPE HTML><html><head><meta charset=\"UTF-8\"><meta name=\"viewport\"content=\"width=device-width, initial-scale=1\"/></head><style type=\"text/css\">img{width:100% !important;}div{font-size:4 !important;}</style><body><div>新文章</div><br/><!--我是分隔符woshifengefu--><img src=\"http://112.126.68.72/image/common/57fb06c587fd4dce9a67c0eea6cd938f.jpg\"/><br/><!--我是分隔符woshifengefu--><br/><!--我是分隔符woshifengefu--><p>好的<img src=\"http://112.126.68.72/image/common/06fbd5be4c0505f267bce58d9129257c.png\" style=\"height:160px; width:580px\" /></p></body></html>";
//		List<String> list = new ArrayList<String>();
//		list.add("《后会无期|100,6》");
//		list.add("《小时代|100,6》");
//		for (String s : list) {
//
//			String str = s.substring(s.indexOf(BOOK_TYPE_BEGIN)+1, s.indexOf(BOOK_TYPE_MID));
//			if(data.indexOf(str)>0){
//				int begin = data.indexOf(str)-1;
//				int end = data.indexOf(str)+str.length()+1;
//				String ss= data.substring(begin, end);
//				data = data.replace(ss, s);
//			}
//		}
//		System.out.println(data);

		String data = "http://112.126.68.72/image/common/6c727f1f0f6164b2.fea9b32a854622ad104x7368.jpg";

		if(data.indexOf("x")>0 && data.indexOf("_")>0){
			data = data.substring(data.indexOf("_")+1, data.lastIndexOf("."));
			String w = data.substring(0,data.indexOf("x"));
			String h = data.substring(data.indexOf("x")+1);

			try {
				int W = Integer.valueOf(w);
				int H = Integer.valueOf(h);
				System.out.println(W+"-----"+H);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println(replaceWHFromImg(data));

		System.out.println(getLengthFromAudioUrl("http://a1.duyao001.com/audio/common/d6f02ef4614c6db784d3905594eb16a7_6.m4a"));
	}
	
	/**
	 *
	 * <p>Title: takeOutPinYin</p>
	 * <p>Description: 去除中文电影中的演员拼音</p>
	 * @author :changjiang
	 * date 2014-9-1 上午12:30:29
	 * @param mvoieName
	 * @param actor
	 * @return
	 */
	public static String takeOutPinYin(String data){
//		if(null == mvoieName || "".equals(mvoieName) || actor == null || "".equals(actor)){
//			return actor;
//		}
		if(null == data || "".equals(data)){
			return data;
		}
		//匹配电影名字中是否含有中文[\u4E00-\u9FA5]+
		Pattern namePattern = Pattern.compile("[\u4E00-\u9FA5]+");
		Matcher nameMatcher = namePattern.matcher(data);
		if(nameMatcher.find()){
//			System.out.println(nameMatcher.group());
			data = data.replaceAll("[A-z]+-?[A-z]+", "").replaceAll(" {2,}", "");
		}
		return data;
	}

	/**
	 * 将height、wight从img中分离出来
	 * @param data
	 * @return
	 */
	public static String replaceWHFromImg(String data){

		if(data == null || "".equals(data)){
			return data;
		}

		data = data.replaceAll("style=\"height:auto !important; width:auto !important\"", "");
		data = data.replaceAll(WebUtils.LAZY_SRC, WebUtils.LAZY_ORIGINALSRC);
		data = data.replaceAll("white-space: nowrap;", "");

//		data = replaceXSS(data);

		int i = 0;
		int j = -1;
		String ht = null;
		while(i >= 0){
			i = data.indexOf(WH_BEGIN,j+1);
			if(i>=0){
				j = data.indexOf(WH_END, i+1);
				if(j>=0){
					ht = data.substring(i, j+WH_END.length());
					data = data.replace(ht, "");
//					System.out.println(ht);
				}else{
					break;
				}
			}
		}

		return data;
	}

	/**
	 * 对xss反编译
	 * @param value
	 * @return
	 */
	private static String replaceXSS(String value) {
        //You'll need to remove the spaces from the html entities below
//		value = value.replaceAll("& lt;", "<").replaceAll("& gt;", ">");
//		value = value.replaceAll("& #40;", "(").replaceAll("& #41;", ")");
//		value = value.replaceAll("& #39;", "'");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("\"\"", "[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']");
		value = value.replaceAll("script", "");
		return value;
	}

	//处理app发表的长文章类型的内容
	public static String addHtmlToAppArticle(String resultContent){
		if(resultContent!=null && resultContent.trim().length()>0){
			//长影评的类型的话对文字做处理
			String imgRegex = "http://p[0-2]{1}\\.duyao001\\.com/[^\\.]+\\.(jpg|bmp|eps|gif|mif|miff|png|tif|tiff|svg|wmf|jpe|jpeg|dib|ico|tga|cut|pic)";

			resultContent = HtmlUtil.delHTMLTag(resultContent);
			Pattern pattern = Pattern.compile(imgRegex);
			Matcher matcher = pattern.matcher(resultContent);
			String tempStr = "";
			while (matcher.find()){
				tempStr = matcher.group();
				resultContent = resultContent.replace(tempStr, "</p><p><a class=\"imagescroll\"><img originalSrc=\"" + tempStr + "\" _originalSrc=\""+tempStr+"\"/></a></p><p>");
			}

			resultContent = resultContent.replaceAll("\r\n","</p><p>").replaceAll("\n", "</p><p>");
			resultContent = "<p>"+resultContent+"</p>";
		}
		return resultContent;
	}
	
	public static List<String> getActNameList() {
		return actNameList;
	}
	
	public static List<String> getPhotoList() {
		return photoList;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	
}
