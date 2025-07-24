package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.business.RegistrationManager;
import fr.hb.mlang.hotel.auth.dto.AuthResponseDTO;
import fr.hb.mlang.hotel.auth.dto.LoginRequestDTO;
import fr.hb.mlang.hotel.auth.dto.LoginResponseDTO;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.email.EmailDetails;
import fr.hb.mlang.hotel.email.EmailService;
import fr.hb.mlang.hotel.user.domain.CustomUserDetails;
import fr.hb.mlang.hotel.security.jwt.JwtProvider;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final EmailService emailService;
  private final RegistrationManager registrationManager;
  private final JwtProvider jwtProvider;
  private final AuthenticationManager authManager;

  @Override
  public AuthResponseDTO register(RegisterRequestDTO request) {
    User user = registrationManager.prepareNewUser(request);
    userRepository.save(user);

    // Set token duration to 7 days
    String token = jwtProvider.generateToken(
        user.getEmail(),
        Instant.now().plus(7, ChronoUnit.DAYS)
    );

    String verificationMailContent = emailService.getVerificationMailContent(token);
    emailService.sendEmail(new EmailDetails(
        user.getEmail(),
        "Welcome to JWT-Hotel",
        verificationMailContent
    ));

    return new AuthResponseDTO("User created; confirmation email sent");
  }

  @Override
  public AuthResponseDTO verifyAccount(String token) {
    CustomUserDetails user = (CustomUserDetails) jwtProvider.validateToken(token);

    User verifiedUser = registrationManager.verifyUser(user.getUsername());
    userRepository.save(verifiedUser);

    return new AuthResponseDTO("Account successfully verified!");
  }

  @Override
  public LoginResponseDTO login(LoginRequestDTO credentials) {
    CustomUserDetails userDetails = (CustomUserDetails) authManager
        .authenticate(new UsernamePasswordAuthenticationToken(
            credentials.getEmail(),
            credentials.getPassword()
        ))
        .getPrincipal();

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
