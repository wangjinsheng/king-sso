package com.king.sso.server.core.config;

import com.king.sso.core.util.JedisUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: king
 * @Date: 2018/7/26 10:28
 * @Description:
 */
@Configuration
public class SsoRedisConfig implements InitializingBean {
    @Value("${redis.address}")
    private String redisAddress;
    @Override
    public void afterPropertiesSet() throws Exception {
        JedisUtil.init(redisAddress);
    }
}
