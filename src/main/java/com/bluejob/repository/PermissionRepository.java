package com.bluejob.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bluejob.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    Permission findPermissionByName(String name);
}
