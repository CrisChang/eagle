package com.poison.eagle.utils;

public class CommentUtils {
	/**
	 * 服务端地址
	 * WEB_SERVER = "http://192.168.0.149:8080/"
	 */
	public static final String WEB_SERVER = "http://wap.duyao001.com:8080";
	/**
	 * 服务端地址
	 * WEB_SERVER = "http://192.168.0.149:8080/"
	 */
//	public static final String WEB_SERVER_ONLINE = "http://112.126.68.72:8001";
	
	
	/**
	 * 图片服务地址 WEB_SERVER_FOR_IMAGE = "http://p.duyao001.com";
	 */
	public static final String WEB_SERVER_FOR_DUYAO = "http://www.duyao001.com";
	/**
	 * 图片服务地址 WEB_SERVER_FOR_IMAGE = "http://p.duyao001.com";
	 */
	public static final String WEB_SERVER_FOR_IMAGE = "http://p.duyao001.com";
	/**
	 * 视频服务地址 WEB_SERVER_FOR_VIDEO = "http://p.duyao001.com";
	 */
	public static final String WEB_SERVER_FOR_VIDEO = "http://v.duyao001.com";
	/**
	 * 音频服务地址 WEB_SERVER_FOR_AUDIO = "http://p.duyao001.com";
	 */
	public static final String WEB_SERVER_FOR_AUDIO = "http://a.duyao001.com";
	/**
	 * 文件服务地址 WEB_SERVER_FOR_FILE = "http://p.duyao001.com"; 
	 */
	public static final String WEB_SERVER_FOR_FILE = "http://f.duyao001.com";
	
	/**
	 * 图片存放地址WEB_UPLOAD_DIR = "/data/img_temp"
	 */
//	public static final String WEB_UPLOAD_DIR = "/data/img_temp";
	/**
	 * WEB_UPLOAD_DIR_LOCALHOST = "d:/data/img_temp";
	 */
//	public static final String WEB_UPLOAD_DIR_LOCALHOST = "d:/data/img_temp";
	/**
	 * 上传图片公共地址"http://112.126.68.72:8001/upload_image.do";
	 */
//	public static final String WEB_UPLOAD_IMG = "http://112.126.68.72:8001/upload_image.do";
	
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
	public static final String WEB_DOWN = "http://112.126.68.72/static/sys/tupian/4152ebb7-9ffc-41c5-b22a-fb80c18e8f06.png";
	/**
	 * WEB_JQUERY_1_6_JS = "http://s.duyao001.com/static/article/js/jquery-1.6.js";
	 */
	public static final String WEB_JQUERY_1_6_JS = "http://s.duyao001.com/static/article/js/jquery-1.6.js";
	/**
	 * WEB_LOADING_JPG = "http://s.duyao001.com/static/article/image/loading.jpg";
	 */
	public static final String WEB_LOADING_JPG = "http://s.duyao001.com/static/article/image/loading.jpg";
	/**
	 * WEB_LYZ_DELAYLOADING_MIN_JS = "http://s.duyao001.com/static/article/js/lyz.delayLoading.min.js";
	 */
	public static final String WEB_LYZ_DELAYLOADING_MIN_JS = "http://s.duyao001.com/static/article/js/lyz.delayLoading.min.js";
	/**
	 * WEB_FZLTH_YS_GB18030_TTF = "http://s.duyao001.com/static/article/zt/FZLTH_YS_GB18030.ttf";
	 */
	public static final String WEB_FZLTH_YS_GB18030_TTF = "http://s.duyao001.com/static/article/zt/FZLTH_YS_GB18030.ttf";
	/**
	 * 下载地址
	 */
	public static final String WEB_FZXH_GB18030_TTF = "http://s.duyao001.com/static/article/zt/FZXH-GB18030.ttf";
	/**
	 * 下载地址  WEB_SEARCH_BOOK = WEB_SERVER_FOR_DUYAO+"search/book/5/1?q=";
	 */
	public static final String WEB_SEARCH_BOOK = WEB_SERVER_FOR_DUYAO+"/search/book/5/1?q=";
	/**
	 * 下载地址	 WEB_SEARCH_MOVIE = WEB_SERVER_FOR_DUYAO+"/search/film/5/1?q=";
	 */
	public static final String WEB_SEARCH_MOVIE = WEB_SERVER_FOR_DUYAO+"/search/film/5/1?q=";
	/**
	 * 下载地址WEB_SEARCH_NETBOOK = WEB_SERVER_FOR_DUYAO+"/search/netBook/5/1?q=";
	 */
	public static final String WEB_SEARCH_NETBOOK = WEB_SERVER_FOR_DUYAO+"/search/netBook/5/1?q=";
	/**
	 * 下载地址  WEB_SEARCH_STAGEPLAY = WEB_SERVER_FOR_DUYAO+"/search/stagePlay/5/1?q=";
	 */
	public static final String WEB_SEARCH_STAGEPLAY = WEB_SERVER_FOR_DUYAO+"/search/stagePlay/5/1?q=";
	/**
	 * 下载地址  WEB_SEARCH_MUSICPLAY = WEB_SERVER_FOR_DUYAO+"/search/musicPlay/5/1?q=";
	 */
	public static final String WEB_SEARCH_MUSICPLAY = WEB_SERVER_FOR_DUYAO+"/search/musicPlay/5/1?q=";
	
	/**
	 * 公共影单id（热搜）
	 */
	public static final Long WEB_PUBLIC_HOT_MOVIELIST_ID = 39931485725261824l;
	/**
	 * 公共书单id（热搜）
	 */
	public static final Long WEB_PUBLIC_HOT_BOOKLIST_ID = 39956335047081984l;
	
	
	
	public static String getWebServer(){
		return WEB_SERVER;
	}
	
	//判断网址从哪链接过来
	public static final String REQUEST_FROM_WEB = "/w";
	public static final String REQUEST_FROM_MOBLIE = "/m";// "/m";
	
	/**
	 * RESOURCE_PAGE_SIZE = 10;分页显示数目
	 */
	public static final int RESOURCE_PAGE_SIZE = 10;
	
	/**
	 * RESOURCE_CONTENT_SIZE = 50;资源类字数限制
	 */
	public static final int RESOURCE_CONTENT_SIZE_DISCOVERY = 50;
	/**
	 * RESOURCE_CONTENT_SIZE_INDEX = 100;资源类字数限制
	 */
	public static final int RESOURCE_CONTENT_SIZE_INDEX = 180;
	/**
	 * RESOURCE_CONTENT_SIZE_DIARY = 140;资源类字数限制
	 */
	public static final int RESOURCE_CONTENT_SIZE_DIARY = 140;

	//客户端传值常量
	/**
	 * 客户端会传类似isList、isDB的数据，该值表示真
	 * REQ_ISON_TRUE = "0"
	 */
	public static final String REQ_ISON_TRUE = "0";

	/**
	 * 小说的登录接口
	 */
	public static final String REQ_ISON_STORY = "2";
	
	/**
	 * 用户的游客登录
	 */
	public static final String REQ_ISON_GUESS = "100";
	
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
	 * 新的关注人条数
	 */
	public static final int PAGE_NEW_SIZE = 100000;
	
	/**
	 * 推送文本ACT_MESSAGE = ",在毒药中提到了你";
	 */
	public static final String ACT_MESSAGE = ",在毒药中提到了你";
	
	/**
	 * CONTENT_IS_DEL = "抱歉，此信息已被作者删除。";
	 */
	public static final String CONTENT_IS_DEL = "抱歉，此信息已被作者删除。";
	
	
	public static final int TRUE = 0;
	public static final int FALSE = 1;
	
	//{资源标示}
	/**
	 * TYPE_ALL = "100";//全部资源
	 */
	public static final String TYPE_ALL = "100";//全部资源
	/**
	 * TYPE_BOOK = "0";//书单
	 */
	public static final String TYPE_BOOKLIST = "0";//书单
	/**
	 * TYPE_MOIVE = "1";//电影
	 */
	public static final String TYPE_MOVIELIST = "1";//影单
	/**
	 * TYPE_PUSH = "2";//连载
	 */
	public static final String TYPE_PUSH = "2";//连载
	/**
	 * TYPE_DIARY = "3";//日志
	 */
	public static final String TYPE_DIARY = "3";//日志
	/**
	 * TYPE_PUBLISH = "4";//推送
	 */
	public static final String TYPE_PUBLISH = "4";//推送
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
	/**
	 * TYPE_ALBUM = "21";//相册
	 */
	public static final String TYPE_ALBUM = "21";//相册
	/**
	 * TYPE_ARTICLE = "22";//长微博
	 */
	public static final String TYPE_ARTICLE = "22";//长文章
	/**
	 * TYPE_ARTICLE_BOOK = "23";//长书评
	 */
	public static final String TYPE_ARTICLE_BOOK = "23";//长书评
	/**
	 * TYPE_ARTICLE_MOVIE = "24";//长影评
	 */
	public static final String TYPE_ARTICLE_MOVIE = "24";//长影评
	/**
	 * TYPE_COMMENT = "25";//评论
	 */
	public static final String TYPE_COMMENT = "25";//评论
	/**
	 * TYPE_DISCOVERY = "26";//发现页
	 */
	public static final String TYPE_DISCOVERY = "26";//发现页
	/**
	 * TYPE_BOOK = "27";//书
	 */
	public static final String TYPE_BOOK = "27";//书
	/**
	 * TYPE_MOVIE = "28";//电影
	 */
	public static final String TYPE_MOVIE = "28";//电影
	/**
	 * TYPE_NETBOOK = "29";//网络小说
	 */
	public static final String TYPE_NETBOOK = "29";//网络小说
	/**
	 * TYPE_GRAPHIC_FILM = "30";//图解电影
	 */
	public static final String TYPE_GRAPHIC_FILM = "30";//图解电影
	/**
	 * TYPE_FEEDBACK = "31";//反馈
	 */
	public static final String TYPE_FEEDBACK = "31";//反馈
	/**
	 * TYPE_USER = "32";//用户
	 */
	public static final String TYPE_USER = "32";//用户
	
	/**
	 * TYPE_TOPIC = "33";//话题
	 */
	public static final String TYPE_TOPIC = "33";//话题
	/**
	 * TYPE_NEWARTICLE = "34";//新的长文章
	 */
	public static final String TYPE_NEWARTICLE = "34";//新的长文章
	
	/**
	 * TYPE_NEWARTICLE = "35";//新电影详情-角色评论
	 */
	public static final String TYPE_ACTOR_COMMENT = "35";//新电影详情-角色评论
	/**
	 * TYPE_ACTIVITY_COMMENT = "36";//百幕大战评论
	 */
	public static final String TYPE_ACTIVITY_TOPIC_COMMENT = "36";//百幕大战评论
	/**
	 * TYPE_BOOK_DIGEST = "39";//书摘(关于一本书的文字内容)
	 */
	public static final String TYPE_BOOK_DIGEST = "39";//书摘
	
	/**小说评论*/
	public static final String TYPE_STORY_COMMENT = "43";
	
	/**
	 * TYPE_MESSAGE = "53";//电影资讯
	 */
	public static final String TYPE_MV_MESSAGE = "53";//电影咨讯
	/**
	 * TYPE_MOVIE_COMMENT_MATCH = "77";//影评大赛影评
	 */
	public static final String TYPE_MOVIE_COMMENT_MATCH = "77";//影评大赛影评
	/**
	 * TYPE_RANKINGLIST_ORIGINAL = "191";//原创榜
	 */
	public static final String TYPE_RANKINGLIST_ORIGINAL = "191";//原创榜
	/**
	 * TYPE_RANKINGLIST_LASTWEEK = "192";//上周回复榜
	 */
	public static final String TYPE_RANKINGLIST_LASTWEEK = "192";//上周回复榜
	/**
	 * TYPE_OUT_LINK = "81";//外链(banner中使用)
	 */
	public static final String TYPE_OUT_LINK = "81";//外链(banner中使用)


	//运营类型
	/**
	 * CASETYPE_TOP_MOVIE = "10";//发现页顶部经典
	 */
	public static final String CASETYPE_TOP_MOVIE = "10";//精选页，顶部电影
	/**
	 * CASETYPE_TOP_BOOK = "20";//精选页，顶部书
	 */
	public static final String CASETYPE_TOP_BOOK = "20";//精选页，顶部书
	/**
	 * CASETYPE_hot_movie = "30";//精选页，第二排热映电影
	 */
	public static final String CASETYPE_HOT_MOVIE = "30";//精选页，第二排热映电影
	/**
	 * CASETYPE_hot_movie_more = "31";//精选页，第二排热映电影更多
	 */
	public static final String CASETYPE_HOT_MOVIE_MORE = "31";//精选页，第二排热映电影更多
	/**
	 * CASETYPE_HOT_BOOK = "40";//精选页，第二排热搜书
	 */
	public static final String CASETYPE_HOT_BOOK = "40";//精选页，第二排热搜书
	/**
	 * CASETYPE_HOT_BOOK_MORE = "41";//精选页，第二排热搜书更多
	 */
	public static final String CASETYPE_HOT_BOOK_MORE = "41";//精选页，第二排热搜书更多
	/**
	 * CASETYPE_MOVIELIST = "50";//精选页，第三排影单
	 */
	public static final String CASETYPE_MOVIELIST = "50";//精选页，第三排影单
	/**
	 * CASETYPE_BOOKLIST = "60";//精选页，第三排书单
	 */
	public static final String CASETYPE_BOOKLIST = "60";//精选页，第三排书单
	/**
	 * CASETYPE_PRODUCT_RANK_MOVIE = "70";//精选页，电影精品榜单
	 */
	public static final String CASETYPE_PRODUCT_RANK_MOVIE = "70";//精选页，电影精品榜单
	/**
	 * CASETYPE_PRODUCT_RANK_MOVIE_MORE = "71";//精选页，电影精品榜单更多
	 */
	public static final String CASETYPE_PRODUCT_RANK_MOVIE_MORE = "71";//精选页，电影精品榜单更多
	/**
	 * CASETYPE_PRODUCT_RANK_BOOK = "80";//精选页，书精品榜单
	 */
	public static final String CASETYPE_PRODUCT_RANK_BOOK = "80";//精选页，书精品榜单
	/**
	 * CASETYPE_PRODUCT_RANK_BOOK_MORE = "81";//精选页，书精品榜单更多
	 */
	public static final String CASETYPE_PRODUCT_RANK_BOOK_MORE = "81";//精选页，书精品榜单更多
	/**
	 * CASETYPE_UPCOMING_MOVIE = "90";//精选页，即将上映电影
	 */
	public static final String CASETYPE_UPCOMING_MOVIE = "90";//精选页，即将上映电影
	/**
	 * CASETYPE_MOVIE_ARTICLE = "110";//电影关联长文章
	 */
	public static final String CASETYPE_MOVIE_ARTICLE = "110";//电影关联长文章
	/**
	 * CASETYPE_MOVIE_GRAPHICFILM = "111";////电影关联图解电影
	 */
	public static final String CASETYPE_MOVIE_GRAPHICFILM = "111";//电影关联图解电影
	/**
	 * CASETYPE_MOVIE_SCHEME = "111";//电影图解
	 */
	public static final String CASETYPE_MOVIE_SCHEME = "111";//电影图解
	/**
	 * TYPE_MOVIE_SCOPY = "112";//电影花絮
	 */
	public static final String TYPE_MOVIE_SCOOPY = "112";//电影花絮
	/**
	 * CASETYPE_BOOK_ARTICLE = "120";//书关联长文章
	 */
	public static final String CASETYPE_BOOK_ARTICLE = "120";//书关联长文章
	/**
	 * CASETYPE_INDEX = "130";//首页三篇精选
	 */
	public static final String CASETYPE_INDEX = "130";//首页三篇精选
	/**
	 * CASETYPE_BUSINESS_NEW_MOVIELIST = "140";//筛选类，最新影单
	 */
	public static final String CASETYPE_BUSINESS_NEW_MOVIELIST = "140";//筛选类，最新影单
	/**
	 * CASETYPE_BUSINESS_HOT_MOVIELIST = "141";//筛选类，最热影单
	 */
	public static final String CASETYPE_BUSINESS_HOT_MOVIELIST = "141";//筛选类，最热影单
	/**
	 * CASETYPE_BUSINESS_COLLECT_MOVIELIST = "142";//筛选类，收藏最多
	 */
	public static final String CASETYPE_BUSINESS_COLLECT_MOVIELIST = "142";//筛选类，收藏最多
	/**
	 * CASETYPE_BUSINESS_NEW_BOOKLIST = "150";//筛选类，最新书单
	 */
	public static final String CASETYPE_BUSINESS_NEW_BOOKLIST = "150";//筛选类，最新书单
	/**
	 * CASETYPE_BUSINESS_HOT_BOOKLIST = "151";//筛选类，最热书单
	 */
	public static final String CASETYPE_BUSINESS_HOT_BOOKLIST = "151";//筛选类，最热书单
	/**
	 * CASETYPE_BUSINESS_COLLECT_BOOKLIST = "152";//筛选类，收藏书单
	 */
	public static final String CASETYPE_BUSINESS_COLLECT_BOOKLIST = "152";//筛选类，收藏书单
	/**
	 * CASETYPE_BUSINESS_NEW_BOOK = "160";//筛选类，最新书
	 */
	public static final String CASETYPE_BUSINESS_NEW_BOOK = "160";//筛选类，最新书
	/**
	 * CASETYPE_BUSINESS_HOT_BOOK = "161";//筛选类，最热书
	 */
	public static final String CASETYPE_BUSINESS_HOT_BOOK = "161";//筛选类，最热书
	/**
	 * CASETYPE_BUSINESS_COLLECT_BOOKLIST = "152";//筛选类，收藏书单
	 */
	public static final String CASETYPE_BUSINESS_COLLECT_BOOK = "162";//筛选类，收藏书
	/**
	 * CASETYPE_BUSINESS_NEW_MOVIE = "170";//筛选类,毒药认证
	 */
	public static final String CASETYPE_BUSINESS_NEW_MOVIE = "170";//筛选类,即将上映
	/**
	 * CASETYPE_BUSINESS_HOT_MOVIE = "171";//筛选类，热映电影
	 */
	public static final String CASETYPE_BUSINESS_HOT_MOVIE = "171";//筛选类，热映电影
	/**
	 * CASETYPE_BUSINESS_COLLECT_MOVIE = "172";//筛选类，即将上映
	 */
	public static final String CASETYPE_BUSINESS_COLLECT_MOVIE = "172";//筛选类，毒药认证
	/**
	 * CASETYPE_BUSINESS_INDEX = "190";//首页一条运营内容
	 */
	public static final String CASETYPE_BUSINESS_INDEX = "190";//首页一条运营内容
	
	/**
	 * CASETYPE_HOT_MVCOMMENT = "600" 热门影评
	 */
	public static final String CASETYPE_HOT_MVCOMMENT = "600";//热门影评
	/**
	 * CASETYPE_REC_MVLIST = "601";//推荐影单
	 */
	public static final String CASETYPE_REC_MVLIST = "601";//推荐影单
	/**
	 * CASETYPE_WRITING_CLASS = "602";//编剧课堂(长文章类型)
	 */
	public static final String CASETYPE_WRITING_CLASS = "602";//编剧课堂(长文章类型)
	/**
	 * CASETYPE_MV_BOX_OFFICE = "603";//电影票房榜
	 */
	public static final String CASETYPE_MV_BOX_OFFICE = "603";//电影票房榜
	/**
	 * CASETYPE_MAY_WATCHED_MV = "604";//毒药用户可能看过的电影
	 */
	public static final String CASETYPE_MAY_WATCHED_MV = "604";//毒药用户可能看过的电影
	/**
	 * CASETYPE_MAY_WATCHED_BK = "605";//毒药用户可能看过的书
	 */
	public static final String CASETYPE_MAY_WATCHED_BK = "605";//毒药用户可能看过的书
	/**
	 * CASETYPE_LATEST_MVLIST = "606";//影单列表(首页中的影单标签)
	 */
	public static final String CASETYPE_INDEX_MVLIST = "606";//影单列表(首页中的影单标签)--------------废了，不用了
	/**
	 * CASETYPE_MV_LINK_TOPIC = "607";//电影关联的话题
	 */
	public static final String CASETYPE_MV_LINK_TOPIC = "607";//电影关联的话题
	/**
	 * CASETYPE_GOOD_MVCOMMENT = "608";//电影详情页的——精彩影评
	 */
	public static final String CASETYPE_GOOD_MVCOMMENT = "608";//电影详情页的——精彩影评
	/**
	 * CASETYPE_SELECTED_MVCOMMENT = "609" 精选书评
	 */
	public static final String CASETYPE_SELECTED_BKCOMMENT = "609";//精选书评
	/**
	 * CASETYPE_SELECTED_DIGEST = "610" 精选书摘
	 */
	public static final String CASETYPE_SELECTED_DIGEST = "610";//精选书摘
	/**
	 * CASETYPE_BK_BANNER = "611" 图书下的banner
	 */
	public static final String CASETYPE_BK_BANNER = "611";//图书下的banner
	/**
	 * CASETYPE_REC_BKLIST = "612";//书单推荐
	 */
	public static final String CASETYPE_REC_BKLIST = "612";//书单推荐
	/**
	 * CASETYPE_MV_SELECTED_ARTICLE = "613";//影视动态(实际为长文章)
	 */
	public static final String CASETYPE_MV_SELECTED_ARTICLE = "613";//影视动态(实际为长文章)
	
	
	//标签常量
	/**
	 * TAG_BOOK = "1";//书相关标签
	 */
	public static final String TAG_BOOK = "1";//书相关标签
	/**
	 * TAG_MOVIE = "2";//影相关标签
	 */
	public static final String TAG_MOVIE = "2";//影相关标签
	/**
	 * TAG_USER = "3";//用户
	 */
	public static final String TAG_USER = "3";//用户
	
	
	
	
	
	
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
	
	/**
	 * COMMENT_PARISE = "0";//赞
	 */
	public static final String COMMENT_PARISE = "0";//赞
	/**
	 * COMMENT_NOTPARISE = "1";//取消赞
	 */
	public static final String COMMENT_NOTPARISE = "1";//取消赞
	
	public static final String COMMENT_USEFUL = "1";//有用
	
	public static final String COMMENT_USELESS = "2";//没用
	
	public static final String COMMENT_NOCHOOSEUSE = "0";//没有点击是否有用
	
	public static final String COMMENT_PL = "2";//评论
	public static final String COMMENT_ACT = "3";//转发
	
	
	/**
	 * 书单影单详情显示数量
	 */
	public static final int BOOK_MOVIE_LIST_SIZE = 10;
	
	
	// 错误信息
	/**
	 * 错误信息json数据
	 * RES_ERROR_BEGIN = "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\"";
	 */
	public static final String RES_ERROR_BEGIN = "{\"res\":{\"data\":{\"flag\":\"1\",\"error\":\"";
	public static final String RES_ERROR_END = "\"}}}";
	/**
	 * 数据获取失败
	 * ERROR_DATANOTGET = "接口数据获取失败";
	 */
	public static final String ERROR_DATANOTGET = "接口数据获取失败";
	/**
	 * 
	 * ERROR_USERNOTLOGIN = "您还没有登陆，请登录后再做此操作";
	 */
	public static final String ERROR_USERNOTLOGIN = "您还没有登陆，请登录后再做此操作";

	/**
	 * 游客登录操作提示
	 */
	public static final String ERROR_TOURISTLOGIN = "您当前身份为游客，请登录后再做此操作";
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
	
	/**
	 * 活动书单，用户收藏的书单
	 */
	public static final String ACTIVITY_COLLECTED_BOOKLIST = "我的书架藏书";
}
