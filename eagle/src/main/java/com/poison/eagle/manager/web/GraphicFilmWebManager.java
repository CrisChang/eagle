package com.poison.eagle.manager.web; 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.BookInfo;
import com.poison.eagle.entity.MovieInfo;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.manager.PostManager;
import com.poison.eagle.manager.ResourceManager;
import com.poison.eagle.utils.CheckParams;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.MovieUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.Uploader;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.GraphicFilmFacade;
import com.poison.resource.client.PostFacade;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.GraphicFilm;
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
public class GraphicFilmWebManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(GraphicFilmWebManager.class);
	
//	private long id;
	
	private UcenterFacade ucenterFacade;
	private PostFacade postFacade;
	private ActFacade actFacade;
	private String savePath;
	private ResourceManager resourceManager;
	private BkFacade bkFacade;
	private MvFacade mvFacade;
	private GraphicFilmFacade graphicFilmFacade;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private SerializeUtils serializeUtils = SerializeUtils.getInstance();
	private HttpUtils httpUtils = HttpUtils.getInstance();
	private MovieUtils movieUtils = MovieUtils.getInstance();
	
	
	/**
	 * 批量上传图片
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map<String, Object> uploadImageForFilm(HttpServletRequest request,HttpServletResponse response){
		String uploadPath = "";//上传到服务器的图片地址
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> images = new ArrayList<String>();
        response.setContentType("text/html; charset=UTF-8");  
        PrintWriter out = null;
		try {
			request.setCharacterEncoding("UTF-8");  
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
          
        //文件保存目录路径  
        //String savePath = "E:/";  
        //String savePath = this.getServletContext().getRealPath("/") + configPath;  
          
        // 临时文件目录   
        String tempPath = savePath;//this.getServletContext().getRealPath("/") + dirTemp;  
          
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
        String ymd = sdf.format(new Date());  
        //savePath += "/" + ymd + "/";  
        //创建文件夹  
        File dirFile = new File(savePath);  
        if (!dirFile.exists()) {  
            dirFile.mkdirs();  
        }  
          
        //tempPath += "/" + ymd + "/";  
        //创建临时文件夹  
        File dirTempFile = new File(tempPath);  
        if (!dirTempFile.exists()) {  
            dirTempFile.mkdirs();  
        }  
          
        DiskFileItemFactory  factory = new DiskFileItemFactory();  
        factory.setSizeThreshold(20 * 1024 * 1024); //设定使用内存超过5M时，将产生临时文件并存储于临时目录中。     
        factory.setRepository(new File(tempPath)); //设定存储临时文件的目录。     
        ServletFileUpload upload = new ServletFileUpload(factory);  
        upload.setHeaderEncoding("UTF-8");  
        try {  
            List items = upload.parseRequest(request);  
            System.out.println("items = " + items);  
            Iterator itr = items.iterator();  
              
            
            while (itr.hasNext())   
            {  
                FileItem item = (FileItem) itr.next();  
                String fileName = item.getName();  
                long fileSize = item.getSize();  
                if (!item.isFormField()) {  
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();  
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");  
                    String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;  
                    try{  
                        File uploadedFile = new File(savePath, newFileName);  
                        //第二种方法  
                        OutputStream os = new FileOutputStream(uploadedFile);  
                        InputStream is = item.getInputStream();  
                        byte buf[] = new byte[1024];//可以修改 1024 以提高读取速度  
                        int length = 0;    
                        while( (length = is.read(buf)) > 0 ){    
                            os.write(buf, 0, length);    
                        }    
                        System.out.println("os输出："+os.toString());
                        //关闭流    
                        os.flush();  
                        os.close();    
                        is.close();    
                        LOG.info("本地保存地址："+savePath+newFileName);
                        System.out.println("本地保存地址："+savePath+newFileName);
                        String path = httpUtils.uploadFile(savePath+newFileName, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_ARTICLE);
                        images.add(path);
                        map.put("image", path);
                        LOG.info("上传成功！路径："+path);
                        System.out.println("上传成功！路径："+path);  
                        out.print(path);  
                    }catch(Exception e){  
                        e.printStackTrace();  
                    }  
                }else {  
                    String filedName = item.getFieldName();  
                    System.out.println("===============");   
                    System.out.println("FieldName："+filedName);  
                    System.out.println("String："+item.getString());  
                }             
            }   
              
        } catch (FileUploadException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        out.flush();  
        out.close(); 
		
        map.put("images", images);
		return map;
	}
	/**
	 * 上传图片
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map<String, String> uploadImg(HttpServletRequest request){
		String uploadPath = "";//上传到服务器的图片地址
		
		
		Map<String, String> map = httpUtils.uploadImage(request,savePath);
		String savePath = map.get("savePath");
		if("0".equals(map.get("flag"))){
			uploadPath = httpUtils.uploadFile(savePath, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_ARTICLE);
			map.put("uploadPath", uploadPath);
			map.put("error", "上传成功");
		}
		
		
		
		
		return map;
	}
	/**
	 * 某个人的图解电影列表
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map viewGraphicFilmList(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		
		List<GraphicFilm> graphicFilms = graphicFilmFacade.findGraphicFilmByUid(uid);
		
		List<ResourceInfo> resourceInfos = new ArrayList<ResourceInfo>(); 
		resourceInfos = resourceManager.getResponseList(graphicFilms, uid, resourceInfos);
		Collections.sort(resourceInfos);
		request.setAttribute("list", resourceInfos);
		map.put("list", resourceInfos);
		
		
		return map;
	}
	/**
	 * 写图解电影
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map writeGraphicFilm(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		String title = request.getParameter("title");
		String imageUrl = request.getParameter("picUrl");
		String[] images = request.getParameterValues("images");
		String[] titles = request.getParameterValues("titles");
		
		int length = 0;
		if(images != null){
			length = images.length;
		}
		JSONArray array = new JSONArray();
		if(length>0){
			for (int i = 0; i < length; i++) {
				JSONObject object = new JSONObject();
				try {
					object.put("title", titles[i]);
					object.put("url", images[i]);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e.fillInStackTrace());
				}
				array.put(object);
			}
		}
		
		GraphicFilm graphicFilm = graphicFilmFacade.insertGraphicFilm(uid, title, array.toString(), CommentUtils.TYPE_GRAPHIC_FILM, "", imageUrl);
		
		
		//写长文章成功后添加到缓存
		
		map.put("flag", graphicFilm.getFlag());
		
		
		return map;
	}
	/**
	 * 显示修改长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map viewUpdateGrahicFilm(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		Long id = Long.valueOf(request.getParameter("id"));
		
		GraphicFilm graphicFilm = graphicFilmFacade.findGraphicFilmById(id);
		
//		Post post = postFacade.queryByIdName(id);
		String title = graphicFilm.getTitle();
		String imageUrl = graphicFilm.getCover();
		String content = graphicFilm.getContent();
		
		
		List<Map<String, String>> images = new ArrayList<Map<String,String>>();
		try {
			images = getObjectMapper().readValue(content, new TypeReference<List<Map<String, String>>>(){});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		
		int flagint = graphicFilm.getFlag();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			request.setAttribute("id", id);
			request.setAttribute("title", title);
			request.setAttribute("imageUrl", imageUrl);
			request.setAttribute("images", images);
		}
		
		map.put("flag", flagint);
		
		
		return map;
	}
	/**
	 * 修改长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map updateArticle(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		Long id = Long.valueOf(request.getParameter("id"));
		String title = request.getParameter("title");
		String imageUrl = request.getParameter("picUrl");
		String content = request.getParameter("content");
		String summary = request.getParameter("summary");
		if(summary != null && !"".equals(summary)){
			summary = summary.replace("\t", "");
			summary = summary.replaceAll("\\t", "");
		}
		
		UserAllInfo userAllInfo = ucenterFacade.findUserInfo(null, uid);
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat sf = new SimpleDateFormat("MM-dd HH:mm");
		
		content = WebUtils.replaceWHFromImg(content);
		content = WebUtils.HTML_CSS_FOR_ARTICLE_BEGIN+ "<div class=\"title\">"+title
				+"</div><div class=\"author\"><span style=\"float: left;\">作者:"+userAllInfo.getName()+"&nbsp;&nbsp;"
				+sf.format(data)+"</span><span style=\"float: right;\">阅读量:<span id=\"readNum\"></span></span></div><br/>"
				+hasImage(imageUrl)
				+"<hr SIZE=1 color=\"#f0f0f0\"  />" +
				"<div class=\"content content_position\">"+content+"</div>"+WebUtils.HTML_CSS_FOR_ARTICLE_END;
		title = WebUtils.putStringImageVideoAudioToHTML5(title, imageUrl, null, null);
		int flagint = 0;
		String filePath = savePath+UUID.randomUUID()+".html";
		flagint = fileUtils.writeFile(filePath, content);
		Post post = new Post();
		if(flagint == ResultUtils.SUCCESS){
			String url = httpUtils.uploadFile(filePath, HttpUtils.HTTPUTIL_UPLOAD_FILE_URL_FOR_ARTICLE);
			
			post = postFacade.updateByIdPost(id, title, uid, url, summary);//(id, title, uid, url);
		}
		flagint = post.getFlag();
		
		map.put("flag", flagint);
		//修改长文章成功后添加进缓存
		if(post != null && post.getId() != 0){
			resourceManager.setResourceToJedis(post, uid,uid,0l);
		}
		
		return map;
	}
	
	private String hasImage(String imageUrl){
		if(imageUrl == null || "".equals(imageUrl)){
			return "";
		}else{
			return "<img id=\"articlePic\" src=\""+imageUrl+"\" />" ;
		}
	}
	/**
	 * 删除长文章
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map delArticle(HttpServletRequest request,Long uid){
		Map map = new HashMap();
		Long id = Long.valueOf(request.getParameter("id"));
		
		int flagint = 0;
		Post post = postFacade.deleteByIdPost(id);
		flagint = post.getFlag();
		
		map.put("flag", flagint);
		
		//修改长文章成功后添加进缓存
		if(post != null && post.getId() != 0){
			resourceManager.delResourceFromJedis(uid, id, null);//ResourceToJedis(post, uid);
		}
		
		return map;
	}
	/**
	 * 显示图解电影
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map viewGraphicFilm(HttpServletRequest request,Long uid,Long id){
		Map map = new HashMap();
		GraphicFilm graphicFilm = graphicFilmFacade.findGraphicFilmById(id);
		
		String title = graphicFilm.getTitle();
		String imageUrl = graphicFilm.getCover();
		String content = graphicFilm.getContent();
		
		
		List<Map<String, String>> images = new ArrayList<Map<String,String>>();
		try {
			images = getObjectMapper().readValue(content, new TypeReference<List<Map<String, String>>>(){});
		} catch (Exception e) {
			LOG.error(e.getMessage(), e.fillInStackTrace());
		}
		
		
		int flagint = graphicFilm.getFlag();
		if(flagint == ResultUtils.SUCCESS|| flagint == UNID){
			request.setAttribute("id", id);
			request.setAttribute("title", title);
			request.setAttribute("imageUrl", imageUrl);
			request.setAttribute("images", images);
		}
		
		map.put("flag", flagint);
		return map;
	}
	public void setUcenterFacade(UcenterFacade ucenterFacade) {
		this.ucenterFacade = ucenterFacade;
	}
	public void setActFacade(ActFacade actFacade) {
		this.actFacade = actFacade;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public void setPostFacade(PostFacade postFacade) {
		this.postFacade = postFacade;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public void setBkFacade(BkFacade bkFacade) {
		this.bkFacade = bkFacade;
	}
	public void setMvFacade(MvFacade mvFacade) {
		this.mvFacade = mvFacade;
	}
	public void setGraphicFilmFacade(GraphicFilmFacade graphicFilmFacade) {
		this.graphicFilmFacade = graphicFilmFacade;
	}
	
}
