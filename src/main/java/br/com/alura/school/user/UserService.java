package br.com.alura.school.user;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User %s not found", username)));
    }

    public URI addNewUser(NewUserRequest newUserRequest){
        userRepository.save(newUserRequest.toEntity());
        return URI.create(format("/users/%s", newUserRequest.getUsername()));
    }
}
