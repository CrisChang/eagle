package com.poison.eagle.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.StringUtils;
/**
 * page
 * @author 温晓宁
 *
 */
public class BookTalkInfo extends BaseDO implements Comparable<BookTalkInfo>,Serializable  {
	/**
	 * serialVersionUID = -5810951055109280046L;
	 */
	private static final long serialVersionUID = -5810951055109280046L;
	private int page;//页码
	private List bookTalk = new ArrayList();//数组为每个书摘的实体HashMap结构，
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public List getBookTalk() {
		return bookTalk;
	}
	public void setBookTalk(List bookTalk) {
		this.bookTalk = bookTalk;
	}
	@Override
	public int compareTo(BookTalkInfo o) {
		if(o.page<=this.page){
			return 1;
		}
		return -1;
	}
	
}
