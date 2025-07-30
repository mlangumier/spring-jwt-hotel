package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.business.RegistrationManager;
import fr.hb.mlang.hotel.auth.dto.JwtTokensDto;
import fr.hb.mlang.hotel.auth.dto.LoginRequest;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.email.EmailService;
import fr.hb.mlang.hotel.security.CookieUtil;
import fr.hb.mlang.hotel.security.token.JwtService;
import fr.hb.mlang.hotel.security.token.RefreshToken;
import fr.hb.mlang.hotel.security.token.RefreshTokenRepository;
import fr.hb.mlang.hotel.user.UserService;
import fr.hb.mlang.hotel.user.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  @Value("${app.jwt.refresh}")
  private String jwtRefreshTokenName;

  private final UserService userService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;
  private final EmailService emailService;
  private final RegistrationManager registrationManager;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void register(RegisterRequest request) {
    User user = registrationManager.createUser(request);

    String token = jwtService.generateVerificationToken(user);

    emailService.sendVerificationEmail(user, token);
  }

  public void verifyAccount(String token) {
    String userEmail = jwtService.extractUsernameFromToken(token);

    registrationManager.verifyUser(userEmail);
  }

  public void sendResetPasswordEmail(String email) {
    User user = (User) userService.loadUserByUsername(email);

    String token = jwtService.generateResetPasswordToken(user);

    emailService.sendResetPasswordEmail(user, token);
  }

  public void updatePassword(String token, String newPassword) {
    String userEmail = jwtService.extractUsernameFromToken(token);

    User user = (User) userService.loadUserByUsername(userEmail);

    String hashedPassword = passwordEncoder.encode(newPassword);
    user.setPassword(hashedPassword);

    userService.updateUser(user);
  }

  public JwtTokensDto authenticate(LoginRequest request, HttpServletResponse response) {
    Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
    ));
    User user = (User) authentication.getPrincipal();

    String accessToken = jwtService.generateAccessToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    RefreshToken refreshTokenEntity = RefreshToken
        .builder()
        .token(refreshToken)
        .user(user)
        .expiresAt(Instant.now().plusMillis(jwtService.getRefreshTokenExpiration()))
        .build()
        ;
    refreshTokenRepository.save(refreshTokenEntity);

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        CookieUtil.createRefreshTokenCookie(refreshToken).toString()
    );

    return JwtTokensDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }

  public JwtTokensDto refreshToken(HttpServletRequest request) {
    String refreshToken = Arrays
        .stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
        .filter(cookie -> cookie.getName().equals(jwtRefreshTokenName))
        .map(Cookie::getValue)
        .findFirst()
        .orElse(null)
        ;

    if (refreshToken == null || refreshToken.isBlank()) {
      throw new ValidationException("Refresh token cookie is missing");
    }

    // Validate token structure
    String userEmail = jwtService.extractUsernameFromToken(refreshToken);
    UserDetails userDetails = userService.loadUserByUsername(userEmail);

    // Check if corresponds to a persisted entity
    if (!jwtService.isTokenValid(refreshToken, userDetails)) {
      throw new ValidationException("Invalid refresh token");
    }

    // Issue new access token
    String newAccessToken = jwtService.generateAccessToken(userDetails);

    return JwtTokensDto.builder().accessToken(newAccessToken).refreshToken(refreshToken).build();
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    Arrays
        .stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
        .filter(cookie -> cookie.getName().equals(jwtRefreshTokenName))
        .map(Cookie::getValue)
        .findFirst()
        .flatMap(refreshTokenRepository::findByToken)
        .ifPresent(refreshTokenRepository::delete)
    ;

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        CookieUtil.cleanRefreshTokenCookie().toString()
    );
  }
}
