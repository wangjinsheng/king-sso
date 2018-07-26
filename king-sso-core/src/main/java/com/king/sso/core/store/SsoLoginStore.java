package com.king.sso.core.store;

import com.king.sso.core.conf.Conf;
import com.king.sso.core.user.KingUser;
import com.king.sso.core.util.JedisUtil;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 19:16
 * @Description:
 */
public class SsoLoginStore {
    public static KingUser get(String sessionId) {
        String key = redisKey(sessionId);

        Object object = JedisUtil.getObjectValue(key);
        if (object != null) {
            KingUser kingUser = (KingUser) object;
            return kingUser;
        }
        return null;
    }

    public static void remove(String sessionId) {
        String key = redisKey(sessionId);
        JedisUtil.del(key);
    }

    public static void put(String sessionId, KingUser kingUser) {
        String key = redisKey(sessionId);
        JedisUtil.setObjectValue(key, kingUser);
    }
    private static String redisKey(String sessionId) {
        return Conf.SSO_SESSION_ID.concat("#").concat(sessionId);
    }
}
