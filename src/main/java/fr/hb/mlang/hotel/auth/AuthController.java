package fr.hb.mlang.hotel.auth;

import fr.hb.mlang.hotel.auth.email.EmailDetails;
import fr.hb.mlang.hotel.auth.email.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

  private final EmailService emailService;

  @GetMapping("/validate")
  public void validateEmail() {
    EmailDetails email = new EmailDetails("vevewe1937@dosonex.com", "First test email", "Hello, there!", null);

    emailService.sendEmail(email);
  }
}
