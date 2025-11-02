package com.grabit.repository;

import com.grabit.entity.Role;
import com.grabit.enums.RolesList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRole(RolesList role);

    @Query("SELECT r.id FROM role r WHERE r.role=:role")
    Optional<Long> findIdByRole(@Param("role") RolesList role);
}
