package com.foro.repository;

import com.foro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔹 Búsqueda por nombre de usuario
    Optional<User> findByUsername(String username);

    // 🔹 Búsqueda por email
    Optional<User> findByEmail(String email);

    // 🔹 Verificación de existencia por email (más eficiente)
    boolean existsByEmail(String email);

    // 🔹 Verificación de existencia por username
    boolean existsByUsername(String username);

    // 🔹 Búsqueda combinada por email o username
    Optional<User> findByUsernameOrEmail(String username, String email);

    // 🔹 Método para actualizar email y género del usuario sin afectar otros campos
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email = :email, u.gender = :gender WHERE u.username = :username")
    void updateUserProfile(String username, String email, String gender);
}