package com.grabit.service.security;

import com.grabit.Utilities.Utility;
import com.grabit.bean.member.LoginDetailsDTO;
import com.grabit.entity.Role;
import com.grabit.enums.RolesList;
import com.grabit.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetails implements UserDetailsService {

    private RestTemplate restTemplate;

    private RoleRepository roleRepository;

    public void setDetails(RestTemplate restTemplate, RoleRepository roleRepository) {
        this.restTemplate = restTemplate;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        // Add token
        LoginDetailsDTO credentials=restTemplate.getForObject("http://localhost:8090/member/credentials",LoginDetailsDTO.class);
        if(Utility.isNullOrEmpty(credentials))
            throw new UsernameNotFoundException("User Not Found");
        List<GrantedAuthority> authorities=new ArrayList<>();
        Role role=roleRepository.findByRole(credentials.getRole()).orElseThrow(()->new EntityNotFoundException("No Role Exists with name : "+credentials.getRole()));
        authorities.add(new SimpleGrantedAuthority(role.getRole().name()));
        authorities.addAll(role.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission().name())).toList());
        return new User(mobile, credentials.getPassword(), Boolean.parseBoolean(credentials.getIsActive().toValue()),true,true,true,authorities);
    }
}
