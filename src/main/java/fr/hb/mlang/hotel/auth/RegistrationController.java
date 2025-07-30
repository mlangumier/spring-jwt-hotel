package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.ForgotPasswordRequest;
import fr.hb.mlang.hotel.auth.dto.RegisterRequest;
import fr.hb.mlang.hotel.auth.dto.UserResetPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RegistrationController {

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

  @PostMapping("/forgot-password")
  public ResponseEntity<String> sentResetPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    authService.sendResetPasswordEmail(request.email());
    return ResponseEntity.ok("Reset password email sent!");
  }

  @PatchMapping("/reset-password")
  public ResponseEntity<String> updatePassword(@RequestParam("token") String token, @Valid @RequestBody UserResetPasswordRequest request) {
    authService.updatePassword(token, request.password());
    return ResponseEntity.ok("Password changed successfully! You can now log in to your account!");
  }
}
