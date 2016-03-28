package com.poison.eagle.manager.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.keel.utils.ahocorasick.trie.Token;
import com.keel.utils.ahocorasick.trie.Trie;
import com.keel.utils.ahocorasick.trie.Emit;
import com.poison.resource.client.MyBkFacade;


public class SensitiveWordManager {
	private static final  Log LOG = LogFactory.getLog(SensitiveWordManager.class);
	
	private MyBkFacade myBkFacade;
	
	private Trie trie;

	public MyBkFacade getMyBkFacade() {
		return myBkFacade;
	}

	public void setMyBkFacade(MyBkFacade myBkFacade) {
		this.myBkFacade = myBkFacade;
	}

	public Trie getTrie() {
		return trie;
	}

	public void setTrie(Trie trie) {
		this.trie = trie;
	}
	
	public void initMethod(){
		LOG.info("SensitiveWordManager init start!");
		this.trie = new Trie(false).caseInsensitive().removeOverlaps();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isDelete", 0);
		map.put("sensitiveLevel", 1);
		List<String> list = this.myBkFacade.selectSensitiveWord(map);
		
		for(String word : list){
			this.trie.addKeyword(word);
		}
		
		LOG.info("SensitiveWordManager init end!");
	}
	
	public boolean onlyCheckSensitiveWord(String org){
		if(StringUtils.isNotBlank(org)){
			Collection<Emit> emits = trie.parseText(org);
			
			if(! emits.isEmpty()){
				LOG.info("catch sensitive word：" + StringUtils.join(emits, ','));
				return true;
			}
		}
		
		return false;
	}
	
	public String checkSensitiveWord(String org){
		if(StringUtils.isNotBlank(org)){
			Collection<Emit> emits = trie.parseText(org);
			
			return StringUtils.join(emits, ',');
		}
		
		return null;
	}
	
	public String switchSensitiveWord(String org, String switchText){
		if(StringUtils.isBlank(switchText)){
			switchText = "***";
		}
		
	    Collection<Token> tokens = trie.tokenize(org);
	    StringBuffer to = new StringBuffer();
	    for (Token token : tokens) {
	        if (token.isMatch()) {
	        	to.append(switchText);
	        } else {
		        to.append(token.getFragment());
	        }
	    }

	    return to.toString();
	}
}
