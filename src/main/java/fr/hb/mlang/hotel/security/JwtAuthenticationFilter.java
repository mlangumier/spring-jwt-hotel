package fr.hb.mlang.hotel.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Interceptor that applies filters to our HTTP requests. Reads the <code>request</code> and manages
 * the <code>response</code>.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    final String authHeader = request.getHeader("Authorization");
    final String accessToken;
    final String userEmail;

    System.err.println("> Enter Authentication Filter");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      System.err.println("> Not authentication token provided");
      filterChain.doFilter(request, response);
      return;
    }

    accessToken = authHeader.substring("Bearer ".length());
    userEmail = jwtService.extractUsernameFromToken(accessToken);

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      System.err.println("> User found & not authenticated");
      UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

      if (jwtService.isTokenValid(accessToken, userDetails)) {
        System.err.println("> Access token valid for found user");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );


        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.err.println("> User authenticated successfully");
      }
    }

    filterChain.doFilter(request, response);
    System.err.println("> Exit authentication filter");
  }
}
