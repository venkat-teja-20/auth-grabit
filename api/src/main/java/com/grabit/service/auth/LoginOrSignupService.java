package com.grabit.service.auth;

import com.grabit.Utilities.JWTUtil;
import com.grabit.Utilities.ModelMapperUtility;
import com.grabit.Utilities.Utility;
import com.grabit.bean.member.LoginDetailsDTO;
import com.grabit.bean.member.MemberDTO;
import com.grabit.enums.RolesList;
import com.grabit.exception.CustomException;
import com.grabit.feign.MemberInterface;
import com.grabit.mapper.MemberUrlMapper;
import com.grabit.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Log4j2
public class LoginOrSignupService {

    private final MemberInterface memberInterface;

    private final RoleRepository roleRepository;

    public LoginOrSignupService(MemberInterface memberInterface, RoleRepository roleRepository) {
        this.memberInterface = memberInterface;
        this.roleRepository = roleRepository;
    }

    public ResponseEntity<Map<String, String>> signUp(LoginDetailsDTO request) {
        ResponseEntity<MemberDTO> response=null;
        try {
            if (!Utility.isNullOrEmpty(request.getRole()) && !"USER".equalsIgnoreCase(request.getRole().toValue()))
                throw new CustomException(Utility.buildErrorObject("INVALID_USER", "Only role 'USER' is allowed for a member", 400, "signUp"));
            MemberDTO memberSignUpRequest = ModelMapperUtility.map(request, MemberDTO.class);
            response = memberInterface.createMemberProfile(memberSignUpRequest, null);
            Long id = roleRepository.findIdByRole(RolesList.USER).orElseThrow(() -> new EntityNotFoundException("No role exists with role : " + RolesList.USER));
            if (response.getBody() == null) {
                throw new CustomException(Utility.buildErrorObject("INVALID_RESPONSE", "No Response from member service", 500, "signUp"));
            }
            return ResponseEntity.ok(Map.of("Authorization", JWTUtil.generateTokenForEndUser(response.getBody(), id)));
        } catch (Exception e) {
            if(response!=null && response.hasBody() && response.getStatusCode().is2xxSuccessful()){
                memberInterface.memberDelete(String.valueOf(response.getBody().getId()));
                log.info("Member Deleted Successfully : "+response.getBody().getId());
            }
            throw e;
        }
    }

    public ResponseEntity<Map<String, String>> signUpForAdmin(String email) {
        ResponseEntity<MemberDTO> response=null;
        try {
//            if (!Utility.isNullOrEmpty(request.getRole()) && !"USER".equalsIgnoreCase(request.getRole().toValue()))
//                throw new CustomException(Utility.buildErrorObject("INVALID_USER", "Only role 'USER' is allowed for a member", 400, "signUp"));
//            MemberDTO memberSignUpRequest = ModelMapperUtility.map(request, MemberDTO.class);
//            response = memberInterface.createMemberProfile(memberSignUpRequest, null);
            Long id = roleRepository.findIdByRole(RolesList.ADMIN).orElseThrow(() -> new EntityNotFoundException("No role exists with role : " + RolesList.ADMIN));
//            if (response.getBody() == null) {
//                throw new CustomException(Utility.buildErrorObject("INVALID_RESPONSE", "No Response from member service", 500, "signUp"));
//            }
            return ResponseEntity.ok(Map.of("Authorization", JWTUtil.generateTokenForAdmin(email, id)));
        } catch (Exception e) {
//            if(response!=null && response.hasBody() && response.getStatusCode().is2xxSuccessful()){
//                memberInterface.memberDelete(String.valueOf(response.getBody().getId()));
//                log.info("Member Deleted Successfully : "+response.getBody().getId());
//            }
            throw e;
        }
    }
}
