package com.foro.service;

import com.foro.DTO.RegisterRequest;
import com.foro.enums.UserRole;
import com.foro.model.User;
import com.foro.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 Registro sin encriptación de contraseña
    @Transactional
    public Map<String, String> register(RegisterRequest registerRequest) {
        System.out.println("🔹 Registro recibido - Verificando existencia de usuario");

        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }

        if (registerRequest.getGender() == null || registerRequest.getGender().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El género no puede estar vacío");
        }

        try {
            // Crear usuario sin encriptación de contraseña
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(registerRequest.getPassword()); // ✅ Se guarda sin encriptar
            newUser.setRole(UserRole.USER);
            newUser.setGender(registerRequest.getGender());

            userRepository.save(newUser);
            System.out.println("✅ Usuario registrado correctamente: " + registerRequest.getUsername());
            return Map.of("message", "Usuario registrado exitosamente");
        } catch (Exception e) {
            System.err.println("❌ Error al registrar usuario: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el usuario en la base de datos.");
        }
    }

    // 🔹 Autenticación sin desencriptación de contraseña
    public Map<String, Object> login(String username, String password) {
        System.out.println("🔹 Login request recibido - Username: " + username);

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) { // ✅ Comparación sin `BCrypt.checkpw()`
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        System.out.println("✅ Autenticación exitosa para usuario: " + username);

        return Map.of(
            "message", "Inicio de sesión exitoso",
            "role", user.getRole().name(),
            "username", user.getUsername(),
            "email", user.getEmail(),
            "gender", user.getGender()
        );
    }

    // 🔹 Método de recuperación de contraseña con retorno correcto
    public Optional<Map<String, String>> recoverPassword(String email) {
        System.out.println("🔹 Solicitud de recuperación de contraseña para email: " + email);

        return userRepository.findByEmail(email)
            .map(user -> {
                System.out.println("✅ Contraseña recuperada: " + user.getPassword());
                return Map.of("password", user.getPassword()); // ✅ Envía la contraseña real
            });
    }
}