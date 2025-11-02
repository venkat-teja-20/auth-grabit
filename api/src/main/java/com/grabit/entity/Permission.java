package com.grabit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grabit.enums.PermissionsList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "permission")
@Getter
@Setter
@Table(name = "permission")
public class Permission extends AuditDetails<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "permission", nullable = false,unique = true)
    @Enumerated(EnumType.STRING)
    @NotNull
    private PermissionsList permission;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    Set<Role> roles=new HashSet<>();
}
