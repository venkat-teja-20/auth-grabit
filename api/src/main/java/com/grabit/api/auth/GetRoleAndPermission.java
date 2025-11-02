package com.grabit.api.auth;

import com.grabit.bean.auth.RoleDTO;
import com.grabit.service.auth.GetRoleAndPermissionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetRoleAndPermission {

    private final GetRoleAndPermissionService getRoleAndPermissionService;

    public GetRoleAndPermission(GetRoleAndPermissionService getRoleAndPermissionService) {
        this.getRoleAndPermissionService = getRoleAndPermissionService;
    }

    @GetMapping(value = "/role/permissions",produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO getPermissions(@RequestParam(value = "role") String role){
        return getRoleAndPermissionService.getPermissionsByRole(role);
    }
}
