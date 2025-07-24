package fr.hb.mlang.hotel.auth.business;

import fr.hb.mlang.hotel.auth.dto.LoginRequestDTO;
import fr.hb.mlang.hotel.user.domain.User;
import fr.hb.mlang.hotel.user.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginManager {

  private final AuthenticationManager authenticationManager;

  /**
   * Authenticates the {@link User} and returns their full {@link CustomUserDetails} data.
   *
   * @param credentials Credentials used to authenticate the user
   * @return the information of the authenticated user
   */
  public CustomUserDetails authenticateUser(LoginRequestDTO credentials) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        credentials.getEmail(),
        credentials.getPassword()
    ));

    return (CustomUserDetails) authentication.getPrincipal();
  }
}
