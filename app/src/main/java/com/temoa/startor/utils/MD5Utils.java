package com.temoa.startor.utils;

import java.security.MessageDigest;

/**
 * Created by Temoa
 * on 2016/9/5 22:12
 */
public class MD5Utils {

    private MD5Utils() {

    }

    public static String strToMD5(String string) {
        String md5Str;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] input = string.getBytes();
            byte[] buff = md5.digest(input);
            md5Str = bytesToHex(buff);
            return md5Str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 二进制转16进制
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder md5str = new StringBuilder();
        //把数组每一字节换成16进制连成md5字符串
        int digital;
        for (byte aByte : bytes) {
            digital = aByte;
            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString();
    }
}
