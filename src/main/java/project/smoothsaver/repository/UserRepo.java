package project.smoothsaver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.smoothsaver.entity.User;

public interface UserRepo extends JpaRepository<User,String> {
    User findByUsername(String username);
}
