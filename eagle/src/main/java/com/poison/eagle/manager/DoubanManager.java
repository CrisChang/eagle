package com.poison.eagle.manager; 

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import com.keel.utils.dbbook.SearchDBBook;
import com.keel.utils.dbbook.SearchDBMovie;
import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.utils.BaseManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.PageCommon;
import com.poison.resource.model.Post;
import com.poison.resource.model.Serialize;
import com.poison.store.client.BkFacade;
import com.poison.store.client.MvFacade;
import com.poison.store.model.BkInfo;
import com.poison.store.model.MvInfo;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class DoubanManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(DoubanManager.class);
	
//	private long id;
	
	private BookManager bookManager;
	private BkFacade bkFacade;
	
	private MovieManager movieManager;
	private MvFacade mvFacade;
	//根据书名和数目控制查询的条数
	public String saveDouban(String reqs){
		//调用一个方法访问Dban,接受返回值
		
		String res  = bookManager.saveValue(reqs);
		return res;
	}

	public void setBookManager(BookManager bookManager) {
		this.bookManager = bookManager;
	}
	/**
	 * 控制保存豆瓣书的数量
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String controlBKNumbers(String bookName,int number){
		 List<Map<String, Object>> listRetNum = new ArrayList<Map<String, Object>>();
		 String RetString = "";
		 List<BkInfo> listByNum = new ArrayList<BkInfo>();
		 SearchDBBook searchDBBook = new SearchDBBook();
		 int pages;
		 int numbers;
		 if(number>0){
			  numbers = number;
		 }else{
			  numbers = 0;
		 }
		try {
			String path = "https://api.douban.com/v2/book/search?q="+ URLEncoder.encode(bookName, "UTF-8");
			//此方法待更新，
			listByNum = searchDBBook.searchBook(path);
			if(listByNum!=null){
				
				if(listByNum.size()>numbers){
					pages = numbers;
				}else{
						pages=listByNum.size();
				}
				for(int i=0;i<pages;i++){
					Map<String, Object> map = new HashMap<String, Object>();
					BkInfo bkInfo = new BkInfo();
						//根据isbn从本地库中查询
				 	if(bkInfo.getId()==0&&listByNum.get(i).getIsbn()!=null){
				 		bkInfo = bkFacade.findBkInfoByIsbn(listByNum.get(i).getIsbn());}
						if(bkInfo.getId()==0&&listByNum.get(i).getBookUrl()!=null){
							//根据bookurl从本地库中查询
							bkInfo = bkFacade.findBkInfoBybookurl(listByNum.get(i).getBookUrl());
						}
						if(bkInfo.getId()==0){
							bkInfo = bkFacade.insertBkInfo(listByNum.get(i));
						}
						if(bkInfo.getId()!=0){
							map.put("Id", bkInfo.getId());
							map.put("bookName",bkInfo.getName());
							map.put("bookPic", bkInfo.getBookPic());
							map.put("authorName", bkInfo.getAuthorName());
							map.put("press",bkInfo.getPress());
							map.put("publishingTime", bkInfo.getPublishingTime());
							listRetNum.add(map);
						}
					}
			}else{
				  Map<String, Object> map = new HashMap<String, Object>();
				  map.put(null, null);
				 String num = getResponseData(map);
				 return num;
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			String num =e.getMessage();
			e.printStackTrace();
			return num;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", listRetNum);
		RetString = getResponseData(map);
		return RetString;
	}

	/**
	 * 控制保存豆瓣电影的数量
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String controlMVNumbers(String movieName,int number){
		 List<Map<String, Object>> listRetNum = new ArrayList<Map<String, Object>>();
		 String RetString = "";
		 List<MvInfo> listByNum = new ArrayList<MvInfo>();
		 SearchDBMovie searchDBMovie = new SearchDBMovie();
		 int pages;
		 int numbers;
		 if(number>0){
			  numbers = number;
		 }else{
			  numbers = 0;
		 }
		try {
			String path = "https://api.douban.com/v2/movie/search?q="+ URLEncoder.encode(movieName, "UTF-8");
			listByNum = searchDBMovie.searchMovie(path);
if(listByNum!=null){
				
				if(listByNum.size()>numbers){
					pages = numbers;
				}else{
						pages=listByNum.size();
				}
				for(int i=0;i<pages;i++){
					Map<String, Object> map = new HashMap<String, Object>();
					MvInfo mvInfo = new MvInfo();
						//根据movieURL从本地库中查询
				 	if(mvInfo.getId()==0&&listByNum.get(i).getMovieUrl()!=null){
				 		//创建方法根据bookurl为条件去查询数据库表
				 		mvInfo = mvFacade.queryByMvURL(listByNum.get(i).getMovieUrl());}
						if(mvInfo.getId()==0){
							mvInfo = mvFacade.insertMvInfo(listByNum.get(i));
						}
						if(mvInfo.getId()!=0){
							map.put("Id", mvInfo.getId());
							map.put("name",mvInfo.getName());
							map.put("moviePic", mvInfo.getMoviePic());
							map.put("director", mvInfo.getDirector());
							map.put("releaseDate",mvInfo.getReleaseDate());
							listRetNum.add(map);
						}
					}
			}else{
				  Map<String, Object> map = new HashMap<String, Object>();
				  map.put(null, null);
				 String num = getResponseData(map);
				 return num;
			}
		}catch(Exception e){
//			System.out.println(e.getMessage());
			String num =e.getMessage();
			e.printStackTrace();
			return num;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", listRetNum);
		RetString = getResponseData(map);
		return RetString;
		}
	

	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}

	public void setMovieManager(MovieManager movieManager) {
		this.movieManager = movieManager;
	}

	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	
	
}
