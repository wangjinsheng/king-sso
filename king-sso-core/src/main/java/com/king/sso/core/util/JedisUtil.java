package com.king.sso.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 17:29
 * @Description:
 */
public class JedisUtil {

    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    private static final int DEFAULT_EXPIRE_TIME = 7200; // 默认过期时间,单位/秒, 60*60*2=2H, 两小时

    public static void init(String address) {
        jedis = getJedisCluster(address);
    }

    private static JedisCluster jedis;

    private static JedisCluster getJedisCluster(String ips) {
        String[] serverArray = ips.split(",");//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
        Set<HostAndPort> nodes = new HashSet<>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMaxTotal(10);
        jedisPoolConfig.setMinIdle(0);
        jedisPoolConfig.setMaxWaitMillis(2000);
        jedisPoolConfig.setTestOnBorrow(true);
        return new JedisCluster(nodes, jedisPoolConfig);
    }

    private static byte[] serialize(Object object){
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        }catch (Exception e){
            logger.error("{}", e);
        }finally {
            try {
                if (oos != null){
                    oos.close();
                }
                if (baos != null){
                    baos.close();
                }
            } catch (Exception e) {
                logger.error("{}",e);
            }

        }
        return null;
    }

    private static Object unserialize(byte[] bytes){
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            logger.error("{}",e);
        }finally {
            try {
                bais.close();
            }catch (Exception e){
                logger.error("{}",e);
            }
        }
        return null;
    }

    public static String setStringValue(String key, String value, int seconds) {
        String result = null;
        try {
            result = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return result;
    }

    /**
     * Set String (默认存活时间, 2H)
     * @param key
     * @param value
     * @return
     */
    public static String setStringValue(String key, String value) {
        return setStringValue(key, value, DEFAULT_EXPIRE_TIME);
    }

    /**
     * Set Object
     *
     * @param key
     * @param obj
     * @param seconds	存活时间,单位/秒
     */
    public static String setObjectValue(String key, Object obj, int seconds) {
        String result = null;
        try {
            result = jedis.setex(key.getBytes(), seconds, serialize(obj));
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return result;
    }
    /**
     * Set Object (默认存活时间, 2H)
     * @param key
     * @param obj
     * @return
     */
    public static String setObjectValue(String key, Object obj) {
        return setObjectValue(key, obj, DEFAULT_EXPIRE_TIME);
    }

    /**
     * Get String
     * @param key
     * @return
     */
    public static String getStringValue(String key) {
        String value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            logger.info("", e);
        }
        return value;
    }

    /**
     * Get Object
     * @param key
     * @return
     */
    public static Object getObjectValue(String key) {
        Object obj = null;
        try {
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null && bytes.length > 0) {
                obj = unserialize(bytes);
            }
        } catch (Exception e) {
            logger.info("", e);
        }
        return obj;
    }

    /**
     * Delete
     * @param key
     * @return Integer reply, specifically:
     * 		an integer greater than 0 if one or more keys were removed
     *      0 if none of the specified key existed
     */
    public static Long del(String key) {
        Long result = null;
        try {
            result = jedis.del(key);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return result;
    }

    /**
     * incrBy	value值加i
     * @param key
     * @param i
     * @return new value after incr
     */
    public static Long incrBy(String key, int i) {
        Long result = null;
        try {
            result = jedis.incrBy(key, i);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return result;
    }

    /**
     * exists
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    public static boolean exists(String key) {
        Boolean result = null;
        try {
            result = jedis.exists(key);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return result;
    }
    /**
     * expire	重置存活时间
     * @param key
     * @param seconds	存活时间,单位/秒
     * @return Integer reply, specifically:
     * 		1: the timeout was set.
     * 		0: the timeout was not set since the key already has an associated timeout (versions lt 2.1.3), or the key does not exist.
     */
    public static long expire(String key, int seconds) {
        Long result = null;
        try {
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return result;
    }

    /**
     * expireAt		设置存活截止时间
     * @param key
     * @param unixTime		存活截止时间戳
     * @return
     */
    public static long expireAt(String key, long unixTime) {
        Long result = null;
        try {
            result = jedis.expireAt(key, unixTime);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return result;
    }

    public static void main(String[] args) {
        JedisUtil.init("10.247.16.139:7115");
        JedisUtil.setStringValue("oall","test",2);
        System.out.println(JedisUtil.getStringValue("oall"));
//        System.out.println(cluster.ttl("oall"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(JedisUtil.getStringValue("oall"));

    }
}
