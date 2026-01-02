package com.lixin.vehical_management.config.security.jwt;

import lombok.Data;

import java.util.Collection;

@Data
public class JwtResponse {
    private Long userId;
    private String displayName;
    private String providerId;
    private Long expiration;
    private String token;
    private String refreshToken;
    private Collection role;
    private String photoURL;

    // todo
    public JwtResponse(Long userId,
                       Long expiration,
                       String token,
                       String refreshToken,
                       String displayName,
                       Collection role) {
        this.userId = userId;
        this.displayName = displayName;
        this.expiration = expiration;
        this.token = token;
        this.refreshToken = refreshToken;
        this.role = role;
    }
}
