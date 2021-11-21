package com.exist.dao.data;

import com.exist.model.user.ERole;
import com.exist.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
