package com.lixin.vehical_management.service.impl;

import com.lixin.vehical_management.config.security.jwt.JwtTokenUtils;
import com.lixin.vehical_management.dto.RefreshTokenRequest;
import com.lixin.vehical_management.entities.Employee;
import com.lixin.vehical_management.entities.RefreshToken;
import com.lixin.vehical_management.exception.ApplicationRuntimeException;
import com.lixin.vehical_management.repositories.EmployeeRepository;
import com.lixin.vehical_management.repositories.RefreshTokenRepository;
import com.lixin.vehical_management.service.RefreshTokenService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${application.jwtExpiration}")
    private int jwtExpirationMs;
    @Value("${application.jwtExpirationRefreshToken}")
    private long jwtExpirationRefreshToken;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final EmployeeRepository userRepository;

    private final String MESSAGE_END_LOGIN_SESSION = "Login session expired, please contact admin";


    @Override
    public void logout(Long userId) {
        Optional<Employee> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByEmployee(user.get());
            refreshTokenRepository.deleteAll(refreshTokens);
        } else {
            throw new ApplicationRuntimeException(MESSAGE_END_LOGIN_SESSION, HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public Map<String, String> refreshToken(Long userId, RefreshTokenRequest refreshTokenRequest) {
        try {
            jwtTokenUtils.validateRefreshJwtToken(refreshTokenRequest.getRefreshToken());
            RefreshToken refreshToken = refreshTokenRepository.findByRefreshTokenCode(refreshTokenRequest.getRefreshToken());
            if (ObjectUtils.isEmpty(refreshToken)) {
                throw new ApplicationRuntimeException(MESSAGE_END_LOGIN_SESSION, HttpStatus.UNAUTHORIZED);
            }
            Optional<Employee> user = userRepository.findById(userId);
            if (user.isPresent()) {
                Map<String, Object> claims = new HashMap<>();
                List<GrantedAuthority> authorities = Collections.
                        singletonList(new SimpleGrantedAuthority(user.get().getRole().name()));
                String token = Jwts.builder()
                        .setClaims(claims)
                        .setSubject(user.get().getUserName())
                        .setIssuedAt(new Date())
                        .addClaims(Map.of("authorities", authorities))
                        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(jwtTokenUtils.getKey()).compact();
                String refreshTokenNew = jwtTokenUtils.generateJwtTokenRefresh();
                refreshToken.setRefreshTokenCode(refreshTokenNew);
                refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(jwtExpirationRefreshToken));
                refreshTokenRepository.save(refreshToken);
                return Map.of("token", token, "refreshToken", refreshTokenNew);
            }
            else  {
                throw new ApplicationRuntimeException(MESSAGE_END_LOGIN_SESSION, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            throw new ApplicationRuntimeException(MESSAGE_END_LOGIN_SESSION, HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public void clearRefreshToken() {
        refreshTokenRepository.deleteAll();
    }
}
