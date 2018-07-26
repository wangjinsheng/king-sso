package com.king.sso.core.util;

import org.apache.commons.codec.binary.Base64;

/**
 * @Auther: king
 * @Date: 2018/7/26 16:12
 * @Description:
 */
public class CodecUtil {

    public static void main(String[] args) {
        String abc = "http://localhost:9090/ayx?xyz=中午好";
        String encode = base64Encode(abc);
        System.out.println("encode > " + encode);
        System.out.println("decode > "  + base64Decode(encode));
    }

    public static String base64Encode(String data) {
        return Base64.encodeBase64URLSafeString(data.getBytes());
    }

    public static String base64Decode(String target) {
        return new String(Base64.decodeBase64(target.getBytes()));
    }
}
