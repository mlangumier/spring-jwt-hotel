package fr.hb.mlang.hotel.security;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

  private CookieUtil() {
    // Required but not needed
  }

  public static ResponseCookie createRefreshTokenCookie(String token) {
    long  tokenExpiration = 60L * 15 * 24 * 7; // 7 days

    return ResponseCookie
        .from("refreshToken", token)
        .httpOnly(true)
        .secure(false) // {true} for production & HTTPS setup
        .path("/api/v1/auth")
        .sameSite(SameSiteCookies.NONE.toString())
        .maxAge(tokenExpiration)
        .build();
  }

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
