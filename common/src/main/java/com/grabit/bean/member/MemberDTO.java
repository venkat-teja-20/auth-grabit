package com.grabit.bean.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grabit.enums.MemberActiveStatus;
import com.grabit.enums.RolesList;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("otp")
    private String otp;

    @JsonProperty("password")
    private String password;

    @JsonProperty("is_active")
    @Enumerated(EnumType.STRING)
    private MemberActiveStatus isActive;

    @JsonProperty("role")
    @Enumerated(EnumType.STRING)
    private RolesList role;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("created_ts")
    private String createdTs;

    @JsonProperty("updated_by")
    private String updatedBy;

    @JsonProperty("updated_ts")
    private String updatedTs;
}
