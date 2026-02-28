package com.grabit.service.auth;

import com.grabit.Utilities.JWTUtil;
import com.grabit.Utilities.ModelMapperUtility;
import com.grabit.Utilities.Utility;
import com.grabit.bean.auth.RefreshTokenRequest;
import com.grabit.bean.member.LoginDetailsDTO;
import com.grabit.bean.member.MemberDTO;
import com.grabit.entity.Permission;
import com.grabit.entity.Role;
import com.grabit.enums.AccessLevel;
import com.grabit.enums.RolesList;
import com.grabit.exception.CustomException;
import com.grabit.feign.MemberInterface;
import com.grabit.repository.RoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class LoginOrSignupService {

    private final MemberInterface memberInterface;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String refreshToken="refresh_token";

    public LoginOrSignupService(MemberInterface memberInterface, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.memberInterface = memberInterface;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Map<String, Object>> signUp(LoginDetailsDTO request) {
        ResponseEntity<MemberDTO> response = null;
        try {
            if (!Utility.isNullOrEmpty(request.getRole()) && !"USER".equalsIgnoreCase(request.getRole().toValue()))
                throw new CustomException(Utility.buildErrorObject("INVALID_USER", "Only role 'USER' is allowed for a member", 400, "signUp"));
            MemberDTO memberSignUpRequest = ModelMapperUtility.map(request, MemberDTO.class);
            response = memberInterface.createMemberProfile(memberSignUpRequest, null);
            Role role = roleRepository.findByRole(RolesList.USER).orElseThrow(() -> new EntityNotFoundException("No role exists with role : " + RolesList.USER));
            if (response.getBody() == null) {
                throw new CustomException(Utility.buildErrorObject("INVALID_RESPONSE", "No Response from member service", 500, "signUp"));
            }
            List<Long> permissionIds = role.getPermissions().stream().map(Permission::getId).toList();
            Map<String,Object> authDetails=new HashMap<>();
            authDetails.put("auth_token",JWTUtil.generateTokenForEndUser(response.getBody().getEmail(),response.getBody().getId(), role.getId(), permissionIds, AccessLevel.member.name()));
            authDetails.put("refresh_token",JWTUtil.generateTokenForEndUser(response.getBody().getEmail(),refreshToken));
            return ResponseEntity.ok(authDetails);
        } catch (Exception e) {
            if (response != null && response.hasBody() && response.getStatusCode().is2xxSuccessful()) {
                memberInterface.memberDelete(String.valueOf(response.getBody().getId()));
                log.info("Member Deleted Successfully : " + response.getBody().getId());
            }
            throw e;
        }
    }

    public ResponseEntity<Map<String, String>> signUpForAdmin(String email) {
        ResponseEntity<MemberDTO> response = null;
        try {
//            if (!Utility.isNullOrEmpty(request.getRole()) && !"USER".equalsIgnoreCase(request.getRole().toValue()))
//                throw new CustomException(Utility.buildErrorObject("INVALID_USER", "Only role 'USER' is allowed for a member", 400, "signUp"));
//            MemberDTO memberSignUpRequest = ModelMapperUtility.map(request, MemberDTO.class);
//            response = memberInterface.createMemberProfile(memberSignUpRequest, null);
            Role role = roleRepository.findByRole(RolesList.ADMIN).orElseThrow(() -> new EntityNotFoundException("No role exists with role : " + RolesList.ADMIN));
//            if (response.getBody() == null) {
//                throw new CustomException(Utility.buildErrorObject("INVALID_RESPONSE", "No Response from member service", 500, "signUp"));
//            }
            List<Long> permissionIds=role.getPermissions().stream().map(Permission::getId).toList();

            return ResponseEntity.ok(Map.of("Authorization", JWTUtil.generateTokenForAdmin(email, role.getId(),permissionIds)));
        } catch (Exception e) {
//            if(response!=null && response.hasBody() && response.getStatusCode().is2xxSuccessful()){
//                memberInterface.memberDelete(String.valueOf(response.getBody().getId()));
//                log.info("Member Deleted Successfully : "+response.getBody().getId());
//            }
            throw e;
        }
    }

    public ResponseEntity<Map<String, Object>> login(LoginDetailsDTO request) {
        if (Utility.isNullOrEmpty(request.getEmail()) || Utility.isNullOrEmpty(request.getPassword()))
            throw new CustomException(Utility.buildErrorObject("MANDATORY_DATA_MISSING", "Email and Password are required for the Login", 400, "login"));
        LoginDetailsDTO response = memberInterface.getDetails(request.getEmail());
        if(!response.getRole().equals(RolesList.USER))
            throw new CustomException(Utility.buildErrorObject("MEMBER_INCONSISTENCY","Member cannot have role other than USER but found : "+response.getRole(),500,"login"));
        if(!passwordEncoder.matches(request.getPassword(), response.getPassword()))
            throw new CustomException(Utility.buildErrorObject("INCORRECT_PASSWORD","Password that is provided is incorrect",401,"login"));
        Role role = roleRepository.findByRole(RolesList.USER).orElseThrow(() -> new EntityNotFoundException("No role exists with role : " + RolesList.USER));
        List<Long> permissionIds = role.getPermissions().stream().map(Permission::getId).toList();
        Map<String,Object> authDetails=new HashMap<>();
        authDetails.put("auth_token",JWTUtil.generateTokenForEndUser(response.getEmail(),response.getId(), role.getId(), permissionIds, AccessLevel.member.name()));
        authDetails.put("refresh_token",JWTUtil.generateTokenForEndUser(response.getEmail(),refreshToken));
        return ResponseEntity.ok(authDetails);
    }

    public ResponseEntity<Map<String, Object>> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        if (Utility.isNullOrEmpty(refreshTokenRequest.getRefreshToken()))
            throw new CustomException(Utility.buildErrorObject("MANDATORY_DATA_MISSING", "Email and Password are required for the Login", 400, "login"));
        String email=JWTUtil.getEmailFromRefreshToken(refreshTokenRequest.getRefreshToken());
        LoginDetailsDTO response = memberInterface.getDetails(email);
        Role role = roleRepository.findByRole(RolesList.USER).orElseThrow(() -> new EntityNotFoundException("No role exists with role : " + RolesList.USER));
        List<Long> permissionIds = role.getPermissions().stream().map(Permission::getId).toList();
        Map<String,Object> authDetails=new HashMap<>();
        authDetails.put("auth_token",JWTUtil.generateTokenForEndUser(response.getEmail(),response.getId(), role.getId(), permissionIds, AccessLevel.member.name()));
        authDetails.put("refresh_token",JWTUtil.generateTokenForEndUser(response.getEmail(),refreshToken));
        return ResponseEntity.ok(authDetails);
    }
}
