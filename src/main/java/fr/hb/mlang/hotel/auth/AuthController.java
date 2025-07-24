package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthResponseDTO;
import fr.hb.mlang.hotel.auth.dto.LoginRequestDTO;
import fr.hb.mlang.hotel.auth.dto.LoginResponseDTO;
import fr.hb.mlang.hotel.auth.dto.RegisterRequestDTO;
import fr.hb.mlang.hotel.user.domain.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
    AuthResponseDTO response = authService.register(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/verify")
  public ResponseEntity<AuthResponseDTO> verify(@RequestParam("token") String token) {
    AuthResponseDTO response = authService.verifyAccount(token);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO credentials) {
    LoginResponseDTO response = authService.login(credentials);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }

//  @PostMapping("/logout")
//  public ResponseEntity<AuthResponseDTO> logout(@RequestParam("token") String token) {
//    AuthResponseDTO response = authService.logout(token);
//    return ResponseEntity.status(HttpStatus.OK).body(response);
//  }

  @GetMapping("/protected")
  public ResponseEntity<AuthResponseDTO> checkProtected(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(new AuthResponseDTO(
            "Access granted for logged in user: " + userDetails.getUsername()));
  }
}
