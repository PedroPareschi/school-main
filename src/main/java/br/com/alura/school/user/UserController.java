package br.com.alura.school.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

@RestController
class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{username}")
    ResponseEntity<UserResponse> userByUsername(@PathVariable("username") String username) {
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (UserException e) {
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/users")
    ResponseEntity<Void> newUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        URI location = userService.addNewUser(newUserRequest);
        return ResponseEntity.created(location).build();
    }

}
