package com.king.sso.example.controller;

import com.king.sso.core.conf.Conf;
import com.king.sso.core.model.ReturnT;
import com.king.sso.core.user.KingUser;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: king
 * @Date: 2018/7/26 14:01
 * @Description:
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(Model model, HttpServletRequest request) {

        KingUser kingUser = (KingUser) request.getAttribute(Conf.SSO_USER);
        model.addAttribute("kingUser", kingUser);
        return "index";
    }

    @RequestMapping("/json")
    @ResponseBody
    public ReturnT<KingUser> json(Model model, HttpServletRequest request) {
        KingUser kingUser = (KingUser) request.getAttribute(Conf.SSO_USER);
        return new ReturnT(kingUser);
    }
}
