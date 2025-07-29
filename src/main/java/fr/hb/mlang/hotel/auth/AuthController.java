package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.JwtTokensDto;
import fr.hb.mlang.hotel.auth.dto.LoginRequest;
import fr.hb.mlang.hotel.auth.dto.LoginResponse;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.security.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthServiceImpl authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
    authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
  }

  @GetMapping("/verify")
  public ResponseEntity<String> verify(@RequestParam("token") String token) {
    authService.verifyAccount(token);
    return ResponseEntity.ok("User verified successfully!");
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody LoginRequest request,
      HttpServletResponse response
  ) {
    JwtTokensDto tokens = authService.authenticate(request, response);

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        CookieUtil.createRefreshTokenCookie(tokens.getRefreshToken()).toString()
    );

    return ResponseEntity.ok(LoginResponse.builder().accessToken(tokens.getAccessToken()).build());
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    JwtTokensDto tokens = authService.refreshToken(request);

    response.addHeader(
        HttpHeaders.SET_COOKIE,
        CookieUtil.createRefreshTokenCookie(tokens.getRefreshToken()).toString()
    );

    return ResponseEntity.ok(LoginResponse.builder().accessToken(tokens.getAccessToken()).build());
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
    authService.logout(request, response);
    return ResponseEntity.noContent().build();
  }
}
