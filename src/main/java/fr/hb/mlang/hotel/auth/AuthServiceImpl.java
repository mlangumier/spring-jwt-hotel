package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthResponseDTO;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.email.EmailDetails;
import fr.hb.mlang.hotel.email.EmailService;
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

  @Override
  public AuthResponseDTO register(RegisterRequestDTO request) {
    User user = registrationManager.prepareNewUser(request);
    userRepository.save(user);

    String verificationMailContent = emailService.getVerificationMailContent();
    emailService.sendEmail(new EmailDetails(user.getEmail(), "Welcome to JWT-Hotel", verificationMailContent));

    return new AuthResponseDTO("User created; confirmation email sent");
  }

  @Override
  public void validateUser(User user) {
    //TODO: Check validation email & update user.validated to true
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
