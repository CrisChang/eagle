package com.poison.eagle.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

public class UserPOIManager {
	private static final  Log LOG = LogFactory.getLog(UserPOIManager.class);
	
	private Client esClient;

	public void setEsClient(Client esClient) {
		this.esClient = esClient;
	}
	
	/**put用户poi信息*/
	public void put(long userId, String lon, String lat, String mark) {
		String index = "user";
		String type = "user_info";
		UpdateResponse response = null;
		try {
			response = esClient.prepareUpdate(index, type, String.valueOf(userId))
			.setDoc(XContentFactory.jsonBuilder()
				.startObject()
			        .latlon("poi", NumberUtils.createDouble(lat), NumberUtils.createDouble(lon))
			        .field("poi_mark", mark)
				.endObject())
			.get();
		} catch (Exception ex) {
        	LOG.error(ex.getMessage(), ex.fillInStackTrace());
		}
        return;
	}
	
	/** 发现周边的人 */
	public List<Map<String, Object>> ring(long userId, String lon, String lat, int radius, int from, int num) {
		String index = "user";
		String type = "user_info";
		String [] types = {type};
		
		Double indexLon = null;
		Double indexLat = null;
		if (StringUtils.isBlank(lon) || StringUtils.isBlank(lat)) {
			GetResponse response = esClient.prepareGet(index, type, String.valueOf(userId))
					.execute().actionGet();
			if(null == response || ! response.isExists()){
				return null;
			}
			
			Map<String, Object> resultTemp = response.getSource();
			if(null == resultTemp.get("poi")){
				return null;
			}

			Map<String, Double> poi = (Map<String, Double>)(resultTemp.get("poi"));
			indexLon = poi.get("lon");
			indexLat = poi.get("lat");
		} else {
			indexLon = NumberUtils.createDouble(lon);
			indexLat = NumberUtils.createDouble(lat);
		}
		
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("user_id", String.valueOf(userId)));
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

            newResultTemp.put("id", String.valueOf(resultTemp.get("user_id")));
			if(null != resultTemp.get("poi")){
				Map<String, Double> tempPoi = (Map<String, Double>)(resultTemp.get("poi"));
				newResultTemp.put("distance", df.format(GeoDistance.PLANE.calculate(tempPoi.get("lat"), tempPoi.get("lon"), indexLat, indexLon, DistanceUnit.METERS)));
			}
            
            retList.add(newResultTemp);
        }
        
        return retList;
	}
	
	/** 发现周边的人 */
	public String findRing(long userId, String lon, String lat, int radius, int from, int num) {
		String index = "user";
		String type = "user_info";
		String [] types = {type};
		
		Double indexLon = null;
		Double indexLat = null;
		if (StringUtils.isBlank(lon) || StringUtils.isBlank(lat)) {
			GetResponse response = esClient.prepareGet(index, type, String.valueOf(userId))
					.execute().actionGet();
			if(null == response || ! response.isExists()){
				return "";
			}
			
			Map<String, Object> resultTemp = response.getSource();
			if(null == resultTemp.get("poi")){
				return "";
			}

			Map<String, Double> poi = (Map<String, Double>)(resultTemp.get("poi"));
			indexLon = poi.get("lon");
			indexLat = poi.get("lat");
		} else {
			indexLon = NumberUtils.createDouble(lon);
			indexLat = NumberUtils.createDouble(lat);
		}
		
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(types);
        searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        boolQueryBuilder.mustNot(QueryBuilders.termQuery("user_id", String.valueOf(userId)));
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

            newResultTemp.put("id", String.valueOf(resultTemp.get("user_id")));
            newResultTemp.put("name", (String) resultTemp.get("user_name"));
            newResultTemp.put("mark", resultTemp.get("poi_mark") == null ? "" : (String) resultTemp.get("poi_mark"));
			if(null != resultTemp.get("poi")){
				Map<String, Double> tempPoi = (Map<String, Double>)(resultTemp.get("poi"));
				newResultTemp.put("distance", df.format(GeoDistance.PLANE.calculate(tempPoi.get("lat"), tempPoi.get("lon"), indexLat, indexLon, DistanceUnit.METERS)));
			}

            retList.add(newResultTemp);
        }
        
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", hits.length);
        retJson.put("start", from);
        retJson.put("total", searchHits.getTotalHits());
        retJson.put("subjects", retList);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        String ret = "";
        try {
			ret = mapper.writeValueAsString(retJson);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
			return "";
		}
        
		return ret;
	}
}
