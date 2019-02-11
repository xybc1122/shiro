package com.dt.user.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class BaseRedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    // stringRedisTemplate.opsForValue();//操作字符串
    // stringRedisTemplate.opsForHash();//操作hash
    // stringRedisTemplate.opsForList();//操作list
    // stringRedisTemplate.opsForSet();//操作set
    // stringRedisTemplate.opsForZSet();//操作有序set


    /**
     * 定义成功一个方法调用
     *
     * @return
     */
    public ValueOperations<String, String> ForValue() {
        return stringRedisTemplate.opsForValue();
    }

    // String方法无time
    public void setString(String key, Object value) {
        this.setObject(key, value, null);
    }

    // String方法有time
    public void setString(String key, Object value, Long time) {
        this.setObject(key, value, time);
    }

    // List方法
    public void setList(String key, Object value) {
        this.setObject(key, value, null);
    }

    public void setObject(String key, Object value, Long time) {
        // redis几张有效期限 string list set zset hash
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        // 判断类型 存放string
        if (value instanceof String) {
            String strValue = (String) value;
            stringRedisTemplate.opsForValue().set(key, strValue);    // 存入模板
            if (time != null) {
                stringRedisTemplate.opsForValue().set(key, strValue, time, TimeUnit.SECONDS);// 存入模板
            }
            return;
        }
        // 存放list类型
        if (value instanceof List) {
            List<Object> listValue = (List<Object>) value;
            if (listValue != null) {
                stringRedisTemplate.opsForList().leftPush(key, listValue.toString());
            }
        }
    }

    /**
     * 取String
     *
     * @param key
     * @return
     */
    public String getStringKey(String key) {
        if (key != null) {
            return this.ForValue().get(key);
        }
        return null;
    }

    public String getListKey(String key) {
        if (key != null) {
            String leftPop = stringRedisTemplate.opsForList().leftPop(key);
            return leftPop;
        }
        return null;
    }

    //获取key 过期时间
    public Long getTtl(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    //redis 删除数据
    public void delData(String key) {
        if (key != null) {
            stringRedisTemplate.delete(key);
        }
    }

}
