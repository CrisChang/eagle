package com.poison.eagle.utils;


/**
 * 操作返回值、四位int数据、各位按照success和error 的例子继续编写。
 * @author Administrator
 *
 */
public class ResultUtils {
	/**
	 * 操作成功 SUCCESS = 1000
	 */
	public static final int SUCCESS = 1000;
	/**
	 * 操作失败 ERROR = 1111
	 */
	public static final int ERROR = 1111;
	
	/**
	 * 信息插入错误
	 */
	public static final int INSERT_ERROR = 5000;
	
	/**
	 * 信息更新出错
	 */
	public static final int UPDATE_ERROR = 5100;
	
	/**
	 * 查询出错
	 */
	public static final int QUERY_ERROR = 5200;
	
	/**
	 * 用户已存在
	 */
	public static final int EXISTED_USER=6000;
	
	/**
	 * 用户昵称已存在
	 */
	public static final int EXISTED_USER_NAME=6600;
	
	/**
	 * 用户已关注
	 */
	public static final int EXISTED_ATTENTION=6001;
	
	/**
	 * 用户已经取消关注
	 */
	public static final int EXISTED_CANCEL_ATTENTION = 6002;
	
	/**
	 * 已经存在的第三方用户
	 */
	public static final int EXISTED_THIRD_PARTY = 6003;
	
	/**
	 * 两次错误的密码
	 */
	public static final int ERROR_VERIFY_PASSWORD = 6004;
	
	/**
	 * 输入错误的密码
	 */
	public static final int ERROR_INPUT_PASSWORD = 6005;
	
	/**
	 * 不存在的用户
	 */
	public static final int NO_EXISTED_USER=6100;
	
	/**
	 * 不存在的用户
	 */
	public static final int IS_ALREADY_REPORT=6201;
	
	/**
	 * 不存在的关注信息
	 */
	public static final int NO_EXISTED_ATTENTION = 6101;
	
	
	/**
	 * 存在的点赞信息
	 */
	public static final int EXISTED_PRAISE = 6300;
	
	/**
	 * 不存在的点赞信息
	 */
	public static final int EXISTED_CANCEL_PRAISE = 6301;
	
	/**
	 * 存在的加热信息
	 */
	public static final int EXISTED_IS_HOT = 6302;

	/**
	 * 存在的投票信息
	 */
	public static final int EXISTED_IS_VOTE = 6303;
	
	/**
	 * 已经删除的书单
	 */
	public static final int BOOKLIST_IS_ALREADY_DELETE = 6400;
	
	/**
	 * 已经发布的书单
	 */
	public static final int BOOKLIST_IS_ALREADY_PUBLISH = 6401;
	
	/**
	 * 书单名称已存在
	 */
	public static final int BKLISTNAME_IS_ALREADY_EXISTED = 6402;
	
	/**
	 * 书单不存在
	 */
	public static final int BKLIST_IS_NOT_EXISTED = 6403;
	
	/**
	 * 已经从书单中删除该本书
	 */
	public static final int BOOK_DELETE_FROM_BOOKLIST=6410;
	
	/**
	 * 书单中已经添过该本书
	 */
	public static final int BOOK_IS_ALREADY_EXIST=6411;
	
	/**
	 * 该书单只能通过扫描条形码加入
	 */
	public static final int BOOK_IS_COLLECTED_BY_SCAN = 6412;
	
	/**
	 * 影单中已经添加过这部电影
	 */
	public static final int FILM_IS_ALREADY_EXIST=6421;
	
	/**
	 * 影单名字已经存在
	 */
	public static final int FILMNAME_IS_ALREADY_EXIST=6422;
	
	/**
	 * 影片剧照已经存在
	 */
	public static final int FILMSTILLS_IS_ALREADY_EXIST=6423;
	
	/**
	 * 已经收藏过
	 */
	public static final int IS_COLLECTED = 6430;
	
	/**
	 * 已经取消收藏
	 */
	public static final int IS_CANCELED_COLLECT=6431;
	
	/**
	 * 已经订阅过
	 */
	public static final int IS_SUBSCRIBED = 6440;
	
	/**
	 * 已经取消订阅
	 */
	public static final int IS_CANCELED_SUBSCRIBE=6441;

	/**
	 * 已经加入书架
	 */
	public static final int IS_ALREADY_NOVELLIST = 6442;
	
	/**
	 * 用户密码错误
	 */
	public static final int USER_PASSWORD_ERROR = 6200;
	/**
	 * DATAISNULL=2001 数据不存在
	 */
	public static final int DATAISNULL=2001;
	/**
	 * DELETE_ERROR=2002;标识删除失败
	 */
    public static final int DELETE_ERROR=2002;
    
    /**
     * 请输入正确的手机格式
     */
    public static final int PHONENUM_ERROR = 3001;

	/**
	 * 已经存在该手机号
	 */
	public static final int EXISTED_PHONE = 3002;
    
    /**
     * 不能为空
     */
    public static final int NOT_NULL = 8000;
    
    /**
     * 您超过今天摇一摇次数了，请明天继续哟!
     */
    public static final int OVER_COUNT = 8001;
    
	/**
	 * 您超过今天的限制总额，请明天继续哟!
	 */
	public static final int OVER_DAY_TOTAL = 8002;
	
	/**
	 * 抱歉，您不能为自己打赏
	 */
	public static final int REWARD_ERROR = 8003;
	
	/**
	 * 业务繁忙，请稍后再试
	 */
	public static final int SYSTEM_BUSY = 8004;
	/**
	 * 已经设置过提现密码了
	 */
	public static final int REPEAT_TAKE_PASSWORD = 8005;
	/**
	 * 密码不能为空
	 */
	public static final int PASSWORD_ISNULL = 8006;
	/**
	 * 提现密码错误
	 */
	public static final int PASSWORD_WRONG = 8007;
	/**
	 * 请先设置提现密码再提现
	 */
	public static final int PASSWORD_NOTSET = 8008;
	/**
	 * 超过提现次数
	 */
	public static final int OVER_TAKETIMES = 8009;
	/**
	 * 低于最低提现金额
	 */
	public static final int LOW_TAKE_MONEY = 8010;
	/**
	 * 提现人姓名不能为空
	 */
	public static final int RECEIVE_NAME_ISNULL = 8011;
	/**
	 * 提现类型错误
	 */
	public static final int TAKE_TYPE_ERROR = 8012;
	/**
	 * 提现账户不能为空
	 */
	public static final int RECEIVE_ACCOUNT_ISNULL = 8013;
	/**
	 * 提现银行不能为空
	 */
	public static final int RECEIVE_BANK_ISNULL = 8014;
	/**
	 * 账户余额不足
	 */
	public static final int ACC_AMT_NOT_ENOUGH = 8015;
	/**
	 * 连续输错密码超过限定次数,请24小时候后再进行提现操作
	 */
	public static final int OVER_PASSWORD_WRONG_TIMES = 8016;
	/**
	 * 原始密码错误
	 */
	public static final int OLDPASSWORD_WRONG = 8017;
	/**
	 * 超出了允许提现时间范围
	 */
	public static final int OUT_TAKE_TIME = 8018;
	/**
	 * 身份证号错误
	 */
	public static final int SID_WRONG = 8019;
	/**
	 * 大赛影评内容不允许修改
	 */
	public static final int MATCH_MV_COMMENT_NOT_ALLOW_UPDATE = 8020;
	/**
	 * 金币余额不足
	 */
	public static final int ACC_GOLD_NOT_ENOUGH = 8021;
}
