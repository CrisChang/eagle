package com.poison.eagle.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poison.eagle.manager.MovieManager;
import com.poison.eagle.utils.craw.AnalyzerUtils;

public class CheckParams {
	public static final String BOOKPIC = "http://img3.douban.com/lpic/";
	public static final String MOVIEPIC = "http://img3.douban.com/view/photo/photo/public/";
	private static final Log LOG = LogFactory.getLog(CheckParams.class);
	private static final String HTML_STYLE = "<style>div{line-height: 25px;margin: 20px 5px 0 5px;}</style>";
	private static final String HTML_BEGIN = "<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	private static final String HTML_END = "</div>";

	/**
	 *
	 * <p>
	 * Title: objectToStr
	 * </p>
	 * <p>
	 * Description: 把null转成""，去掉字符串的空格
	 * </p>
	 *
	 * @author :changjiang date 2014-8-7 下午4:04:35
	 * @param obj
	 * @return
	 */
	public static String objectToStr(Object obj) {
		if (null == obj) {
			return "";
		} else {
			return obj.toString().trim();
		}
	}

	/**
	 * 转换为数字
	 * @param obj
	 * @return
	 */
	public static int objectToInt(Object obj) {
		if (null == obj||"".equals(obj)||"1".equals(obj)) {
			return 1;
		} else {
			return Integer.valueOf(obj.toString().trim());
		}
	}

	/**
	 *
	 * <p>
	 * Title: objectToFloat
	 * </p>
	 * <p>
	 * Description: 字符串转换为float
	 * </p>
	 *
	 * @author :changjiang date 2014-8-7 下午4:05:58
	 * @param obj
	 * @return
	 */
	public static float stringToFloat(String obj) {
		if (null == obj) {
			return 0;
		} else {
			float i = Float.valueOf(obj.trim());
			return i;
		}
	}

	public static double stringToDouble(String obj) {
		if (null == obj) {
			return 0;
		} else {
			try {
				double i = Double.valueOf(obj.trim());
				return i;
			} catch (Exception e) {
				return 0;
			}
		}
	}

	public static long stringToLong(String obj) {
		if (null == obj) {
			return 0;
		} else {
			long i = Long.valueOf(obj.trim());
			return i;
		}
	}

	/**
	 * <pre>
	 * 获取上映日期 格式为 2014-09-09、2014-09、2014、2014年09月09日、2014年09月、2014年
	 * </pre>
	 *
	 * @return 20140909、20140900、20140000、20140909、20140900、20140000
	 */
	public static double getRealeaseDate(String releaseDateString) {
		double releaseDate = 0;
		StringBuffer sb = new StringBuffer();
		AnalyzerUtils analy = new AnalyzerUtils();
		if (StringUtils.isEmpty(releaseDateString))
			return releaseDate;
		try {
			releaseDateString = releaseDateString.replaceAll(" ", "");
			releaseDateString = releaseDateString.replaceAll("年", "-");
			releaseDateString = releaseDateString.replaceAll("月", "-");
			releaseDateString = releaseDateString.replaceAll("日", "-");
			String rex = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
			String date = analy.analysis(releaseDateString, rex, null);
			if (StringUtils.isEmpty(date)) {
				rex = "[0-9]{4}-[0-9]{1,2}";
				date = analy.analysis(releaseDateString, rex, null);
				if (StringUtils.isEmpty(date)) {
					rex = "[0-9]{4}";
					date = analy.analysis(releaseDateString, rex, null);
					if (StringUtils.isEmpty(date)) {
						return releaseDate;
					} else {
						sb.append(date);
						sb.append("-");
						sb.append("00");
						sb.append("-");
						sb.append("00");
					}
				} else {
					int length = date.trim().length();
					if (length == 7) {
						sb.append(date);
						sb.append("-");
						sb.append("00");
					} else if (length == 6) {
						String[] dates = date.split("-");
						sb.append(dates[0]);
						sb.append("-");
						sb.append("0");
						sb.append(dates[1]);
						sb.append("-");
						sb.append("00");
					}
				}
			} else {
				int length = date.trim().length();
				if (length == 10) {
					sb.append(date);
				} else {
					String[] dates = date.split("-");
					int i = 0;
					for (String proxy : dates) {
						if (i == 0) {
							sb.append(proxy);
						} else if (i == 1 || i == 2) {
							if (proxy.length() == 2) {
								sb.append("-");
								sb.append(proxy);
							} else if (proxy.length() == 1) {
								sb.append("-");
								sb.append("0");
								sb.append(proxy);
							}
						}
						i++;
					}
				}
			}
			String proxy = sb.toString().replaceAll("-", "");
			releaseDate = Double.valueOf(proxy);
		} catch (Exception e) {
			releaseDate = 0;
			e.printStackTrace();
		}
		return releaseDate;
	}

	/**
	 * 获取时间格式 2014-09-09
	 *
	 * @param date
	 * @return
	 */
	public static String getSplitRealeaseDate(String date) {
		StringBuffer sb = new StringBuffer();
		try {
			if (date.length() == 8) {
				sb.append(date.substring(0, 4));
				sb.append("-");
				sb.append(date.substring(4, 6));
				sb.append("-");
				sb.append(date.substring(6, 8));
			}
			if (StringUtils.isEmpty(date) || StringUtils.equals(date, "0")) {
				sb = new StringBuffer("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			sb = new StringBuffer("0");
		}
		return sb.toString();
	}

	/**
	 * 去掉数据中的空格
	 *
	 * @param obj
	 * @return
	 */
	public static String replaceKG(String obj) {
		String result = "";

		if (obj != null && !"".equals(obj)) {
			result = obj.replaceAll(" {2,}", " ");
			result = result.replace("\n", "");
		}

		if ("".equals(result) || result == null) {
			result = "";
		}

		return result;
	}

	/**
	 * 替换掉\n
	 *
	 * @param obj
	 * @return
	 */
	public static String replaceN(String obj) {
		if (obj == null || "".equals(obj)) {
			return obj;
		}
		obj = obj.trim();
		obj = obj.replace("\n", "\\n");

		return obj;

	}

	/**
	 * 读取读书详情时将\n转换为html
	 *
	 * @param obj
	 * @return
	 */
	public static String putDescribeToHTML(String obj) {
		if (obj == null || "".equals(obj)) {
			return obj;
		}
		obj = obj.trim();
		if (obj.indexOf("\\n") > 0) {
			obj = obj.replace("\\n", HTML_END + HTML_BEGIN);
		}

		obj = HTML_STYLE + HTML_BEGIN + obj + HTML_END;

		return obj;
	}

	/**
	 * 将c_db_bk中封面地址修改
	 * 
	 * @param bookPic
	 * @return
	 */
	public static String matcherBookPic(String bookPic) {
		if (null == bookPic || "".equals(bookPic)) {
			return "";
		}
		bookPic = bookPic.replaceAll("book/[0-9]+/", BOOKPIC);
		return bookPic;
	}

	/**
	 * 将c_db_mv中封面地址修改
	 * 
	 * @param moviePic
	 * @return
	 */
	public static String matcherMoviePic(String moviePic) {

		String result = "";
		if (!"".equals(moviePic) && moviePic != null) {
			result = moviePic.replaceAll("[0-9]+//application/image/movie/[0-9]+/", MOVIEPIC);
			result = result.replace("movie_poster_cover/ipst", "photo/photo");
			result = result.replace("spic", "lpic");
		} else {
			return result;
		}

		return result;
	}

	/**
	 * 将c_db_mv中电影地址修改
	 * 
	 * @param moviePic
	 * @return
	 */
	public static String matcherMovieUrl(String movieUrl) {

		String result = "";
		if (!"".equals(movieUrl) && movieUrl != null) {
			result = movieUrl.replace("http://movie.douban.com/movie/", "http://movie.douban.com/subject/");
		} else {
			return result;
		}

		return result;
	}

	/**
	 * 将c_db_mv中剧照地址修改
	 * 
	 * @param moviePic
	 * @return
	 */
	public static String matcherMoviePhoto(String MoviePhoto) {

		String result = "";
		if (!"".equals(MoviePhoto) && MoviePhoto != null) {
			result = MoviePhoto.replace(",\"http://img3.douban.com/pics/morepic.png\"", "");
		} else {
			return result;
		}

		return result;
	}

	/**
	 * 将imdb地址转为imdb值
	 * 
	 * @param moviePic
	 * @return
	 */
	public static String matcherIMDB(String imdbUrl) {

		String result = "";
		if (!"".equals(imdbUrl) && imdbUrl != null) {
			result = imdbUrl.replace("http://www.imdb.com/title/", "");
		} else {
			return result;
		}

		return result;
	}

	/**
	 * 将imdb地址转为imdb值
	 * 
	 * @param moviePic
	 * @return
	 */
	public static String matcherDirector(String director) {

		String result = "";
		if (!"".equals(director) && director != null) {
			result = director.replaceAll("/ <a href=\"/search/\\d{1,10}[0-9]+/\" rel=\"v:directedBy\">", "");
		} else {
			return result;
		}

		return result;
	}

	/**
	 * 将数组数据拼接为字符串
	 * 
	 * @param list
	 * @return
	 */
	public static String putListToString(List<String> list) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				sb.append(list.get(i));
			} else {
				sb.append(list.get(i) + ",");
			}
		}

		return sb.toString();
	}

	/**
	 * 将带有，的字符窜转换为list
	 * 
	 * @param tag
	 * @return
	 */
	public static List<String> getListFromString(String tag) {
		List<String> list = new ArrayList<String>();
		if (tag == null || "".equals(tag)) {
			return list;
		}
		String[] tagS = tag.split(",");
		if (tagS == null) {
			return list;
		}
		for (String string : tagS) {
			list.add(string);
		}

		return list;
	}

	/**
	 * 将库中的名称集合转换为list
	 * 
	 * @param data
	 * @return
	 */
	public static List<String> putStringToList(String data) {
		List<String> list = new ArrayList<String>();
		if (data == null || "".equals(data)) {
			return list;
		}

		if (data.indexOf(WebUtils.FGF) > 0) {
			String[] datas = data.split(WebUtils.FGF);
			list = toList(list, datas);
			return list;
		} else if (data.indexOf(WebUtils.FGF_C) > 0) {
			String[] datas = data.split(WebUtils.FGF_C);
			list = toList(list, datas);
			return list;
		} else if (data.indexOf(",") > 0) {
			String[] datas = data.split(",");
			list = toList(list, datas);
			return list;
		} else if (data.indexOf("/") > 0) {
			String[] datas = data.split("/");
			list = toList(list, datas);
			return list;
		} else if (data.indexOf(" ") > 0) {
			String[] datas = data.split(" ");
			list = toList(list, datas);
			return list;
		} else {
			list.add(data);
			return list;
		}
	}

	private static List<String> toList(List<String> list, String[] datas) {
		if (datas == null || datas.length == 0) {
			return list;
		}
		int index = 5;
		if (datas.length < 6) {
			index = datas.length;
		} else {
			index++;
		}
		for (int i = 0; i < index; i++) {
			try {
				list.add(WebUtils.takeOutPinYin(datas[i].trim()));
			} catch (Exception e) {
				LOG.error(e.getMessage(), e.fillInStackTrace());
			}
		}
		return list;
	}

	/**
	 * 将长文章中的用户产生的数据分离出来
	 * 
	 * @param data
	 * @return
	 */
	public static String getContentFromHtmlByArticle(String data) {
		if (data == null || "".equals(data)) {
			return data;
		}
		String div_begin = "<div class=\"content content_position\">";
		String div_begin2 = "<div class=\"content\">";
		String div_end = "</div></body></html>";

		int i = 0;
		i = data.indexOf(div_begin);
		if (i < 0) {
			i = data.indexOf(div_begin2);
		}
		if (i > 0) {
			data = data.substring(i + div_begin.length());
			data = data.substring(0, data.indexOf(div_end));
			data = data.replace(WebUtils.LAZY_ORIGINALSRC, WebUtils.LAZY_SRC);
		}

		return data;
	}

	/**
	 * 将分数格式化
	 * 
	 * @param score
	 * @return
	 */
	public static String getScore(String score) {
		if ("".equals(score) || score == null) {
			return "0";
		} else {
			score = CheckParams.replaceKG(score);
			if ("".equals(score) || score == null) {
				score = "0";
			}
			return score;
		}
	}

	/**
	 * 将0与1对调
	 * 
	 * @param isTrue
	 * @return
	 */
	public static int changeIntForTrueFalse(int isTrue) {
		int tmp = 0;
		if (isTrue == 0) {
			tmp = 1;
		} else if (isTrue == 1) {
			tmp = 0;
		}

		return tmp;
	}

	/**
	 * jiang [
	 * "http://112.126.68.72/image/common/cbded3a0646fd9901b222f0e71c55e34.png"]
	 * 变成可用String
	 * 
	 * @param data
	 * @return
	 */
	public static String replaceStringOfJson(String data) {
		if (data == null || "".equals(data)) {
			return data;
		}
		data = data.replace("[\"", "");
		data = data.replace("\"]", "");

		return data;
	}

	/**
	 * 把分数修改
	 * 
	 * @param data
	 * @return
	 */
	public static String changeScore(String score) {
		if (score == null) {
			return score;
		}

		score = score.trim();
		score = score.replace("\\n", "");

		if ("".equals(score) || "0".equals(score) || "0.0".equals(score)) {
			return score;
		} else if ("10.0".equals(score)) {
			return "9.8";
		} else {
			score = score.trim();
			score = score.replace("\\n", "");
			float s = Float.valueOf(score);
			s += 0.1f;
			if (s >= 10) {
				s = 10;
			}
			if (s <= 0) {
				s = 0;
			}
			return String.valueOf(s);
		}
	}

	/**
	 * 将库中单一的文字数据转换为list
	 * 
	 * @param data
	 * @return
	 */
	public static List<Map<String, Object>> putHtml5ToList(String data) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (data == null || "".equals(data)) {
			return list;
		}

		if (data.indexOf(WebUtils.FGF) > 0) {
			list = WebUtils.putHTMLToData(data);
			return list;
		} else {
			list = WebUtils.putDataToList(data, WebUtils.TYPE_DIV);
			return list;
		}

	}

	/**
	 * 将info类中字段格式化
	 * 
	 * @param data
	 * @return
	 */
	public static String formatStringForInfo(String data) {
		if (data == null || "".equals(data)) {
			return "";
		}

		return data;
	}

	/**
	 * 
	 * <p>
	 * Title: checkInputByRegex
	 * </p>
	 * <p>
	 * Description: 增则匹配
	 * </p>
	 * 
	 * @author :changjiang date 2015-1-29 下午12:27:00
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean checkInputByRegex(String str, String regex) {
		boolean flag = false;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		flag = matcher.matches();
		return flag;
	}

	/**
	 * 程序分页
	 * 
	 * @param list
	 * @param page
	 *            页码
	 * @param pageSize
	 *            分页数量
	 * @return
	 */
	public static List getListByPage(List list, int page, int pageSize) {
		if (list == null) {
			return new ArrayList();
		}
		// 分页
		int size = 0;
		if (list != null) {
			size = list.size();
		}
		if (page > 0 && size > 0) {
			int beginIndex = pageSize * (page - 1);
			int endIndex = beginIndex + pageSize;
			if (size >= endIndex) {
				list = list.subList(beginIndex, endIndex);
			} else {
				if (size > beginIndex) {
					list = list.subList(beginIndex, size);
				} else {
					list = new ArrayList();
				}
			}
		}

		return list;
	}

	/**
	 * 
	 * <p>
	 * Title: isPhone
	 * </p>
	 * <p>
	 * Description: 是否是手机号
	 * </p>
	 * 
	 * @author :changjiang date 2015-1-29 下午12:27:16
	 * @param str
	 * @return
	 */
	public static boolean isPhone(String str) {
		if (checkInputByRegex(str, "^1[3-9]\\d{9}$")) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		// System.out.println(matcherBookPic("book/17/s1727038.jpg"));
		//
		// System.out.println(matcherMoviePic("21873//application/image/movie/21873/p2187353211.jpg"));
		//
		// System.out.println(matcherIMDB("http://www.imdb.com/title/tt0460649"));
		// System.out.println(matcherDirector("Pamela Fryman,  / <a href=\"/search/Rob%20Greenberg\" rel=\"v:directedBy\">Rob Greenberg"));
		//
		// String data =
		// "《从你的全世界路过》是微博上最会写故事的人张嘉佳献给你的心动故事。\n最初以“睡前故事”系列的名义在网上疯狂流传，几天内达到1,500,000 次转发，超4亿次阅读，引来电影投资方的巨资抢购，转瞬便签下其中5个故事的电影版权。每1分钟，都有人在张嘉佳的故事里看到自己。\n读过睡前故事的人会知道，这是一本纷杂凌乱的书。像朋友在深夜跟你在叙述，叙述他走过的千山万水。那么多篇章，有温暖的，有明亮的，有落单的，有疯狂的，有无聊的，有胡说八道的。当你辗转失眠时，当你需要安慰时，当你等待列车时，当你赖床慵懒时，当你饭后困顿时，应该都能找到一章合适的。\n我希望写一本书，你可以留在枕边、放进书架，或者送给最重要的那个人。\n从你的全世界路过，随便打开一篇就可以了。\n——张嘉佳";
		// System.out.println(replaceN(data));

		// List<String> list = putStringToList("129分钟(中国,大陆),131分钟(美国)");
		// for (String string : list) {
		// System.out.println(string);
		// }
		// System.out.println(matcherMovieUrl("http://movie.douban.com/movie/4268598"));
		// System.out.println(changeScore("0"));
		// putHtml5ToList(null);
		// System.out.println(matcherBookPic("book/98/s9855156.jpg"));
		 System.out.println(objectToStr(1));
		 double d = 0.00;
		 
		 System.out.println(stringToDouble("1000")==0);
		
	}
}
