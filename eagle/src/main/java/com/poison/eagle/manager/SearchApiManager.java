package com.poison.eagle.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.keel.framework.runtime.ProductContextHolder;
import com.keel.framework.web.WebProductContextHolder;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.UserEntity;
import com.poison.eagle.manager.util.SensitiveWordManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.resource.client.MyMovieFacade;
import com.poison.resource.model.MvListLink;
import com.poison.store.client.MvFacade;
import com.poison.store.model.MvInfo;

public class SearchApiManager {
	private static final  Log LOG = LogFactory.getLog(SearchApiManager.class);
	
	private Client esClient;
	
	private SensitiveWordManager sensitiveWordManager;
	
	private MyMovieFacade myMovieFacade;
	
	private MvFacade mvFacade;
	
	private MovieUtils movieUtils = MovieUtils.getInstance();

	public void setSensitiveWordManager(SensitiveWordManager sensitiveWordManager) {
		this.sensitiveWordManager = sensitiveWordManager;
	}

	public void setEsClient(Client esClient) {
		this.esClient = esClient;
	}
	
	public void setMyMovieFacade(MyMovieFacade myMovieFacade) {
		this.myMovieFacade = myMovieFacade;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}

	/**用户通用搜索*/
	public List<UserEntity> userSearchByName(String name, int from, int num) {
		String index = "user";
		String [] types = {"user_info"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(StringUtils.isNotBlank(name)){
            String [] fields = {"user_name"};
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(name, fields));
        }

        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        List<UserEntity> retList = new ArrayList<UserEntity>();
        for (int i = 0; i < hits.length; i++) {
            
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();

            UserEntity entity = new UserEntity();
            entity.setId(Long.valueOf( String.valueOf(resultTemp.get("user_id"))));
            entity.setNickName((String) resultTemp.get("user_name"));
            entity.setSex(resultTemp.get("sex") == null ? "" : (String) resultTemp.get("sex"));
            entity.setFace_url(resultTemp.get("face_address") == null ? "" : (String) resultTemp.get("face_address"));
            entity.setLevel((Integer) resultTemp.get("level"));
            entity.setType((Integer) resultTemp.get("level"));
            entity.setSign(resultTemp.get("affective_states") == null ? "" : (String)resultTemp.get("affective_states"));
            entity.setProfession(resultTemp.get("profession") == null ? "" : (String) resultTemp.get("profession"));

            //检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		entity.getNickName(), entity.getProfession(), entity.getSign()
            		}))){
            	continue;
            }

            retList.add(entity);
        }
        
		return retList;
	}
	
	/**用户通用搜索（内部）*/
	public Map<String, Object> userSearchByCommon(String keyWord, String fieldsStr, int from, int num) {
		String index = "user";
		String [] types = {"user_info"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
            String [] fields = {"user_name"};
            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }

        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();

            newResultTemp.put("user_id", Long.valueOf( String.valueOf(resultTemp.get("user_id"))));
            newResultTemp.put("id", String.valueOf(resultTemp.get("user_id")));
            newResultTemp.put("type",CommentUtils.TYPE_USER);
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)resultTemp.get("user_name")
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);
        
        return retJson;
	}
	
	/**用户通用搜索（外部）*/
	public String userSearchByCommonOuter(String keyWord, String fieldsStr, int from, int num) {
		
		Map<String, Object> retJson = this.userSearchByCommon(keyWord, fieldsStr, from, num);
		
		if(null == retJson) {
			return "";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**用户附近资源搜索（外部）*/
	public String eventSearchByLocationOuter(String lon, String lat, int radius, int from, int num){
		Map<String, Object> retJson = this.eventSearchByLocation(lon, lat, radius, from, num);
		
		if(null == retJson) {
			return "";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**用户附近资源搜索（内部）*/
	public Map<String, Object> eventSearchByLocation(String lon, String lat, int radius, int from, int num){
		String index = "resource";
		String [] types = {"bookcomment","moviecomment","diary"};
		
		Double indexLon = null;
		Double indexLat = null;
		if (StringUtils.isBlank(lon) || StringUtils.isBlank(lat)) {
			return null;
		} else {
			indexLon = NumberUtils.createDouble(lon);
			indexLat = NumberUtils.createDouble(lat);
		}
		
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());

        GeoDistanceFilterBuilder geoFilter = FilterBuilders.geoDistanceFilter("poi").distance(radius, DistanceUnit.METERS);
        geoFilter = geoFilter.lat(indexLat).lon(indexLon);
        searchRequestBuilder.setPostFilter(geoFilter);
        searchRequestBuilder.addSort(SortBuilders.geoDistanceSort("poi").point(indexLat, indexLon).order(SortOrder.ASC).sortMode("min"));
        
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        DecimalFormat df = new DecimalFormat("0");
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();

            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("addTime", resultTemp.get("addTime") == null ? "" : String.valueOf(resultTemp.get("addTime")));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));
			if(null != resultTemp.get("poi")){
				String[] latAndLon = StringUtils.split((String)(resultTemp.get("poi")), ',');
				newResultTemp.put("distance", df.format(GeoDistance.PLANE.calculate(NumberUtils.createDouble(latAndLon[0]), NumberUtils.createDouble(latAndLon[1]), indexLat, indexLon, DistanceUnit.METERS)));
			}

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);
        
        return retJson;
	}
	
	
	/**用户动态搜索（外部）*/
	public String eventSearchByUserIdOuter(long userId, String keyWord, int from, int num) {
		Map<String, Object> retJson = this.eventSearchByUserId(userId, keyWord, from, num);
		
		if(null == retJson) {
			return "";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**用户动态搜索（内部）*/
	public Map<String, Object> eventSearchByUserId(long userId, String keyWord, int from, int num) {
		String index = "resource";
		String [] types = {"booklist","movielist","article","bookcomment","moviecomment","diary"};

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(0 < userId){
            boolQueryBuilder.must(QueryBuilders.termQuery("u_id", userId));
        }
        if(StringUtils.isNotBlank(keyWord)){
        	String [] fields = new String[]{"filmlist_name", "booklist_name", "reason", "content", "summary", "description", "comment", "name", "title"};

            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }
        
        searchRequestBuilder.addSort("addTime", SortOrder.DESC).addSort("_score", SortOrder.DESC);

        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();

            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("addTime", resultTemp.get("addTime") == null ? "" : String.valueOf(resultTemp.get("addTime")));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);
        
        return retJson;
	}
	
	/**话题通用搜索*/
	public String topicSearchByCommon(String keyWord, String fieldsStr, String tags, int from, int num) {
		String index = "resource";
		String [] types = {"topic"};

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
        	String [] fields = new String[]{"title", "description", "tags_a"};

            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(tags, ',')));
        }

        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();

            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            //newResultTemp.put("addTime", String.valueOf(resultTemp.get("addTime")));
            //newResultTemp.put("uid", String.valueOf(resultTemp.get("u_id")));
            newResultTemp.put("nickname", resultTemp.get("u_name") == null ? "" : String.valueOf(resultTemp.get("u_name")));
            newResultTemp.put("title", resultTemp.get("title") == null ? "" : String.valueOf(resultTemp.get("title")));
            newResultTemp.put("cover", resultTemp.get("cover") == null ? "" : String.valueOf(resultTemp.get("cover")));
            newResultTemp.put("description", resultTemp.get("description") == null ? "" : StringUtils.left(String.valueOf(resultTemp.get("description")), 100));

            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("nickname"), (String)newResultTemp.get("title"), (String)newResultTemp.get("description")
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**书籍通用搜索*/
	public String bookSearchByCommon(String keyWord, String fieldsStr, String tags, String set, int from, int num) {
		String index = "store";
		String [] types = {"book", "netbook"};

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
        	String [] fields = null;
            if(StringUtils.equalsIgnoreCase(set, "new")){
            	fields = new String[]{"name"};
            } else {
            	fields = new String[]{"name", "authorName", "introduction", "tags"};
            }
             
            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            
            if(StringUtils.equalsIgnoreCase(set, "new")){
            	boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("name", keyWord));
            } else {
            	boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
            }
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags", StringUtils.split(tags, ',')));
        }
        /*fun 3*/
        if(StringUtils.equalsIgnoreCase(set, "new")){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("isOperation", new String[] {"book_3", "netbook_3"}));
        }

        String sessionID = ProductContextHolder.getProductContext().getSessionId();
        if(StringUtils.isNotBlank(sessionID)){
            searchRequestBuilder.setPreference(sessionID);
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            if(StringUtils.equalsIgnoreCase(set, "new")){
            	String tempName = StringUtils.substringBefore((String) resultTemp.get("name"), " ");
            	if(StringUtils.isBlank(tempName)){
            		break;
            	}
            	if (!StringUtils.equalsIgnoreCase(tempName, keyWord)){
                    continue;
            	}
            }

            //newResultTemp.put("publisher", (String) resultTemp.get("press"));
            //newResultTemp.put("pubdate", (String) resultTemp.get("publishTime"));
            newResultTemp.put("name", (String) resultTemp.get("name"));
            //newResultTemp.put("authorName", StringUtils.split(((String) resultTemp.get("authorName")), ','));
            newResultTemp.put("authorName", (String) resultTemp.get("authorName"));
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("pagePic", CheckParams.matcherBookPic((String) resultTemp.get("pagePic")));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));
            if(StringUtils.equals(hit.getType(), "book")) {
            	newResultTemp.put("press", resultTemp.get("press") == null ? "" : (String) resultTemp.get("press"));
            } else if(StringUtils.equals(hit.getType(), "netbook")) {
            	newResultTemp.put("press", "网络文学");
            }
            if(StringUtils.equals(hit.getType(), "book")) {
            	newResultTemp.put("publishTime", resultTemp.get("publishTime") == null ? "" : (String) resultTemp.get("publishTime"));
            } else if(StringUtils.equals(hit.getType(), "netbook")) {
            	newResultTemp.put("publishTime", "");
            }
            
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name"), (String)newResultTemp.get("authorName"),
            		(String)newResultTemp.get("press")
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
            
            if(StringUtils.equalsIgnoreCase(set, "new")){
            	break;
            }
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        if(StringUtils.equalsIgnoreCase(set, "new")){
            retJson.put("count", retList.size());
            retJson.put("start", from);
            retJson.put("total", retList.size());
        } else {
            retJson.put("count", hits.length);
            retJson.put("start", from);
            retJson.put("total", searchHits.getTotalHits());
        }
        retJson.put("books", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}

	/**电影通用搜索*/
	public String movieSearchByCommon(String keyWord, String fieldsStr,
			String tags, String set, int from, int num) {
		String index = "store";
		String [] types = {"movie"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
        	String [] fields = null;
            if(StringUtils.equalsIgnoreCase(set, "new")){
            	fields = new String[]{"name"};;
            } else {
            	fields = new String[]{"name", "alias", "director", "screenwriter", "actor", "describe", "tags"};
            }

            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            
            if(StringUtils.equalsIgnoreCase(set, "new")){
            	boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("name", keyWord));
            } else {
            	boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
            }
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags", StringUtils.split(tags, ',')));
        }
        /*fun 3*/
        if(StringUtils.equalsIgnoreCase(set, "new")){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("isOperation", "movie_3"));
        }

        String sessionID = ProductContextHolder.getProductContext().getSessionId();
        if(StringUtils.isNotBlank(sessionID)){
            searchRequestBuilder.setPreference(sessionID);
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            if(StringUtils.equalsIgnoreCase(set, "new")){
            	String tempName = StringUtils.substringBefore((String) resultTemp.get("name"), " ");
            	if (!(StringUtils.isNotBlank(tempName) && StringUtils.equalsIgnoreCase(tempName, keyWord))){
                    break;
            	}
            }
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("name"));
            newResultTemp.put("moviePic", CheckParams.matcherMoviePic((String) resultTemp.get("moviePic")));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));
            newResultTemp.put("director", resultTemp.get("director") == null ? new ArrayList<String>() : (List<String>) resultTemp.get("director"));
            newResultTemp.put("actor", resultTemp.get("actor") == null ? new ArrayList<String>() : (List<String>) resultTemp.get("actor"));
            newResultTemp.put("releaseDate", resultTemp.get("releaseDate") == null ? "" : (String) resultTemp.get("releaseDate"));
            newResultTemp.put("score", String.valueOf(resultTemp.get("score")));
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name"), ((ArrayList<String>)newResultTemp.get("director")).toString(),
            		((ArrayList<String>)newResultTemp.get("actor")).toString()
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
            
            if(StringUtils.equalsIgnoreCase(set, "new")){
            	break;
            }
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        if(StringUtils.equalsIgnoreCase(set, "new")){
            retJson.put("count", retList.size());
            retJson.put("start", from);
            retJson.put("total", retList.size());
        } else {
            retJson.put("count", hits.length);
            retJson.put("start", from);
            retJson.put("total", searchHits.getTotalHits());
        }
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**书籍精确搜索*/
	public String bookExactSearch(String keyWord, int from, int num) {
		String index = "store";
		String [] types = {"book", "netbook"};
		
        Map<String, Object> retJson = new HashMap<String, Object>();
        Map<String, Object> exactResult = new HashMap<String, Object>();
        Map<String, Object> retPSJson = new HashMap<String, Object>();

/*------ name ------*/
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(StringUtils.isNotBlank(keyWord)){
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("name", keyWord));
        }
        searchRequestBuilder.addSort("_score", SortOrder.DESC ).addSort("score", SortOrder.DESC );
        searchRequestBuilder.setQuery(boolQueryBuilder).setFrom(0).setSize(3).setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        
        List<String> tags = null;
        String exactID = null;
        if(hits.length >= 1){
        	String tempName = StringUtils.substringBefore((String) hits[0].getSource().get("name"), " ");
        	if (StringUtils.isNotBlank(tempName) && StringUtils.equalsIgnoreCase(tempName, keyWord)){
            	Object obj = hits[0].getSource().get("tags");
            	if(null != obj){
            		tags = (List<String>) obj;
            	}
                exactID =  String.valueOf(hits[0].getSource().get("id"));
                exactResult.put("name", hits[0].getSource().get("name") == null ? "" : (String) hits[0].getSource().get("name"));
                exactResult.put("authorName", hits[0].getSource().get("authorName") == null ? "" : (String) hits[0].getSource().get("authorName"));
                exactResult.put("id", String.valueOf(hits[0].getSource().get("id")));
                exactResult.put("pagePic", hits[0].getSource().get("pagePic") == null ? "" : CheckParams.matcherBookPic((String) hits[0].getSource().get("pagePic")));
                exactResult.put("type", (String) hits[0].getSource().get("innerType"));
                if(StringUtils.equals(hits[0].getType(), "book")) {
                	exactResult.put("press", hits[0].getSource().get("press") == null ? "" : (String) hits[0].getSource().get("press"));
                } else if(StringUtils.equals(hits[0].getType(), "netbook")) {
                	exactResult.put("press", "网络文学");
                }
                
                /*检查敏感词
                if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
                		(String)exactResult.get("name"), (String)exactResult.get("authorName"),
                		(String)exactResult.get("press")
                		}))){
                	exactResult = new HashMap<String, Object>(); 
                }*/
        	}
        }
        retJson.put("exact", exactResult);
        /*----------------------------------------*/
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();

        if(null != exactID && null != tags){
            SearchRequestBuilder searchRequestBuilder2 = esClient.prepareSearch(index).setTypes(types);
            searchRequestBuilder2.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
            BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        	boolQueryBuilder2.must(QueryBuilders.termsQuery("tags", tags));
        	if(null != exactID){
        		boolQueryBuilder2.mustNot(QueryBuilders.termQuery("id", exactID));
        	}
            searchRequestBuilder2.addSort("_score", SortOrder.DESC);
            searchRequestBuilder2.setQuery(boolQueryBuilder2);
            searchRequestBuilder2.setFrom(from).setSize(num);
            searchRequestBuilder2.setExplain(true);
            response = null;
            try {
                response = searchRequestBuilder2.execute().actionGet();
            } catch (Exception ex) {
            	LOG.error(ex.getMessage(), ex.fillInStackTrace());
            	return "";
            }
            searchHits = response.getHits();
            hits = searchHits.getHits();
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setSerializationInclusion(Inclusion.NON_NULL);
            
            for (int i = 0; i < hits.length; i++) {
                
                SearchHit hit = hits[i];
                Map<String, Object> resultTemp = hit.getSource();
                Map<String, Object> newResultTemp2 = new HashMap<String, Object>();
                
                newResultTemp2.put("name", resultTemp.get("name") == null ? "" : (String) resultTemp.get("name"));
                newResultTemp2.put("authorName", resultTemp.get("authorName") == null ? "" : (String) resultTemp.get("authorName"));
                newResultTemp2.put("id", String.valueOf(resultTemp.get("id")));
                newResultTemp2.put("pagePic", resultTemp.get("pagePic") == null ? "" : CheckParams.matcherBookPic((String) resultTemp.get("pagePic")));
                newResultTemp2.put("type", (String) resultTemp.get("innerType"));
                if(StringUtils.equals(hit.getType(), "book")) {
                	newResultTemp2.put("press", resultTemp.get("press") == null ? "" : (String) resultTemp.get("press"));
                } else if(StringUtils.equals(hit.getType(), "netbook")) {
                	newResultTemp2.put("press", "网络文学");
                }
                
                /*检查敏感词
                if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
                		(String)newResultTemp2.get("name"), (String)newResultTemp2.get("authorName"),
                		(String)newResultTemp2.get("press")
                		}))){
                	continue;
                }*/
                
                retList.add(newResultTemp2);
            }
            retPSJson.put("count", hits.length);
            retPSJson.put("start", from);
            retPSJson.put("total", searchHits.getTotalHits());
            retPSJson.put("subjects", retList);
        } else if (null == exactID){
/* ------ tags ------ */
            SearchRequestBuilder searchRequestBuilder2 = esClient.prepareSearch(index).setTypes(types);
        	searchRequestBuilder2.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        	BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        	boolQueryBuilder2.must(QueryBuilders.termsQuery("tags", keyWord));
        	searchRequestBuilder2.addSort("score", SortOrder.DESC);
        	searchRequestBuilder2.setQuery(boolQueryBuilder2).setFrom(from).setSize(num).setExplain(true);
        	response = null;
        	try {
        	    response = searchRequestBuilder2.execute().actionGet();
        	} catch (Exception ex) {
        	    LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	    return "";
        	}
        	if(response.getHits().getHits().length <= 0) {
/*------ authorName ------ */
                SearchRequestBuilder searchRequestBuilder3 = esClient.prepareSearch(index).setTypes(types);
                searchRequestBuilder3.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
                BoolQueryBuilder boolQueryBuilder3 = QueryBuilders.boolQuery();
                boolQueryBuilder3.should(QueryBuilders.matchPhraseQuery("authorName", keyWord));
            	boolQueryBuilder3.minimumNumberShouldMatch(1);
            	searchRequestBuilder3.addSort("score", SortOrder.DESC);
                searchRequestBuilder3.setQuery(boolQueryBuilder3).setFrom(from).setSize(num).setExplain(true);
                response = null;
                try {
                    response = searchRequestBuilder3.execute().actionGet();
                } catch (Exception ex) {
                	LOG.error(ex.getMessage(), ex.fillInStackTrace());
                	return "";
                }
                if(response.getHits().getHits().length <= 0) {
/* ------ userTags ------ */
                    SearchRequestBuilder searchRequestBuilder4 = esClient.prepareSearch(index).setTypes(types);
                    searchRequestBuilder4.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
                    BoolQueryBuilder boolQueryBuilder4 = QueryBuilders.boolQuery();
                	boolQueryBuilder4.must(QueryBuilders.termsQuery("userTags", keyWord));
                	searchRequestBuilder4.addSort("score", SortOrder.DESC);
                    searchRequestBuilder4.setQuery(boolQueryBuilder4).setFrom(from).setSize(num).setExplain(true);
                    response = null;
                    try {
                        response = searchRequestBuilder4.execute().actionGet();
                    } catch (Exception ex) {
                    	LOG.error(ex.getMessage(), ex.fillInStackTrace());
                    	return "";
                    }
                    if(response.getHits().getHits().length <= 0){
/* ------ common ------ */
                        SearchRequestBuilder searchRequestBuilder5 = esClient.prepareSearch(index).setTypes(types);
                        searchRequestBuilder5.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
                        BoolQueryBuilder boolQueryBuilder5 = QueryBuilders.boolQuery();
                    	boolQueryBuilder5.must(QueryBuilders.multiMatchQuery(keyWord, new String[]{"name", "authorName", "introduction", "tags"}));
                    	searchRequestBuilder5.addSort("score", SortOrder.DESC);
                        searchRequestBuilder5.setQuery(boolQueryBuilder5).setFrom(from).setSize(num).setExplain(true);
                        response = null;
                        try {
                            response = searchRequestBuilder5.execute().actionGet();
                        } catch (Exception ex) {
                        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
                        	return "";
                        }
                        if(response.getHits().getHits().length <= 0){
/* ------ Nothing ------ */
                        	//return "";
                        }
                    }
                }
        	}
        	
            searchHits = response.getHits();
            hits = searchHits.getHits();
            for (int i = 0; i < hits.length; i++) {
                
                SearchHit hit = hits[i];
                Map<String, Object> resultTemp = hit.getSource();
                Map<String, Object> newResultTemp2 = new HashMap<String, Object>();
                
                newResultTemp2.put("name", resultTemp.get("name") == null ? "" : (String) resultTemp.get("name"));
                newResultTemp2.put("authorName", resultTemp.get("authorName") == null ? "" : (String) resultTemp.get("authorName"));
                newResultTemp2.put("id", String.valueOf(resultTemp.get("id")));
                newResultTemp2.put("pagePic", resultTemp.get("pagePic") == null ? "" : CheckParams.matcherBookPic((String) resultTemp.get("pagePic")));
                newResultTemp2.put("type", (String) resultTemp.get("innerType"));
                if(StringUtils.equals(hit.getType(), "book")) {
                	newResultTemp2.put("press", resultTemp.get("press") == null ? "" : (String) resultTemp.get("press"));
                } else if(StringUtils.equals(hit.getType(), "netbook")) {
                	newResultTemp2.put("press", "网络文学");
                }
                
                /*检查敏感词
                if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
                		(String)newResultTemp2.get("name"), (String)newResultTemp2.get("authorName"),
                		(String)newResultTemp2.get("press")
                		}))){
                	continue;
                }*/
                
                retList.add(newResultTemp2);
            }
            
            retPSJson.put("count", hits.length);
            retPSJson.put("start", from);
            retPSJson.put("total", searchHits.getTotalHits());
            retPSJson.put("subjects", retList);
        }

        retJson.put("ps", retPSJson);
        retJson.put("flag", "0");

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}

	/**电影精确搜索*/
	public String movieExactSearch(String keyWord, int from, int num) {
		String index = "store";
		String [] types = {"movie"};

        Map<String, Object> retJson = new HashMap<String, Object>();
        Map<String, Object> exactResult = new HashMap<String, Object>();
        Map<String, Object> retPSJson = new HashMap<String, Object>();
        
/* ------ name ------ */
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if(StringUtils.isNotBlank(keyWord)){
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("name", keyWord));
        }
        searchRequestBuilder.addSort("_score", SortOrder.DESC ).addSort("score", SortOrder.DESC );
        searchRequestBuilder.setQuery(boolQueryBuilder).setFrom(0).setSize(3).setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<String> tags = null;
        String exactID = null;
        if(hits.length >= 1){
        	String tempName = StringUtils.substringBefore((String) hits[0].getSource().get("name"), " ");
        	if (StringUtils.isNotBlank(tempName) && StringUtils.equalsIgnoreCase(tempName, keyWord)){
            	Object obj = hits[0].getSource().get("tags");
            	if(null != obj){
            		tags = (List<String>) obj;
            	}
                exactID = String.valueOf(hits[0].getSource().get("id"));
                exactResult.put("id", String.valueOf(hits[0].getSource().get("id")));
                exactResult.put("name", hits[0].getSource().get("name") == null ? "" : (String) hits[0].getSource().get("name"));
                exactResult.put("moviePic", hits[0].getSource().get("moviePic") == null ? "" : CheckParams.matcherMoviePic((String) hits[0].getSource().get("moviePic")));
                exactResult.put("type", (String) hits[0].getSource().get("innerType"));
                exactResult.put("director", hits[0].getSource().get("director") == null ? new ArrayList<String>() : (List<String>) hits[0].getSource().get("director"));
                exactResult.put("actor", hits[0].getSource().get("actor") == null ? new ArrayList<String>() : (List<String>) hits[0].getSource().get("actor"));
                exactResult.put("tags", hits[0].getSource().get("tags") == null ? new ArrayList<String>() : (List<String>) hits[0].getSource().get("tags"));
        	    
                /*检查敏感词
                if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
                		(String)exactResult.get("name"), ((ArrayList<String>)exactResult.get("director")).toString(),
                		((ArrayList<String>)exactResult.get("actor")).toString()
                		}))){
                	exactResult = new HashMap<String, Object>();
                }*/
        	}
        }
        retJson.put("exact", exactResult);
        /*--------------------------------------------*/
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();

        if(null != exactID && null != tags){
            SearchRequestBuilder searchRequestBuilder2 = esClient.prepareSearch(index).setTypes(types);
            searchRequestBuilder2.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
            BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        	boolQueryBuilder2.must(QueryBuilders.termsQuery("tags", tags));
        	if(null != exactID){
        		boolQueryBuilder2.mustNot(QueryBuilders.termQuery("id", exactID));
        	}
            searchRequestBuilder2.addSort("_score", SortOrder.DESC);
            searchRequestBuilder2.setQuery(boolQueryBuilder2);
            searchRequestBuilder2.setFrom(from).setSize(num);
            searchRequestBuilder2.setExplain(true);
            response = null;
            try {
                response = searchRequestBuilder2.execute().actionGet();
            } catch (Exception ex) {
            	LOG.error(ex.getMessage(), ex.fillInStackTrace());
            	return "";
            }
            searchHits = response.getHits();
            hits = searchHits.getHits();
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setSerializationInclusion(Inclusion.NON_NULL);
            
            for (int i = 0; i < hits.length; i++) {
                
                SearchHit hit = hits[i];
                Map<String, Object> resultTemp = hit.getSource();
                Map<String, Object> newResultTemp2 = new HashMap<String, Object>();
                
                newResultTemp2.put("id", String.valueOf(resultTemp.get("id")));
                newResultTemp2.put("name", resultTemp.get("name") == null ? "" : (String) resultTemp.get("name"));
                newResultTemp2.put("moviePic", resultTemp.get("moviePic") == null ? "" : CheckParams.matcherMoviePic((String) resultTemp.get("moviePic")));
                newResultTemp2.put("type", (String) resultTemp.get("innerType"));
                newResultTemp2.put("director", resultTemp.get("director") == null ? new ArrayList<String>() : (List<String>) resultTemp.get("director"));
                newResultTemp2.put("actor", resultTemp.get("actor") == null ? new ArrayList<String>() : (List<String>) resultTemp.get("actor"));
                newResultTemp2.put("tags", resultTemp.get("tags") == null ? new ArrayList<String>() : (List<String>) resultTemp.get("tags"));
                
                /*检查敏感词
                if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
                		(String)newResultTemp2.get("name"), ((ArrayList<String>)newResultTemp2.get("director")).toString(),
                		((ArrayList<String>)newResultTemp2.get("actor")).toString()
                		}))){
                	continue;
                }*/
                
                retList.add(newResultTemp2);
            }
            
            retPSJson.put("count", hits.length);
            retPSJson.put("start", from);
            retPSJson.put("total", searchHits.getTotalHits());
            retPSJson.put("subjects", retList);
        } else if (null == exactID){
/* ------ tags ------ */
            SearchRequestBuilder searchRequestBuilder2 = esClient.prepareSearch(index).setTypes(types);
            searchRequestBuilder2.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
            BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
        	boolQueryBuilder2.must(QueryBuilders.termsQuery("tags", keyWord));
        	searchRequestBuilder2.addSort("score", SortOrder.DESC);
            searchRequestBuilder2.setQuery(boolQueryBuilder2).setFrom(from).setSize(num).setExplain(true);
            response = null;
            try {
                response = searchRequestBuilder2.execute().actionGet();
            } catch (Exception ex) {
            	LOG.error(ex.getMessage(), ex.fillInStackTrace());
            	return "";
            }
            if(response.getHits().getHits().length <= 0) {
/* ------ director/actor ------ */
                SearchRequestBuilder searchRequestBuilder3 = esClient.prepareSearch(index).setTypes(types);
                searchRequestBuilder3.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
                BoolQueryBuilder boolQueryBuilder3 = QueryBuilders.boolQuery();
                boolQueryBuilder3.should(QueryBuilders.matchPhraseQuery("director", keyWord));
                boolQueryBuilder3.should(QueryBuilders.matchPhraseQuery("actor", keyWord));
            	boolQueryBuilder3.minimumNumberShouldMatch(1);
            	searchRequestBuilder3.addSort("score", SortOrder.DESC);
                searchRequestBuilder3.setQuery(boolQueryBuilder3).setFrom(from).setSize(num).setExplain(true);
                response = null;
                try {
                    response = searchRequestBuilder3.execute().actionGet();
                } catch (Exception ex) {
                	LOG.error(ex.getMessage(), ex.fillInStackTrace());
                	return "";
                }
                if(response.getHits().getHits().length <= 0) {
/* ------ userTags ------ */
                    SearchRequestBuilder searchRequestBuilder4 = esClient.prepareSearch(index).setTypes(types);
                    searchRequestBuilder4.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
                    BoolQueryBuilder boolQueryBuilder4 = QueryBuilders.boolQuery();
                	boolQueryBuilder4.must(QueryBuilders.termsQuery("userTags", keyWord));
                	searchRequestBuilder4.addSort("score", SortOrder.DESC);
                    searchRequestBuilder4.setQuery(boolQueryBuilder4).setFrom(from).setSize(num).setExplain(true);
                    response = null;
                    try {
                        response = searchRequestBuilder4.execute().actionGet();
                    } catch (Exception ex) {
                    	LOG.error(ex.getMessage(), ex.fillInStackTrace());
                    	return "";
                    }
                    if(response.getHits().getHits().length <= 0){
/* ------ common ------ */
                        SearchRequestBuilder searchRequestBuilder5 = esClient.prepareSearch(index).setTypes(types);
                        searchRequestBuilder5.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
                        BoolQueryBuilder boolQueryBuilder5 = QueryBuilders.boolQuery();
                    	boolQueryBuilder5.must(QueryBuilders.multiMatchQuery(keyWord, new String[]{"name", "alias", "director", "screenwriter", "actor", "describe", "tags"}));
                    	searchRequestBuilder5.addSort("score", SortOrder.DESC);
                        searchRequestBuilder5.setQuery(boolQueryBuilder5).setFrom(from).setSize(num).setExplain(true);
                        response = null;
                        try {
                            response = searchRequestBuilder5.execute().actionGet();
                        } catch (Exception ex) {
                        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
                        	return "";
                        }
                        if(response.getHits().getHits().length <= 0){
/* ------ Nothing ------ */
                        	//return "";
                        }
                    }
                }
            }
            
            searchHits = response.getHits();
            hits = searchHits.getHits();
            for (int i = 0; i < hits.length; i++) {
                
                SearchHit hit = hits[i];
                Map<String, Object> resultTemp = hit.getSource();
                Map<String, Object> newResultTemp2 = new HashMap<String, Object>();
                
                newResultTemp2.put("id", String.valueOf(resultTemp.get("id")));
                newResultTemp2.put("name", resultTemp.get("name") == null ? "" : (String) resultTemp.get("name"));
                newResultTemp2.put("moviePic", resultTemp.get("moviePic") == null ? "" : CheckParams.matcherMoviePic((String) resultTemp.get("moviePic")));
                newResultTemp2.put("type", (String) resultTemp.get("innerType"));
                newResultTemp2.put("director", resultTemp.get("director") == null ? new ArrayList<String>() : (List<String>) resultTemp.get("director"));
                newResultTemp2.put("actor", resultTemp.get("actor") == null ? new ArrayList<String>() : (List<String>) resultTemp.get("actor"));
                newResultTemp2.put("tags", resultTemp.get("tags") == null ? new ArrayList<String>() : (List<String>) resultTemp.get("tags"));
                
                /*检查敏感词
                if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
                		(String)newResultTemp2.get("name"), ((ArrayList<String>)newResultTemp2.get("director")).toString(),
                		((ArrayList<String>)newResultTemp2.get("actor")).toString()
                		}))){
                	continue;
                }*/
                
                retList.add(newResultTemp2);
            }
            
            retPSJson.put("count", hits.length);
            retPSJson.put("start", from);
            retPSJson.put("total", searchHits.getTotalHits());
            retPSJson.put("subjects", retList);
        }

        retJson.put("ps", retPSJson);
        retJson.put("flag", "0");

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**书单通用搜索*/
	public String bookListSearchByCommon(String keyWord, String fieldsStr,
			String tags, int from, int num) {
		String index = "resource";
		String [] types = {"booklist"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //boolQueryBuilder.must(QueryBuilders.termsQuery("source_type", "booklist_2"));
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
            String [] fields = {"booklist_name", "reason", "tags_a"};
            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(tags, ',')));
        }
        searchRequestBuilder.addSort("source_type", SortOrder.DESC).addSort("_score", SortOrder.DESC);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("booklist_name"));
            newResultTemp.put("fristBookPicUrl", CheckParams.matcherBookPic((String) resultTemp.get("cover")));
            newResultTemp.put("reason", (String) resultTemp.get("reason"));
            
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name"), (String)newResultTemp.get("reason")
            		}))){
            	continue;
            }*/

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}

	/**影单通用搜索*/
	public String movieListSearchByCommon(String keyWord, String fieldsStr,
			String tags, int from, int num) {
		String index = "resource";
		String [] types = {"movielist"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //boolQueryBuilder.must(QueryBuilders.termsQuery("source_type", "movielist_2"));
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
            String [] fields = {"filmlist_name", "reason", "tags_a"};
            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(tags, ',')));
        }
        searchRequestBuilder.addSort("source_type", SortOrder.DESC).addSort("_score", SortOrder.DESC);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("filmlist_name"));
            newResultTemp.put("firstMoviePic", CheckParams.matcherMoviePic((String) resultTemp.get("cover")));
            newResultTemp.put("reason", (String) resultTemp.get("reason"));

            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name"), (String)newResultTemp.get("reason")
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**书评通用搜索（外部）*/
	public String bookCommentSearchByCommonOuter(String keyWord, String fieldsStr,
			String innerTypes, int from, int num) {
		Map<String, Object> retJson = this.bookCommentSearchByCommon(keyWord, fieldsStr, innerTypes, from, num);
		
		if(null == retJson) {
			return "";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**书评通用搜索（内部）*/
	public Map<String, Object> bookCommentSearchByCommon(String keyWord, String fieldsStr,
			String innerTypes, int from, int num) {
		String index = "resource";
		String [] types = {"bookcomment"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
            String [] fields = {"title"};
            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(innerTypes)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("innerType", StringUtils.split(innerTypes, ',')));
        }
        //searchRequestBuilder.addSort("source_type", SortOrder.DESC).addSort("_score", SortOrder.DESC);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("title", resultTemp.get("title") == null ? "" : (String) resultTemp.get("title"));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("title")
            		}))){
            	continue;
            }*/

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

		return retJson;
	}
	
	/**影评通用搜索（外部）*/
	public String movieCommentSearchByCommonOuter(String keyWord, String fieldsStr,
			String innerTypes, int from, int num) {
		Map<String, Object> retJson = this.movieCommentSearchByCommon(keyWord, fieldsStr, innerTypes, from, num);
		
		if(null == retJson) {
			return "";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**影评通用搜索（内部）*/
	public Map<String, Object> movieCommentSearchByCommon(String keyWord, String fieldsStr,
			String innerTypes, int from, int num) {
		String index = "resource";
		String [] types = {"moviecomment"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
            String [] fields = {"title"};
            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(innerTypes)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("innerType", StringUtils.split(innerTypes, ',')));
        }
        //searchRequestBuilder.addSort("source_type", SortOrder.DESC).addSort("_score", SortOrder.DESC);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("title", resultTemp.get("title") == null ? "" : (String) resultTemp.get("title"));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("title")
            		}))){
            	continue;
            }*/

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);
        
		return retJson;
	}
	
	/**文章通用搜索*/
	public Map<String, Object> articleSearchByCommon(String keyWord, String fieldsStr,
			String tags, int from, int num) {
		String index = "resource";
		String [] types = {"article"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //boolQueryBuilder.must(QueryBuilders.termsQuery("source_type", "movielist_2"));
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
            String [] fields = {"name", "tags_a"};
            if(StringUtils.isNotBlank(fieldsStr)){
            	fields = StringUtils.split(fieldsStr, ',');
            }
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(tags, ',')));
        }
        searchRequestBuilder.addSort("source_type", SortOrder.DESC).addSort("_score", SortOrder.DESC);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", resultTemp.get("name") == null ? "" : (String) resultTemp.get("name"));
            newResultTemp.put("cover", resultTemp.get("cover") == null ? "" : (String) resultTemp.get("cover"));

            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name")
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);
        
		return retJson;
	}
	
	
	/**电影分类搜索*/
	public String movieSearchByFields(Map<String, String> fields, Map<String, String> sort, int from, int num) {
		String index = "store";
		String [] types = {"movie"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String country = fields.get("country");
        String year = fields.get("year");
        String tag = fields.get("tag");
        String topic = fields.get("topic");
        String type = fields.get("type");
        String star = fields.get("star");
        String scene = fields.get("scene");
        String mind = fields.get("mind");

        String sortField = sort.get("sort");
        String desc = sort.get("desc");
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        if(StringUtils.isNotBlank(country)){
            boolQueryBuilder.must(QueryBuilders.regexpQuery("countries", ".*" + country + ".*"));
        }
        if(StringUtils.isNotBlank(year)){
        	boolQueryBuilder.must(QueryBuilders.regexpQuery("releaseDate", ".*" + year + ".*"));
        }
        if(StringUtils.isNotBlank(tag)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags", StringUtils.split(tag, ',')));
        }
        if(StringUtils.isNotBlank(topic)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags", StringUtils.split(topic, ',')));
        }
        if(StringUtils.isNotBlank(type)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags", StringUtils.split(type, ',')));
        }
        if(StringUtils.isNotBlank(star)){
        	BoolQueryBuilder clauseBoolQueryBuilder = QueryBuilders.boolQuery();
        	clauseBoolQueryBuilder.should(QueryBuilders.termsQuery("director", StringUtils.split(star, ',')));
        	clauseBoolQueryBuilder.should(QueryBuilders.termsQuery("actor", StringUtils.split(star, ',')));
        	clauseBoolQueryBuilder.minimumNumberShouldMatch(1);
        	boolQueryBuilder.must(clauseBoolQueryBuilder);
        }
        if(StringUtils.isNotBlank(scene)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags", StringUtils.split(scene, ',')));
        }
        if(StringUtils.isNotBlank(mind)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags", StringUtils.split(mind, ',')));
        }
        /*sort*/
        searchRequestBuilder.addSort("isOperation", SortOrder.DESC);
        if(StringUtils.equalsIgnoreCase(sortField, "score")) {
        	searchRequestBuilder.addSort("score", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "reviewNum")) {
        	searchRequestBuilder.addSort("review", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "addTime")) {
        	searchRequestBuilder.addSort("releaseDate", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("name"));
            newResultTemp.put("moviePic", CheckParams.matcherMoviePic((String) resultTemp.get("moviePic")));
            newResultTemp.put("score", String.valueOf(resultTemp.get("score")));
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name")
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**影单分类搜索*/
	public String movieListSearchByFields(Map<String, String> fields, Map<String, String> sort, int from, int num) {
		String index = "resource";
		String [] types = {"movielist"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String country = fields.get("country");
        String year = fields.get("year");
        String director = fields.get("director");
        String topic = fields.get("topic");
        String type = fields.get("type");
        String star = fields.get("star");
        String scene = fields.get("scene");
        String mind = fields.get("mind");

        String sortField = sort.get("sort");
        String desc = sort.get("desc");
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        //boolQueryBuilder.must(QueryBuilders.termsQuery("source_type", "movielist_2"));
        if(StringUtils.isNotBlank(country)){
            boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(country, ',')));
        }
        if(StringUtils.isNotBlank(year)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(year, ',')));
        }
        if(StringUtils.isNotBlank(director)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(director, ',')));
        }
        if(StringUtils.isNotBlank(topic)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(topic, ',')));
        }
        if(StringUtils.isNotBlank(type)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(type, ',')));
        }
        if(StringUtils.isNotBlank(star)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(star, ',')));
        }
        if(StringUtils.isNotBlank(scene)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(scene, ',')));
        }
        if(StringUtils.isNotBlank(mind)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(mind, ',')));
        }
        /*sort*/
        searchRequestBuilder.addSort("source_type", SortOrder.DESC);
        if(StringUtils.equalsIgnoreCase(sortField, "addTime")) {
        	searchRequestBuilder.addSort("addTime", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "collectNum")) {
        	searchRequestBuilder.addSort("collect", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "reviewNum")) {
        	searchRequestBuilder.addSort("review", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("filmlist_name"));
            newResultTemp.put("firstMoviePic", CheckParams.matcherMoviePic((String) resultTemp.get("cover")));
            newResultTemp.put("reason", (String) resultTemp.get("reason"));
            newResultTemp.put("type", CommentUtils.TYPE_MOVIELIST);
            
            try{
            	//每个影单增加最多30个电影
                List<MvListLink> mvListLinks = myMovieFacade.findMovieListInfo(Long.valueOf(resultTemp.get("id")+""),null, 30);
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
    							MovieInfo movieInfo = movieUtils.putMVToMovieInfo(mvInfo, 0);
    							mvInfos.add(movieInfo);
    						}
    					}
    					//将电影集合放入影单中
    					newResultTemp.put("movieInfos", mvInfos);
    				}
    			}
            }catch(Exception e){
            	e.printStackTrace();
            }
            
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name"), (String)newResultTemp.get("reason")
            		}))){
            	continue;
            }*/

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**书籍分类搜索*/
	public String bookSearchByFields(Map<String, String> fields, Map<String, String> sort, int from, int num) {
		String index = "store";
		String [] types = null;
		String [] tags = null;
		String bookTags = fields.get("book");
		String netBookTags = fields.get("netbook");
		String catoonTags = fields.get("catoon");
		String ecoEncTags = fields.get("eco_enc");
		String shekeTags = fields.get("sheke");
		
		if (StringUtils.isNotBlank(bookTags)) {
			types = new String[] {"book"};
			tags = StringUtils.split(bookTags, ',');
		} else if (StringUtils.isNotBlank(netBookTags)) {
			types = new String[] {"netbook"};
			tags = StringUtils.split(netBookTags, ',');
		} else if (StringUtils.isNotBlank(catoonTags)) {
			types = new String[] {"book"};
			tags = StringUtils.split(catoonTags, ',');
		} else if (StringUtils.isNotBlank(ecoEncTags)) {
			types = new String[] {"book"};
			tags = StringUtils.split(ecoEncTags, ',');
		} else if (StringUtils.isNotBlank(shekeTags)) {
			types = new String[] {"book"};
			tags = StringUtils.split(shekeTags, ',');
		} else {
			types = new String[] {"book", "netbook"};
			tags = null;
		}
		
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String sortField = sort.get("sort");
        String desc = sort.get("desc");
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        if (StringUtils.isNotBlank(netBookTags)){
        	BoolQueryBuilder clauseBoolQueryBuilder = QueryBuilders.boolQuery();
        	clauseBoolQueryBuilder.should(QueryBuilders.termsQuery("tags", tags));
        	clauseBoolQueryBuilder.should(QueryBuilders.termsQuery("type", tags));
        	clauseBoolQueryBuilder.minimumNumberShouldMatch(1);
        	boolQueryBuilder.must(clauseBoolQueryBuilder);
        } else {
        	if (null != tags) {
        		boolQueryBuilder.must(QueryBuilders.termsQuery("tags", tags));
        	}
        	searchRequestBuilder.addSort("isOperation", SortOrder.DESC);
        }
        
        /*sort*/
        if(StringUtils.equalsIgnoreCase(sortField, "score")) {
        	searchRequestBuilder.addSort("score", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "reviewNum")) {
        	searchRequestBuilder.addSort("review", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "addTime")) {
        	searchRequestBuilder.addSort("publishTime", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("name"));
            newResultTemp.put("pagePic", CheckParams.matcherBookPic((String) resultTemp.get("pagePic")));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));
            
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name")
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**书单分类搜索*/
	public String bookListSearchByFields(Map<String, String> fields, Map<String, String> sort, int from, int num) {
		String index = "resource";
		String [] types = {"booklist"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String topic = fields.get("topic");
        String type = fields.get("type");
        String star = fields.get("star");
        String rank = fields.get("rank");
        String year = fields.get("year");
        String recommend = fields.get("recommend");
        String sortField = sort.get("sort");
        String desc = sort.get("desc");
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        //boolQueryBuilder.must(QueryBuilders.termsQuery("source_type", "booklist_2"));
        if(StringUtils.isNotBlank(topic)){
            boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(topic, ',')));
        }
        if(StringUtils.isNotBlank(type)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(type, ',')));
        }
        if(StringUtils.isNotBlank(star)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(star, ',')));
        }
        if(StringUtils.isNotBlank(rank)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(rank, ',')));
        }
        if(StringUtils.isNotBlank(year)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(year, ',')));
        }
        if(StringUtils.isNotBlank(recommend)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(recommend, ',')));
        }
        /*sort*/
        searchRequestBuilder.addSort("source_type", SortOrder.DESC);
        if(StringUtils.equalsIgnoreCase(sortField, "addTime")) {
        	searchRequestBuilder.addSort("addTime", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "collectNum")) {
        	searchRequestBuilder.addSort("collect", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "reviewNum")) {
        	searchRequestBuilder.addSort("review", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("booklist_name"));
            newResultTemp.put("fristBookPicUrl", CheckParams.matcherBookPic((String) resultTemp.get("cover")));
            newResultTemp.put("reason", (String) resultTemp.get("reason"));
            
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name"), (String)newResultTemp.get("reason")
            		}))){
            	continue;
            }*/

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**小说的分类搜索*/
	public String serializeSearchByFields(Map<String, String> fields, Map<String, String> sort, int from, int num) {
		String index = "resource";
		String [] types = {"serialize"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String tags = fields.get("tags");
        String type = fields.get("type");
        String sortField = sort.get("sort");
        String desc = sort.get("desc");
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        //boolQueryBuilder.must(QueryBuilders.termsQuery("source_type", "booklist_2"));
        if(StringUtils.isNotBlank(tags)){
            boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(tags, ',')));
        }
        //特殊分支
        if(StringUtils.isBlank(tags) && StringUtils.equals(type, "2")){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("source_type", "serialize_2"));
        } else if(StringUtils.isBlank(tags) && StringUtils.equals(type, "37")){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("type", "37"));
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", "短篇"));
        }else {
            if(StringUtils.isNotBlank(type)){
            	boolQueryBuilder.must(QueryBuilders.termsQuery("type", StringUtils.split(type, ',')));
            }
        }

        /*sort*/
		FunctionScoreQueryBuilder clause = QueryBuilders.functionScoreQuery(
				QueryBuilders.termsQuery("source_type", new String[] {
						"serialize_2", "serialize_3" }),
				ScoreFunctionBuilders.scriptFunction("2.0")).scoreMode(
				"multiply").boostMode("replace");
		boolQueryBuilder.should(clause);
		searchRequestBuilder = searchRequestBuilder.addSort("_score", SortOrder.DESC);

        /*bug here: 因为要保持兼容，这里应该按照最后修改时间排序*/
        if(StringUtils.equalsIgnoreCase(sortField, "addTime")) {
        	searchRequestBuilder.addSort("updateTime", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC);
        }
        
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        ArrayList<String> userIdList = new ArrayList<String>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("u_id", (Integer) resultTemp.get("u_id"));
            newResultTemp.put("id", Long.valueOf(String.valueOf(resultTemp.get("id"))));
            newResultTemp.put("idStr", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));
            newResultTemp.put("chapterTitle", "");
            newResultTemp.put("isSubscribe", 0);
            newResultTemp.put("title", resultTemp.get("name") == null ? "" : (String) resultTemp.get("name"));
            newResultTemp.put("url", resultTemp.get("url") == null ? "" : (String) resultTemp.get("url"));
            newResultTemp.put("btime", resultTemp.get("addTime") == null ? "" : (String) resultTemp.get("addTime"));
            newResultTemp.put("utime", resultTemp.get("updateTime") == null ? "" : (String) resultTemp.get("updateTime"));
            newResultTemp.put("describe", resultTemp.get("introduce") == null ? "" : (String) resultTemp.get("introduce"));
            newResultTemp.put("author", resultTemp.get("author") == null ? "" : (String) resultTemp.get("author"));
            newResultTemp.put("tags", resultTemp.get("tags_a") == null ? new ArrayList<String>() : resultTemp.get("tags_a"));
            newResultTemp.put("userEntity", new HashMap<String, Object>());

            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("title"), (String)newResultTemp.get("describe"),
            		(String)newResultTemp.get("author")
            		}))){
            	continue;
            }*/
            userIdList.add(String.valueOf((Integer) resultTemp.get("u_id")));
            retList.add(newResultTemp);
        }
        
        MultiGetRequestBuilder multiGetRequestBuilder = esClient.prepareMultiGet();
        multiGetRequestBuilder.add("user", "user_info", userIdList);
        MultiGetResponse multiGetResponse = null;
        try {
        	multiGetResponse = multiGetRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        }
        if (null != multiGetResponse){
            MultiGetItemResponse [] mutilGetItemResponse = multiGetResponse.getResponses();
            for (int i = 0 ; i < mutilGetItemResponse.length; i++){
            	Map<String, Object> item = mutilGetItemResponse[i].getResponse().getSource();
            	if(null == item){
            		continue;
            	}
            	
                /*检查敏感词
                if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
                		(String)item.get("user_name")
                		}))){
                	continue;
                }*/
            	
            	HashMap<String, Object> userEntity = (HashMap<String, Object>)retList.get(i).get("userEntity");
            	userEntity.put("id", item.get("user_id"));
            	userEntity.put("nickName", (String)item.get("user_name"));
            	userEntity.put("sex", (String)item.get("sex"));
            	userEntity.put("type", (Integer)item.get("level"));
            	userEntity.put("face_url", item.get("face_address") == null ? "": (String)item.get("face_address"));
            }
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**依据用户标签推荐*/
	public Map<String, Object> userRecByTags(String [] type_list, String [] tag_list,  int from, int num) {
		String index = "resource";
		String [] types = type_list;
		String [] tags = tag_list;
		
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		if (tags != null && tags.length > 0) {
			FunctionScoreQueryBuilder clause = QueryBuilders
					.functionScoreQuery(
							QueryBuilders.termsQuery("tags_a", tags),
							ScoreFunctionBuilders.scriptFunction("1.0"))
					.scoreMode("multiply").boostMode("replace");
			boolQueryBuilder.should(clause);
		}
		FunctionScoreQueryBuilder clause = QueryBuilders.functionScoreQuery(
				QueryBuilders.termsQuery("source_type", new String[] {
						"booklist_2", "movielist_2", "bookcomment_nil",
						"moviecomment_nil", "article_nil" }),
				ScoreFunctionBuilders.scriptFunction("0.0")).scoreMode(
				"multiply").boostMode("replace");
		boolQueryBuilder.must(clause);
        searchRequestBuilder.addSort("_score", SortOrder.DESC).addSort("id", SortOrder.DESC);
        //searchRequestBuilder.addSort("id", SortOrder.DESC);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            
            if (StringUtils.equals(hit.getType(), "booklist") ) {
            	resultTemp.put("cover", CheckParams.matcherBookPic((String) resultTemp.get("cover")));
            }
            if (StringUtils.equals(hit.getType(), "movielist") ) {
            	resultTemp.put("cover", CheckParams.matcherMoviePic((String) resultTemp.get("cover")));
            }
            
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
                    resultTemp.toString()
            		}))){
            	continue;
            }*/

            retList.add(resultTemp);
        }

        Map<String, Object> retMap = new HashMap<String, Object>();
        
        retMap.put("count", hits.length);
        retMap.put("start", from);
        retMap.put("total", searchHits.getTotalHits());
        retMap.put("list", retList);
        
        return retMap;
	}
	
	/**标签通用搜索*/
	public String tagsSearchByCommon(String keyWord, String tagType, int from, int num) {
		String index = "tag";
		String [] types = {"tag_info"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
            String [] fields = {"tag_name"};
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(tagType)){
        	boolQueryBuilder.must(QueryBuilders.termQuery("type", tagType));
        }

        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return "";
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("tag_name", resultTemp.get("tag_name") == null ? "": (String)resultTemp.get("tag_name"));
            newResultTemp.put("tag_group", resultTemp.get("tag_group") == null ? "": (String)resultTemp.get("tag_group"));
            newResultTemp.put("type", resultTemp.get("type") == null ? "": (String)resultTemp.get("type"));

            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("tag_name")
            		}))){
            	continue;
            }*/
            
            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**通过标签搜索所有资源（外部）*/
	public String resourceSearchByTagOuter(String keyWord, String tags, String typesStr, int from, int num,String flag) {
		Map<String, Object> retJson = this.resourceSearchByTag(keyWord, tags, typesStr, from, num,flag);
		
		if(null == retJson) {
			return "";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**通过标签搜索所有资源（内部）*/
	public Map<String, Object> resourceSearchByTag(String keyWord, String tags,
			String typesStr, int from, int num,String flag) {
		String [] indexs = {"resource", "store"};
		String [] types = {"booklist", "movielist", "article", "bookcomment", "moviecomment", "diary", "topic", "netbook", "book", "movie"};
		if(StringUtils.isNotBlank(typesStr)){
			types = StringUtils.split(typesStr, ',');
		}
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(indexs).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.boolQuery().minimumNumberShouldMatch(1)
        			.should(QueryBuilders.termsQuery("tags_a", StringUtils.split(tags, ',')))
        			.should(QueryBuilders.termsQuery("tags", StringUtils.split(tags, ','))));
        }
        
		if (StringUtils.isNotBlank(keyWord)) {
			//"description","introduction","describe"因为普遍较大并且关联度较小而未列入
			String[] fields = { "booklist_name", "filmlist_name", "reason",
					"tags_a", "name", "title", "comment", "authorName", "tags",
					"alias", "director", "screenwriter", "actor" };
			boolQueryBuilder.must(QueryBuilders
					.multiMatchQuery(keyWord, fields));
		}
        
        String sessionID = ProductContextHolder.getProductContext().getSessionId();
        if(StringUtils.isNotBlank(sessionID)){
            searchRequestBuilder.setPreference(sessionID);
        }
        
        searchRequestBuilder.addSort("source_type_num", SortOrder.DESC).addSort("updateTime", SortOrder.DESC).addSort("_score", SortOrder.DESC);
        searchRequestBuilder.setQuery(boolQueryBuilder);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();

            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("type", (String) resultTemp.get("innerType"));
            if("all".equals(flag)){
        		if("booklist".equals(typesStr)){
        			newResultTemp.put("cover", resultTemp.get("cover"));
        			newResultTemp.put("reason", resultTemp.get("reason"));
        			newResultTemp.put("uid", resultTemp.get("u_id"));
        			newResultTemp.put("title", resultTemp.get("booklist_name"));
        			newResultTemp.put("name", resultTemp.get("booklist_name"));//方便统一展示使用
        			newResultTemp.put("content", resultTemp.get("reason"));//方便统一展示使用
            	}else if("movielist".equals(typesStr)){
            		newResultTemp.put("cover", resultTemp.get("cover"));
        			newResultTemp.put("reason", resultTemp.get("reason"));
        			newResultTemp.put("title", resultTemp.get("filmlist_name"));
        			newResultTemp.put("uid", resultTemp.get("u_id"));
        			newResultTemp.put("name", resultTemp.get("filmlist_name"));//方便统一展示使用
        			newResultTemp.put("content", resultTemp.get("reason"));//方便统一展示使用
            	}else if("article".equals(typesStr)){
            		newResultTemp.put("summary", resultTemp.get("summary"));
            		newResultTemp.put("title", resultTemp.get("name"));
                	newResultTemp.put("content", resultTemp.get("content"));
                	newResultTemp.put("uid", resultTemp.get("u_id"));
                	newResultTemp.put("name", resultTemp.get("name"));//方便统一展示使用
            	}else if("bookcomment".equals(typesStr)){
            		newResultTemp.put("title", resultTemp.get("title"));
                	newResultTemp.put("bookid", resultTemp.get("book_id"));
                	newResultTemp.put("name", resultTemp.get("name"));
                	newResultTemp.put("score", resultTemp.get("score"));
                	newResultTemp.put("content", resultTemp.get("comment"));
                	newResultTemp.put("uid", resultTemp.get("u_id"));
                	newResultTemp.put("name", resultTemp.get("title"));//方便统一展示使用
            	}else if("moviecomment".equals(typesStr)){
            		newResultTemp.put("title", resultTemp.get("title"));
                	newResultTemp.put("movieid", resultTemp.get("movie_id"));
                	newResultTemp.put("name", resultTemp.get("name"));
                	newResultTemp.put("score", resultTemp.get("score"));
                	newResultTemp.put("content", resultTemp.get("comment"));
                	newResultTemp.put("uid", resultTemp.get("u_id"));
            	}else if("diary".equals(typesStr)){
            		newResultTemp.put("title", resultTemp.get("title"));
                	newResultTemp.put("content", resultTemp.get("comment"));
                	newResultTemp.put("uid", resultTemp.get("u_id"));
                	newResultTemp.put("name", resultTemp.get("title"));//方便统一展示使用
            	}else if("topic".equals(typesStr)){
            		newResultTemp.put("cover", resultTemp.get("cover"));
        			newResultTemp.put("description", resultTemp.get("description"));
        			newResultTemp.put("title", resultTemp.get("title"));
        			newResultTemp.put("uid", resultTemp.get("u_id"));
        			newResultTemp.put("uname", resultTemp.get("u_name"));
        			newResultTemp.put("name", resultTemp.get("title"));//方便统一展示使用
        			newResultTemp.put("content", resultTemp.get("description"));//方便统一展示使用
            	}else if("netbook".equals(typesStr)){
            		newResultTemp.put("bookUrl", resultTemp.get("bookUrl"));
            		newResultTemp.put("name", resultTemp.get("name"));
            		newResultTemp.put("pagePic", resultTemp.get("pagePic"));
            		newResultTemp.put("authorName", resultTemp.get("authorName"));
            		newResultTemp.put("introduction", resultTemp.get("introduction"));
            		newResultTemp.put("tags", resultTemp.get("tags"));
            		newResultTemp.put("title", resultTemp.get("name"));//方便统一展示使用
            		newResultTemp.put("content", resultTemp.get("introduction"));//方便统一展示使用
            	}else if("book".equals(typesStr)){
            		newResultTemp.put("press", resultTemp.get("press"));
            		newResultTemp.put("name", resultTemp.get("name"));
            		newResultTemp.put("pagePic", resultTemp.get("pagePic"));
            		newResultTemp.put("authorName", resultTemp.get("authorName"));
            		newResultTemp.put("introduction", resultTemp.get("introduction"));
            		newResultTemp.put("tags", resultTemp.get("tags"));
            		newResultTemp.put("publishTime", resultTemp.get("publishTime"));
            		newResultTemp.put("score", resultTemp.get("score"));
            		newResultTemp.put("title", resultTemp.get("name"));//方便统一展示使用
            		newResultTemp.put("content", resultTemp.get("introduction"));//方便统一展示使用
            	}else if("movie".equals(typesStr)){
            		newResultTemp.put("moviePic", resultTemp.get("moviePic"));
            		newResultTemp.put("tags", resultTemp.get("tags"));
            		newResultTemp.put("score", resultTemp.get("score"));
            		newResultTemp.put("actor", resultTemp.get("actor"));
            		newResultTemp.put("countries", resultTemp.get("countries"));
            		newResultTemp.put("name", resultTemp.get("name"));
            		newResultTemp.put("director", resultTemp.get("director"));
            		newResultTemp.put("releaseDate", resultTemp.get("releaseDate"));
            		newResultTemp.put("screenwriter", resultTemp.get("screenwriter"));
            		newResultTemp.put("title", resultTemp.get("name"));//方便统一展示使用
            		newResultTemp.put("content", "");//方便统一展示使用
            	}
            }
            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);
        
        return retJson;
	}
	
	/**通过标签随机获取小说（外部）*/
	public String storyRandomSearchByTagsOuter(String tags, int num) {
		
		Map<String, Object> retJson = this.storyRandomSearchByTags(tags, num);
		
		if(null == retJson) {
			return "";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**通过标签随机获取小说*/
	public Map<String, Object> storyRandomSearchByTags(String tags, int num) {
		String index = "resource";
		String [] types = {"story"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder = QueryBuilders.boolQuery();
        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(tags, ',')));
        }

        searchRequestBuilder.setQuery(boolQueryBuilder);

        searchRequestBuilder.setFrom(0).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        
        /*结果大于需要数目时随机取*/
        /*TODO: 连续执行查询？*/
        int startIndex = 0;
        if(searchHits.getTotalHits() > num){
        	/*FIXME: (int)*/
        	startIndex = RandomUtils.nextInt((int)(searchHits.getTotalHits()) - num + 1);
        	searchRequestBuilder.setFrom(startIndex).setSize(num);

            try {
                response = searchRequestBuilder.execute().actionGet();
            } catch (Exception ex) {
            	LOG.error(ex.getMessage(), ex.fillInStackTrace());
            	return null;
            }
            searchHits = response.getHits();
            hits = searchHits.getHits();
        }

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("name"));
            newResultTemp.put("author", resultTemp.get("author") == null ? "": (String) resultTemp.get("author"));
            
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name"), (String)newResultTemp.get("author")
            		}))){
            	continue;
            }*/

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", startIndex);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);
        
		return retJson;
	}
	
	
	
	/**标准小说通用搜索（外部）*/
	public String storySearchByCommonOuter(String keyWord, String fieldsStr,
			String type, String tags, String channel, Map<String, String> sort, int from, int num) {
		
		Map<String, Object> retJson = this.storySearchByCommon(keyWord, fieldsStr, type, tags, channel, sort, from, num);
		
		if(null == retJson) {
			return "";
		}
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	/**标准小说通用搜索*/
	public Map<String, Object> storySearchByCommon(String keyWord, String fieldsStr,
			String type, String tags, String channel, Map<String, String> sort, int from, int num) {
		String index = "resource";
		String [] types = {"story"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder = QueryBuilders.boolQuery();
        /*fun 1*/
        if(StringUtils.isNotBlank(keyWord)){
            if(StringUtils.isBlank(fieldsStr)){
                BoolQueryBuilder keyWordBoolQueryBuilder = QueryBuilders.boolQuery();
                String [] firstFields = {"author"};
                String [] secondFields = {"name"};
                //keyWordBoolQueryBuilder.should(QueryBuilders
    			//		.functionScoreQuery(
    			//				QueryBuilders.termQuery("name_str", keyWord),
    			//				ScoreFunctionBuilders.scriptFunction("6.0"))
    			//		.scoreMode("multiply").boostMode("replace"));
                keyWordBoolQueryBuilder.should(QueryBuilders
    					.functionScoreQuery(
    							QueryBuilders.termQuery("author_str", keyWord),
    							ScoreFunctionBuilders.scriptFunction("4.0"))
    					.scoreMode("multiply").boostMode("replace"));
                keyWordBoolQueryBuilder.should(QueryBuilders
    					.functionScoreQuery(
    							QueryBuilders.multiMatchQuery(keyWord, firstFields),
    							ScoreFunctionBuilders.scriptFunction("3.0"))
    					.scoreMode("multiply").boostMode("replace"));
                keyWordBoolQueryBuilder.should(QueryBuilders
    					.functionScoreQuery(
    							QueryBuilders.multiMatchQuery(keyWord, secondFields),
    							ScoreFunctionBuilders.scriptFunction("1.0"))
    					.scoreMode("multiply").boostMode("replace"));
                keyWordBoolQueryBuilder.minimumNumberShouldMatch(1);
    			boolQueryBuilder.must(keyWordBoolQueryBuilder);
            } else {
            	String [] fields = StringUtils.split(fieldsStr, ',');
            	boolQueryBuilder.must(QueryBuilders.multiMatchQuery(keyWord, fields));
            }
        }
        /*fun 2*/
        if(StringUtils.isNotBlank(tags)){
        	boolQueryBuilder.must(QueryBuilders.termsQuery("tags_a", StringUtils.split(tags, ',')));
        }
        /*fun 3*/
        if(StringUtils.isNotBlank(type)){
        	boolQueryBuilder.must(QueryBuilders.termQuery("type", type));
        }
        /*fun 4*/
        if(StringUtils.isNotBlank(channel)){
        	boolQueryBuilder.must(QueryBuilders.termQuery("channel", channel));
        }

        /*sort*/
        String sortField = null;
        String desc = null;
        if(null == sort){
        	//sortField = "updateTime";
        	//desc = "1";
        } else {
        	sortField = sort.get("sort");
        	desc = sort.get("desc");
        }
        if(StringUtils.equalsIgnoreCase(sortField, "updateTime")) {
        	searchRequestBuilder.addSort("updateTime", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        if(StringUtils.equalsIgnoreCase(sortField, "visit")) {
        	searchRequestBuilder.addSort("visit", StringUtils.equals(desc, "1") ? SortOrder.DESC : SortOrder.ASC).addSort("_score", SortOrder.DESC);
        }
        
        searchRequestBuilder.setQuery(boolQueryBuilder);

        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        SearchResponse response = null;
        try {
            response = searchRequestBuilder.execute().actionGet();
        } catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
        	return null;
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();

        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            Map<String, Object> resultTemp = hit.getSource();
            Map<String, Object> newResultTemp = new HashMap<String, Object>();
            
            newResultTemp.put("id", String.valueOf(resultTemp.get("id")));
            newResultTemp.put("name", (String) resultTemp.get("name"));
            newResultTemp.put("author", resultTemp.get("author") == null ? "": (String) resultTemp.get("author"));
            
            /*检查敏感词
            if(this.sensitiveWordManager.onlyCheckSensitiveWord(StringUtils.join(new String[]{
            		(String)newResultTemp.get("name"), (String)newResultTemp.get("author")
            		}))){
            	continue;
            }*/

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);
        
		return retJson;
	}
	
	public String tempUserRecByTags(String [] type_list, String [] tag_list,  int from, int num) {
		Map<String, Object> retJson = this.userRecByTags(type_list, tag_list, from, num);
		
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}

}
