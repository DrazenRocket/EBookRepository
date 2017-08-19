package com.drazendjanic.ebookrepository.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {

    private static String secret = "ThisIsTheBiggestSecret";
    private static String issuer = "EBookRepository";

    // TODO: Make method to throws exceptions (for now, the current implementation is ok)
    public static String generateJwt(Long subject) {
        String jwt = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            jwt = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(subject.toString())
                    .sign(algorithm);
        }
        catch (Exception e) {
            jwt = null;
        }

        return jwt;
    }

    // TODO: Make method to throws exceptions (for now, the current implementation is ok)
    public static Long unpackJwtSubjectAsLong(String jwt) {
        Long subject = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();
            DecodedJWT decoded = verifier.verify(jwt);

            subject = new Long(decoded.getSubject());
        }
        catch (Exception e) {
            subject = null;
        }

        return subject;
    }

}
