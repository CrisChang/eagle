package com.poison.eagle.utils.craw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

/**
 * 分析器
 * 
 * @author kyo
 * 
 */
public class AnalyzerUtils {
	public List<String> analysisForList(String data, String rex, Integer tactics) {
		Pattern p;
		if (tactics == null) {
			p = Pattern.compile(rex);
		} else {
			p = Pattern.compile(rex, tactics);
		}
		Matcher m = p.matcher(data);
		ArrayList<String> list = new ArrayList<String>();
		while (m.find()) {
			String tmp = m.group();
			// System.out.println("结果"+i+":"+tmp);
			list.add(tmp);
		}
		return list;
	}

	public String analysis(String data, String rex, Integer tactics) {
		Pattern p;
		if (tactics == null) {
			p = Pattern.compile(rex);
		} else {
			p = Pattern.compile(rex, tactics);
		}
		Matcher m = p.matcher(data);
		if (m.find()) {
			String tmp = m.group();
			// System.out.println("结果:"+tmp);
			return tmp;
		}
		return "";

	}
}
