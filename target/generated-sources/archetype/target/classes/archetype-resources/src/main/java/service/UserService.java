#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import ${package}.model.User;
import ${package}.DTO.UserDTO;
import ${package}.enums.UserRole;
import ${package}.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDTO saveUser(UserDTO userDTO) {
        // Si no se especifica un rol, asignar USER por defecto
        if (userDTO.getRole() == null) {
            userDTO.setRole(UserRole.USER);
        }
    

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
    }

    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .build();
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public UserDTO findUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));
    return convertToDTO(user);
    }

    public void deleteUserById(Long id) {
    if (!userRepository.existsById(id)) {
        throw new RuntimeException("Usuario con ID " + id + " no encontrado");
    }
    userRepository.deleteById(id);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado"));

    existingUser.setUsername(userDTO.getUsername());
    existingUser.setPassword(userDTO.getPassword());
    existingUser.setRole(userDTO.getRole());

    User updatedUser = userRepository.save(existingUser);
    return convertToDTO(updatedUser);
}

public String login(String username, String password) {
    Optional<User> user = findByUsername(username);

    if (user.isPresent() && user.get().getPassword().equals(password)) {
        UserRole role = user.get().getRole();

        switch (role) {
            case ADMIN:
                return "Inicio de sesión exitoso. Rol: ADMIN";
            case MODERATOR:
                return "Inicio de sesión exitoso. Rol: MODERATOR";
            case USER:
                return "Inicio de sesión exitoso. Rol: USER";
            default:
                throw new RuntimeException("Rol desconocido");
        }
    } else {
        throw new RuntimeException("Credenciales inválidas");
    }
}



}