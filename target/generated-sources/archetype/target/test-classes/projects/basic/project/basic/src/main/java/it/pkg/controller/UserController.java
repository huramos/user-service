package it.pkg.controller;

import it.pkg.DTO.UserDTO;
import it.pkg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
    return userService.findUserById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
    userService.deleteUserById(id);
    return ResponseEntity.ok("Usuario con ID " + id + " eliminado correctamente");
}

@PutMapping("/{id}")
public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
    UserDTO updatedUser = userService.updateUser(id, userDTO);
    return ResponseEntity.ok(updatedUser);
}

@PostMapping("/login")
public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
    try {
        String response = userService.login(userDTO.getUsername(), userDTO.getPassword());
        return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}


}