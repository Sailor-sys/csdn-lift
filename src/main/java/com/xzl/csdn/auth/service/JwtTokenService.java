package com.xzl.csdn.auth.service;

import com.google.common.base.Strings;
import com.xzl.csdn.auth.model.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * @author: liupu
 * @description: jwt token
 * @date: 2021/8/9
 */
@Slf4j
@Service
public class JwtTokenService {

    public static final String HASH_KEY_PREFIX = "hash_";
    /**
     * 60天过期
     */
    private final int expiration = 60 * 60 * 24 * 60;
    /**
     * 7天过期暂时改成30分钟
     * 7*60*60*24;
     */
    private final int redisExpiration = 60 * 60 * 24;
    private final int version = 1;
    private static final String CLAIM_KEY_USERCODE = "sub";
    private static final String CLAIM_KEY_GUID = "guid";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_UUID = "uuid";
    private static final String CLAIM_KEY_NICKNAME = "nickName";
    private static final String CLAIM_KEY_MOBILE_PHONE = "mobilePhone";
    private static final String CLAIM_KEY_LOGIN_TYPE = "loginType";
    private static final String CLAIM_KEY_VERSION = "loginVersion";
    private static final String CLAIM_KEY_APP_TYPE = "appType";
    private static final String CLAIM_KEY_UNIT_ID = "unitId";

    //Jwt令牌信息
    public static final String JWT_KEY = "csdn";

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 是否允许重复登陆
     */
    private boolean repeatLogin = false;

    public void cacheNewToken(String token, String value, JwtUser jwtUser) {
        cacheNewToken(token, value, jwtUser, repeatLogin);
    }

    private String getUuidTokenKey(String suffix) {
        return "uuid:" + suffix;
    }

    public void cacheNewToken(String token, String value, JwtUser jwtUser, boolean repeatLogin) {
        jedisCluster.setex(token, redisExpiration, value);
        if (jwtUser != null && !Strings.isNullOrEmpty(jwtUser.getUuid())) {
            jedisCluster.setex(getUuidTokenKey(token), redisExpiration, jwtUser.getUuid());
            jedisCluster.setex(getUuidTokenKey(jwtUser.getUuid()), redisExpiration, token);
            jedisCluster.hset(HASH_KEY_PREFIX + value, token, jwtUser.getUuid());
        }
        if (!repeatLogin) {
            String oldToken = jedisCluster.get(value);
            if (!Strings.isNullOrEmpty(oldToken)) {
                //旧的token需要剔除
                deleteToken(oldToken);
            }
            jedisCluster.setex(value, redisExpiration, token);
        }
    }

    public void cacheNewToken(String token, String value, boolean repeatLogin) {
        cacheNewToken(token, value, null, repeatLogin);
    }

    public void forceLogout(String userKey) {
        Map<String, String> value = jedisCluster.hgetAll(HASH_KEY_PREFIX + userKey);
        for (Map.Entry<String, String> entry : value.entrySet()) {
            String token = entry.getKey();
            deleteToken(token);
        }
    }

    public void deleteToken(String oldToken) {
        if (Boolean.TRUE.equals(jedisCluster.exists(getUuidTokenKey(oldToken)))) {
            String userKey = jedisCluster.get(oldToken);
            jedisCluster.hdel(HASH_KEY_PREFIX + userKey, oldToken);
            String uuid = jedisCluster.get(getUuidTokenKey(oldToken));
            jedisCluster.del(getUuidTokenKey(oldToken));
            jedisCluster.del(getUuidTokenKey(uuid));
        }
        jedisCluster.del(oldToken);
    }

    public boolean isTokenExpired(String token) {
        String redisValue = jedisCluster.get(token);
        if (Strings.isNullOrEmpty(redisValue)) {
            return true;
        }
        jedisCluster.expire(token, redisExpiration);
        if (Boolean.TRUE.equals(jedisCluster.exists(getUuidTokenKey(token)))) {
            jedisCluster.expire(getUuidTokenKey(token), redisExpiration);
            String uuid = jedisCluster.get(getUuidTokenKey(token));
            jedisCluster.expire(getUuidTokenKey(uuid), redisExpiration);
        }
        if (!repeatLogin) {
            jedisCluster.expire(redisValue, redisExpiration);
        }
        return false;
    }

    public String generateToken(JwtUser jwtUser) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USERCODE, jwtUser.getUsername());
        claims.put(CLAIM_KEY_GUID, jwtUser.getId());
        claims.put(CLAIM_KEY_MOBILE_PHONE, jwtUser.getMobilePhone());
        claims.put(CLAIM_KEY_NICKNAME, jwtUser.getNickName());
        claims.put(CLAIM_KEY_LOGIN_TYPE, jwtUser.getLoginType());
        claims.put(CLAIM_KEY_VERSION, version);
        claims.put(CLAIM_KEY_APP_TYPE, jwtUser.getAppType());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_UUID, jwtUser.getUuid());
        claims.put(CLAIM_KEY_UNIT_ID, jwtUser.getUnitId());
        return generateToken(claims);
    }


    public JwtUser parseJwtUser(String token) {
        JwtUser jwtUser = new JwtUser();

        final Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return null;
        }

        jwtUser.setUsername(claims.getSubject());
        jwtUser.setId((Integer) claims.get(CLAIM_KEY_GUID));
        jwtUser.setMobilePhone((String) claims.get(CLAIM_KEY_MOBILE_PHONE));
        jwtUser.setNickName((String) claims.get(CLAIM_KEY_NICKNAME));
        jwtUser.setLoginType((Integer) claims.get(CLAIM_KEY_LOGIN_TYPE));
        jwtUser.setVersion((Integer) claims.get(CLAIM_KEY_VERSION));
        jwtUser.setAppType((Integer) claims.get(CLAIM_KEY_APP_TYPE));
        jwtUser.setUuid((String) claims.get(CLAIM_KEY_UUID));
        jwtUser.setUnitId((Integer) claims.get(CLAIM_KEY_UNIT_ID));
        jwtUser.setToken(token);
        return jwtUser;
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = (Claims) Jwts.parser().setSigningKey(generalKey()).parse(token).getBody();
        } catch (Exception e) {
            claims = null;
            log.error("get value from token error", e);
        }
        return claims;
    }


    /**
     * 刷新token
     */
    String refresh(String oldToken) {
        try {
            Claims claim = getClaimsFromToken(oldToken);
            claim.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claim);
        } catch (Exception e) {
            log.error("refresh token error", e);
            return null;
        }
    }


    /**
     * RSA非对称加密算法，高安全性 保护token信息安全
     */
    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS256, generalKey()).compact();
    }

    private Date generateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, expiration);
        return calendar.getTime();
    }


    /**
     * 生成加密 secretKey
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getEncoder().encode(JwtTokenService.JWT_KEY.getBytes());
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

}
