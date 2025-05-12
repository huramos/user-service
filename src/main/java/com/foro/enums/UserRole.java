package com.foro.enums;

public enum UserRole {
    ADMIN,     // Puede hacer todo
    MODERATOR, // Puede moderar posts y comentarios, pero no administrar usuarios
    USER;      // Solo puede crear/editar sus propios posts y comentarios

    // 🔹 Método para verificar si un rol existe
    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no válido: " + role);
        }
    }
}