package fr.hb.mlang.hotel.security;

import fr.hb.mlang.hotel.security.token.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Prevents injections trying to steal cookies (no sessions)
        .csrf(AbstractHttpConfigurer::disable)
        // Manages routes authorizations
        .authorizeHttpRequests(auth -> auth
            // Public routes (whitelisted)
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/auth/verify").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/auth/forgot-password").permitAll()
            .requestMatchers(HttpMethod.PATCH, "/api/v1/auth/reset-password").permitAll()
            // Private routes (authenticated only)
            .anyRequest().authenticated())
        // Prevents Spring Security from managing sessions ()
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Set our jwtFilter to be the method that
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
    ;

    return http.build();
  }
}
