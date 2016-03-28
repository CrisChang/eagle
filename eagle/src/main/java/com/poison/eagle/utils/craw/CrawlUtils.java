package com.poison.eagle.utils.craw;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.eagle.manager.MovieManager;
import com.poison.eagle.utils.DBMovieDO;
import com.poison.eagle.utils.MovieUtils;
import com.poison.store.model.MvInfo;


public class CrawlUtils {
	AnalyzerUtils analy = new AnalyzerUtils();
	MovieUtils movieUtils = MovieUtils.getInstance();
	private static final  Log log = LogFactory.getLog(CrawlUtils.class);
	
	private String basePath = "http://www.douban.com";			//豆瓣域名
	private String bookBasePath = "http://book.douban.com";		//豆瓣读书二级域名
	private String movieBasePath = "http://movie.douban.com";		//豆瓣电影二级域名
	private static String imgSrcBasePath = "http://img5.douban.com/view/photo/raw/public/"; //豆瓣原图基础路径
	private static String imgBKSrcBasePath = "http://img3.douban.com/mpic/"; //豆瓣图书原图基础路径
	private String baseNewImagePath = "/application/image/"; 		//采集后保存的基础路径
	private SaveNetImage saveImgFile = new SaveNetImage();
	private String celebrityPath = "celebrity/";					//名人
	private String moviePath = "movie/";							//电视
	private String bookPath = "book/";								//书
	private String special = "special";								//特殊
	private int sleepTime = 1000;									//休眠时间
	private int tryCount = 5;										//重试次数
	/**
	 * 豆瓣电影的采集
	 * @param data
	 */
	public MvInfo movie(String url,String data,long movieId){
		
			MvInfo movieDO = new MvInfo();
			//采集电影的基本信息
			System.out.println();
			System.out.println("========================电影"+movieId+"基本信息开始===========================");
			this.crawlMovieBaseInfo(data, movieDO);
			System.out.println("========================电影"+movieId+"基本信息结束===========================");
			
			//采集演职人员列表信息
			System.out.println("========================电影"+movieId+"演职员信息开始===========================");
			this.crawlEmployeeListInfo(data, movieDO);
			System.out.println("========================电影"+movieId+"演职员信息开始===========================");
			
			long subjectId = 0;
			//保存电影信息
			try {
				movieDO.setMovieUrl(url);
				movieDO.setCollTime((int)Calendar.getInstance().getTimeInMillis()/1000);
			} catch (Exception e) {
				System.out.println("保存书评异常:"+e.getMessage());
			}
			return movieDO;

	}
	
	/**
	 * 采集电影的基本信息
	 * @param data
	 * @param movieDO
	 */
	private void crawlMovieBaseInfo(String data,MvInfo movieDO){
		//获取电影的名字
		String nameRex = "(?<=<span property=\"v:itemreviewed\">).*?(?=</span>)";
		String name = analy.analysis(data,nameRex,null);
		movieDO.setName(this.toTrim(name));
		
		//影片描述
		String describeDataRex = "(?<=<div class=\"indent\" id=\"link-report\">).*?(?=<div id=\"related-pic\")";	
		String describeData = analy.analysis(data,describeDataRex,Pattern.DOTALL);
		String describeListRex ="(?<=<span property=\"v:summary\").*?(?=</span>)";
		List<String> describeList = analy.analysisForList(describeData,describeListRex,Pattern.DOTALL);
		String describe = "";
		if(describeList != null){
			if(describeList.size() == 2){
				describe = describeList.get(1);
				if(StringUtils.isNotBlank(describe)){
					describe = analy.analysis(describe,"(?<=\">).*", Pattern.DOTALL);
				}
			}
			if(describeList.size() == 1){
				describe = describeList.get(0);
				if(StringUtils.isNotBlank(describe)){
					describe = analy.analysis(describe,"(?<=\">).*", Pattern.DOTALL);
					
				}
			}
		}
		movieDO.setDescription(this.toTrim(describe));
		
		//综合评分
		String scoreRex = "(?<=<strong class=\"ll rating_num\" property=\"v:average\">).*?(?=</strong>)";
		String score = analy.analysis(data,scoreRex,Pattern.DOTALL);
		movieDO.setScore(score);
		
		//海报
		String imageRex = "(?<=spst/public/).*?(?=.jpg)";
		String imageSrcName = analy.analysis(data,imageRex,null);//获取图片的id
		if(StringUtils.isNotBlank(imageSrcName)){
			String srcPath = this.imgSrcBasePath+imageSrcName+".jpg";
			String targetFileName = imageSrcName+".jpg";
			String targetDirectoryPath = "";
			long srcImageId = 0l;
			String folder = "";
			try{
				srcImageId = Long.parseLong(imageSrcName);
				folder = String.valueOf(srcImageId/100000);
			}catch(Exception e){
				try{
					imageSrcName = imageSrcName.substring(1,imageSrcName.length());
					srcImageId = Long.parseLong(imageSrcName);
					folder = String.valueOf(srcImageId/100000);
				}catch(Exception ex){
					log.error("imgage:"+ex.getMessage());
					folder = this.special;
				}
			}
			//获取新文件夹位置
			targetDirectoryPath = this.baseNewImagePath+this.moviePath+folder+"/";
			SaveNetImageResult imageRs = saveImgFile.save(srcPath, targetDirectoryPath,targetFileName);
			if(imageRs.isSuccess()){
				String moviePic = targetDirectoryPath+targetFileName;
				movieDO.setMoviePic(folder+"/"+moviePic);
			}
		}

		
		//制作国家或地区
		String proCountriesRex = "(?<=制片国家/地区:</span>).*?(?=<br/>)";
		String proCountries = analy.analysis(data,proCountriesRex,null);
		movieDO.setProCountries(this.toTrim(proCountries));
		
		//语言
		String languageRex = "(?<=语言:</span>).*?(?=<br/>)";
		String language = analy.analysis(data,languageRex,null);
		movieDO.setLanguage(this.toTrim(language));
		
		//上映时间  可以换成首播
		String releaseDateRex = "(?<=上映日期:</span> <span\\s{1,5}property=\"v:initialReleaseDate\"\\s{1,5}content=\").*?(?=\")";
		String releaseDate = analy.analysis(data,releaseDateRex,null);
		movieDO.setReleaseDate(this.toTrim(releaseDate));
		
		
		//季的选择
		String aboutRex = "(?<=季数:</span> <select id='season'>).*?(?=</select>)";
		String about = analy.analysis(data,aboutRex,null);
		movieDO.setAbout(about);
		
		//集数
		String numberRex = "(?<=集数:</span>).*?(?=<br/>)";
		String number = analy.analysis(data,numberRex,null);
		movieDO.setAbout(number);
		
		//单集片长
		String filmTimeRex = "(?<=单集片长:</span>).*?(?=<br/>)";
		String filmTimeRex2 = "(?<=片长:</span> <span property=\"v:runtime\" content=\"\\d{1,4}\">).*?(?=<br/>)";
		String filmTime = analy.analysis(data,filmTimeRex,Pattern.DOTALL);
		if(StringUtils.isBlank(filmTime)){
			filmTime = analy.analysis(data,filmTimeRex2,Pattern.DOTALL);
		}
		filmTime = StringUtils.replace(filmTime, "</span>","");
		movieDO.setFilmTime(filmTime);
		
		//别名
		String aliasRex = "(?<=又名:</span>).*?(?=<br/>)";
		String alias = analy.analysis(data,aliasRex,null);
		movieDO.setAlias(this.toTrim(alias));
		
		//国外电影库连接
		String imdbLinkRex = "(?<=IMDb链接:</span> <a href=\").*?(?=\")";
		String imdbLink = analy.analysis(data,imdbLinkRex,null);
		movieDO.setImdbLink(this.toTrim(imdbLink));
		
		//官方标签
		String tagsRex = "(?<=<span property=\"v:genre\">).*?(?=</span>)";
		List<String> tagList = analy.analysisForList(data,tagsRex,null);
		String tags = "";
		if(tagList !=null && tagList.size() > 0){
			tags = tagList.toString();
			if(StringUtils.isNotBlank(tags)){
				tags = tags.substring(1,tags.length()-1);
			}
		}
		movieDO.setTags(tags);
		log.info("官方标签:"+tags);
		
		//用户标签 
		String userTagsRex = "(?<=<div class=\"tags-body\">).*?(?=</div>)";
		String userTags = analy.analysis(data,userTagsRex,Pattern.DOTALL);
		String userTagsListRex = "(?<= class=\"\">).*?(?=<span>)";
		List<String> userTagsList = analy.analysisForList(userTags,userTagsListRex,Pattern.DOTALL);
		String userTagss = "";
		if(userTagsList !=null && userTagsList.size() > 0){
			userTagss = userTagsList.toString();
			if(StringUtils.isNotBlank(userTagss)){
				userTagss = userTagss.substring(1,userTagss.length()-1);
			}
		}
		movieDO.setUserTags(userTagss);
		log.info("用户标签标签:"+tags);
	}
	
	/**
	 * 采集演职人员列表信息
	 * @param data
	 * @param movieDO
	 */
	private void crawlEmployeeListInfo(String data,MvInfo movieDO){
		//导演信息
		String directorRex = "(?<=导演</span>:).*?(</span>)";
		String directorInfo = analy.analysis(data,directorRex,null);
		System.out.println(directorInfo);
		//导演详情连接***
		String directorLinkRex = "(?<=<a href=\"/celebrity/\\d{1,10}/\" rel=\"v:directedBy\">).*?(?=</a>)";
		List<String> directorLink = analy.analysisForList(directorInfo,directorLinkRex,null);
//		this.crawlEmployeeInfo(directorLink);
		//导演名字***
		String directorNameRex = "(?<=>).*?(?=</a>)";
		List<String> directorName = analy.analysisForList(directorInfo,directorNameRex,null);
		if(directorName == null || directorName.size()==0){
			directorNameRex= "(?<=\">).*?(?=</a>)";
			directorName = analy.analysisForList(directorInfo,directorNameRex,null);
		}
		String directorNames = directorName.toString();
		if(StringUtils.isNotBlank(directorNames)){
			directorNames = directorNames.substring(1,directorNames.length()-1);
		}
		movieDO.setDirector(this.toTrim(directorNames));
		log.info("导演们的名字:"+directorNames);
		
		//编剧信息
		String screenwriterRex = "(?<=编剧</span>:).*?(?=</span>)";
		String screenwriterInfo = analy.analysis(data,screenwriterRex,null);
		System.out.println(screenwriterInfo);
		//编剧详情连接***
		String screenwriterLinkRex = "(?<=href=\").*?(?=\")";
		List<String> screenwriterLink = analy.analysisForList(screenwriterInfo,screenwriterLinkRex,null);
//		this.crawlEmployeeInfo(screenwriterLink);
		//编剧名字***
		String screenwriterNameRex = "(?<=<a href=\"/celebrity/\\d{1,10}/\">).*?(?=</a>)";
		List<String> screenwriterName = analy.analysisForList(screenwriterInfo,screenwriterNameRex,null);
		if(screenwriterName == null || screenwriterName.size()==0){
			screenwriterNameRex= "(?<=\">).*?(?=</a>)";
			screenwriterName = analy.analysisForList(screenwriterInfo,screenwriterNameRex,null);
		}
		String screenwriterNames = screenwriterName.toString();
		if(StringUtils.isNotBlank(screenwriterNames)){
			screenwriterNames = screenwriterNames.substring(1,screenwriterNames.length()-1);
		}
		movieDO.setScreenWriter(this.toTrim(screenwriterNames));
		log.info("编剧们的名字:"+screenwriterNames);
		
		//主演信息
		String actorInfoRex = "(?<=主演</span>:).*?(?=</span>)";
		String actorInfo = analy.analysis(data,actorInfoRex,null);
		//主演详情连接***
		String actorLinkRex = "(?<=href=\").*?(?=\")";
		List<String> actorLink = analy.analysisForList(actorInfo,actorLinkRex,null);
//		this.crawlEmployeeInfo(actorLink);
		//主演名字***
		String actorNameRex = "(?<=<a href=\"/celebrity/\\d{1,10}/\" rel=\"v:starring\">).*?(?=</a>)";
		List<String> actorName = analy.analysisForList(actorInfo,actorNameRex,null);
		if(actorName == null || actorName.size()==0){
			actorNameRex= "(?<=\">).*?(?=</a>)";
			actorName = analy.analysisForList(actorInfo,actorNameRex,null);
		}
		String actorNames = actorName.toString();
		if(StringUtils.isNotBlank(actorNames)){
			actorNames = actorNames.substring(1,actorNames.length()-1);
		}
		movieDO.setActor(this.toTrim(actorNames));
		log.info("主演们的名字:"+actorNames);
	}
	/**
	 * 采集演职人员信息
	 * @param data
	 * @param movieDO
	 */
	/*private void crawlEmployeeInfo(List<String> employeeLinkList){
		for(String employeeLine : employeeLinkList){
			
				DBMovieCelebrityDO celeDO = new DBMovieCelebrityDO();
				
				//名人的url
				celeDO.setCeleUrl(employeeLine);
				
				//返回名人信息页
				String data = coll.getDate(movieUtils.getHeaders(),"",tryCount);
				data = StringUtils.replace(data, "\n","");
				//姓名
				String nameRex = "(?<=<h1>).*?(?=</h1>)";
				String name = analy.analysis(data, nameRex, null);
				celeDO.setName(this.toTrim(name));
				log.info("姓名:"+name);
				
				//更多外文名
				String aliasENRex = "(?<=更多外文名</span>:).*?(?=</li>)";
				String aliasEN = StringUtils.trim(analy.analysis(data, aliasENRex,Pattern.DOTALL));
				celeDO.setAliasEN(this.toTrim(aliasEN));
				log.info("更多外文名:"+aliasEN);
				
				//更多中文名
				String aliasCNRex = "(?<=更多中文名</span>:).*?(?=</li>)";
				String aliasCN = StringUtils.trim(analy.analysis(data, aliasCNRex,Pattern.DOTALL));
				celeDO.setAliasCN(this.toTrim(aliasCN));
				log.info("更多中文名:"+aliasCN);
				
				//家庭成员
				String familyRex = "(?<=家庭成员</span>:).*?(?=</li>)";
				String family = StringUtils.trim(analy.analysis(data, familyRex,Pattern.DOTALL));
				celeDO.setFamily(this.toTrim(family));
				log.info("家庭成员:"+family);
				
				//性别
				String sexRex = "(?<=性别</span>:).*?(?=</li>)";
				String sex = StringUtils.trim(analy.analysis(data, sexRex,Pattern.DOTALL));
				celeDO.setSex(sex);
				log.info("性别:"+sex);
				
				//星座
				String constellationRex = "(?<=星座</span>:).*?(?=</li>)";
				String constellation = StringUtils.trim(analy.analysis(data, constellationRex,Pattern.DOTALL));
				celeDO.setConstellation(constellation);
				log.info("星座:"+constellation);
				
				//出生日期
				String birthdayRex = "(?<=出生日期</span>:).*?(?=</li>)";
				String birthday = StringUtils.trim(analy.analysis(data, birthdayRex,Pattern.DOTALL));
				celeDO.setBirthday(this.toTrim(birthday));
				log.info("生日:"+birthday);
				
				//出生地
				String bornAreaRex = "(?<=出生地</span>:).*?(?=</li>)";
				String bornArea = StringUtils.trim(analy.analysis(data, bornAreaRex,Pattern.DOTALL));
				celeDO.setBornArea(this.toTrim(bornArea));
				log.info("出生地:"+bornArea);
				
				//职业
				String occupationRex = "(?<=职业</span>:).*?(?=</li>)";
				String occupation = StringUtils.trim(analy.analysis(data, occupationRex,Pattern.DOTALL));
				celeDO.setOccupation(this.toTrim(occupation));
				log.info("职业:"+occupation);
				
				//imdb连接
				String imdbRex = "(?<=imdb编号</span>:).*?(?=</a>)";
				String imdb = StringUtils.trim(analy.analysis(data, imdbRex,Pattern.DOTALL));
				if(StringUtils.isNotBlank(imdb)){
					imdb = imdb.substring(imdb.lastIndexOf(">")+1);
					celeDO.setImdbId(imdb);	
				}
				log.info("imdb:"+imdb);
				
				//官网
				String webSiteRex = "(?<=官方网站</span>:).*?(?=</a>)";
				String webSite = StringUtils.trim(analy.analysis(data, webSiteRex,Pattern.DOTALL));
				if(StringUtils.isNotBlank(webSite)){
					webSite = webSite.substring(webSite.lastIndexOf(">")+1);
					celeDO.setWebSite(webSite);
				}
				log.info("webSite:"+webSite);
				
				
				//图片
				String srcImagePathRex = "(http://img\\d{1,10}+.douban.com/img/celebrity/large/).*?(.jpg)";
				String srcImagePath = StringUtils.trim(analy.analysis(data, srcImagePathRex,Pattern.DOTALL));
				String srcImageIdStrRex = "(?<=http://img\\d{1,10}+.douban.com/img/celebrity/large/).*?(?=.jpg)";
				String srcImageIdStr = StringUtils.trim(analy.analysis(data, srcImageIdStrRex,Pattern.DOTALL));
				String targetFileName = srcImageIdStr+".jpg";
	 			if(StringUtils.isNotBlank(srcImageIdStr)){
					long srcImageId = 0;
					String folder = "";
					try{
						srcImageId = Long.parseLong(srcImageIdStr);
						folder = String.valueOf(srcImageId/10000);
					}catch(Exception e){
						try{
							srcImageIdStr = srcImageIdStr.substring(1,srcImageIdStr.length());
							srcImageId = Long.parseLong(srcImageIdStr);
							folder = String.valueOf(srcImageId/10000);
						}catch(Exception ex){
							folder = this.special;
							log.error("imgage:"+ex.getMessage());
						}
						
					}
					String targetDirectoryPath = this.baseNewImagePath+this.celebrityPath+folder+"/";
					SaveNetImageResult imageRs = saveImgFile.save(srcImagePath, targetDirectoryPath,targetFileName);
					if(imageRs.isSuccess()){
						String bookPic = this.bookPath+folder+"/"+targetFileName;
						celeDO.setCelePic(bookPic);
						log.info("图片:"+folder+"/"+srcImageIdStr);
					}
					
				}
				
				//简介
				String descListRex = "(?<=<div class=\"bd\">).*?(?=</div>)";
				List<String> descList = analy.analysisForList(data, descListRex,Pattern.DOTALL);
				if(descList !=null && descList.size()>0){
					String introduction = StringUtils.trim(descList.get(1));
					//判断是否直接介绍
					String descAllRex = "(?<=<span class=\"all hidden\">).*?(?=</span>)";
					String descAll = analy.analysis(introduction, descAllRex,null);
					if(StringUtils.isNotBlank(descAll)){
						introduction = this.toTrim(descAll);
						celeDO.setIntroduction(introduction);
					}
					log.info("简介:"+introduction);
				}

				log.info("==================名人完毕====================");
			
		}
		
	}*/
	
	private String toTrim(String data){
		if(StringUtils.isNotBlank(data)){
			data = StringUtils.trim(data);
			data = StringUtils.replace(data, "\n","");
			data = StringUtils.replace(data, "&nbsp;","");
			return data;
		}
		return "";
	}
}
