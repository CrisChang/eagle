package com.poison.eagle.manager.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.apache.commons.collections4.trie.UnmodifiableTrie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.poison.resource.client.MyBkFacade;
import com.poison.resource.model.Tag;

public class PromptTagManager {
	private static final  Log LOG = LogFactory.getLog(SensitiveWordManager.class);
	
	//默认为影单的tag
	private PatriciaTrie<Item> trie = null;
	
	//书单的tag
	private PatriciaTrie<Item> bkTrie = null;
	
	private MyBkFacade myBkFacade = null;

	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}

	public void initMethod(){
		LOG.info("PromptTagManager init start!");
		long startTS = System.currentTimeMillis();
		
		//初始化影单的tag
		trie = new PatriciaTrie<Item>();
		List<Tag> tags = myBkFacade.findTagByTypeOrderById("400");
		for(Tag tag : tags){
			trie.put(tag.getTagName(), new Item(tag.getId(), tag.getTagName(), tag.getType(), tag.getTagGroup()));
		}
		
		//初始化书单的tag
		bkTrie = new PatriciaTrie<Item>();
		List<Tag> bkTags = myBkFacade.findTagByTypeOrderById("410");
		for(Tag tag : bkTags){
			bkTrie.put(tag.getTagName(), new Item(tag.getId(), tag.getTagName(), tag.getType(), tag.getTagGroup()));
		}

		long durationTS = System.currentTimeMillis() - startTS;
		LOG.info("PromptTagManager init end! ms: " + durationTS);
		
		return;
	}
	
	public PatriciaTrie<Item> chooseTrie(TagType type){
		if(type == TagType.MOVIELIST){
			return this.trie;
		}else if(type == TagType.BOOKLIST){
			return this.bkTrie;
		}else{
			return null;
		}
	}
	
	public String prefixMatch(String key, TagType type){
		PatriciaTrie<Item> trie = chooseTrie(type);
		
		if(null == trie){
			return "";
		}
		
		SortedMap<String, Item> sortedMap = trie.prefixMap(key);
		
		
		List<Item> items = new LinkedList<Item>(sortedMap.values());
		Collections.sort(items, new Comparator<Item>(){  
            public int compare(Item a, Item b){
                int i = a.getTagName().length() - b.getTagName().length();
                
                if(0 == i){
                	return 0;
                }else{
                	return (i > 0) ? 1 : -1;
                }
            }
		});
		
        Map<String, Object> retJson = new HashMap<String, Object>();
        retJson.put("flag", "0");
        retJson.put("count", items.size());
        retJson.put("start", 0);
        retJson.put("total", items.size());
        retJson.put("subjects", items);
		
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
	
	
	public class Item  {
		private long id;
		private String tagName;
		private String type;
		private String tagGroup;
		
		public Item(long id, String tagName, String type, String tagGroup) {
			super();
			this.id = id;
			this.tagName = tagName;
			this.type = type;
			this.tagGroup = tagGroup;
		}
		
		public long getId(){
			return id;
		}

		public String getTagName() {
			return tagName;
		}

		public String getType() {
			return type;
		}

		public String getTagGroup() {
			return tagGroup;
		}
	}
	
	public enum TagType {
	    MOVIELIST, BOOKLIST;
	}
}
