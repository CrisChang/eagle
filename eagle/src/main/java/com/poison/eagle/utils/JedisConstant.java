package com.poison.eagle.utils;

public class JedisConstant {

	/**
	 * 朋友圈的个人id
	 */
	public static final String USER_MOMENT_ID = "MOMENTS_USER_ID_";
	
	/**
	 * 神人广场的id
	 */
	public static final String SHENREN_MOMENT_ID = "SHENREN_MOMENT_USER_ID_0";
	
	
	/**
	 * 普通人的广场id
	 * 
	 */
	public static final String COMMON_MOMENT_ID = "COMMON_MOMENT_USER_ID_0";
	
	/**
	 * 精选列表id
	 * 
	 */
	public static final String SELECTED_MOMENT_ID = "SELECTED_MOMENT_ID_0";
	
	/**
	 * 精选列表的个人id
	 */
	public static final String SELECTED_MOMENT_DYNAMEIC_ID = "SELECTED_USER_ID_";

	/**
	 * 运营精选的个人id
	 */
	public static final String OPERATION_SELECTED_DYNAMEIC_ID = "OPERATION_USER_ID_";
	
	/**
	 * 个人动态的用户id
	 */
	public static final String USER_DYNAMEIC_ID = "USER_DYNAMEIC_ID_";
	
	/**
	 * 个人收藏的用户id
	 */
	public static final String USER_COLLECTED_ID = "USER_COLLECTED_ID_";
	
	/**
	 * 资源hash的id
	 */
	public static final String RESOURCE_HASH_ID = "RESOURCE_HASH_ID_";
	
	/**
	 * 资源的hash信息
	 */
	public static final String RESOURCE_HASH_INFO = "RESOURCE_HASH_INFO";
	
	/**
	 * 用户对资源的关系格式为userid#res#resourceid
	 */
	public static final String RELATION_TO_USER_AND_RES = "#res#";
	
	/**
	 * 是否已经赞
	 */
	public static final String RELATION_IS_PRAISE = "RELATION_IS_PRAISE";
	
	/**
	 * 是否有用
	 */
	public static final String RLEATION_IS_USEFUL = "RELATION_IS_USEFUL";
	
	/**
	 * 是否看过
	 */
	public static final String RELATION_IS_READ = "RELATION_IS_READ";
	
	/**
	 * 资源和附加信息 关系格式为RES_OTHER_INFO_rid
	 */
	public static final String RELATION_TO_RES_OTHERINFO = "RES_OTHER_INFO_";
	
	/**
	 * 是否收藏
	 */
	public static final String RELATION_IS_COLLECT = "RELATION_IS_COLLECT";
	
	/**
	 * 点赞的数量
	 */
	public static final String RELATION_PRAISE_NUM = "RELATION_PRAISE_NUM";
	
	/**
	 * 评论的数量
	 */
	public static final String RELATION_COMMENT_NUM = "RELATION_COMMENT_NUM";
	
	/**
	 * 有用的数量
	 */
	public static final String RELATION_USEFUL_NUM = "RELATION_USEFUL_NUM";
	
	/**
	 * 没用的数量
	 */
	public static final String RELATION_USELESS_NUM = "RELATION_USELESS_NUM";
	
	/**
	 * 用户对书的关系 是否加入书单 格式为user_id#book#id
	 */
	public static final String RELATION_TO_USER_AND_BOOK = "#book#";
	
	/**
	 * 是否加入书单或影单
	 */
	public static final String RELATION_IS_INLIST = "RELATION_IS_INLIST";
	
	/**
	 * 用户对电影的关系 是否加入影单 格式为user_id#movie#id
	 */
	public static final String RELATION_TO_USER_AND_MOVIE = "#movie#";
	
	/**
	 * 用户的基本信息 格式为userid#userinfo
	 */
	public static final String USER_HASH_INFO = "#userinfo";
	
	/**
	 * 用户资源数量  格式为userid#user_resource_count
	 */
	public static final String USER_RESOURCE_COUNT = "#user_resource_count";
	//书评数量、影评数量、书单数量、影单数量、用户转发总数、文字总数、旧的长文章总数、新的长文章总数、连载总数、所有资源总数
	
	/**
	 * 任务完成数量(累计任务)
	 */
	public static final String USER_QUEST_COUNT = "USER_QUEST_COUNT";
	/**
	 * 任务完成数量（每日任务）
	 */
	public static final String USER_DAY_QUEST_COUNT = "USER_DAY_QUEST_COUNT";
	/**
	 * 书评的数量
	 */
	public static final String BKCOMMENT_COUNT = "BKCOMMENT_COUNT";
	/**
	 * 影评的数量
	 */
	public static final String MVCOMMENT_COUNT = "MVCOMMENT_COUNT";
	/**
	 * 书单数量
	 */
	public static final String BKLIST_COUNT = "BKLIST_COUNT";
	/**
	 * 影单数量
	 */
	public static final String MVLIST_COUNT = "MVLIST_COUNT";
	/**
	 * 用户转发总数
	 */
	public static final String TRANSMIT_COUNT = "TRANSMIT_COUNT";
	/**
	 * 文字总数
	 */
	public static final String DIARY_COUNT = "DIARY_COUNT";
	/**
	 * 旧的长文章总数
	 */
	public static final String POST_COUNT = "POST_COUNT";
	/**
	 * 新的长文章
	 */
	public static final String ARTICLE_COUNT = "ARTICLE_COUNT";
	/**
	 * 连载总数
	 */
	public static final String SERIALIZE_COUNT = "SERIALIZE_COUNT";
	/**
	 * 所有资源总数
	 */
	public static final String RES_TOTAL_COUNT = "RES_TOTAL_COUNT";
	
	
	
	/**
	 * 用户的id
	 */
	public static final String USER_HASH_ID = "id";
	
	/**
	 * 用户的昵称
	 */
	public static final String USER_HASH_NAME = "nickName";
	
	/**
	 * 用户的头像地址
	 */
	public static final String USER_HASH_FACE = "face_url";
	
	/**
	 * 用户的生日
	 */
	public static final String USER_HASH_BIRTHDAY = "USER_BIRTHDAY";
	
	/**
	 * 用户的性别
	 */
	public static final String USER_HASH_SEX = "sex";
	
	/**
	 * 用户的等级
	 */
	public static final String USER_HASH_LEVEL = "level";
	
	/**
	 * 用户的类型
	 */
	public static final String USER_HASH_TYPE = "type";
	
	/**
	 * 用户的二维码
	 */
	public static final String USER_HASH_TWO_CODE = "USER_TWO_CODE";
	
	/**
	 * 用户的感情状态
	 */
	public static final String USER_HASH_AFFECTIVE_STATES = "affective";
	
	/**
	 * 用户的居住地
	 */
	public static final String USER_HASH_RESIDENCE = "residence";
	
	/**
	 * 用户的职业
	 */
	public static final String USER_HASH_PROFESSION = "profession";
	
	/**
	 * 用户的关注数
	 */
	public static final String USER_HASH_ATTENTION_COUNT = "plusNum";
	
	/**
	 * 用户的粉丝数
	 */
	public static final String USER_HASH_FANS_COUNT = "fansNum";
	
	/**
	 * 用户的签名
	 */
	public static final String USER_HASH_SIGN = "sign";
	
	/**
	 * 用户的兴趣爱好
	 */
	public static final String USER_HASH_INTEREST = "USER_INTEREST";
	
	/**
	 * 用户的个人简介
	 */
	public static final String USER_HASH_INTRODUCTION = "introduction";
	
	/**
	 * 用户的点赞提醒
	 */
	public static final String USER_HASH_PRAISE_NOTICE = "USER_PRAISE_NOTICE";
	
	/**
	 * 用户的有用信息提醒
	 */
	public static final String USER_HASH_USEFUL_NOTICE = "USER_USEFUL_NOTICE";
	
	/**
	 * 用户的粉丝通知数量
	 */
	public static final String USER_HASH_FANS_NOTICE = "USER_FANS_NOTICE";
	
	/**
	 * 用户的粉丝更新时间
	 */
	public static final String USER_HASH_FANS_TIME = "USER_HASH_FANS_TIME";
	
	/**
	 * 用户的打赏通知数量
	 */
	public static final String USER_HASH_REWARD_NOTICE = "USER_REWARD_NOTICE";
	
	/**
	 * 用户的评论通知数量
	 */
	public static final String USER_HASH_COMMENT_NOTICE = "USER_COMMENT_NOTICE";
	
	/**
	 * 用户的at通知数量
	 */
	public static final String USER_HASH_AT_NOTICE = "USER_AT_NOTICE";
	
	/**
	 * 用户的朋友圈通知数量
	 */
	public static final String USER_HASH_MOMENT_NOTICE = "USER_MOMENT_NOTICE";
	
	/**
	 * 用户的发布时间
	 */
	public static final String USER_HASH_PUBLISH_TIME = "USER_HASH_PUBLISH_TIME";
	
	/**
	 * 用户的发布统计
	 */
	public static final String USER_HASH_PUBLISH_COUNT = "USER_HASH_PUBLISH_COUNT";
	
	/**
	 * 用户的开始计数时间  2015-06-01 00:00
	 */
	public static final long USER_HASH_PUBLISH_START_TIME = 1433088000000l;
	
	/**
	 * 用户开始计数的时间间隔 暂定为2个月 24*60*60*1000*30*2
	 */
	public static final long USER_HASH_PUBLISH_TIME_INTERVAL = 5184000000l;
	
	/**
	 * 用户的关注列表格式为userid#userattention
	 */
	public static final String USER_ATTENTION_LIST = "#userattention";
	
	/**
	 * 用户的粉丝列表
	 */
	public static final String USER_FANS_LIST = "#userfans";
	
	/**
	 * 用户的关注关系userid#attention#attentionid
	 */
	public static final String RELATION_USER_ATTENTION = "#attention#";
	
	/**
	 * 用户关系的是否关注信息
	 */
	public static final String RELATION_USER_ISINTEREST = "isInterest";
}
