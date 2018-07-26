package com.king.sso.core.util;

import com.king.sso.core.conf.Conf;
import com.king.sso.core.store.SsoLoginStore;
import com.king.sso.core.user.KingUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 19:22
 * @Description:
 */
public class SsoLoginHelper {

    public static String getSessionIdByCookie(HttpServletRequest request) {
        return CookiesUtil.getValue(request, Conf.SSO_SESSION_ID);
    }

    public static void setSessionIdInCookie(HttpServletResponse response, String sessonId) {
        if (MiscUtils.isNotEmpty(sessonId)) {
            CookiesUtil.set(response,Conf.SSO_SESSION_ID,sessonId,false);
        }
    }

    public static void removeSessionId(HttpServletRequest request,HttpServletResponse response) {
        CookiesUtil.remove(request,response,Conf.SSO_SESSION_ID);
    }

    public static String getSessionIdByHeader(HttpServletRequest request){
        return request.getHeader(Conf.SSO_SESSION_ID);
    }

    public static KingUser loginCheck(String sessionId){
        if (MiscUtils.isNotEmpty(sessionId)) {
            return SsoLoginStore.get(sessionId);
        }
        return null;
    }

    public static KingUser loginCheck(HttpServletRequest request) {
        String key = getSessionIdByCookie(request);
        if (MiscUtils.isNotEmpty(key)) {
            return loginCheck(key);
        }
        return null;
    }

    public static void loginOut(String sessionId) {
        SsoLoginStore.remove(sessionId);
    }

    public static void loginOut(HttpServletRequest request,HttpServletResponse response){
        String sessionId = getSessionIdByCookie(request);
        if (MiscUtils.isNotEmpty(sessionId)) {
            loginOut(sessionId);
        }
        CookiesUtil.remove(request,response,Conf.SSO_SESSION_ID);
    }

    /**
     * client login (web)
     *
     * @param response
     * @param sessionId
     * @param xxlUser
     */
    public static void login(HttpServletResponse response, String sessionId,
        KingUser xxlUser) {

        SsoLoginStore.put(sessionId, xxlUser);
        CookiesUtil.set(response, Conf.SSO_SESSION_ID, sessionId, false);
    }

    /**
     * client login (app)
     *
     * @param sessionId
     * @param xxlUser
     */
    public static void login(String sessionId,KingUser xxlUser) {
        SsoLoginStore.put(sessionId, xxlUser);
    }
}
