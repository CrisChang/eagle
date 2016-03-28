package com.poison.paycenter.dao;

import java.util.List;

import com.poison.paycenter.model.LegendHero;

/**
 * 封神榜 dao
 * 
 * @author zhangqi
 * 
 */
public interface LegendHeroDao {

	public int insertIntoLegendHero(LegendHero hero);

	public List<LegendHero> findAllLegendHero();

	public LegendHero findLegendHeroById(long id);

	public LegendHero findLegendHeroByUserId(long userId);

	/**
	 * 更新封神榜信息
	 * 
	 * @param hero
	 * @return
	 */
	public int updateLegendHeroById(LegendHero hero);
	
	public int updateLegendHeroByUserId(LegendHero hero);

	public int deleteLegendHeroById(long id);

	public int deleteLegendHeroByUserId(long userId);

}
