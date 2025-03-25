package com.foro.model;

import jakarta.persistence.*;
import com.foro.enums.UserRole;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Constructor vacío requerido por Hibernate
    public User() {}

    // Constructor privado utilizado por el Builder
    private User(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
    }

    // Método estático para iniciar la construcción
    public static Builder builder() {
        return new Builder();
    }

    // Clase estática Builder
    public static class Builder {
        private Long id;
        private String username;
        private String password;
        private UserRole role;

        // Métodos 'setter' que retornan la instancia del Builder
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        // Método para construir la instancia de User
        public User build() {
            return new User(this);
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    // Setters para permitir la edición de usuarios
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    // Métodos helper para verificar roles
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isModerator() {
        return role == UserRole.MODERATOR;
    }

    public boolean isUser() {
        return role == UserRole.USER;
    }

    public boolean canModerate() {
        return isAdmin() || isModerator();
    }
}