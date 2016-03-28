package com.poison.eagle.manager.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;

import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ActorInfo;
import com.poison.eagle.entity.ArticleInfo;
import com.poison.eagle.entity.DiaryInfo;
import com.poison.eagle.entity.MvActorInfo;
import com.poison.eagle.manager.MvBkManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.ResourceLinkFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.Diary;
import com.poison.resource.model.ResourceLink;
import com.poison.store.client.MvActorFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.dao.MvInfoDAO;
import com.poison.store.dao.impl.MvActorDAOImpl;
import com.poison.store.model.Actor;
import com.poison.store.model.MvActor;
import com.poison.store.model.MvInfo;

/**
 * 电影操作工具类，包括电影，角色相关
 * 
 * @author :zhangqi
 * @time:2015-6-10下午7:31:25
 * @return
 */
public class MovieAutoUtil {

	private ActFacade actFacade;
	private MvActorFacade mvActorFacade;
	private MvFacade mvFacade;
	private ObjectMapper objectMapper;
	private ResourceLinkFacade resourceLinkFacade;
	private ArticleFacade articleFacade;
	private DiaryFacade diaryFacade;
	private MvBkManager mvBkManager;
	private Log logger = LogFactory.getLog(this.getClass());

	private ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = new ObjectMapper();

			objectMapper.setSerializationInclusion(Inclusion.NON_NULL);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			objectMapper.configure(DeserializationConfig.Feature.WRAP_EXCEPTIONS, false);
			// objectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
			// true) ;
			// objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS,
			// true) ;
			return objectMapper;
		} else {
			return objectMapper;
		}
	}

	/**
	 * 新增加电影角色信息
	 * 
	 * @param mvId
	 *            电影ID
	 * @param type
	 *            角色类别 空代表所有角色
	 * @param viewDetailNone
	 *            是否显示角色详情为空的角色 true 显示 false 不显示
	 */
	public List<ActorInfo> getActorInfos(Long mvId, String type, boolean viewDetailNone) {
		List<MvActor> actors = mvActorFacade.findListMvActorByMvId(mvId, type);
		List<ActorInfo> actorInfos = new ArrayList<ActorInfo>();
		if (null != actors && actors.size() > 0) {
			for (MvActor mvActor : actors) {
				long actorId = mvActor.getActorId();
				ActorInfo actorInfo = new ActorInfo();
				actorInfo.setName(mvActor.getName());
				actorInfo.setActorName(mvActor.getActorName());
				actorInfo.setActorType(String.valueOf(mvActor.getActorType()));
				if (actorId != 0) {
					Actor actor = mvActorFacade.findActorById(actorId);
					if (actor.getFlag() == ResultUtils.SUCCESS) {
						actorInfo.setId(actor.getId());
						actorInfo.setPic(actor.getPic());
					} else {
						if (!viewDetailNone) {
							continue;
						}
					}
				} else {
					if (!viewDetailNone) {
						continue;
					}
				}
				actorInfos.add(actorInfo);
			}
		}
		return actorInfos;
	}

	/**
	 * 查询角色详情
	 * 
	 * @param actorId
	 * @return
	 */
	public ActorInfo getActorInfoById(long actorId) {
		ActorInfo actorInfo = new ActorInfo();
		try {
			Actor actor = mvActorFacade.findActorWithStillById(actorId);
			if (actor.getFlag() == ResultUtils.SUCCESS) {
				actorInfo.setId(actor.getId());
				actorInfo.setName(actor.getChineseName());
				actorInfo.setPic(actor.getPic());
				actorInfo.setForeignName(actor.getForeignName());
				actorInfo.setBirthday(actor.getBirthday());
				actorInfo.setBirthplace(actor.getBirthplace());
				actorInfo.setConstellation(actor.getConstellation());
				actorInfo.setAboutLarge(actor.getAboutLarge().replaceAll("<br/>", ""));
				String sex = "";
				int sexInt = actor.getSex();
				if (sexInt == 0) {
					sex = "女";
				} else if (sexInt == 1) {
					sex = "男";
				}
				actorInfo.setSex(sex);
				if (actor.getActorStills() != null) {
					String actorStills = actor.getActorStills().getActorStills();
					if (actorStills != null && !"".equals(actorStills)) {
						actorStills = CheckParams.replaceKG(actorStills);
						List list = getObjectMapper().readValue(actorStills, new TypeReference<List>() {
						});
						actorInfo.setActorStills(list);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actorInfo;
	}

	/**
	 * 查询角色所参与的电影信息
	 * 
	 * @param actorId
	 * @param start
	 *            -1 代表所有
	 * @return
	 */
	public List<MvActorInfo> getMvActorInfoByActorI3(long actorId, int start, int pageSize) {
		long beginTime = System.currentTimeMillis();
		List<MvActorInfo> mvActorInfos = new ArrayList<MvActorInfo>();
		try {
			List<MvActor> mvActorsPrxoxy = mvActorFacade.findListMvActorByActorIdDictinctMvUrl(actorId, start, pageSize);
			boolean flag = false;
			for (MvActor mvActor : mvActorsPrxoxy) {
				MvInfo info = mvFacade.queryById(mvActor.getMvId());
				if (info.getFlag() == ResultUtils.SUCCESS) {
					// 导演信息
					List<MvActor> directors = mvActorFacade.findListMvActorByMvId(info.getId(), MvActorDAOImpl.actorTypeDirector);
					MvActorInfo mvActorInfo = new MvActorInfo();
					mvActorInfo.setActorName(mvActor.getActorName());
					mvActorInfo.setMoivePic(info.getMoviePic());
					mvActorInfo.setMovieName(info.getName());
					mvActorInfo.setMvId(info.getId());
					String releaseDateStr = "";
					try {
						Double releaseDate = CheckParams.getRealeaseDate(info.getReleaseDate());
						releaseDateStr = CheckParams.getSplitRealeaseDate(String.valueOf(releaseDate.intValue()));
						String releaseDateSortStr = String.valueOf(info.getReleaseDateSort());
						if (!StringUtils.equals(releaseDateSortStr, String.valueOf(releaseDate.intValue()))) {
							flag = true;
							long releaseDateSort = releaseDate.longValue();
							mvFacade.updateMvInfoReleaseDateSort(info.getId(), releaseDateSort);
						} else {
							releaseDateStr = CheckParams.getSplitRealeaseDate(String.valueOf(info.getReleaseDateSort()));
						}
					} catch (Exception e) {
						Double releaseDate = CheckParams.getRealeaseDate(info.getReleaseDate());
						releaseDateStr = CheckParams.getSplitRealeaseDate(String.valueOf(releaseDate.intValue()));
					}
					mvActorInfo.setReleaseDate(releaseDateStr);
					StringBuffer sb = new StringBuffer();
					for (MvActor actor : directors) {
						sb.append(actor.getName());
						sb.append(",");
					}
					if (directors.size() > 0)
						sb = new StringBuffer(sb.substring(0, sb.length() - 1));
					mvActorInfo.setDirectors(sb.toString());
					mvActorInfos.add(mvActorInfo);
				}
			}
			if (flag) {
				mvActorInfos = sortMoviesByReleaseDate3(mvActorInfos);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return mvActorInfos;
		}
		long endTime = System.currentTimeMillis();
		logger.info("getMvActorInfoByActorI3 time:=>" + (endTime - beginTime) + " 毫秒");
		return mvActorInfos;
	}

	/**
	 * 查询角色所参与的电影信息
	 * 
	 * @param actorId
	 * @param start
	 *            -1 代表所有
	 * @return
	 */
	public List<MvActorInfo> getMvActorInfoByActorId(long actorId, int start, int pageSize) {
		long beginTime = System.currentTimeMillis();
		List<MvActorInfo> mvActorInfos = new ArrayList<MvActorInfo>();
		try {
			// List<MvActor> mvActorsPrxoxy =
			// mvActorFacade.findListMvActorByActorId(actorId, 0, 1000);
			List<MvActor> mvActorsPrxoxy = mvActorFacade.findListMvActorByActorIdDictinctMv(actorId, 0, 1000);
			int mvActorSize = mvActorsPrxoxy.size();
			int toIndex = start + pageSize;
			if (start > mvActorSize - 1) {
				return mvActorInfos;
			}
			if (toIndex > mvActorSize) {
				toIndex = mvActorSize;
			}
			if (start > toIndex) {
				return mvActorInfos;
			}

			for (MvActor mvActor : mvActorsPrxoxy) {
				MvInfo info = mvFacade.queryById(mvActor.getMvId());
				if (info.getFlag() == ResultUtils.SUCCESS) {
					// 导演信息
					List<MvActor> directors = mvActorFacade.findListMvActorByMvId(info.getId(), MvActorDAOImpl.actorTypeDirector);
					MvActorInfo mvActorInfo = new MvActorInfo();
					mvActorInfo.setActorName(mvActor.getActorName());
					mvActorInfo.setMoivePic(info.getMoviePic());
					mvActorInfo.setMovieName(info.getName());
					mvActorInfo.setMvId(info.getId());
					Double releaseDate = CheckParams.getRealeaseDate(info.getReleaseDate());
					mvActorInfo.setReleaseDate(String.valueOf(releaseDate.intValue()));
					StringBuffer sb = new StringBuffer();
					for (MvActor actor : directors) {
						sb.append(actor.getName());
						sb.append(",");
					}
					if (directors.size() > 0)
						sb = new StringBuffer(sb.substring(0, sb.length() - 1));
					mvActorInfo.setDirectors(sb.toString());
					mvActorInfos.add(mvActorInfo);
				}
			}
			mvActorInfos = sortMoviesByReleaseDate(mvActorInfos);
			mvActorInfos = mvActorInfos.subList(start, toIndex);
		} catch (Exception e) {
			e.printStackTrace();
			return mvActorInfos;
		}
		long endTime = System.currentTimeMillis();
		logger.info("getMvActorInfoByActorId time:=>" + (endTime - beginTime) + " 毫秒");
		return mvActorInfos;
	}

	public List<MvActor> getDirectorMvActor(List<MvActor> mvActors, MvActor mvActor) {
		List<MvActor> actors = new ArrayList<MvActor>();
		try {
			for (MvActor proxy : mvActors) {
				if (mvActor.getMvId() != proxy.getMvId())
					continue;
				if (String.valueOf(mvActor.getActorType()).equals(MvActorDAOImpl.actorTypeDirector)) {
					actors.add(mvActor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actors;
	}

	/**
	 * 获取资讯信息（长文章）
	 * 
	 * @param id
	 * @param type
	 * @param last
	 * @return
	 */
	public List<ArticleInfo> findResListByResIdAndType(long id, String type, int last) {
		List<ArticleInfo> infos = new ArrayList<ArticleInfo>();
		try {
			List<ResourceLink> mvMessage = resourceLinkFacade.findResListByResIdAndType(id, type);
			if (last == -1 && mvMessage.size() > 0) {
				ResourceLink link = mvMessage.get(mvMessage.size() - 1);
				mvMessage.clear();
				mvMessage.add(link);
			}
			for (ResourceLink link : mvMessage) {
				try {
					Article art = articleFacade.queryArticleById(link.getResLinkId());
					if (art.getFlag() == ResultUtils.SUCCESS) {
						ArticleInfo info = new ArticleInfo();
						info.setActiclePic(art.getCover());
						info.setArticleType(type);// CommentUtils.TYPE_MV_MESSAGE
													// //CommentUtils.TYPE_NEWARTICLE
						info.setSummary(art.getSummary());
						info.setTitle(art.getName());
						info.setTypeId(art.getId());
						infos.add(info);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	/**
	 * 获取资讯信息（长文章）
	 * 
	 * @param id
	 * @param type
	 * @param last
	 * @return
	 */
	public List<DiaryInfo> findResListByResIdAndType(long id, String type) {
		List<DiaryInfo> infos = new ArrayList<DiaryInfo>();
		try {
			List<ResourceLink> mvMessage = resourceLinkFacade.findResListByResIdAndType(id, type);
			for (ResourceLink link : mvMessage) {
				try {
					Diary act = diaryFacade.queryByIdDiaryWithoutDel(link.getResLinkId());
					if (act.getFlag() == ResultUtils.SUCCESS) {
						DiaryInfo info = new DiaryInfo();
						info.setContent(act.getContent());
						// info.setId(art.getId());
						// info.setType(type);
						infos.add(info);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	/**
	 * 获取 resourceLink
	 * 
	 * @param id
	 * @param type
	 * @param last
	 * @return
	 */
	public List<ResourceLink> listResListByResIdAndType(long id, String type) {
		List<ResourceLink> mvMessage = new ArrayList<ResourceLink>();
		try {
			mvMessage = resourceLinkFacade.findResListByResIdAndType(id, type);
		} catch (Exception e) {
			mvMessage = new ArrayList<ResourceLink>();
			e.printStackTrace();
		}
		return mvMessage;
	}

	public List<MvActorInfo> sortMoviesByReleaseDate(List<MvActorInfo> mvActorInfos) {
		List<MvActorInfo> mvInfos = new ArrayList<MvActorInfo>();
		try {
			MvActorInfo[] args = new MvActorInfo[mvActorInfos.size()];
			int index = 0;
			for (MvActorInfo info : mvActorInfos) {
				args[index] = info;
				index++;
			}
			for (int i = 0; i < args.length - 1; i++) {
				for (int j = i + 1; j < args.length; j++) {
					MvActorInfo temp;
					long rsDatei = Long.valueOf(args[i].getReleaseDate());
					long rsDatej = Long.valueOf(args[j].getReleaseDate());
					if (rsDatei > rsDatej) {
						temp = args[j];
						args[j] = args[i];
						args[i] = temp;
					}
				}
			}
			for (int i = args.length - 1; i >= 0; i--) {
				MvActorInfo time = args[i];
				String releaseDate = CheckParams.getSplitRealeaseDate(time.getReleaseDate());
				time.setReleaseDate(releaseDate);
				mvInfos.add(time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mvInfos;
	}

	public List<MvActorInfo> sortMoviesByReleaseDate3(List<MvActorInfo> mvActorInfos) {
		List<MvActorInfo> mvInfos = new ArrayList<MvActorInfo>();
		try {
			MvActorInfo[] args = new MvActorInfo[mvActorInfos.size()];
			int index = 0;
			for (MvActorInfo info : mvActorInfos) {
				args[index] = info;
				index++;
			}
			for (int i = 0; i < args.length - 1; i++) {
				for (int j = i + 1; j < args.length; j++) {
					MvActorInfo temp;
					long rsDatei = Long.valueOf(args[i].getReleaseDate().replaceAll("-", ""));
					long rsDatej = Long.valueOf(args[j].getReleaseDate().replaceAll("-", ""));
					if (rsDatei > rsDatej) {
						temp = args[j];
						args[j] = args[i];
						args[i] = temp;
					}
				}
			}
			for (int i = args.length - 1; i >= 0; i--) {
				MvActorInfo time = args[i];
				mvInfos.add(time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mvInfos;
	}

	public void setDiaryFacade(DiaryFacade diaryFacade) {
		this.diaryFacade = diaryFacade;
	}

	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}

	public void setMvActorFacade(MvActorFacade mvActorFacade) {
		this.mvActorFacade = mvActorFacade;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}

	public void setResourceLinkFacade(ResourceLinkFacade resourceLinkFacade) {
		this.resourceLinkFacade = resourceLinkFacade;
	}

	public void setArticleFacade(ArticleFacade articleFacade) {
		this.articleFacade = articleFacade;
	}

	public MvBkManager getMvBkManager() {
		return mvBkManager;
	}

	public void setMvBkManager(MvBkManager mvBkManager) {
		this.mvBkManager = mvBkManager;
	}

}
