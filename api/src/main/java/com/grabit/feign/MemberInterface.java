package com.grabit.feign;

import com.grabit.bean.member.LoginDetailsDTO;
import com.grabit.bean.member.MemberDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "memberservice")
public interface MemberInterface {
    @GetMapping(value = "/member/credentials",produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginDetailsDTO getDetails(@RequestParam(value = "mobile") String mobile);

    @PostMapping(value = "/member/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberDTO> createMemberProfile(@RequestBody MemberDTO request, @RequestHeader Map<String, String> headers);

    @DeleteMapping(value = "/member/delete/{memberId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,String>> memberDelete(@PathVariable(value = "memberId") String memberId);
}
