/***
 * @pName management
 * @name TokenUtil
 * @user HongWei
 * @date 2018/8/14
 * @desc
 */
package com.panda.game.management.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具
 */
public class TokenUtil {
    private static final String SECRET = "session_secret";
    private static final String ISSUER = "mooc_user";

    /**
     * 创建token
     * @param map
     * @return
     */
    public static String create(Map<String, String> map){
        // 加密算法
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        // 创建发布者
        JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER).withExpiresAt(DateUtil.getDateAdd(new Date(), 1));
        map.forEach((k,v) -> builder.withClaim(k, v));
        return builder.sign(algorithm).toString();
    }

    /**
     * 验证token
     * @param token
     * @return
     */
    public static Map<String, String> validate(String token){
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT verify = jwtVerifier.verify(token);
        Map<String, Claim> claims = verify.getClaims();
        Map<String, String> map = new HashMap<>();
        claims.forEach((k,v) -> map.put(k, v.asString()));
        return map;
    }
}
