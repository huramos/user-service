package com.foro.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foro.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 4, max = 30, message = "El nombre de usuario debe tener entre 4 y 30 caracteres") // ✅ Se agregó longitud máxima
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El nombre de usuario solo puede contener letras, números y guion bajo") // ✅ Restricción adicional
    private String username;

    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "Debe ingresar un correo válido")
    @Size(max = 50, message = "El correo electrónico debe tener un máximo de 50 caracteres") // ✅ Se agregó límite de tamaño
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @JsonIgnore // ✅ Evita que la contraseña se envíe en respuestas JSON
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres") // ✅ Se agregó límite máximo
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    @NotNull(message = "El rol no puede ser nulo")
    private UserRole role;

    @NotBlank(message = "El género no puede estar vacío") // ✅ Se agregó validación para género
    @Pattern(regexp = "^(male|female|other)$", message = "El género debe ser 'male', 'female' u 'other'") // ✅ Restricción en formato
    private String gender;

    // 🔹 Constructor adicional para llamadas sin contraseña
    public UserDTO(Long id, String username, String email, UserRole role, String gender) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.gender = gender;
    }
}