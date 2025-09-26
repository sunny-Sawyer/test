package com.example.dg.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author xinxin
 * @version 1.0
 */
public class TokenUtil {
    //    @Resourceq
    //    static StringRedisTemplate stringRedisTemplate;
    //    设置token的有效时间为一天
    private static final long TIME = 1000 * 3600 * 24;
    private static final String SECRET_KEY = "login";

    public static String createJWT(Map<String, Object> claims) {
        return createJWT( TIME, claims);
    }

    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     *
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT( long ttlMillis, Map<String, Object> claims) {

        // 指定签名的时候使用的签名算法，也就是header那部分
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;

        // 设置jwt的body
        String token = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(new Date(expMillis))
                .compact();

        return token;
    }


    public static Claims parseJWT(String token) {
        return parseJWT(SECRET_KEY, token);
    }

    public static Claims parseJWT(String secretKey, String token) {
        // 得到DefaultJwtParser
        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(SECRET_KEY)
                // 设置需要解析的jwt
                .parseClaimsJws(token)
                .getBody();
    }


}
