package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.keel.common.cache.redis.JedisSimpleClient;
import com.keel.common.cache.redis.JedisWorker;
import com.poison.act.client.ActFacade;
import com.poison.act.model.ActPraise;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StoreJedisConstant;
import com.poison.paycenter.domain.repository.PaycenterDomainRepository;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.ext.constant.ResStatisticConstant;
import com.poison.resource.ext.utils.ResRandomUtils;
import com.poison.resource.model.Article;
import com.poison.resource.model.Post;
import com.poison.resource.model.ResStatistic;
import com.poison.resource.service.ResStatisticService;

/**
 * 点赞数，评论数，打赏数，阅读数,随机阅读数，分享数,热度,被搜索数 的 redis 管理器,
 * 
 * @author zhangqi
 * 
 */
public class ResStatJedisManager {

	private static final Log LOG = LogFactory.getLog(ResStatJedisManager.class);
	private static AtomicInteger recStatCountAtomic = new AtomicInteger(0);
	/**
	 * 更新多少条就入库一次
	 */
	private int updatePageSize = 100;

	private static List<Map<String, String>> recStatList = new ArrayList<Map<String, String>>();
	private static Set<String> resStatSet = new HashSet<String>();

	private static String[] types = new String[] { CommentUtils.TYPE_BOOK_COMMENT, CommentUtils.TYPE_MOVIE_COMMENT, CommentUtils.TYPE_ARTICLE, CommentUtils.TYPE_NEWARTICLE, CommentUtils.TYPE_DIARY };

	private JedisSimpleClient resourceVisitClient;
	private ResStatisticService resStatisticService;
	private ActFacade actFacade;
	private PostFacade postFacade;
	private ArticleFacade articleFacade;
	private PaycenterDomainRepository paycenterDomainRepository;

	public JedisSimpleClient getResourceVisitClient() {
		return resourceVisitClient;
	}

	public void setResourceVisitClient(JedisSimpleClient resourceVisitClient) {
		this.resourceVisitClient = resourceVisitClient;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}

	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}

	public void setResStatisticService(ResStatisticService resStatisticService) {
		this.resStatisticService = resStatisticService;
	}

	public void setPaycenterDomainRepository(PaycenterDomainRepository paycenterDomainRepository) {
		this.paycenterDomainRepository = paycenterDomainRepository;
	}

	/**
	 * 获取资源的点赞数
	 */
	public int getActPraiseNum(final long resourceId, final String type, final long resLinkId, final String resLinkType) {
		int count = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return count;
		ResStatistic resStatic = null;
		String num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER);
		if (num == null) {
			try {
				ResStatistic reStat = new ResStatistic();
				reStat.setResId(resourceId);
				reStat.setType(type);
				// reStat.setResLinkId(resLinkId);
				// reStat.setResLinkType(resLinkType);
				resStatic = resStatisticService.findResStatisticById(reStat);
				if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
					count = actFacade.findPraiseCount(null, resourceId);
					reStat.setIsDelete(0);
					reStat.setPraiseNumber(count);
					reStat.setLatestRevisionDate(System.currentTimeMillis());
					resStatisticService.insertResStatistic(reStat);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER);
				} else if (resStatic.getFlag() == ResultUtils.SUCCESS) {
					count = actFacade.findPraiseCount(null, resourceId);
					resStatic.setPraiseNumber(count);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER);
		}
		try {
			count = Integer.parseInt(num);
		} catch (Exception e) {
			LOG.error("data: resourceId=>" + resourceId + " :type=>" + type + " :resLinkId=>" + resLinkId + " :resLinkType=>" + resLinkType);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return count;
	}

	/**
	 * 获取资源的有用数
	 */
	public long getUsefulNum(final long resourceId, final String type, final long resLinkId, final String resLinkType, final long count, final long activityStageId) {
		long usefulNum = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return usefulNum;
		ResStatistic resStatic = null;
		String num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_USEFUL_NUMBER);
		if (num == null) {
			try {
				ResStatistic reStat = new ResStatistic();
				reStat.setResId(resourceId);
				reStat.setType(type);
				resStatic = resStatisticService.findResStatisticById(reStat);
				if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
					reStat.setIsDelete(0);
					reStat.setUsefulNumber(count);
					reStat.setActivityStageId(activityStageId);
					reStat.setLatestRevisionDate(System.currentTimeMillis());
					reStat.setResLinkId(resLinkId);
					reStat.setResLinkType(resLinkType);
					resStatisticService.insertResStatistic(reStat);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_USEFUL_NUMBER);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID);
				} else if (resStatic.getFlag() == ResultUtils.SUCCESS) {
					resStatic.setUsefulNumber(count);
					reStat.setActivityStageId(activityStageId);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_USEFUL_NUMBER);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_USEFUL_NUMBER);
		}
		try {
			usefulNum = Long.parseLong(num);
		} catch (Exception e) {
			LOG.error(":resourceId=>" + resourceId + " :type=>" + type);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return usefulNum;
	}

	/**
	 * 获取资源的无用数
	 */
	public int getNousefulNum(final long resourceId, final String type, final long resLinkId, final String resLinkType) {
		int count = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return count;
		ResStatistic resStatic = null;
		String num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER);
		if (num == null) {
			try {
				ResStatistic reStat = new ResStatistic();
				reStat.setResId(resourceId);
				reStat.setType(type);
				resStatic = resStatisticService.findResStatisticById(reStat);
				if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
					reStat.setIsDelete(0);
					reStat.setNousefulNumber(count);
					reStat.setLatestRevisionDate(System.currentTimeMillis());
					reStat.setResLinkId(resLinkId);
					reStat.setResLinkType(resLinkType);
					resStatisticService.insertResStatistic(reStat);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER);
				} else if (resStatic.getFlag() == ResultUtils.SUCCESS) {
					resStatic.setNousefulNumber(count);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER);
		}
		try {
			count = Integer.parseInt(num);
		} catch (Exception e) {
			LOG.error("data: resourceId=>" + resourceId + " :type=>" + type);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return count;
	}

	/**
	 * 更新资源的点赞数（点赞，取消点赞）
	 */
	public long updateActPraiseNum(final long resourceId, final String type, final ActPraise act, final long resLinkId, final String resLinkType) {
		long praiseCount = 0;
		if (act.getFlag() == ResultUtils.SUCCESS && act.getIsPraise() == 1) {// 点赞
			praiseCount = resourceVisitClient.execute(new JedisWorker<Long>() {
				public Long work(Jedis jedis) {
					String resHashKey = getResStatHashKey(resourceId, type);
					boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER);
					if (exists) {
						recStatCountAtomic.incrementAndGet();
						resStatSet.add(resHashKey);
						long praiseNum = jedis.hincrBy(resHashKey, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER, 1);
						updatePraiseNum(resourceId, type, praiseNum, resLinkId, resLinkType);
						setExprieTime(jedis, resHashKey);
						return praiseNum;
					} else {
						return (long) getActPraiseNum(resourceId, type, resLinkId, resLinkType);
						// return updateActPraiseNum(resourceId, type, act);
					}
				}

			});
		} else if (act.getFlag() == ResultUtils.SUCCESS && act.getIsPraise() == 0) {// 取消点赞
			praiseCount = resourceVisitClient.execute(new JedisWorker<Long>() {
				public Long work(Jedis jedis) {
					String resHashKey = getResStatHashKey(resourceId, type);
					boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER);
					if (exists) {
						recStatCountAtomic.incrementAndGet();
						resStatSet.add(resHashKey);
						long praiseNum = jedis.hincrBy(resHashKey, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER, -1);
						setExprieTime(jedis, resHashKey);
						updatePraiseNum(resourceId, type, praiseNum, resLinkId, resLinkType);
						return praiseNum;
					} else {
						return (long) getActPraiseNum(resourceId, type, resLinkId, resLinkType);
						// return updateActPraiseNum(resourceId, type, act);
					}
				}
			});
		}
		return praiseCount;
	}

	/**
	 * 实时更新点赞数
	 * 
	 * @param resourceId
	 * @param type
	 * @param praiseNum
	 */
	private void updatePraiseNum(final long resourceId, final String type, long praiseNum, final long resLinkId, final String resLinkType) {
		ResStatistic reStat = new ResStatistic();
		reStat.setResId(resourceId);
		reStat.setType(type);
		ResStatistic resStatic = resStatisticService.findResStatisticById(reStat);
		if (resStatic.getFlag() == ResultUtils.SUCCESS) {
			resStatic.setPraiseNumber(praiseNum);
			if (resLinkId != 0) {
				resStatic.setResLinkId(resLinkId);
			}
			if (!StringUtils.isEmpty(resLinkType)) {
				resStatic.setResLinkType(resLinkType);
			}
			resStatisticService.updateResStatistic(resStatic);
		} else if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
			reStat = new ResStatistic();
			reStat.setResId(resourceId);
			reStat.setType(type);
			reStat.setIsDelete(0);
			reStat.setLatestRevisionDate(System.currentTimeMillis());
			reStat.setResLinkId(resLinkId);
			reStat.setResLinkType(resLinkType);
			resStatic.setPraiseNumber(praiseNum);
			resStatisticService.insertResStatistic(reStat);
		}
	}

	/**
	 * 更新资源的有用数
	 */
	public long doNousefulNum(final long resourceId, final String type, final long resLinkId, final String resLinkType) {
		long count = 0;
		count = resourceVisitClient.execute(new JedisWorker<Long>() {
			public Long work(Jedis jedis) {
				String resHashKey = getResStatHashKey(resourceId, type);
				boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER);
				if (exists) {
					recStatCountAtomic.incrementAndGet();
					resStatSet.add(resHashKey);
					long usefulNum = jedis.hincrBy(resHashKey, RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER, 1);
					setExprieTime(jedis, resHashKey);
					updateUserfulNum(resourceId, type, usefulNum, resLinkId, resLinkType, 1, 0);
					return usefulNum;
				} else {
					getNousefulNum(resourceId, type, resLinkId, resLinkType);
					return doNousefulNum(resourceId, type, resLinkId, resLinkType);
				}
			}
		});
		return count;
	}

	/**
	 * 更新资源的无用数
	 */
	public long doUsefulNum(final long resourceId, final String type, final long resLinkId, final String resLinkType, final long count, final long activityStageId) {
		long usefulNum = 0;
		usefulNum = resourceVisitClient.execute(new JedisWorker<Long>() {
			public Long work(Jedis jedis) {
				String resHashKey = getResStatHashKey(resourceId, type);
				boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_USEFUL_NUMBER);
				if (exists) {
					recStatCountAtomic.incrementAndGet();
					resStatSet.add(resHashKey);
					// long usefulNum = jedis.hincrBy(resHashKey,
					// RecStatJedisConstant.RESSTAT_USEFUL_NUMBER, 1);
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_USEFUL_NUMBER, CheckParams.objectToStr(count));
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID, CheckParams.objectToStr(activityStageId));
					setExprieTime(jedis, resHashKey);
					updateUserfulNum(resourceId, type, count, resLinkId, resLinkType, 0, activityStageId);
					return count;
				} else {
					return getUsefulNum(resourceId, type, resLinkId, resLinkType, count, activityStageId);
					// return doUsefulNum(resourceId, type, resLinkId,
					// resLinkType, count, activityStageId);
				}
			}
		});
		return usefulNum;
	}

	/**
	 * 实时更新有用无用数
	 * 
	 * @param resourceId
	 * @param type
	 * @param userfulNum
	 * @param resLinkId
	 * @param resLinkType
	 * @param doUserful
	 *            0:有用数,1:无用数
	 */
	private void updateUserfulNum(final long resourceId, final String type, long userfulNum, final long resLinkId, final String resLinkType, int doUserful, long activityStageId) {
		ResStatistic reStat = new ResStatistic();
		reStat.setResId(resourceId);
		reStat.setType(type);
		ResStatistic resStatic = resStatisticService.findResStatisticById(reStat);
		if (resStatic.getFlag() == ResultUtils.SUCCESS) {
			if (doUserful == 0) {
				resStatic.setUsefulNumber(userfulNum);
				resStatic.setActivityStageId(activityStageId);
			} else if (doUserful == 1) {
				resStatic.setNousefulNumber(userfulNum);
			}
			// resStatic.setResLinkId(resLinkId);
			// resStatic.setResLinkType(resLinkType);

			if (resLinkId != 0) {
				resStatic.setResLinkId(resLinkId);
			}
			if (!StringUtils.isEmpty(resLinkType)) {
				resStatic.setResLinkType(resLinkType);
			}
			resStatisticService.updateResStatistic(resStatic);
		} else if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
			reStat = new ResStatistic();
			reStat.setResId(resourceId);
			reStat.setType(type);
			if (doUserful == 0) {
				reStat.setUsefulNumber(userfulNum);
				resStatic.setActivityStageId(activityStageId);
			} else if (doUserful == 1) {
				reStat.setNousefulNumber(userfulNum);
			}
			reStat.setIsDelete(0);
			reStat.setLatestRevisionDate(System.currentTimeMillis());
			reStat.setResLinkId(resLinkId);
			reStat.setResLinkType(resLinkType);
			resStatisticService.insertResStatistic(reStat);
		}
	}

	/**
	 * 实时更新热度数
	 * 
	 * @param resourceId
	 * @param type
	 * @param heatNum
	 * @param resLinkId
	 * @param resLinkType
	 */
	private void updateHeatNum(final long resourceId, final String type, long heatNum, final long resLinkId, final String resLinkType, final long activityStageId) {
		ResStatistic reStat = new ResStatistic();
		reStat.setResId(resourceId);
		reStat.setType(type);
		ResStatistic resStatic = resStatisticService.findResStatisticById(reStat);
		if (resStatic.getFlag() == ResultUtils.SUCCESS) {
			resStatic.setHeatNumber(heatNum);
			resStatic.setActivityStageId(activityStageId);
			// resStatic.setResLinkId(resLinkId);
			// resStatic.setResLinkType(resLinkType);

			if (resLinkId != 0) {
				resStatic.setResLinkId(resLinkId);
			}
			if (!StringUtils.isEmpty(resLinkType)) {
				resStatic.setResLinkType(resLinkType);
			}
			resStatisticService.updateResStatistic(resStatic);
		} else if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
			reStat = new ResStatistic();
			reStat.setResId(resourceId);
			reStat.setType(type);
			reStat.setHeatNumber(heatNum);
			reStat.setIsDelete(0);
			reStat.setLatestRevisionDate(System.currentTimeMillis());
			reStat.setResLinkId(resLinkId);
			reStat.setResLinkType(resLinkType);
			reStat.setActivityStageId(activityStageId);
			resStatisticService.insertResStatistic(reStat);
		}
	}

	/**
	 * 更新资源的执度计算
	 */
	public long doHeatNum(final long resourceId, final String type, final long resLinkId, final String resLinkType, final long count, final long activityStageId) {
		long heatNum = 0;
		try {
			heatNum = resourceVisitClient.execute(new JedisWorker<Long>() {
				public Long work(Jedis jedis) {
					String resHashKey = getResStatHashKey(resourceId, type);
					boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_HEAT_NUMBER);
					if (exists) {
						recStatCountAtomic.incrementAndGet();
						resStatSet.add(resHashKey);
						jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_HEAT_NUMBER, CheckParams.objectToStr(count));
						jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID, CheckParams.objectToStr(activityStageId));
						setExprieTime(jedis, resHashKey);
						updateHeatNum(resourceId, type, count, resLinkId, resLinkType, activityStageId);
						return count;
					} else {
						// return getHeatNum(resourceId, type, resLinkId,
						// resLinkType, count, activityStageId);
						// return doHeatNum(resourceId, type, resLinkId,
						// resLinkType, count, activityStageId);
						updateHeatNum(resourceId, type, count, resLinkId, resLinkType, activityStageId);
						return count;
					}
				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			updateHeatNum(resourceId, type, count, resLinkId, resLinkType, activityStageId);
			return count;
		}
		return heatNum;
	}

	/**
	 * 获取资源的热度
	 */
	public long getHeatNum(final long resourceId, final String type, final long resLinkId, final String resLinkType, final long count, final long activityStageId) {
		long heatNum = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return heatNum;
		ResStatistic resStatic = null;
		String num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_HEAT_NUMBER);
		if (num == null) {
			try {
				ResStatistic reStat = new ResStatistic();
				reStat.setResId(resourceId);
				reStat.setType(type);
				resStatic = resStatisticService.findResStatisticById(reStat);
				if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
					reStat.setIsDelete(0);
					reStat.setHeatNumber(count);
					resStatic.setActivityStageId(activityStageId);
					reStat.setLatestRevisionDate(System.currentTimeMillis());
					reStat.setResLinkId(resLinkId);
					reStat.setResLinkType(resLinkType);
					resStatisticService.insertResStatistic(reStat);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_HEAT_NUMBER);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID);
				} else if (resStatic.getFlag() == ResultUtils.SUCCESS) {
					resStatic.setHeatNumber(count);
					resStatic.setActivityStageId(activityStageId);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_HEAT_NUMBER);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_HEAT_NUMBER);
		}
		try {
			heatNum = Long.parseLong(num);
		} catch (Exception e) {
			LOG.error(":resourceId=>" + resourceId + " :type=>" + type);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return heatNum;
	}

	/**
	 * redis 同步数据库
	 * 
	 * @param resStatistic
	 * @param type
	 * @param field
	 */
	public void updateResStatistic() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Set<String> deleteSet = new HashSet<String>();
				Set<ResStatistic> updateSet = new HashSet<ResStatistic>();
				for (String str : resStatSet) {
					try {
						Map<String, String> resStatMap = getResStatistic(str);
						if (resStatMap.size() > 0) {
							try {
								String type = resStatMap.get(RecStatJedisConstant.RESSTAT_TYPE);
								long resourceId = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_RES_ID));
								long systemTime = System.currentTimeMillis();

								long updateTime = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_LATEST_REVISION_DATE));
								long praiseNumber = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_PRAISE_NUMBER));
								long readNumber = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_READ_NUMBER));
								long readRandomNumber = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER));
								long commentNumber = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_COMMENT_NUMBER));
								long rewardNumber = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_REWARD_NUMBER));
								long usefulNumber = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_USEFUL_NUMBER));
								long nousefulNumber = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER));
								long activityStageId = CheckParams.stringToLong(resStatMap.get(RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID));

								ResStatistic reStat = new ResStatistic();
								reStat.setResId(resourceId);
								reStat.setType(type);
								ResStatistic resStatic = resStatisticService.findResStatisticById(reStat);
								if (resStatic.getFlag() == ResultUtils.SUCCESS) {
									if (systemTime - Long.valueOf(updateTime) >= ResStatisticConstant.STATISTIC_TIME_INTERVALS) {
										boolean flag = false;
										long praiseNumberDb = resStatic.getPraiseNumber();
										long readNumberDb = resStatic.getReadNumber();
										long readRandomNumberDb = resStatic.getReadRandomNumber();
										long commentNumberDb = resStatic.getCommentNumber();
										long rewardNumberDb = resStatic.getRewardNumber();
										long usefulNumberDb = resStatic.getUsefulNumber();
										long nousefulNumberDb = resStatic.getNousefulNumber();
										long activityStageIdDb = resStatic.getActivityStageId();

										if (praiseNumberDb != praiseNumber) {
											resStatic.setPraiseNumber(praiseNumber);
											flag = true;
										}
										if (readNumberDb != readNumber) {
											resStatic.setReadNumber(readNumber);
											flag = true;
										}
										if (readRandomNumberDb != readRandomNumber) {
											resStatic.setReadRandomNumber(readRandomNumber);
											flag = true;
										}
										if (commentNumberDb != commentNumber) {
											resStatic.setCommentNumber(commentNumber);
											flag = true;
										}
										if (rewardNumberDb != rewardNumber) {
											resStatic.setRewardNumber(rewardNumber);
											flag = true;
										}
										if (usefulNumberDb != usefulNumber) {
											resStatic.setUsefulNumber(usefulNumber);
											flag = true;
										}
										if (nousefulNumberDb != nousefulNumber) {
											resStatic.setNousefulNumber(nousefulNumber);
											flag = true;
										}
										if (activityStageIdDb != activityStageId) {
											resStatic.setActivityStageId(activityStageId);
											flag = true;
										}
										if (flag) {
											resStatic.setLatestRevisionDate(systemTime);
											updateSet.add(resStatic);
										}
									}
								} else {
									deleteSet.add(str);
								}
							} catch (Exception e) {
								LOG.error(e.getMessage(), e.fillInStackTrace());
								continue;
							}
						}
					} catch (Exception e) {
						LOG.error(e.getMessage(), e.fillInStackTrace());
					}
				}
				for (ResStatistic str : updateSet) {
					int i = resStatisticService.updateResStatistic(str);
					if (i == ResultUtils.SUCCESS) {
						String key = getResStatHashKey(str.getResId(), str.getType());
						setResStatistic(key, RecStatJedisConstant.RESSTAT_LATEST_REVISION_DATE, String.valueOf((System.currentTimeMillis())));
						deleteSet.add(key);
					}
				}
				resStatSet.removeAll(deleteSet);
			}
		});
		thread.start();
	}

	/**
	 * 初始化时使用，并阻止重复添加，数据错误
	 * 
	 * @param resStatistic
	 * @param type
	 * @param field
	 * @return
	 */
	public Map<String, String> setResStatistic(final ResStatistic resStatistic, final String type, final String field) {
		if (null == resStatistic) {
			return new HashMap<String, String>();
		}
		final Map<String, String> movieMap = resourceVisitClient.execute(new JedisWorker<Map<String, String>>() {
			final String resHashKey = getResStatHashKey(resStatistic.getResId(), type);

			@Override
			public Map<String, String> work(Jedis jedis) {
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_ID)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_ID, CheckParams.objectToStr(resStatistic.getId()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_TYPE)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_TYPE, CheckParams.objectToStr(resStatistic.getType()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_RES_ID)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_RES_ID, CheckParams.objectToStr(resStatistic.getResId()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_IS_DELETE)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_IS_DELETE, CheckParams.objectToStr(resStatistic.getIsDelete()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_LATEST_REVISION_DATE)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_LATEST_REVISION_DATE, CheckParams.objectToStr(resStatistic.getLatestRevisionDate()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_CREATE_TIME)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_CREATE_TIME, CheckParams.objectToStr(System.currentTimeMillis()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.ResStat_False_Visit)) {
					jedis.hset(resHashKey, RecStatJedisConstant.ResStat_False_Visit, CheckParams.objectToStr(resStatistic.getFalseVisit()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_VISIT_NUMBER)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_VISIT_NUMBER, CheckParams.objectToStr(resStatistic.getVisitNumber()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER_TIME)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER_TIME, CheckParams.objectToStr(resStatistic.getLatestRevisionDate()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER)) {
					// jedis.hset(resHashKey,
					// RecStatJedisConstant.RESSTAT_PRAISE_NUMBER,
					// CheckParams.objectToStr(resStatistic.getPraiseNumber()));
					int praiseCount = actFacade.findPraiseCount(null, resStatistic.getResId());
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_PRAISE_NUMBER, CheckParams.objectToStr(praiseCount));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_COMMENT_NUMBER)) {
					// jedis.hset(resHashKey,
					// RecStatJedisConstant.RESSTAT_COMMENT_NUMBER,
					// CheckParams.objectToStr(resStatistic.getCommentNumber()));
					int commentNumber = actFacade.findCommentCount(null, resStatistic.getResId());
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_COMMENT_NUMBER, CheckParams.objectToStr(commentNumber));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_REWARD_NUMBER)) {
					int sourceType = Integer.parseInt(type);
					// 只查询成功的打赏次数
					int count = paycenterDomainRepository.selectRewardTypeCount(resStatistic.getResId(), 1, sourceType);
					if (count == -1) {
						jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_REWARD_NUMBER, CheckParams.objectToStr(resStatistic.getRewardNumber()));
					} else {
						jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_REWARD_NUMBER, CheckParams.objectToStr(count));
					}
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER)) {
					if (StringUtils.equals(type, CommentUtils.TYPE_ARTICLE)) {
						Post post = postFacade.queryByIdName(resStatistic.getResId());
						if (post.getFlag() == ResultUtils.SUCCESS) {
							resStatistic.setReadNumber(post.getReadingCount());
							jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER, CheckParams.objectToStr(post.getReadingCount()));
						} else {
							jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER, CheckParams.objectToStr(resStatistic.getReadNumber()));
						}
					} else if (StringUtils.equals(type, CommentUtils.TYPE_NEWARTICLE)) {
						Article article = articleFacade.queryArticleById(resStatistic.getResId());
						if (article.getFlag() == ResultUtils.SUCCESS) {
							resStatistic.setReadNumber(article.getReadingCount());
							jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER, CheckParams.objectToStr(article.getReadingCount()));
						} else {
							jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER, CheckParams.objectToStr(resStatistic.getReadNumber()));
						}
					} else {
						jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER, CheckParams.objectToStr(resStatistic.getReadNumber()));
					}
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER)) {
					int randomNumber = ResRandomUtils.RandomIntWithChance();
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER, CheckParams.objectToStr(randomNumber));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_USEFUL_NUMBER)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_USEFUL_NUMBER, CheckParams.objectToStr(resStatistic.getUsefulNumber()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_NOUSEFUL_NUMBER, CheckParams.objectToStr(resStatistic.getNousefulNumber()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_ACTIVITY_STAGE_ID, CheckParams.objectToStr(resStatistic.getActivityStageId()));
				}
				if (!jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_HEAT_NUMBER)) {
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_HEAT_NUMBER, CheckParams.objectToStr(resStatistic.getHeatNumber()));
				}
				// 新增 redis 有效期验证
				if (jedis.ttl(resHashKey) == -1) {
					jedis.expire(resHashKey, StoreJedisConstant.expireSeconds);
				}
				return jedis.hgetAll(resHashKey);
			}
		});
		return movieMap;
	}

	/**
	 * 获取hashMap 所有属性
	 * 
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public Map<String, String> getResStatistic(final long resourceId, final String type) {
		Map<String, String> resStatMap = new HashMap<String, String>();
		try {
			final String key = getResStatHashKey(resourceId, type);
			resStatMap = getResStatistic(key);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return resStatMap;
	}

	/**
	 * 获取hashMap 所有属性
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> getResStatistic(final String key) {
		Map<String, String> resStatMap = new HashMap<String, String>();
		try {
			resStatMap = resourceVisitClient.execute(new JedisWorker<Map<String, String>>() {
				public Map<String, String> work(Jedis jedis) {
					return jedis.hgetAll(key);
				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return resStatMap;
	}

	/**
	 * 获取资源统计数量
	 * 
	 * @param resourceId
	 * @param type
	 * @param field
	 * @return
	 */
	public String getResStatistic(final long resourceId, final String type, final String field) {
		String value = null;
		try {
			final String key = getResStatHashKey(resourceId, type);
			value = resourceVisitClient.execute(new JedisWorker<String>() {
				public String work(Jedis jedis) {
					return jedis.hget(key, field);
				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return value;
	}

	public String setResStatistic(final String key, final String field, final String fieldValue) {
		String value = "";
		try {
			value = resourceVisitClient.execute(new JedisWorker<String>() {
				public String work(Jedis jedis) {
					if (jedis.hexists(key, field)) {
						jedis.hset(key, field, fieldValue);
					}
					setExprieTime(jedis, key);
					return jedis.hget(key, field);
				}
			});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return value;
	}

	/**
	 * 获取打赏次数
	 */
	public long getRewarDetailsNum(final long resourceId, final String type) {
		int count = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return count;
		ResStatistic resStatic = null;
		String num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_REWARD_NUMBER);
		if (num == null) {
			try {
				ResStatistic reStat = new ResStatistic();
				reStat.setResId(resourceId);
				reStat.setType(type);
				resStatic = resStatisticService.findResStatisticById(reStat);
				int sourceType = Integer.parseInt(type);
				if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
					count = paycenterDomainRepository.selectRewardTypeCount(resourceId, 1, sourceType);
					reStat.setIsDelete(0);
					reStat.setRewardNumber(count);
					reStat.setLatestRevisionDate(System.currentTimeMillis());
					reStat.setResLinkId(0);
					reStat.setResLinkType("");
					resStatisticService.insertResStatistic(reStat);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_REWARD_NUMBER);
				} else if (resStatic.getFlag() == ResultUtils.SUCCESS) {
					count = paycenterDomainRepository.selectRewardTypeCount(resourceId, 1, sourceType);
					resStatic.setRewardNumber(count);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_REWARD_NUMBER);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_REWARD_NUMBER);
		}
		try {
			count = Integer.parseInt(num);
		} catch (Exception e) {
			LOG.error("data: resourceId=>" + resourceId + " :type=>" + type);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return count;
	}

	/**
	 * 更新资源的打赏次数
	 */
	public long addRewardDetailNum(final long resourceId, final String type) {
		long count = 0;
		count = resourceVisitClient.execute(new JedisWorker<Long>() {
			public Long work(Jedis jedis) {
				String resHashKey = getResStatHashKey(resourceId, type);
				boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_REWARD_NUMBER);
				if (exists) {
					recStatCountAtomic.incrementAndGet();
					resStatSet.add(resHashKey);
					setExprieTime(jedis, resHashKey);
					return jedis.hincrBy(resHashKey, RecStatJedisConstant.RESSTAT_REWARD_NUMBER, 1);
				} else {
					getRewarDetailsNum(resourceId, type);
					return addRewardDetailNum(resourceId, type);
				}
			}
		});
		if (recStatCountAtomic.get() % updatePageSize == 0) {
			updateResStatistic();
		}
		return count;
	}

	/**
	 * 评论数
	 */
	public long getActCommentNum(final long resourceId, final String type) {
		int count = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return count;
		ResStatistic resStatic = null;
		String num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_COMMENT_NUMBER);
		if (num == null) {
			try {
				ResStatistic reStat = new ResStatistic();
				reStat.setResId(resourceId);
				reStat.setType(type);
				resStatic = resStatisticService.findResStatisticById(reStat);
				if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
					count = actFacade.findCommentCount(null, resourceId);
					reStat.setIsDelete(0);
					reStat.setCommentNumber(count);
					reStat.setLatestRevisionDate(System.currentTimeMillis());
					reStat.setResLinkId(0);
					reStat.setResLinkType("");
					resStatisticService.insertResStatistic(reStat);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_COMMENT_NUMBER);
				} else if (resStatic.getFlag() == ResultUtils.SUCCESS) {
					count = actFacade.findCommentCount(null, resourceId);
					resStatic.setCommentNumber(count);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_COMMENT_NUMBER);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_COMMENT_NUMBER);
		}
		try {
			count = Integer.parseInt(num);
		} catch (Exception e) {
			LOG.error("data: resourceId=>" + resourceId + " :type=>" + type);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return count;
	}

	/**
	 * 增加评论次数
	 */
	public long addActCommentNum(final long resourceId, final String type) {
		long count = 0;
		count = resourceVisitClient.execute(new JedisWorker<Long>() {
			public Long work(Jedis jedis) {
				String resHashKey = getResStatHashKey(resourceId, type);
				boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_COMMENT_NUMBER);
				if (exists) {
					recStatCountAtomic.incrementAndGet();
					resStatSet.add(resHashKey);
					setExprieTime(jedis, resHashKey);
					return jedis.hincrBy(resHashKey, RecStatJedisConstant.RESSTAT_COMMENT_NUMBER, 1);
				} else {
					getActCommentNum(resourceId, type);
					return addActCommentNum(resourceId, type);
				}
			}
		});
		if (recStatCountAtomic.get() % updatePageSize == 0) {
			updateResStatistic();
		}
		return count;
	}

	/**
	 * 更新阅读数
	 * 
	 * @param resourceId
	 * @param type
	 * @param manito
	 *            是否是大神查询 0 不是，1 是
	 */
	public int getReadNum(final long resourceId, final String type) {
		int count = 0;
		long readRandomNumber = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return count;
		ResStatistic resStatic = null;
		// if (!isResStatType(type))
		// return count;
		String num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_READ_NUMBER);
		if (num == null) {
			try {
				ResStatistic reStat = new ResStatistic();
				reStat.setResId(resourceId);
				if (!StringUtils.isEmpty(type))
					reStat.setType(type);
				resStatic = resStatisticService.findResStatisticById(reStat);
				if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
					count = actFacade.findPraiseCount(null, resourceId);
					if (StringUtils.equals(type, CommentUtils.TYPE_ARTICLE)) {
						Post post = postFacade.queryByIdName(resourceId);
						if (post.getFlag() == ResultUtils.SUCCESS) {
							count = post.getReadingCount();
						}
					} else if (StringUtils.equals(type, CommentUtils.TYPE_NEWARTICLE)) {
						Article article = articleFacade.queryArticleById(resourceId);
						if (article.getFlag() == ResultUtils.SUCCESS) {
							count = article.getReadingCount();
						}
					}
					readRandomNumber = ResRandomUtils.RandomIntWithChance();
					reStat.setIsDelete(0);
					reStat.setReadNumber(count);
					reStat.setReadRandomNumber(count + readRandomNumber);
					reStat.setResLinkId(0);
					reStat.setResLinkType("");
					reStat.setLatestRevisionDate(System.currentTimeMillis());
					resStatisticService.insertResStatistic(reStat);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_READ_NUMBER);
				} else if (resStatic.getFlag() == ResultUtils.SUCCESS) {
					count = actFacade.findPraiseCount(null, resourceId);
					if (StringUtils.equals(type, CommentUtils.TYPE_ARTICLE)) {
						Post post = postFacade.queryByIdName(resourceId);
						if (post.getFlag() == ResultUtils.SUCCESS) {
							count = post.getReadingCount();
						}
					} else if (StringUtils.equals(type, CommentUtils.TYPE_NEWARTICLE)) {
						Article article = articleFacade.queryArticleById(resourceId);
						if (article.getFlag() == ResultUtils.SUCCESS) {
							count = article.getReadingCount();
						}
					}
					readRandomNumber = ResRandomUtils.RandomIntWithChance();
					resStatic.setReadRandomNumber(count + readRandomNumber);
					resStatic.setReadNumber(count);
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_READ_NUMBER);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_READ_NUMBER);
		}

		try {
			readRandomNumber = getReadRandomNumber(resourceId, type);
			count = Integer.parseInt(num);
		} catch (Exception e) {
			LOG.error("data: resourceId=>" + resourceId + " :type=>" + type);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return count + (int) readRandomNumber;
	}

	/**
	 * 更新阅读数
	 */
	private int getReadNum(final long resourceId, final String type, final long resLinkId, final String resLinkType) {
		int count = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return count;
		long readRandomNumber = 0;
		ResStatistic resStatic = null;
		// if (!isResStatType(type))
		// return count;
		String num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_READ_NUMBER);
		if (num == null) {
			try {
				ResStatistic reStat = new ResStatistic();
				reStat.setResId(resourceId);
				// reStat.setResLinkId(resLinkId);
				// reStat.setResLinkType(resLinkType);
				if (!StringUtils.isEmpty(type))
					reStat.setType(type);
				resStatic = resStatisticService.findResStatisticById(reStat);
				if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
					count = actFacade.findPraiseCount(null, resourceId);
					if (StringUtils.equals(type, CommentUtils.TYPE_ARTICLE)) {
						Post post = postFacade.queryByIdName(resourceId);
						if (post.getFlag() == ResultUtils.SUCCESS) {
							count = post.getReadingCount();
						}
					} else if (StringUtils.equals(type, CommentUtils.TYPE_NEWARTICLE)) {
						Article article = articleFacade.queryArticleById(resourceId);
						if (article.getFlag() == ResultUtils.SUCCESS) {
							count = article.getReadingCount();
						}
					}
					readRandomNumber = ResRandomUtils.RandomIntWithChance();
					reStat.setReadRandomNumber(readRandomNumber);
					reStat.setIsDelete(0);
					reStat.setReadNumber(count);
					reStat.setLatestRevisionDate(System.currentTimeMillis());
					reStat.setResLinkId(resLinkId);
					reStat.setResLinkType(resLinkType);
					resStatisticService.insertResStatistic(reStat);
					setResStatistic(reStat, type, RecStatJedisConstant.RESSTAT_READ_NUMBER);
				} else if (resStatic.getFlag() == ResultUtils.SUCCESS) {
					if (resLinkId != resStatic.getResLinkId() || !resLinkType.equals(resStatic.getResLinkType())) {
						try {
							count = actFacade.findPraiseCount(null, resourceId);
							if (StringUtils.equals(type, CommentUtils.TYPE_ARTICLE)) {
								Post post = postFacade.queryByIdName(resourceId);
								if (post.getFlag() == ResultUtils.SUCCESS) {
									count = post.getReadingCount();
								}
							} else if (StringUtils.equals(type, CommentUtils.TYPE_NEWARTICLE)) {
								Article article = articleFacade.queryArticleById(resourceId);
								if (article.getFlag() == ResultUtils.SUCCESS) {
									count = article.getReadingCount();
								}
							}
							readRandomNumber = ResRandomUtils.RandomIntWithChance();
							resStatic.setReadRandomNumber(readRandomNumber);
							resStatic.setReadNumber(count);
							resStatic.setResLinkId(resLinkId);
							resStatic.setResLinkType(resLinkType);
							resStatisticService.updateResStatistic(resStatic);
						} catch (Exception e) {
							LOG.error(e.getMessage(), e.fillInStackTrace());
						}
					}
					setResStatistic(resStatic, type, RecStatJedisConstant.RESSTAT_READ_NUMBER);
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
			num = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_READ_NUMBER);
		}
		try {
			readRandomNumber = getReadRandomNumber(resourceId, type);
			count = Integer.parseInt(num);
		} catch (Exception e) {
			LOG.error("data: resourceId=>" + resourceId + " :type=>" + type + " :resLinkId=>" + resLinkId + " :resLinkType=>" + resLinkType);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return count + (int) readRandomNumber;
	}

	/**
	 * 查询阅读随机数
	 * 
	 * @param resourceId
	 * @param type
	 * @param resLinkId
	 * @param resLinkType
	 * @return
	 */
	private long getReadRandomNumber(final long resourceId, final String type) {
		long readRandomNumber = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return readRandomNumber;
		try {
			String readRandomNumberStr = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER);
			if (!StringUtils.isEmpty(readRandomNumberStr)) {
				try {
					readRandomNumber = Integer.parseInt(readRandomNumberStr);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
			} else {
				readRandomNumber = resourceVisitClient.execute(new JedisWorker<Long>() {
					public Long work(Jedis jedis) {
						String resHashKey = getResStatHashKey(resourceId, type);
						int randomNumber = ResRandomUtils.RandomIntWithChance();
						jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER, CheckParams.objectToStr(randomNumber));
						return (long) randomNumber;
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return readRandomNumber;
	}
	/**
	 * 新增阅读数
	 */
	public long addReadNum(final long resourceId, final String type, final long resLinkId, final String resLinkType, final int manito) {
		int random = 0;
		int manitoProxy = 1;
		if (manito == 1) {
			manitoProxy = 100;
			random = 1;
		}
		int randomNumber = ResRandomUtils.RandomIntWithChance(random, manitoProxy);
		return addReadNum(resourceId, type, resLinkId, resLinkType, manito, randomNumber);
	}
	/**
	 * 新增阅读数
	 */
	public long addReadNum(final long resourceId, final String type, final long resLinkId, final String resLinkType, final int manito,final long randomNumber) {
		long count = 0;
		// if (!isResStatType(type))
		// return count;
		count = resourceVisitClient.execute(new JedisWorker<Long>() {
			public Long work(Jedis jedis) {
				String resHashKey = getResStatHashKey(resourceId, type);
				boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER);
				if (exists) {
					recStatCountAtomic.incrementAndGet();
					resStatSet.add(resHashKey);
					setExprieTime(jedis, resHashKey);
					long readNumber = jedis.hincrBy(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER, 1);
					return addReadRandomNumber(resourceId, type, manito, jedis, resHashKey, readNumber);
				} else {
					long readNumber = (long) getReadNum(resourceId, type, resLinkId, resLinkType);
					return addReadRandomNumber(resourceId, type, manito, jedis, resHashKey, readNumber);
				}
			}

			private Long addReadRandomNumber(final long resourceId, final String type, final int manito, Jedis jedis, String resHashKey, long readNumber) {
				long readRandomNumber = 0;
				//int random = 0;
				//int manitoProxy = 1;
				//if (manito == 1) {
					//manitoProxy = 30;
					//random = 1;
				//}
				//int randomNumber = ResRandomUtils.RandomIntWithChance(random, manitoProxy);
				String oldReadRandomNumber = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER);
				if (StringUtils.isEmpty(oldReadRandomNumber)) {
					readRandomNumber = randomNumber;
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER, CheckParams.objectToStr(randomNumber));
				} else {
					readRandomNumber = Integer.parseInt(oldReadRandomNumber);
					readRandomNumber += randomNumber;
					jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER, CheckParams.objectToStr(readRandomNumber));
				}
				updateReadNum(resourceId, type, readRandomNumber, resLinkId, resLinkType, readNumber);
				return readNumber + readRandomNumber;
			}
		});
		// if (recStatCountAtomic.get() % updatePageSize == 0) {
		// updateResStatistic();
		// }
		return count;
	}
	
	/**
	 * 判断是否替换阅读数
	 */
	public long setReadNum(final long resourceId, final String type, final long resLinkId, final String resLinkType, final int manito,final long readnum,final long falsereadnum) {
		long count = 0;
		// if (!isResStatType(type))
		// return count;
		count = resourceVisitClient.execute(new JedisWorker<Long>() {
			public Long work(Jedis jedis) {
				String resHashKey = getResStatHashKey(resourceId, type);
				boolean exists = jedis.hexists(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER);
				if (exists) {
					recStatCountAtomic.incrementAndGet();
					resStatSet.add(resHashKey);
					setExprieTime(jedis, resHashKey);
					long readNumber = jedis.hincrBy(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER, 1);
					return addReadRandomNumber(resourceId, type, manito, jedis, resHashKey, readNumber);
				} else {
					long readNumber = (long) getReadNum(resourceId, type, resLinkId, resLinkType);
					return addReadRandomNumber(resourceId, type, manito, jedis, resHashKey, readNumber);
				}
			}

			private Long addReadRandomNumber(final long resourceId, final String type, final int manito, Jedis jedis, String resHashKey, long readNumber) {
				long readRandomNumber = 0;
				//int random = 0;
				//int manitoProxy = 1;
				//if (manito == 1) {
					//manitoProxy = 30;
					//random = 1;
				//}
				//int randomNumber = ResRandomUtils.RandomIntWithChance(random, manitoProxy);
				String oldReadRandomNumberStr = getResStatistic(resourceId, type, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER);
				if(com.poison.eagle.utils.StringUtils.isInteger(oldReadRandomNumberStr)){
					readRandomNumber = Long.valueOf(oldReadRandomNumberStr);
				}
				if(readnum>readNumber || falsereadnum>readRandomNumber){
					if(readnum>readNumber){
						jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_NUMBER, readnum+"");
						readNumber = readnum;
					}
					if(falsereadnum>readRandomNumber){
						jedis.hset(resHashKey, RecStatJedisConstant.RESSTAT_READ_RANDOM_NUMBER, falsereadnum+"");
						readRandomNumber = falsereadnum;
					}
					updateReadNum(resourceId, type, readRandomNumber, resLinkId, resLinkType, readNumber);
				}
				return readNumber + readRandomNumber;
			}
		});
		// if (recStatCountAtomic.get() % updatePageSize == 0) {
		// updateResStatistic();
		// }
		return count;
	}

	/**
	 * 实时更新有用无用数
	 * 
	 * @param resourceId
	 * @param type
	 * @param readRandomNumber
	 * @param resLinkId
	 * @param resLinkType
	 * @param readNumber
	 */
	private void updateReadNum(final long resourceId, final String type, long readRandomNumber, final long resLinkId, final String resLinkType, long readNumber) {
		try {
			ResStatistic reStat = new ResStatistic();
			reStat.setResId(resourceId);
			reStat.setType(type);
			ResStatistic resStatic = resStatisticService.findResStatisticById(reStat);
			if (resStatic.getFlag() == ResultUtils.SUCCESS) {

				// resStatic.setResLinkId(resLinkId);
				// resStatic.setResLinkType(resLinkType);
				resStatic.setReadNumber(readNumber);
				resStatic.setReadRandomNumber(readRandomNumber);

				if (resLinkId != 0) {
					resStatic.setResLinkId(resLinkId);
				}
				if (!StringUtils.isEmpty(resLinkType)) {
					resStatic.setResLinkType(resLinkType);
				}
				resStatisticService.updateResStatistic(resStatic);
			} else if (resStatic.getFlag() == ResultUtils.DATAISNULL) {
				reStat = new ResStatistic();
				reStat.setResId(resourceId);
				reStat.setType(type);
				resStatic.setReadNumber(readNumber);
				resStatic.setReadRandomNumber(readRandomNumber);
				reStat.setIsDelete(0);
				reStat.setLatestRevisionDate(System.currentTimeMillis());
				reStat.setResLinkId(resLinkId);
				reStat.setResLinkType(resLinkType);
				resStatisticService.insertResStatistic(reStat);
			}
		} catch (Exception e) {
			LOG.error("data: resourceId=>" + resourceId + " :type=>" + type + " :readRandomNumber=>" + readRandomNumber + " :readNumber=>" + readNumber);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
	}

	/**
	 * 获取总的搜索数
	 * 
	 * @param resourceId
	 * @param type
	 */
	public int getTotalSearchNum(final long resourceId, final String type) {
		int count = 0;
		if (resourceId == 0 || StringUtils.isEmpty(type))
			return count;
		ResStatistic resStatic = null;
		// if (!isResStatType(type))
		// return count;
		try {
			ResStatistic reStat = new ResStatistic();
			reStat.setResId(resourceId);
			if (!StringUtils.isEmpty(type))
				reStat.setType(type);
			resStatic = resStatisticService.findResStatisticById(reStat);
			if (resStatic.getFlag() == ResultUtils.SUCCESS) {
				count = (int) resStatic.getTotalSearchNumber();
				// LOG.info("==========查询数量信息的id为："+resStatic.getId()+" 数量为:"+count);
			} else {
				// LOG.error("==============查询数量信息出错了，错误代码:"+resStatic.getFlag());
			}
		} catch (Exception e) {
			// LOG.error("============查询数量信息异常,data: resourceId=>" + resourceId
			// + " :type=>" + type);
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}

		return count;
	}

	/**
	 * 获取 ResStat hashKey
	 * 
	 * @param resourceId
	 * @param type
	 * @return
	 */
	public String getResStatHashKey(long resourceId, String type) {
		String key = "";
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("#ResStat_Hash_ResId_");
			sb.append(resourceId);
			sb.append("_Type_");
			sb.append(type);
			key = sb.toString();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		return key;
	}

	public static boolean isResStatType(String type) {
		boolean flag = false;
		try {
			for (String str : types) {
				if (StringUtils.equals(str, type)) {
					return flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 设置 redis key 有效期
	 * 
	 * @param jedis
	 * @param key
	 */
	private void setExprieTime(Jedis jedis, String key) {
		if (jedis.ttl(key) == -1) {
			try {
				jedis.expire(key, StoreJedisConstant.expireSeconds);
			} catch (Exception e) {
				LOG.error(":error=>" + e.getMessage() + " :po=>" + key);
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
		}
	}

	/**
	 * 更新点赞数
	 */
	private void updateTestNum(final long id) {
		resourceVisitClient.execute(new JedisWorker<Object>() {
			@Override
			public Object work(Jedis jedis) {
				String key = ResStatisticConstant.ARTICLE_STATISTIC_MARK + id + ResStatisticConstant.ARTICLE_STATISTIC_TYPE;
				String beforeDate = jedis.hget(key, ResStatisticConstant.RESOURCE_STATISTIC_DATE);
				// 当没有数据时，置0
				if (null == beforeDate || "".equals(beforeDate)) {
					beforeDate = "0";
				}
				long sysdate = System.currentTimeMillis();
				jedis.hset(key, ResStatisticConstant.RESOURCE_STATISTIC_DATE, sysdate + "");
				long falseVisit = jedis.hincrBy(key, ResStatisticConstant.STATISTIC_FALSE_VISIT, ResRandomUtils.RandomInt());
				long visitNumber = jedis.hincrBy(key, ResStatisticConstant.RESOURCE_STATISTIC_VISIT, 1);
				// 当大于等于十分钟时，更新数据库
				if (sysdate - Long.valueOf(beforeDate) >= ResStatisticConstant.STATISTIC_TIME_INTERVALS) {
					ResStatistic resStatistic = new ResStatistic();
					resStatistic.setResId(id);
					resStatistic.setType(CommentUtils.TYPE_POST);
					resStatistic.setFalseVisit(falseVisit);
					resStatistic.setVisitNumber(visitNumber);
					resStatistic.setLatestRevisionDate(sysdate);
					resStatisticService.insertResStatistic(resStatistic);
				}
				return jedis.hgetAll(key);
			}
		});
	}
}
