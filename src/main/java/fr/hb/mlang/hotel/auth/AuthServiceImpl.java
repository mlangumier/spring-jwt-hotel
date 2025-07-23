package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.business.RegistrationManager;
import fr.hb.mlang.hotel.auth.business.VerificationManager;
import fr.hb.mlang.hotel.auth.dto.AuthResponseDTO;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.email.EmailDetails;
import fr.hb.mlang.hotel.email.EmailService;
import fr.hb.mlang.hotel.security.CustomUserDetails;
import fr.hb.mlang.hotel.security.JwtProvider;
import fr.hb.mlang.hotel.user.User;
import fr.hb.mlang.hotel.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final EmailService emailService;
  private final RegistrationManager registrationManager;
  private final VerificationManager verificationManager;
  private final JwtProvider jwtProvider;

  @Override
  public AuthResponseDTO register(RegisterRequestDTO request) {
    User user = registrationManager.prepareNewUser(request);
    userRepository.save(user);

    String token = jwtProvider.generateToken(user.getEmail());
    String verificationMailContent = emailService.getVerificationMailContent(token);
    emailService.sendEmail(new EmailDetails(user.getEmail(), "Welcome to JWT-Hotel", verificationMailContent));

    return new AuthResponseDTO("User created; confirmation email sent");
  }

  @Override
  public AuthResponseDTO verifyAccount(String token) {
    CustomUserDetails user = (CustomUserDetails) jwtProvider.validateToken(token);

    User verifiedUser = verificationManager.verifyUser(user.getUsername());
    userRepository.save(verifiedUser);

    return new AuthResponseDTO("Account successfully verified!");
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
