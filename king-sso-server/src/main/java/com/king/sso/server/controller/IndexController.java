package com.king.sso.server.controller;

import com.king.sso.core.conf.Conf;
import com.king.sso.core.exception.KingSsoException;
import com.king.sso.core.user.KingUser;
import com.king.sso.core.util.MiscUtils;
import com.king.sso.core.util.SsoLoginHelper;
import com.king.sso.server.core.entity.UserInfo;
import com.king.sso.server.dao.UserInfoDao;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @Auther: king
 * @Date: 2018/7/26 11:04
 * @Description:
 */
@Controller
public class IndexController {
    @Autowired
    private UserInfoDao userInfoDao;

    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {

        // login check
        KingUser kingUser = SsoLoginHelper.loginCheck(request);

        if (kingUser == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("kingUser", kingUser);
            return "index";
        }
    }

    @RequestMapping(Conf.SSO_LOGIN)
    public String login(Model model, HttpServletRequest request) {
        KingUser kingUser = SsoLoginHelper.loginCheck(request);
        if (kingUser != null) {
            String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
            if (MiscUtils.isNotEmpty(redirectUrl)) {
                String sessionId = SsoLoginHelper.getSessionIdByCookie(request);
                String redirectUrlEnd = redirectUrl.concat("?").concat(Conf.SSO_SESSION_ID).concat("=")
                    .concat(sessionId);
                return "redirect:" + redirectUrlEnd;
            }else {
                return "redirect:/";
            }
        }
        model.addAttribute("errorMsg", request.getParameter("errorMsg"));
        model.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return "login";
    }

    /**
     * Login
     *
     * @param request
     * @param redirectAttributes
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request,
        HttpServletResponse response,
        RedirectAttributes redirectAttributes,
        String username,
        String password) {
        String errorMsg = null;
        // valid
        UserInfo existUser = null;
        try {
            if (StringUtils.isBlank(username)) {
                throw new KingSsoException("Please input username.");
            }
            if (StringUtils.isBlank(password)) {
                throw new KingSsoException("Please input password.");
            }
            existUser = userInfoDao.findByUsername(username);
            if (existUser == null) {
                throw new KingSsoException("username is invalid.");
            }
            if (!existUser.getPassword().equals(password)) {
                throw new KingSsoException("password is invalid.");
            }
        } catch (KingSsoException e) {
            errorMsg = e.getMessage();
        }

        if (StringUtils.isNotBlank(errorMsg)) {
            redirectAttributes.addAttribute("errorMsg", errorMsg);

            redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
            return "redirect:/login";
        }

        // login success
        KingUser kingUser = new KingUser();
        kingUser.setUserid(existUser.getId());
        kingUser.setUsername(existUser.getUsername());

        String sessionId = UUID.randomUUID().toString();

        SsoLoginHelper.login(response, sessionId, kingUser);

        // success redirect
        String redirectUrl = request.getParameter(Conf.REDIRECT_URL);
        if (StringUtils.isNotBlank(redirectUrl)) {
            String redirectUrlFinal = redirectUrl + "?" + Conf.SSO_SESSION_ID + "=" + sessionId;
            return "redirect:" + redirectUrlFinal;
        } else {
            return "redirect:/";
        }
    }

    /**
     * Logout
     *
     * @param request
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(Conf.SSO_LOGOUT)
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {

        // logout
        SsoLoginHelper.loginOut(request, response);

        redirectAttributes.addAttribute(Conf.REDIRECT_URL, request.getParameter(Conf.REDIRECT_URL));
        return "redirect:/login";
    }
}
