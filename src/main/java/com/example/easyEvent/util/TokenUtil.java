package com.example.easyEvent.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class TokenUtil {
    //set how long this useid will expair
    static final long MILLI_SECONDS_IN_HOUR = 1 * 60 * 60 * 1000;
    static final String ISSUER = "Jie's project";
    static final String USER_ID = "userId";
    static Algorithm algorithm = Algorithm.HMAC256("mysecretkey");

    //
    public static String signToken(Integer userId,int expairationInHour){
        String token = JWT.create()
                .withIssuer(ISSUER)
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis()+ expairationInHour * MILLI_SECONDS_IN_HOUR))
                .sign(algorithm);

        return token;
    }
    public static Integer verifyToken(String token){
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        Integer userId = jwt.getClaim(USER_ID).asInt();
        return userId;
    }
}
