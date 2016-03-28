package com.poison.eagle.manager;

import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.utils.*;
import com.poison.resource.client.ArticleFacade;
import com.poison.resource.client.BkCommentFacade;
import com.poison.resource.client.DiaryFacade;
import com.poison.resource.client.MvCommentFacade;
import com.poison.resource.model.Article;
import com.poison.resource.model.BkComment;
import com.poison.resource.model.Diary;
import com.poison.resource.model.MvComment;
import com.poison.store.client.MvFacade;
import com.poison.store.model.MvInfo;
import com.poison.store.model.OnlineRead;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/12/4
 * Time: 15:07
 */
public class ResourceRelationManager extends BaseManager{

    private static final Log LOG = LogFactory.getLog(ResourceRelationManager.class);

    private MvFacade mvFacade;
    private SearchApiManager searchApiManager;
    private ResourceManager resourceManager;
    private MvCommentFacade mvCommentFacade;
    private BkCommentFacade bkCommentFacade;
    private DiaryFacade diaryFacade;
    private ArticleFacade articleFacade;

    public void setArticleFacade(ArticleFacade articleFacade) {
        this.articleFacade = articleFacade;
    }

    public void setDiaryFacade(DiaryFacade diaryFacade) {
        this.diaryFacade = diaryFacade;
    }

    public void setBkCommentFacade(BkCommentFacade bkCommentFacade) {
        this.bkCommentFacade = bkCommentFacade;
    }

    public void setMvCommentFacade(MvCommentFacade mvCommentFacade) {
        this.mvCommentFacade = mvCommentFacade;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setSearchApiManager(SearchApiManager searchApiManager) {
        this.searchApiManager = searchApiManager;
    }

    public void setMvFacade(MvFacade mvFacade) {
        this.mvFacade = mvFacade;
    }

    public String viewRelationReading(String reqs,Long uid){
//		LOG.info("客户端json数据："+reqs);
        Map<String, Object> req ;
        Map<String, Object> dataq;
        Map<String, Object> res ;
        Map<String, Object> datas ;
        String resString;//返回数据
        String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
        String error;
        Long id = 0l;
        String type = "";
        String tag = "";
        int page = 1;
        //去掉空格
        reqs = reqs.trim();

        //转化成可读类型
        try {
            req = getObjectMapper().readValue(reqs,  new TypeReference<Map<String, Object>>(){});
            req = (Map<String, Object>) req.get("req");
            dataq = (Map<String, Object>) req.get("data");
            
            String idstr = dataq.get("id")+"";
            
            if(StringUtils.isInteger(idstr)){
            	id = Long.valueOf(idstr);
            }
            
            try {
                type = (String) dataq.get("type");
            } catch (Exception e) {
                type = "";
            }
            if(type==null){
            	type="";
            }
            tag = (String) dataq.get("tag");
            String pagestr = (String) dataq.get("page");
            if(StringUtils.isInteger(pagestr)){
            	page = Integer.valueOf(pagestr);
            }
            if(page<1){
            	page = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RES_DATA_NOTGET;
        }
        
        int size = 10;
        long start = PageUtils.getRecordStart(size, page);
        
        String tags = "";
//		System.out.println(req);
        long resId = 0l;
        String resType = "";
        List<String> tagList = new ArrayList<String>();
        List<ResourceInfo> resultList = new ArrayList<ResourceInfo>();
        if(type.equals(CommentUtils.TYPE_DIARY)){//当为日志时
            Diary diary = diaryFacade.queryByIdDiary(id);
            tags = diary.getTag();
            if(tagList!=null && tagList.size()>0){
            	tags = tagList.get(0);
            }
        }
        else if(type.equals(CommentUtils.TYPE_BOOK_COMMENT)||type.equals(CommentUtils.TYPE_ARTICLE_BOOK)){//当为书评时
            BkComment bkComment = bkCommentFacade.findCommentIsExistById(id);
            tags = bkComment.getTag();
            if(tagList!=null && tagList.size()>0){
            	tags = tagList.get(0);
            }
        }
        else if(type.equals(CommentUtils.TYPE_MOVIE_COMMENT)||type.equals(CommentUtils.TYPE_ARTICLE_MOVIE)){//当为影评是
            MvComment mvComment = mvCommentFacade.findMvCommentIsExist(id);
            tags = mvComment.getTag();
            //tagList = CheckParams.putStringToList(tags);
            if(tagList!=null && tagList.size()>0){
            	tags = tagList.get(0);
            }
        }
        else if(type.equals(CommentUtils.TYPE_MOVIE)){//当为电影时
            MvInfo mvInfo = mvFacade.queryById(id);
            tags = mvInfo.getTags();
            if(tagList!=null && tagList.size()>0){
            	tags = tagList.get(0);
            }
        }
        else if(type.equals(CommentUtils.TYPE_NEWARTICLE)){//当为长文章时
            Article article = articleFacade.queryArticleById(id);
            tags = article.getTag();
            if(tagList!=null && tagList.size()>0){
            	tags = tagList.get(0);
            }
        }
        
        if(tag!=null && tag.trim().length()>0){
        	if(tags!=null && tags.trim().length()>0){
        		tags = tags+","+tag;
        	}else{
        		tags = tag;
        	}
        }
        
        if(tags!=null && tags.trim().length()>0){
        	String typestr = "article,bookcomment,moviecomment";
        	Map<String,Object> map = searchApiManager.resourceSearchByTag(null, tags, typestr, (int)start, size,null);
            List<Map<String,Object>> subjects = (ArrayList)map.get("subjects");
            Iterator<Map<String,Object>> subjectsIt = subjects.iterator();
            while(subjectsIt.hasNext()){
                Map<String,Object> subjectMap = subjectsIt.next();
                resId = Long.valueOf((String)subjectMap.get("id"));
                resType = (String)subjectMap.get("type");
                ResourceInfo resourceInfo = resourceManager.getResourceByIdAndType(resId, resType, uid);
                resultList.add(resourceInfo);
            }
        }

        int flagint = ResultUtils.SUCCESS;

        //数据不存在属于正常情况，也要返回正确
        if(flagint == ResultUtils.DATAISNULL){
            flagint = ResultUtils.SUCCESS;
        }
        datas = new HashMap<String, Object>();
        if(flagint == ResultUtils.SUCCESS){
            flag = CommentUtils.RES_FLAG_SUCCESS;
            datas.put("list", resultList);
        }else{
            flag = CommentUtils.RES_FLAG_ERROR;
            error = MessageUtils.getResultMessage(flagint);
            LOG.error("错误代号:"+flagint+",错误信息:"+error);
            datas.put("error", error);
        }
        datas.put("flag", flag);
        //处理返回数据
        resString = getResponseData(datas);
//		System.out.println(resString);
        return resString;
    }
}
