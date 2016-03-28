package com.poison.eagle.manager;

import java.util.Map;

import com.poison.store.client.BkOnlineReadFacade;
import com.poison.store.client.MvStillsFacade;
import com.poison.store.model.MovieStills;

public class CrawlersManager {
	private String doubanFilmSoUrl = "http://movie.douban.com/subject_search?search_text=";
	
	private String dangdangBookSoUrlByISBN = "http://search.dangdang.com/?medium=01&key4=";
	
	private int bkId;
	
	private BkOnlineReadFacade bkOnlineReadFacade;
	
	private MvStillsFacade mvStillsFacade;

	public void setMvStillsFacade(MvStillsFacade mvStillsFacade) {
		this.mvStillsFacade = mvStillsFacade;
	}

	public void setBkOnlineReadFacade(BkOnlineReadFacade bkOnlineReadFacade) {
		this.bkOnlineReadFacade = bkOnlineReadFacade;
	}

	public int getBkId() {
		return bkId;
	}

	public void setBkId(int bkId) {
		this.bkId = bkId;
	}

	public String getDoubanFilmSoUrl(String key) {
		return doubanFilmSoUrl + key;
	}

	public void setDoubanFilmSoUrl(String doubanFilmSoUrl) {
		this.doubanFilmSoUrl = doubanFilmSoUrl;
	}

	public String getDangdangBookSoUrlByISBN(String isbn) {
		return dangdangBookSoUrlByISBN + isbn;
	}

	public void setDangdangBookSoUrlByISBN(String dangdangBookSoUrlByISBN) {
		this.dangdangBookSoUrlByISBN = dangdangBookSoUrlByISBN;
	}
	
	/**
	 * 
	 * <p>Title: insertBkOnlineRead</p> 
	 * <p>Description: 插入试读</p> 
	 * @author :changjiang
	 * date 2014-8-29 下午11:34:24
	 * @param bkId
	 * @param onlineRead
	 * @return
	 */
	public int insertBkOnlineRead(int bkId,String onlineRead){
		return bkOnlineReadFacade.insertBkOnlineRead(bkId, onlineRead);
	}
	
	/**
	 * 
	 * <p>Title: updateMvStills</p> 
	 * <p>Description: 更新电影的剧照信息</p> 
	 * @author :changjiang
	 * date 2014-8-30 下午2:16:30
	 * @param movieStills
	 * @param mvId
	 * @return
	 */
	public MovieStills updateMvStills(String movieStills, long mvId) {
		return mvStillsFacade.updateMvStills(movieStills, mvId);
	}
	
	/**
	 * 
	 * <p>Title: updateMvOther</p> 
	 * <p>Description: 更新电影的预告片</p> 
	 * @author :changjiang
	 * date 2014-8-30 下午2:17:16
	 * @param other
	 * @param mvId
	 * @return
	 */
	public MovieStills updateMvOther(String other, long mvId){
		return mvStillsFacade.updateMvOther(other, mvId);
	}
}
