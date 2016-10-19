package com.temoa.startor2.utils;

import java.util.Random;

/**
 * Created by Temoa
 * on 2016/9/23 14:37
 */

public class CommonUtils {

    /**
     * 格式化播放量和收藏量
     *
     * @param number 播放量和收藏量
     */
    public static String formatNumber(String number) {
        if (number == null)
            return null;
        int len = number.length();
        if (len >= 5) {
            switch (len) {
                case 5:
                    return String.format("%s.%s万", number.charAt(0), number.charAt(1));
                case 6:
                    return String.format("%s.%s万", number.substring(0, 2), number.charAt(2));
                case 7:
                    return String.format("%s.%s万", number.substring(0, 3), number.charAt(3));
                default:
                    return "一个亿啊";
            }
        } else {
            return number;
        }
    }

    public static String formatNumber(int number) {
        String strNum = String.valueOf(number);
        return formatNumber(strNum);
    }

    /**
     * 随机获取字符
     *
     * @param length 所需字符多少
     */
    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        Random randGen = new Random();
        char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz").toCharArray();
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
        }
        return new String(randBuffer);
    }
}
