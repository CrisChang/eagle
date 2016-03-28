package com.poison.eagle.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.JsonGenerationException;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.StoreJedisConstant;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.ResourceLinkFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BkAvgMark;
import com.poison.resource.model.BookList;
import com.poison.resource.model.BookListLink;
import com.poison.resource.model.MovieList;
import com.poison.resource.model.MvAvgMark;
import com.poison.resource.model.MvListLink;
import com.poison.resource.model.ResourceLink;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;

/**
 * 存储电影图书的 redis 管理器
 * 
 * @author zhangqi
 * 
 */
public class StoreJedisManager {

	private static final Log LOG = LogFactory.getLog(StoreJedisManager.class);

	private JedisSimpleClient storeHashClient;
	private MvCommentFacade mvCommentFacade;
	private BkCommentFacade bkCommentFacade;

	private ResourceLinkFacade resourceLinkFacade;
	private MyMovieFacade myMovieFacade;
	private MvFacade mvFacade;
	private BkFacade bkFacade;
	private GetResourceInfoFacadeImpl getResourceInfoFacade;

	public GetResourceInfoFacadeImpl getGetResourceInfoFacade() {
		return getResourceInfoFacade;
	}

	public void setGetResourceInfoFacade(GetResourceInfoFacadeImpl getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}

	public void setResourceLinkFacade(ResourceLinkFacade resourceLinkFacade) {
		this.resourceLinkFacade = resourceLinkFacade;
	}

	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}

	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	public void setStoreHashClient(JedisSimpleClient storeHashClient) {
		this.storeHashClient = storeHashClient;
	}

	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}

	public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
		this.bkCommentFacade = bkCommentFacade;
	}

	/**
	 * 
	 * <p>
	 * Title: saveOneResource
	 * </p>
	 * <p>
	 * Description: 存储一条资源信息
	 * </p>
	 * 
	 * @param resId
	 * @param resourceInfo
	 * @return
	 */
	public String saveOneResource(final long resId, ResourceInfo resourceInfo) {
		String itemStr = "";
		if (null == resourceInfo) {
			return "";
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false);

			itemStr = objectMapper.writeValueAsString(resourceInfo);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 存入最终条目
		final String saveItem = itemStr;
		itemStr = storeHashClient.execute(new JedisWorker<String>() {
			String resHashKey = StoreJedisConstant.STORE_MOVIE_HASH_ID + resId;
			String resHashValue = StoreJedisConstant.STORE_MOVIE_HASH_INFO;

			@Override
			public String work(Jedis jedis) {
				// System.out.println(resHashKey);
				jedis.hset(resHashKey, resHashValue, saveItem);
				// jedis.hset(userInfoKey, StoreJedisConstant.USER_HASH_TYPE,
				// System.out.println(jedis.hget(resHashKey, resHashValue));
				return jedis.hget(resHashKey, resHashValue);
			}
		});
		return itemStr;
	}

	/**
	 * 
	 * <p>
	 * Title: delOneResource
	 * </p>
	 * <p>
	 * Description: 删除一条资源信息
	 * </p>
	 * 
	 * @param rid
	 * @return
	 */
	public int delOneResource(final long rid) {
		int flag = ResultUtils.ERROR;
		storeHashClient.execute(new JedisWorker<Integer>() {
			String resHashKey = StoreJedisConstant.STORE_MOVIE_HASH_ID + rid;
			String resHashValue = StoreJedisConstant.STORE_MOVIE_HASH_INFO;

			@Override
			public Integer work(Jedis jedis) {
				System.out.println(jedis.hdel(resHashKey, resHashValue));
				int status = ResultUtils.DATAISNULL;
				long index = jedis.hdel(resHashKey, resHashValue);
				if (index > 0) {
					status = ResultUtils.SUCCESS;
				}
				return status;
			}
		});
		return flag;
	}

	/**
	 * 
	 * <p>
	 * Title: saveOneResource
	 * </p>
	 * <p>
	 * Description: 存储一条资源信息
	 * </p>
	 * 
	 * @param resId
	 * @param movieInfo
	 * @return
	 */
	public Map<String, String> saveMovie(final long resId, final MvInfo movieInfo) {
		if (null == movieInfo) {
			return new HashMap<String, String>();
		}
		final Map<String, String> movieMap = storeHashClient.execute(new JedisWorker<Map<String, String>>() {
			String resHashKey = StoreJedisConstant.STORE_MOVIE_HASH + resId;

			@Override
			public Map<String, String> work(Jedis jedis) {
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIE_HASH_ID, CheckParams.objectToStr(movieInfo.getId()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIE_HASH_MOVIE_PIC, CheckParams.objectToStr(movieInfo.getMoviePic()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIE_HASH_MOVIE_PIC2, CheckParams.objectToStr(movieInfo.getMoviePic2()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIE_HASH_MOVIE_URL, CheckParams.objectToStr(movieInfo.getMovieUrl()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIE_HASH_NAME, CheckParams.objectToStr(movieInfo.getName()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIE_HASH_SCORE, CheckParams.objectToStr(movieInfo.getScore()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIE_HASH_RELEASE_DATE, CheckParams.objectToStr(movieInfo.getReleaseDate()));

				jedis.expire(resHashKey, StoreJedisConstant.expireSeconds);
				return jedis.hgetAll(resHashKey);
			}
		});
		return movieMap;
	}

	public Map<String, String> saveBook(final long resId, final BkInfo bkInfo) {
		if (null == bkInfo) {
			return new HashMap<String, String>();
		}
		final Map<String, String> movieMap = storeHashClient.execute(new JedisWorker<Map<String, String>>() {
			String resHashKey = StoreJedisConstant.STORE_BOOK_HASH + resId;

			@Override
			public Map<String, String> work(Jedis jedis) {

				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_ID, CheckParams.objectToStr(bkInfo.getId()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_BOOK_URL, CheckParams.objectToStr(bkInfo.getBookUrl()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_BOOK_PIC, CheckParams.objectToStr(bkInfo.getBookPic()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_NAME, CheckParams.objectToStr(bkInfo.getName()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_AUTHOR_NAME, CheckParams.objectToStr(bkInfo.getAuthorName()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_PUBLISHING_TIME, CheckParams.objectToStr(bkInfo.getPublishingTime()));

				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_PRESS, CheckParams.objectToStr(bkInfo.getPress()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_NUMBER, CheckParams.objectToStr(bkInfo.getNumber()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_BINDING, CheckParams.objectToStr(bkInfo.getBinding()));

				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_ISBN, CheckParams.objectToStr(bkInfo.getIsbn()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_PRICE, CheckParams.objectToStr(bkInfo.getPrice()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOK_HASH_SCORE, CheckParams.objectToStr(bkInfo.getScore()));

				// 新增加 redis 有效期
				jedis.expire(resHashKey, StoreJedisConstant.expireSeconds);

				return jedis.hgetAll(resHashKey);
			}
		});
		return movieMap;
	}

	public Map<String, String> saveMovieList(final long resId, final MovieList movieList) {
		if (null == movieList) {
			return new HashMap<String, String>();
		}
		final Map<String, String> movieMap = storeHashClient.execute(new JedisWorker<Map<String, String>>() {
			String resHashKey = StoreJedisConstant.STORE_MOVIELIST_HASH + resId;

			@Override
			public Map<String, String> work(Jedis jedis) {
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_ID, CheckParams.objectToStr(movieList.getId()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_FILMLIST_NAME, CheckParams.objectToStr(movieList.getFilmListName()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_REASON, CheckParams.objectToStr(movieList.getReason()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_IS_DEL, CheckParams.objectToStr(movieList.getIsDel()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_TYPE, CheckParams.objectToStr(movieList.getType()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_U_ID, CheckParams.objectToStr(movieList.getUid()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_TAG, CheckParams.objectToStr(movieList.getTag()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_IS_PUBLISHING, CheckParams.objectToStr(movieList.getIsPublishing()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_CREATE_DATE, CheckParams.objectToStr(movieList.getCreateDate()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_LATEST_REVISION_DATE, CheckParams.objectToStr(movieList.getLatestRevisionDate()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_MOVIELIST_HASH_COVER, CheckParams.objectToStr(movieList.getCover()));

				// 新增加 redis 有效期
				jedis.expire(resHashKey, StoreJedisConstant.expireSeconds);

				return jedis.hgetAll(resHashKey);
			}
		});
		return movieMap;
	}

	public Map<String, String> saveBookList(final long resId, final BookList bookList) {
		if (null == bookList) {
			return new HashMap<String, String>();
		}
		final Map<String, String> movieMap = storeHashClient.execute(new JedisWorker<Map<String, String>>() {
			String resHashKey = StoreJedisConstant.STORE_BOOKLIST_HASH + resId;

			@Override
			public Map<String, String> work(Jedis jedis) {
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_ID, CheckParams.objectToStr(bookList.getId()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_BOOKLIST_NAME, CheckParams.objectToStr(bookList.getBookListName()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_REASON, CheckParams.objectToStr(bookList.getReason()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_IS_DEL, CheckParams.objectToStr(bookList.getIsDel()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_TYPE, CheckParams.objectToStr(bookList.getType()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_U_ID, CheckParams.objectToStr(bookList.getuId()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_TAG, CheckParams.objectToStr(bookList.getTag()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_IS_PUBLISHING, CheckParams.objectToStr(bookList.getIsPublishing()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_CREATE_DATE, CheckParams.objectToStr(bookList.getCreateDate()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_LATEST_REVISION_DATE, CheckParams.objectToStr(bookList.getLatestRevisionDate()));
				jedis.hset(resHashKey, StoreJedisConstant.STORE_BOOKLIST_HASH_COVER, CheckParams.objectToStr(bookList.getCover()));

				// 新增加 redis 有效期
				jedis.expire(resHashKey, StoreJedisConstant.expireSeconds);

				return jedis.hgetAll(resHashKey);
			}
		});
		return movieMap;
	}

	public String saveMovieId(final MvInfo movieInfo, final String caseType, final MvListLink mvLink) {
		String sssString = null;
		if (null == movieInfo) {
			return null;
		}
		sssString = storeHashClient.execute(new JedisWorker<String>() {
			String resSetKeyScore = "";
			String resSetKeyViewNum = "";
			String resSetKeyAddTime = "";

			@Override
			public String work(Jedis jedis) {
				if (caseType.equals("170")) {
					resSetKeyScore = StoreJedisConstant.STORE_MOVIE_SET_SCORE_170;
					resSetKeyViewNum = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_170;
					resSetKeyAddTime = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_170;
				} else if (caseType.equals("171")) {
					resSetKeyScore = StoreJedisConstant.STORE_MOVIE_SET_SCORE_171;
					resSetKeyViewNum = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_171;
					resSetKeyAddTime = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_171;
				} else if (caseType.equals("172")) {
					resSetKeyScore = StoreJedisConstant.STORE_MOVIE_SET_SCORE_172;
					resSetKeyViewNum = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_172;
					resSetKeyAddTime = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_172;
				} else {
					return "";
				}

				Double newScore = CheckParams.stringToDouble(String.valueOf(movieInfo.getScore()));
				String movieInfoId = CheckParams.objectToStr(movieInfo.getId());
				boolean hasField = jedis.zrank(resSetKeyScore, movieInfoId) != null ? true : false;
				if (hasField) {
					Double score = jedis.zscore(resSetKeyScore, movieInfoId);
					if (score != null && score.doubleValue() < newScore.doubleValue()) {
						jedis.zadd(resSetKeyScore, newScore, movieInfoId);
					} else if (score == null) {
						jedis.zadd(resSetKeyScore, newScore, movieInfoId);
					}
				} else {
					jedis.zadd(resSetKeyScore, newScore, movieInfoId);
				}

				// jedis.zadd(resSetKeyScore, newScore, movieInfoId);

				newScore = 0.0;
				try {
					newScore = CheckParams.stringToDouble(String.valueOf(mvLink.getLatestRevisionDate()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				hasField = jedis.zrank(resSetKeyAddTime, movieInfoId) != null ? true : false;
				if (hasField) {
					Double score = jedis.zscore(resSetKeyAddTime, movieInfoId);
					if (score != null && score.doubleValue() < newScore.doubleValue()) {
						jedis.zadd(resSetKeyAddTime, newScore, movieInfoId);
					} else if (score == null) {
						jedis.zadd(resSetKeyAddTime, newScore, movieInfoId);
					}
				} else {
					jedis.zadd(resSetKeyAddTime, newScore, movieInfoId);
				}

				// jedis.zadd(resSetKeyAddTime, newScore, movieInfoId);

				MvAvgMark mvAvgMarkA = mvCommentFacade.findMvAvgMarkByMvId(movieInfo.getId());
				double viewNum = 0;
				if (mvAvgMarkA != null && mvAvgMarkA.getId() != 0) {
					try {
						viewNum = mvAvgMarkA.getMvTotalNum() + mvAvgMarkA.getExpertsTotalNum();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				hasField = jedis.zrank(resSetKeyViewNum, movieInfoId) != null ? true : false;
				if (hasField) {
					Double score = jedis.zscore(resSetKeyViewNum, movieInfoId);
					if (score != null && score.doubleValue() < viewNum) {
						jedis.zadd(resSetKeyViewNum, viewNum, movieInfoId);
					} else if (score == null) {
						jedis.zadd(resSetKeyViewNum, viewNum, movieInfoId);
					}
				} else {
					jedis.zadd(resSetKeyViewNum, viewNum, movieInfoId);
				}

				// jedis.zadd(resSetKeyViewNum, viewNum, movieInfoId);

				// 新增加 redis 有效期
				jedis.expire(resSetKeyScore, StoreJedisConstant.expireSeconds);
				jedis.expire(resSetKeyViewNum, StoreJedisConstant.expireSeconds);
				jedis.expire(resSetKeyAddTime, StoreJedisConstant.expireSeconds);

				Set<String> set = jedis.zrange(resSetKeyScore, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return sssString;
	}

	public String saveMovieListId(final ResourceLink link, final MovieList movieList, final String caseType) {
		String sssString = null;
		if (null == movieList) {
			return null;
		}
		sssString = storeHashClient.execute(new JedisWorker<String>() {
			String resSetKeyScore = "";

			@Override
			public String work(Jedis jedis) {
				if (caseType.equals("140")) {
					resSetKeyScore = StoreJedisConstant.STORE_MOVIEORDER_SET_140;
				} else if (caseType.equals("141")) {
					resSetKeyScore = StoreJedisConstant.STORE_MOVIEORDER_SET_141;
				} else if (caseType.equals("142")) {
					resSetKeyScore = StoreJedisConstant.STORE_MOVIEORDER_SET_142;
				} else {
					return "";
				}

				Double newScore = CheckParams.stringToDouble(String.valueOf(link.getId()));
				String movieListId = CheckParams.objectToStr(movieList.getId());
				boolean hasField = jedis.zrank(resSetKeyScore, movieListId) != null ? true : false;
				if (hasField) {
					Double score = jedis.zscore(resSetKeyScore, movieListId);
					if (score != null && score.doubleValue() < newScore.doubleValue()) {
						jedis.zadd(resSetKeyScore, newScore, movieListId);
					} else if (score == null) {
						jedis.zadd(resSetKeyScore, newScore, movieListId);
					}
				} else {
					jedis.zadd(resSetKeyScore, newScore, movieListId);
				}

				// 新增加 redis 有效期
				jedis.expire(resSetKeyScore, StoreJedisConstant.expireSeconds);

				Set<String> set = jedis.zrange(resSetKeyScore, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return sssString;
	}

	public String saveBookId(final BkInfo bookInfo, final String caseType, final BookListLink mvLink) {
		String sssString = null;
		if (null == bookInfo) {
			return null;
		}
		sssString = storeHashClient.execute(new JedisWorker<String>() {
			String resSetKeyScore = "";
			String resSetKeyViewNum = "";
			String resSetKeyAddTime = "";

			@Override
			public String work(Jedis jedis) {
				if (caseType.equals("160")) {
					resSetKeyScore = StoreJedisConstant.STORE_BOOK_SET_SCORE_160;
					resSetKeyViewNum = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_160;
					resSetKeyAddTime = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_160;
				} else if (caseType.equals("161")) {
					resSetKeyScore = StoreJedisConstant.STORE_BOOK_SET_SCORE_161;
					resSetKeyViewNum = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_161;
					resSetKeyAddTime = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_161;
				} else if (caseType.equals("162")) {
					resSetKeyScore = StoreJedisConstant.STORE_BOOK_SET_SCORE_162;
					resSetKeyViewNum = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_162;
					resSetKeyAddTime = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_162;
				} else {
					return "";
				}

				Double newScore = CheckParams.stringToDouble(String.valueOf(bookInfo.getScore()));
				String bookInfoId = CheckParams.objectToStr(bookInfo.getId());
				boolean hasField = jedis.zrank(resSetKeyScore, bookInfoId) != null ? true : false;
				if (hasField) {
					Double score = jedis.zscore(resSetKeyScore, bookInfoId);
					if (score != null && score.doubleValue() < newScore.doubleValue()) {
						jedis.zadd(resSetKeyScore, newScore, bookInfoId);
					} else if (score == null) {
						jedis.zadd(resSetKeyScore, newScore, bookInfoId);
					}
				} else {
					jedis.zadd(resSetKeyScore, newScore, bookInfoId);
				}
				// jedis.zadd(resSetKeyScore, newScore, bookInfoId);

				newScore = 0.0;
				try {
					newScore = CheckParams.stringToDouble(String.valueOf(mvLink.getLatestRevisionDate()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				hasField = jedis.zrank(resSetKeyAddTime, bookInfoId) != null ? true : false;
				if (hasField) {
					Double score = jedis.zscore(resSetKeyAddTime, bookInfoId);
					if (score != null && score.doubleValue() < newScore.doubleValue()) {
						jedis.zadd(resSetKeyAddTime, newScore, bookInfoId);
					} else if (score == null) {
						jedis.zadd(resSetKeyAddTime, newScore, bookInfoId);
					}
				} else {
					jedis.zadd(resSetKeyAddTime, newScore, bookInfoId);
				}
				// jedis.zadd(resSetKeyAddTime, newScore, bookInfoId);

				BkAvgMark bkAvgMark = bkCommentFacade.findBkAvgMarkByBkId(bookInfo.getId());
				double viewNum = 0.0;
				if (bkAvgMark != null && bkAvgMark.getId() != 0) {
					try {
						viewNum = bkAvgMark.getBkTotalNum();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				hasField = jedis.zrank(resSetKeyViewNum, bookInfoId) != null ? true : false;
				if (hasField) {
					Double score = jedis.zscore(resSetKeyViewNum, bookInfoId);
					if (score != null && score.doubleValue() < viewNum) {
						jedis.zadd(resSetKeyViewNum, viewNum, bookInfoId);
					} else if (score == null) {
						jedis.zadd(resSetKeyViewNum, viewNum, bookInfoId);
					}
				} else {
					jedis.zadd(resSetKeyViewNum, viewNum, bookInfoId);
				}

				// jedis.zadd(resSetKeyViewNum, viewNum, bookInfoId);

				// 新增加 redis 有效期
				jedis.expire(resSetKeyScore, StoreJedisConstant.expireSeconds);
				jedis.expire(resSetKeyViewNum, StoreJedisConstant.expireSeconds);
				jedis.expire(resSetKeyAddTime, StoreJedisConstant.expireSeconds);

				Set<String> set = jedis.zrange(resSetKeyScore, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return sssString;
	}

	public String saveBookListId(final ResourceLink link, final BookList bookList, final String caseType) {
		String sssString = null;
		if (null == bookList) {
			return null;
		}
		sssString = storeHashClient.execute(new JedisWorker<String>() {
			String resSetKeyScore = "";

			@Override
			public String work(Jedis jedis) {
				if (caseType.equals("150")) {
					resSetKeyScore = StoreJedisConstant.STORE_BOOKORDER_SET_150;
				} else if (caseType.equals("151")) {
					resSetKeyScore = StoreJedisConstant.STORE_BOOKORDER_SET_151;
				} else if (caseType.equals("152")) {
					resSetKeyScore = StoreJedisConstant.STORE_BOOKORDER_SET_152;
				} else {
					return "";
				}
				Double newScore = CheckParams.stringToDouble(String.valueOf(link.getId()));
				String bookListId = CheckParams.objectToStr(bookList.getId());
				boolean hasField = jedis.zrank(resSetKeyScore, bookListId) != null ? true : false;
				if (hasField) {
					Double score = jedis.zscore(resSetKeyScore, bookListId);
					if (score != null && score.doubleValue() < newScore.doubleValue()) {
						jedis.zadd(resSetKeyScore, newScore, bookListId);
					} else if (score == null) {
						jedis.zadd(resSetKeyScore, newScore, bookListId);
					}
				} else {
					jedis.zadd(resSetKeyScore, newScore, bookListId);
				}

				// 新增加 redis 有效期
				jedis.expire(resSetKeyScore, StoreJedisConstant.expireSeconds);

				Set<String> set = jedis.zrange(resSetKeyScore, 0, 0);
				String result = set.toString().replace("[", "").replace("]", "");
				return result;
			}
		});
		return sssString;
	}

	public int delOneMovie(final long rid) {
		int flag = ResultUtils.ERROR;
		storeHashClient.execute(new JedisWorker<Integer>() {
			String resHashKey = StoreJedisConstant.STORE_MOVIE_HASH_ID + rid;
			String resHashValue = StoreJedisConstant.STORE_MOVIE_HASH_INFO;

			@Override
			public Integer work(Jedis jedis) {
				System.out.println(jedis.hdel(resHashKey, resHashValue));
				int status = ResultUtils.DATAISNULL;
				long index = jedis.hdel(resHashKey, resHashValue);
				if (index > 0) {
					status = ResultUtils.SUCCESS;
				}
				return status;
			}
		});
		return flag;
	}

	/**
	 * 查询此电影的信息
	 * 
	 * @param movieId
	 * @return
	 */
	public Map<String, String> getMovieInfo(final long movieId) {
		Map<String, String> userAndResInfo = storeHashClient.execute(new JedisWorker<Map<String, String>>() {
			@Override
			public Map<String, String> work(Jedis jedis) {
				String relationkey = StoreJedisConstant.STORE_MOVIE_HASH + movieId;
				return jedis.hgetAll(relationkey);
			}
		});
		return userAndResInfo;
	}

	/**
	 * 判断 redis 是否存在此key
	 * 
	 * @param key
	 * @return
	 */
	public boolean isRedisExists(final String key) {
		boolean exists = storeHashClient.execute(new JedisWorker<Boolean>() {
			@Override
			public Boolean work(Jedis jedis) {
				return jedis.exists(key);
			}
		});
		return exists;
	}

	/**
	 * 获取运营商添加的电影
	 * 
	 * @param lastUserId
	 * @return
	 */
	public List<Map<String, String>> getOperMovies(final Long lastUserId, final String caseType, final String sort, final String newType) {
		final List<Map<String, String>> userFansList = storeHashClient.execute(new JedisWorker<List<Map<String, String>>>() {
			@Override
			public List<Map<String, String>> work(Jedis jedis) {
				String storeSetKey = "";
				String proxyStoreHashKey = "";
				if (caseType.equals("170")) {
					if (sort.equals("score")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_170;
					} else if (sort.equals("reviewNum")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_170;
					} else if (sort.equals("addTime")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_170;
					} else {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_170;
					}
					proxyStoreHashKey = StoreJedisConstant.STORE_MOVIE_HASH;
				} else if (caseType.equals("171")) {
					if (sort.equals("score")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_171;
					} else if (sort.equals("reviewNum")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_171;
					} else if (sort.equals("addTime")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_171;
					} else {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_171;
					}
					proxyStoreHashKey = StoreJedisConstant.STORE_MOVIE_HASH;
				} else if (caseType.equals("172")) {
					if (sort.equals("score")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_172;
					} else if (sort.equals("reviewNum")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_172;
					} else if (sort.equals("addTime")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_172;
					} else {
						storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_172;
					}
					proxyStoreHashKey = StoreJedisConstant.STORE_MOVIE_HASH;
				} else if (caseType.equals("140")) {
					storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_140;
					proxyStoreHashKey = StoreJedisConstant.STORE_MOVIELIST_HASH;
				} else if (caseType.equals("141")) {
					storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_141;
					proxyStoreHashKey = StoreJedisConstant.STORE_MOVIELIST_HASH;
				} else if (caseType.equals("142")) {
					storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_142;
					proxyStoreHashKey = StoreJedisConstant.STORE_MOVIELIST_HASH;
				} else if (caseType.equals("160")) {
					if (sort.equals("score")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_160;
					} else if (sort.equals("reviewNum")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_160;
					} else if (sort.equals("addTime")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_160;
					} else {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_160;
					}
					proxyStoreHashKey = StoreJedisConstant.STORE_BOOK_HASH;
				} else if (caseType.equals("161")) {
					if (sort.equals("score")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_161;
					} else if (sort.equals("reviewNum")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_161;
					} else if (sort.equals("addTime")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_161;
					} else {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_161;
					}
					proxyStoreHashKey = StoreJedisConstant.STORE_BOOK_HASH;
				} else if (caseType.equals("162")) {
					if (sort.equals("score")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_162;
					} else if (sort.equals("reviewNum")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_162;
					} else if (sort.equals("addTime")) {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_162;
					} else {
						storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_162;
					}
					proxyStoreHashKey = StoreJedisConstant.STORE_BOOK_HASH;
				} else if (caseType.equals("150")) {
					storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_150;
					proxyStoreHashKey = StoreJedisConstant.STORE_BOOKLIST_HASH;
				} else if (caseType.equals("151")) {
					storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_151;
					proxyStoreHashKey = StoreJedisConstant.STORE_BOOKLIST_HASH;
				} else if (caseType.equals("152")) {
					storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_152;
					proxyStoreHashKey = StoreJedisConstant.STORE_BOOKLIST_HASH;
				}
				// 新增 redis 有效期验证
				if (jedis.ttl(storeSetKey) == -1) {
					jedis.expire(storeSetKey, StoreJedisConstant.expireSeconds);
				}

				jedis.zrevrange(storeSetKey, 0, -1);
				final String storeHashKey = proxyStoreHashKey;
				final String storeSetKeyProxy = storeSetKey;
				long end = jedis.zcard(storeSetKey);
				int size = StoreJedisConstant.pageSize;// 10=>12
				if (sort.equals("")) {
					if (caseType.equals("150") || caseType.equals("151") || caseType.equals("152") || caseType.equals("140") || caseType.equals("141") || caseType.equals("142")
							|| newType.equals("171")) {
						size = (int) (end);
					}
				}
				List<Map<String, String>> resList = new ArrayList<Map<String, String>>();
				Set<String> tempSet = new HashSet<String>();
				if (end == 0) {
					return resList;
				}
				if (null == lastUserId) {
					if (end > size) {// 当缓存中得数量超过分页的数量时
						// tempSet = jedis.zrange(storeSetKey, 0, size-1);
						tempSet = jedis.zrevrange(storeSetKey, 0, size - 1);
					} else {// 当缓存数量小于分页数量时
						// tempSet = jedis.zrange(storeSetKey, 0,end - 1);
						tempSet = jedis.zrevrange(storeSetKey, 0, end - 1);
					}
				} else {
					// 查找最后一条用户所在的位置
					// long index = jedis.zrank(storeSetKey, lastUserId + "");
					long index = jedis.zrevrank(storeSetKey, lastUserId + "");
					// 得到分页的set列表
					long endIndex = index + size;
					if (endIndex > end) {// 当分页大于长度的时候
						// tempSet = jedis.zrange(storeSetKey,index + 1, end);
						tempSet = jedis.zrevrange(storeSetKey, index + 1, end);
					} else {// 当分页小于最后的长度的时候
						// tempSet = jedis.zrange(storeSetKey,index + 1,
						// endIndex);
						tempSet = jedis.zrevrange(storeSetKey, index + 1, endIndex);
					}
				}
				final Set<String> set = new LinkedHashSet<String>(tempSet);
				List<Map<String, String>> resList2 = storeHashClient.execute(new JedisWorker<List<Map<String, String>>>() {
					@Override
					public List<Map<String, String>> work(Jedis jedis) {
						List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
						for (String uidStr : set) {
							final String uidKey = uidStr;
							if ("0".equals(uidKey) || null == uidKey || "".equals(uidKey)) {
								continue;
							}
							Map<String, String> redisMap = storeHashClient.execute(new JedisWorker<Map<String, String>>() {
								@Override
								public Map<String, String> work(Jedis jedis) {
									try {
										String infoKey = storeHashKey + uidKey;
										// 如果不存在就去存储一边
										if (!jedis.exists(infoKey)) {
											updateRedis(uidKey, caseType);
										}

										// 新增 redis 有效期验证
										if (jedis.ttl(infoKey) == -1) {
											jedis.expire(infoKey, StoreJedisConstant.expireSeconds);
										}

										Map<String, String> tempMap = jedis.hgetAll(infoKey);
										// resultList.add(tempMap);
										return tempMap;
									} catch (Exception e) {
										e.printStackTrace();
									}
									return null;
								}
							});
							if (null != redisMap) {
								resultList.add(redisMap);
							}
						}
						return resultList;
					}
				});
				return resList2;
			}
		});
		return userFansList;
	}

	/**
	 * 更新redis ,哪果没有数据就去 redis服务器中找
	 * 
	 * @param storeHashKey
	 *            有问题，所以此方法不再使用
	 * @param uidKey
	 * @param caseType
	 */
	private void updateRedis(final String storeSetKey, final String uidKey, final String caseType) {
		try {
			long uid = CheckParams.stringToLong(uidKey);
			long resListId = CheckParams.stringToLong(storeSetKey);
			if (StringUtils.equals(caseType, "170") || StringUtils.equals(caseType, "171") || StringUtils.equals(caseType, "172")) {
				List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(resListId, uid, null);
				for (MvListLink mvLink : mvListLinks) {
					try {
						MvInfo info = mvFacade.queryById(mvLink.getMovieId());
						if (info.getFlag() == ResultUtils.SUCCESS) {
							// saveMovieId(info, caseType, mvLink);
							saveMovie(info.getId(), info);
						}
					} catch (Exception e) {
						LOG.error(e.getMessage(), e.fillInStackTrace());
					}
				}
			}
			if (StringUtils.equals(caseType, "160") || StringUtils.equals(caseType, "161") || StringUtils.equals(caseType, "162")) {
				List<BookListLink> bookListLinks = getResourceInfoFacade.findBookListInfo(resListId, uid, null);
				try {
					for (BookListLink mvLink : bookListLinks) {
						BkInfo info = bkFacade.findBkInfo(mvLink.getBookId());
						if (info.getFlag() == ResultUtils.SUCCESS) {
							// saveBookId(info, caseType, mvLink);
							saveBook(info.getId(), info);
						}
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}
			if (caseType.equals("140") || caseType.equals("141") || caseType.equals("142")) {
				List<ResourceLink> resourceLinks = resourceLinkFacade.findResListByResLinkIdAndType(resListId, caseType);
				for (ResourceLink link : resourceLinks) {
					MovieList movieList = myMovieFacade.findMovieListById(link.getResLinkId());
					try {
						if (movieList.getFlag() == ResultUtils.SUCCESS) {
							// saveMovieListId(link, movieList, caseType);
							saveMovieList(movieList.getId(), movieList);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (caseType.equals("150") || caseType.equals("151") || caseType.equals("152")) {
				List<ResourceLink> resourceLinks = resourceLinkFacade.findResListByResLinkIdAndType(resListId, caseType);
				for (ResourceLink link : resourceLinks) {
					BookList bookList = getResourceInfoFacade.queryByIdBookList(link.getResLinkId());
					try {
						if (bookList.getFlag() == ResultUtils.SUCCESS) {
							// saveBookListId(link, bookList, caseType);
							saveBookList(bookList.getId(), bookList);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
	}

	/**
	 * 更新redis ,哪果没有数据就去 redis服务器中找
	 * 
	 * @param infoKey
	 * @param storeHashKey
	 * @param uidKey
	 * @param caseType
	 */
	public void updateRedis(final String uidKey, final String caseType) {
		try {
			long uid = CheckParams.stringToLong(uidKey);
			if (StringUtils.equals(caseType, "170") || StringUtils.equals(caseType, "171") || StringUtils.equals(caseType, "172")) {
				try {
					MvInfo info = mvFacade.queryById(uid);
					if (info.getFlag() == ResultUtils.SUCCESS) {
						// saveMovieId(info, caseType, mvLink);
						saveMovie(info.getId(), info);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}
			if (StringUtils.equals(caseType, "160") || StringUtils.equals(caseType, "161") || StringUtils.equals(caseType, "162")) {
				try {
					BkInfo info = bkFacade.findBkInfo((int) uid);
					if (info.getFlag() == ResultUtils.SUCCESS) {
						// saveBookId(info, caseType, mvLink);
						saveBook(info.getId(), info);
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			}
			if (caseType.equals("140") || caseType.equals("141") || caseType.equals("142")) {
				MovieList movieList = myMovieFacade.findMovieListById(uid);
				try {
					if (movieList.getFlag() == ResultUtils.SUCCESS) {
						// saveMovieListId(link, movieList, caseType);
						saveMovieList(movieList.getId(), movieList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (caseType.equals("150") || caseType.equals("151") || caseType.equals("152")) {
				BookList bookList = getResourceInfoFacade.queryByIdBookList(uid);
				try {
					if (bookList.getFlag() == ResultUtils.SUCCESS) {
						// saveBookListId(link, bookList, caseType);
						saveBookList(bookList.getId(), bookList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
	}

	public Long getRedisIndex(final String caseType, final long index, final String sort) {
		final Long ids = storeHashClient.execute(new JedisWorker<Long>() {
			public Long work(Jedis jedis) {
				try {
					String storeSetKey = "";
					if (caseType.equals("170")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_170;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_170;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_170;
						} else {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_170;
						}
					} else if (caseType.equals("171")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_171;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_171;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_171;
						} else {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_171;
						}
					} else if (caseType.equals("172")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_172;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_172;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_172;
						} else {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_172;
						}
					} else if (caseType.equals("140")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_140;
					} else if (caseType.equals("141")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_141;
					} else if (caseType.equals("142")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_142;
					} else if (caseType.equals("160")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_160;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_160;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_160;
						} else {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_160;
						}
					} else if (caseType.equals("161")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_161;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_161;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_161;
						} else {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_161;
						}
					} else if (caseType.equals("162")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_162;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_162;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_162;
						} else {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_162;
						}
					} else if (caseType.equals("150")) {
						storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_150;
					} else if (caseType.equals("151")) {
						storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_151;
					} else if (caseType.equals("152")) {
						storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_152;
					}

					// 新增加 redis 有效期设置
					if (jedis.ttl(storeSetKey) == -1) {
						jedis.expire(storeSetKey, StoreJedisConstant.expireSeconds);
					} else if (jedis.ttl(storeSetKey) == -2) {
						// 过期设置
						saveBkMvRedis(caseType);
					}

					// Set<String> set = jedis.zrange(storeSetKey, index
					// ,index);
					Set<String> set = jedis.zrevrange(storeSetKey, index, index);
					String id = set.iterator().next();
					return Long.parseLong(id);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				return null;
			}
		});
		return ids;
	}

	/**
	 * 保存电影和图书相关的redis
	 * 
	 * @param caseType
	 */
	public void saveBkMvRedis(String caseType) {
		if (caseType.equals("170") || caseType.equals("171") || caseType.equals("172")) {
			List<ResourceLink> resourceLinks = resourceLinkFacade.findResListByType(caseType);
			for (ResourceLink link : resourceLinks) {
				try {
					List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(link.getResLinkId(), null, null);
					for (MvListLink mvLink : mvListLinks) {
						MvInfo info = mvFacade.queryById(mvLink.getMovieId());
						if (info.getFlag() == ResultUtils.SUCCESS) {
							saveMovieId(info, caseType, mvLink);
							saveMovie(info.getId(), info);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (caseType.equals("160") || caseType.equals("161") || caseType.equals("162")) {
			List<ResourceLink> resourceLinks = resourceLinkFacade.findResListByType(caseType);
			for (ResourceLink link : resourceLinks) {
				List<BookListLink> bookListLinks = getResourceInfoFacade.findBookListInfo(link.getResLinkId(), null, null);
				try {
					for (BookListLink mvLink : bookListLinks) {
						BkInfo info = bkFacade.findBkInfo(mvLink.getBookId());
						if (info.getFlag() == ResultUtils.SUCCESS) {
							saveBookId(info, caseType, mvLink);
							saveBook(info.getId(), info);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (caseType.equals("140") || caseType.equals("141") || caseType.equals("142")) {
			List<ResourceLink> resourceLinks = resourceLinkFacade.findResListByType(caseType);
			for (ResourceLink link : resourceLinks) {
				MovieList movieList = myMovieFacade.findMovieListById(link.getResLinkId());
				try {
					if (movieList.getFlag() == ResultUtils.SUCCESS) {
						saveMovieListId(link, movieList, caseType);
						saveMovieList(movieList.getId(), movieList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (caseType.equals("150") || caseType.equals("151") || caseType.equals("152")) {
			List<ResourceLink> resourceLinks = resourceLinkFacade.findResListByType(caseType);
			for (ResourceLink link : resourceLinks) {
				BookList bookList = getResourceInfoFacade.queryByIdBookList(link.getResLinkId());
				try {
					if (bookList.getFlag() == ResultUtils.SUCCESS) {
						saveBookListId(link, bookList, caseType);
						saveBookList(bookList.getId(), bookList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询redis长度
	 * 
	 * @param caseType
	 * @param sort
	 * @return
	 */
	public Long getEndRedisIndex(final String caseType, final String sort) {
		final Long redisIndex = storeHashClient.execute(new JedisWorker<Long>() {
			public Long work(Jedis jedis) {
				try {
					String storeSetKey = "";
					String proxyStoreHashKey = "";
					if (caseType.equals("170")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_170;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_170;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_170;
						} else {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_170;
						}
						proxyStoreHashKey = StoreJedisConstant.STORE_MOVIE_HASH;
					} else if (caseType.equals("171")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_171;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_171;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_171;
						} else {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_171;
						}
						proxyStoreHashKey = StoreJedisConstant.STORE_MOVIE_HASH;
					} else if (caseType.equals("172")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_SCORE_172;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_VIEWNUM_172;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_172;
						} else {
							storeSetKey = StoreJedisConstant.STORE_MOVIE_SET_ADDTIME_172;
						}
						proxyStoreHashKey = StoreJedisConstant.STORE_MOVIE_HASH;
					} else if (caseType.equals("140")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_140;
						proxyStoreHashKey = StoreJedisConstant.STORE_MOVIELIST_HASH;
					} else if (caseType.equals("141")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_141;
						proxyStoreHashKey = StoreJedisConstant.STORE_MOVIELIST_HASH;
					} else if (caseType.equals("142")) {
						storeSetKey = StoreJedisConstant.STORE_MOVIEORDER_SET_142;
						proxyStoreHashKey = StoreJedisConstant.STORE_MOVIELIST_HASH;
					} else if (caseType.equals("160")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_160;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_160;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_160;
						} else {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_160;
						}
						proxyStoreHashKey = StoreJedisConstant.STORE_BOOK_HASH;
					} else if (caseType.equals("161")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_161;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_161;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_161;
						} else {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_161;
						}
						proxyStoreHashKey = StoreJedisConstant.STORE_BOOK_HASH;
					} else if (caseType.equals("162")) {
						if (sort.equals("score")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_SCORE_162;
						} else if (sort.equals("reviewNum")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_VIEWNUM_162;
						} else if (sort.equals("addTime")) {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_162;
						} else {
							storeSetKey = StoreJedisConstant.STORE_BOOK_SET_ADDTIME_162;
						}
						proxyStoreHashKey = StoreJedisConstant.STORE_BOOK_HASH;
					} else if (caseType.equals("150")) {
						storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_150;
						proxyStoreHashKey = StoreJedisConstant.STORE_BOOKLIST_HASH;
					} else if (caseType.equals("151")) {
						storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_151;
						proxyStoreHashKey = StoreJedisConstant.STORE_BOOKLIST_HASH;
					} else if (caseType.equals("152")) {
						storeSetKey = StoreJedisConstant.STORE_BOOKORDER_SET_152;
						proxyStoreHashKey = StoreJedisConstant.STORE_BOOKLIST_HASH;
					}
					jedis.zrevrange(storeSetKey, 0, -1);
					long end = jedis.zcard(storeSetKey);
					return end;
				} catch (Exception e) {
					LOG.error("getEndRedisIndex :error=>" + e.getMessage() + " :caseType=>" + caseType + " :sort=>" + sort);
				}
				return null;
			}
		});
		return redisIndex;
	}
}
