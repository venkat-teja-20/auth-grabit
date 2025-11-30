package com.grabit.config;

import com.grabit.handler.HandleAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final HandleAuthenticationEntryPoint authenticationEntryPoint;

    private static String[] PERMITTED_PATHS=new String[]{
            "/add/role",
            "/add/permission",
            "/role/*/permission/*/assign",
            "/member/signup",
            "/role/permissions",
            "/admin/signup",
            "/member/login",
            "/role/*/",
            "/member/token/refresh",
            "/webhook/cache/redis/refresh",
            "/cache-details/*"
    };

    public SecurityConfig(HandleAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests->requests
                        .requestMatchers(PERMITTED_PATHS).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
