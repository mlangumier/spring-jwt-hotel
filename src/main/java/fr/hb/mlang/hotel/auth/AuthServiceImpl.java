package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.business.RegistrationManager;
import fr.hb.mlang.hotel.auth.dto.AuthenticationResponse;
import fr.hb.mlang.hotel.auth.dto.AuthenticationRequest;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.email.EmailService;
import fr.hb.mlang.hotel.security.JwtService;
import fr.hb.mlang.hotel.security.token.RefreshToken;
import fr.hb.mlang.hotel.security.token.RefreshTokenRepository;
import fr.hb.mlang.hotel.user.UserRepository;
import fr.hb.mlang.hotel.user.domain.User;
import jakarta.validation.ValidationException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;
  private final EmailService emailService;
  private final RegistrationManager registrationManager;

  @Override
  public void register(RegisterRequest request) {
    User user = registrationManager.createUser(request);

    String token = jwtService.generateVerificationToken(user);

    emailService.sendVerificationEmail(user, token);
  }

  @Override
  public AuthenticationResponse verifyAccount(String token) {
    String userEmail = jwtService.extractUsernameFromToken(token);

    registrationManager.verifyUser(userEmail);

    return AuthenticationResponse.builder().accessToken("").build();
  }

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
    ));

    User user = userRepository
        .findByEmail(request.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException(
            "Couldn't find user with email: " + request.getEmail()
        ));

    String accessToken = jwtService.generateAccessToken(user);

    RefreshToken refreshToken = RefreshToken
        .builder()
        .token(UUID.randomUUID().toString())
        .user(user)
        .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
        .build()
        ;
    refreshTokenRepository.save(refreshToken);

    return AuthenticationResponse
        .builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken.getId())
        .build();
  }

  public AuthenticationResponse refreshToken(String refreshTokenId) {
    final RefreshToken refreshToken = refreshTokenRepository
        .findById(refreshTokenId)
        .orElseThrow(() -> new ValidationException("Refresh token not found"));

    if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
      throw new ValidationException("Refresh token expired");
    }

    final String newAccessToken = jwtService.generateRefreshToken(refreshToken.getUser());

    return AuthenticationResponse
        .builder()
        .accessToken(newAccessToken)
        .refreshToken(refreshToken.getId())
        .build();
  }
}
