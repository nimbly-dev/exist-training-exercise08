package com.exist.exercise08.services.data;

import java.util.Optional;

import com.exist.exercise08.model.user.ERole;
import com.exist.exercise08.model.user.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
