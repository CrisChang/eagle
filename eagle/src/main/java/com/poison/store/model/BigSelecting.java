package com.poison.store.model;

import com.keel.common.lang.BaseDO;

public class BigSelecting extends BaseDO{

	/**
	 * BigSelecting序列号
	 */
	private static final long serialVersionUID = 9219977574878099566L;
	
	private long id;					//主键id
	private String title;			//题目
	private int type;				//选择类型0为单选1为多选
	private String aItem;			//A选项
	private String aAnswer;	//A选项答案
	private float aScore;		//A选项得分
	private String bItem;			//B选项
	private String bAnswer;	//B选项答案
	private float bScore;		//B选项得分
	private String cItem;			//C选项
	private String cAnswer;	//C选项答案
	private float cScore;		//C选项得分
	private String dItem;			//D选项
	private String dAnswer;	//D选项答案
	private float dScore;		//D选项得分
	private String otherItem;	//其他选项
	private int isDelete;			//是否删除
	private long createDate;	//创建日期
	private int flag;				//标示位
	
	public String getaAnswer() {
		return aAnswer;
	}
	public void setaAnswer(String aAnswer) {
		this.aAnswer = aAnswer;
	}
	public float getaScore() {
		return aScore;
	}
	public void setaScore(float aScore) {
		this.aScore = aScore;
	}
	public String getbAnswer() {
		return bAnswer;
	}
	public void setbAnswer(String bAnswer) {
		this.bAnswer = bAnswer;
	}
	public float getbScore() {
		return bScore;
	}
	public void setbScore(float bScore) {
		this.bScore = bScore;
	}
	public String getcAnswer() {
		return cAnswer;
	}
	public void setcAnswer(String cAnswer) {
		this.cAnswer = cAnswer;
	}
	public float getcScore() {
		return cScore;
	}
	public void setcScore(float cScore) {
		this.cScore = cScore;
	}
	public String getdAnswer() {
		return dAnswer;
	}
	public void setdAnswer(String dAnswer) {
		this.dAnswer = dAnswer;
	}
	public float getdScore() {
		return dScore;
	}
	public void setdScore(float dScore) {
		this.dScore = dScore;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getaItem() {
		return aItem;
	}
	public void setaItem(String aItem) {
		this.aItem = aItem;
	}
	public String getbItem() {
		return bItem;
	}
	public void setbItem(String bItem) {
		this.bItem = bItem;
	}
	public String getcItem() {
		return cItem;
	}
	public void setcItem(String cItem) {
		this.cItem = cItem;
	}
	public String getdItem() {
		return dItem;
	}
	public void setdItem(String dItem) {
		this.dItem = dItem;
	}
	public String getOtherItem() {
		return otherItem;
	}
	public void setOtherItem(String otherItem) {
		this.otherItem = otherItem;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	
}
