package com.king.sso.core.conf;

import com.king.sso.core.model.ReturnT;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 15:27
 * @Description:
 */
public class Conf {
    public static final String REDIRECT_URL = "redirect_url";
    public static final String SSO_SESSION_ID = "king_sso_sessionId";
    public static final String SSO_USER = "king_sso_user";
    public static final String SSO_SERVER = "sso_server";
    public static final String SSO_LOGIN = "/login";
    public static final String SSO_LOGOUT = "/logout";
    public static final String SSO_LOGOUT_PATH = "logoutPath";

    public static final ReturnT<String> SSO_LOGIN_FAIL_RESULT = new ReturnT(501, "sso not login.");

}
