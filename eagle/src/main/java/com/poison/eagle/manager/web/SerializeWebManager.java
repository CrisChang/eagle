package com.poison.eagle.manager.web; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.poison.act.client.ActFacade;
import com.poison.eagle.entity.ResourceInfo;
import com.poison.eagle.entity.SerializeInfo;
import com.poison.eagle.utils.CommentUtils;
import com.poison.eagle.utils.FileUtils;
import com.poison.eagle.utils.HttpUtils;
import com.poison.eagle.utils.MessageUtils;
import com.poison.eagle.utils.PageUtils;
import com.poison.eagle.utils.ResultUtils;
import com.poison.eagle.utils.SerializeUtils;
import com.poison.eagle.utils.WebUtils;
import com.poison.resource.client.SerializeFacade;
import com.poison.resource.client.impl.GetResourceInfoFacadeImpl;
import com.poison.resource.model.BookList;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.PageCommon;
import com.poison.resource.model.Serialize;
import com.poison.ucenter.client.UcenterFacade;
import com.poison.ucenter.model.UserAllInfo;
import com.poison.ucenter.model.UserInfo;
/**
 * 用户写书单影单manager
 * @author Administrator
 *
 */
public class SerializeWebManager extends BaseManager {
	private static final  Log LOG = LogFactory.getLog(SerializeWebManager.class);
	private Map<String, Object> req ;
	private Map<String, Object> dataq;
	private Map<String, Object> res ;
	private Map<String, Object> datas ;
	
	private JSONObject resJson;
	private String resString;//返回数据
	private String flag = CommentUtils.RES_FLAG_ERROR;//0：成功、1：失败
	private int flagint;
	private String error;
//	private long id;
	
	private List<ResourceInfo> resourceList;
	
	private SerializeFacade serializeFacade;
	private UcenterFacade ucenterFacade;
	private ActFacade actFacade;
	private String savePath;
	
	private FileUtils fileUtils = FileUtils.getInstance();
	private SerializeUtils serializeUtils = SerializeUtils.getInstance();
	private HttpUtils httpUtils = HttpUtils.getInstance();
	
	
	/**
	 * 上传图片
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map uploadSerializeImg(HttpServletRequest request){
		
		
		
		Map<String, String> map = httpUtils.uploadImage(request,savePath);
		String savePath = map.get("savePath");
		Long id = Long.valueOf(map.get("id"));
		
		String uploadPath = httpUtils.uploadFile(savePath, HttpUtils.HTTPUTIL_UPLOAD_IMAGE_URL_FOR_SERIALIZE);
		
		Serialize serialize = new Serialize();
		serialize =serializeFacade.updateSerializeUrl(id, uploadPath);
		
		flagint = serialize.getFlag();
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		
		return datas;
	}
	/**
	 * 写连载
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map writeSerialize(Long id,String title,String describe,String author , String url,String tag,Long uid){
		
		Serialize serialize = new Serialize();
		if(id == 0){
			serialize = serializeFacade.addSerialize(tag, title, author, describe, url, uid);
		}else{
			flagint = serializeFacade.updateSerialize(id, title, uid);
			flagint = serializeFacade.updateSerializeIntroduce(id, describe, uid);
			serializeFacade.updateSerializeType(id, tag, uid);
			serialize.setId(id);
			serialize.setName(title);
		}

		long sid = serialize.getId();
		flagint = serialize.getFlag();
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == UNID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("id", sid);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据

		return datas;
	}
	/**
	 * 写章节
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public Map writeChapter(Long cid,Long id, String title,String content,int isPublish,Long uid){
		Chapter chapter = new Chapter();
		//更新连载
		if(cid == 0){
			
			chapter = serializeFacade.addOldChapter(id, title, WebUtils.putStringToHTML5(content),uid);
			flagint = chapter.getFlag();
		}else{
			flagint = serializeFacade.updateChapter(cid, title, uid);
			flagint = serializeFacade.updateChapterContent(cid, WebUtils.putStringToHTML5(content), uid);
			chapter.setId(cid);
		}
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS || flagint == 0){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("chatperId", chapter.getId());
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
		}
		datas.put("flag", flag);
		//处理返回数据
		
		return datas;
	}
	/**
	 * 修改连载
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String updateSerialize(Long id , String title, String content , String type, Long uid, HttpServletRequest request){
		
		if(CommentUtils.REQ_ISON_TRUE.equals(type)){
			//修改连载
			flagint = serializeFacade.updateSerialize(id, title, uid);
			//flagint = serializeFacade.updateSerializeType(id, type, uid);
			flagint = serializeFacade.updateSerializeIntroduce(id, content, uid);
		}else{
			//修改章节
			flagint = serializeFacade.updateChapter(id, title, uid);
			List reasonList = new ArrayList();
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "0");
			map.put("content", content);
			reasonList.add(map);
			flagint = serializeFacade.updateChapterContent(id, WebUtils.putDataToHTML5(reasonList), uid);
		}
		
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			error = MessageUtils.getResultMessage(flagint);
			request.setAttribute("error", error);
		}
		request.setAttribute("flag",flag);
		
		return flag;
	}
	/**
	 * 删除连载
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String delSerialize(HttpServletRequest request){
		String type = request.getParameter("type");
		long id = Long.valueOf(request.getParameter("id"));
		
		if(CommentUtils.REQ_ISON_TRUE.equals(type)){
			//删除连载
			flagint = serializeFacade.deleteSerialize(id);
		}else{
			//删除章节
			flagint = serializeFacade.deleteChapter(id);
		}
		
		if(flagint == ResultUtils.SUCCESS){
			flag = CommentUtils.RES_FLAG_SUCCESS;
		}else{
			error = MessageUtils.getResultMessage(flagint);
			request.setAttribute("error", error);
		}
		request.setAttribute("flag", flag);
		
		return flag;
	}
	
	/**
	 * 根据连载id查询该连载详细信息
	 * @param id 连载id
	 */
	public String seleceSerializeByid(HttpServletRequest request,HttpServletResponse response){
		long id = Long.valueOf(request.getParameter("id"));
		Serialize serialize = new Serialize();
		serialize = serializeFacade.seleceSerializeByid(id);
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			SerializeInfo serializeInfo = serializeUtils.putSerializeToInfo(serialize, actFacade, ucenterFacade, serializeFacade);
			request.setAttribute("serialize", serializeInfo);
		}else{
			flag = CommentUtils.RES_FLAG_ERROR;
			//error = MessageUtils.getResultMessage(flagint);
			//request.setAttribute("error", error);
		}
		//request.setAttribute("flag", flag);
		return flag;
	}
	
	/**
	 * 连载目录
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewSerializeList(Long uid,HttpServletRequest request,HttpServletResponse response){
		
		List<Serialize> sList = new ArrayList<Serialize>();
		List<SerializeInfo> resList = new ArrayList<SerializeInfo>();
		//用户全部连载目录
		//sList = serializeFacade.findSerializeByUser(uid);
		//用户全部连载目录(分页)
		int currPage = Integer.valueOf(request.getParameter("currPage"));
		int pageSize = PageUtils.getPageSize("pageSize");
		int beginNo = (currPage - 1) * pageSize;
		sList = serializeFacade.findSerializePageByUserId(uid, beginNo, pageSize);
		
		resList = getSerializeResponseLists(sList, resList);
		
		datas = new HashMap<String, Object>();
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			datas.put("list", resList);
			int totalCount = serializeFacade.findSerializeTotalCountByUserId(uid);
			PageCommon page = new PageCommon(currPage, pageSize, totalCount, resList);
			request.setAttribute("page", page);
			
		}else{
			error = MessageUtils.getResultMessage(flagint);
			datas.put("error", error);
			request.setAttribute("error", error);
		}
		datas.put("flag", flag);
		request.setAttribute("flag", flag);
		//处理返回数据
		
		return flag;
	}
	/**
	 * 章节目录
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewChapterList(HttpServletRequest request){
		//连载id
		long id = Long.valueOf(request.getParameter("id"));
		
		List<Chapter> cList = new ArrayList<Chapter>();
		resourceList  = new ArrayList<ResourceInfo>();
		if(!"".equals(id)){
			cList = serializeFacade.findChapter(id);
		}
		
		resourceList = getResponseList(cList, null, resourceList);
		
		Serialize serialize = new Serialize();
		serialize = serializeFacade.seleceSerializeByid(id);
		SerializeInfo serializeInfo = serializeUtils.putSerializeToInfo(serialize, actFacade, ucenterFacade, serializeFacade);
		
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			request.setAttribute("list", resourceList);
			request.setAttribute("serialize", serializeInfo);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			request.setAttribute("error", error);
		}
		
		return flag;
	}
	/**
	 * 章节内容
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String viewChapter(Long uid,HttpServletRequest request, HttpServletResponse response){
		Long id = Long.valueOf(request.getParameter("rid"));
		Long serId = Long.valueOf(request.getParameter("id"));
		Chapter c = new Chapter();
		//c = (Chapter) serializeFacade.findChapterContent(id);

		int currPage = Integer.valueOf(request.getParameter("currPage"));
		
		int beginNo  = (currPage - 1);
		c = serializeFacade.findChapterToPage(uid, serId, beginNo);
		Serialize serialize = new Serialize();
		serialize = serializeFacade.seleceSerializeByid(serId);
		SerializeInfo serializeInfo = serializeUtils.putSerializeToInfo(serialize, actFacade, ucenterFacade, serializeFacade);
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			int totalCount = serializeFacade.findChapterTotalCountByUserId(serId);
			PageCommon page = new PageCommon(currPage, 1, totalCount, null);
			request.setAttribute("page", page);
			request.setAttribute("chapter", c);
			request.setAttribute("serialize", serializeInfo);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			request.setAttribute("error", error);
		}
		request.setAttribute("flag", flag);
		
		return flag;
	}
	/**
	 * 修改章节内容
	 * @param req	从客户端获取到的json数据字符窜
	 * @return
	 */
	public String updateChapter(Long uid,HttpServletRequest request, HttpServletResponse response){
		Long id = Long.valueOf(request.getParameter("rid"));
		Long serId = Long.valueOf(request.getParameter("id"));
		Chapter c = new Chapter();
		c = (Chapter) serializeFacade.findChapterContent(id);
		Serialize serialize = new Serialize();
		serialize = serializeFacade.seleceSerializeByid(serId);
		SerializeInfo serializeInfo = serializeUtils.putSerializeToInfo(serialize, actFacade, ucenterFacade, serializeFacade);
		if(flagint == ResultUtils.SUCCESS|| flagint == CommentUtils.UN_ID){
			flag = CommentUtils.RES_FLAG_SUCCESS;
			request.setAttribute("chapter", c);
			request.setAttribute("serialize", serializeInfo);
		}else{
			error = MessageUtils.getResultMessage(flagint);
			request.setAttribute("error", error);
		}
		request.setAttribute("flag", flag);
		
		return flag;
	}
	/**
	 * 将连载的数据表结构转换成客户端适合输出的类型
	 * @param list
	 * @param type
	 * @return
	 */
	public List<SerializeInfo> getSerializeResponseLists(List<Serialize> reqList  , List<SerializeInfo> resList){
		SerializeInfo si = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (Serialize s : reqList) {
				si = serializeUtils.putSerializeToInfo(s, actFacade, ucenterFacade,serializeFacade);
				resList.add(si);
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			Serialize s = reqList.get(0);
			flagint = (int) s.getFlag();
			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
				long id = s.getId();
				if(id != 0){
					UserAllInfo ui = ucenterFacade.findUserInfo(null, s.getUid());
					si = serializeUtils.putSerializeToInfo(s, actFacade, ucenterFacade,serializeFacade);
					resList.add(si);
				}
			}
		}
		return resList;
	}
//	
	/**
	 * 分组list
	 * @param list
	 * @param type
	 * @return
	 */
	public List getResponseList(List reqList , String type , List<ResourceInfo> resList){
		ResourceInfo ri = null;
		if(reqList.size()>1){
			flagint = ResultUtils.SUCCESS;
			for (Object obj : reqList) {
				ri = fileUtils.putObjectToResource(obj, ucenterFacade,actFacade);
				resList.add(ri);
			}
		}else if(reqList.size() == 0){
			flagint = ResultUtils.SUCCESS;
		}else{
			Object object = reqList.get(0);
			ResourceInfo resourceInfo = fileUtils.putObjectToResource(object,ucenterFacade);
			flagint = resourceInfo.getFlag();
			if(flagint == ResultUtils.SUCCESS || flagint == CommentUtils.UN_ID){
				long id = resourceInfo.getRid();
				if(id != 0){
					ri = fileUtils.putObjectToResource(object, ucenterFacade, actFacade);
					resList.add(ri);
				}
			}
		}
		return resList;
	}
	public void setSerializeFacade(SerializeFacade serializeFacade) {
		this.serializeFacade = serializeFacade;
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
}
