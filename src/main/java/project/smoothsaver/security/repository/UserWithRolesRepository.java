package project.smoothsaver.security.repository;

import project.smoothsaver.security.entity.UserWithRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface
UserWithRolesRepository extends JpaRepository<UserWithRoles,String> {
    //Boolean existsByEmail(String email);
}
