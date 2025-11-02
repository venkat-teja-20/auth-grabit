package com.grabit.service.auth;

import com.grabit.Utilities.ModelMapperUtility;
import com.grabit.bean.auth.RoleDTO;
import com.grabit.entity.Role;
import com.grabit.enums.RolesList;
import com.grabit.repository.PermissionsRepository;
import com.grabit.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetRoleAndPermissionService {

    private final RoleRepository roleRepository;

    private final PermissionsRepository permissionsRepository;

    public GetRoleAndPermissionService(RoleRepository roleRepository, PermissionsRepository permissionsRepository) {
        this.roleRepository = roleRepository;
        this.permissionsRepository = permissionsRepository;
    }

    public RoleDTO getPermissionsByRole(String role){
        Role roleDetails=roleRepository.findByRole(RolesList.fromValue(role)).orElseThrow(()->new EntityNotFoundException("No such role exists with name : "+role));
        return ModelMapperUtility.map(roleDetails,RoleDTO.class);
    }
}
