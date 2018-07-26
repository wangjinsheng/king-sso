package com.king.sso.core.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 15:51
 * @Description:
 */
public class CookiesUtil {
    // 默认缓存时间,单位/秒, 2H
    private static final int COOKIE_MAX_AGE = 60 * 60 * 2;
    // 保存路径,根路径
    private static final String COOKIE_PATH = "/";

    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param ifKeeping
     */
    public static void set(HttpServletResponse response, String key, String value, boolean ifKeeping) {
        int age = ifKeeping?COOKIE_MAX_AGE:-1;
        addCookie(response, key, value, null, COOKIE_PATH, age, true);
    }

    /**
     * 保存
     *
     * @param response
     * @param key
     * @param value
     * @param maxAge
     */
    private static void addCookie(HttpServletResponse response, String key, String value, String domain, String path, int maxAge, boolean isHttpOnly) {
        Cookie cookie = new Cookie(key, value);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }

    /**
     * 查询value
     *
     * @param request
     * @param key
     * @return
     */
    public static String getValue(HttpServletRequest request, String key) {
        Cookie cookie = get(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    private static Cookie get(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                if (MiscUtils.equals(cookie.getName(),key)){
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void remove(HttpServletRequest request,HttpServletResponse response,String key){
        Cookie cookie = get(request,key);
        if (cookie != null){
            addCookie(response,key,"",null,COOKIE_PATH,0,true);
        }
    }
}
