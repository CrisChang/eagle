package com.keel.utils.dbbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.keel.utils.dbbook.BaseManager;
import com.poison.store.model.BkInfo;


public class SearchDBBook extends BaseManager{
	
	private static final  Log LOG = LogFactory.getLog(SearchDBBook.class);
	private Map<String, Object> req ;
	private List<Map<String, Object>>reqs ;
	private Map<String,Object> rating;
	private List<Map<String,Object>> tags;
	public List<BkInfo> searchBook(String path){
	//	public String searchBook(String path){
		 List<BkInfo> list = new ArrayList<BkInfo>();
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
            //.out.println("JSON串是："+req);
            reqs = (List<Map<String, Object>>) req.get("books");
            //.out.println("reqs.size()="+reqs.size());
         if(reqs!=null&&reqs.size()>0){
           for(int i=0;i<reqs.size();i++){
        	   BkInfo cDbBk =  new BkInfo();
    	    RegExp regexp = new RegExp();
            rating = (Map<String, Object>) reqs.get(i);
        //   .out.println("**********************************************************************rating="+rating);
          //作者名称
           Object autho =rating.get("author");
           if(autho!=null){
        	   String author = autho.toString().replace("]", "").replace("[", "").trim();
        	   if(author.length()!=0){
        		 //  .out.println("**********************************************************************author="+author);
            	   cDbBk.setAuthorName(author);
        	   }
        	}
        
           //average
           Object averag =rating.get("rating");
              if(averag!=null){
            	  String average=  averag.toString().trim();
            	  if(average.length()!=0){
            		  int ii = average.indexOf("average=");
                      int iii =average.indexOf("min=");
                      String averages = average.substring(ii, iii);
                      int iiii = averages.indexOf("=");
                      int iiiii=averages.lastIndexOf(",");
                     String averags = averages.substring(iiii+1,iiiii);
                 //    .out.println("**********************************************************************averag="+averags);
            	   cDbBk.setScore(averags);
            	  }
           }
           //coll_time
          Integer collTime = new Long(Calendar.getInstance().getTimeInMillis()/1000).intValue();
          //.out.println("**********************************************************************collTime="+collTime);
          cDbBk.setCollTime(collTime);
          
           //subtitle
          Object  subtitl =rating.get("subtitle");
           if(subtitl!=null){
        	  String subtitle = subtitl.toString().trim();
        	  if(subtitle.length()!=0){
        		  //.out.println("**********************************************************************subtitle="+subtitle);
           	   cDbBk.setSubtitle(subtitle);
        	  }
               }
         
           //pubdate
           Object pubdat =rating.get("pubdate");
       //    .out.println("**********************************************************************pubdate="+pubdat);
           if(pubdat!=null){
        	   String pubdate = pubdat.toString().trim();
        	   if(pubdate.length()!=0){
        		   cDbBk.setPublishingTime(pubdate);
        	   }
        	}
         
           //tags
           tags= (List<Map<String, Object>>) rating.get("tags");
           if(tags!=null&&tags.size()!=0){
        	   String names= "";
        	   String nameed="";
               for(int j=0;j<tags.size();j++){
            	  Object named = tags.get(j).get("name");
            	   if(named!=null){
            		   names = named.toString().trim();
            		   if(names.length()!=0){
            			   nameed =nameed +","+names ;
            		   }
            	   }
               }
               String tags=nameed.substring(0,nameed.length()).trim();
             //  .out.println("**********************************************************************tags="+tags);
               cDbBk.setTags(tags);
             }
       
           //original_name
           Object originalNames = rating.get("origin_title");
           if(originalNames!=null){
        	   String originalName = originalNames.toString().trim();
        	   if(originalName.length()!=0){
        		//   .out.println("**********************************************************************originalName="+originalName);
            	   cDbBk.setOriginalName(originalName);
        	   }
               }
         
           //image
           Object images = rating.get("image");
           if(images!=null){
        	   String image = images.toString().trim();
        	   if(image.length()!=0){
        		//   .out.println("**********************************************************************image="+image);
            	//   cDbBk.setPagePic(image);
            	   cDbBk.setBookPic(image);
        	   }
             }
          
           //binding
           Object bindings = rating.get("binding");
           if(bindings!=null){
        	   String binding = bindings.toString().trim();
        	   if(binding.length()!=0){
        		//   .out.println("**********************************************************************binding="+binding);
            	   cDbBk.setBinding(binding);
        	   }
              }
         
           //translator
           Object translators =rating.get("translator");
          if(translators!=null){
        	  String translator =translators.toString().replace("]", "").replace("[", "").trim();
        	  if(translator.length()!=0){
        		//  .out.println("**********************************************************************translator="+translator);
            	  cDbBk.setTranslator(translator);
        	  }
             }
         
          //catalog
          Object catalogs = rating.get("catalog");
          if(catalogs!=null){
        	  String catalog = catalogs.toString().trim();
        	  if(catalog.length()!=0){
        		//  .out.println("**********************************************************************catalog="+catalog);
            	  cDbBk.setCatalog(catalog);
        	  }
              }
        
         
          //publisher
          Object publishers = rating.get("publisher");
          if(publishers!=null){
        	  String publisher = publishers.toString().trim();
        	  if(publisher.length()!=0){
        		  //  .out.println("**********************************************************************publisher="+publisher);
            	    cDbBk.setPress(publisher);
        	  }
               }
        
      
          //isbn13
          Object isbn133 = rating.get("isbn13");
         // .out.println("**********************************************************************isbn13="+isbn133);
        if(isbn133!=null){
        	String isbn13 = isbn133.toString().trim();
        	if(isbn13.length()!=0){
        		// cDbBk.setIsbn13(isbn13);
        		cDbBk.setIsbn(isbn13);
        	}
        	 }
     
          //title
        Object titles = rating.get("title");
          if(titles!=null){
        	  String title = titles.toString().trim();
        	  if(title.length()!=0){
        		//   .out.println("**********************************************************************title="+title);
              	  cDbBk.setName(title);
        	  }
          }
         
          //author_intro
          Object author_intros = rating.get("author_intro");
          if(author_intros!=null){
        	  String author_intro = author_intros.toString().trim();
        	  if(author_intro.length()!=0){
        	//	  .out.println("**********************************************************************author_intro="+author_intro);
            	  cDbBk.setAuthorInfo(author_intro);
        	  }
              }
         
          
          //summary
          Object summarys = rating.get("summary");
          if(summarys!=null){
        	  String summary = summarys.toString().trim();
        	  if(summary.length()!=0){
        		//  .out.println("**********************************************************************summary="+summary);
            	  cDbBk.setContent(summary);
        	  }
            }
         
          //price
          Object prices = rating.get("price");
          if(prices!=null){
        	   String price = prices.toString().trim();
        	   if(price.length()!=0){
        		//   .out.println("**********************************************************************price="+price);
             	  cDbBk.setPrice(price);
        	   }
             }
         
          //alt
          Object book_urls = rating.get("alt");
          if(book_urls!=null){
        	  String book_url = book_urls.toString().trim();
        	  if(book_url.length()!=0){
        		//  .out.println("**********************************************************************book_url="+book_url);
            	  //cDbBk.setBookurl(book_url);
        		  cDbBk.setBookUrl(book_url);
        	  }
              }
         
          //pages
          Object pagee = rating.get("pages");
          if(pagee!=null){
        	 // .out.println("**********************************************************************pagee="+pagee);
        	  String page = pagee.toString().trim();
        	  if(page.length()!=0){
        		  if( regexp.isNumeric(page)){
        			      int pages = Integer.parseInt(page);
                	//	  .out.println("**********************************************************************pages="+pages);
                    	 // cDbBk.setNumber(pages);
                    	  cDbBk.setNumber(pages);
        			  }else{
        				  String temp = regexp.reNumeric(page);
        				  int pages = Integer.parseInt(temp);
                		//  .out.println("**********************************************************************pages="+pages);
                    	  //cDbBk.setNumber(pages);
                		  cDbBk.setNumber(pages);
        			  }
        		  }
        	  }
          list.add(cDbBk);
            }
          inputStream.close();
       }
       }else{
        //	   .out.println("连接失败！");
           }
        
           } catch (IOException e) {
    			e.printStackTrace();
    		}
		return list;
	}

}
