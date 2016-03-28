package com.poison.eagle.utils;

/**
 * @Todo:TODO
 * @author :zhangqi
 * @time:2015-5-22下午3:34:12
 * @return
 */
public class StoreJedisConstant {

	public final static int expireSeconds = 30 * 24 * 60 * 60;// redis有效期:30天
	public final static int pageSize = 12;// 每页显示的条数

	/**
	 * 电影图书的hasH
	 */
	public static final String STORE_MOVIE_HASH = "#STORE_MOVIE_HASH_ID_";
	public static final String STORE_MOVIE_HASH_INFO = "STORE_MOVIE_HASH_INFO";

	public static final String STORE_MOVIE_HASH_ID = "id";
	public static final String STORE_MOVIE_HASH_MOVIE_URL = "movie_url";
	public static final String STORE_MOVIE_HASH_MOVIE_PIC = "movie_pic";
	public static final String STORE_MOVIE_HASH_MOVIE_PIC2 = "movie_pic2";
	public static final String STORE_MOVIE_HASH_NAME = "name";
	public static final String STORE_MOVIE_HASH_SCORE = "score";
	public static final String STORE_MOVIE_HASH_DIRECTOR = "director";
	public static final String STORE_MOVIE_HASH_SCREENWRITER = "screenwriter";
	public static final String STORE_MOVIE_HASH_ACTOR = "actor";
	public static final String STORE_MOVIE_HASH_TAGS = "tags";
	public static final String STORE_MOVIE_HASH_USER_TAGS = "user_tags";
	public static final String STORE_MOVIE_HASH_PRO_COUNTRIES = "pro_countries";
	public static final String STORE_MOVIE_LANGUAGE = "language";
	public static final String STORE_MOVIE_HASH_RELEASE_DATE = "release_date";
	public static final String STORE_MOVIE_HASH_ABOUT = "about";
	public static final String STORE_MOVIE_HASH_NUMBER = "number";
	public static final String STORE_MOVIE_HASH_FILM_TIME = "film_time";
	public static final String STORE_MOVIE_HASH_ALIAS = "alias";
	public static final String STORE_MOVIE_HASH_IMDB_LINK = "imdb_link";
	public static final String STORE_MOVIE_HASH_DESCRIBE = "describe";
	public static final String STORE_MOVIE_HASH_COLL_TIME = "coll_time";
	public static final String STORE_MOVIE_HASH_BOX_OFFICE = "box_office";
	public static final String STORE_MOVIE_HASH_WEEK_BOX_OFFICE = "week_box_office";
	public static final String STORE_MOVIE_HASH_TYPE = "type";
	public static final String STORE_MOVIE_HASH_RANKING_LIST = "ranking_list";
	// public static final String STORE_MOVIE_HASH_IS_OPERATION =
	// "is_operation";

	// 电影的排序 sortset
	public static final String STORE_MOVIE_SET_SCORE_170 = "#STORE_MOVIE_SET_ID_SCORE_170";
	public static final String STORE_MOVIE_SET_VIEWNUM_170 = "#STORE_MOVIE_SET_ID_VIEWNUM_170";
	public static final String STORE_MOVIE_SET_ADDTIME_170 = "#STORE_MOVIE_SET_ID_ADDTIME_170";

	public static final String STORE_MOVIE_SET_SCORE_171 = "#STORE_MOVIE_SET_ID_SCORE_171";
	public static final String STORE_MOVIE_SET_VIEWNUM_171 = "#STORE_MOVIE_SET_ID_VIEWNUM_171";
	public static final String STORE_MOVIE_SET_ADDTIME_171 = "#STORE_MOVIE_SET_ID_ADDTIME_171";

	public static final String STORE_MOVIE_SET_SCORE_172 = "#STORE_MOVIE_SET_ID_SCORE_172";
	public static final String STORE_MOVIE_SET_VIEWNUM_172 = "#STORE_MOVIE_SET_ID_VIEWNUM_172";
	public static final String STORE_MOVIE_SET_ADDTIME_172 = "#STORE_MOVIE_SET_ID_ADDTIME_172";

	// 图书的排序 sortset
	public static final String STORE_BOOK_SET_SCORE_160 = "#STORE_BOOK_SET_ID_SCORE_160";
	public static final String STORE_BOOK_SET_VIEWNUM_160 = "#STORE_BOOK_SET_ID_VIEWNUM_160";
	public static final String STORE_BOOK_SET_ADDTIME_160 = "#STORE_BOOK_SET_ID_ADDTIME_160";

	public static final String STORE_BOOK_SET_SCORE_161 = "#STORE_BOOK_SET_ID_SCORE_161";
	public static final String STORE_BOOK_SET_VIEWNUM_161 = "#STORE_BOOK_SET_ID_VIEWNUM_161";
	public static final String STORE_BOOK_SET_ADDTIME_161 = "#STORE_BOOK_SET_ID_ADDTIME_161";

	public static final String STORE_BOOK_SET_SCORE_162 = "#STORE_BOOK_SET_ID_SCORE_162";
	public static final String STORE_BOOK_SET_VIEWNUM_162 = "#STORE_BOOK_SET_ID_VIEWNUM_162";
	public static final String STORE_BOOK_SET_ADDTIME_162 = "#STORE_BOOK_SET_ID_ADDTIME_162";

	// 影单
	public static final String STORE_MOVIEORDER_SET_140 = "#STORE_MOVIEORDER_SET_140";
	public static final String STORE_MOVIEORDER_SET_141 = "#STORE_MOVIEORDER_SET_141";
	public static final String STORE_MOVIEORDER_SET_142 = "#STORE_MOVIEORDER_SET_142";

	// 书单
	public static final String STORE_BOOKORDER_SET_150 = "#STORE_BOOKORDER_SET_150";
	public static final String STORE_BOOKORDER_SET_151 = "#STORE_BOOKORDER_SET_151";
	public static final String STORE_BOOKORDER_SET_152 = "#STORE_BOOKORDER_SET_152";

	public static final String STORE_MOVIELIST_HASH = "#STORE_MOVIELIST_HASH_ID_";
	public static final String STORE_MOVIELIST_HASH_ID = "id";
	public static final String STORE_MOVIELIST_HASH_FILMLIST_NAME = "filmlist_name";
	public static final String STORE_MOVIELIST_HASH_REASON = "reason";
	public static final String STORE_MOVIELIST_HASH_IS_DEL = "is_del";
	public static final String STORE_MOVIELIST_HASH_TYPE = "type";
	public static final String STORE_MOVIELIST_HASH_U_ID = "u_id";
	public static final String STORE_MOVIELIST_HASH_TAG = "tag";
	public static final String STORE_MOVIELIST_HASH_IS_PUBLISHING = "is_publishing";
	public static final String STORE_MOVIELIST_HASH_CREATE_DATE = "create_date";
	public static final String STORE_MOVIELIST_HASH_LATEST_REVISION_DATE = "latest_revision_date";
	public static final String STORE_MOVIELIST_HASH_COVER = "cover";
	public static final String STORE_MOVIELIST_HASH_COMMENT_COUNT = "comment_count";

	public static final String STORE_BOOK_HASH = "#STORE_BOOK_HASH_ID_";
	public static final String STORE_BOOK_HASH_INFO = "STORE_BOOK_HASH_INFO";

	public static final String STORE_BOOK_HASH_ID = "id";
	public static final String STORE_BOOK_HASH_BOOK_URL = "book_url";
	public static final String STORE_BOOK_HASH_BOOK_PIC = "book_pic";
	public static final String STORE_BOOK_HASH_NAME = "name";
	public static final String STORE_BOOK_HASH_SCORE = "score";
	public static final String STORE_BOOK_HASH_AUTHOR_NAME = "author_name";
	public static final String STORE_BOOK_HASH_TRANSLATOR = "translator";
	public static final String STORE_BOOK_HASH_PRESS = "press";
	public static final String STORE_BOOK_HASH_ORIGINAL_NAME = "original_name";
	public static final String STORE_BOOK_HASH_SUBTITLE = "subtitle";
	public static final String STORE_BOOK_HASH_PUBLISHING_TIME = "publishing_time";
	public static final String STORE_BOOK_HASH_NUMBER = "number";
	public static final String STORE_BOOK_HASH_PRICE = "price";
	public static final String STORE_BOOK_HASH_BINDING = "binding";
	public static final String STORE_BOOK_HASH_SERIES_NAME = "series_name";
	public static final String STORE_BOOK_HASH_TAGS = "tags";
	public static final String STORE_BOOK_HASH_CONTENT = "content";
	public static final String STORE_BOOK_HASH_AUTHOR_INFO = "author_info";
	public static final String STORE_BOOK_HASH_CATALOG = "catalog";
	public static final String STORE_BOOK_HASH_SERIES_INFO = "series_info";
	public static final String STORE_BOOK_HASH_ISBN = "isbn";
	public static final String STORE_BOOK_HASH_COLL_TIME = "coll_time";
	public static final String STORE_BOOK_HASH_SALES_VOLUME = "sales_volume";
	public static final String STORE_BOOK_HASH_RANKING_LIST = "ranking_list";
	// public static final String STORE_BOOK_HASH_IS_OPERATION = "is_operation";
	public static final String STORE_BOOK_HASH_USER_TAGS = "user_tags";

	public static final String STORE_BOOKLIST_HASH = "#STORE_BOOKLIST_HASH_ID_";
	public static final String STORE_BOOKLIST_HASH_ID = "id";
	public static final String STORE_BOOKLIST_HASH_BOOKLIST_NAME = "booklist_name";
	public static final String STORE_BOOKLIST_HASH_REASON = "reason";
	public static final String STORE_BOOKLIST_HASH_IS_DEL = "is_del";
	public static final String STORE_BOOKLIST_HASH_TYPE = "type";
	public static final String STORE_BOOKLIST_HASH_U_ID = "u_id";
	public static final String STORE_BOOKLIST_HASH_TAG = "tag";
	public static final String STORE_BOOKLIST_HASH_IS_PUBLISHING = "is_publishing";
	public static final String STORE_BOOKLIST_HASH_CREATE_DATE = "create_date";
	public static final String STORE_BOOKLIST_HASH_LATEST_REVISION_DATE = "latest_revision_date";
	public static final String STORE_BOOKLIST_HASH_COVER = "cover";
	public static final String STORE_BOOKLIST_HASH_COMMENT_COUNT = "comment_count";

}
