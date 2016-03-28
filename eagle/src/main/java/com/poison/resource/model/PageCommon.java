package com.poison.resource.model;

import java.util.List;

import com.keel.common.lang.BaseDO;
import com.poison.eagle.utils.PageUtils;

/**
 * 自定义通用分页实体类
 * @author 陈呈泰
 */
public class PageCommon extends BaseDO{

	/**
	 * PageCommon序列号
	 */
	private static final long serialVersionUID = -3367574150186805103L;
	/**
	 * 当前页码(已知，页面传参数)
	 */
	private int currPage;
	/**
	 * 每页记录数(已知,从配置文件中读取)
	 */
	private int pageSize = PageUtils.getPageSize("pageSize");
	/**
	 * 总页数(计算)
	 */
	private int totalPage = 1;
	/**
	 * 总记录数(已知)
	 */
	private int totalCount = 0;
	/**
	 * 结果集, 泛型
	 */
	private List<?> list;
	
	//提供一个无参的构造方法
	public PageCommon() {}
	
	//提供一个计算数据的有参构造方法	//计算另外三个数据
	public PageCommon(int currPage, int pageSize, int totalCount, List list) {
		this.currPage = currPage;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.list = list;
		if (totalCount == 0) {
			totalPage = 1;
		}else{
			//计算总页数
			if(totalCount % pageSize != 0){
				totalPage = totalCount / pageSize + 1;
			}else{
				totalPage = totalCount / pageSize;
			}
		}
	}
	
	/********** getter/sertter方法  ****************/
	
	public int getCurrPage() {
		return currPage;
	}
	
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<?> getList() {
		return list;
	}
	public void setList(List<?> list) {
		this.list = list;
	}
	
	
}
