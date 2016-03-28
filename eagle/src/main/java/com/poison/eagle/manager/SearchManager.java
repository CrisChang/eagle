package com.poison.eagle.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.elasticsearch.search.highlight.HighlightField;

public class SearchManager {
	private static final  Log LOG = LogFactory.getLog(SearchManager.class);
	
	private Client esClient;

	public void setEsClient(Client esClient) {
		this.esClient = esClient;
	}
	
	public String bookSearch(String keyWord, int from, int num) {
		String index = "store";
		String [] types = {"book"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        //searchRequestBuilder.setQuery(QueryBuilders.matchQuery("title", keyWord));
        String [] fields = {"name", "authorName", "introduction"};
		searchRequestBuilder.setQuery(QueryBuilders.multiMatchQuery(keyWord, fields));
				//.should(QueryBuilders.matchQuery("content", keyWord)));
		
		// searchRequestBuilder.setFilter(FilterBuilders.);
        searchRequestBuilder.setFrom(from).setSize(num);
        searchRequestBuilder.setExplain(true);
        
        //searchRequestBuilder.addHighlightedField("title", 5, 50);
        //searchRequestBuilder.addHighlightedField("content", 5, 50);
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
            String tempPagePic = (String) resultTemp.get("pagePic");
            if (null != tempPagePic)
                tempPagePic = tempPagePic.replaceAll("book/[0-9]+/", "http://img3.douban.com/lpic/" );
            resultTemp.put("pagePic", tempPagePic);
            
            retList.add(resultTemp);
        }
        
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	public String netBookSearch(String keyWord, int from, int num) {
		String index = "store";
		String [] types = {"netbook"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        String [] fields = {"name", "authorName", "introduction"};
		searchRequestBuilder.setQuery(QueryBuilders.multiMatchQuery(keyWord, fields));
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
            retList.add(resultTemp);
        }
        
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
	
	
	public String filmSearch(String keyWord, int from, int num, String type) {
		String index = "store";
		String [] types = {"movie"};
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        String [] fields = {"name", "alias", "director", "screenwriter", "actor", "describe"};
        
        searchRequestBuilder.setPostFilter(FilterBuilders.termFilter("type", type));
		searchRequestBuilder.setQuery(QueryBuilders.multiMatchQuery(keyWord, fields));
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
            retList.add(resultTemp);
        }
        
        ObjectMapper objectMaper = new ObjectMapper();
        objectMaper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMaper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = objectMaper.writeValueAsString(retList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
}
