package fr.hb.mlang.hotel.email;

import fr.hb.mlang.hotel.email.exception.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Value("${spring.mail.username}")
  private String sender;

  @Override
  public void sendEmail(EmailDetails email) {
    MimeMessage message = mailSender.createMimeMessage();

    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(sender);
      helper.setTo(email.getRecipient());
      helper.setSubject(email.getSubject());
      helper.setText(email.getMessageBody(), true);

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new EmailSendingException("Email couldn't be sent to " + email.getRecipient(), e);
    }
  }

  @Override
  public String getVerificationMailContent(String jwtToken) {
    Context context = new Context();

    String serverUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
    String verificationUrl = serverUrl + "/api/v1/auth/verify/" + jwtToken;

    context.setVariable("verificationUrl", verificationUrl);
    return templateEngine.process("user-verify", context);
  }
}
