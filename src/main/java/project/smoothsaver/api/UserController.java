package project.smoothsaver.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.smoothsaver.dtos.UserRequest;
import project.smoothsaver.dtos.UserResponse;
import project.smoothsaver.entity.User;
import project.smoothsaver.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse addUser(@RequestBody UserRequest body) {
        return userService.addUser(body);
    }

    @GetMapping("/profile")
    public UserResponse getLoggedInCustomerProfile(Principal principal) {
        System.out.println("Principal: " + principal);
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not logged in");
        }
        String username = principal.getName();
        return userService.findById(username);
    }

    @DeleteMapping(path="/{username}")
    void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }
}

