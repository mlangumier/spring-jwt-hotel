package fr.hb.mlang.hotel.email;

import fr.hb.mlang.hotel.email.exception.EmailSendingException;
import fr.hb.mlang.hotel.user.domain.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  //TODO: refactor for "SOLID"

  @Value("${app.url.base}")
  private String baseUrl;

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
      helper.setTo(email.getRecipient().getEmail());
      helper.setSubject(email.getSubject());
      helper.setText(email.getMessageBody(), true);

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new EmailSendingException("Email couldn't be sent to " + email.getRecipient(), e);
    }
  }

  @Override
  public void sendVerificationEmail(User user, String token) {
    String verificationMailContent = this.prepareVerificationEmailContent(token);

    EmailDetails emailDetails = new EmailDetails(user,"Welcome to JWT-Hotel", verificationMailContent);

    this.sendEmail(emailDetails);
  }

  @Override
  public String prepareVerificationEmailContent(String token) {
    Context context = new Context();

    String verificationUrl = baseUrl + "/verify?token=" + token;

    context.setVariable("verificationUrl", verificationUrl);
    return templateEngine.process("user-verify", context);
  }

  @Override
  public void sendResetPasswordEmail(User user, String token) {
    String resetPasswordMailContent = this.prepareResetPasswordEmailContent(token);

    EmailDetails emailDetails = new EmailDetails(user,"JWT Hotel - Reset Password", resetPasswordMailContent);

    this.sendEmail(emailDetails);
  }

  @Override
  public String prepareResetPasswordEmailContent(String token) {
    Context context = new Context();

    String resetPasswordUrl = baseUrl + "/reset-password?token=" + token;

    context.setVariable("resetPasswordUrl", resetPasswordUrl);
    return templateEngine.process("reset-password", context);
  }
}
