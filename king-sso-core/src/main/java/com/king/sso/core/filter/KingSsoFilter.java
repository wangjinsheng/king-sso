package com.king.sso.core.filter;

import com.king.sso.core.conf.Conf;
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
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 19:39
 * @Description:
 */
public class KingSsoFilter extends HttpServlet implements Filter {
    private static Logger logger = LoggerFactory.getLogger(KingSsoFilter.class);

    private String ssoServer;
    private String logoutPath;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ssoServer = filterConfig.getInitParameter(Conf.SSO_SERVER);
        if (ssoServer!=null && ssoServer.trim().length()>0) {
            logoutPath = filterConfig.getInitParameter(Conf.SSO_LOGOUT_PATH);
        }

        logger.info("KingSsoFilter init.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath();
        String link = request.getRequestURL().toString();

        if (MiscUtils.isNotEmpty(logoutPath) && MiscUtils.equals(path, logoutPath)) {
            SsoLoginHelper.removeSessionId(request,response);
            response.sendRedirect(ssoServer.concat(Conf.SSO_LOGOUT));
            return;
        }

        String sessionId = SsoLoginHelper.getSessionIdByCookie(request);
        KingUser kingUser = SsoLoginHelper.loginCheck(sessionId);
        // valid param user, client login
        if (kingUser == null) {
            //remove old cookie
            SsoLoginHelper.removeSessionId(request,response);
            String paramSessionId = request.getParameter(Conf.SSO_SESSION_ID);
            if (MiscUtils.isNotEmpty(paramSessionId)) {
                kingUser = SsoLoginHelper.loginCheck(paramSessionId);
                if (kingUser != null) {
                    SsoLoginHelper.setSessionIdInCookie(response, paramSessionId);
                }
            }
        }

        if (kingUser == null) {
            String header = request.getHeader("content-type");
            boolean isJson=  header!=null && header.contains("json");
            if (isJson) {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().println(JsonMapper.toJson(Conf.SSO_LOGIN_FAIL_RESULT));
                return;
            }else {
                String loginPageUrl = ssoServer.concat(Conf.SSO_LOGIN).concat("?").concat(Conf.REDIRECT_URL)
                    .concat("=").concat(link);
                response.sendRedirect(loginPageUrl);
                return;
            }
        }

        request.setAttribute(Conf.SSO_USER, kingUser);

        // already login, allow
        filterChain.doFilter(request, response);
        return;
    }
}
