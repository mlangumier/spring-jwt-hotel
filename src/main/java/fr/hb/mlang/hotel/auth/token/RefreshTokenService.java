package fr.hb.mlang.hotel.auth.token;

import fr.hb.mlang.hotel.user.domain.User;

public interface RefreshTokenService {

  /**
   * Generates a new <code>refresh token</code> using the {@link User}'s ID.
   *
   * @param userIdentifier UUID identifier of the user
   * @return a new refresh token
   */
  String generateToken(String userIdentifier);
}
