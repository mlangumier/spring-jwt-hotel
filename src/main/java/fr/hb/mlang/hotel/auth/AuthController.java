package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.dto.AuthMapper;
import fr.hb.mlang.hotel.auth.dto.RegisterCredentialsDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthServiceImpl authService;
  private final UserDetailsService userService;
  private final AuthMapper mapper;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public String register(@RequestBody @Valid RegisterCredentialsDTO credentialsDTO) {
    authService.register(mapper.convertToUser(credentialsDTO));

    return "Please, check your emails.";
  }

  @PostMapping("/validate")
  public String validate(@RequestBody String email) {
    //TODO:
//    UserDetails user = userService.loadUserByUsername(email);
//    authService.validateUser(user);

    return "Your account has been validated";
  }
}
