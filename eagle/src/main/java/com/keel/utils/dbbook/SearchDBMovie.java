package com.keel.utils.dbbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.keel.utils.dbbook.BaseManager;
import com.poison.store.model.MvInfo;

public class SearchDBMovie extends BaseManager{

	private static final  Log LOG = LogFactory.getLog(SearchDBMovie.class);
	private Map<String, Object> req ;
	private List<Map<String, Object>>reqs ;
	private Map<String,Object> rating;
	
	public List<MvInfo> searchMovie(String path){
	//	public String searchBook(String path){
		 List<MvInfo> list = new ArrayList<MvInfo>();
		  String jsonStr = "";
		try {
			URL url = new URL(path);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			// 设置连接属性
	        httpConn.setConnectTimeout(15000);
	        httpConn.setDoInput(true);
	        httpConn.setRequestMethod("GET");
        // 获取相应码
        int respCode = httpConn.getResponseCode();
        if (respCode == 200)
        {
        	InputStream inputStream=httpConn.getInputStream();
          
            // ByteArrayOutputStream相当于内存输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            // 将输入流转移到内存输出流中
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
            {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            jsonStr = new String(out.toByteArray(),"utf8");
            req =getObjectMapper().readValue(jsonStr,  new TypeReference<Map<String, Object>>(){});
            reqs = (List<Map<String, Object>>) req.get("subjects");
         if(reqs!=null&&reqs.size()>0){
          for(int i=0;i<reqs.size();i++){
        	   MvInfo cDbMV =  new MvInfo();
    	    RegExp regexp = new RegExp();
            rating = (Map<String, Object>) reqs.get(i);
        //   .out.println("**********************************************************************rating="+rating);
            
           // 上传路径 String movieUrl;
            Object movieUrls =rating.get("alt");
            if(movieUrls!=null){
         	   String movieUrl = movieUrls.toString().trim();
         	   if(movieUrl.length()!=0){
         		 //  .out.println("**********************************************************************movieUrl="+movieUrl);
         		  cDbMV.setMovieUrl(movieUrl);
         	   }
         	}
            
          
            // 封面  String moviePic;
           Map<String,Object> moviePic1 = (Map<String, Object>) rating.get("images");   
            Object moviePic2 =  moviePic1.get("small");
           // .out.println(moviePic2);
            if(moviePic2!=null){
            	String moviePic = moviePic2.toString().trim();
            	if(moviePic.length()!=0){
            	//	.out.println("**********************************************************************moviePic="+moviePic);
           		  cDbMV.setMoviePic(moviePic);
            	}
            }
            // 影片名称String name;
           Object name1 = rating.get("title");
           if(name1!=null){
        	   String name = name1.toString().trim();
        	   if(name.length()!=0){
        		//	.out .println("**********************************************************************name="+name);
            		cDbMV.setName(name);
        	   }
           }
            // 分数  String score;
            Map<String,Object> scores1 = (Map<String, Object>) rating.get("rating");
            Object scores2 =  scores1.get("average");
            if(scores2!=null){
            	String score = scores2.toString().trim();
            	if(score.length()!=0){
            	//	.out .println("**********************************************************************score="+score);
            		cDbMV.setScore(score);
            	}
            }
            
            // 导演String director(存入数据库中的导演以","为间隔符！); 
            List<Map<String,Object>> director1 = (List<Map<String,Object>>)rating.get("directors");
            StringBuffer director5 = new StringBuffer();
            if(director1.size()>0){
            	 for(int j=0;j<director1.size();j++){
                 //	.out.println(director1.size());
                 	Map<String,Object> director2 = director1.get(j);
                 //	.out.println(director2.get("name"));
                 	Object director3 = director2.get("name");
                 	if(director3!=null){
                     	String director4 = director3.toString().trim();
                     	if(director4.length()!=0){
                     		director5.append(director4).append(",");
                     	}
                     }
                 }
            }else{
            	director5.append("");
            }
            String director = director5.toString();
          //  .out.println("***********************************************************************director="+director);
            cDbMV.setDirector(director);
          
            // 上映日期 String releaseDate;
            Object releaseDate1 = rating.get("year");
            if(releaseDate1!=null){
            	String releaseDate = releaseDate1.toString().trim();
            	if(releaseDate.length()!=0){
            	//	 .out.println("***********************************************************************releaseDate="+releaseDate);
                     cDbMV.setReleaseDate(releaseDate);
            	}
            }
            
            // 主要演员 String actor; 
            List<Map<String,Object>> actor1 = (List<Map<String,Object>>)rating.get("casts");
            StringBuffer actor5 = new StringBuffer();
            if(actor1.size()>0){
            	for(int actor1i=0;actor1i<actor1.size();actor1i++){
            		  Map<String,Object> actor2 = actor1.get(actor1i);
            		  Object actor3 = actor2.get("name"); 
            		  if(actor3!=null){
            			  String actor4 = actor3.toString().trim();
            			  if(actor4.length()!=0){
            				  actor5.append(actor4).append(",");
            			  }
            		  }
            	}
            }else{
            	actor5.append("");
            }
            String actor = actor5.toString();
           // .out.println("***********************************************************************actor="+actor);
            cDbMV.setActor(actor);
            
            // 分类或者标签String tags;
            Object tags1 = rating.get("genres");
            if(tags1!=null){
            	String tags2 = tags1.toString().trim();
            	if(tags2.length()!=0){
            		String tags3 = tags2.replace("[", "");
            		String tags = tags3.substring(0,tags3.length()-1);
            		//.out.println("***********************************************************************tags="+tags);
            		cDbMV.setTags(tags);
            	}
            }
            
        /*    //票房String boxOffice;
            String boxOffice = "";
            cDbMV.setBoxOffice(boxOffice);*/
            
           // .out.println("*******************到此结束！*******************************************");
            /**
             * c_db_mv表中没有的字段
            String screenWriter;// 编剧
            String userTags;// 用户给打的标签
            String proCountries;// 制作国家或地区
            String language;// 语种
            String about;// 关联的连续剧,二季 三季啥的
            int number;// 连续剧的集数
            String filmTime;// 电影时长或者单集时长
            String alias;// 别名
            String imdbLink;//
            String description;// 描述
            int collTime;// 采集时间
             */
           
           
          list.add(cDbMV);
           }
          inputStream.close();
       }
       }else{
           }
        
           } catch (IOException e) {
    			e.printStackTrace();
    		}
		return list;
	}

/*	public static void main(String[] args) {
		SearchDBMovie searchDBMovie = new SearchDBMovie();
		String MovieName = "鬼子来了";
			String path = "https://api.douban.com/v2/movie/search?q=鬼子来了";
			searchDBMovie.searchMovie(path);
	}*/
}
