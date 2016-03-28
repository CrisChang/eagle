package com.poison.eagle.utils;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: changjiang
 * Date: 15/12/4
 * Time: 11:27
 */
public class RandUtils {


    /**
     * 产生相应的字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0 ; i < length; ++i){
            int number = random.nextInt(62);//[0,62)

            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
