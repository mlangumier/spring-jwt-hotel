package fr.hb.mlang.hotel.security;

import fr.hb.mlang.hotel.auth.business.LogoutManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutManager logoutManager;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Prevents injections trying to steal cookies (no sessions)
        .csrf(AbstractHttpConfigurer::disable)
        // Manages routes authorizations
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            // Public routes (whitelisted)
            .requestMatchers("/api/v1/auth/**").permitAll()
            // Private routes (authenticated only)
            .anyRequest().authenticated())
        // Prevents Spring Security from managing sessions ()
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Set our jwtFilter to be the method that
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutManager)
            .logoutSuccessHandler((request, response, authentication) -> {
              response.setStatus(HttpServletResponse.SC_OK);
              response.getWriter().write("User logged out successfully!");
            })
            .clearAuthentication(true))
    ;

    return http.build();
  }
}
