package com.foro.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Habilita CORS para que se apliquen las reglas definidas en CorsConfig si las tienes,
            // o se usarán configuraciones por defecto:
            .cors().and()
            // Deshabilita CSRF porque generalmente cuando se usan APIs REST se utiliza otro mecanismo de seguridad:
            .csrf(csrf -> csrf.disable())
            // Configura las reglas de autorización
            .authorizeHttpRequests(auth -> auth
                // Permite todas las solicitudes OPTIONS para que el preflight request no sea bloqueado
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Permite acceso público a los endpoints de autenticación (login, recover-password, etc.)
                .requestMatchers("/auth/**").permitAll()
                // Permite métodos PUT para actualizar perfil de usuario
                .requestMatchers(HttpMethod.PUT, "/auth/user-profile").authenticated() // ✅ Se agrega autorización para PUT
                // Establece restricciones para otros endpoints según roles:
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/moderator/**").hasAnyRole("ADMIN", "MODERATOR")
                .requestMatchers("/user/**").hasAnyRole("ADMIN", "MODERATOR", "USER")
                // El resto de solicitudes requieren autenticación
                .anyRequest().authenticated()
            )
            // Configura el login por formulario (puedes personalizar la página de login si lo requieres)
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            // Permite el logout a todos
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}