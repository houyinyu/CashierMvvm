package com.hby.cashier.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/13
 */
public class EncryptionUtils {

    /**
     *登录密码的加密
     * @param source 密码
     * @param salt 账号
     * @return
     */
    public static String md5(String source, String salt) {
        if (source != null && source.length() != 0) {
            StringBuilder builder = new StringBuilder();
            char[] characters = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            byte[] bytes = source.getBytes();

            try {
                MessageDigest digest = MessageDigest.getInstance("md5");
                if (salt != null) {
                    digest.update(salt.getBytes());
                }

                byte[] targetArr = digest.digest(bytes);

                for (int i = 0; i < targetArr.length; ++i) {
                    byte b = targetArr[i];
                    int lowNum = b & 15;
                    int highNum = b >> 4 & 15;
                    char lowChar = characters[lowNum];
                    char highChar = characters[highNum];
                    builder.append(highChar).append(lowChar);
                }
            } catch (NoSuchAlgorithmException var13) {
                var13.printStackTrace();
            }

            return builder.toString();
        } else {
            return null;
        }
    }
}
