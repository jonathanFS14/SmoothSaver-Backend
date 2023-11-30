package project.smoothsaver.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project.smoothsaver.dtos.UserRequest;
import project.smoothsaver.dtos.UserResponse;
import project.smoothsaver.service.UserService;

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
}
