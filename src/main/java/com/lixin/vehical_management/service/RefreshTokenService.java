package com.lixin.vehical_management.service;



import com.lixin.vehical_management.dto.RefreshTokenRequest;

import java.util.Map;

public interface RefreshTokenService {
    void logout(Long userId);

    Map<String, String> refreshToken(Long userId, RefreshTokenRequest refreshTokenRequest);

    void clearRefreshToken();
}
