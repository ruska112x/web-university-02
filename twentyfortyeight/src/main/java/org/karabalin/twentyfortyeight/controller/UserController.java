package org.karabalin.twentyfortyeight.controller;

import org.karabalin.twentyfortyeight.repository.UserRepository;
import org.karabalin.twentyfortyeight.repository.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> list() {
        return userRepository.getAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody User newUser) {
        User createdUser = userRepository.create(newUser);
        URI location = UriComponentsBuilder.fromPath("/users/")
                .path(String.valueOf(createdUser.getId()))
                .build().toUri();
        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping("/users/{id}")
    public User read(@PathVariable int id) {
        return userRepository.read(id);
    }

    @PutMapping("/users")
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(userRepository.update(user));
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable int id) {
        userRepository.delete(id);
    }
}
