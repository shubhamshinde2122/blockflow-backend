package com.blockflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.blockflow.model.User;
import com.blockflow.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/users/{id}/make-admin")
    public ResponseEntity<?> makeUserAdmin(@PathVariable Long id) {
        User user = userService.getUserById(id);
        user.setRole(User.Role.ADMIN);
        userService.saveUser(user);
        return ResponseEntity.ok("User promoted to admin");
    }

    @PostMapping("/users/{id}/remove-admin")
    public ResponseEntity<?> removeUserAdmin(@PathVariable Long id) {
        User user = userService.getUserById(id);
        user.setRole(User.Role.USER);
        userService.saveUser(user);
        return ResponseEntity.ok("Admin role removed");
    }

    @PostMapping("/users/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        user.setEnabled(false);
        userService.saveUser(user);
        return ResponseEntity.ok("User account disabled");
    }

    @PostMapping("/users/{id}/enable")
    public ResponseEntity<?> enableUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        user.setEnabled(true);
        userService.saveUser(user);
        return ResponseEntity.ok("User account enabled");
    }
} // End of class
