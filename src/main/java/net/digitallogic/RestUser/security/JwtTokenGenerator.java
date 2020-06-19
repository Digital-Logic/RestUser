package net.digitallogic.RestUser.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.digitallogic.RestUser.persistence.model.UserEntity;

import java.util.Date;

public class JwtTokenGenerator {

    private final String tokenSecret;
    private final String iss;

    public JwtTokenGenerator(String tokenSecret, String iss) {
        this.tokenSecret = tokenSecret;
        this.iss = iss;
    }


    public String allocateToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuer(iss)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }


    public String verifyToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
