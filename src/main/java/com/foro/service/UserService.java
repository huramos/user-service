package com.foro.service;

import com.foro.model.User;
import com.foro.DTO.UserDTO;
import com.foro.enums.UserRole;
import com.foro.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDTO saveUser(UserDTO userDTO) {
        if (userDTO.getRole() == null) {
            userDTO.setRole(UserRole.USER);
        }

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent() ||
            userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public Map<String, Object> login(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseGet(() -> userRepository.findByEmail(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")));

        if (!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Inicio de sesión exitoso");
        response.put("role", user.getRole().name());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("gender", user.getGender()); // ✅ Se incluye género en la respuesta

        return response;
    }

    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setRole(userDTO.getRole());
            user.setGender(userDTO.getGender()); // ✅ Se actualiza el género

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(userDTO.getPassword());
            }

            userRepository.save(user);
            return convertToDTO(user);
        });
    }

    // 🔹 Nuevo método para actualizar el perfil del usuario logueado
    @Transactional
    public Optional<UserDTO> updateUserProfile(String username, UserDTO userDTO) {
        return userRepository.findByUsername(username).map(user -> {
            user.setEmail(userDTO.getEmail());
            user.setGender(userDTO.getGender()); // ✅ Permite la actualización del género sin alterar el username

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(userDTO.getPassword());
            }

            userRepository.save(user);
            return convertToDTO(user);
        });
    }

    // 🔹 Método para obtener el perfil del usuario logueado
    public Optional<UserDTO> getUserProfile(String username) {
        return userRepository.findByUsername(username).map(this::convertToDTO);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getGender()); // ✅ Se incluye género
    }

    private User convertToEntity(UserDTO userDTO) {
        return new User(
            userDTO.getId(),
            userDTO.getUsername(),
            userDTO.getEmail(),
            userDTO.getPassword(),
            userDTO.getRole(),
            userDTO.getGender() // ✅ Se asigna género
        );
    }
}