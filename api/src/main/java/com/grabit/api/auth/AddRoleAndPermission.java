package com.grabit.api.auth;

import com.grabit.bean.auth.PermissionDTO;
import com.grabit.bean.auth.RoleDTO;
import com.grabit.service.auth.RoleAndPermissionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class AddRoleAndPermission {

    private RoleAndPermissionService roleAndPermissionService;

    public AddRoleAndPermission(RoleAndPermissionService roleAndPermissionService) {
        this.roleAndPermissionService = roleAndPermissionService;
    }

    @PostMapping(value = "/add/role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO addNewRole(@RequestBody RoleDTO roleDTO, HttpServletResponse response) {
        response.setStatus(201);
        return roleAndPermissionService.addRole(roleDTO);
    }

    @PostMapping(value = "/add/permission", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PermissionDTO addNewPermission(@RequestBody PermissionDTO permissionDTO, HttpServletResponse response) {
        response.setStatus(201);
        return roleAndPermissionService.addPermission(permissionDTO);
    }

    @PostMapping(value = "/role/{roleId}/permission/{permissionId}/assign", produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleDTO assignPermissionToRole(@PathVariable(value = "roleId") String roleId,@PathVariable(value = "permissionId") String permissionId, HttpServletResponse response) {
        return roleAndPermissionService.assignPermissionToARole(roleId,permissionId);
    }
}
