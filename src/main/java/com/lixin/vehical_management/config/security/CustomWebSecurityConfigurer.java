package com.lixin.vehical_management.config.security;

import com.lixin.vehical_management.config.security.handler.CustomAuthenticationFailureHandler;
import com.lixin.vehical_management.config.security.jwt.CustomAuthenticationFilter;
import com.lixin.vehical_management.config.security.jwt.ExceptionHandlerFilter;
import com.lixin.vehical_management.config.security.jwt.TokenAuthenticationFilter;
import com.lixin.vehical_management.config.security.jwt.TokenProvider;
import com.lixin.vehical_management.config.security.user.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
@EnableWebSecurity()
@EnableMethodSecurity()
public class CustomWebSecurityConfigurer {
    private TokenProvider tokenProvider;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    public static String[] mergeArrays(String[]... arrays) {
        int totalLength = 0;
        for (String[] array : arrays) {
            totalLength += array.length;
        }
        String[] result = new String[totalLength];
        int destPos = 0;
        for (String[] array : arrays) {
            System.arraycopy(array, 0, result, destPos, array.length);
            destPos += array.length;
        }
        return result;
    }

    @Autowired
    public void setTokenProvider(@Lazy TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter(resolver);
    }

    @Bean
    @Qualifier("BCrypt")
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(4));
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
            }
        };
    }

    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }


    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(tokenProvider);
        customAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        customAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
        return customAuthenticationFilter;
    }
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // Set permission on endpoints
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers(permitAllPaths()).permitAll()
                        .anyRequest().permitAll()
                );;
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(exceptionHandlerFilter(), LogoutFilter.class);
        return http.build();
    }

    public String[] permitAllPaths() {
        String[] homePaths = new String[]{
                "/", // Home path
                "/error",
                "/favicon.ico"};
        String[] resourcePaths = new String[]{
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"};
        String[] authenPaths = new String[]{
                "/auth/**",
                "/oauth2/**"};
        String[] additionalPaths = new String[]{
                "/api/v1/register",
                "/api/v1/refreshToken",
                "/api/v1/logout",
                "/api/v1/login",
                "/api/v1/**"
        };
        String[] swaggerPaths = new String[]{
                "/swagger-ui.html",
                "/swagger-ui/index.html",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**"
        };
        return mergeArrays(homePaths, resourcePaths, authenPaths, additionalPaths, swaggerPaths);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
