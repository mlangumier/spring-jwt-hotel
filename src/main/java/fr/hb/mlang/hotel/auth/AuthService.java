package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.JwtTokensDto;
import fr.hb.mlang.hotel.auth.dto.LoginRequest;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.security.token.RefreshToken;
import fr.hb.mlang.hotel.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

  /**
   * Registers the {@link User} using the <code>register form</code> and sends them the
   * <code>verification email</code>.
   *
   * @param request Data from the registration form
   */
  void register(RegisterRequest request);

  /**
   * Verifies the {@link User}'s account by checking if the code from their <code>verification
   * email</code> is valid.
   *
   * @param token Verification token to verify
   */
  void verifyAccount(String token);

  /**
   * Sends an email to the {@link User} to <code>reset</code> their password.
   *
   * @param email Email that will receive the token
   */
  void sendResetPasswordEmail(String email);

  /**
   * Updates the {@link User}'s password if the <code>token</code> from the email they received is
   * valid.
   *
   * @param token       Reset password token
   * @param newPassword New password the user will use to authenticate
   */
  void updatePassword(String token, String newPassword);

  /**
   * Authenticates the {@link User} by verifying their credentials and creating a cookie with
   * {@link RefreshToken} to keep them authenticated.
   *
   * @param request  User credentials
   * @param response Response with the cookie containing the refresh token.
   * @return the refresh token and access token.
   */
  JwtTokensDto authenticate(LoginRequest request, HttpServletResponse response);

  /**
   * Automatically refreshes the <code>access token</code> when expired if the refresh token is
   * still valid.
   *
   * @param request Object with the cookie containing the refresh token
   * @return the refresh token and access token.
   */
  JwtTokensDto refreshToken(HttpServletRequest request);

  /**
   * Logs the {@link User} out by removing their {@link RefreshToken} from the database and sending
   * a non-valid cookie in the response.
   *
   * @param request  Object containing the cookie to modify and refresh token to delete.
   * @param response Object that will set the new non-valid cookie.
   */
  void logout(HttpServletRequest request, HttpServletResponse response);
}
