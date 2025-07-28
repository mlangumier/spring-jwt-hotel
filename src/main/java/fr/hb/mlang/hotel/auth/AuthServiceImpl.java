package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.business.RegistrationManager;
import fr.hb.mlang.hotel.auth.dto.AuthenticationResponse;
import fr.hb.mlang.hotel.auth.dto.AuthenticationRequest;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.email.EmailService;
import fr.hb.mlang.hotel.security.JwtService;
import fr.hb.mlang.hotel.user.UserRepository;
import fr.hb.mlang.hotel.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtService jwtService;
  private final AuthenticationManager authManager;
  private final UserRepository userRepository;
  private final EmailService emailService;
  private final RegistrationManager registrationManager;

  @Override
  public AuthenticationResponse register(RegisterRequest request) {
    User user = registrationManager.createUser(request);

    //TODO: Set validation token duration to 30 days
    String token = jwtService.generateToken(user);

    emailService.sendVerificationEmail(user, token);

    return AuthenticationResponse.builder().token(token).build();
  }

  @Override
  public AuthenticationResponse verifyAccount(String token) {
    String userEmail = jwtService.extractUsername(token);

    registrationManager.verifyUser(userEmail);

    return AuthenticationResponse.builder().token("").build();
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

    //TODO: Set access token duration to 5-15 minutes
    String accessToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder().token(accessToken).build();
  }
}
