package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.act.client.ActFacade;
import com.poison.act.model.ActPraise;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.ResourceGroupInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.DateUtil;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.StoreJedisConstant;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.client.ResourceLinkFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
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
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;

/**
 * app 获取电影类型为 170，171，172,影单，图书的查询方式
 * 
 * @author :zhangqi
 * @time:2015-5-26下午12:31:28
 * @return
 */
public class MvBkManager extends BaseManager {

	private static final Log LOG = LogFactory.getLog(MvBkManager.class);

	private StoreJedisManager storeJedisManager;
	private ResourceLinkFacade resourceLinkFacade;
	private MyMovieFacade myMovieFacade;
	private MvFacade mvFacade;
	private BkFacade bkFacade;

	private ResourceLinkManager resourceLinkManager;
	private GetResourceInfoFacadeImpl getResourceInfoFacade;
	private UcenterFacade ucenterFacade;
	private ActFacade actFacade;
	private MvCommentFacade mvCommentFacade;
	private ResStatJedisManager resStatJedisManager;
	private RankingManager rankingManager;
	
	private MovieUtils movieUtils = MovieUtils.getInstance();

	private FileUtils fileUtils = FileUtils.getInstance();

	public void setResourceLinkManager(ResourceLinkManager resourceLinkManager) {
		this.resourceLinkManager = resourceLinkManager;
	}

	public void setStoreJedisManager(StoreJedisManager storeJedisManager) {
		this.storeJedisManager = storeJedisManager;
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

	public void setGetResourceInfoFacade(GetResourceInfoFacadeImpl getResourceInfoFacade) {
		this.getResourceInfoFacade = getResourceInfoFacade;
	}

	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}

	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
		this.mvCommentFacade = mvCommentFacade;
	}

	public void setResStatJedisManager(ResStatJedisManager resStatJedisManager) {
		this.resStatJedisManager = resStatJedisManager;
	}

	public void setRankingManager(RankingManager rankingManager) {
		this.rankingManager = rankingManager;
	}

	/**
	 * 获取resource列表
	 * 
	 * @param id
	 * @param caseType
	 * @param uid
	 * @return
	 */
	public List<ResourceInfo> getResourcesFromLink(Long id, String caseType, Long uid, String sort, Long lastId, int page) {
		int pageSize = StoreJedisConstant.pageSize;
		List<ResourceLink> resourceLinks = new ArrayList<ResourceLink>();
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
		long begin = System.currentTimeMillis();
		try {
			if (ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.RESOURCES)) {
			} else if (ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.MOVIES)) {
				long pageStartIndex = 0;
				if (lastId == null) {
					if (page <= 1) {
						lastId = null;
					} else {
						pageStartIndex = (page - 1) * pageSize - 1;
						lastId = storeJedisManager.getRedisIndex(caseType, pageStartIndex, sort);
						if (lastId == null) {
							return resourceInfos;
						}
					}
				}
				if (caseType.equals("170") || caseType.equals("171") || caseType.equals("172")) {
					List<Map<String, String>> mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, "");
					if (mvMap.size() <= 0) {
						// resourceLinks =
						// resourceLinkFacade.findResListByType(caseType);
						// for (ResourceLink link : resourceLinks) {
						// try {
						// List<MvListLink> mvListLinks =
						// myMovieFacade.findMovieListInfo(link.getResLinkId(),
						// null, null);
						// for (MvListLink mvLink : mvListLinks) {
						// MvInfo info =
						// mvFacade.queryById(mvLink.getMovieId());
						// if (info.getFlag() == ResultUtils.SUCCESS) {
						// storeJedisManager.saveMovieId(info, caseType,
						// mvLink);
						// storeJedisManager.saveMovie(info.getId(), info);
						// }
						// }
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						storeJedisManager.saveBkMvRedis(caseType);
						mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, "");
					}
					List movieInfos = new ArrayList();
					for (Map<String, String> map : mvMap) {
						try {
							MovieInfo info = new MovieInfo();
							String infoId = map.get(StoreJedisConstant.STORE_MOVIE_HASH_ID);
							String moviePic = map.get(StoreJedisConstant.STORE_MOVIE_HASH_MOVIE_PIC);
							String moviePic2 = map.get(StoreJedisConstant.STORE_MOVIE_HASH_MOVIE_PIC2);
							String score = map.get(StoreJedisConstant.STORE_MOVIE_HASH_SCORE);
							String name = map.get(StoreJedisConstant.STORE_MOVIE_HASH_NAME);
							String releaseDate = map.get(StoreJedisConstant.STORE_MOVIE_HASH_RELEASE_DATE);

							info.setId(CheckParams.stringToLong(infoId));
							info.setMoviePic(moviePic);
							info.setMovieHPic(moviePic2);
							info.setName(name);
							info.setYear(releaseDate);
							// info.setScore(score);
							info.setTalentScore(score);

							if (StringUtils.isEmpty(info.getMovieHPic())) {
								info.setMovieHPic(info.getMoviePic());
							}

							MvInfo mvInfo = mvFacade.queryById(Long.valueOf(infoId));
							if (mvInfo.getFlag() == ResultUtils.SUCCESS) {
								if (StringUtils.isEmpty(mvInfo.getBoxOffice())) {
									info.setTotalTicket("暂无");
								} else {
									info.setTotalTicket(mvInfo.getBoxOffice());
								}
								
								info.setDirector(mvInfo.getDirector());
								//演员
								info.setActor(mvInfo.getActor());
							}
							MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(Long.valueOf(infoId));
							if (mvAvgMark.getFlag() == ResultUtils.SUCCESS || mvAvgMark.getFlag() == 0) {
								info.setTalentScore(String.valueOf(mvAvgMark.getExpertsAvgMark()));
								info.setScore(String.valueOf(mvAvgMark.getMvAvgMark()));
								if (StringUtils.isEmpty(info.getTalentScore()) || CheckParams.stringToDouble(info.getTalentScore()) == 0) {
									if (!StringUtils.isEmpty(info.getScore())) {
										info.setTalentScore(info.getScore());
									}
								}
							}
							int totalSearchNum = resStatJedisManager.getTotalSearchNum(info.getId(), CommentUtils.TYPE_MOVIE);
							long searchNum = rankingManager.getSearchNum(info.getId(), totalSearchNum);
							info.setSearchNum(String.valueOf(searchNum));

							movieInfos.add(info);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// 排序
					resourceInfos = getMovieResponseList(movieInfos, uid, resourceInfos);
				}
			} else if (ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.BOOKS)) {
				long pageStartIndex = 0;
				if (lastId == null) {
					if (page <= 1) {
						lastId = null;
					} else {
						pageStartIndex = (page - 1) * pageSize - 1;
						lastId = storeJedisManager.getRedisIndex(caseType, pageStartIndex, sort);
						if (lastId == null) {
							return resourceInfos;
						}
					}
				}
				if (caseType.equals("160") || caseType.equals("161") || caseType.equals("162")) {
					// if(CommentUtils.TYPE_BOOK.equals(obj.getResType())){
					// bkInfo = bkFacade.findBkInfo(obj.getBookId());
					// object = bookUtils.putBKToBookInfo(bkInfo,
					// TRUE,bookList.getuId(),bkCommentFacade , ucenterFacade);
					// }else
					// if(CommentUtils.TYPE_NETBOOK.equals(obj.getResType())){
					// NetBook netBook =
					// netBookFacade.findNetBookInfoById(obj.getBookId());
					// object = bookUtils.putBKToBookInfo(netBook,
					// TRUE,bookList.getuId(),bkCommentFacade , ucenterFacade);
					// }

					List<Map<String, String>> mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, "");
					if (mvMap.size() <= 0) {
						// resourceLinks =
						// resourceLinkFacade.findResListByType(caseType);
						// for (ResourceLink link : resourceLinks) {
						// List<BookListLink> bookListLinks =
						// getResourceInfoFacade.findBookListInfo(link.getResLinkId(),
						// null, null);
						// try {
						// for (BookListLink mvLink : bookListLinks) {
						// BkInfo info =
						// bkFacade.findBkInfo(mvLink.getBookId());
						// if (info.getFlag() == ResultUtils.SUCCESS) {
						// storeJedisManager.saveBookId(info, caseType, mvLink);
						// storeJedisManager.saveBook(info.getId(), info);
						// }
						// }
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						storeJedisManager.saveBkMvRedis(caseType);
						mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, "");
					}
					for (Map<String, String> map : mvMap) {
						try {
							BkInfo bkInfo = new BkInfo();
							String infoId = map.get(StoreJedisConstant.STORE_BOOK_HASH_ID);
							String name = map.get(StoreJedisConstant.STORE_BOOK_HASH_NAME);
							String authorName = map.get(StoreJedisConstant.STORE_BOOK_HASH_AUTHOR_NAME);
							String isdb = map.get(StoreJedisConstant.STORE_BOOK_HASH_ISBN);
							String score = map.get(StoreJedisConstant.STORE_BOOK_HASH_SCORE);
							String publishTime = map.get(StoreJedisConstant.STORE_BOOK_HASH_PUBLISHING_TIME);
							String bookPic = map.get(StoreJedisConstant.STORE_BOOK_HASH_BOOK_PIC);

							bkInfo.setId(Integer.parseInt(infoId));
							bkInfo.setName(name);
							bkInfo.setAuthorName(authorName);
							bkInfo.setIsbn(isdb);
							bkInfo.setScore(score);
							bkInfo.setPublishingTime(publishTime);
							bkInfo.setBookPic(bookPic);

							BookInfo object = fileUtils.putBKToBookInfo(bkInfo, TRUE);
							ResourceInfo resourceInfo = new ResourceInfo();
							resourceInfo.setFlag(ResultUtils.SUCCESS);
							resourceInfo.setRid(bkInfo.getId());
							resourceInfo.setRidStr(infoId);
							resourceInfo.setBookInfo(object);
							resourceInfos.add(resourceInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else if (ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.RESOURCE_BY_ID)) {
			} else if (ResourceLinkManager.caseTypeInList(caseType, ResourceLinkManager.BOOK_MOVIE_LISTS)) {
				long pageStartIndex = 0;
				if (lastId == null) {
					if (page <= 1) {
						lastId = null;
					} else {
						pageStartIndex = (page - 1) * pageSize - 1;
						lastId = storeJedisManager.getRedisIndex(caseType, pageStartIndex, sort);
						if (lastId == null) {
							return resourceInfos;
						}
					}
				}
				if (caseType.equals("140") || caseType.equals("141") || caseType.equals("142")) {
					List<Map<String, String>> mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, "");
					if (mvMap.size() <= 0) {
						// resourceLinks =
						// resourceLinkFacade.findResListByType(caseType);
						// for (ResourceLink link : resourceLinks) {
						// MovieList movieList =
						// myMovieFacade.findMovieListById(link.getResLinkId());
						// try {
						// if (movieList.getFlag() == ResultUtils.SUCCESS) {
						// storeJedisManager.saveMovieListId(link, movieList,
						// caseType);
						// storeJedisManager.saveMovieList(movieList.getId(),
						// movieList);
						// }
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						storeJedisManager.saveBkMvRedis(caseType);
						mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, "");
					}
					for (Map<String, String> map : mvMap) {
						try {
							String infoId = map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_ID);
							String filmlistName = map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_FILMLIST_NAME);
							String reason = map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_REASON);
							String conver = map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_COVER);
							String createDate = map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_CREATE_DATE);
							String uId = map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_U_ID);

							// String isDel =
							// map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_IS_DEL);
							// String type =
							// map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_TYPE);
							// String tag =
							// map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_TAG);
							// String isPublishing =
							// map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_IS_PUBLISHING);
							// String latestRevisionDate =
							// map.get(StoreJedisConstant.STORE_MOVIELIST_HASH_LATEST_REVISION_DATE);

							ResourceInfo resourceInfo = new ResourceInfo();
							resourceInfo.setBtime(createDate);
							resourceInfo.setRid(CheckParams.stringToLong(infoId));
							// ri.setContList(WebUtils.putHTMLToData(object.getContent()));
							resourceInfo.setType(CommentUtils.TYPE_MOVIELIST);
							resourceInfo.setTitle(filmlistName);
							// ri.setReason(object.getReason());
							resourceInfo.setSummary(reason);
							resourceInfo.setImageUrl(CheckParams.matcherMoviePic(conver));
							resourceInfo.setMovieListId(CheckParams.stringToLong(infoId));
							resourceInfo.setFlag(ResultUtils.SUCCESS);
							
							// //如果是书单或影单，提取书单id或影单id
							// long r_id = resourceInfo.getRid();
							// if(CommentUtils.TYPE_BOOKLIST.equals(resourceInfo.getType())){
							// r_id = resourceInfo.getBookListId();
							// }else
							// if(CommentUtils.TYPE_MOVIE_TALK.equals(resourceInfo.getType())){
							// r_id = resourceInfo.getMovieListId();
							// }
							// int rNum= actFacade.findTransmitCount(null,
							// r_id);
							// int cNum = actFacade.findCommentCount(null,
							// r_id);
							// int zNum= actFacade.findPraiseCount(null, r_id);
							// int lNum = actFacade.findLowCount(r_id);
							// resourceInfo.setrNum(rNum);
							// resourceInfo.setcNum(cNum);
							// resourceInfo.setzNum(zNum);
							// resourceInfo.setlNum(lNum);
							//
							// //是否赞过
							// if(uid != null && uid != 0){
							// ActPraise actPraise =
							// actFacade.findActPraise(uid,
							// r_id);
							// if(actPraise != null && actPraise.getFlag() ==
							// ResultUtils.SUCCESS){
							// resourceInfo.setIsPraise(changeIntForTrueFalse(actPraise.getIsPraise()));
							// resourceInfo.setIsLow(changeIntForTrueFalse(actPraise.getIsLow()));
							// }
							// }
							if (caseType.equals("141") && !StringUtils.isEmpty(uId)) {
								try {
									long userId = CheckParams.stringToLong(uId);
									UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
									UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, FALSE);
									resourceInfo.setUserEntity(userEntity);
								} catch (Exception e) {
									LOG.error(e.getMessage(), e.fillInStackTrace());
								}
							}
							try{
								//需要为每个影单增加最多30个电影
								List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(resourceInfo.getRid(),null, 30);
								if(mvListLinks!=null && mvListLinks.size()>0){
									long ids[] = new long[mvListLinks.size()];
									for(int j=0;j<mvListLinks.size();j++){
										ids[j]=mvListLinks.get(j).getMovieId();
									}
									List<MvInfo> mvinfos = mvFacade.findMvInfosByIds(ids);
									if(mvinfos!=null && mvinfos.size()>0){
										List<MovieInfo> mvInfos = new ArrayList<MovieInfo>(mvListLinks.size());
										for(int j=0;j<mvinfos.size();j++){
											MvInfo mvInfo = mvinfos.get(j);
											if(mvInfo!=null && mvInfo.getId()>0){
												MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, TRUE);
												mvInfos.add(movieInfo);
											}
										}
										resourceInfo.setMovieInfos(mvInfos);
									}
								}
							}catch(Exception e){
								e.printStackTrace();
							}

							resourceInfos.add(resourceInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if (caseType.equals("150") || caseType.equals("151") || caseType.equals("152")) {
					List<Map<String, String>> mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, "");
					if (mvMap.size() <= 0) {
						// resourceLinks =
						// resourceLinkFacade.findResListByType(caseType);
						// for (ResourceLink link : resourceLinks) {
						// BookList bookList =
						// getResourceInfoFacade.queryByIdBookList(link.getResLinkId());
						// try {
						// if (bookList.getFlag() == ResultUtils.SUCCESS) {
						// storeJedisManager.saveBookListId(link, bookList,
						// caseType);
						// storeJedisManager.saveBookList(bookList.getId(),
						// bookList);
						// }
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						// }
						storeJedisManager.saveBkMvRedis(caseType);
						mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, "");
					}

					for (Map<String, String> map : mvMap) {
						try {
							String infoId = map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_ID);
							String filmlistName = map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_BOOKLIST_NAME);
							String reason = map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_REASON);
							String createDate = map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_CREATE_DATE);
							String conver = map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_COVER);
							String uId = map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_U_ID);

							// String isDel =
							// map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_IS_DEL);
							// String type =
							// map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_TYPE);
							// String tag =
							// map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_TAG);
							// String isPublishing =
							// map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_IS_PUBLISHING);
							// String latestRevisionDate =
							// map.get(StoreJedisConstant.STORE_BOOKLIST_HASH_LATEST_REVISION_DATE);

							ResourceInfo resourceInfo = new ResourceInfo();
							resourceInfo.setBtime(createDate);
							resourceInfo.setRid(CheckParams.stringToLong(infoId));
							// ri.setContList(WebUtils.putHTMLToData(object.getContent()));
							resourceInfo.setType(CommentUtils.TYPE_BOOKLIST);
							resourceInfo.setTitle(filmlistName);
							// ri.setReason(object.getReason());
							resourceInfo.setSummary(reason);
							resourceInfo.setImageUrl(CheckParams.matcherMoviePic(conver));
							resourceInfo.setBookListId(CheckParams.stringToLong(infoId));
							resourceInfo.setFlag(ResultUtils.SUCCESS);

							// 如果是书单或影单，提取书单id或影单id
							// long r_id = resourceInfo.getRid();
							// if(CommentUtils.TYPE_BOOKLIST.equals(resourceInfo.getType())){
							// r_id = resourceInfo.getBookListId();
							// }else
							// if(CommentUtils.TYPE_MOVIE_TALK.equals(resourceInfo.getType())){
							// r_id = resourceInfo.getMovieListId();
							// }
							// int rNum= actFacade.findTransmitCount(null,
							// r_id);
							// int cNum = actFacade.findCommentCount(null,
							// r_id);
							// int zNum= actFacade.findPraiseCount(null, r_id);
							// int lNum = actFacade.findLowCount(r_id);
							// resourceInfo.setrNum(rNum);
							// resourceInfo.setcNum(cNum);
							// resourceInfo.setzNum(zNum);
							// resourceInfo.setlNum(lNum);
							//
							// //是否赞过
							// if(uid != null && uid != 0){
							// ActPraise actPraise =
							// actFacade.findActPraise(uid,
							// r_id);
							// if(actPraise != null && actPraise.getFlag() ==
							// ResultUtils.SUCCESS){
							// resourceInfo.setIsPraise(changeIntForTrueFalse(actPraise.getIsPraise()));
							// resourceInfo.setIsLow(changeIntForTrueFalse(actPraise.getIsLow()));
							// }
							// }
							if (caseType.equals("151") && !StringUtils.isEmpty(uId)) {
								try {
									long userId = CheckParams.stringToLong(uId);
									UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, userId);
									UserEntity userEntity = fileUtils.copyUserInfo(userAllInfo, FALSE);
									resourceInfo.setUserEntity(userEntity);
								} catch (Exception e) {
									LOG.error(e.getMessage(), e.fillInStackTrace());
								}
							}

							resourceInfos.add(resourceInfo);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		LOG.info("link表获取时间：" + (end - begin));
		return resourceInfos;
	}

	/**
	 * 获取resource列表
	 * 
	 * @param id
	 * @param caseType
	 * @param uid
	 * @return
	 */
	public List<ResourceGroupInfo> getResourcesFromLink(Long id, String caseType, Long uid, String sort, Long lastId, int page, String newType) {
		List<ResourceLink> resourceLinks = new ArrayList<ResourceLink>();
		List<ResourceGroupInfo> resourceInfos = new ArrayList<ResourceGroupInfo>();
		long begin = System.currentTimeMillis();
		try {
			LOG.info("getResourcesFromLink begin caseType:" + caseType + " id:" + id + " uid:" + uid + " sort:" + sort + " lastId:" + lastId + " page:" + page + " netType:" + newType);
			List<Map<String, String>> mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, newType);
			LOG.info("mvMap outer:" + mvMap.size() + " map:");
			if (mvMap.size() <= 0) {
				// resourceLinks =
				// resourceLinkFacade.findResListByType(caseType);
				// for (ResourceLink link : resourceLinks) {
				// try {
				// List<MvListLink> mvListLinks =
				// myMovieFacade.findMovieListInfo(link.getResLinkId(), null,
				// null);
				// for (MvListLink mvLink : mvListLinks) {
				// MvInfo info = mvFacade.queryById(mvLink.getMovieId());
				// if (info.getFlag() == ResultUtils.SUCCESS) {
				// storeJedisManager.saveMovieId(info, caseType, mvLink);
				// storeJedisManager.saveMovie(info.getId(), info);
				// }
				// }
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// }
				storeJedisManager.saveBkMvRedis(caseType);
				mvMap = storeJedisManager.getOperMovies(lastId, caseType, sort, newType);
				LOG.info("mvMap inner:" + mvMap.size() + " map:");
			}
			List<MovieInfo> movieInfos = new ArrayList<MovieInfo>();
			for (Map<String, String> map : mvMap) {
				try {
					MovieInfo info = new MovieInfo();
					String infoId = map.get(StoreJedisConstant.STORE_MOVIE_HASH_ID);
					String moviePic = map.get(StoreJedisConstant.STORE_MOVIE_HASH_MOVIE_PIC);
					String moviePic2 = map.get(StoreJedisConstant.STORE_MOVIE_HASH_MOVIE_PIC2);
					String score = map.get(StoreJedisConstant.STORE_MOVIE_HASH_SCORE);
					String name = map.get(StoreJedisConstant.STORE_MOVIE_HASH_NAME);
					String releaseDate = map.get(StoreJedisConstant.STORE_MOVIE_HASH_RELEASE_DATE);
					Double releaseDateDouble = CheckParams.getRealeaseDate(map.get(StoreJedisConstant.STORE_MOVIE_HASH_RELEASE_DATE));
					String splitDate = CheckParams.getSplitRealeaseDate(String.valueOf(releaseDateDouble.intValue()));
					List<String> year = new ArrayList<String>();
					year.add(splitDate);

					info.setId(CheckParams.stringToLong(infoId));
					info.setMoviePic(moviePic);
					info.setMovieHPic(moviePic2);
					info.setName(name);
					// info.setYear(releaseDate);
					info.setYear(splitDate);
					// info.setScore(score);
					info.setTalentScore(score);

					if (StringUtils.isEmpty(info.getMovieHPic())) {
						info.setMovieHPic(info.getMoviePic());
					}

					// 即将上映 判断上映日期是否大于今天
					boolean afterNow = DateUtil.compareNow(splitDate);
					if (!afterNow)
						continue;

					MvInfo mvInfo = mvFacade.queryById(info.getId());
					if (mvInfo!=null && mvInfo.getFlag() == ResultUtils.SUCCESS) {
						if (StringUtils.isEmpty(mvInfo.getBoxOffice())) {
							info.setTotalTicket("暂无");
						} else {
							info.setTotalTicket(mvInfo.getBoxOffice());
						}
						
						info.setDirector(mvInfo.getDirector());
						//演员
						info.setActor(mvInfo.getActor());
					}
					MvAvgMark mvAvgMark = mvCommentFacade.findMvAvgMarkByMvId(Long.valueOf(infoId));
					if (mvAvgMark.getFlag() == ResultUtils.SUCCESS || mvAvgMark.getFlag() == 0) {
						info.setTalentScore(String.valueOf(mvAvgMark.getExpertsAvgMark()));
						info.setScore(String.valueOf(mvAvgMark.getMvAvgMark()));
						if (StringUtils.isEmpty(info.getTalentScore()) || CheckParams.stringToDouble(info.getTalentScore()) == 0) {
							if (!StringUtils.isEmpty(info.getScore())) {
								info.setTalentScore(info.getScore());
							}
						}
					}
					int totalSearchNum = resStatJedisManager.getTotalSearchNum(info.getId(), CommentUtils.TYPE_MOVIE);
					long searchNum = rankingManager.getSearchNum(info.getId(), totalSearchNum);
					info.setSearchNum(String.valueOf(searchNum));
					movieInfos.add(info);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<MovieInfo> movieInfosProxy = sortMoviesByReleaseDate(movieInfos);
			Map<String, List<MovieInfo>> infoMap = getMonthGroupMap(movieInfosProxy);
			Set<String> groupSet = getMonthGroupSet(movieInfosProxy);
			resourceInfos = getMovieResponseList(uid, resourceInfos, infoMap, groupSet);
			LOG.info("resourceInfos  " + " size:" + resourceInfos.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		LOG.info("link表获取时间：" + (end - begin));
		return resourceInfos;
	}

	/**
	 * 将资源转为首页
	 * 
	 * @param list
	 * @param type
	 * @return
	 */
	public List getMovieResponseList(List reqList, Long uid, List<ResourceInfo> resList) {
		if (reqList == null || reqList.size() <= 0)
			return resList;
		for (Object object : reqList) {
			try {
				MovieInfo info = (MovieInfo) object;
				ResourceInfo ri = new ResourceInfo();
				ri.setType(CommentUtils.TYPE_MOVIE);
				ri.setRid(info.getId());
				ri.setRidStr(String.valueOf(info.getId()));
				ri.setMovieInfo(info);
				resList.add(ri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resList;
	}

	/**
	 * 171将资源转为首页
	 * 
	 * @param list
	 * @param type
	 * @return
	 */
	public List<ResourceGroupInfo> getMovieResponseList(Long uid, List<ResourceGroupInfo> resList, Map<String, List<MovieInfo>> infoMap, Set<String> groupSet) {
		if (groupSet == null || groupSet.size() <= 0)
			return resList;
		for (String str : groupSet) {
			if (StringUtils.isEmpty(str))
				continue;
			List<MovieInfo> mvInfos = infoMap.get(str);
			ResourceGroupInfo groupInfo = new ResourceGroupInfo();
			// groupInfo.setMonth(str.substring(4, 6));
			String month = CheckParams.getSplitRealeaseDate(str.substring(0, 8));
			String weekDay = DateUtil.getWeekDay(month);
			groupInfo.setMonth(month + "  " + weekDay);
			List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>();
			for (Object object : mvInfos) {
				try {
					MovieInfo info = (MovieInfo) object;
					ResourceInfo ri = new ResourceInfo();
					ri.setType(CommentUtils.TYPE_MOVIE);
					ri.setRid(info.getId());
					ri.setRidStr(String.valueOf(info.getId()));
					ri.setMovieInfo(info);
					resourceInfos.add(ri);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			groupInfo.setResourceInfos(resourceInfos);
			resList.add(groupInfo);
		}
		return resList;
	}

	/**
	 * 排序组合
	 * 
	 * @param movieInfos
	 * @return
	 */
	public List<MovieInfo> sortMoviesByReleaseDate(List<MovieInfo> movieInfos) {
		List<MovieInfo> mvInfos = new ArrayList<MovieInfo>();
		try {
			List<MovieInfo> movieInfosProxy = new ArrayList<MovieInfo>();
			for (MovieInfo info : movieInfos) {
				try {
					String releaseDate = info.getYear().get(0).replaceAll("-", "");
					if (StringUtils.equals(releaseDate, "0")) {
						continue;
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				movieInfosProxy.add(info);
			}

			MovieInfo[] args = new MovieInfo[movieInfosProxy.size()];
			int index = 0;
			for (MovieInfo info : movieInfosProxy) {
				args[index] = info;
				index++;
			}

			for (int i = 0; i < args.length - 1; i++) {
				for (int j = i + 1; j < args.length; j++) {
					MovieInfo temp;

					String releaseDatei = args[i].getYear().get(0).replaceAll("-", "");
					String releaseDatej = args[j].getYear().get(0).replaceAll("-", "");

					long rsDatei = Long.valueOf(releaseDatei);
					long rsDatej = Long.valueOf(releaseDatej);
					if (rsDatei > rsDatej) {
						temp = args[j];
						args[j] = args[i];
						args[i] = temp;
					}
				}
			}
			for (int i = 0; i <= args.length - 1; i++) {
				MovieInfo time = args[i];
				mvInfos.add(time);
			}
		} catch (Exception e) {
			LOG.error(":error=>" + e.getMessage());
			e.printStackTrace();
		}
		return mvInfos;
	}

	/**
	 * 电影按月分组
	 * 
	 * @param infoMap
	 * @param infos
	 * @return
	 */
	public Map<String, List<MovieInfo>> getMonthGroupMap(List<MovieInfo> infos) {
		Map<String, List<MovieInfo>> infoMap = new HashMap<String, List<MovieInfo>>();
		List<MovieInfo> inf = new ArrayList<MovieInfo>();
		for (MovieInfo info : infos) {
			String releaseDate = info.getYear().get(0).replaceAll("-", "");
			if (StringUtils.equals(releaseDate, "0")) {
				inf.add(info);
				continue;
			}
			// String yearMonthKey = releaseDate.substring(0, 6);
			String yearMonthKey = releaseDate.substring(0, 8);
			boolean hasKey = infoMap.containsKey(yearMonthKey);
			if (hasKey) {
				List<MovieInfo> oldInfo = infoMap.get(yearMonthKey);
				oldInfo.add(info);
			} else {
				List<MovieInfo> infoList = new ArrayList<MovieInfo>();
				infoList.add(info);
				infoMap.put(yearMonthKey, infoList);
			}
		}
		return infoMap;
	}

	/**
	 * 电影按月分组
	 * 
	 * @param infoMap
	 * @param infos
	 * @return
	 */
	public Set<String> getMonthGroupSet(List<MovieInfo> infos) {
		TreeSet<String> set = new TreeSet<String>();
		for (MovieInfo info : infos) {
			String releaseDate = info.getYear().get(0).replaceAll("-", "");
			if (StringUtils.equals(releaseDate, "0")) {
				continue;
			}
			// String yearMonthKey = releaseDate.substring(0, 6);
			String yearMonthKey = releaseDate.substring(0, 8);
			set.add(yearMonthKey);
		}
		return set;
	}

	public static void main(String[] args) {
		String newType = "171";
		String[] newString = newType.split(",");
		System.out.println(newString.length);
		MovieInfo info = new MovieInfo();
		info.setYear("0");
		System.out.println(info.getYear().get(0));
	}
}
