package com.grabit.bean.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grabit.enums.MemberActiveStatus;
import com.grabit.enums.RolesList;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDetailsDTO {

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("is_active")
    @Enumerated(EnumType.STRING)
    private MemberActiveStatus isActive;

    @JsonProperty("role")
    @Enumerated(EnumType.STRING)
    private RolesList role;
}
