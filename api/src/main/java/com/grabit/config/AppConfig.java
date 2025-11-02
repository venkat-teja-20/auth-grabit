package com.grabit.config;

import com.grabit.mapper.MemberUrlMapper;
import com.grabit.service.security.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetails();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager memberAuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoAuthenticationProvider);
    }

//    @Bean
//    public AuthenticationManager deliveryPartnerAuthenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
//        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider(userDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//
//        return new ProviderManager(daoAuthenticationProvider);
//    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public MemberUrlMapper memberUrlMapper(){
        return new MemberUrlMapper(System.getenv("member_router_url"));
    }
}
