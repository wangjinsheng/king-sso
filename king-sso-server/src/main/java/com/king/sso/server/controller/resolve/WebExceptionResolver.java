package com.king.sso.server.controller.resolve;

import com.king.sso.core.exception.KingSsoException;
import com.king.sso.core.util.JsonMapper;
import com.king.sso.server.core.result.ReturnT;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 *  * 统一异常处理（Controller切面方式实现）
 *
 *      1、@ControllerAdvice：扫描所有Controller；
 *      2、@ControllerAdvice(annotations=RestController.class)：扫描指定注解类型的Controller；
 *      3、@ControllerAdvice(basePackages={"com.aaa","com.bbb"})：扫描指定package下的Controller
 *
 * @Auther: king
 * @Date: 2018/7/26 10:56
 * @Description:
 */
@Component
public class WebExceptionResolver  implements HandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) {
        logger.error("WebExceptionResolver:{}", ex);
        // if json
        boolean isJson = false;
        HandlerMethod method = (HandlerMethod)handler;
        ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
        if (responseBody != null) {
            isJson = true;
        }
        // error result
        ReturnT<String> errorResult = null;
        if (ex instanceof KingSsoException) {
            errorResult = new ReturnT<String>(ReturnT.FAIL_CODE, ex.getMessage());
        } else {
            errorResult = new ReturnT<String>(ReturnT.FAIL_CODE, ex.toString().replaceAll("\n", "<br/>"));
        }

        // response
        ModelAndView mv = new ModelAndView();
        if (isJson) {
            try {
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().print(JsonMapper.toJson(errorResult));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            return mv;
        } else {

            mv.addObject("exceptionMsg", errorResult.getMsg());
            mv.setViewName("/common/common.exception");
            return mv;
        }
    }
}
