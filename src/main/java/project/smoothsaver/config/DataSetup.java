package project.smoothsaver.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import project.smoothsaver.entity.User;
import project.smoothsaver.repository.UserRepo;
import project.security.entity.Role;

@Configuration
public class DataSetup implements ApplicationRunner {

    final UserRepo userRepo;

    public DataSetup(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = new User("Admin", "Admin", "Admin", "Admin", "admin", "admin");
        user.addRole(Role.ADMIN);
        userRepo.save(user);
    }
}
