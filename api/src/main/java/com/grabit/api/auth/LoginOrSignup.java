package com.grabit.api.auth;

import com.grabit.bean.auth.RefreshTokenRequest;
import com.grabit.bean.member.LoginDetailsDTO;
import com.grabit.service.auth.LoginOrSignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginOrSignup {

    @Autowired
    LoginOrSignupService loginOrSignupService;

    @PostMapping(value = "/member/signup",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> doSignUp(@RequestBody LoginDetailsDTO request){
        return loginOrSignupService.signUp(request);
    }

    @PostMapping(value = "/admin/signup",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> doSignUp(@RequestParam(value = "email") String email){
        return loginOrSignupService.signUpForAdmin(email);
    }

    @PostMapping(value = "/member/login",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> doLogin(@RequestBody LoginDetailsDTO request){
        return loginOrSignupService.login(request);
    }

    @PostMapping(value = "/member/token/refresh",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return loginOrSignupService.refreshToken(refreshTokenRequest);
    }
}
