package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthenticationRequest;
import fr.hb.mlang.hotel.auth.dto.AuthenticationResponse;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.user.domain.User;

public interface AuthService {

  void register(RegisterRequest request);

  //TODO: requires an update
  AuthenticationResponse verifyAccount(String verificationToken);

  /**
   * Authenticates the {@link User} after verifying their credentials.
   *
   * @param request The user's credentials
   * @return the generated access token
   */
  AuthenticationResponse authenticate(AuthenticationRequest request);

  //TokenPairDTO refreshToken(String refreshToken);

  //void resetPassword(String email);

  //void deleteAccount(User user);
}
