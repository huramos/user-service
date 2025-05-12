package com.foro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.foro.enums.UserRole;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 4, max = 30, message = "El nombre de usuario debe tener entre 4 y 30 caracteres")
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "Debe ingresar un correo válido")
    private String email;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, length = 10) // 🔹 Nuevo campo de género
    @NotBlank(message = "El género no puede estar vacío")
    private String gender;

    // 🔹 Constructor con asignación de género y rol correcta
    public User(String username, String email, String password, String role, String gender) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = UserRole.fromString(role); // ✅ Conversión segura del String a UserRole
        this.gender = gender;
    }
}