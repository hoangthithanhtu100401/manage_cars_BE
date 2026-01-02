package com.lixin.vehical_management.config.security.jwt;


import com.lixin.vehical_management.config.security.user.UserPrincipal;
import com.lixin.vehical_management.exception.ApplicationRuntimeException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);
    @Value("${application.jwtExpiration}")
    private int jwtExpirationMs;
    @Value("${application.jwtExpirationRefreshToken}")
    private long jwtExpirationRefreshToken;

    private final JwtParser jwtParser;
    private final JwtParser refreshJwtParser;
    @Getter
    private final Key key;
    @Getter
    private final Key refreshKey;
    private final JwtBuilder jwtBuilder;

    public JwtTokenUtils(@Value("${application.jwtSecret}") String jwtSecret, @Value("${application.jwtRefreshTokenSecret}") String jwtRefreshTokenSecret) {
        key = new SecretKeySpec(jwtSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        refreshKey = new SecretKeySpec(jwtRefreshTokenSecret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        refreshJwtParser = Jwts.parserBuilder().setSigningKey(refreshKey).build();
        jwtBuilder = Jwts.builder();
    }

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        assert userPrincipal != null;
        return jwtBuilder
                .setClaims(claims)
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .addClaims(
                        Map.of(
                                "authorities",
                                authentication.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList()
                        )
                )

                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(key).compact();
    }

    //for retrieving any information from token, we will need the secret key
    public Claims getAllClaimsFromToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            jwtParser.parseClaimsJws(jwtToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            logger.trace("Invalid JWT signature trace: ", e);
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            logger.trace("Invalid JWT token: ", e);
            throw e;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            logger.trace("JWT token is expired: ", e);
            throw new ApplicationRuntimeException("Token is expired", HttpStatus.GONE);
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            logger.trace("JWT token is unsupported: ", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            logger.trace("JWT claims string is empty: ", e);
            throw e;
        }
        return false;
    }

    public boolean validateRefreshJwtToken(String refreshToken) {
        try {
            refreshJwtParser.parseClaimsJws(refreshToken);
            return true;
        } catch (Exception e) {
            logger.error("Refresh JWT validation error: {}", e.getMessage());
            throw e;
        }
    }

    public String generateJwtTokenRefresh() {
        return jwtBuilder
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationRefreshToken))
                .signWith(refreshKey).compact();
    }

}
