package com.poison.resource.domain.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.keel.utils.UKeyWorker;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.TopicDAO;
import com.poison.resource.dao.TopicLinkDAO;
import com.poison.resource.model.Topic;
import com.poison.resource.model.TopicLink;

public class TopicDomainRepository {

	private TopicDAO topicDAO;
	
	private TopicLinkDAO topicLinkDAO;
	
	private UKeyWorker reskeyWork;
	
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}
	public void setTopicLinkDAO(TopicLinkDAO topicLinkDAO) {
		this.topicLinkDAO = topicLinkDAO;
	}

	public void setReskeyWork(UKeyWorker reskeyWork) {
		this.reskeyWork = reskeyWork;
	}
	//保存话题或话题关联
	public Topic saveTopicOrLink(Topic topic,TopicLink topicLink){
		boolean result = false;
		//需要判断话题是否存在,不存在则创建
		String title = topic.getTitle();
		int flag = ResultUtils.ERROR;
		Topic topic2 = topicDAO.findTopic(title);
		if(topic2==null || topic2.getId()==0){
			//不存在,可以插入
			flag = topicDAO.insertTopic(topic);
			if(flag==ResultUtils.SUCCESS){
				//保存话题成功
				topic2 = topicDAO.findTopicByID(topic.getId());
				result = true;
			}else{
				topic2 = new Topic();
				topic2.setFlag(flag);
				result = false;
			}
		}else{
			//存在话题,不需要插入
			result = true;
		}
		if(result){
			if(topicLink!=null){
				topicLink.setTopicid(topic2.getId());
				//再判断话题关联是否存在
				TopicLink tk = topicLinkDAO.findTopicIsExist(topicLink);
				if(tk==null || tk.getId()==0){
					//关联不存在,可以插入;
					flag = topicLinkDAO.addTopicLink(topicLink);
					if(flag==ResultUtils.SUCCESS){
						//保存关联成功
						result = true;
					}else{
						result = false;
					}
				}else{
					//关联已经存在
					result = false;
				}
			}
		}
		return topic2;
	}
	
	
	/**
	 * 保存一个话题
	 * @Title: insertTopic 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:18:27
	 * @param @param topic
	 * @param @return
	 * @return int
	 * @throws
	 */
	public Topic insertTopic(Topic topic){
		//需要判断话题是否存在
		String title = topic.getTitle();
		int flag = ResultUtils.ERROR;
		Topic topic2 = topicDAO.findTopic(title);
		Topic topic3 = new Topic();
		if(topic2==null || topic2.getId()==0){
			//不存在,可以插入
			flag = topicDAO.insertTopic(topic);
			if(flag==ResultUtils.SUCCESS){
				topic3 = topicDAO.findTopicByID(topic.getId());
				if(topic3==null || topic3.getId()==0){
					topic3 = new Topic();
					flag = ResultUtils.DATAISNULL;
				}
			}
		}
		topic3.setFlag(flag);
		return topic3;
	}
	
	/**
	 * 根据话题名称查询话题记录
	 * @Title: findTopic 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:18:39
	 * @param @param title
	 * @param @return
	 * @return Topic
	 * @throws
	 */
	public Topic findTopic(String title){
		Topic topic = topicDAO.findTopic(title);
		return topic;
		/*if(topic!=null && topic.getId()>0){
			return topic;
		}else{
			//不存在,可以插入
			topic = new Topic();
			topic.setUserid(userId);
			topic.setTitle(title.trim());
			topic.setCover("");
			topic.setTags("");
			topic.setDescription("");
			topic.setCreateDate(System.currentTimeMillis());
			topic.setLatestRevisionDate(System.currentTimeMillis());
			topic.setIsDelete(0);
			long id = reskeyWork.getId();
			topic.setId(id);
			int flag = topicDAO.insertTopic(topic);
			if(flag==ResultUtils.SUCCESS){
				//保存话题成功
				topic = topicDAO.findTopicByID(topic.getId());
				return topic;
			}else{
				topic.setFlag(flag);
				return topic;
			}
		}*/
	}
	
	/**
	 * 根据用户id查询话题列表
	 * @Title: findTopicSByUserId 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:20:11
	 * @param @param userId
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	public List<Topic> findTopicsByUserId(Long userId){
		return topicDAO.findTopicsByUserId(userId);
	}
	
	/**
	 * 根据活动id和用户id删除一个话题
	 * @Title: deleteTopic 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:22:49
	 * @param @param id
	 * @param @param userId
	 * @param @return
	 * @return int
	 * @throws
	 */
	public Topic deleteTopic(Long id,Long userId){
		int flag = ResultUtils.ERROR;
		Topic topic = topicDAO.findTopicByID(id);
		if(null==topic){
			topic = new Topic();
			flag = ResultUtils.DATAISNULL;
			topic.setFlag(flag);
			return topic;
		}
		flag = topicDAO.deleteTopic(id, userId);
		topic.setFlag(flag);
		return topic;
	}
	
	/**
	 * 编辑一个话题
	 * @Title: updateTopic 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-10 下午3:23:47
	 * @param @param topic
	 * @param @return
	 * @return int
	 * @throws
	 */
	public Topic updateTopic(String cover,String tags,String description,long id,long userid){
		int flag = ResultUtils.ERROR;
		Topic topic = topicDAO.findTopicByID(id);
		if(topic!=null && topic.getId()>0){
			topic.setCover(cover);
			topic.setTags(tags);
			topic.setDescription(description);
			topic.setLatestRevisionDate(System.currentTimeMillis());
			topic.setUserid(userid);
			flag = topicDAO.updateTopic(topic);
			if(flag==ResultUtils.SUCCESS){
				topic = topicDAO.findTopicByID(topic.getId());
				if(topic!=null && topic.getId()>0){
					topic.setFlag(ResultUtils.SUCCESS);
					return topic;
				}
			}
		}
		topic = new Topic();
		topic.setFlag(flag);
		return topic;
	}
	/**
	 *  根据话题id查询话题记录
	 */
	public Topic findTopicByID(long id){
		return topicDAO.findTopicByID(id);
	}
	/**
	 * 增加阅读量
	 */
	public Topic addTopicReadcount(Topic topic){
		int flag = topicDAO.addTopicReadcount(topic);
		if(flag == ResultUtils.SUCCESS){
			topic = topicDAO.findTopicByID(topic.getId());
			return topic;
		}else{
			topic = new Topic();
			topic.setFlag(flag);
			return topic;
		}
	}
	/**
	 * 根据id集合查询话题（包含讨论数）
	 * @Title: findTopicRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 下午12:05:14
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	public List<Topic> findTopicsByTopicIds(List<Long> topicids){
		//
		List<Topic> topics = topicDAO.findTopicsByIds(topicids);
		if(topics!=null && topics.size()>0){
			//
			if(topics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				List<Topic> topictalkcount = topicDAO.findTopicTalkCount(topicids);//查询话题的总讨论数
				//赋值话题的总讨论数
				if(topictalkcount!=null && topictalkcount.size()>0){
					for(int i=0;i<topics.size();i++){
						Topic topic = topics.get(i);
						for(int j=0;j<topictalkcount.size();j++){
							Topic info = topictalkcount.get(j);
							if(topic.getId()==info.getId()){
								topic.setTalkcount(info.getTalkcount());
								break;
							}
						}
					}
				}
			}else{
				//查询出错
				return topics;
			}
		}else{
			//没有查询出符合条件的话题
			topics = new ArrayList<Topic>();
		}
		return topics;
	}
	/**
	 * 根据讨论数查询话题排行榜
	 * @Title: findTopicRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 下午12:05:14
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	public List<Topic> findTopicRanking(long starttime,long endtime,long start){
		int pagesize = 20;
		int cha1 = 0;
		
		//需要先查询人工干预的排行的话题
		List<Topic> scoretopics = topicDAO.findTopicsOrderbyScore(start, pagesize);
		if(scoretopics!=null && scoretopics.size()>0){
			//
			if(scoretopics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				cha1=pagesize - scoretopics.size();
				
				//需要根据话题id查询总讨论数
				List<Long> topicids = new ArrayList<Long>();
				for(int i=0;i<scoretopics.size();i++){
					Topic topic = scoretopics.get(i);
					topicids.add(topic.getId());
				}
				List<Topic> topictalkcount = topicDAO.findTopicTalkCount(topicids);//查询话题的总讨论数
				//赋值话题的总讨论数
				if(topictalkcount!=null && topictalkcount.size()>0){
					for(int i=0;i<scoretopics.size();i++){
						Topic scoretopic = scoretopics.get(i);
						for(int j=0;j<topictalkcount.size();j++){
							Topic info = topictalkcount.get(j);
							if(scoretopic.getId()==info.getId()){
								scoretopic.setTalkcount(info.getTalkcount());
								break;
							}
						}
					}
				}
			}else{
				//查询出错
				return scoretopics;
			}
		}else{
			//没有查询出符合条件的话题
			cha1 = pagesize;
			scoretopics = new ArrayList<Topic>();
		}
		
		if(cha1>0){
			List<Topic> scoreTopicIds = topicDAO.findScoreTopicIds();
			long scorecount = 0;
			
			List<Long> scoretopicids = null;
			if(scoreTopicIds!=null && scoreTopicIds.size()>0 && scoreTopicIds.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				scorecount = scoreTopicIds.size();
				scoretopicids = new ArrayList<Long>(scoreTopicIds.size());
				for(int i=0;i<scoreTopicIds.size();i++){
					scoretopicids.add(scoreTopicIds.get(i).getId());
				}
			}else{
				scoretopicids = new ArrayList<Long>(0);
			}
			
			start = start-scorecount;
			if(start<0){
				start = 0;
			}
			List<Topic> topics = topicDAO.findTopicRankingExceptTopicids(scoretopicids, starttime, endtime, start, cha1);
			
			int cha = 0;
			int flag = 0;
			if(topics!=null && topics.size()>0){
				if(topics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
					cha = cha1 - topics.size();
					flag = 1;
				}else{
					//查询出错
					return topics;
				}
			}else{
				//没有规定时间的讨论话题,需要查询所有的
				cha = cha1;
				flag = 1;
				topics = new ArrayList<Topic>();
			}
			
			//需要根据话题id查询出话题列表
			List<Long> topicids = new ArrayList<Long>();
			for(int i=0;i<topics.size();i++){
				Topic topic = topics.get(i);
				topicids.add(topic.getId());
			}
			List<Topic> topicinfos = topicDAO.findTopicsByIds(topicids);//查询话题的详细信息
			if(topicinfos!=null && topicinfos.size()==1 && topicinfos.get(0).getFlag()==ResultUtils.QUERY_ERROR){
				//出错了
				return topicinfos;
			}else{
				List<Topic> topictalkcount = topicDAO.findTopicTalkCount(topicids);//查询话题的总讨论数
				for(int i=0;i<topics.size();i++){
					Topic topic = topics.get(i);
					int have = 0;
					//赋值话题详细信息
					for(int j=0;j<topicinfos.size();j++){
						Topic info = topicinfos.get(j);
						if(topic.getId()==info.getId()){
							topic.setCover(info.getCover());
							topic.setCreateDate(info.getCreateDate());
							topic.setDescription(info.getDescription());
							topic.setTitle(info.getTitle());
							topic.setUserid(info.getUserid());
							topic.setReadcount(info.getReadcount());
							topic.setFalsereading(info.getFalsereading());
							have = 1;
							break;
						}
					}
					if(have==0){
						//没有话题详情，需要移除
						topics.remove(i);
						i--;
						continue;
					}
					//赋值话题的总讨论数
					if(topictalkcount!=null && topictalkcount.size()>0){
						for(int j=0;j<topictalkcount.size();j++){
							Topic info = topictalkcount.get(j);
							if(topic.getId()==info.getId()){
								topic.setTalkcount(info.getTalkcount());
								break;
							}
						}
					}
				}
				
				
				if(cha>0){
					//差的话题数量,需要按总得讨论数查询剩下的
					//Map<String, Object> map = topicDAO.findTopicRankingCount(starttime, endtime);
					//long count = (Long) map.get("count");
					List<Topic> idtopics = topicDAO.findTopicRankingAll(starttime, endtime);
					
					long count = 0;
					List<Long> notusedtopicids = null;
					if(idtopics!=null && idtopics.size()>0 && idtopics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
						count = idtopics.size();
						notusedtopicids = new ArrayList<Long>(idtopics.size());
						for(int i=0;i<idtopics.size();i++){
							notusedtopicids.add(idtopics.get(i).getId());
						}
					}else{
						notusedtopicids = new ArrayList<Long>(0);
					}
					if(scoretopicids!=null && scoretopicids.size()>0){
						notusedtopicids.addAll(scoretopicids);
					}
					start = start-count;
					if(start<0){
						start = 0;
					}
					List<Topic> alltalkcounttopics = topicDAO.findTopicRankingByAllTalkCount(notusedtopicids,start,cha);
					if(alltalkcounttopics!=null && alltalkcounttopics.size()>0 && alltalkcounttopics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
						//需要根据话题id查询出话题列表
						List<Long> alltalkcounttopicids = new ArrayList<Long>();
						for(int i=0;i<alltalkcounttopics.size();i++){
							Topic topic = alltalkcounttopics.get(i);
							alltalkcounttopicids.add(topic.getId());
						}
						
						List<Topic> alltalkcounttopicinfos = topicDAO.findTopicsByIds(alltalkcounttopicids);//查询话题的详细信息
						
						for(int i=0;i<alltalkcounttopics.size();i++){
							Topic topic = alltalkcounttopics.get(i);
							//赋值话题详细信息
							for(int j=0;j<alltalkcounttopicinfos.size();j++){
								Topic info = alltalkcounttopicinfos.get(j);
								if(topic.getId()==info.getId()){
									topic.setCover(info.getCover());
									topic.setCreateDate(info.getCreateDate());
									topic.setDescription(info.getDescription());
									topic.setTitle(info.getTitle());
									topic.setUserid(info.getUserid());
									topic.setReadcount(info.getReadcount());
									topic.setFalsereading(info.getFalsereading());
									break;
								}
							}
						}
						
						topics.addAll(alltalkcounttopics);
					}
				}
				scoretopics.addAll(topics);
				return scoretopics;
			}
		}else{
			//不差话题数量
			return scoretopics;
		}
	}
	
	/**
	 * 根据讨论数查询话题排行榜---所有时间的
	 * @Title: findTopicRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 下午12:05:14
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	public List<Topic> findTopicRankingAllTime(long start,int pagesize){
		//int pagesize = 20;
		int cha1 = 0;
		
		//需要先查询人工干预的排行的话题
		List<Topic> scoretopics = topicDAO.findTopicsOrderbyAllScore(start, pagesize);
		if(scoretopics!=null && scoretopics.size()>0){
			//
			if(scoretopics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				cha1=pagesize - scoretopics.size();
				
				//需要根据话题id查询总讨论数
				List<Long> topicids = new ArrayList<Long>();
				for(int i=0;i<scoretopics.size();i++){
					Topic topic = scoretopics.get(i);
					topicids.add(topic.getId());
				}
				List<Topic> topictalkcount = topicDAO.findTopicTalkCount(topicids);//查询话题的总讨论数
				//赋值话题的总讨论数
				if(topictalkcount!=null && topictalkcount.size()>0){
					for(int i=0;i<scoretopics.size();i++){
						Topic scoretopic = scoretopics.get(i);
						for(int j=0;j<topictalkcount.size();j++){
							Topic info = topictalkcount.get(j);
							if(scoretopic.getId()==info.getId()){
								scoretopic.setTalkcount(info.getTalkcount());
								break;
							}
						}
					}
				}
			}else{
				//查询出错
				return scoretopics;
			}
		}else{
			//没有查询出符合条件的话题
			cha1 = pagesize;
			scoretopics = new ArrayList<Topic>();
		}
		
		if(cha1>0){
			List<Topic> scoreTopicIds = topicDAO.findAllScoreTopicIds();
			long scorecount = 0;
			
			List<Long> scoretopicids = null;
			if(scoreTopicIds!=null && scoreTopicIds.size()>0 && scoreTopicIds.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				scorecount = scoreTopicIds.size();
				scoretopicids = new ArrayList<Long>(scoreTopicIds.size());
				for(int i=0;i<scoreTopicIds.size();i++){
					scoretopicids.add(scoreTopicIds.get(i).getId());
				}
			}else{
				scoretopicids = new ArrayList<Long>(0);
			}
			
			start = start-scorecount;
			if(start<0){
				start = 0;
			}
			
			List<Topic> alltalkcounttopics = topicDAO.findTopicRankingByAllTalkCount(scoretopicids,start,cha1);
			if(alltalkcounttopics!=null && alltalkcounttopics.size()>0 && alltalkcounttopics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				//需要根据话题id查询出话题列表
				List<Long> alltalkcounttopicids = new ArrayList<Long>();
				for(int i=0;i<alltalkcounttopics.size();i++){
					Topic topic = alltalkcounttopics.get(i);
					alltalkcounttopicids.add(topic.getId());
				}
				
				List<Topic> alltalkcounttopicinfos = topicDAO.findTopicsByIds(alltalkcounttopicids);//查询话题的详细信息
				
				for(int i=0;i<alltalkcounttopics.size();i++){
					Topic topic = alltalkcounttopics.get(i);
					int have = 0;
					//赋值话题详细信息
					for(int j=0;j<alltalkcounttopicinfos.size();j++){
						Topic info = alltalkcounttopicinfos.get(j);
						if(topic.getId()==info.getId()){
							topic.setCover(info.getCover());
							topic.setCreateDate(info.getCreateDate());
							topic.setDescription(info.getDescription());
							topic.setTitle(info.getTitle());
							topic.setUserid(info.getUserid());
							topic.setReadcount(info.getReadcount());
							topic.setFalsereading(info.getFalsereading());
							have = 1;
							break;
						}
					}
					
					if(have==0){
						//没有话题详情，需要移除
						alltalkcounttopics.remove(i);
						i--;
					}
				}
				
				scoretopics.addAll(alltalkcounttopics);
			}
		}
		return scoretopics;
	}
	
	//增加人工干预排行前的方法
	public List<Topic> findTopicRanking3(long starttime,long endtime,long start){
		List<Topic> topics = topicDAO.findTopicRanking(starttime, endtime, start);
		int pagesize = 20;
		int cha = 0;
		int flag = 0;
		if(topics!=null && topics.size()>0){
			if(topics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				cha = pagesize - topics.size();
				flag = 1;
			}else{
				//查询出错
				return topics;
			}
		}else{
			//没有规定时间的讨论话题,需要查询所有的
			cha = pagesize;
			flag = 1;
			topics = new ArrayList<Topic>();
		}
		
		//需要根据话题id查询出话题列表
		List<Long> topicids = new ArrayList<Long>();
		for(int i=0;i<topics.size();i++){
			Topic topic = topics.get(i);
			topicids.add(topic.getId());
		}
		List<Topic> topicinfos = topicDAO.findTopicsByIds(topicids);//查询话题的详细信息
		if(topicinfos!=null && topicinfos.size()==1 && topicinfos.get(0).getFlag()==ResultUtils.QUERY_ERROR){
			//出错了
			return topicinfos;
		}else{
			List<Topic> topictalkcount = topicDAO.findTopicTalkCount(topicids);//查询话题的总讨论数
			for(int i=0;i<topics.size();i++){
				Topic topic = topics.get(i);
				//赋值话题详细信息
				for(int j=0;j<topicinfos.size();j++){
					Topic info = topicinfos.get(j);
					if(topic.getId()==info.getId()){
						topic.setCover(info.getCover());
						topic.setCreateDate(info.getCreateDate());
						topic.setDescription(info.getDescription());
						topic.setTitle(info.getTitle());
						topic.setUserid(info.getUserid());
						topic.setReadcount(info.getReadcount());
						topic.setFalsereading(info.getFalsereading());
						break;
					}
				}
				//赋值话题的总讨论数
				if(topictalkcount!=null && topictalkcount.size()>0){
					for(int j=0;j<topictalkcount.size();j++){
						Topic info = topictalkcount.get(j);
						if(topic.getId()==info.getId()){
							topic.setTalkcount(info.getTalkcount());
							break;
						}
					}
				}
			}
			
			
			if(cha>0){
				//差的话题数量,需要按总得讨论数查询剩下的
				//Map<String, Object> map = topicDAO.findTopicRankingCount(starttime, endtime);
				//long count = (Long) map.get("count");
				List<Topic> idtopics = topicDAO.findTopicRankingAll(starttime, endtime);
				long count = 0;
				List<Long> notusedtopicids = null;
				if(idtopics!=null && idtopics.size()>0 && idtopics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
					count = idtopics.size();
					notusedtopicids = new ArrayList<Long>(idtopics.size());
					for(int i=0;i<idtopics.size();i++){
						notusedtopicids.add(idtopics.get(i).getId());
					}
				}else{
					notusedtopicids = new ArrayList<Long>(0);
				}
				start = start-count;
				if(start<0){
					start = 0;
				}
				List<Topic> alltalkcounttopics = topicDAO.findTopicRankingByAllTalkCount(notusedtopicids,start,cha);
				if(alltalkcounttopics!=null && alltalkcounttopics.size()>0 && alltalkcounttopics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
					//需要根据话题id查询出话题列表
					List<Long> alltalkcounttopicids = new ArrayList<Long>();
					for(int i=0;i<alltalkcounttopics.size();i++){
						Topic topic = alltalkcounttopics.get(i);
						alltalkcounttopicids.add(topic.getId());
					}
					
					List<Topic> alltalkcounttopicinfos = topicDAO.findTopicsByIds(alltalkcounttopicids);//查询话题的详细信息
					
					for(int i=0;i<alltalkcounttopics.size();i++){
						Topic topic = alltalkcounttopics.get(i);
						//赋值话题详细信息
						for(int j=0;j<alltalkcounttopicinfos.size();j++){
							Topic info = alltalkcounttopicinfos.get(j);
							if(topic.getId()==info.getId()){
								topic.setCover(info.getCover());
								topic.setCreateDate(info.getCreateDate());
								topic.setDescription(info.getDescription());
								topic.setTitle(info.getTitle());
								topic.setUserid(info.getUserid());
								topic.setReadcount(info.getReadcount());
								topic.setFalsereading(info.getFalsereading());
								break;
							}
						}
					}
					
					topics.addAll(alltalkcounttopics);
				}
			}
			
			return topics;
		}
	}
	
	/**
	 * 根据讨论数查询话题排行榜
	 * @Title: findTopicRanking 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-5-14 下午12:05:14
	 * @param @param starttime
	 * @param @param endtime
	 * @param @param start
	 * @param @return
	 * @return List<Topic>
	 * @throws
	 */
	public List<Topic> findTopicRanking2(long starttime,long endtime,long start){
		List<Topic> topics = topicDAO.findTopicRanking(starttime, endtime, start);
		if(topics!=null && topics.size()>0){
			if(topics.get(0).getFlag()!=ResultUtils.QUERY_ERROR){
				//需要根据话题id查询出话题列表
				List<Long> topicids = new ArrayList<Long>();
				for(int i=0;i<topics.size();i++){
					Topic topic = topics.get(i);
					topicids.add(topic.getId());
				}
				List<Topic> topicinfos = topicDAO.findTopicsByIds(topicids);//查询话题的详细信息
				if(topicinfos!=null && topicinfos.size()==1 && topicinfos.get(0).getFlag()==ResultUtils.QUERY_ERROR){
					//出错了
					return topicinfos;
				}else{
					List<Topic> topictalkcount = topicDAO.findTopicTalkCount(topicids);//查询话题的总讨论数
					for(int i=0;i<topics.size();i++){
						Topic topic = topics.get(i);
						//赋值话题详细信息
						for(int j=0;j<topicinfos.size();j++){
							Topic info = topicinfos.get(j);
							if(topic.getId()==info.getId()){
								topic.setCover(info.getCover());
								topic.setCreateDate(info.getCreateDate());
								topic.setDescription(info.getDescription());
								topic.setTitle(info.getTitle());
								topic.setUserid(info.getUserid());
								topic.setReadcount(info.getReadcount());
								topic.setFalsereading(info.getFalsereading());
								break;
							}
						}
						//赋值话题的总讨论数
						if(topictalkcount!=null && topictalkcount.size()>0){
							for(int j=0;j<topictalkcount.size();j++){
								Topic info = topictalkcount.get(j);
								if(topic.getId()==info.getId()){
									topic.setTalkcount(info.getTalkcount());
									break;
								}
							}
						}
					}
					return topics;
				}
			}else{
				//查询出错
				return topics;
			}
		}else{
			topics = new ArrayList<Topic>();
			return topics;
		}
	}
	
	
	/**
	 * 
	 * 方法的描述 :此方法的作用是关联一个话题与资源
	 * @param bookLink
	 * @return
	 */
	public TopicLink addTopicLink(TopicLink topicLink){
		int flag = topicLinkDAO.addTopicLink(topicLink);
		if(flag == ResultUtils.SUCCESS){
			return topicLinkDAO.findTopicIsExist(topicLink);
		}else{
			topicLink = new TopicLink();
			topicLink.setFlag(flag);
			return topicLink;
		}
	}
	/**
	 * 根据话题id查询话题关联列表
	 * @Title: findTopicLinkInfoByUserid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-15 下午6:03:46
	 * @param @param topicid
	 * @param @param userid
	 * @param @param pageSize
	 * @param @return
	 * @return List<TopicLink>
	 * @throws
	 */
	public List<TopicLink> findTopicLinkInfoByTopicid(long topicid,Integer isOperation,Long resId,Integer pageSize){
		return topicLinkDAO.findTopicLinkInfoByTopicid(topicid,isOperation,resId, pageSize);
	}
	/**
	 * 
	 * 方法的描述 :根据话题id和资源id查询记录
	 * @param topicLink
	 * @return
	 */
	public TopicLink findTopicIsExist(TopicLink topicLink){
		return topicLinkDAO.findTopicIsExist(topicLink);
	}
	/**
	 * 
	 * 方法的描述 :删除一个话题关联
	 * @param mvLink
	 * @return
	 */
	public TopicLink deleteTopicLink(TopicLink topicLink){
		TopicLink tk = topicLinkDAO.findTopicLinkById(topicLink.getId());
		int flag = topicLinkDAO.deleteTopicLink(topicLink);
		if(flag == ResultUtils.SUCCESS){
			tk.setIsDel(1);
			return tk;
		}
		topicLink.setFlag(flag);
		return topicLink;
	}
	/**
	 * 根据资源id更新最新更新时间
	 * @param topicLink
	 * @return
	 */
	public int updateTopicLinkLatestrevisiondateByResid(TopicLink topicLink){
		return topicLinkDAO.updateTopicLinkLatestrevisiondateByResid(topicLink);
	}
	/**
	 * 根据资源id和用户id删除一个话题关联
	 * @Title: deleteTopicLinkByResid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-22 下午4:50:04
	 * @param @param topicLink
	 * @param @return
	 * @return int
	 * @throws
	 */
	public boolean deleteTopicLinkByResid(TopicLink topicLink){
		try{
			int flag = topicLinkDAO.deleteTopicLinkByResid(topicLink);
			if(flag == ResultUtils.SUCCESS){
				return true;
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 
	 * 方法的描述 :增加赞的数量
	 * @param topicLink
	 * @return
	 */
	public TopicLink addTopicLinkPraisecount(TopicLink topicLink){
		int flag = topicLinkDAO.addTopicLinkPraisecount(topicLink);
		if(flag == ResultUtils.SUCCESS){
			topicLink = topicLinkDAO.findTopicLinkById(topicLink.getId());
			return topicLink;
		}else{
			topicLink = new TopicLink();
			topicLink.setFlag(flag);
			return topicLink;
		}
	}
	/**
	 * 
	 * 方法的描述 :增加评论的数量
	 * @param topicLink
	 * @return
	 */
	public TopicLink addTopicLinkCommentcount(TopicLink topicLink){
		int flag = topicLinkDAO.addTopicLinkCommentcount(topicLink);
		if(flag == ResultUtils.SUCCESS){
			topicLink = topicLinkDAO.findTopicLinkById(topicLink.getId());
			return topicLink;
		}else{
			topicLink = new TopicLink();
			topicLink.setFlag(flag);
			return topicLink;
		}
	}
	/**
	 * 
	 * 方法的描述 :查询单个关联信息根据id
	 * @param id
	 * @return
	 */
	public TopicLink findTopicLinkById(long id){
		return topicLinkDAO.findTopicLinkById(id);
	}
	/**
	 * 根据话题id查询话题相关联的数量
	 * topicid
	 */
	public long getTopicLinkCountByTopicId(long topicid){
		return topicLinkDAO.getTopicLinkCountByTopicId(topicid);
	}
	/**
	 * 根据话题id查询某个话题关联列表按赞的数量排序
	 * @Title: findTopicLinkInfoByUserid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-15 下午6:03:46
	 * @param @param topicid
	 * @param @param userid
	 * @param @param pageSize
	 * @param @return
	 * @return List<TopicLink>
	 * @throws
	 */
	public List<TopicLink> findTopicLinkByTopicidOrderbyPraisecount(long topicid,Integer pageSize){
		return topicLinkDAO.findTopicLinkByTopicidOrderbyPraisecount(topicid, pageSize);
	}
	/**
	 * 根据话题id查询某个话题关联列表按评论的数量排序
	 * @Title: findTopicLinkInfoByUserid 
	 * @Description: TODO
	 * @author Administrator
	 * @date 2015-4-15 下午6:03:46
	 * @param @param topicid
	 * @param @param userid
	 * @param @param pageSize
	 * @param @return
	 * @return List<TopicLink>
	 * @throws
	 */
	public List<TopicLink> findTopicLinkByTopicidOrderbyCommentcount(long topicid,Integer pageSize){
		return topicLinkDAO.findTopicLinkByTopicidOrderbyCommentcount(topicid, pageSize);
	}
}
