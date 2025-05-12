package com.foro.controller;

import com.foro.DTO.LoginRequest;
import com.foro.DTO.RegisterRequest;
import com.foro.DTO.UserDTO;
import com.foro.service.AuthService;
import com.foro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService; // Se agrega UserService para obtener datos del usuario

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    // Endpoint para el login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("🔹 Solicitud de login recibida");

        try {
            Map<String, Object> response = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Error inesperado en el servidor"));
        }
    }

    // Nuevo endpoint para obtener el perfil del usuario
    @GetMapping("/user-profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestParam String username) {
        System.out.println("🔹 Solicitud de perfil para usuario: " + username);
        Optional<UserDTO> userProfile = userService.getUserProfile(username);
        return userProfile.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Nuevo endpoint para actualizar el perfil del usuario
    @PutMapping("/user-profile")
    public ResponseEntity<UserDTO> updateUserProfile(@RequestParam String username, @RequestBody UserDTO userDTO) {
        System.out.println("🔹 Actualización de perfil para usuario: " + username);

        Optional<UserDTO> updatedUser = userService.updateUserProfile(username, userDTO);
        return updatedUser.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint para recuperación de contraseña
    @PostMapping("/recover-password")
    public ResponseEntity<Map<String, String>> recoverPassword(@RequestBody Map<String, String> request) {
        System.out.println("🔹 Solicitud de recuperación de contraseña recibida");

        return authService.recoverPassword(request.get("email"))
                .map(result -> ResponseEntity.ok(result))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado")));
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        System.out.println("🔹 Solicitud de registro recibida");

        try {
            Map<String, String> response = authService.register(registerRequest);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "Error inesperado en el servidor"));
        }
    }
}