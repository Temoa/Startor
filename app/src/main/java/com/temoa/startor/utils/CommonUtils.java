package com.temoa.startor.utils;

import java.util.Random;

/**
 * Created by Temoa
 * on 2016/9/23 14:37
 */

public class CommonUtils {

    public static String formatNumber(int number) {
        String str = String.valueOf(number);
        int len = str.length();
        if (len >= 5) {
            switch (len) {
                case 5:
                    return String.format("%s.%s万", str.charAt(0), str.charAt(1));
                case 6:
                    return String.format("%s.%s万", str.substring(0, 2), str.charAt(2));
                case 7:
                    return String.format("%s.%s万", str.substring(0, 3), str.charAt(3));
                default:
                    return "一个亿啊";
            }
        } else {
            return str;
        }
    }

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
