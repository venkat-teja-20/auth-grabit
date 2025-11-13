package com.grabit.service.auth;

import com.grabit.Utilities.ModelMapperUtility;
import com.grabit.Utilities.Utility;
import com.grabit.bean.auth.PermissionDTO;
import com.grabit.bean.auth.RoleDTO;
import com.grabit.entity.Permission;
import com.grabit.entity.Role;
import com.grabit.enums.PermissionsList;
import com.grabit.enums.RolesList;
import com.grabit.exception.CustomException;
import com.grabit.helper.auth.RoleAndPermissionHelper;
import com.grabit.repository.PermissionsRepository;
import com.grabit.repository.RoleRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Log4j2
public class RoleAndPermissionService {

    private final RoleRepository roleRepository;

    private final PermissionsRepository permissionsRepository;

    public RoleAndPermissionService(RoleRepository roleRepository, PermissionsRepository permissionsRepository) {
        this.roleRepository = roleRepository;
        this.permissionsRepository = permissionsRepository;
    }

    @Transactional
    public RoleDTO addRole(RoleDTO request) {
        RolesList givenRole = request.getRole();
        if (roleRepository.findByRole(givenRole).isPresent()) {
            throw new EntityExistsException("A role already exists with name : " + givenRole);
        }
        Role role = ModelMapperUtility.map(request, Role.class);
        if (role.getPermissions() == null)
            role.setPermissions(new HashSet<>());
        if (!role.getPermissions().isEmpty()) {
            role.setPermissions(role.getPermissions().stream().peek(permission -> permission.getRoles().add(role)).collect(Collectors.toSet()));
        }
        Role savedRole = roleRepository.save(role);
        log.info("Saved Role : "+Utility.toJson(savedRole));
        return ModelMapperUtility.map(savedRole, RoleDTO.class);
    }

    @Transactional
    public PermissionDTO addPermission(PermissionDTO request) {
        if (permissionsRepository.findByPermission(request.getPermission()).isPresent()) {
            throw new EntityExistsException("A permission already exists with name : " + request.getPermission());
        }
        Permission permission = ModelMapperUtility.map(request, Permission.class);
        Permission savedPermission = permissionsRepository.save(permission);
        log.info("Saved Permission : "+Utility.toJson(savedPermission));
        return ModelMapperUtility.map(savedPermission, PermissionDTO.class);
    }

    @Transactional
    public RoleDTO assignPermissionToARole(String roleId, String permissionId) {
        RoleAndPermissionHelper helper = new RoleAndPermissionHelper();
        helper.isValid(roleId, "role", "assignPermissionToARole");
        helper.isValid(permissionId, "permission", "assignPermissionToARole");
        Role role = roleRepository.findById(Long.valueOf(roleId)).orElseThrow(() -> new EntityNotFoundException("No role exists with id : " + roleId));
        Permission permission = permissionsRepository.findById(Long.valueOf(permissionId)).orElseThrow(() -> new EntityNotFoundException("No permission exists with id : " + permissionId));

        if (role.getPermissions().contains(permission)) {
            throw new CustomException(Utility.buildErrorObject(
                    "PERMISSION_ALREADY_ASSIGNED",
                    "Permission '" + permission.getPermission() + "' is already assigned to role '" + role.getRole() + "'",
                    409,
                    "assignPermissionToARole"
            ));
        }
        role.getPermissions().add(permission);
        Role savedRole=roleRepository.save(role);
        log.info("Saved Permission to the Role : "+Utility.toJson(savedRole));
        return ModelMapperUtility.map(savedRole,RoleDTO.class);
    }

    public RoleDTO getRoleById(String id){
        if(!Utility.isNumeric(id))
            throw new CustomException(Utility.buildErrorObject("INVALID_ROLE_ID","Role provided is not valid",400,"getRoleById"));
        Role role=roleRepository.findById(Long.valueOf(id)).orElseThrow(()->new EntityNotFoundException("No role exists with role id : "+id));
        return ModelMapperUtility.map(role,RoleDTO.class);

    }
}
