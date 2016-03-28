package com.keel.utils.dbbook;

public class CommentUtils {
	/**
	 * 服务端地址
	 * WEB_SERVER = "http://192.168.0.149:8080/"
	 */
	public static final String WEB_SERVER = "http://192.168.0.149:8080";
	
	/**
	 * 上传图片公共地址
	 */
	public static final String WEB_UPLOAD_IMG = "http://112.126.68.72:8001/upload_image.do";
	
	/**
	 * 公共logo地址
	 */
	public static final String WEB_PUBLIC_LOGO = "http://112.126.68.72/image/common/b140d44fdfa1eb410c0f5f7b41f8c640.png";
	
	/**
	 * 公共头像
	 */
	public static final String WEB_PUBLIC_PLOGO = "http://112.126.68.72/image/common/3afb40f4a87246de27b73ebc91ba19d9.png";
	
	/**
	 * 公共书的封面
	 */
	public static final String WEB_PUBLIC_BOOKPIC ="http://112.126.68.72/image/common/3014c4ccd5bfa39f2a23dc3c2aaae1de.png";
	
	/**
	 * 下载地址
	 */
	public static final String WEB_DOWN = "http://112.126.68.72/image/common/21b0f38b0fb7c16d215dcbb83655d655.png";
	
	public static String getWebServer(){
		return WEB_SERVER;
	}

	//客户端传值常量
	/**
	 * 客户端会传类似isList、isDB的数据，该值表示真
	 * REQ_ISON_TRUE = "0"
	 */
	public static final String REQ_ISON_TRUE = "0";
	/**
	 * 客户端会传类似isList、isDB的数据，该值表示假
	 * REQ_ISON_FALSE = "1"
	 */
	public static final String REQ_ISON_FALSE = "1";
	/**
	 * 客户端返回成功值
	 * RES_FLAG_SUCCESS = "0"
	 */
	public static final String RES_FLAG_SUCCESS = "0";
	/**
	 * 客户端返回失败值
	 * RES_FLAG_ERROR = "1"
	 */
	public static final String RES_FLAG_ERROR = "1";
	
	/**
	 * 获取数据失败
	 * ERROR_CODE_GETDATAERROR = "1001"
	 */
	public static final String ERROR_CODE_GETDATAERROR = "1001";
	
	/**
	 * 已关注
	 * RES_ATTENTION = "0"
	 */
	public static final String RES_ATTENTION = "0";
	/**
	 * 未注册
	 * RES_UNREGIST = "1"
	 */
	public static final String RES_UNREGIST = "1";
	/**
	 * 已注册
	 * RES_REGIST = "2"
	 */
	public static final String RES_REGIST = "2";
	/**
	 * 不明ID
	 * UN_ID = 0
	 */
	public static final long UN_ID = 0;
	/**
	 * 默认资源id
	 * RESOURCE_ID = 0
	 */
	public static final long RESOURCE_ID = 0;
	/**
	 * 尹楠ID
	 * UID_YINNAN = 15
	 */
	public static final long UID_YINNAN = 15;
	
	/**
	 * 韩东id
	 * UID_HANDONG = 35
	 */
	public static final long UID_HANDONG = 35;
	
	/**
	 * 分页条数
	 * PAGE_SIZE = 300
	 */
	public static final int PAGE_SIZE = 3000;
	
	/**
	 * 推送文本ACT_MESSAGE = ",在毒药中提到了你";
	 */
	public static final String ACT_MESSAGE = ",在毒药中提到了你";
	
	
	//资源标示
	/**
	 * TYPE_ALL = "100";//全部资源
	 */
	public static final String TYPE_ALL = "100";//全部资源
	/**
	 * TYPE_BOOK = "0";//书
	 */
	public static final String TYPE_BOOK = "0";//书单
	/**
	 * TYPE_MOIVE = "1";//电影
	 */
	public static final String TYPE_MOVIELIST = "1";//影单
	/**
	 * TYPE_PUSH = "2";//连载
	 */
	public static final String TYPE_PUSH = "2";//连载
	/**
	 * TYPE_LOG = "3";//日志
	 */
	public static final String TYPE_LOG = "3";//日志
	/**
	 * TYPE_RELPY = "4";//转发
	 */
	public static final String TYPE_RELPY = "4";//转发
	/**
	 * TYPE_RELPY = "5";//帖子
	 */
	public static final String TYPE_POST = "5";//帖子
	/**
	 * TYPE_BOOK_COMMENT = "6";//书评
	 */
	public static final String TYPE_BOOK_COMMENT = "6";//书评
	/**
	 * TYPE_MOVIE_COMMENT = "7";//影评
	 */
	public static final String TYPE_MOVIE_COMMENT = "7";//影评
	/**
	 * TYPE_CHAPTER = "8";//连载章节
	 */
	public static final String TYPE_CHAPTER = "8";//连载章节
	/**
	 * TYPE_MOVIE_TALK = "9";//电影讨论
	 */
	public static final String TYPE_MOVIE_TALK = "9";//电影讨论
	/**
	 * TYPE_BOOK_TALK = "10";//书讨论
	 */
	public static final String TYPE_BOOK_TALK = "10";//书讨论
	/**
	 * TYPE_MOVIE_UPCOMING = "11";//即将上映
	 */
	public static final String TYPE_MOVIE_UPCOMING = "11";//即将上映
	/**
	 * TYPE_ACT = "12";//act
	 */
	public static final String TYPE_ACT = "12";//act
	/**
	 * TYPE_RESOURCEINFO_BOOK = "13";//书评的评论
	 */
	public static final String TYPE_RESOURCEINFO_BOOK = "13";//书评的评论
	/**
	 * TYPE_RESOURCEINFO_MOVIE = "14";//影评的评论
	 */
	public static final String TYPE_RESOURCEINFO_MOVIE = "14";//影评的评论
	/**
	 * TYPE_TALENTZONE_BOOK = "15";//达人圈书相关
	 */
	public static final String TYPE_TALENTZONE_BOOK = "15";//达人圈书相关
	/**
	 * TYPE_TALENTZONE_MOVIE = "16";//达人圈影相关
	 */
	public static final String TYPE_TALENTZONE_MOVIE = "16";//达人圈影相关
	/**
	 * TYPE_COLLECT = "17";//收藏
	 */
	public static final String TYPE_COLLECT = "17";//收藏
	/**
	 * TYPE_TALENTZONE18 = "18";//推荐领袖
	 */
	public static final String TYPE_TALENTZONE18 = "18";//推荐领袖
	/**
	 * TYPE_TALENTZONE19 = "19";//领袖推荐
	 */
	public static final String TYPE_TALENTZONE19 = "19";//领袖推荐
	/**
	 * TYPE_SHARE = "20";//晒一晒
	 */
	public static final String TYPE_SHARE = "20";//晒一晒
	
	
	
	//各种单的类型
	/**
	 * type_list0 = 0;//默认
	 */
	public static final int type_list0 = 0;//默认
	/**
	 * type_list1 = 1;//自助创建
	 */
	public static final int type_list1 = 1;//自助创建
	/**
	 * type_list2 = 2;//；公共
	 */
	public static final int type_list2 = 2;//；公共
	
	
	//用户表示常量
	/**
	 * USER_LEVEL_NORMAL = 0;//正常用户
	 */
	public static final int USER_LEVEL_NORMAL = 0;//正常用户
	/**
	 * USER_LEVEL_TALENT = 50;//达人
	 */
	public static final int USER_LEVEL_TALENT = 50;//达人
	/**
	 * USER_LEVEL_LEADER = 60;//大达人
	 */
	public static final int USER_LEVEL_LEADER = 60;//大达人
	/**
	 * USER_LEVEL_XIAOBIAN = 99;//小编
	 */
	public static final int USER_LEVEL_XIAOBIAN = 99;//小编
	
	public static final String COMMENT_PARISE = "0";//赞
	public static final String COMMENT_NOTPARISE = "1";//取消赞
	public static final String COMMENT_PL = "2";//评论
	public static final String COMMENT_ACT = "3";//转发
	
	
	// 错误信息
	/**
	 * 错误信息json数据
	 * RES_ERROR_BEGIN = "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\"";
	 */
	public static final String RES_ERROR_BEGIN = "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\"";
	public static final String RES_ERROR_END = "\"}}}";
	/**
	 * 数据获取失败
	 * ERROR_DATANOTGET = "数据获取失败"
	 */
	public static final String ERROR_DATANOTGET = "数据获取失败";
	/**
	 * 数据获取失败
	 * ERROR_DATANOTGET = "数据获取失败"
	 */
	public static final String ERROR_USERNOTLOGIN = "您还没有登陆，请登录后再做此操作";
	/**
	 * 数据获取失败
	 * ERROR_DATARESULTERROR = "数据返回失败"
	 */
	public static final String ERROR_DATARESULTERROR = "数据返回失败";
	
	/**
	 * ERROR_NOTUNKOWN = "错误未知"
	 */
	public static final String ERROR_NOTUNKOWN = "错误未知";
	
	/**
	 * 默认web端分页，每页显示数据量
	 * DEFAULT_PAGESIZE = "5"
	 */
	public static final String DEFAULT_PAGESIZE = "5";
}
