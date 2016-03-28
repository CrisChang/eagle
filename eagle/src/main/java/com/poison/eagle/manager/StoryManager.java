package com.poison.eagle.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.util.StringUtils;

import com.poison.eagle.entity.BannerInfo;
import com.poison.eagle.entity.StoryChapterInfo;
import com.poison.eagle.entity.StoryInfo;
import com.poison.eagle.utils.*;
import com.poison.product.client.AccGoldFacade;
import com.poison.story.client.StoryFacade;
import com.poison.story.client.StoryStatisticFacade;
import com.poison.story.model.Story;
import com.poison.story.model.StoryChapter;
import com.poison.story.model.StoryEnumerate;
import com.poison.story.model.StoryPay;
import com.poison.story.model.StoryPromote;
import com.poison.story.model.StoryShelf;
import com.poison.story.model.StoryStatistic;
import com.poison.ucenter.client.AuthorFacade;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.Author;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/11/27
 * Time: 16:06
 */
public class StoryManager extends BaseManager{

    private static final Log LOG = LogFactory.getLog(StoryManager.class);

    private StoryFacade storyFacade;
    private StoryStatisticFacade storyStatisticFacade;
    private UcenterFacade ucenterFacade;
    private SearchApiManager searchApiManager;
    private AccGoldFacade accGoldFacade;
    private AuthorFacade authorFacade;
    //男频
    private List<StoryEnumerate> maleStoryEnumerate;
    //女频
    private List<StoryEnumerate> femaleStoryEnumerate;
    //排行榜
    private List<StoryEnumerate> rangkingStoryEnumerate;
    //排行榜女频
    private List<StoryEnumerate> femaleRangkingStoryEnumerate;

    public void setAuthorFacade(AuthorFacade authorFacade) {
        this.authorFacade = authorFacade;
    }

    public void setSearchApiManager(SearchApiManager searchApiManager) {
        this.searchApiManager = searchApiManager;
    }

    public void setUcenterFacade(UcenterFacade ucenterFacade) {
        this.ucenterFacade = ucenterFacade;
    }

    public void setStoryStatisticFacade(StoryStatisticFacade storyStatisticFacade) {
        this.storyStatisticFacade = storyStatisticFacade;
    }

    public void setStoryFacade(StoryFacade storyFacade) {
        this.storyFacade = storyFacade;
    }

    public void setAccGoldFacade(AccGoldFacade accGoldFacade) {
		this.accGoldFacade = accGoldFacade;
	}

	public String viewStoryDetail(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";

        Long id = null;

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            id = Long.valueOf(dataq.get("id").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        Story story = storyFacade.findStoryById(id);

        StoryInfo storyInfo = putStoryToInfo(story,uid);

        int flagint = Integer.valueOf(story.getFlag());

        datas = new HashMap<String, Object>();
        if(flagint == ResultUtils.SUCCESS){
            //增加点击量
            storyStatisticFacade.addClickRateByResourceId(story.getId(),story.getChannel(),story.getType());
            flag = CommentUtils.RES_FLAG_SUCCESS;
            datas.put("storyInfo", storyInfo);
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(flagint);
            LOG.error("错误代号:"+flagint+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }


    public String viewStoryChapterList(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";

        Long id = null;
        Long lastId = null;
        String lastIdStr = "";

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            id = Long.valueOf(dataq.get("id").toString());
            lastIdStr = (String)dataq.get("lastId");
            if(null==lastIdStr||"".equals(lastIdStr)){
                lastId = null;
            }else{
                lastId = Long.valueOf(lastIdStr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int flagint = 0;//Integer.valueOf(story.getFlag());

        List<StoryChapter> chapterList = storyFacade.findChapterListByStoryId(id,lastId);
        List<StoryChapterInfo> chapterInfoList = null;
        if(null!=chapterList){
            flagint = ResultUtils.SUCCESS;
            chapterInfoList = new ArrayList<StoryChapterInfo>(chapterList.size());
            List<Long> chapterids = new ArrayList<Long>(chapterInfoList.size());//章节id集合，用于查询是否付费
            for(int i=0;i<chapterList.size();i++){
            	if(chapterList.get(i).getIsPay()==1){
            		chapterids.add(chapterList.get(i).getId());
            	}
            }
            //根据章节id集合查询章节付费状态
            List<StoryPay> storyPays = storyFacade.findStoryPaysByChapterIds(chapterids, uid);
            if(storyPays!=null && storyPays.size()==1 && (ResultUtils.QUERY_ERROR+"").equals(storyPays.get(0).getFlag())){
            	flagint = ResultUtils.QUERY_ERROR;
            }else{
            	for(int i=0;i<chapterList.size();i++){
            		int payed = 0;//默认未支付
            		if(storyPays!=null && storyPays.size()>0){
            			for(int j=0;j<storyPays.size();j++){
            				//遇到支付的再中断循环，怕同一个章节有多个不同付费状态的付费信息
                			if(storyPays.get(j).getChapterId()==chapterList.get(i).getId() && storyPays.get(j).getPayed()==1){
                				payed = 1;
                				break;
                			}
                		}
            		}
            		StoryChapterInfo storyChapterInfo = putStoryChapterToStoryChapterInfo(chapterList.get(i), payed);
            		chapterInfoList.add(storyChapterInfo);
            	}
            }
        }else{
        	chapterInfoList = new ArrayList<StoryChapterInfo>(0);
        }
        datas = new HashMap<String, Object>();
        if(flagint == ResultUtils.SUCCESS){
            flag = CommentUtils.RES_FLAG_SUCCESS;
            datas.put("chapterList", chapterInfoList);
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(flagint);
            LOG.error("错误代号:"+flagint+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    public String viewStoryChapterContent(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";

        Long id = null;
        Long lastId = null;
        String idStr = "";

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {


            id = Long.valueOf(reqs);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //int flagint = 0;//Integer.valueOf(story.getFlag());
        
        String resultContent=null;
        boolean allow = false;
        
        datas = new HashMap<String, Object>();
        
        StoryChapter storyChapter = storyFacade.findStoryChapterById(id);
        if(storyChapter!=null && storyChapter.getId()>0){
        	if(storyChapter.getIsPay()==0){
        		//免费的
        		allow = true;
        	}else{
        		//需要付费的
        		StoryPay storyPay = storyFacade.findStoryPayByChapterId(id,uid);
        		if(storyPay!=null && storyPay.getId()>0 && storyPay.getPayed()==1){
        			//已经支付了
        			allow = true;
        		}else{
        			//没有支付，需要付款
        			
        			//Map<String,Object> map = new HashMap<String,Object>(1);
        			//map.put("price", storyChapter.getPrice());
        			//AccGold accGold = accGoldFacade.findAccGoldByUserId(uid);
        			//long accAmount = 0;
        			//if(accGold!=null){
        				//accAmount = accGold.getGoldamount();
        			//}
        			//map.put("accAmount", accAmount);
        			//map.put("wordNum", storyChapter.getWordNumber());
        			//flag = "2";//需要付费
        			flag = CommentUtils.RES_FLAG_ERROR;
        			datas.put("flag", flag);
        			//datas.put("map",map);
        			datas.put("error", "需要付费");
        			resultContent = getResponseData(datas);
        		}
        	}
        }else if(storyChapter==null || (ResultUtils.DATAISNULL+"").equals(storyChapter.getFlag())){
        	//没有小说章节内容
        	//datas = new HashMap<String, Object>();
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("flag", flag);
			datas.put("error", "要查询的小说章节不存在");
			resultContent = getResponseData(datas);
        }else{
        	//查询出错
        	//返回null还是错误信息？？？？？？？？？
        	//datas = new HashMap<String, Object>();
			flag = CommentUtils.RES_FLAG_ERROR;
			datas.put("flag", flag);
			datas.put("error", "查询小说章节信息出错");
			resultContent = getResponseData(datas);
        }
        if(allow){
			String introduce = storyChapter.getIntroduce();
			String name = storyChapter.getName();
        	resultContent = storyFacade.findChapterContent(id);
        	resultContent = name+"\n\n\n"+resultContent+"\n\n\n"+introduce;
        	//String content = storyFacade.findChapterContent(id);
        	//content = content+ "\n\n\n"+introduce;
			//flag = CommentUtils.RES_FLAG_SUCCESS;
			//datas.put("flag", flag);
			//Map<String,Object> map = new HashMap<String,Object>(1);
			//map.put("content", content);
			//map.put("id", id);
			//datas.put("map", map);
        }
        
//        datas = new HashMap<String, Object>();
//        if(flagint == ResultUtils.SUCCESS){
//            flag = CommentUtils.RES_FLAG_SUCCESS;
//            datas.put("chapterList", chapterList);
//        }else{
//            flag = CommentUtils.RES_FLAG_ERROR;
//            error = MessageUtils.getResultMessage(flagint);
//            LOG.error("错误代号:"+flagint+",错误信息:"+error);
//            datas.put("error", error);
//        }
//        datas.put("flag", flag);
//        //处理返回数据
//        resString = getResponseData(datas);
        
        //resultContent = getResponseData(datas);
        return resultContent;
    }

    /**
     * 查询书架
     * @param reqs
     * @param uid
     * @return
     */
    public String viewStoryShelf(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";

        Long id = null;
        Long lastId = null;

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            id = Long.valueOf(dataq.get("id").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        int flagint = 0;//Integer.valueOf(story.getFlag());

        List<StoryShelf> shelfList = storyFacade.findMyShelfByUserId(id);

        List<StoryInfo> storyList = new ArrayList<StoryInfo>();
        if(null!=shelfList){
            flagint = ResultUtils.SUCCESS;
            Iterator<StoryShelf> shelfListIt = shelfList.iterator();
            while(shelfListIt.hasNext()){
                StoryShelf storyShelf = shelfListIt.next();
                if(0==storyShelf.getId()){
                    continue;
                }
                Story story = storyFacade.findStoryById(storyShelf.getStoryId());
                if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                    storyList.add(putStoryToInfo(story,uid));
                }
            }
        }
        datas = new HashMap<String, Object>();
        if(flagint == ResultUtils.SUCCESS){
            flag = CommentUtils.RES_FLAG_SUCCESS;
            datas.put("shoryShelf", storyList);
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(flagint);
            LOG.error("错误代号:"+flagint+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 加入书架
     * @param reqs
     * @param uid
     * @return
     */
    public String putStoryshelf(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";

        Long id = null;
        Long lastId = null;

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            id = Long.valueOf(dataq.get("id").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Integer.valueOf(story.getFlag());

        StoryShelf storyShelf = storyFacade.insertStoryShelf(uid,id);

        int flagint = storyShelf.getFlag();
        datas = new HashMap<String, Object>();
        if(flagint == ResultUtils.SUCCESS){
            Story story = storyFacade.findStoryById(storyShelf.getStoryId());
            flag = CommentUtils.RES_FLAG_SUCCESS;
            storyStatisticFacade.addOnshelfTotalByResourceId(story.getId(),story.getChannel(),story.getType());
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(flagint);
            LOG.error("错误代号:"+flagint+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 删除书架中的书
     * @param reqs
     * @param uid
     * @return
     */
    public String removeStoryshelf(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";

        Long id = null;
        Long lastId = null;
        List idList = new ArrayList();

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            idList = (List)dataq.get("idList");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(null!=idList&&idList.size()>0){
            Iterator<String> it = idList.iterator();
            String idStr = "";
            while(it.hasNext()){
                idStr = it.next();
                storyFacade.deleteStoryShelf(Long.valueOf(idStr),uid);
            }
        }
        int flagint = ResultUtils.SUCCESS;
        datas = new HashMap<String, Object>();
        if(flagint == ResultUtils.SUCCESS){
            flag = CommentUtils.RES_FLAG_SUCCESS;
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(flagint);
            LOG.error("错误代号:"+flagint+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 查询小说列表
     * @param reqs
     * @param uid
     * @return
     */
//    public String viewStoryRanking(String reqs,Long uid){
////		LOG.info("客户端json数据："+reqs);
//        Map<String, Object> req =null;
//        Map<String, Object> dataq=null;
//        Map<String, Object> datas =null;
//        String resString="";//返回数据
//        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
//        String error="";
//
//        String sort = "";//1点击榜 2推荐榜 3运营榜
//
//        //去掉空格
//        reqs = reqs.trim();
//
//        //转化成可读类型
//        try {
//            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
////			System.out.println(req);
//            req = (Map<String, Object>) req.get("req");
//            dataq = (Map<String, Object>) req.get("data");
//
//            sort = CheckParams.objectToStr((String)dataq.get("sort"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        int flagint = 0;//Integer.valueOf(story.getFlag());
//
//        List<StoryShelf> shelfList = storyFacade.findMyShelfByType(sort);
//
//        List<StoryInfo> storyList = new ArrayList<StoryInfo>();
//        if(null!=shelfList){
//            flagint = ResultUtils.SUCCESS;
//            Iterator<StoryShelf> shelfListIt = shelfList.iterator();
//            while(shelfListIt.hasNext()){
//                StoryShelf storyShelf = shelfListIt.next();
//                if(0==storyShelf.getId()){
//                    continue;
//                }
//                Story story = storyFacade.findStoryById(storyShelf.getStoryId());
//                storyList.add(putStoryToInfo(story));
//            }
//        }
//        datas = new HashMap<String, Object>();
//        if(flagint == ResultUtils.SUCCESS){
//            flag = CommentUtils.RES_FLAG_SUCCESS;
//            datas.put("shoryShelf", storyList);
//        }else{
//            flag = CommentUtils.RES_FLAG_ERROR;
//            error = MessageUtils.getResultMessage(flagint);
//            LOG.error("错误代号:"+flagint+",错误信息:"+error);
//            datas.put("error", error);
//        }
//        datas.put("flag", flag);
//        //处理返回数据
//        resString = getResponseData(datas);
//
//        return resString;
//    }

    /**
     * 查询点击量或者推荐的排行榜
     * @param reqs
     * @param uid
     * @return
     */
    public String viewStoryRanking(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error="";

        String caseType = "";//1点击榜 2推荐榜 3运营榜
        int page= 1;//页数
        int pageSize = PageUtils.getPageSize("m_story_ranking_size");
        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            caseType = CheckParams.objectToStr((String)dataq.get("caseType"));
            page = CheckParams.objectToInt((String)dataq.get("page"));
            pageSize = StringUtils.isEmpty(dataq.get("pageSize"))?pageSize:Integer.parseInt((String)dataq.get("pageSize"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int flagint = 0;//Integer.valueOf(story.getFlag());

        List<StoryInfo> storyList = new ArrayList<StoryInfo>();
        List<BannerInfo> bannerList = new ArrayList<BannerInfo>();
        List<String> keyWordList = new ArrayList<String>();
        datas = new HashMap<String, Object>();

        if(caseType.equals("2")||caseType.equals("3")||caseType.equals("4")||caseType.equals("5")||caseType.equals("9")||caseType.equals("200")||caseType.equals("201")||caseType.equals("202")||caseType.equals("203")){
        	StoryPromote promote = new StoryPromote();
        	promote.setType(caseType);
			//编辑推荐的时候 3 重磅推荐 ，4:优秀新作品，5:VIP精选作品 6:精选Banner 7：书架运营位 9：火热推荐
            //200 女性精品  201 女生爱看  202女生火热连载   203优秀新作
//            List<StoryShelf> shelfList = storyFacade.findMyShelfByType(caseType,(page - 1) * 10, 10);
            List<StoryPromote> promoteList = storyFacade.findStoryPromoteByCondition(promote, PageUtils.getRecordStart(pageSize, page), pageSize);

            if(null!=promoteList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryPromote> shelfListIt = promoteList.iterator();
                while(shelfListIt.hasNext()){
                    StoryPromote storyShelf = shelfListIt.next();
                    if(0==storyShelf.getId()){
                        continue;
                    }
                    //查看小说的信息
                    Story story = storyFacade.findStoryById(storyShelf.getStoryId());
                    if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                        storyList.add(putStoryToInfo(story,uid));
                    }
                }
            }
        }
//        else if(caseType.equals("7")){//书架广告位
//            List<StoryShelf> shelfList = storyFacade.findMyShelfByType(caseType,(page - 1) * 10, 10);
//
//            if(null!=shelfList){
//                flagint = ResultUtils.SUCCESS;
//                Iterator<StoryShelf> shelfListIt = shelfList.iterator();
//                while(shelfListIt.hasNext()){
//                    StoryShelf storyShelf = shelfListIt.next();
//                    if(0==storyShelf.getId()){
//                        continue;
//                    }
//                    BannerInfo bannerInfo = putStoryShelfToBannerInfo(storyShelf);
//                    bannerList.add(bannerInfo);
//                }
//            }
//            datas.put("banner", bannerList);
//        }
        else if(caseType.equals("6")||caseType.equals("7")||caseType.equals("10")){//banner地址 6男频banner 7书架广告位 10女频banner
        	StoryPromote promote = new StoryPromote();
        	promote.setType(caseType);
//            List<StoryShelf> shelfList = storyFacade.findMyShelfByType(caseType,(page - 1) * 10, 10);
            List<StoryPromote> promoteList = storyFacade.findStoryPromoteByCondition(promote, PageUtils.getRecordStart(pageSize, page), pageSize);

            if(null!=promoteList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryPromote> promoteListIt = promoteList.iterator();
                while(promoteListIt.hasNext()){
                    StoryPromote storyShelf = promoteListIt.next();
                    if(0==storyShelf.getId()){
                        continue;
                    }       
                    BannerInfo bannerInfo = putStoryPromote2BannerInfo(storyShelf);
                    bannerList.add(bannerInfo);
                }
            }
            datas.put("banner", bannerList);
        }else if(caseType.equals("8")){//大家都在搜
        	StoryPromote promote = new StoryPromote();
        	promote.setType(caseType);
        	List<StoryPromote> shelfList = storyFacade.findStoryPromoteByCondition(promote, PageUtils.getRecordStart(pageSize, page),page);
//            List<StoryShelf> shelfList = storyFacade.findMyShelfByType(caseType,(page - 1) * 10, 10);
            if(null!=shelfList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryPromote> shelfListIt = shelfList.iterator();
                while(shelfListIt.hasNext()){
                    StoryPromote storyShelf = shelfListIt.next();
                    if(0==storyShelf.getId()){
                        continue;
                    }
                    String tempStr = storyShelf.getPromoteWords();
                    keyWordList.add(tempStr);
                }
            }
            datas.put("keyword", keyWordList);
        }
        else if(caseType.equals("100")){//点击量
            List<StoryStatistic> statisticList = storyStatisticFacade.findStatisticByClickRate((page - 1) * 10, 10,"1");

            if(null!=statisticList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryStatistic> statisticListIt = statisticList.iterator();
                while(statisticListIt.hasNext()){
                    StoryStatistic storyStatistic = statisticListIt.next();
                    if(0==storyStatistic.getId()){
                        continue;
                    }
                    Story story = storyFacade.findStoryById(storyStatistic.getResourceId());
                    if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                        storyList.add(putStoryToInfo(story,storyStatistic,uid));
                    }
                }
            }
        }else if(caseType.equals("101")){//推荐榜
            List<StoryStatistic> statisticList = storyStatisticFacade.findStatisticByReommentVote((page - 1) * 10, 10,"1");

            if(null!=statisticList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryStatistic> statisticListIt = statisticList.iterator();
                while(statisticListIt.hasNext()){
                    StoryStatistic storyStatistic = statisticListIt.next();
                    if(0==storyStatistic.getId()){
                        continue;
                    }
                    Story story = storyFacade.findStoryById(storyStatistic.getResourceId());
                    if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                        storyList.add(putStoryToInfo(story,storyStatistic,uid));
                    }
                }
            }
        }else if(caseType.equals("102")){//畅销榜
            List<StoryStatistic> statisticList = storyStatisticFacade.findStatisticByPayTotal((page - 1) * 10, 10,"1");

            if(null!=statisticList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryStatistic> statisticListIt = statisticList.iterator();
                while(statisticListIt.hasNext()){
                    StoryStatistic storyStatistic = statisticListIt.next();
                    if(0==storyStatistic.getId()){
                        continue;
                    }
                    Story story = storyFacade.findStoryById(storyStatistic.getResourceId());
                    if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                        storyList.add(putStoryToInfo(story,storyStatistic,uid));
                    }
                }
            }
        }else if(caseType.equals("103")){//收藏榜
            List<StoryStatistic> statisticList = storyStatisticFacade.findStatisticByOnshelfTotal((page - 1) * 10, 10,"1");

            if(null!=statisticList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryStatistic> statisticListIt = statisticList.iterator();
                while(statisticListIt.hasNext()){
                    StoryStatistic storyStatistic = statisticListIt.next();
                    if(0==storyStatistic.getId()){
                        continue;
                    }
                    Story story = storyFacade.findStoryById(storyStatistic.getResourceId());
                    if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                        storyList.add(putStoryToInfo(story,storyStatistic,uid));
                    }
                }
            }
        }else if(caseType.equals("300")){//女生人气榜
            List<StoryStatistic> statisticList = storyStatisticFacade.findStatisticByClickRate((page - 1) * 10, 10, "2");

            if(null!=statisticList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryStatistic> statisticListIt = statisticList.iterator();
                while(statisticListIt.hasNext()){
                    StoryStatistic storyStatistic = statisticListIt.next();
                    if(0==storyStatistic.getId()){
                        continue;
                    }
                    Story story = storyFacade.findStoryById(storyStatistic.getResourceId());
                    if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                        storyList.add(putStoryToInfo(story,storyStatistic,uid));
                    }
                }
            }
        }else if(caseType.equals("301")){//女生畅销榜
            List<StoryStatistic> statisticList = storyStatisticFacade.findStatisticByPayTotal((page - 1) * 10, 10,"2");

            if(null!=statisticList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryStatistic> statisticListIt = statisticList.iterator();
                while(statisticListIt.hasNext()){
                    StoryStatistic storyStatistic = statisticListIt.next();
                    if(0==storyStatistic.getId()){
                        continue;
                    }
                    Story story = storyFacade.findStoryById(storyStatistic.getResourceId());
                    if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                        storyList.add(putStoryToInfo(story,storyStatistic,uid));
                    }
                }
            }
        }else if(caseType.equals("302")){//女生收藏榜
            List<StoryStatistic> statisticList = storyStatisticFacade.findStatisticByOnshelfTotal((page - 1) * 10, 10,"2");

            if(null!=statisticList){
                flagint = ResultUtils.SUCCESS;
                Iterator<StoryStatistic> statisticListIt = statisticList.iterator();
                while(statisticListIt.hasNext()){
                    StoryStatistic storyStatistic = statisticListIt.next();
                    if(0==storyStatistic.getId()){
                        continue;
                    }
                    Story story = storyFacade.findStoryById(storyStatistic.getResourceId());
                    if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                        storyList.add(putStoryToInfo(story,storyStatistic,uid));
                    }
                }
            }
        }

        if(flagint == ResultUtils.SUCCESS){
            flag = CommentUtils.RES_FLAG_SUCCESS;
            datas.put("rangking", storyList);
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(flagint);
            LOG.error("错误代号:"+flagint+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 查询枚举类型
     * @param reqs
     * @param uid
     * @return
     */
    public String viewStoryEnumerate(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req =null;
        Map<String, Object> dataq=null;
        Map<String, Object> datas =null;
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_SUCCESS;//0：成功、1：失败
        String error="";

        String channel = "";//1男频 2女频 0全部
        int page= 1;//页数

        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
//			System.out.println(req);
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");

            channel = CheckParams.objectToStr((String) dataq.get("channel"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<StoryEnumerate> allList = new ArrayList<StoryEnumerate>();

        if(null==maleStoryEnumerate||maleStoryEnumerate.size()==0){
            maleStoryEnumerate = new ArrayList<StoryEnumerate>();
            maleStoryEnumerate = storyStatisticFacade.findStoryEnumerateByChanel("1");
        }
        if(null==femaleStoryEnumerate||femaleStoryEnumerate.size()==0){
            femaleStoryEnumerate = new ArrayList<StoryEnumerate>();
            femaleStoryEnumerate = storyStatisticFacade.findStoryEnumerateByChanel("2");
        }
        if(null==rangkingStoryEnumerate||rangkingStoryEnumerate.size()==0){
            rangkingStoryEnumerate = new ArrayList<StoryEnumerate>();
            rangkingStoryEnumerate = storyStatisticFacade.findStoryEnumerateByChanel("3");
        }
        if(null==femaleRangkingStoryEnumerate||femaleRangkingStoryEnumerate.size()==0){
            femaleRangkingStoryEnumerate = new ArrayList<StoryEnumerate>();
            femaleRangkingStoryEnumerate = storyStatisticFacade.findStoryEnumerateByChanel("4");
        }

        datas = new HashMap<String, Object>();
        if("0".equals(channel)){//全部
            allList.addAll(maleStoryEnumerate);
            allList.addAll(femaleStoryEnumerate);
            datas.put("channel",allList);
            //datas.put("femaleChannel",femaleStoryEnumerate);
        }else if("1".equals(channel)){//精选男频
            datas.put("channel",maleStoryEnumerate);
        }else if("2".equals(channel)){//精选女频
            datas.put("channel",femaleStoryEnumerate);
        }else if("3".equals(channel)){//排行榜男频
            datas.put("channel",rangkingStoryEnumerate);
        }else if("4".equals(channel)){//排行榜女频
            datas.put("channel",femaleRangkingStoryEnumerate);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 搜索小说
     * @return
     */
    public String searchStory(String keyWord,String fields,String tags,String type,int pageNum,int num){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> datas =new HashMap<String, Object>();
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_SUCCESS;//0：成功、1：失败
        String error="";

        List<StoryInfo> storyList = new ArrayList<StoryInfo>();
        //转化成可读类型
        try {
            Map<String,Object> map = new HashMap<String, Object>();

            if(null!=keyWord){//全部搜索
                map = searchApiManager.storySearchByCommon(keyWord,null,null,null,null,null,pageNum,num);
            }else if(null!=type){//搜索类型
                map = searchApiManager.storySearchByCommon(null,null,type,null,null,null,pageNum,num);
            }else if(null!=tags){//搜索标签
                map = searchApiManager.storySearchByCommon(null,null,null,tags,null,null,pageNum,num);
            }

            List<Map<String, Object>> retList = (ArrayList<Map<String,Object>>)map.get("subjects");
            Iterator<Map<String,Object>> retListIt = retList.listIterator();
            long id = 0;
            while(retListIt.hasNext()){
                Map<String,Object> retmap = retListIt.next();
                if(null==retmap||retmap.isEmpty()){
                    continue;
                }
                id = Long.valueOf((String)retmap.get("id"));
                Story story = storyFacade.findStoryById(id);
                if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                    storyList.add(putStoryToInfo(story));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        datas.put("flag", flag);
        datas.put("story",storyList);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }

    /**
     * 随机搜索小说
     * @param keyWord
     * @param num
     * @return
     */
    public String searchRandomStory(String keyWord,int num){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> datas =new HashMap<String, Object>();
        String resString="";//返回数据
        String flag = CommentUtils.RES_FLAG_SUCCESS;//0：成功、1：失败
        String error="";

        List<StoryInfo> storyList = new ArrayList<StoryInfo>();
        //转化成可读类型
        try {
            Map<String,Object> map = searchApiManager.storyRandomSearchByTags(keyWord,num);

            List<Map<String, Object>> retList = (ArrayList<Map<String,Object>>)map.get("subjects");
            Iterator<Map<String,Object>> retListIt = retList.listIterator();
            long id = 0;
            while(retListIt.hasNext()){
                Map<String,Object> retmap = retListIt.next();
                if(null==retmap||retmap.isEmpty()){
                    continue;
                }
                id = Long.valueOf((String)retmap.get("id"));
                Story story = storyFacade.findStoryById(id);
                if(story.getFlag().equals(ResultUtils.SUCCESS+"")){
                    storyList.add(putStoryToInfo(story));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        datas.put("flag", flag);
        datas.put("story",storyList);
        //处理返回数据
        resString = getResponseData(datas);

        return resString;
    }


    public StoryInfo putStoryToInfo(Story story){
        StoryInfo storyInfo = new StoryInfo();
        storyInfo.setId(story.getId());
        storyInfo.setUserId(story.getUserId());
        storyInfo.setName(story.getName());
        //作者信息
        Author author = authorFacade.findAuthorInfo(story.getUserId());
        storyInfo.setAuthor(author.getName());
//        UserInfo userInfo = ucenterFacade.findUserInfoByUserId(null, story.getUserId());
//
//        storyInfo.setAuthor(userInfo.getName());

        storyInfo.setCover(story.getCover());
        storyInfo.setIntroduce(story.getIntroduce());
        storyInfo.setRecommendedIntroduce(story.getRecommendIntroduce());
        storyInfo.setTag(story.getTag());
        storyInfo.setType(story.getType());
        storyInfo.setWordNumber(story.getWordNumber());
        storyInfo.setState(story.getState());
        storyInfo.setLatestChapter(story.getLatestChapter());
        storyInfo.setCreateDate(story.getCreateDate());
        storyInfo.setLatestRevisionDate(story.getLatestChapterTime());
        storyInfo.setIsPay(story.getIsPay());
        //章节
        Map<String,Object> chapterCountMap = storyFacade.findStoryChapterCount(story.getId());
        storyInfo.setChapterNumber((Integer)chapterCountMap.get("count"));
        //加入月票等相关信息
        storyInfo.setMonthTicket(1);
        StoryStatistic storyStatistic = storyStatisticFacade.findStatisitcByResId(story.getId());
        storyInfo.setRecommendTicket((int)storyStatistic.getRecommentVoteOperation());
        storyInfo.setClickRate((int)storyStatistic.getClickRateOperation());

        //加入是否加入书架等附加信息
//        if(storyFacade.isOnShelf(story.getUserId(),story.getId())){
//            storyInfo.setIsOnShelf(1);
//        }else{
//            storyInfo.setIsOnShelf(0);
//        }

        return storyInfo;
    }

    public StoryInfo putStoryToInfo(Story story,long userId){
        StoryInfo storyInfo = new StoryInfo();
        storyInfo.setId(story.getId());
        storyInfo.setUserId(story.getUserId());
        storyInfo.setName(story.getName());
        //作者信息
        Author author = authorFacade.findAuthorInfo(story.getUserId());
        storyInfo.setAuthor(author.getName());
//        UserInfo userInfo = ucenterFacade.findUserInfoByUserId(null, story.getUserId());
//        storyInfo.setAuthor(userInfo.getName());

        storyInfo.setCover(story.getCover());
        storyInfo.setIntroduce(story.getIntroduce());
        storyInfo.setRecommendedIntroduce(story.getRecommendIntroduce());
        storyInfo.setTag(story.getTag());
        storyInfo.setType(story.getType());
        storyInfo.setWordNumber(story.getWordNumber());
        storyInfo.setState(story.getState());
        storyInfo.setLatestChapter(story.getLatestChapter());
        storyInfo.setCreateDate(story.getCreateDate());
        storyInfo.setLatestRevisionDate(story.getLatestChapterTime());
        storyInfo.setIsPay(story.getIsPay());
        //章节
        Map<String,Object> chapterCountMap = storyFacade.findStoryChapterCount(story.getId());
        storyInfo.setChapterNumber((Integer) chapterCountMap.get("count"));
        //加入月票等相关信息
        storyInfo.setMonthTicket(1);
        StoryStatistic storyStatistic = storyStatisticFacade.findStatisitcByResId(story.getId());
        storyInfo.setRecommendTicket((int)storyStatistic.getRecommentVoteOperation());
        storyInfo.setClickRate((int)storyStatistic.getClickRateOperation());

        //加入是否加入书架等附加信息
        if(storyFacade.isOnShelf(userId,story.getId())){
            storyInfo.setIsOnShelf(0);
        }else{
            storyInfo.setIsOnShelf(1);
        }

        return storyInfo;
    }

    /**
     * 加入推荐票等相关信息
     * @param story
     * @param storyStatistic
     * @return
     */
    public StoryInfo putStoryToInfo(Story story,StoryStatistic storyStatistic,long userId){
        StoryInfo storyInfo = new StoryInfo();
        storyInfo.setId(story.getId());
        storyInfo.setUserId(story.getUserId());
        storyInfo.setName(story.getName());
        //作者信息
        Author author = authorFacade.findAuthorInfo(story.getUserId());
        storyInfo.setAuthor(author.getName());
//        UserInfo userInfo = ucenterFacade.findUserInfoByUserId(null, story.getUserId());
//        storyInfo.setAuthor(userInfo.getName());

        storyInfo.setCover(story.getCover());
        storyInfo.setIntroduce(story.getIntroduce());
        storyInfo.setRecommendedIntroduce(story.getRecommendIntroduce());
        storyInfo.setTag(story.getTag());
        storyInfo.setWordNumber(story.getWordNumber());
        storyInfo.setState(story.getState());
        storyInfo.setIsPay(story.getIsPay());
        storyInfo.setLatestChapter(story.getLatestChapter());
        storyInfo.setCreateDate(story.getCreateDate());
        storyInfo.setLatestRevisionDate(story.getLatestChapterTime());
        //章节
        Map<String,Object> chapterCountMap = storyFacade.findStoryChapterCount(story.getId());
        storyInfo.setChapterNumber((Integer)chapterCountMap.get("count"));
        //加入月票等相关信息
        storyInfo.setMonthTicket(1);
        storyInfo.setRecommendTicket((int)storyStatistic.getRecommentVoteOperation());
        storyInfo.setClickRate((int)storyStatistic.getClickRateOperation());

        //加入是否加入书架等附加信息
        //加入是否加入书架等附加信息
        if(storyFacade.isOnShelf(userId,story.getId())){
            storyInfo.setIsOnShelf(0);
        }else{
            storyInfo.setIsOnShelf(1);
        }
        return storyInfo;
    }

    /**
     * 改为banner信息
     * @param storyShelf
     * @return
     */
    public BannerInfo putStoryShelfToBannerInfo(StoryShelf storyShelf){
        BannerInfo bannerInfo = new BannerInfo();
        bannerInfo.setId(storyShelf.getId());
        bannerInfo.setStoryId(storyShelf.getStoryId());
        bannerInfo.setCover(storyShelf.getCover());
        bannerInfo.setOtherUrl(storyShelf.getOtherUrl());
        bannerInfo.setIntroduce(storyShelf.getIntroduce());
        return bannerInfo;
    }
    
    /**
     * 把小说运营信息转换为Banner信息
     * @param storyPromote
     * @return
     */
    private BannerInfo putStoryPromote2BannerInfo(StoryPromote storyPromote){
    	BannerInfo bannerInfo = new BannerInfo();
    	bannerInfo.setId(storyPromote.getId());
    	bannerInfo.setStoryId(storyPromote.getStoryId());
    	bannerInfo.setCover(storyPromote.getPromoteCover());
    	bannerInfo.setOtherUrl(storyPromote.getCoverLink());
    	bannerInfo.setIntroduce(storyPromote.getPromoteWords());
    	return bannerInfo;
    }

    //将章节类转化为包装类
    public StoryChapterInfo putStoryChapterToStoryChapterInfo(StoryChapter storyChapter,int payed){
    	StoryChapterInfo storyChapterInfo = new StoryChapterInfo();
    	if(storyChapter!=null && storyChapter.getId()>0){
    		storyChapterInfo.setId(storyChapter.getId());
    		storyChapterInfo.setIsPay(storyChapter.getIsPay());
    		storyChapterInfo.setCreateDate(storyChapter.getCreateDate());
    		storyChapterInfo.setLatestRevisionDate(storyChapter.getLatestRevisionDate());
    		storyChapterInfo.setName(storyChapter.getName());
    		storyChapterInfo.setPayed(payed);
    		storyChapterInfo.setPrice(storyChapter.getPrice());
    		storyChapterInfo.setRange(storyChapter.getRange());
    		storyChapterInfo.setStoryId(storyChapter.getStoryId());
    		storyChapterInfo.setUserId(storyChapter.getUserId());
    		storyChapterInfo.setWordNumber(storyChapter.getWordNumber());
    		storyChapterInfo.setIntroduce(storyChapter.getIntroduce());
    	}
        return storyChapterInfo;
    }


}
