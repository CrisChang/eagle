package com.poison.resource.domain.repository;

import java.util.List;

import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.dao.SerializeDAO;
import com.poison.resource.model.Chapter;
import com.poison.resource.model.ChapterSummary;
import com.poison.resource.model.Serialize;

/**
 * 
 * 类的作用:此方法的作用是封装DAO层中一些方法 
 * 作者:闫前刚
 *  创建时间:2014-7-27下午7:09:53 
 * email:1486488968@qq.com 
 * version: 1.0
 */
public class SerializeDomainRepository {
	private SerializeDAO serializeDAO;
	
	public void setSerializeDAO(SerializeDAO serializeDAO) {
		this.serializeDAO = serializeDAO;
	}

	/**
	 * 
	 * 方法的描述 :此方法的作用完成创建连载、章节(同时)
	 * @param ser
	 * @param ch
	 * @return
	 */
	public int addSerializeChapter(Serialize ser,Chapter ch) {
		serializeDAO.addChapter(ch);
		return serializeDAO.addSerialize(ser);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是创建连载（分别）
	 * @param ser
	 * @return
	 */
	public Serialize addSerialize(Serialize ser){
		long userId = ser.getUid();
		String name = ser.getName();
		Serialize serialize = serializeDAO.findSerializeIsExisted(userId, name);
		if(ResultUtils.DATAISNULL==serialize.getFlag()){
			serializeDAO.addSerialize(ser);
			serialize = serializeDAO.findSerializeById(ser.getId());
			if(null!=serialize){
				serialize.setFlag(ResultUtils.SUCCESS);
			}
		}
		
		return serialize;
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是续写
	 * @param ch
	 * @return
	 */
	public int addOldChapter(Chapter ch){
		return serializeDAO.addOldChapter(ch);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改连载名称
	 * @param endDate更新连载日期
	 * @return
	 */
	public int updateSerialize(Serialize ser,long id){
		int i=serializeDAO.findById(id);
		if(i!=0){return serializeDAO.updateSerialize(ser);
		}else{
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改连载类型
	 * @param endDate更新连载日期
	 * @return
	 */
	public int updateSerializeType(Serialize ser,long id){
		int i=serializeDAO.findById(id);
		if(i!=0){return serializeDAO.updateSerializeType(ser);
		}else{
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改章节名称
	 * @param ch
	 * @param id根据id查询记录
	 * @return
	 */
	public int updateChapter(Chapter ch,long id){
		int i=serializeDAO.queryById(id);
		if(i!=0){
			return serializeDAO.updateChapter(ch);
		}else{
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是修改章节内容
	 * @param ch
	 * @param id根据id查询记录
	 * @return
	 */
	public int updateChapterContent(Chapter ch,long id){
		int i=serializeDAO.queryById(id);
		if(i!=0){
			return serializeDAO.updateChapterContent(ch);
		}else{
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用修改连载简介
	 * @param ser
	 * @param id根据id查询记录
	 * @return
	 */
	public int updateSerializeIntroduce(Serialize ser,long id){
		int i=serializeDAO.findById(id);
		if(i!=0){
			return serializeDAO.updateSerializeIntroduce(ser);
		}else{
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除章节
	 * @param ch
	 * @param id根据id查询记录
	 * @return
	 */
	public int deleteChapter(Chapter ch,long id){
		Chapter Chapter = new Chapter();
		int i=serializeDAO.queryById(id);
		if(i!=0){
			return serializeDAO.deleteChapter(ch);
		}else{
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是删除连载
	 * @param ser
	 * @param id 根据id
	 * @return
	 */
	public int deleteSerialize(Serialize ser,long id){
		int i=serializeDAO.findById(id);
		if(i!=0){
			return serializeDAO.deleteSerialize(ser);
		}else{
			return ResultUtils.DATAISNULL;
		}
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询连载信息
	 * @param ser
	 * @return
	 */
	public List<Serialize> findAllSerialize(long id){
		return serializeDAO.findAllSerialize(id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询类别
	 * @param type 根据类别查询
	 * @return
	 */
	public List<Serialize> findSerialize(String type){
		return serializeDAO.findSerialize(type);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询章节目录
	 * @param 
	 * @return
	 */
	public List<Chapter> findChapter(long parentId){
		return serializeDAO.findChapter(parentId);
	}

	public List<ChapterSummary> findChapterSummary(long parentId){
		return serializeDAO.findChapterSummary(parentId);
	}

	/**
	 * 
	 * 方法的描述 :此方法的作用是查询章节内容
	 * @param id
	 * @return
	 */
	public Chapter findChapterContent(long id){
		return serializeDAO.queryChapterContent(id);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询所的连载信息
	 * @return
	 */
	public List<Serialize>findAllSerialize(){
		return serializeDAO.findAllSerialize();
	}
	/**
	 * 
	 * 方法的描述 :查询所用的章节信息 
	 * @return
	 */
	/*public List<Chapter> findAllChapter(){
		return serializeDAO.findAllChapter();
	}*/
	/**
	 * 
	 * 方法的描述 :此方法的作用是查詢当前用户连载信息
	 * @param uid
	 * @return
	 */
	public List<Serialize> findSerializeByUser(long uid){
		return serializeDAO.findSerializeByUser(uid);
	}
	/**
	 * 
	 * 方法的描述 :此方法的作用是查询当前用户列表
	 * @param userList
	 * @param uid
	 * @return
	 */
	public List<Serialize> findSerializeUserId(List<Long> userList, Long uid){
		return serializeDAO.findSerializeUserId(userList, uid);
	}

	/**
	 * 分页查询连载
	 * @param uid 用户id
	 * @param beginNo 开始
	 * @param pageSize 每页显示数
	 * @return
	 */
	public List<Serialize> findSerializePageByUserId(Long uid, int beginNo,
			int pageSize) {
		return serializeDAO.findSerializePageByUserId(uid, beginNo, pageSize);
	}
	
	/**
	 * 根据用户id查询该用户的连载数量
	 * @param uid 用户id
	 * @return 连载数量
	 */
	public int findSerializeTotalCountByUserId(Long uid) {
		return serializeDAO.findSerializeTotalCountByUserId(uid);
	}
	
	/**
	 * 根据连载id查询该连载详细信息
	 * @param id 连载id
	 * @return 连载信息
	 */
	public Serialize seleceSerializeByid(Long id) {
		return serializeDAO.findSerializeById(id);
	}

	//按章节查询章节
	public Chapter findChapterToPage(Long uid,Long serId, int beginNo) {
		return serializeDAO.findChapterToPage(uid, serId, beginNo);
	}

	//根据连载id查询该用户的章节数量
	public int findChapterTotalCountByUserId(Long serId) {
		return serializeDAO.findChapterTotalCountByUserId(serId);
	}
	
	/**
	 * 
	 * <p>Title: updateSerializeUrl</p> 
	 * <p>Description: 修改连载的封面地址</p> 
	 * @author :changjiang
	 * date 2014-10-16 下午2:55:00
	 * @param ser
	 * @return
	 */
	public Serialize updateSerializeUrl(Serialize ser){
		long id = ser.getId();
		int flag = serializeDAO.updateSerializeUrl(ser);
		Serialize serialize = new Serialize();
		if(ResultUtils.ERROR==flag){
			serialize.setFlag(ResultUtils.ERROR);
			return serialize;
		}
		serialize = serializeDAO.findSerializeById(id);
		return serialize;
	}
}
