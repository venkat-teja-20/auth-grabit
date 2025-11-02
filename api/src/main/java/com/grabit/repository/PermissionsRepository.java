package com.grabit.repository;

import com.grabit.entity.Permission;
import com.grabit.enums.PermissionsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionsRepository extends JpaRepository<Permission,Long> {
    Optional<Permission> findByPermission(PermissionsList permission);
}
