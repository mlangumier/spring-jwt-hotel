package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.business.LoginManager;
import fr.hb.mlang.hotel.auth.business.RegistrationManager;
import fr.hb.mlang.hotel.auth.dto.AuthResponseDTO;
import fr.hb.mlang.hotel.auth.dto.LoginRequestDTO;
import fr.hb.mlang.hotel.auth.dto.LoginResponseDTO;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.email.EmailService;
import fr.hb.mlang.hotel.security.jwt.JwtProvider;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.security.CustomUserDetails;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final EmailService emailService;
  private final RegistrationManager registrationManager;
  private final LoginManager loginManager;
  private final JwtProvider jwtProvider;

  @Override
  public AuthResponseDTO register(RegisterRequestDTO request) {
    User user = registrationManager.createUser(request);

    // Set token duration to 7 days
    String token = jwtProvider.generateToken(user.getEmail(), Instant.now().plus(7, ChronoUnit.DAYS));

    emailService.sendVerificationEmail(user, token);

    return new AuthResponseDTO("User created; confirmation email sent");
  }

  @Override
  public AuthResponseDTO verifyAccount(String token) {
    String userEmail = jwtProvider.extractEmail(token);

    registrationManager.verifyUser(userEmail);

    return new AuthResponseDTO("Account successfully verified!");
  }

  @Override
  public LoginResponseDTO login(LoginRequestDTO credentials) {
    CustomUserDetails userDetails = loginManager.authenticateUser(credentials);
    String token = jwtProvider.generateToken(userDetails.getUsername());

    return new LoginResponseDTO(token, userDetails);
  }

  @Override
  public void resetPassword(String email) {
    //TODO:
  }

  @Override
  public void deleteAccount(User user) {
    //TODO
  }
}
