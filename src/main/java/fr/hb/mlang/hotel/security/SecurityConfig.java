package fr.hb.mlang.hotel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain accessControl(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable) // Prevent injections trying to steal cookies
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            .anyRequest().permitAll()) // Open all routes to public (for now)
        .sessionManagement(session -> session
            .sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)); // Prevents Spring Security from managing sessions (we'll be doing it ourselves with JWT)

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
