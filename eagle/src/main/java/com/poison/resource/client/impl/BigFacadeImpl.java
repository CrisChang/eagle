package com.poison.resource.client.impl;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.poison.eagle.utils.BigConstant;
import com.poison.eagle.utils.ResultUtils;
import com.poison.resource.client.BigFacade;
import com.poison.resource.model.Big;
import com.poison.resource.model.BigLevelValue;
import com.poison.resource.service.BigService;

public class BigFacadeImpl extends SqlMapClientDaoSupport implements BigFacade{

	private static BigService bigService;
	
	public void setBigService(BigService bigService) {
		this.bigService = bigService;
	}

	/**
	 * 查询big详情
	 */
	/*@Override
	public Big findBig(String attribute, String branch, String value) {
		return bigService.findBig(attribute, branch, value);
	}*/

	
	/**
	 * 
	 * <p>Title: getPersonalBigValue</p> 
	 * <p>Description: 得到个人属性的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-26 下午5:57:53
	 * @param constellation 星座
	 * @param city 城市
	 * @param interest 兴趣
	 * @param nickname 昵称
	 * @return
	 */
	public float getPersonalBigValue(String constellation,String city,String interest,String nickname){
		float constellationValue = 0;
		float cityValue = 0;
		float interestValue = 0;
		float nicknameValue = 0;
		float randomValue = 0;
		//获取星座值
		if(null!=constellation||!"".equals(constellation)){
			constellationValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_PERSONAL, BigConstant.PERSONAL_BRANCH_CONSTELLATION, constellation);
		}
		if(null!=city||!"".equals(city)){//城市值
			cityValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_PERSONAL, BigConstant.PERSONAL_BRANCH_CITY, city);
		}
		if(null!=interest||!"".equals(interest)){//爱好值
			interestValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_PERSONAL, BigConstant.PERSONAL_BRANCH_INTEREST, interest);
		}
		if(null!=nickname||!"".equals(nickname)){//昵称值
			nicknameValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_PERSONAL, BigConstant.PERSONAL_BRANCH_INTEREST, interest);
		}
		//获取总属性值
		float totalValue = constellationValue+cityValue+interestValue+nicknameValue+randomValue;
		return totalValue;
	}
	
	/**
	 * 
	 * <p>Title: getMovieBigValue</p> 
	 * <p>Description: 得到电影属性的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-27 下午5:42:36
	 * @return
	 */
	public float getMovieBigValue(String releaseddate,String place,String type,String director){
		float releaseValue = 0;
		float placeValue = 0;
		float typeValue = 0;
		float directorValue = 0;
		float randomValue = 0;
		//上映日期值
		if(null!=releaseddate||!"".equals(releaseddate)){
			releaseValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_MOVIE,BigConstant.MOVIE_BRANCH_RELEASEDDATE,releaseddate);
		}
		if(null!=place||!"".equals(place)){//产地值
			placeValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_MOVIE,BigConstant.MOVIE_BRANCH_PLACE,place);
		}
		if(null!=type||!"".equals(type)){//电影类型值
			typeValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_MOVIE,BigConstant.MOVIE_BRANCH_TYPE,type);
		}
		if(null!=director||!"".equals(director)){//电影导演值
			directorValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_MOVIE,BigConstant.MOVIE_BRANCH_DIRECTOR,director);
		}
			randomValue = Math.round(Math.random()*(20-10)+10);
		
		//获取总属性值
		float totalValue = releaseValue + placeValue + typeValue + directorValue+randomValue;
		return totalValue;
	}
	
	/**
	 * 
	 * <p>Title: getBookBigValue</p> 
	 * <p>Description: 得到书籍属性的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-27 下午5:55:28
	 * @return
	 */
	public float getBookBigValue(String author,String tags){
		float authorValue = 0;
		float tagsValue = 0;
		float randomValue = 0;
		//作者值
		if(null!=author||!"".equals(author)){
			authorValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_BOOK,BigConstant.BOOK_BRANCH_AUTHOR,author);
		}
		if(null!=tags||!"".equals(tags)){//书籍标签值
			tagsValue = getSingleValue(BigConstant.BIG_ATTRIBUTE_BOOK,BigConstant.BOOK_BRANCH_TAGS,tags);
		}
			randomValue = Math.round(Math.random()*(20-10)+10);
		
		//总属性值
		float totalValue = authorValue + tagsValue+randomValue;
		return totalValue;
	}
	
	/**
	 * 
	 * <p>Title: getSingleValue</p> 
	 * <p>Description: 得到一种属性的逼格值</p> 
	 * @author :changjiang
	 * date 2014-9-27 下午3:17:48
	 * @param attribute
	 * @param branch
	 * @param value
	 * @return
	 */
	public static float getSingleValue(String attribute,String branch,String value){
		Big big = bigService.findBig(attribute, branch, value);
		float totalValue = 0;
		if(null!=big){
			float attribute_value = big.getAttributeValue();
			float branch_value = big.getBranchValue();
			totalValue = attribute_value*branch_value;
		}
		return totalValue;
	}

	public BigLevelValue getLevelValue(int level) {
		int flag = ResultUtils.DATAISNULL;
		BigLevelValue bigLevelValue = new BigLevelValue();
		try {
			bigLevelValue = (BigLevelValue) getSqlMapClientTemplate().queryForObject("findBigLevelValueByLevel", level);
			if(bigLevelValue == null){
				bigLevelValue = new BigLevelValue();
			}else{
				flag = ResultUtils.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bigLevelValue = new BigLevelValue();
		}
		bigLevelValue.setFlag(flag);
		
		return bigLevelValue;
	}
	
}
