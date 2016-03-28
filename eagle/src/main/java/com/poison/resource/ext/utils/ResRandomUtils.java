package com.poison.resource.ext.utils;

import java.util.Random;

public class ResRandomUtils {

	/**
	 * 
	 * <p>
	 * Title: RandomInt
	 * </p>
	 * <p>
	 * Description: 1-10的随机数
	 * </p>
	 * 
	 * @author :changjiang date 2014-12-22 上午11:21:10
	 * @return
	 */
	public static int RandomInt() {
		return new Random().nextInt(10) + 1;
	}

	/**
	 * 命中概率后再返回随机数
	 */
	public static int RandomIntWithChance() {
		Random r = new Random();
		if (2 == r.nextInt(5)) {
			// 20%的概率
			return r.nextInt(10) + 1;
		} else {
			return 0;
		}
	}

	/**
	 * 大数值随机数---200左右
	 */
	public static int BigRandomIntValue() {
		Random r = new Random();
		return 180 + r.nextInt(40);
	}

	/**
	 * 命中概率返回随机数 * scale  由自增几百到 30=>50
	 */
	public static int RandomIntWithChance(int random, int scale) {
		// if (random == 0) {
		// return RandomIntWithChance() * scale;
		// } else {
		// Random r = new Random();
		// // return (r.nextInt(2)+1) * scale;
		// return 30+r.nextInt(20);
		// }
		if (random == 0) {
			return RandomIntWithChance() * scale;
		} else {
			Random r = new Random();
			return 30 + r.nextInt(20);
		}
	}
}
