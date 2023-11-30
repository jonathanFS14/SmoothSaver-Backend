package project.smoothsaver.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import project.security.entity.Role;
import project.smoothsaver.dtos.UserRequest;
import project.smoothsaver.dtos.UserResponse;
import project.smoothsaver.entity.User;
import project.smoothsaver.repository.UserRepo;

import java.security.Principal;

@Service
public class UserService {

    UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public UserResponse addUser(UserRequest body) {
        if (userRepo.existsById(body.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username already exists");
        }

        User newUser = UserRequest.getUserEntity(body);

        newUser.addRole(Role.USER);

        newUser = userRepo.save(newUser);
        return new UserResponse(newUser, true);
    }

    public UserResponse findById(String username) {
        User user = findByUsername(username);
        return new UserResponse(user, true);
    }

    private User findByUsername(String username) {
        return userRepo.findById(username).
                orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User with this username does not exist"));
    }

}
