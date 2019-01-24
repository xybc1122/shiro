package com.dt.user.utils;

import com.dt.user.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * jwt工具类
 */
public class JwtUtils {

    public static final String SUBJECT = "videoToken";
    /**
     * 毫秒数 过期时间 当前为一周
     */
    public static final long EXPIRE = 1000 * 60 * 60 * 24 * 7;

    public static final String APPSECRET = "boot999";

    /**
     * 生成jwt token
     * @return
     */
    public static String appKeyToken(String key) {
        if (key == null) {
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("appKey", key)
                .setIssuedAt(new Date())//设置新的时间
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();
        return token;
    }


    /**
     * 生成jwt token
     *
     * @param user
     * @return
     */
    public static String genJsonWebToken(UserInfo user) {
        if (user == null || user.getUid() == null || user.getUserName() == null) {
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", user.getUid())
                .claim("name", user.getUserName())
                .claim("status", user.getStatus())
                .claim("roles", user.getRoles())
                .setIssuedAt(new Date())//设置新的时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))//过期时间
                .signWith(SignatureAlgorithm.HS256, APPSECRET).compact();
        return token;
    }

    /**
     * 校验token 解密
     *
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {

        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

    //jwt解析
    public static UserInfo jwtUser(String token) {
        UserInfo userIfo = null;
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.checkJWT(token);
            if (claims != null) {
                userIfo = new UserInfo();
                Integer id = (Integer) claims.get("id");
                String name = (String) claims.get("name");
                int status = (Integer) claims.get("status");
                userIfo.setUid(id.longValue());
                userIfo.setUserName(name);
                userIfo.setStatus(status);
            }
        }
        return userIfo;
    }
}