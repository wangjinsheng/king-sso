package com.king.sso.core.filter;

import com.king.sso.core.conf.Conf;
import com.king.sso.core.model.ReturnT;
import com.king.sso.core.user.KingUser;
import com.king.sso.core.util.JsonMapper;
import com.king.sso.core.util.MiscUtils;
import com.king.sso.core.util.SsoLoginHelper;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: king
 * @Date: 2018/7/25 19:41
 * @Description:
 */
public class KingSsoTokenFilter extends HttpServlet implements Filter{
    private static Logger logger = LoggerFactory.getLogger(KingSsoTokenFilter.class);

    private String ssoServer;
    private String logoutPath;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ssoServer = filterConfig.getInitParameter(Conf.SSO_SERVER);
        if (ssoServer!=null && ssoServer.trim().length()>0) {
            logoutPath = filterConfig.getInitParameter(Conf.SSO_LOGOUT_PATH);
        }

        logger.info("KingSsoTokenFilter init.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse respone = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();
        String link = request.getRequestURL().toString();

        String sessionid = SsoLoginHelper.getSessionIdByHeader(request);
        KingUser kingUser = SsoLoginHelper.loginCheck(sessionid);

        if (MiscUtils.isNotEmpty(logoutPath) && MiscUtils.equals(path, logoutPath)) {
            if (kingUser != null) {
                SsoLoginHelper.loginOut(sessionid);
            }
            // response
            respone.setStatus(HttpServletResponse.SC_OK);
            respone.setContentType("application/json;charset=UTF-8");
            respone.getWriter().println(JsonMapper.toJson(new ReturnT(ReturnT.SUCCESS_CODE, null)));
            return;
        }
        // login filter
        if (kingUser == null) {

            // response
            respone.setStatus(HttpServletResponse.SC_OK);
            respone.setContentType("application/json;charset=UTF-8");
            respone.getWriter().println(JsonMapper.toJson(Conf.SSO_LOGIN_FAIL_RESULT));
            return;
        }

        request.setAttribute(Conf.SSO_USER, kingUser);
        filterChain.doFilter(request, respone);
        return;
    }
}
