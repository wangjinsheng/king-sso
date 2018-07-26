package com.king.sso.core.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.king.sso.core.user.KingUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 16:57
 * @Description:
 */
public class JsonMapper {
    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();
    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    public static String toJson(Object obj) {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        try {
            return getInstance().readValue(jsonStr, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     *
     * 如需读取集合如List/Map, 且不是List<String>時,
     * TypeReference 可以 通过 new 方法来解决：比如
     *
     *  List<String>  通过 new TypeReference<List<String>>(){} 相对于JavaType 更直观明了些
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String jsonString, TypeReference<T> type) {
        if (MiscUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            T t = (T) getInstance().readValue(jsonString, type);
            return t;
        } catch (IOException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("aaa", "111");
            map.put("bbb", "222");
            String json = toJson(map);
            System.out.println(json);
            System.out.println(toBean(json, Map.class));

            KingUser user = new KingUser();
            user.setOther("test");
            user.setUserid(1);
            user.setUsername("username");
            List<KingUser> userList = new ArrayList<>();
            userList.add(user);
            String result = toJson(userList);

            List<KingUser> userList1 = fromJson(result,new TypeReference<List<KingUser>>(){});
            if (userList1 != null){
                for (KingUser user1:userList1){
                    System.out.println(user1.getUsername());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
