package com.king.sso.example.config;

import com.king.sso.core.conf.Conf;
import com.king.sso.core.filter.KingSsoFilter;
import com.king.sso.core.util.JedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: king
 * @Date: 2018/7/26 14:01
 * @Description:
 */
@Configuration
public class KingSsoConfig {
    @Value("${king.sso.server}")
    private String kingSsoServer;

    @Value("${king.sso.logout.path}")
    private String kingSsoLogoutPath;

    @Value("${king.sso.redis.address}")
    private String kingSsoRedisAddress;

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        // redis init
        JedisUtil.init(kingSsoRedisAddress);
        // filter
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setName("KingSsoFilter");
        registration.setOrder(1);
        registration.addUrlPatterns("/*");
        registration.setFilter(new KingSsoFilter());
        registration.addInitParameter(Conf.SSO_SERVER, kingSsoServer);
        registration.addInitParameter(Conf.SSO_LOGOUT_PATH, kingSsoLogoutPath);

        return registration;
    }
}
