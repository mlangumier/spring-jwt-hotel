package fr.hb.mlang.hotel.auth.business;

import fr.hb.mlang.hotel.security.JwtService;
import fr.hb.mlang.hotel.security.token.RefreshToken;
import fr.hb.mlang.hotel.security.token.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutManager implements LogoutHandler {

  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;

  @Override
  public void logout(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");
    final String accessToken;
    final String userEmail;
    System.err.println("> Authentication: " + authentication);

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      accessToken = authHeader.substring("Bearer ".length());

      try {
        userEmail = jwtService.extractUsernameFromToken(accessToken);

        List<RefreshToken> refreshTokens = refreshTokenRepository.findAllByUserEmail(userEmail);
        refreshTokenRepository.deleteAll(refreshTokens);
      } catch (Exception e) {
        System.err.println("Failed to revoke refresh tokens: " + e.getMessage());
      }
    }

    SecurityContextHolder.clearContext();
  }
}
