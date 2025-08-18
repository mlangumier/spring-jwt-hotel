package fr.hb.mlang.hotel.security;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

  private CookieUtil() {
    // Required by JPA (doing this prevents auto-instanciation)
  }

  /**
   * Creates a cookie containing the refresh token (expiration of 7 days) to be sent to the user's
   * device so they can stay logged in to the app.
   *
   * @param token Refresh token
   * @return the generated cookie.
   */
  public static ResponseCookie createRefreshTokenCookie(String token) {
    long tokenExpiration = 60L * 15 * 24 * 7; // 7 days

    return ResponseCookie
        .from("refreshToken", token)
        .httpOnly(true)
        .secure(false) // {true} for production & HTTPS setup
        .path("/api/v1/auth")
        .sameSite(SameSiteCookies.NONE.toString())
        .maxAge(tokenExpiration)
        .build();
  }

  /**
   * Replaces the user's valid cookie with an empty one, effectively "logging out" the user and
   * preventing them from accessing authenticated routes until they log in again.
   *
   * @return an "empty" cookie.
   */
  public static ResponseCookie cleanRefreshTokenCookie() {
    return ResponseCookie
        .from("refreshToken", "")
        .httpOnly(true)
        .secure(false) // {true} for production & HTTPS setup
        .path("/api/v1/auth")
        .sameSite(SameSiteCookies.NONE.toString())
        .maxAge(0)
        .build();
  }
}
